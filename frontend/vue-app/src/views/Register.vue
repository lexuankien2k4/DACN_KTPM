<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 px-4 py-8">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-gray-800">Đăng Ký</h2>
        <p class="text-gray-500 mt-2">Tạo tài khoản mới để quản lý xe</p>
      </div>

      <form @submit.prevent="handleRegister" class="space-y-4">
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tên đăng nhập <span class="text-red-500">*</span></label>
          <input v-model="form.username" type="text" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none" placeholder="VD: nguyenvan_a" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mật khẩu <span class="text-red-500">*</span></label>
          <input v-model="form.password" type="password" required minlength="6" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none" placeholder="Tối thiểu 6 ký tự" />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Họ</label>
            <input v-model="form.firstName" type="text" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none" placeholder="Nguyễn" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên</label>
            <input v-model="form.lastName" type="text" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none" placeholder="Văn A" />
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh</label>
          <input v-model="form.dob" type="date" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none" />
        </div>

        <div v-if="message" :class="{'bg-green-50 text-green-700': isSuccess, 'bg-red-50 text-red-700': !isSuccess}" class="p-3 text-sm rounded-lg text-center">
          {{ message }}
        </div>

        <button 
          type="submit" 
          :disabled="isLoading"
          class="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 rounded-lg transition transform active:scale-95 flex justify-center items-center mt-4"
        >
          <span v-if="isLoading" class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full mr-2"></span>
          {{ isLoading ? 'Đang đăng ký...' : 'Đăng Ký Tài Khoản' }}
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
import api from '@/utils/axios';

const router = useRouter();
const isLoading = ref(false);
const message = ref('');
const isSuccess = ref(false);

// Dữ liệu form khớp với UserCreationRequest của Backend
const form = reactive({
  username: '',
  password: '',
  firstName: '',
  lastName: '',
  dob: '' // YYYY-MM-DD
});

const handleRegister = async () => {
  isLoading.value = true;
  message.value = '';
  isSuccess.value = false;

  try {
    // Gọi API: POST /users
    await api.post('/users', form);

    isSuccess.value = true;
    message.value = 'Đăng ký thành công! Đang chuyển hướng...';
    
    // Chuyển hướng về trang Login sau 1.5 giây
    setTimeout(() => {
      router.push('/login');
    }, 1500);

  } catch (error) {
    isSuccess.value = false;
    // Hiển thị lỗi từ backend (ví dụ: User existed, Password weak...)
    message.value = error.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.';
  } finally {
    isLoading.value = false;
  }
};
</script>