<template>
  <div class="flex h-screen bg-gray-100 font-sans">
    
    <AdminSidebar :isOpen="isSidebarOpen" @close="isSidebarOpen = false" />

    <main class="flex-1 flex flex-col overflow-hidden relative">
      <header class="h-20 bg-white shadow-sm flex items-center justify-between px-6 z-10">
        <div class="flex items-center">
          <button @click="isSidebarOpen = true" class="md:hidden text-gray-600 mr-4">
            <i class="fas fa-bars text-xl"></i>
          </button>
          <div>
            <h1 class="text-2xl font-bold text-gray-800">Nhật ký hệ thống</h1>
            <p class="text-xs text-gray-500">Chào mừng quay trở lại, Admin!</p>
          </div>
        </div>
        
        <div class="flex items-center gap-4">
          <div class="flex items-center gap-3">
            <div class="text-right hidden sm:block">
              <p class="text-sm font-bold text-gray-700">Admin</p>
              <small class="text-gray-500">Quản trị viên</small>
            </div>
            <img src="https://ui-avatars.com/api/?name=Admin&background=0D8ABC&color=fff" alt="Admin" class="h-10 w-10 rounded-full border">
          </div>
        </div>
      </header>

      <div class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-50 p-6">
        
        <div class="bg-white rounded-xl shadow-md overflow-hidden">
            <div class="p-4 border-b bg-gray-50 flex justify-between items-center">
                <h3 class="font-bold text-gray-700">Lịch sử hoạt động</h3>
                <button class="text-sm text-blue-600 hover:underline">Xuất báo cáo</button>
            </div>
            
            <div class="overflow-x-auto">
              <table class="w-full text-left">
                <thead class="bg-gray-100 text-gray-600 uppercase text-xs font-bold">
                  <tr>
                    <th class="p-4">Thời gian</th>
                    <th class="p-4">Người dùng</th>
                    <th class="p-4">Hành động</th>
                    <th class="p-4">Chi tiết</th>
                    <th class="p-4 text-center">Trạng thái</th>
                  </tr>
                </thead>
                <tbody class="text-sm">
                  <tr class="border-b hover:bg-gray-50">
                    <td class="p-4 text-gray-500">25/12/2025 14:30</td>
                    <td class="p-4 font-medium">admin</td>
                    <td class="p-4 text-blue-600 font-medium">Đăng nhập</td>
                    <td class="p-4 text-gray-600">Đăng nhập thành công từ IP 192.168.1.1</td>
                    <td class="p-4 text-center"><span class="bg-green-100 text-green-700 px-2 py-1 rounded text-xs font-bold">Success</span></td>
                  </tr>
                  <tr class="border-b hover:bg-gray-50">
                    <td class="p-4 text-gray-500">25/12/2025 10:15</td>
                    <td class="p-4 font-medium">staff_01</td>
                    <td class="p-4 text-purple-600 font-medium">Cập nhật đơn hàng</td>
                    <td class="p-4 text-gray-600">Thay đổi trạng thái đơn #123 sang "Đã cọc"</td>
                    <td class="p-4 text-center"><span class="bg-green-100 text-green-700 px-2 py-1 rounded text-xs font-bold">Success</span></td>
                  </tr>
                  <tr class="border-b hover:bg-gray-50">
                    <td class="p-4 text-gray-500">24/12/2025 09:00</td>
                    <td class="p-4 font-medium">system</td>
                    <td class="p-4 text-red-600 font-medium">Lỗi hệ thống</td>
                    <td class="p-4 text-gray-600">Kết nối cơ sở dữ liệu thất bại</td>
                    <td class="p-4 text-center"><span class="bg-red-100 text-red-700 px-2 py-1 rounded text-xs font-bold">Error</span></td>
                  </tr>
                </tbody>
              </table>
            </div>
        </div>

      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AdminSidebar from '@/components/AdminSidebar.vue'

const isSidebarOpen = ref(false)
</script>