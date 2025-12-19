package com.Nhom7.DACN_KTPM.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Tên thư mục lưu trữ (Nằm tại thư mục gốc của Project)
    private final Path rootLocation = Paths.get("uploads");

    public FileStorageService() {
        try {
            // Tạo thư mục 'uploads' nếu chưa tồn tại
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Không thể khởi tạo thư mục lưu trữ (uploads).", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            // 1. Kiểm tra file rỗng
            if (file.isEmpty()) {
                throw new RuntimeException("Không thể lưu file rỗng.");
            }

            // 2. Lấy đuôi file gốc (ví dụ: .jpg, .png)
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 3. Tạo tên file mới ngẫu nhiên (UUID) để tránh trùng lặp
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // 4. Xác định đường dẫn lưu file đích
            Path destinationFile = this.rootLocation.resolve(Paths.get(newFileName))
                    .normalize().toAbsolutePath();

            // Kiểm tra bảo mật: Đảm bảo file không bị lưu ra ngoài thư mục uploads
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Không thể lưu file ra ngoài thư mục hiện tại.");
            }

            // 5. Copy file vào thư mục đích (Ghi đè nếu tồn tại)
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // 6. Trả về đường dẫn Web (URL) để Frontend truy cập
            // Ví dụ: /images/550e8400-e29b-41d4-a716-446655440000.png
            return "/images/" + newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Thất bại khi lưu file.", e);
        }
    }
}