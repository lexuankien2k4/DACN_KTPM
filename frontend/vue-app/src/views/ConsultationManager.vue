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
            <h1 class="text-2xl font-bold text-gray-800">Quản lý Yêu cầu Tư vấn</h1>
            <p class="text-xs text-gray-500">Xem và xử lý các yêu cầu lái thử từ khách hàng</p>
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
        
        <div v-if="loading" class="flex justify-center items-center h-64">
           <i class="fas fa-circle-notch fa-spin text-4xl text-blue-600"></i>
        </div>

        <div v-else class="bg-white rounded-xl shadow-md overflow-hidden border border-gray-100">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead class="bg-gray-50 text-gray-600 uppercase text-xs font-bold border-b">
                <tr>
                  <th class="p-4 whitespace-nowrap">ID / Ngày tạo</th>
                  <th class="p-4 whitespace-nowrap">Khách hàng</th>
                  <th class="p-4 whitespace-nowrap">Xe quan tâm</th>
                  <th class="p-4 whitespace-nowrap">Showroom / Địa điểm</th>
                  <th class="p-4 whitespace-nowrap">Lịch hẹn</th>
                  <th class="p-4 whitespace-nowrap">Ghi chú</th>
                  <th class="p-4 whitespace-nowrap">Trạng thái</th>
                  <th class="p-4 whitespace-nowrap text-center">Hành động</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="item in consultations" :key="item.id" class="hover:bg-blue-50 transition-colors">
                  
                  <td class="p-4 align-top">
                    <span class="font-bold text-gray-700">#{{ item.id }}</span>
                    <div class="text-xs text-gray-400 mt-1">
                      {{ formatDate(item.createdAt) }}
                    </div>
                  </td>

                  <td class="p-4 align-top">
                    <div class="font-bold text-gray-800">{{ item.customerName }}</div>
                    <div class="text-sm text-blue-600 flex items-center gap-1 mt-1">
                      <i class="fas fa-phone-alt text-xs"></i> {{ item.phoneNumber }}
                    </div>
                    <div v-if="item.email" class="text-xs text-gray-500 mt-1">
                      <i class="fas fa-envelope text-xs"></i> {{ item.email }}
                    </div>
                  </td>

                  <td class="p-4 align-top">
                    <span class="bg-gray-100 text-gray-700 px-2 py-1 rounded text-xs font-bold border border-gray-200">
                       {{ item.carVariantName || (item.carVariant ? item.carVariant.name : 'Chưa rõ') }}
                    </span>
                  </td>

                  <td class="p-4 align-top max-w-xs">
                    <div v-if="item.showroomName || item.showroom" class="text-sm font-medium text-gray-700">
                       <i class="fas fa-map-marker-alt text-red-500 mr-1"></i>
                       {{ item.showroomName || item.showroom.name }}
                    </div>
                    <div v-else class="text-sm text-gray-500 italic">Chưa chọn Showroom</div>
                    
                    <div class="text-xs text-gray-500 mt-1">
                      Khu vực: <span class="font-semibold">{{ item.province }}</span>
                    </div>
                  </td>

                  <td class="p-4 align-top">
                    <div v-if="item.scheduledAt">
                      <div class="text-sm font-bold text-blue-700">
                        {{ formatDate(item.scheduledAt, true) }}
                      </div>
                      <div class="text-xs text-green-600 mt-1 font-medium bg-green-50 inline-block px-2 py-0.5 rounded-full">
                        Đã đặt lịch
                      </div>
                    </div>
                    <div v-else class="text-xs text-gray-400 italic">Chưa có lịch</div>
                  </td>

                  <td class="p-4 align-top max-w-[200px]">
                    <p class="text-sm text-gray-600 truncate" :title="item.note">
                      {{ item.note || 'Không có ghi chú' }}
                    </p>
                  </td>

                  <td class="p-4 align-top">
                    <span :class="getStatusClass(item.status)">
                      {{ item.status || 'Mới' }}
                    </span>
                  </td>

                  <td class="p-4 align-top text-center">
                    <div class="flex items-center justify-center gap-2">
                       <button class="text-gray-500 hover:text-blue-600 p-2 rounded-full hover:bg-gray-100 transition-all" title="Xem chi tiết">
                        <i class="fas fa-eye"></i>
                      </button>
                      <button class="text-gray-500 hover:text-green-600 p-2 rounded-full hover:bg-gray-100 transition-all" title="Xác nhận">
                        <i class="fas fa-check-circle"></i>
                      </button>
                    </div>
                  </td>
                </tr>
                
                <tr v-if="consultations.length === 0">
                  <td colspan="8" class="p-8 text-center text-gray-500">
                    <i class="fas fa-inbox text-4xl mb-3 text-gray-300"></i>
                    <p>Chưa có yêu cầu tư vấn nào.</p>
                  </td>
                </tr>

              </tbody>
            </table>
          </div>
          
          <div class="bg-gray-50 px-6 py-4 border-t flex justify-between items-center text-sm text-gray-600">
            <span>Hiển thị {{ consultations.length }} bản ghi</span>
            <div class="flex gap-2">
              <button class="px-3 py-1 border rounded bg-white hover:bg-gray-100 disabled:opacity-50" disabled>Trước</button>
              <button class="px-3 py-1 border rounded bg-white hover:bg-gray-100">Sau</button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminSidebar from '@/components/AdminSidebar.vue' // Đảm bảo đường dẫn đúng

const isSidebarOpen = ref(false)
const consultations = ref([])
const loading = ref(true)

// ĐOẠN CODE MỚI (COPY VÀO ĐÂY)
const fetchConsultations = async () => {
  try {
    // 1. Lấy token từ localStorage (hoặc nơi bạn lưu khi đăng nhập)
    // Lưu ý: Kiểm tra xem lúc đăng nhập bạn lưu key là 'token' hay 'accessToken'
    const token = localStorage.getItem('authToken'); 

    if (!token) {
        console.warn("Chưa tìm thấy Token, vui lòng đăng nhập lại.");
    }

    // 2. Sửa URL: Thêm /public để khớp với Controller Backend
    const response = await fetch('http://localhost:8080/api/public/consultations', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        // 3. Quan trọng: Gửi kèm Token để qua ải Security
        'Authorization': `Bearer ${token}` 
      }
    })
    
    // Kiểm tra nếu lỗi 401 (Chưa đăng nhập)
    if (response.status === 401) {
        alert("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
        return;
    }

    const data = await response.json()
    
    // Kiểm tra cấu trúc ApiResponse (code: 1000 là success)
    if (data.code === 1000) {
      consultations.value = data.result
    } else {
      console.error("Lỗi lấy dữ liệu:", data.message)
    }
  } catch (error) {
    console.error("Lỗi kết nối:", error)
  } finally {
    loading.value = false
  }
}
// Hàm format ngày tháng (DD/MM/YYYY HH:mm)
const formatDate = (dateString, includeTime = false) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  
  const options = { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit' 
  }
  
  if (includeTime) {
    options.hour = '2-digit'
    options.minute = '2-digit'
  }
  
  return date.toLocaleDateString('vi-VN', options)
}

// Hàm chọn màu sắc cho Status
const getStatusClass = (status) => {
  const baseClass = "px-3 py-1 rounded-full text-xs font-bold border "
  
  switch (status) {
    case 'Mới':
    case 'PENDING':
      return baseClass + "bg-blue-100 text-blue-700 border-blue-200"
    case 'Đã hẹn lịch':
    case 'CONFIRMED':
      return baseClass + "bg-yellow-100 text-yellow-700 border-yellow-200"
    case 'Hoàn thành':
    case 'COMPLETED':
      return baseClass + "bg-green-100 text-green-700 border-green-200"
    case 'Hủy':
    case 'CANCELLED':
      return baseClass + "bg-red-100 text-red-700 border-red-200"
    default:
      return baseClass + "bg-gray-100 text-gray-700 border-gray-200"
  }
}

// Gọi API khi component được mount
onMounted(() => {
  fetchConsultations()
})
</script>