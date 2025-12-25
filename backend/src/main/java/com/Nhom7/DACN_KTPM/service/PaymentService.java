package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.configuration.VnPayProperties;
import com.Nhom7.DACN_KTPM.configuration.VnPayUtil;
import com.Nhom7.DACN_KTPM.dto.request.PaymentCreationRequest;
import com.Nhom7.DACN_KTPM.dto.response.DepositResponse; // Nhớ import
import com.Nhom7.DACN_KTPM.dto.response.PaymentResponse;
import com.Nhom7.DACN_KTPM.dto.response.VnPayResponse;
import com.Nhom7.DACN_KTPM.entity.CarVariant;
import com.Nhom7.DACN_KTPM.entity.Deposit;
import com.Nhom7.DACN_KTPM.entity.Showroom;
import com.Nhom7.DACN_KTPM.entity.User;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.repository.CarVariantRepository;
import com.Nhom7.DACN_KTPM.repository.DepositRepository;
import com.Nhom7.DACN_KTPM.repository.ShowroomRepository;
import com.Nhom7.DACN_KTPM.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // 👇 INJECT THÊM CÁC REPOSITORY NÀY
    private final UserRepository userRepository;
    private final CarVariantRepository carVariantRepository;
    private final ShowroomRepository showroomRepository;

    public PaymentResponse createPayment(PaymentCreationRequest request, HttpServletRequest httpServletRequest) {
        if (request.getAmount() <= 0) throw new AppException(ErrorCode.INVALID_AMOUNT);

        // 1. Tìm các Entity liên quan từ ID trong request
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        CarVariant variant = carVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

        Showroom showroom = showroomRepository.findById(request.getShowroomId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOWROOM_NOT_FOUND));

        // 2. Lưu Deposit với các mối quan hệ
        Deposit deposit = Deposit.builder()
                .amount(BigDecimal.valueOf(request.getAmount()))
                .user(user)          // Set Object User
                .variant(variant)    // Set Object Variant
                .showroom(showroom)  // Set Object Showroom
                .selectedColor(request.getSelectedColor())
                .paymentMethod("VNPAY")
                .status("PENDING")
                .note(request.getNote())
                .build();

        Deposit savedDeposit = depositRepository.save(deposit);

        // ... (Phần code tạo URL VNPAY bên dưới giữ nguyên) ...
        // Lưu ý: Đoạn lấy vnp_TxnRef vẫn là savedDeposit.getId()

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

    // 👇 CẬP NHẬT HÀM NÀY ĐỂ LẤY TÊN
    public List<DepositResponse> getAllDeposits() {
        return depositRepository.findAll().stream().map(deposit -> {
            // Lấy thông tin từ các quan hệ
            String custName = deposit.getUser() != null ? (deposit.getUser().getLastName() + " " + deposit.getUser().getFirstName()) : "N/A";
            String custPhone = deposit.getUser() != null ? deposit.getUser().getPhone() : "";
            String carName = deposit.getVariant() != null ? deposit.getVariant().getName() : "Unknown Car";
            String showroomName = deposit.getShowroom() != null ? deposit.getShowroom().getName() : "";

            return DepositResponse.builder()
                    .id(deposit.getId())
                    .amount(deposit.getAmount())
                    .status(deposit.getStatus())
                    .paymentMethod(deposit.getPaymentMethod())

                    // Map thông tin chi tiết
                    .userId(deposit.getUser() != null ? deposit.getUser().getId() : null)
                    .customerName(custName)
                    .customerPhone(custPhone)

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

    // ... (Các hàm khác giữ nguyên) ...
    public void updateDepositStatus(Long id, String status) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        deposit.setStatus(status);
        depositRepository.save(deposit);
    }

    public VnPayResponse handleVnPayCallback(HttpServletRequest request) {
        // ... (Giữ nguyên logic cũ, chỉ lưu ý entity đã đổi getter) ...
        // Vì entity dùng Object, nhưng khi getById vẫn trả về Deposit đúng.
        // Chỉ lưu ý nếu bạn có dùng deposit.getUserId() ở đây thì đổi thành deposit.getUser().getId()

        // Code cũ của hàm này vẫn chạy tốt vì ta chỉ setStatus và save, không đụng vào User/Variant.
        // Copy lại code hàm này từ bài trước nếu cần.
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