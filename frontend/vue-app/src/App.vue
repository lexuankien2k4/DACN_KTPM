<script setup>
import { ref, computed } from 'vue' // Thêm computed
import { useRoute } from 'vue-router' // Thêm useRoute
import AppHeader from './components/AppHeader.vue'
import AppFooter from './components/AppFooter.vue'
import SideMenu from './components/SideMenu.vue'

const route = useRoute() // Lấy thông tin route hiện tại

// 1. Khai báo trạng thái menu
const isMenuOpen = ref(false)

// 2. Hàm thay đổi trạng thái
function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value
}
function closeMenu() {
  isMenuOpen.value = false
}


const showLayout = computed(() => {
  return !route.meta.hideHeaderFooter
})
</script>

<template>
  <div class="app-root">
    
    <template v-if="showLayout">
      <AppHeader @toggle-menu="toggleMenu" />
      <SideMenu :is-open="isMenuOpen" @close-menu="closeMenu" />
    </template>

    <main>
      <router-view />
    </main>
    
    <AppFooter v-if="showLayout" />
  </div>
</template>

<style>
/* CSS giữ nguyên */
.app-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
main {
  flex: 1;
}
</style>