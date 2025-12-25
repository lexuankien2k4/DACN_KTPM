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
                  <th class="p-4 whitespace-nowrap">Showroom</th>
                  <th class="p-4 whitespace-nowrap">Lịch hẹn</th>
                  <th class="p-4 whitespace-nowrap">Trạng thái</th>
                  <th class="p-4 whitespace-nowrap text-center">Hành động</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="item in consultations" :key="item.id" class="hover:bg-blue-50 transition-colors">
                  
                  <td class="p-4 align-top">
                    <span class="font-bold text-gray-700">#{{ item.id }}</span>
                    <div class="text-xs text-gray-400 mt-1">{{ formatDate(item.createdAt) }}</div>
                  </td>

                  <td class="p-4 align-top">
                    <div class="font-bold text-gray-800">{{ item.customerName }}</div>
                    <div class="text-sm text-blue-600 mt-1"><i class="fas fa-phone-alt text-xs"></i> {{ item.phoneNumber }}</div>
                  </td>

                  <td class="p-4 align-top">
                    <span class="bg-gray-100 text-gray-700 px-2 py-1 rounded text-xs font-bold border border-gray-200">
                       {{ item.carVariantName || (item.carVariant ? item.carVariant.name : 'Chưa rõ') }}
                    </span>
                  </td>

                  <td class="p-4 align-top">
                    <div class="text-sm font-medium text-gray-700">
                       <i class="fas fa-map-marker-alt text-red-500 mr-1"></i>
                       {{ item.showroomName || (item.showroom ? item.showroom.name : 'Chưa rõ') }}
                    </div>
                  </td>

                  <td class="p-4 align-top">
                    <div v-if="item.scheduledAt" class="text-sm font-bold text-blue-700">{{ formatDate(item.scheduledAt, true) }}</div>
                    <div v-else class="text-xs text-gray-400 italic">Chưa có lịch</div>
                  </td>

                  <td class="p-4 align-top">
                    <span :class="getStatusClass(item.status)">{{ item.status || 'Mới' }}</span>
                  </td>

                  <td class="p-4 align-top text-center">
                    <div class="flex items-center justify-center gap-2">
                      <button v-if="!item.status || item.status === 'Mới'" 
                              @click="receiveConsultation(item)" 
                              class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1.5 rounded-full text-xs font-bold shadow transition-all flex items-center gap-1" 
                              title="Nhận yêu cầu này">
                        <i class="fas fa-user-plus"></i> Nhận
                      </button>

                      <button @click="openDetail(item)" class="text-gray-500 hover:text-blue-600 p-2 rounded-full hover:bg-gray-100 transition-all" title="Xem chi tiết">
                        <i class="fas fa-eye text-lg"></i>
                      </button>
                      <button @click="openUpdate(item)" class="text-gray-500 hover:text-green-600 p-2 rounded-full hover:bg-gray-100 transition-all" title="Cập nhật trạng thái">
                        <i class="fas fa-edit text-lg"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <div v-if="selectedItem" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
      <div class="bg-white rounded-xl shadow-2xl max-w-lg w-full overflow-hidden animate-fade-in">
        <div class="p-6 border-b flex justify-between items-center bg-gray-50">
          <h3 class="text-xl font-bold text-gray-800">
            {{ isUpdateMode ? 'Cập nhật trạng thái' : 'Chi tiết yêu cầu' }} #{{ selectedItem.id }}
          </h3>
          <button @click="selectedItem = null" class="text-gray-400 hover:text-gray-600">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>

        <div class="p-6 space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div class="col-span-2 sm:col-span-1">
              <label class="text-xs text-gray-400 uppercase font-bold">Khách hàng</label>
              <p class="font-bold text-gray-800">{{ selectedItem.customerName }}</p>
            </div>
            <div class="col-span-2 sm:col-span-1">
              <label class="text-xs text-gray-400 uppercase font-bold">Số điện thoại</label>
              <p class="font-bold text-blue-600">{{ selectedItem.phoneNumber }}</p>
            </div>
            <div class="col-span-2">
              <label class="text-xs text-gray-400 uppercase font-bold">Email</label>
              <p class="text-gray-700">{{ selectedItem.email || 'N/A' }}</p>
            </div>
            <div class="col-span-2">
              <label class="text-xs text-gray-400 uppercase font-bold">Xe quan tâm</label>
              <p class="text-red-600 font-bold italic">{{ selectedItem.carVariantName || (selectedItem.carVariant ? selectedItem.carVariant.name : 'N/A') }}</p>
            </div>
            <div class="col-span-2">
              <label class="text-xs text-gray-400 uppercase font-bold">Ghi chú từ khách</label>
              <div class="bg-yellow-50 p-3 rounded border border-yellow-100 text-sm italic mt-1">
                "{{ selectedItem.note || 'Không có ghi chú nào' }}"
              </div>
            </div>
             <div class="col-span-2 border-t pt-2" v-if="selectedItem.staff">
              <label class="text-xs text-gray-400 uppercase font-bold">Nhân viên phụ trách</label>
              <div class="flex items-center gap-2 mt-1">
                  <span class="font-bold text-gray-800">{{ selectedItem.staff.firstName }} {{ selectedItem.staff.lastName }}</span>
                  <span class="text-xs bg-gray-100 px-2 py-0.5 rounded text-gray-600">{{ selectedItem.staff.role }}</span>
              </div>
            </div>
          </div>

          <div v-if="isUpdateMode" class="mt-4 pt-4 border-t">
            <label class="block text-sm font-bold text-gray-700 mb-2 italic">Chọn trạng thái xử lý:</label>
            <select v-model="statusUpdate" class="w-full border-2 rounded-lg p-3 focus:border-blue-500 focus:outline-none bg-white shadow-sm">
              <option value="Đã đặt cọc">Đã đặt cọc</option>
              <option value="Đang liên hệ">Đang liên hệ</option>
              <option value="Không nghe máy">Không nghe máy</option>
              <option value="Đang chờ">Mới</option>
              <option value="Đã hẹn lịch">Đã hẹn lịch</option>
              <option value="Đã hủy">Đã hủy</option>
            </select>
          </div>
        </div>

        <div class="p-6 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="selectedItem = null" class="px-5 py-2 text-gray-600 hover:bg-gray-200 rounded-lg font-medium transition-all">Đóng</button>
          <button v-if="isUpdateMode" @click="updateStatus" class="px-8 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 shadow-lg font-bold transition-all">
            Lưu thay đổi
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminSidebar from '@/components/AdminSidebar.vue'

const isSidebarOpen = ref(false)
const consultations = ref([])
const loading = ref(true)

const selectedItem = ref(null)
const isUpdateMode = ref(false)
const statusUpdate = ref('')

const fetchConsultations = async () => {
  try {
    const token = localStorage.getItem('authToken'); 
    const response = await fetch('http://localhost:8080/api/consultations', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` 
      }
    })
    
    const data = await response.json()
    if (data.code === 1000) {
      consultations.value = data.result
    }
  } catch (error) {
    console.error("Lỗi kết nối:", error)
  } finally {
    loading.value = false
  }
}

// === LOGIC MỚI: NHẬN YÊU CẦU ===
const receiveConsultation = async (item) => {
    if (!confirm(`Bạn có muốn nhận tư vấn khách hàng ${item.customerName} không?`)) return;

    try {
        const token = localStorage.getItem('authToken');
        // Gửi request cập nhật status = "Đang liên hệ"
        // Backend cần tự động lấy User ID từ token để gán vào trường staff_id của bản ghi này
        const response = await fetch(`http://localhost:8080/api/consultations/${item.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ newStatus: 'Đang liên hệ' })
        });

        if (response.ok) {
            alert("Đã nhận yêu cầu thành công!");
            fetchConsultations(); // Load lại danh sách để cập nhật UI
        } else {
            alert("Không thể nhận yêu cầu. Vui lòng thử lại.");
        }
    } catch (error) {
        console.error("Lỗi khi nhận yêu cầu:", error);
    }
}

const openDetail = (item) => {
  selectedItem.value = item
  isUpdateMode.value = false
}

const openUpdate = (item) => {
  selectedItem.value = item
  statusUpdate.value = item.status || 'Đang liên hệ'
  isUpdateMode.value = true
}

const updateStatus = async () => {
  try {
    const token = localStorage.getItem('authToken');
    const response = await fetch(`http://localhost:8080/api/consultations/${selectedItem.value.id}`, {
      method: 'PUT', 
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
     body: JSON.stringify({ newStatus: statusUpdate.value })
    })

    if (response.ok) {
      alert("Đã cập nhật trạng thái thành công!");
      selectedItem.value = null;
      fetchConsultations();
    } else {
      alert("Cập nhật thất bại, vui lòng thử lại.");
    }
  } catch (error) {
    console.error("Lỗi:", error);
  }
}

const formatDate = (dateString, includeTime = false) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
  if (includeTime) { options.hour = '2-digit'; options.minute = '2-digit' }
  return date.toLocaleDateString('vi-VN', options)
}

const getStatusClass = (status) => {
  const baseClass = "px-3 py-1 rounded-full text-xs font-bold border "
  switch (status) {
    case 'Mới': return baseClass + "bg-blue-100 text-blue-700 border-blue-200"
    case 'Đang liên hệ': return baseClass + "bg-indigo-100 text-indigo-700 border-indigo-200"
    case 'Đã hẹn lịch': return baseClass + "bg-yellow-100 text-yellow-700 border-yellow-200"
    case 'Không nghe máy': return baseClass + "bg-orange-100 text-orange-700 border-orange-200"
    case 'Đã hủy': return baseClass + "bg-red-100 text-red-700 border-red-200"
    case 'Đã đặt cọc': return baseClass + "bg-green-100 text-green-700 border-green-200"
    default: return baseClass + "bg-gray-100 text-gray-700 border-gray-200"
  }
}

onMounted(() => {
  fetchConsultations()
})
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.2s ease-out;
}
@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}
</style>