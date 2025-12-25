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
            <p class="text-gray-500">Đang tải dữ liệu...</p>
        </div>
        <div v-else-if="deposits.length === 0" class="bg-white rounded-xl shadow-md p-6 flex flex-col items-center justify-center h-64 text-gray-500">
            <i class="fas fa-file-invoice-dollar text-4xl mb-3"></i>
            <p>Chưa có đơn đặt cọc nào.</p>
        </div>
        <div v-else class="bg-white rounded-xl shadow-md overflow-hidden">
            <table class="min-w-full leading-normal">
                <thead>
                    <tr>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Mã Đơn
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Khách Hàng (User ID)
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Xe (Variant ID) / Màu
                        </th>
                         <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Số Tiền Cọc
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Ngày Tạo
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Trạng Thái
                        </th>
                        <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            Hành Động
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in deposits" :key="item.id">
                        <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <p class="text-gray-900 whitespace-no-wrap font-bold">#{{ item.id }}</p>
                            <p class="text-xs text-gray-500">{{ item.paymentMethod }}</p>
                        </td>
                        <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <p class="text-gray-900 whitespace-no-wrap font-bold">{{ item.userId }}</p>
                            </td>
                         <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <p class="text-gray-900 whitespace-no-wrap">ID Xe: {{ item.variantId }}</p>
                            <p class="text-xs text-gray-500">Màu: {{ item.selectedColor }}</p>
                        </td>
                        <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <p class="text-blue-600 font-bold whitespace-no-wrap">{{ formatCurrency(item.amount) }}</p>
                        </td>
                         <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <p class="text-gray-900 whitespace-no-wrap">{{ formatDate(item.createdAt) }}</p>
                        </td>
                        <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <span :class="['relative inline-block px-3 py-1 font-semibold leading-tight rounded-full', statusColor(item.status)]">
                                <span class="relative">{{ item.status }}</span>
                            </span>
                        </td>
                        <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                            <div class="flex gap-2">
                                <button v-if="item.status === 'PAID'" @click="updateStatus(item.id, 'APPROVED')" class="text-green-600 hover:text-green-900 font-bold text-xs border border-green-600 px-2 py-1 rounded hover:bg-green-50">Duyệt</button>
                                
                                <button v-if="['PENDING', 'PAID'].includes(item.status)" @click="updateStatus(item.id, 'REJECTED')" class="text-red-600 hover:text-red-900 font-bold text-xs border border-red-600 px-2 py-1 rounded hover:bg-red-50">Hủy</button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminSidebar from '@/components/AdminSidebar.vue'

const isSidebarOpen = ref(false)
const deposits = ref([])
const loading = ref(true)

const formatCurrency = (value) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
}

const formatDate = (dateString) => {
    if(!dateString) return ''
    return new Date(dateString).toLocaleString('vi-VN')
}

const statusColor = (status) => {
    switch (status) {
        case 'PAID': return 'text-blue-900 bg-blue-200'; // Đã thanh toán
        case 'APPROVED': return 'text-green-900 bg-green-200'; // Đã duyệt
        case 'REJECTED': return 'text-red-900 bg-red-200'; // Đã hủy
        case 'PENDING': return 'text-yellow-900 bg-yellow-200'; // Chờ thanh toán
        default: return 'text-gray-900 bg-gray-200';
    }
}

const fetchDeposits = async () => {
    loading.value = true
    try {
        // Gọi API Backend mới
        const response = await fetch('http://localhost:8080/api/deposits')
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
        const response = await fetch(`http://localhost:8080/api/deposits/${id}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'text/plain' }, 
            body: status
        })

        const data = await response.json()

        if (data.code === 1000) {
            alert('Cập nhật thành công!')
            await fetchDeposits() // Tải lại danh sách
        } else {
            alert('Cập nhật thất bại')
        }
    } catch (e) {
         console.error("Update status error", e)
         alert('Có lỗi xảy ra')
    }
}

onMounted(() => {
    fetchDeposits()
})
</script>