🚗 VinAuto - Hệ thống Dự toán Trả góp & Đăng ký Lái thử VinFast

📖 Giới thiệu (Introduction)

VinAuto là một ứng dụng web Full-stack được xây dựng theo kiến trúc hướng dịch vụ (SOA), cung cấp giải pháp toàn diện cho khách hàng quan tâm đến xe VinFast. Hệ thống giúp người dùng tra cứu thông tin xe, tính toán chi phí trả góp chi tiết theo thời gian thực và đặt lịch hẹn tư vấn/lái thử tại showroom gần nhất.

Dự án áp dụng mô hình O2O (Online-to-Offline), kết nối trải nghiệm tìm hiểu trực tuyến với dịch vụ thực tế tại Showroom.

🚀 Tính năng Chính (Key Features)

1. 🚘 Quản lý & Hiển thị Sản phẩm

Danh sách các dòng xe (Models) và phiên bản (Variants) chi tiết.

Hiển thị thông số kỹ thuật, hình ảnh và giá niêm yết/ưu đãi.

2. 💰 Dự toán Tài chính (Core Feature)

Tính toán khoản vay, lãi suất và số tiền trả trước.

Logic chuyên sâu: Tính lãi dựa trên dư nợ giảm dần.

Hỗ trợ các gói vay ưu đãi (Lãi suất cố định thời gian đầu, thả nổi thời gian sau).

Xuất bảng lịch trả nợ chi tiết (Amortization Schedule).

3. 📍 Tìm kiếm Showroom & Đặt lịch (O2O)

Tìm kiếm Showroom theo Tỉnh/Thành phố.

Kiểm tra lịch làm việc và khung giờ trống của Showroom.

Đăng ký lái thử kết hợp đặt lịch hẹn chính xác.

4. 📝 Quản lý Yêu cầu (Admin)

Tiếp nhận yêu cầu tư vấn từ khách hàng.

Theo dõi trạng thái xử lý (Mới, Đã liên hệ, Hoàn thành).

🏗️ Kiến trúc Hệ thống (Architecture)

Dự án được xây dựng theo mô hình Modular Monolith (một bước đệm tới Microservices), áp dụng triệt để các nguyên lý của SOA (Service-Oriented Architecture):

Service Provider (Backend): Cung cấp các API RESTful độc lập cho từng nghiệp vụ (Product, Finance, Showroom).

Service Consumer (Frontend): Ứng dụng VueJS tiêu thụ các API để hiển thị giao diện.

Layered Architecture: Controller ↔ Service ↔ Repository ↔ Database.

DTO Pattern: Tách biệt dữ liệu giao tiếp (Request/Response) và dữ liệu lưu trữ (Entity).

Sơ đồ luồng dữ liệu (Data Flow)

Client (VueJS) ➡️ Controller (API) ➡️ Service (Logic) ➡️ Repository (JPA) ➡️ MySQL

🛠️ Công nghệ sử dụng (Tech Stack)

Backend

Core: Java 17, Spring Boot 3.

Database: MySQL, Spring Data JPA (Hibernate).

Mapping: MapStruct.

Utils: Lombok, Validation API.

Build Tool: Maven.

Frontend

Framework: Vue.js 3 (Composition API, Script Setup).

Styling: Tailwind CSS.

HTTP Client: Axios.

Build Tool: Vite.

⚙️ Cài đặt & Chạy dự án (Installation)

Yêu cầu tiên quyết

JDK 17+

Node.js 18+

MySQL Server 8.0+

1. Thiết lập Database

Tạo database trống tên vinfast_installment_db.

Chạy script SQL trong thư mục database/script.sql (nếu có) hoặc để Hibernate tự động sinh bảng (ddl-auto: update).

2. Chạy Backend (Spring Boot)

cd backend
# Cấu hình file application.yml với thông tin DB của bạn
mvn spring-boot:run


Backend sẽ chạy tại: http://localhost:8080

3. Chạy Frontend (Vue.js)

cd frontend
npm install
npm run dev


Frontend sẽ chạy tại: http://localhost:5173 (hoặc port khác tùy cấu hình)


👥 Tác giả

Backend: Lê Xuân Kiên
Frontend: Nông Thế Hưng
Liên hệ: [lexuankien11082004@gmail.com]

Dự án này phục vụ mục đích học tập và nghiên cứu.
