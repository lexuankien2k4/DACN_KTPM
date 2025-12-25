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
            <h1 class="text-2xl font-bold text-gray-800">Quản lý Đặt cọc</h1>
            <p class="text-xs text-gray-500">Danh sách các đơn cọc xe từ khách hàng</p>
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
        <div v-if="loading" class="text-center py-10">
            <i class="fas fa-circle-notch fa-spin text-4xl text-blue-600"></i>
            <p class="text-gray-500 mt-2">Đang tải dữ liệu...</p>
        </div>
        <div v-else-if="deposits.length === 0" class="bg-white rounded-xl shadow-md p-6 flex flex-col items-center justify-center h-64 text-gray-500">
            <i class="fas fa-file-invoice-dollar text-4xl mb-3"></i>
            <p>Chưa có đơn đặt cọc nào.</p>
        </div>
        <div v-else class="bg-white rounded-xl shadow-md overflow-hidden border border-gray-100">
            <table class="min-w-full leading-normal">
                <thead>
                    <tr>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                            Mã Đơn
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                            Khách Hàng
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                            Thông tin Xe
                        </th>
                         <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                            Số Tiền Cọc
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                            Trạng Thái
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-center text-xs font-bold text-gray-600 uppercase tracking-wider">
                            Hành Động
                        </th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    <tr v-for="item in deposits" :key="item.id" class="hover:bg-blue-50 transition-colors">
                        <td class="px-5 py-5 bg-white text-sm align-top">
                            <p class="text-gray-900 font-bold">#{{ item.id }}</p>
                            <p class="text-xs text-gray-400 mt-1">{{ formatDate(item.createdAt) }}</p>
                            <span class="text-[10px] bg-gray-100 px-2 py-0.5 rounded text-gray-500 mt-1 inline-block">{{ item.paymentMethod }}</span>
                        </td>
                        <td class="px-5 py-5 bg-white text-sm align-top">
                            <p class="text-gray-900 font-bold">{{ item.customerName || item.userId }}</p>
                            <p class="text-xs text-blue-600 mt-0.5">{{ item.phoneNumber }}</p>
                        </td>
                         <td class="px-5 py-5 bg-white text-sm align-top">
                            <p class="text-gray-900 font-medium">{{ item.carVariantName || ('ID: ' + item.variantId) }}</p>
                            <div class="flex items-center gap-1 mt-1">
                                <span class="w-3 h-3 rounded-full border border-gray-300" :style="{ backgroundColor: getColorHex(item.selectedColor) }"></span>
                                <span class="text-xs text-gray-500">{{ item.selectedColor }}</span>
                            </div>
                            <p class="text-xs text-gray-400 mt-0.5"><i class="fas fa-map-marker-alt"></i> {{ item.showroomName }}</p>
                        </td>
                        <td class="px-5 py-5 bg-white text-sm align-top">
                            <p class="text-blue-700 font-bold">{{ formatCurrency(item.amount) }}</p>
                        </td>
                        <td class="px-5 py-5 bg-white text-sm align-top">
                            <span :class="['px-3 py-1 font-semibold text-xs rounded-full border', statusColor(item.status)]">
                                {{ item.status }}
                            </span>
                        </td>
                        <td class="px-5 py-5 bg-white text-sm text-center align-top">
                            <div class="flex justify-center gap-2">
                                <button v-if="item.status === 'PAID'" @click="updateStatus(item.id, 'APPROVED')" class="bg-green-100 text-green-700 border border-green-200 hover:bg-green-200 px-3 py-1.5 rounded-lg text-xs font-bold transition-all" title="Duyệt đơn hàng">
                                    <i class="fas fa-check mr-1"></i> Duyệt
                                </button>
                                
                                <button v-if="['PENDING', 'PAID'].includes(item.status)" @click="updateStatus(item.id, 'REJECTED')" class="bg-red-100 text-red-700 border border-red-200 hover:bg-red-200 px-3 py-1.5 rounded-lg text-xs font-bold transition-all" title="Hủy đơn hàng">
                                    <i class="fas fa-times mr-1"></i> Hủy
                                </button>

                                <button v-if="['APPROVED', 'REJECTED'].includes(item.status)" @click="viewDetails(item)" class="bg-blue-100 text-blue-700 border border-blue-200 hover:bg-blue-200 px-3 py-1.5 rounded-lg text-xs font-bold transition-all" title="Xem chi tiết">
                                    <i class="fas fa-eye mr-1"></i> Chi tiết
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
      </div>
    </main>

    <div v-if="selectedDeposit" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
        <div class="bg-white rounded-xl shadow-2xl max-w-2xl w-full overflow-hidden animate-fade-in flex flex-col max-h-[90vh]">
            <div class="p-5 border-b flex justify-between items-center bg-gray-50">
                <div>
                    <h3 class="text-xl font-bold text-gray-800">Chi tiết Đơn đặt cọc</h3>
                    <p class="text-xs text-gray-500">Mã đơn: #{{ selectedDeposit.id }}</p>
                </div>
                <button @click="selectedDeposit = null" class="text-gray-400 hover:text-gray-600 transition-colors">
                    <i class="fas fa-times text-2xl"></i>
                </button>
            </div>

            <div class="p-6 overflow-y-auto">
                <div class="mb-6 flex justify-center">
                    <span :class="['px-6 py-2 rounded-full font-bold border-2 text-lg', statusColor(selectedDeposit.status)]">
                        {{ selectedDeposit.status }}
                    </span>
                </div>

                <div class="grid grid-cols-2 gap-6">
                    <div class="col-span-2 md:col-span-1 space-y-3">
                        <h4 class="font-bold text-blue-700 uppercase text-xs border-b pb-1 mb-2">Khách hàng</h4>
                        <div>
                            <label class="text-xs text-gray-500 font-bold block">Họ tên</label>
                            <p class="text-gray-800 font-medium">{{ selectedDeposit.customerName || selectedDeposit.userId }}</p>
                        </div>
                        <div>
                            <label class="text-xs text-gray-500 font-bold block">Số điện thoại</label>
                            <p class="text-gray-800">{{ selectedDeposit.phoneNumber || '---' }}</p>
                        </div>
                        <div>
                            <label class="text-xs text-gray-500 font-bold block">Email</label>
                            <p class="text-gray-800">{{ selectedDeposit.email || '---' }}</p>
                        </div>
                    </div>

                    <div class="col-span-2 md:col-span-1 space-y-3">
                        <h4 class="font-bold text-blue-700 uppercase text-xs border-b pb-1 mb-2">Sản phẩm</h4>
                        <div>
                            <label class="text-xs text-gray-500 font-bold block">Dòng xe</label>
                            <p class="text-red-600 font-bold">{{ selectedDeposit.carVariantName || selectedDeposit.variantId }}</p>
                        </div>
                        <div>
                            <label class="text-xs text-gray-500 font-bold block">Màu sắc</label>
                            <div class="flex items-center gap-2">
                                <span class="w-4 h-4 rounded-full border border-gray-300 shadow-sm" :style="{ backgroundColor: getColorHex(selectedDeposit.selectedColor) }"></span>
                                <span class="text-gray-800">{{ selectedDeposit.selectedColor }}</span>
                            </div>
                        </div>
                        <div>
                            <label class="text-xs text-gray-500 font-bold block">Showroom</label>
                            <p class="text-gray-800 text-sm"><i class="fas fa-map-marker-alt text-red-500 mr-1"></i> {{ selectedDeposit.showroomName }}</p>
                            <p class="text-xs text-gray-500 ml-4">{{ selectedDeposit.province }}</p>
                        </div>
                    </div>

                    <div class="col-span-2 bg-gray-50 p-4 rounded-lg border border-gray-200">
                        <h4 class="font-bold text-gray-600 uppercase text-xs mb-3">Thông tin thanh toán</h4>
                        <div class="grid grid-cols-2 gap-4">
                            <div>
                                <label class="text-xs text-gray-500 font-bold block">Số tiền cọc</label>
                                <p class="text-blue-700 font-bold text-xl">{{ formatCurrency(selectedDeposit.amount) }}</p>
                            </div>
                            <div>
                                <label class="text-xs text-gray-500 font-bold block">Phương thức</label>
                                <p class="text-gray-800 font-medium">{{ selectedDeposit.paymentMethod }}</p>
                            </div>
                            <div>
                                <label class="text-xs text-gray-500 font-bold block">Ngày tạo đơn</label>
                                <p class="text-gray-800">{{ formatDate(selectedDeposit.createdAt, true) }}</p>
                            </div>
                             <div>
                                <label class="text-xs text-gray-500 font-bold block">Ngày cập nhật</label>
                                <p class="text-gray-800">{{ formatDate(selectedDeposit.updatedAt, true) }}</p>
                            </div>
                            <div class="col-span-2">
                                <label class="text-xs text-gray-500 font-bold block">Ghi chú từ khách</label>
                                <p class="text-gray-700 italic bg-white p-2 rounded border border-dashed border-gray-300 text-sm">
                                    "{{ selectedDeposit.note || 'Không có ghi chú' }}"
                                </p>
                            </div>
                        </div>
                    </div>

                    <div v-if="selectedDeposit.staff" class="col-span-2 border-t pt-3">
                        <label class="text-xs text-gray-400 uppercase font-bold">Nhân viên xử lý</label>
                        <div class="flex items-center gap-3 mt-2">
                            <div class="w-10 h-10 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-bold border border-indigo-200">
                                {{ getInitials(selectedDeposit.staff.firstName, selectedDeposit.staff.lastName) }}
                            </div>
                            <div>
                                <p class="font-bold text-gray-800">{{ selectedDeposit.staff.firstName }} {{ selectedDeposit.staff.lastName }}</p>
                                <span class="text-xs bg-gray-100 text-gray-600 px-2 py-0.5 rounded">{{ selectedDeposit.staff.role }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
                <button @click="selectedDeposit = null" class="px-5 py-2.5 bg-white border border-gray-300 text-gray-700 font-medium rounded-lg hover:bg-gray-100 transition-all shadow-sm">
                    Đóng
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
const deposits = ref([])
const loading = ref(true)
const selectedDeposit = ref(null) // Biến lưu đơn hàng đang xem chi tiết

const formatCurrency = (value) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
}

const formatDate = (dateString, includeTime = false) => {
    if(!dateString) return ''
    const date = new Date(dateString)
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
    if(includeTime) { options.hour = '2-digit'; options.minute = '2-digit'; }
    return date.toLocaleString('vi-VN', options)
}

const statusColor = (status) => {
    switch (status) {
        case 'PAID': return 'text-blue-700 bg-blue-100 border-blue-200'; // Đã thanh toán
        case 'APPROVED': return 'text-green-700 bg-green-100 border-green-200'; // Đã duyệt
        case 'REJECTED': return 'text-red-700 bg-red-100 border-red-200'; // Đã hủy
        case 'PENDING': return 'text-yellow-700 bg-yellow-100 border-yellow-200'; // Chờ thanh toán
        default: return 'text-gray-700 bg-gray-100 border-gray-200';
    }
}

// Helper lấy mã màu Hex để hiển thị ô màu
const getColorHex = (colorName) => {
    if(!colorName) return '#ccc';
    const lower = colorName.toLowerCase();
    if(lower.includes('white')) return '#ffffff';
    if(lower.includes('red')) return '#dc2626';
    if(lower.includes('grey') || lower.includes('gray')) return '#6b7280';
    if(lower.includes('blue')) return '#2563eb';
    if(lower.includes('yellow') || lower.includes('orange')) return '#fbbf24';
    if(lower.includes('black')) return '#000000';
    return '#ccc';
}

const getInitials = (first, last) => {
    const f = first ? first.charAt(0) : '';
    const l = last ? last.charAt(0) : '';
    return (f + l).toUpperCase();
}

const fetchDeposits = async () => {
    loading.value = true
    try {
        const token = localStorage.getItem('authToken');
        // Gọi API lấy danh sách
        const response = await fetch('http://localhost:8080/api/deposits', {
             headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        const data = await response.json()
        
        if (data.code === 1000) {
            deposits.value = data.result
            // Sắp xếp mới nhất lên đầu
            deposits.value.sort((a, b) => b.id - a.id)
        }
    } catch (e) {
        console.error("Fetch deposits error", e)
    } finally {
        loading.value = false
    }
}

const updateStatus = async (id, status) => {
    let actionName = status === 'APPROVED' ? 'DUYỆT' : 'HỦY';
    if (!confirm(`Bạn có chắc chắn muốn ${actionName} đơn hàng #${id}?`)) return;

    try {
        const token = localStorage.getItem('authToken')
        const response = await fetch(`http://localhost:8080/api/deposits/${id}/status`, {
            method: 'PUT',
            headers: { 
                'Content-Type': 'text/plain',
                'Authorization': `Bearer ${token}`
            }, 
            body: status
        })

        const data = await response.json()

        if (data.code === 1000) {
            alert('Cập nhật thành công!')
            await fetchDeposits() // Tải lại danh sách để cập nhật giao diện
        } else {
            alert('Cập nhật thất bại: ' + data.message)
        }
    } catch (e) {
         console.error("Update status error", e)
         alert('Có lỗi xảy ra khi kết nối server')
    }
}

// Hàm mở modal chi tiết
const viewDetails = (item) => {
    selectedDeposit.value = item;
}

onMounted(() => {
    fetchDeposits()
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