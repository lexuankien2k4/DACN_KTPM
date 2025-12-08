<script setup>
import { ref } from 'vue'
import AppHeader from './components/AppHeader.vue'
import AppFooter from './components/AppFooter.vue'
import SideMenu from './components/SideMenu.vue' // Import SideMenu

// 1. Khai báo trạng thái menu (giống như 1 biến toàn cục cho App)
const isMenuOpen = ref(false)

// 2. Hàm để thay đổi trạng thái
function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value
}
function closeMenu() {
  isMenuOpen.value = false
}
</script>

<template>
  <div class="app-root">
    <AppHeader @toggle-menu="toggleMenu" />

    <SideMenu :is-open="isMenuOpen" @close-menu="closeMenu" />

    <main>
      <router-view />
    </main>
    
    <AppFooter />
  </div>
</template>

<style>
/* CSS của bạn ở đây vẫn giữ nguyên */
.app-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
main {
  flex: 1;
  /* Bỏ padding ở đây để các view tự kiểm soát */
  /* padding: 1rem; */
}
</style>