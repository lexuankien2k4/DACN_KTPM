<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

// --- Logic Sticky Header (từ ui-main.js) ---
const isScrolled = ref(false)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

// onMounted/onUnmounted thay thế cho 'DOMContentLoaded'
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// --- Logic Nút Bấm Menu ---
const emit = defineEmits(['toggle-menu'])

function onToggleClick() {
  emit('toggle-menu')
}
</script>

<template>
  <header 
    id="main-header" 
    class="fixed top-0 left-0 right-0 z-50 transition-all duration-300"
    :class="{ 'header-scrolled': isScrolled }"
  >
    <!-- Thay đổi ở đây: Thay 'container mx-auto' bằng 'w-full' để nội dung trải rộng ra 2 bên -->
    <div class="w-full px-6 py-4 flex justify-between items-center">
      
      <!-- 1. LOGO -->
      <router-link to="/" class="text-2xl font-extrabold text-gray-800 tracking-wider">
        Vin<span class="text-blue-600">Auto</span>
      </router-link>

      <!-- 2. MAIN NAVIGATION (Chỉ hiện trên Desktop) -->
      <nav class="hidden lg:flex items-center space-x-8">
        <router-link to="/#cars-showcase" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Ô tô</router-link>
        <router-link to="/#bikes-showcase" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Xe máy điện</router-link>
        <router-link to="/#accessories" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Phụ kiện</router-link>
        <router-link to="/#after-sales" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Dịch vụ</router-link>
        <router-link to="/#why-us" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Tại sao chọn chúng tôi ?</router-link>
      </nav>

      <!-- 3. RIGHT ACTIONS (Nút Tư vấn + Nút Menu) -->
      <div class="flex items-center gap-4">
        <!-- Nút Đăng ký tư vấn (Ẩn trên mobile nhỏ, hiện trên tablet/desktop) -->
        <RouterLink 
          to="/consultation"
          class="hidden md:block bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-5 rounded-full transition-all duration-300 shadow-md hover:shadow-lg"
        >
          Đăng ký tư vấn
        </RouterLink>

        <!-- Nút Menu / Hamburger (LUÔN HIỆN) -->
        <!-- Đã xóa class 'lg:hidden' để hiện trên desktop -->
        <button 
          @click="onToggleClick" 
          class="text-gray-900 hover:text-blue-600 focus:outline-none transition-colors duration-300 p-1"
          aria-label="Toggle Menu"
        >
          <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
          </svg>
        </button>
      </div>

    </div>
  </header>
</template>