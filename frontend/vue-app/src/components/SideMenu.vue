<script setup>
// 1. Nhận 'props' (thuộc tính) 'isOpen' từ App.vue
const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

// 2. Định nghĩa sự kiện 'close-menu'
const emit = defineEmits(['close-menu'])

// 3. Hàm này được gọi để phát tín hiệu 'đóng menu'
function onCloseClick() {
  emit('close-menu')
}
</script>

<template>
  <div 
    id="menu-overlay" 
    class="fixed inset-0 bg-black bg-opacity-50 z-40"
    :class="{ 'is-open': isOpen }"
    @click="onCloseClick" 
  ></div>
  
  <div 
    id="side-menu" 
    class="fixed top-0 right-0 h-full w-80 bg-white shadow-lg p-8 z-50"
    :class="{ 'is-open': isOpen }"
  >
    <div class="flex justify-between items-center mb-8">
      <h2 class="text-2xl font-bold text-gray-900">Tiện ích</h2>
      <button id="close-menu-button" @click="onCloseClick" class="text-gray-500 hover:text-gray-900 text-2xl">
        <i class="fas fa-times"></i>
      </button>
    </div>
    
    <nav class="flex flex-col space-y-4">
      <a href="#" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-car-side w-6 mr-2 text-blue-500"></i>Đăng ký lái thử
      </a>
      <a href="#" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-right-left w-6 mr-2 text-blue-500"></i>So sánh xe
      </a>
      <a href="#" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-file-invoice-dollar w-6 mr-2 text-blue-500"></i>Dự đoán chi phí lăn bánh
      </a>
      
      <router-link to="/installment" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-hand-holding-dollar w-6 mr-2 text-blue-500"></i>Dự đoán vay trả góp
      </router-link>

      <a href="#" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-file-signature w-6 mr-2 text-blue-500"></i>Thẩm định vay
      </a>
      <a href="#" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-calendar-check w-6 mr-2 text-blue-500"></i>Đặt lịch dịch vụ
      </a>

      <router-link to="/#contact" class="text-gray-700 hover:text-blue-600 transition-colors text-lg" @click="onCloseClick">
        <i class="fas fa-headset w-6 mr-2 text-blue-500"></i>Liên hệ
      </router-link>
    </nav>
    
  </div>
</template>