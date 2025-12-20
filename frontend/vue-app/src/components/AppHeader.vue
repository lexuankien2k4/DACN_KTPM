<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// --- Logic Sticky Header (Giữ nguyên) ---
const isScrolled = ref(false)
const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

// --- Logic Quản lý Trạng thái Đăng nhập (MỚI) ---
const isLoggedIn = ref(false)
const currentUser = ref(null)

// Hàm kiểm tra đăng nhập (đọc từ localStorage)
const checkLoginStatus = () => {
  const token = localStorage.getItem('authToken')
  const userStr = localStorage.getItem('user') // Giả sử bạn lưu info user vào đây lúc login
  
  if (token) {
    isLoggedIn.value = true
    if (userStr) {
      currentUser.value = JSON.parse(userStr)
    }
  } else {
    isLoggedIn.value = false
    currentUser.value = null
  }
}

// Hàm Đăng xuất
const handleLogout = () => {
  // Xóa token và thông tin user
  localStorage.removeItem('authToken')
  localStorage.removeItem('user')
  
  // Cập nhật trạng thái
  isLoggedIn.value = false
  currentUser.value = null
  
  // Chuyển về trang login hoặc trang chủ
  router.push('/login')
}

// --- Lifecycle Hooks ---
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  checkLoginStatus() // Kiểm tra ngay khi load header
  
  // (Tùy chọn) Lắng nghe sự kiện storage để đồng bộ giữa các tab
  window.addEventListener('storage', checkLoginStatus)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('storage', checkLoginStatus)
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
    :class="{ 'header-scrolled': isScrolled, 'bg-white shadow-md': isScrolled, 'bg-transparent': !isScrolled }"
  >
    <div class="w-full px-6 py-4 flex justify-between items-center">
      
      <router-link to="/" class="text-2xl font-extrabold text-gray-800 tracking-wider flex items-center gap-2">
        <span>Vin<span class="text-blue-600">Auto</span></span>
      </router-link>

      <nav class="hidden lg:flex items-center space-x-8">
        <router-link to="/#cars-showcase" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Ô tô</router-link>
        <router-link to="/#bikes-showcase" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Xe máy điện</router-link>
        <router-link to="/#accessories" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Phụ kiện</router-link>
        <router-link to="/#after-sales" class="text-gray-900 hover:text-blue-600 font-medium transition-colors duration-300">Dịch vụ</router-link>
      </nav>

      <div class="flex items-center gap-4">
        
        <div class="hidden md:flex items-center gap-3 mr-2">
          
          <template v-if="isLoggedIn">
            <div class="flex items-center gap-2">
                <span class="text-sm font-semibold text-gray-700">
                  Chào, {{ currentUser?.lastName || 'Khách' }}
                </span>
                <button 
                  @click="handleLogout" 
                  class="text-sm text-red-500 hover:text-red-700 font-medium transition-colors"
                >
                  (Đăng xuất)
                </button>
            </div>
            
            <router-link 
              v-if="currentUser?.roles?.some(r => r.name === 'ADMIN')" 
              to="/admin/dashboard"
              class="text-gray-900 hover:text-blue-600" title="Trang quản trị"
            >
              <i class="fas fa-user-cog text-xl"></i>
            </router-link>
          </template>

          <template v-else>
            <RouterLink 
              to="/login" 
              class="text-gray-700 hover:text-blue-600 font-medium transition-colors"
            >
              Đăng nhập
            </RouterLink>
            <span class="text-gray-300">|</span>
            <RouterLink 
              to="/register" 
              class="text-gray-700 hover:text-blue-600 font-medium transition-colors"
            >
              Đăng ký
            </RouterLink>
          </template>
        </div>
        <RouterLink 
          to="/consultation"
          class="hidden md:block bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-5 rounded-full transition-all duration-300 shadow-md hover:shadow-lg"
        >
          Tư vấn
        </RouterLink>

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

<style scoped>
/* Thêm class này để header có nền trắng khi cuộn */
.header-scrolled {
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}
</style>