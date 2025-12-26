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
            <h1 class="text-2xl font-bold text-gray-800">Quản lý Hợp đồng</h1>
            <p class="text-xs text-gray-500">Tạo và quản lý hợp đồng mua bán xe</p>
          </div>
        </div>
        
        <div class="flex items-center gap-4">
           <button @click="openCreateModal" class="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 text-sm font-bold mr-4 hidden sm:block transition-all">
              <i class="fas fa-plus mr-1"></i> Tạo Hợp đồng Mới
           </button>

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
         <div class="text-center text-gray-500 mt-20">
            <i class="fas fa-file-contract text-6xl mb-4 text-gray-300"></i>
            <p class="text-lg">Danh sách hợp đồng sẽ hiển thị tại đây</p>
         </div>
      </div>
    </main>

    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
        <div class="bg-white rounded-xl shadow-2xl w-full max-w-4xl overflow-hidden animate-fade-in flex flex-col max-h-[90vh]">
            
            <div class="p-5 border-b flex justify-between items-center bg-gray-50">
                <h3 class="text-xl font-bold text-blue-800">Soạn thảo Hợp đồng Mới</h3>
                <button @click="showModal = false" class="text-gray-400 hover:text-gray-600">
                    <i class="fas fa-times text-2xl"></i>
                </button>
            </div>

            <div class="p-8 overflow-y-auto">
                <form @submit.prevent="submitContract">
                    
                    <div class="mb-8 bg-blue-50 p-4 rounded-lg border border-blue-200">
                        <label class="block text-sm font-bold text-blue-800 mb-2">
                            <i class="fas fa-search mr-1"></i> Chọn Khách hàng / Đơn đặt cọc (Đã duyệt)
                        </label>
                        <select 
                            v-model="selectedDepositId" 
                            class="w-full p-3 border border-blue-300 rounded focus:ring-2 focus:ring-blue-500 outline-none bg-white font-medium"
                            required
                        >
                            <option value="" disabled>-- Vui lòng chọn đơn cọc --</option>
                            <option v-for="dep in eligibleDeposits" :key="dep.id" :value="dep.id">
                                #{{ dep.id }} - {{ dep.customerName }} - {{ dep.carName }} ({{ formatCurrency(dep.amount) }})
                            </option>
                        </select>
                        <p v-if="eligibleDeposits.length === 0" class="text-xs text-red-500 mt-2 italic">
                            * Không có đơn cọc nào ở trạng thái APPROVED để tạo hợp đồng.
                        </p>
                    </div>

                    <div v-if="contractForm.customerName" class="grid grid-cols-2 gap-8 animate-fade-in">
                        
                        <div class="space-y-4">
                            <h4 class="font-bold text-gray-700 border-b pb-2 uppercase text-xs">I. Bên Mua (Thông tin tự động)</h4>
                            <div>
                                <label class="block text-xs font-bold text-gray-500">Họ và tên khách hàng</label>
                                <input type="text" v-model="contractForm.customerName" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-gray-700 font-bold">
                            </div>
                            <div class="grid grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-xs font-bold text-gray-500">Số điện thoại</label>
                                    <input type="text" v-model="contractForm.phone" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-gray-700">
                                </div>
                                <div>
                                    <label class="block text-xs font-bold text-gray-500">Số CCCD/CMND</label>
                                    <input type="text" v-model="contractForm.citizenId" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-gray-700">
                                </div>
                            </div>
                            <div>
                                <label class="block text-xs font-bold text-gray-500">Email</label>
                                <input type="text" v-model="contractForm.email" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-gray-700">
                            </div>
                        </div>

                        <div class="space-y-4">
                            <h4 class="font-bold text-gray-700 border-b pb-2 uppercase text-xs">II. Đối tượng Hợp đồng</h4>
                            <div>
                                <label class="block text-xs font-bold text-gray-500">Dòng xe</label>
                                <input type="text" v-model="contractForm.carName" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-red-600 font-bold">
                            </div>
                            <div class="grid grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-xs font-bold text-gray-500">Màu sắc</label>
                                    <input type="text" v-model="contractForm.color" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-gray-700">
                                </div>
                                <div>
                                    <label class="block text-xs font-bold text-gray-500">Showroom giao xe</label>
                                    <input type="text" v-model="contractForm.showroom" readonly class="w-full mt-1 p-2 bg-gray-100 border rounded text-gray-700">
                                </div>
                            </div>
                            
                            <div class="bg-yellow-50 p-4 rounded border border-yellow-200">
                                <label class="block text-xs font-bold text-gray-600 mb-1">Giá trị hợp đồng (VNĐ)</label>
                                <input 
                                    type="number" 
                                    v-model="contractForm.finalPrice" 
                                    class="w-full p-2 border border-yellow-300 rounded font-bold text-lg text-blue-800 focus:ring-2 focus:ring-yellow-500 outline-none"
                                >
                                <p class="text-xs text-gray-500 mt-1 italic">
                                    * Đã bao gồm VAT. (Đã trừ cọc: {{ formatCurrency(contractForm.depositAmount) }})
                                </p>
                            </div>
                        </div>

                        <div class="col-span-2 space-y-4 mt-2">
                             <h4 class="font-bold text-gray-700 border-b pb-2 uppercase text-xs">III. Điều khoản & Thời hạn</h4>
                             <div class="grid grid-cols-2 gap-8">
                                 <div>
                                     <label class="block text-xs font-bold text-gray-500 mb-1">Số Hợp đồng</label>
                                     <input type="text" v-model="contractForm.contractNumber" class="w-full p-2 border rounded font-mono uppercase focus:ring-blue-500 outline-none" placeholder="VD: HĐMB-2025-001" required>
                                 </div>
                                 <div>
                                     <label class="block text-xs font-bold text-gray-500 mb-1">Ngày ký (Dự kiến)</label>
                                     <input type="date" class="w-full p-2 border rounded text-gray-700">
                                 </div>
                             </div>
                        </div>

                    </div>
                </form>
            </div>

            <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
                <button @click="showModal = false" class="px-5 py-2.5 bg-white border border-gray-300 text-gray-700 font-medium rounded-lg hover:bg-gray-100 transition-all">
                    Hủy bỏ
                </button>
                <button 
                    @click="submitContract" 
                    :disabled="!selectedDepositId || loading"
                    class="px-6 py-2.5 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 shadow-md transition-all flex items-center gap-2 disabled:opacity-50"
                >
                    <i v-if="loading" class="fas fa-spinner fa-spin"></i>
                    <i v-else class="fas fa-file-signature"></i>
                    Lưu Hợp Đồng
                </button>
            </div>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import AdminSidebar from '@/components/AdminSidebar.vue'

const isSidebarOpen = ref(false)
const showModal = ref(false)
const loading = ref(false)

const allDeposits = ref([])
const eligibleDeposits = ref([]) // Danh sách cọc Approved
const selectedDepositId = ref('')

// Form dữ liệu Hợp đồng
const contractForm = reactive({
    contractNumber: '',
    customerName: '',
    phone: '',
    email: '',
    citizenId: '',
    carName: '',
    color: '',
    showroom: '',
    depositAmount: 0,
    finalPrice: 0 // Giá chốt
})

const formatCurrency = (val) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val)

// 1. Hàm lấy danh sách đơn cọc từ Backend
const fetchDeposits = async () => {
    try {
        const token = localStorage.getItem('authToken');
        const res = await fetch('http://localhost:8080/api/deposits', {
            headers: { 'Authorization': `Bearer ${token}` }
        })
        const data = await res.json()
        if (data.code === 1000) {
            allDeposits.value = data.result
            // Chỉ lấy các đơn APPROVED để làm hợp đồng
            eligibleDeposits.value = allDeposits.value.filter(d => d.status === 'APPROVED')
        }
    } catch (e) {
        console.error(e)
    }
}

// 2. Mở Modal và tải dữ liệu
const openCreateModal = () => {
    showModal.value = true
    fetchDeposits()
    // Reset form
    selectedDepositId.value = ''
    Object.keys(contractForm).forEach(key => contractForm[key] = '')
    contractForm.contractNumber = `HD-${new Date().getTime().toString().slice(-6)}` // Tự sinh mã ngẫu nhiên
}

// 3. Tự động điền (Auto-fill) khi chọn Đơn cọc
watch(selectedDepositId, (newId) => {
    const selected = eligibleDeposits.value.find(d => d.id === newId)
    if (selected) {
        // Map dữ liệu từ Deposit API vào Form Hợp đồng
        contractForm.customerName = selected.customerName || selected.userId
        contractForm.phone = selected.customerPhone || selected.phoneNumber
        contractForm.email = selected.email
        contractForm.citizenId = selected.citizenId || selected.cccd
        
        contractForm.carName = selected.carName || 'VinFast VF'
        contractForm.color = selected.selectedColor
        contractForm.showroom = selected.showroomName
        contractForm.depositAmount = selected.amount
        
        // Giả lập giá xe (Ở thực tế nên lấy từ API car_variant)
        // Ví dụ tạm: Giá xe = Tiền cọc * 10 (Logic tạm để hiển thị)
        contractForm.finalPrice = selected.amount * 10 + 500000000 
    }
})

// 4. Gửi yêu cầu tạo hợp đồng
const submitContract = async () => {
    if(!selectedDepositId.value) return alert("Vui lòng chọn đơn cọc!")
    
    loading.value = true
    try {
        const token = localStorage.getItem('authToken');
        const payload = {
            depositId: selectedDepositId.value,
            contractNumber: contractForm.contractNumber,
            finalPrice: contractForm.finalPrice,
            status: 'DRAFT'
        }

        const res = await fetch('http://localhost:8080/api/contracts', {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        })

        const data = await res.json()
        if (data.code === 1000) {
            alert("Tạo hợp đồng thành công!")
            showModal.value = false
            // Có thể reload lại list hợp đồng ở đây
        } else {
            alert("Lỗi: " + data.message)
        }
    } catch (e) {
        console.error(e)
        alert("Lỗi kết nối Server")
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.3s ease-out;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>