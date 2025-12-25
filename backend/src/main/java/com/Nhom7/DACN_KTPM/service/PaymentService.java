package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.configuration.VnPayProperties;
import com.Nhom7.DACN_KTPM.configuration.VnPayUtil;
import com.Nhom7.DACN_KTPM.dto.request.PaymentCreationRequest;
import com.Nhom7.DACN_KTPM.dto.response.DepositResponse;
import com.Nhom7.DACN_KTPM.dto.response.PaymentResponse;
import com.Nhom7.DACN_KTPM.dto.response.VnPayResponse;
import com.Nhom7.DACN_KTPM.entity.*;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VnPayProperties vnpProperties;
    private final DepositRepository depositRepository;
    private final UserRepository userRepository;
    private final CarVariantRepository carVariantRepository;
    private final ShowroomRepository showroomRepository;

    // Inject thêm để set password mặc định cho user mới (nếu cần)
    private final PasswordEncoder passwordEncoder;

    @Transactional // Đảm bảo User và Deposit cùng thành công hoặc cùng thất bại
    public PaymentResponse createPayment(PaymentCreationRequest request, HttpServletRequest httpServletRequest) {
        if (request.getAmount() <= 0) throw new AppException(ErrorCode.INVALID_AMOUNT);

        // 1. TẠO USER MỚI (Dựa trên thông tin form)
        // Tách họ và tên (Ví dụ: "Nguyễn Văn A" -> Ho: "Nguyễn Văn", Ten: "A")
        String fullName = request.getFullName();
        String firstName = "";
        String lastName = "";
        if(fullName != null && !fullName.isBlank()){
            int lastSpaceIdx = fullName.lastIndexOf(" ");
            if(lastSpaceIdx != -1){
                firstName = fullName.substring(0, lastSpaceIdx);
                lastName = fullName.substring(lastSpaceIdx + 1);
            } else {
                lastName = fullName;
            }
        }

        User newUser = new User();

        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhone(request.getPhone());

        // Tạo username unique (Vì bảng User yêu cầu username unique)
        // Cách đơn giản: dùng SĐT + timestamp để tránh trùng lặp
        newUser.setUsername(request.getPhone() + "_" + System.currentTimeMillis());

        // Set mật khẩu mặc định (VD: 123456)
        newUser.setPassword(passwordEncoder.encode("123456"));
        newUser.setCustomerStatus("NEW"); // Khách hàng mới

        // Lưu User vào DB
        User savedUser = userRepository.save(newUser);


        // 2. TÌM CÁC THÔNG TIN XE VÀ SHOWROOM
        CarVariant variant = carVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

        Showroom showroom = showroomRepository.findById(request.getShowroomId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOWROOM_NOT_FOUND));


        // 3. TẠO DEPOSIT (Lưu thông tin User mới và thông tin Form)
        Deposit deposit = Deposit.builder()
                .amount(BigDecimal.valueOf(request.getAmount()))
                .user(savedUser)          // Link tới User vừa tạo

                // 👇 LƯU TRỰC TIẾP CÁC TRƯỜNG THÔNG TIN VÀO BẢNG DEPOSIT
                .customerName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .cccd(request.getCitizenId())

                .variant(variant)
                .showroom(showroom)
                .selectedColor(request.getSelectedColor())
                .paymentMethod("VNPAY")
                .status("PENDING")
                .note(request.getNote())
                .build();

        Deposit savedDeposit = depositRepository.save(deposit);

        // 4. TẠO URL VNPAY (Giữ nguyên logic cũ)
        String vnp_TxnRef = String.valueOf(savedDeposit.getId());
        long amount = request.getAmount() * 100;
        String vnp_IpAddr = VnPayUtil.getIpAddress(httpServletRequest);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpProperties.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", request.getOrderInfo());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpProperties.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (Exception e) {
                    throw new AppException(ErrorCode.PAYMENT_CREATION_FAILED);
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayUtil.hmacSHA512(vnpProperties.getHashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpProperties.getPayUrl() + "?" + queryUrl;

        return PaymentResponse.builder().paymentUrl(paymentUrl).build();
    }

    // 👇 CẬP NHẬT HÀM LẤY DANH SÁCH ĐỂ HIỂN THỊ ĐÚNG THÔNG TIN MỚI LƯU
    public List<DepositResponse> getAllDeposits() {
        return depositRepository.findAll().stream().map(deposit -> {

            // Ưu tiên lấy thông tin từ bảng Deposit (nếu có), nếu null thì mới lấy từ bảng User
            String finalName = deposit.getCustomerName();
            if (finalName == null && deposit.getUser() != null) {
                finalName = deposit.getUser().getLastName() + " " + deposit.getUser().getFirstName();
            }

            String finalPhone = deposit.getPhone();
            if (finalPhone == null && deposit.getUser() != null) {
                finalPhone = deposit.getUser().getPhone();
            }

            String carName = deposit.getVariant() != null ? deposit.getVariant().getName() : "Unknown Car";
            String showroomName = deposit.getShowroom() != null ? deposit.getShowroom().getName() : "";

            return DepositResponse.builder()
                    .id(deposit.getId())
                    .amount(deposit.getAmount())
                    .status(deposit.getStatus())
                    .paymentMethod(deposit.getPaymentMethod())

                    .userId(deposit.getUser() != null ? deposit.getUser().getId() : null)

                    // 👇 Trả về các trường thông tin chi tiết
                    .customerName(finalName)
                    .customerPhone(finalPhone)
                    .email(deposit.getEmail())       // Map email
                    .citizenId(deposit.getCccd())    // Cần thêm field này vào DepositResponse

                    .variantId(deposit.getVariant() != null ? deposit.getVariant().getId() : null)
                    .carName(carName)
                    .showroomId(deposit.getShowroom() != null ? deposit.getShowroom().getId() : null)
                    .showroomName(showroomName)
                    .selectedColor(deposit.getSelectedColor())
                    .note(deposit.getNote())
                    .createdAt(deposit.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    // ... Các hàm updateDepositStatus và handleVnPayCallback giữ nguyên
    public void updateDepositStatus(Long id, String status) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        deposit.setStatus(status);
        depositRepository.save(deposit);
    }

    public VnPayResponse handleVnPayCallback(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        if ("00".equals(status)) {
            Long depositId = Long.parseLong(vnp_TxnRef);
            Deposit deposit = depositRepository.findById(depositId)
                    .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

            if ("PENDING".equals(deposit.getStatus())) {
                deposit.setStatus("PAID");
                deposit.setPaymentMethod("VNPAY");
                depositRepository.save(deposit);
            }
            return VnPayResponse.builder().code("00").message("Success").build();
        } else {
            return VnPayResponse.builder().code("99").message("Failed").build();
        }
    }
}