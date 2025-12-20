<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 px-4 py-8">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-gray-800">Đăng Ký Tài Khoản</h2>
        <p class="text-gray-500 mt-2">Tạo tài khoản để trải nghiệm dịch vụ</p>
      </div>

      <form @submit.prevent="handleRegister" class="space-y-4">
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tên đăng nhập <span class="text-red-500">*</span></label>
          <input 
            v-model="form.username" 
            type="text" 
            required 
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
            placeholder="VD: nguyenvan_a (Tối thiểu 4 ký tự)" 
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mật khẩu <span class="text-red-500">*</span></label>
          <input 
            v-model="form.password" 
            type="password" 
            required 
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
            placeholder="Tối thiểu 6 ký tự" 
          />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Họ</label>
            <input 
              v-model="form.firstName" 
              type="text" 
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
              placeholder="Nguyễn" 
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên</label>
            <input 
              v-model="form.lastName" 
              type="text" 
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
              placeholder="Văn A" 
            />
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh <span class="text-red-500">*</span></label>
          <input 
            v-model="form.dob" 
            type="date" 
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
          />
          <p class="text-xs text-gray-500 mt-1">Bạn phải trên 10 tuổi.</p>
        </div>

        <div v-if="message" 
             :class="{'bg-green-100 text-green-700 border-green-200': isSuccess, 'bg-red-100 text-red-700 border-red-200': !isSuccess}" 
             class="p-3 text-sm rounded-lg text-center border">
          {{ message }}
        </div>

        <button 
          type="submit" 
          :disabled="isLoading"
          class="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 rounded-lg transition transform active:scale-95 flex justify-center items-center mt-6 disabled:bg-gray-400 disabled:cursor-not-allowed"
        >
          <span v-if="isLoading" class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full mr-2"></span>
          {{ isLoading ? 'Đang xử lý...' : 'Đăng Ký Ngay' }}
        </button>
      </form>

      <div class="mt-6 text-center text-sm text-gray-600">
        Đã có tài khoản? 
        <router-link to="/login" class="text-blue-600 font-semibold hover:underline">Đăng nhập</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/utils/axios'; // Đảm bảo bạn đang dùng file axios.js có cấu hình proxy

const router = useRouter();
const isLoading = ref(false);
const message = ref('');
const isSuccess = ref(false);

// Form dữ liệu khớp với UserCreationRequest ở Backend
const form = reactive({
  username: '',
  password: '',
  firstName: '',
  lastName: '',
  dob: '' // Format mặc định của input date là YYYY-MM-DD, đúng chuẩn Backend cần
});

const handleRegister = async () => {
  // Validate cơ bản phía client trước khi gọi API
  if (form.username.length < 4) {
    message.value = "Tên đăng nhập phải có ít nhất 4 ký tự.";
    isSuccess.value = false;
    return;
  }
  if (form.password.length < 6) {
    message.value = "Mật khẩu phải có ít nhất 6 ký tự.";
    isSuccess.value = false;
    return;
  }

  isLoading.value = true;
  message.value = '';
  isSuccess.value = false;

  try {
    // 🔑 QUAN TRỌNG: Gọi đúng endpoint /api/users
    // Backend: UserController -> @RequestMapping("/api/users")
    const response = await api.post('/api/users', form);

    isSuccess.value = true;
    message.value = 'Đăng ký thành công! Đang chuyển đến trang đăng nhập...';
    
    // Reset form
    form.username = '';
    form.password = '';
    
    // Chuyển hướng sau 1.5 giây
    setTimeout(() => {
      router.push('/login');
    }, 1500);

  } catch (error) {
    isSuccess.value = false;
    console.error("Lỗi đăng ký:", error);

    // Xử lý thông báo lỗi từ Backend trả về
    if (error.response && error.response.data) {
        const errorCode = error.response.data.code;
        const errorMsg = error.response.data.message;

        // Map một số mã lỗi phổ biến (Dựa trên AppConfig/Exception của bạn)
        if (errorCode === 1002) message.value = "Tài khoản đã tồn tại."; // USER_EXISTED
        else if (errorCode === 1003) message.value = "Tên đăng nhập không hợp lệ (tối thiểu 4 ký tự)."; // USERNAME_INVALID
        else if (errorCode === 1004) message.value = "Mật khẩu quá yếu (tối thiểu 6 ký tự)."; // INVALID_PASSWORD
        else if (message.value === "INVALID_DOB") message.value = "Bạn chưa đủ tuổi (yêu cầu > 10 tuổi).";
        else message.value = errorMsg || "Đăng ký thất bại. Vui lòng thử lại.";
    } else {
        message.value = "Không thể kết nối đến máy chủ.";
    }
  } finally {
    isLoading.value = false;
  }
};
</script>