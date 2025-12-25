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
            <h1 class="text-2xl font-bold text-gray-800">Khách hàng Đã Đặt Cọc</h1>
            <p class="text-xs text-gray-500">Danh sách khách hàng đã chốt đơn thành công</p>
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
                  <th class="p-4">Thông tin khách hàng</th>
                  <th class="p-4">Xe đã chọn</th>
                  <th class="p-4">Showroom</th>
                  <th class="p-4">Nhân viên phụ trách</th>
                  <th class="p-4">Trạng thái</th>
                  <th class="p-4 text-center">Hành động</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="customer in depositedCustomers" :key="customer.id" class="hover:bg-green-50 transition-colors">
                  <td class="p-4">
                    <div class="font-bold text-gray-800">{{ customer.customerName }}</div>
                    <div class="text-sm text-blue-600 mt-1">
                      <i class="fas fa-phone-alt text-xs mr-1"></i> {{ customer.phoneNumber }}
                    </div>
                    <div class="text-xs text-gray-500 mt-1">{{ customer.email }}</div>
                  </td>

                  <td class="p-4">
                    <span class="bg-blue-100 text-blue-700 px-2 py-1 rounded text-xs font-bold whitespace-nowrap">
                      {{ customer.carVariantName || 'N/A' }}
                    </span>
                  </td>

                  <td class="p-4">
                    <div class="font-medium text-gray-700 text-sm">
                        <i class="fas fa-store text-red-500 mr-1"></i> 
                        {{ customer.showroomName || 'Chưa xác định' }}
                    </div>
                    <div class="text-xs text-gray-400 mt-1">{{ customer.province }}</div>
                  </td>

                  <td class="p-4">
                    <div v-if="customer.staff" class="flex items-center gap-3">
                        <div class="w-9 h-9 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-bold text-xs border border-indigo-200">
                            {{ getInitials(customer.staff.firstName, customer.staff.lastName) }}
                        </div>
                        <div>
                            <div class="text-sm font-bold text-gray-800">
                                {{ customer.staff.firstName }} {{ customer.staff.lastName }}
                            </div>
                            <div class="mt-0.5">
                                <span :class="getRoleBadge(customer.staff.role)">
                                    {{ customer.staff.role }}
                                </span>
                            </div>
                        </div>
                    </div>
                    <div v-else class="text-xs text-gray-400 italic bg-gray-50 px-2 py-1 rounded inline-block">
                        <i class="fas fa-user-slash mr-1"></i> Chưa phân công
                    </div>
                  </td>

                  <td class="p-4">
                    <span class="px-3 py-1 rounded-full text-xs font-bold bg-green-100 text-green-700 border border-green-200 whitespace-nowrap">
                      {{ customer.status }}
                    </span>
                  </td>

                  <td class="p-4 text-center">
                    <button @click="viewDetails(customer)" class="text-blue-600 hover:text-blue-800 font-bold text-sm bg-blue-50 px-3 py-1.5 rounded-lg border border-blue-200 transition-all">
                      <i class="fas fa-file-contract mr-1"></i> Hợp đồng
                    </button>
                  </td>
                </tr>

                <tr v-if="depositedCustomers.length === 0">
                  <td colspan="6" class="p-12 text-center text-gray-500">
                    <i class="fas fa-users-slash text-4xl mb-3 text-gray-300"></i>
                    <p>Chưa có khách hàng nào đặt cọc.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <div v-if="selectedCustomer" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
      <div class="bg-white rounded-xl shadow-2xl max-w-2xl w-full overflow-hidden animate-fade-in">
        <div class="p-6 border-b flex justify-between items-center bg-gray-50">
          <div>
              <h3 class="text-xl font-bold text-gray-800">Thông tin Hợp đồng</h3>
              <p class="text-xs text-gray-500">Mã tham chiếu: #{{ selectedCustomer.id }}</p>
          </div>
          <button @click="selectedCustomer = null" class="text-gray-400 hover:text-gray-600 transition-colors">
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>

        <div class="p-6 overflow-y-auto max-h-[70vh]">
          <div class="grid grid-cols-2 gap-6">
              <div class="col-span-2 md:col-span-1 space-y-4">
                  <h4 class="font-bold text-blue-700 border-b pb-2 uppercase text-xs">Bên mua xe (Khách hàng)</h4>
                  
                  <div>
                      <label class="block text-xs font-bold text-gray-500 uppercase">Họ và tên</label>
                      <p class="text-gray-800 font-medium text-lg">{{ selectedCustomer.customerName }}</p>
                  </div>
                  <div>
                      <label class="block text-xs font-bold text-gray-500 uppercase">Số điện thoại</label>
                      <p class="text-gray-800">{{ selectedCustomer.phoneNumber }}</p>
                  </div>
                  <div>
                      <label class="block text-xs font-bold text-gray-500 uppercase">Email</label>
                      <p class="text-gray-800">{{ selectedCustomer.email || 'Chưa cung cấp' }}</p>
                  </div>
              </div>

              <div class="col-span-2 md:col-span-1 space-y-4">
                  <h4 class="font-bold text-blue-700 border-b pb-2 uppercase text-xs">Thông tin Xe & Đại lý</h4>
                  
                  <div>
                      <label class="block text-xs font-bold text-gray-500 uppercase">Dòng xe</label>
                      <p class="text-red-600 font-bold text-lg">{{ selectedCustomer.carVariantName || 'Chưa chọn' }}</p>
                  </div>
                  <div>
                      <label class="block text-xs font-bold text-gray-500 uppercase">Showroom</label>
                      <p class="text-gray-800"><i class="fas fa-map-marker-alt text-red-500 mr-1"></i> {{ selectedCustomer.showroomName || 'Chưa xác định' }}</p>
                  </div>
              </div>

              <div class="col-span-2 bg-gray-50 p-4 rounded-lg border border-gray-200 mt-2">
                  <h4 class="font-bold text-gray-600 uppercase text-xs mb-3">Nhân viên thực hiện hợp đồng</h4>
                  <div class="flex items-center gap-4" v-if="selectedCustomer.staff">
                       <div class="w-12 h-12 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold text-lg shadow-md">
                          {{ getInitials(selectedCustomer.staff.firstName, selectedCustomer.staff.lastName) }}
                       </div>
                       <div>
                          <p class="font-bold text-gray-800 text-lg">{{ selectedCustomer.staff.firstName }} {{ selectedCustomer.staff.lastName }}</p>
                          <span :class="getRoleBadge(selectedCustomer.staff.role)">{{ selectedCustomer.staff.role }}</span>
                       </div>
                  </div>
                  <div v-else class="text-gray-500 italic text-sm">Chưa có nhân viên phụ trách</div>
              </div>
          </div>
        </div>

        <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="selectedCustomer = null" class="px-5 py-2.5 bg-white border border-gray-300 text-gray-700 font-medium rounded-lg hover:bg-gray-100 transition-all shadow-sm">
            Đóng
          </button>
          <button class="px-6 py-2.5 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 shadow-md transition-all flex items-center gap-2">
            <i class="fas fa-print"></i> Xuất file PDF
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import AdminSidebar from '@/components/AdminSidebar.vue'

const isSidebarOpen = ref(false)
const allConsultations = ref([])
const loading = ref(true)
const selectedCustomer = ref(null) // State để lưu khách hàng đang xem chi tiết

// Dữ liệu lọc: Chỉ lấy khách đã đặt cọc
const depositedCustomers = computed(() => {
  return allConsultations.value.filter(item => 
    item.status === 'Đã đặt cọc' || 
    item.status === 'PAID' || 
    item.status === 'APPROVED'
  )
})

const fetchCustomers = async () => {
  try {
    const token = localStorage.getItem('authToken');
    const response = await fetch('http://localhost:8080/api/customers-with-staff', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    const data = await response.json();
    if (data.code === 1000) {
      allConsultations.value = data.result;
    }
  } catch (error) {
    console.error("Lỗi khi tải danh sách khách hàng:", error);
  } finally {
    loading.value = false;
  }
}

const getInitials = (first, last) => {
    const f = first ? first.charAt(0) : '';
    const l = last ? last.charAt(0) : '';
    return (f + l).toUpperCase();
}

const getRoleBadge = (role) => {
    switch (role) {
        case 'ADMIN': 
            return 'bg-red-100 text-red-700 text-[10px] uppercase font-bold px-1.5 py-0.5 rounded border border-red-200';
        case 'MANAGER': 
            return 'bg-purple-100 text-purple-700 text-[10px] uppercase font-bold px-1.5 py-0.5 rounded border border-purple-200';
        case 'STAFF': 
            return 'bg-blue-100 text-blue-700 text-[10px] uppercase font-bold px-1.5 py-0.5 rounded border border-blue-200';
        default: 
            return 'bg-gray-100 text-gray-600 text-[10px] uppercase font-bold px-1.5 py-0.5 rounded border border-gray-200';
    }
}

// Hàm mở Modal chi tiết
const viewDetails = (customer) => {
  selectedCustomer.value = customer;
}

onMounted(() => {
  fetchCustomers();
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