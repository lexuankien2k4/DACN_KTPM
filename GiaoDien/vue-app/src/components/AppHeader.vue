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
// 1. Định nghĩa sự kiện mà component này sẽ 'phát ra' (emit)
const emit = defineEmits(['toggle-menu'])

// 2. Hàm này được gọi khi bấm nút, nó sẽ 'phát' sự kiện lên App.vue
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
    <div class="container mx-auto px-6 py-4 flex justify-between items-center">
      <router-link to="/" class="text-2xl font-extrabold text-gray-800 tracking-wider">
        Vin<span class="text-blue-600">Auto</span>
      </router-link>
      <nav class="hidden lg:flex items-center space-x-8">
        <router-link to="/#cars-showcase" class="text-gray-900 ...">Ô tô</router-link>
        <router-link to="/#bikes-showcase" class="text-gray-900 ...">Xe máy điện</router-link>
        <router-link to="/#accessories" class="text-gray-900 ...">Phụ kiện</router-link>
        <router-link to="/#after-sales" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Dịch vụ</router-link>
        <router-link to="/#why-us" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Tại sao chọn chúng tôi ?</router-link>
        </nav>
      <div class="flex items-center space-x-4">
        <a href="#" class="hidden md:block bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-5 rounded-full transition-all duration-300">
                    Lái thử
        </a>
        <button 
          id="menu-toggle-button" 
          @click="onToggleClick" 
          class="text-gray-800 text-2xl z-50"
        >
          <i class="fas fa-bars"></i>
        </button>
      </div>
    </div>
  </header>
</template>

