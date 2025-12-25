<template>
  <div class="flex h-screen bg-gray-100 font-sans">
    
   <AdminSidebar :isOpen="isSidebarOpen" @close="isSidebarOpen = false" />

    <main class="flex-1 flex flex-col overflow-hidden relative">
      <header class="h-20 bg-white shadow-sm flex items-center justify-between px-6 z-10">
        <div class="flex items-center">
          <button @click="isSidebarOpen = true" class="md:hidden text-gray-600 mr-4">
            <i class="fas fa-bars text-xl"></i>
          </button>
          <h1 class="text-2xl font-bold text-gray-800">Quản lý Nhân sự</h1>
        </div>
        <div class="flex items-center gap-4">
          <div class="text-right hidden sm:block">
            <p class="text-sm font-bold text-gray-700">Admin</p>
            <small class="text-gray-500">Quản trị viên</small>
          </div>
          <img src="https://ui-avatars.com/api/?name=Admin&background=0D8ABC&color=fff" alt="Admin" class="h-10 w-10 rounded-full border">
        </div>
      </header>

      <div class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-50 p-6">
        
        <div class="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4">
          <button @click="openCreateModal()" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg shadow-md font-medium flex items-center gap-2">
            <i class="fas fa-user-plus"></i> Thêm Nhân Viên
          </button>
          
          <div class="relative w-full sm:w-72">
            <input type="text" v-model="searchTerm" placeholder="Tìm kiếm tên, username..." class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none">
            <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-md overflow-hidden">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead class="bg-gray-100 text-gray-600 uppercase text-xs font-bold">
                <tr>
                  <th class="p-4 border-b">Username</th>
                  <th class="p-4 border-b">Họ và Tên</th>
                  <th class="p-4 border-b">Ngày sinh</th>
                  <th class="p-4 border-b text-center">Vai trò (Role)</th>
                  <th class="p-4 border-b text-center">Hành động</th>
                </tr>
              </thead>
              <tbody class="text-sm text-gray-700">
                <tr v-if="isLoading"><td colspan="5" class="p-8 text-center text-gray-500"><i class="fas fa-spinner fa-spin mr-2"></i> Đang tải...</td></tr>
                <tr v-else-if="filteredUsers.length === 0"><td colspan="5" class="p-8 text-center text-gray-500">Không tìm thấy dữ liệu.</td></tr>
                
                <tr v-for="user in filteredUsers" :key="user.id" class="border-b hover:bg-blue-50 transition-colors">
                  <td class="p-4 font-semibold text-blue-600">{{ user.username }}</td>
                  <td class="p-4 font-medium">{{ user.firstName }} {{ user.lastName }}</td>
                  <td class="p-4 text-gray-500">{{ user.dob }}</td>
                  
                  <td class="p-4 text-center">
                    <div class="flex justify-center gap-1 flex-wrap">
                        <span v-for="role in user.roles" :key="role.name" 
                              :class="getRoleBadgeClass(role.name)"
                              class="px-2 py-1 rounded-full text-xs font-bold border uppercase">
                            {{ role.name }}
                        </span>
                    </div>
                  </td>
                  
                  <td class="p-4 text-center">
                    <div class="flex items-center justify-center gap-2">
                        <button @click="openEditModal(user)" class="text-blue-600 hover:bg-blue-100 p-2 rounded transition-all" title="Sửa">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button @click="deleteUser(user.id)" class="text-red-600 hover:bg-red-100 p-2 rounded transition-all" title="Xóa">
                            <i class="fas fa-trash-alt"></i>
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

    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm" @click="closeModal"></div>
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-lg overflow-hidden flex flex-col relative z-10 animate-fade-in-up">
        
        <div class="px-6 py-4 border-b flex justify-between items-center bg-gray-50">
          <h2 class="text-xl font-bold text-gray-800">{{ isEditing ? 'Cập Nhật Nhân Viên' : 'Đăng Ký Tài Khoản Mới' }}</h2>
          <button @click="closeModal" class="text-gray-400 hover:text-red-500 text-2xl">&times;</button>
        </div>

        <div class="p-6">
          <form @submit.prevent="submitForm" class="space-y-4">
            
            <div>
                <label class="block text-sm font-semibold mb-1">Tên đăng nhập <span class="text-red-500">*</span></label>
                <input v-model="form.username" type="text" required :disabled="isEditing" 
                       class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none disabled:bg-gray-100 disabled:text-gray-500">
            </div>

            <div>
                <label class="block text-sm font-semibold mb-1">
                    Mật khẩu <span v-if="!isEditing" class="text-red-500">*</span>
                </label>
                <input v-model="form.password" type="password" :required="!isEditing" 
                       :placeholder="isEditing ? 'Để trống nếu không đổi mật khẩu' : ''"
                       class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-semibold mb-1">Họ (First Name)</label>
                    <input v-model="form.firstName" type="text" required class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
                </div>
                <div>
                    <label class="block text-sm font-semibold mb-1">Tên (Last Name)</label>
                    <input v-model="form.lastName" type="text" required class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
                </div>
            </div>

            <div>
                <label class="block text-sm font-semibold mb-1">Ngày sinh (DOB)</label>
                <input v-model="form.dob" type="date" required class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
            </div>

            <div class="border-t pt-4 mt-2">
                <label class="block text-sm font-bold text-gray-700 mb-2">Phân quyền (Roles)</label>
                <div class="flex flex-wrap gap-4">
                    <div v-if="availableRoles.length === 0" class="text-sm text-gray-500 italic">Đang tải danh sách quyền...</div>
                    
                    <label v-for="role in availableRoles" :key="role.name" class="flex items-center space-x-2 cursor-pointer bg-gray-50 px-3 py-2 rounded border hover:bg-blue-50 transition-colors">
                        <input type="checkbox" :value="role.name" v-model="form.roles" class="w-4 h-4 text-blue-600 rounded focus:ring-blue-500">
                        <span class="text-sm font-medium text-gray-700">{{ role.name }}</span>
                    </label>
                </div>
            </div>

            <div class="pt-4 flex justify-end gap-3 border-t mt-4">
                <button type="button" @click="closeModal" class="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100">Hủy</button>
                <button type="submit" :disabled="isLoadingSubmit" class="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 font-bold flex items-center gap-2">
                    <i v-if="isLoadingSubmit" class="fas fa-spinner fa-spin"></i>
                    {{ isLoadingSubmit ? 'Đang xử lý...' : (isEditing ? 'Lưu thay đổi' : 'Xác nhận') }}
                </button>
            </div>
          </form>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import api from '@/utils/axios'
import { useRouter } from 'vue-router'
import AdminSidebar from '@/components/AdminSidebar.vue'
const router = useRouter()
const isSidebarOpen = ref(false)
const users = ref([])
const availableRoles = ref([]) 
const showModal = ref(false)
const isEditing = ref(false)
const isLoading = ref(false)
const isLoadingSubmit = ref(false)
const searchTerm = ref('')

// Form Model
const form = reactive({
  id: null,
  username: '',
  password: '',
  firstName: '',
  lastName: '',
  dob: '',
  roles: []
})

// --- Computed ---
const filteredUsers = computed(() => {
  if (!searchTerm.value) return users.value
  const term = searchTerm.value.toLowerCase()
  return users.value.filter(u => 
    u.username.toLowerCase().includes(term) || 
    (u.firstName + ' ' + u.lastName).toLowerCase().includes(term)
  )
})

const getRoleBadgeClass = (roleName) => {
    switch(roleName) {
        case 'ADMIN': return 'bg-red-100 text-red-700 border-red-200';
        case 'STAFF': return 'bg-blue-100 text-blue-700 border-blue-200';
        case 'USER': return 'bg-green-100 text-green-700 border-green-200';
        default: return 'bg-gray-100 text-gray-600 border-gray-200';
    }
}

// --- API Calls ---
const fetchInitialData = async () => {
  isLoading.value = true
  try {
    const [resUsers, resRoles] = await Promise.all([
        api.get('/api/users'),
        api.get('/api/roles')
    ])
    
    // 🚀 LỌC DỮ LIỆU: Chỉ lấy user có role là 'STAFF'
    const allUsers = resUsers.data.result || []
    users.value = allUsers.filter(user => 
        user.roles && user.roles.some(role => role.name === 'STAFF')
    )

    availableRoles.value = resRoles.data.result || []
  } catch (error) {
    console.error("Lỗi tải dữ liệu:", error)
  } finally {
    isLoading.value = false
  }
}

const submitForm = async () => {
  isLoadingSubmit.value = true
  try {
    const payload = {
        firstName: form.firstName,
        lastName: form.lastName,
        dob: form.dob,
        roles: form.roles 
    }

    if (isEditing.value) {
      if(form.password) payload.password = form.password;
      await api.put(`/api/users/${form.id}`, payload)
      alert("Cập nhật thành công!")
    } else {
      payload.username = form.username;
      payload.password = form.password;
      await api.post('/api/users', payload)
      alert("Đăng ký tài khoản thành công!")
    }
    
    closeModal()
    fetchInitialData() // Gọi lại hàm load để cập nhật danh sách
    
  } catch (error) {
    const msg = error.response?.data?.message || error.message
    alert("Thất bại: " + msg)
  } finally {
    isLoadingSubmit.value = false
  }
}

const deleteUser = async (id) => {
  if (!confirm("Bạn có chắc muốn xóa nhân viên này?")) return
  try {
    await api.delete(`/api/users/${id}`)
    fetchInitialData()
  } catch (error) {
    alert("Lỗi khi xóa: " + (error.response?.data?.message || error.message))
  }
}

// --- Modal Actions ---
const openCreateModal = () => {
  isEditing.value = false
  Object.assign(form, { 
      id: null, 
      username: '', 
      password: '', 
      firstName: '', 
      lastName: '', 
      dob: '',
      roles: ['STAFF'] // 🚀 MẶC ĐỊNH CHỌN STAFF
  })
  showModal.value = true
}

const openEditModal = (user) => {
  isEditing.value = true
  Object.assign(form, {
    id: user.id,
    username: user.username,
    password: '',
    firstName: user.firstName,
    lastName: user.lastName,
    dob: user.dob,
    roles: user.roles ? user.roles.map(r => r.name) : []
  })
  showModal.value = true
}

const closeModal = () => showModal.value = false

const handleLogout = () => {
  localStorage.removeItem('authToken')
  router.push('/login')
}

onMounted(fetchInitialData)
</script>

<style scoped>
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fadeInUp 0.3s ease-out forwards;
}
</style>