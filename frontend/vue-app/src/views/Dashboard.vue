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
            <h1 class="text-2xl font-bold text-gray-800">Tổng quan</h1>
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
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          
          <div class="bg-white rounded-xl shadow-sm p-6 flex items-center border-b-4 border-blue-500">
            <div class="p-3 bg-blue-100 rounded-full text-blue-600 mr-4">
              <i class="fas fa-car text-2xl"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm font-medium">Tổng Phiên Bản Xe</p>
              <h3 class="text-2xl font-bold text-gray-800">{{ stats.totalCars }}</h3>
            </div>
          </div>

          <div class="bg-white rounded-xl shadow-sm p-6 flex items-center border-b-4 border-green-500">
            <div class="p-3 bg-green-100 rounded-full text-green-600 mr-4">
              <i class="fas fa-users text-2xl"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm font-medium">Khách Hàng</p>
              <h3 class="text-2xl font-bold text-gray-800">{{ stats.totalUsers }}</h3>
            </div>
          </div>

          <div class="bg-white rounded-xl shadow-sm p-6 flex items-center border-b-4 border-yellow-500">
            <div class="p-3 bg-yellow-100 rounded-full text-yellow-600 mr-4">
              <i class="fas fa-comments text-2xl"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm font-medium">Yêu Cầu Tư Vấn</p>
              <h3 class="text-2xl font-bold text-gray-800">18</h3> </div>
          </div>

          <div class="bg-white rounded-xl shadow-sm p-6 flex items-center border-b-4 border-red-500">
            <div class="p-3 bg-red-100 rounded-full text-red-600 mr-4">
              <i class="fas fa-chart-line text-2xl"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm font-medium">Doanh Thu Tháng</p>
              <h3 class="text-2xl font-bold text-gray-800">2.5 Tỷ</h3> </div>
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          <div class="bg-white rounded-xl shadow-sm p-6 lg:col-span-2">
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-bold text-gray-800">Khách Hàng Mới Nhất</h3>
              <router-link to="/admin/users" class="text-blue-600 text-sm hover:underline">Xem tất cả</router-link>
            </div>
            <div class="overflow-x-auto">
              <table class="w-full text-left border-collapse">
                <thead class="bg-gray-50 text-gray-600 text-xs uppercase">
                  <tr>
                    <th class="p-3 border-b">Username</th>
                    <th class="p-3 border-b">Họ Tên</th>
                    <th class="p-3 border-b">Ngày sinh</th>
                    <th class="p-3 border-b text-center">Vai trò</th>
                  </tr>
                </thead>
                <tbody class="text-sm">
                  <tr v-if="isLoading"><td colspan="4" class="p-4 text-center">Đang tải...</td></tr>
                  <tr v-else v-for="user in recentUsers" :key="user.id" class="border-b hover:bg-gray-50">
                    <td class="p-3 font-medium">{{ user.username }}</td>
                    <td class="p-3 text-gray-600">{{ user.firstName }} {{ user.lastName }}</td>
                    <td class="p-3 text-gray-500">{{ user.dob }}</td>
                    <td class="p-3 text-center">
                      <span class="bg-blue-100 text-blue-700 px-2 py-1 rounded text-xs font-bold">User</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div class="bg-white rounded-xl shadow-sm p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Top Xe Quan Tâm</h3>
            <div class="space-y-4">
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span class="font-medium">VinFast VF 8</span>
                  <span class="text-gray-500">85%</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2.5">
                  <div class="bg-blue-600 h-2.5 rounded-full" style="width: 85%"></div>
                </div>
              </div>
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span class="font-medium">VinFast VF 9</span>
                  <span class="text-gray-500">60%</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2.5">
                  <div class="bg-green-500 h-2.5 rounded-full" style="width: 60%"></div>
                </div>
              </div>
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span class="font-medium">VinFast VF 5</span>
                  <span class="text-gray-500">45%</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2.5">
                  <div class="bg-yellow-500 h-2.5 rounded-full" style="width: 45%"></div>
                </div>
              </div>
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span class="font-medium">VinFast VF e34</span>
                  <span class="text-gray-500">30%</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2.5">
                  <div class="bg-red-500 h-2.5 rounded-full" style="width: 30%"></div>
                </div>
              </div>
            </div>
            
            <div class="mt-8 p-4 bg-blue-50 rounded-lg border border-blue-100">
                <h4 class="font-bold text-blue-800 mb-2">Thông báo hệ thống</h4>
                <p class="text-sm text-blue-600">Hệ thống sẽ bảo trì định kỳ vào 00:00 ngày 25/12/2025. Vui lòng lưu dữ liệu trước thời điểm này.</p>
            </div>
          </div>

        </div>

      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import api from '@/utils/axios'
import { useRouter } from 'vue-router'
import AdminSidebar from '@/components/AdminSidebar.vue'
const router = useRouter()
const isSidebarOpen = ref(false)
const isLoading = ref(false)

// State chứa số liệu thống kê
const stats = reactive({
  totalCars: 0,
  totalUsers: 0
})

// Danh sách user mới nhất
const recentUsers = ref([])

// Hàm lấy dữ liệu Dashboard
const fetchDashboardData = async () => {
  isLoading.value = true
  try {
    // 1. Gọi API lấy danh sách xe để đếm
    const carResponse = await api.get('/api/variants')
    const cars = carResponse.data.result || []
    stats.totalCars = cars.length

    // 2. Gọi API lấy danh sách User để đếm & hiển thị top 5
    const userResponse = await api.get('/api/users')
    const users = userResponse.data.result || []
    stats.totalUsers = users.length
    
    // Lấy 5 user mới nhất (Giả sử list trả về theo thứ tự, hoặc cắt 5 người đầu)
    recentUsers.value = users.slice(0, 5)

  } catch (error) {
    console.error("Lỗi tải dashboard:", error)
  } finally {
    isLoading.value = false
  }
}

const handleLogout = () => {
  localStorage.removeItem('authToken')
  router.push('/login')
}

onMounted(() => {
  fetchDashboardData()
})
</script>