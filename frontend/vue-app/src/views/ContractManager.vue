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
            <h1 class="text-2xl font-bold text-gray-800">Quản lý Hợp đồng</h1>
            <p class="text-xs text-gray-500">Chào mừng quay trở lại, Admin!</p>
          </div>
        </div>
        
        <div class="flex items-center gap-4">
           <button class="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 text-sm font-bold mr-4 hidden sm:block">
              <i class="fas fa-plus mr-1"></i> Tạo Hợp đồng
           </button>

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
         <div class="text-center text-gray-500 mt-20">
            <i class="fas fa-file-contract text-6xl mb-4 text-gray-300"></i>
            <p class="text-lg">Hệ thống quản lý hợp đồng điện tử</p>
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