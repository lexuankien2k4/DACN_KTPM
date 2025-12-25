<template>
  <aside :class="['w-64 bg-white shadow-xl flex-shrink-0 transition-all duration-300', isOpen ? 'translate-x-0' : '-translate-x-64', 'md:translate-x-0 fixed md:relative z-20 h-full']">
    <div class="h-full flex flex-col">
      <div class="flex items-center justify-center h-20 border-b">
        <img src="https://cdn-001.haui.edu.vn//img/logo-haui-size.png" alt="Logo" class="h-10 mr-2">
        <h2 class="text-xl font-bold text-gray-800">NHÓM 7</h2>
        <button @click="$emit('close')" class="md:hidden ml-auto mr-4 text-gray-600">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <nav class="flex-1 overflow-y-auto py-4">
        
        <router-link to="/admin/dashboard" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-th-large w-6"></i>
          <span class="font-medium">Dashboard</span>
        </router-link>

        <router-link to="/admin/cars" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-car w-6"></i>
          <span class="font-medium">Quản lý Sản phẩm</span>
        </router-link>

        <router-link to="/admin/employee" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-users-cog w-6"></i>
          <span class="font-medium">Quản lý Nhân sự</span>
        </router-link>

        <router-link to="/admin/customers" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-users w-6"></i>
          <span class="font-medium">Khách hàng</span>
        </router-link>

        <router-link to="/admin/consultations" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-headset w-6"></i>
          <span class="font-medium">Yêu cầu Tư vấn</span>
        </router-link>

        <router-link to="/admin/orders" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-file-invoice-dollar w-6"></i>
          <span class="font-medium">Đặt cọc & Đơn hàng</span>
        </router-link>

        <router-link to="/admin/contracts" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-file-contract w-6"></i>
          <span class="font-medium">Hợp đồng</span>
        </router-link>
<router-link to="/admin/nhatky" active-class="bg-blue-50 text-blue-600 border-r-4 border-blue-600" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
          <i class="fas fa-history w-6"></i>
          <span class="font-medium">Nhật ký hệ thống</span>
        </router-link>
      </nav>

      <div class="p-4 border-t">
        <button @click="handleLogout" class="flex items-center w-full px-6 py-3 text-red-600 hover:bg-red-50 rounded-lg transition-colors">
          <i class="fas fa-sign-out-alt w-6"></i>
          <span class="font-medium">Đăng xuất</span>
        </button>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { useRouter } from 'vue-router'

// Nhận prop từ cha để điều khiển đóng mở trên mobile
defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

// Define events để đóng sidebar
defineEmits(['close'])

const router = useRouter()

const handleLogout = () => {
  if(confirm("Bạn có chắc chắn muốn đăng xuất?")) {
      localStorage.removeItem('authToken')
      router.push('/login')
  }
}
</script>