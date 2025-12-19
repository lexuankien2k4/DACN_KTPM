import axios from 'axios';

// 1. Tạo instance
const api = axios.create({
    // Vì vite.config.mjs đã có proxy '/api' -> 'http://localhost:8080'
    // Nên ở đây ta không cần điền localhost:8080 nữa.
    // Chỉ cần để trống hoặc '/', trình duyệt sẽ gửi về localhost:3000, và Vite sẽ proxy đi.
    baseURL: '', 
    headers: {
        'Content-Type': 'application/json',
    },
});

// 2. Interceptor REQUEST: Tự động gắn Token trước khi gửi đi
api.interceptors.request.use(
    (config) => {
        // Lấy token từ bộ nhớ trình duyệt
        const token = localStorage.getItem('authToken');
        if (token) {
            // Gắn vào Header: Authorization: Bearer <token>
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 3. Interceptor RESPONSE: Tự động xử lý khi Token hết hạn
api.interceptors.response.use(
    (response) => {
        return response; // Trả về dữ liệu nếu thành công
    },
    (error) => {
        // Nếu Backend trả về lỗi 401 (Unauthorized) -> Token sai hoặc hết hạn
        if (error.response && error.response.status === 401) {
            // Xóa token cũ
            localStorage.removeItem('authToken');
            
            // Chuyển hướng về trang đăng nhập
            // (Dùng window.location để force reload cho sạch state)
            window.location.href = '/login'; 
            
            // Hoặc dùng router nếu muốn mượt hơn:
            // import router from '@/router';
            // router.push('/login');
        }
        return Promise.reject(error);
    }
);

export default api;