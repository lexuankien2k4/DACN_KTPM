<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 px-4">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-gray-800">Đăng Nhập</h2>
        <p class="text-gray-500 mt-2">Chào mừng bạn quay trở lại</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tên đăng nhập</label>
          <input 
            v-model="username" 
            type="text" 
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
            placeholder="Nhập username"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mật khẩu</label>
          <input 
            v-model="password" 
            type="password" 
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
            placeholder="••••••••"
          />
        </div>

        <div v-if="errorMessage" class="p-3 bg-red-50 text-red-600 text-sm rounded-lg text-center">
          {{ errorMessage }}
        </div>

        <button 
          type="submit" 
          :disabled="isLoading"
          class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg transition transform active:scale-95 flex justify-center items-center"
        >
          <span v-if="isLoading" class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full mr-2"></span>
          {{ isLoading ? 'Đang xử lý...' : 'Đăng nhập' }}
        </button>
      </form>

      <div class="mt-6 text-center text-sm text-gray-600">
        Chưa có tài khoản? 
        <router-link to="/register" class="text-blue-600 font-semibold hover:underline">Đăng ký ngay</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/utils/axios'; 

const router = useRouter();
const username = ref('');
const password = ref('');
const isLoading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
  isLoading.value = true;
  errorMessage.value = '';

  try {
    // Gọi API: POST /auth/token
    const response = await api.post('/api/auth/token', {
      username: username.value,
      password: password.value
    });

    // Backend trả về: { result: { token: "...", authenticated: true } }
    const { token, authenticated } = response.data.result;

    if (authenticated) {
      // Lưu token vào LocalStorage
      localStorage.setItem('authToken', token);
      
      // Chuyển hướng đến trang Admin (hoặc trang chủ)
      router.push('/admin/dashboard'); 
    } else {
      errorMessage.value = 'Xác thực thất bại.';
    }

  } catch (error) {
    console.error(error);
    // Xử lý lỗi từ Backend trả về (nếu có message)
    errorMessage.value = error.response?.data?.message || 'Tên đăng nhập hoặc mật khẩu không đúng.';
  } finally {
    isLoading.value = false;
  }
};
</script>