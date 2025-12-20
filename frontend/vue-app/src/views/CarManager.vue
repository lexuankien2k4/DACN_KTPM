<template>
  <div class="flex h-screen bg-gray-100 font-sans">
    
    <aside :class="['w-64 bg-white shadow-xl flex-shrink-0 transition-all duration-300', isSidebarOpen ? 'translate-x-0' : '-translate-x-64', 'md:translate-x-0 fixed md:relative z-20 h-full']">
      <div class="h-full flex flex-col">
        <div class="flex items-center justify-center h-20 border-b">
          <img src="https://cdn-001.haui.edu.vn//img/logo-haui-size.png" alt="Logo" class="h-10 mr-2">
          <h2 class="text-xl font-bold text-gray-800">NHÓM 7</h2>
          <button @click="isSidebarOpen = false" class="md:hidden ml-auto mr-4 text-gray-600">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <nav class="flex-1 overflow-y-auto py-4">
          <a href="#" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
            <i class="fas fa-th-large w-6"></i>
            <span class="font-medium">Dashboard</span>
          </a>
          <a href="#" class="flex items-center px-6 py-3 bg-blue-50 text-blue-600 border-r-4 border-blue-600">
            <i class="fas fa-car w-6"></i>
            <span class="font-medium">Quản lý Sản phẩm</span>
          </a>
          <a href="#" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
            <i class="fas fa-receipt w-6"></i>
            <span class="font-medium">Đơn hàng</span>
          </a>
          <a href="#" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
            <i class="fas fa-users w-6"></i>
            <span class="font-medium">Khách hàng</span>
          </a>
        </nav>
        <div class="p-4 border-t">
          <button @click="handleLogout" class="flex items-center w-full px-6 py-3 text-red-600 hover:bg-red-50 rounded-lg transition-colors">
            <i class="fas fa-sign-out-alt w-6"></i>
            <span class="font-medium">Đăng xuất</span>
          </button>
        </div>
      </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden relative">
      <header class="h-20 bg-white shadow-sm flex items-center justify-between px-6 z-10">
        <div class="flex items-center">
          <button @click="isSidebarOpen = true" class="md:hidden text-gray-600 mr-4">
            <i class="fas fa-bars text-xl"></i>
          </button>
          <h1 class="text-2xl font-bold text-gray-800">Quản lý Sản phẩm</h1>
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
        
        <div class="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4">
          <button @click="openCreateModal()" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg shadow-md transition-all flex items-center gap-2 font-medium">
            <i class="fas fa-plus"></i> Thêm Xe Mới
          </button>
          
          <div class="relative w-full sm:w-72">
            <input type="text" v-model="searchTerm" placeholder="Tìm kiếm sản phẩm..." class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none">
            <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-md overflow-hidden">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead class="bg-gray-100 text-gray-600 uppercase text-xs font-bold">
                <tr>
                  <th class="p-4 border-b text-center w-16">ID</th>
                  <th class="p-4 border-b w-32 text-center">Hình ảnh</th>
                  <th class="p-4 border-b">Tên Phiên Bản</th>
                  <th class="p-4 border-b">Dòng Xe</th>
                  <th class="p-4 border-b text-right">Giá Niêm Yết</th>
                  <th class="p-4 border-b text-center">Trạng thái</th>
                  <th class="p-4 border-b text-center w-40">Hành động</th>
                </tr>
              </thead>
              <tbody class="text-sm text-gray-700">
                <tr v-if="isLoading"><td colspan="7" class="p-8 text-center text-gray-500"><i class="fas fa-spinner fa-spin mr-2"></i> Đang tải dữ liệu...</td></tr>
                <tr v-else-if="filteredVariants.length === 0"><td colspan="7" class="p-8 text-center text-gray-500">Không tìm thấy dữ liệu.</td></tr>
                
                <tr v-for="variant in filteredVariants" :key="variant.id" class="border-b hover:bg-blue-50 transition-colors group">
                  <td class="p-4 font-bold text-gray-500 text-center">#{{ variant.id }}</td>
                  
                  <td class="p-4 flex justify-center">
                    <div class="relative h-16 w-24 rounded-lg overflow-hidden border bg-gray-100 shadow-sm group-hover:shadow-md transition-all">
                      <img 
                        :src="getImageUrl(variant.firstImageUrl)" 
                        class="h-full w-full object-contain bg-white"
                        @error="handleImageError"
                      >
                    </div>
                  </td>

                  <td class="p-4 font-semibold text-gray-800">{{ variant.name }}</td>
                  <td class="p-4"><span class="bg-blue-100 text-blue-800 py-1 px-3 rounded-full text-xs font-bold">{{ variant.modelName || 'N/A' }}</span></td>
                  <td class="p-4 text-right font-mono font-medium text-blue-600">{{ formatPrice(variant.price) }}</td>
                  <td class="p-4 text-center">
                    <span :class="variant.isActive ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'" class="py-1 px-3 rounded-full text-xs font-bold">
                      {{ variant.isActive ? 'Active' : 'Hidden' }}
                    </span>
                  </td>
                  
                  <td class="p-4 text-center">
                    <div class="flex items-center justify-center gap-2">
                        <button @click="openDetailModal(variant.id)" class="text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100 p-2 rounded-lg transition-all" title="Xem chi tiết">
                            <i class="fas fa-eye text-lg"></i>
                        </button>
                        <button @click="openEditModal(variant.id)" class="text-blue-600 hover:text-blue-800 hover:bg-blue-100 p-2 rounded-lg transition-all" title="Sửa">
                            <i class="fas fa-edit text-lg"></i>
                        </button>
                        <button @click="deleteVariant(variant.id)" class="text-red-600 hover:text-red-800 hover:bg-red-100 p-2 rounded-lg transition-all" title="Xóa">
                            <i class="fas fa-trash-alt text-lg"></i>
                        </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div class="p-4 border-t bg-gray-50 flex justify-between items-center text-xs text-gray-500">
            <span>Hiển thị {{ filteredVariants.length }} kết quả</span>
            <div class="flex gap-2">
              <button class="px-3 py-1 border rounded hover:bg-gray-200" disabled>Trước</button>
              <button class="px-3 py-1 border rounded bg-blue-600 text-white">1</button>
              <button class="px-3 py-1 border rounded hover:bg-gray-200" disabled>Sau</button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm" @click="closeModal"></div>
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-3xl max-h-[90vh] overflow-hidden flex flex-col relative z-10 animate-fade-in-up">
        
        <div class="px-6 py-4 border-b flex justify-between items-center bg-gray-50">
          <h2 class="text-xl font-bold text-gray-800">{{ isEditing ? 'Cập Nhật Sản Phẩm' : 'Thêm Sản Phẩm Mới' }}</h2>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600 text-2xl">&times;</button>
        </div>

        <div class="p-6 overflow-y-auto flex-1">
          <form @submit.prevent="submitForm" class="space-y-6">
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">Tên Phiên Bản <span class="text-red-500">*</span></label>
                <input v-model="form.name" type="text" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" placeholder="VD: VF 8 Eco">
              </div>
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">Thuộc Dòng Xe (Model) <span class="text-red-500">*</span></label>
                <select v-model="form.modelId" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white">
                  <option :value="null" disabled>-- Chọn dòng xe --</option>
                  <option v-for="model in carModels" :key="model.id" :value="model.id">{{ model.name }}</option>
                </select>
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">Giá Niêm Yết <span class="text-red-500">*</span></label>
                <input v-model.number="form.price" type="number" required min="0" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none">
              </div>
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">Giá Khuyến Mãi</label>
                <input v-model.number="form.discountedPrice" type="number" min="0" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none">
              </div>
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">Trạng Thái</label>
                <select v-model="form.isActive" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white">
                  <option :value="true">Đang bán</option>
                  <option :value="false">Ngừng bán</option>
                </select>
              </div>
            </div>

            <div class="border-t pt-4">
              <label class="block text-sm font-semibold text-gray-700 mb-3">Hình Ảnh Sản Phẩm</label>
              
              <div v-if="isEditing && currentImages.length > 0" class="mb-4">
                <p class="text-xs text-gray-500 mb-2">Ảnh hiện tại:</p>
                <div class="flex flex-wrap gap-3">
                  <div v-for="img in currentImages" :key="img.id" class="relative group">
                    <img :src="getImageUrl(img.imageUrl)" class="h-20 w-24 object-contain rounded-lg border shadow-sm bg-gray-50" @error="handleImageError">
                    <button type="button" @click="deleteImage(img.id)" class="absolute -top-2 -right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center shadow-md opacity-0 group-hover:opacity-100 transition-all hover:bg-red-600">
                      <i class="fas fa-times text-xs"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div 
                class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-blue-500 hover:bg-blue-50 transition-all cursor-pointer relative"
                @click="$refs.fileInput.click()"
              >
                <input ref="fileInput" type="file" multiple accept="image/*" class="hidden" @change="handleFileSelect">
                
                <div v-if="selectedFiles.length === 0">
                  <i class="fas fa-cloud-upload-alt text-4xl text-blue-400 mb-3"></i>
                  <p class="font-medium text-gray-600">Nhấn để tải ảnh lên</p>
                  <p class="text-xs text-gray-400">Hỗ trợ JPG, PNG, WEBP</p>
                </div>

                <div v-else class="grid grid-cols-4 sm:grid-cols-6 gap-4">
                  <div v-for="(preview, index) in imagePreviews" :key="index" class="relative">
                    <img :src="preview" class="h-20 w-full object-contain rounded-lg shadow-sm bg-white border">
                    <div class="absolute inset-0 bg-black/40 rounded-lg flex items-center justify-center opacity-0 hover:opacity-100 transition-opacity">
                        <span class="text-white text-xs font-bold">Mới</span>
                    </div>
                  </div>
                  <div class="flex items-center justify-center h-20 border rounded-lg text-blue-600 font-bold bg-white">
                    +{{ selectedFiles.length }} ảnh
                  </div>
                </div>
              </div>
            </div>

          </form>
        </div>

        <div class="px-6 py-4 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="closeModal" class="px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-100 font-medium transition-colors">Hủy bỏ</button>
          <button @click="submitForm" :disabled="isLoadingSubmit" class="px-6 py-2.5 rounded-lg bg-blue-600 text-white hover:bg-blue-700 font-bold shadow-md hover:shadow-lg transition-all flex items-center disabled:opacity-70 disabled:cursor-not-allowed">
            <i v-if="isLoadingSubmit" class="fas fa-spinner fa-spin mr-2"></i>
            {{ isLoadingSubmit ? 'Đang xử lý...' : 'Lưu Sản Phẩm' }}
          </button>
        </div>

      </div>
    </div>

    <div v-if="showDetailModal" class="fixed inset-0 z-[60] flex items-center justify-center p-4">
       <div class="absolute inset-0 bg-black/70 backdrop-blur-sm" @click="showDetailModal = false"></div>
       <div class="bg-white rounded-xl shadow-2xl w-full max-w-2xl max-h-[85vh] overflow-hidden flex flex-col relative z-10 animate-fade-in-up">
          
          <div v-if="isLoadingDetail" class="h-64 flex items-center justify-center">
             <i class="fas fa-spinner fa-spin text-3xl text-blue-600"></i>
          </div>

          <template v-else>
              <div class="relative h-56 bg-gray-100 flex justify-center items-center overflow-hidden">
                 <img 
                   :src="getDetailHeroImage()" 
                   class="h-full w-auto object-contain"
                   @error="handleImageError"
                 >
                 <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent flex items-end p-6">
                    <div>
                       <span class="bg-blue-600 text-white text-xs font-bold px-2 py-1 rounded mb-2 inline-block">{{ selectedVariant?.modelName }}</span>
                       <h2 class="text-3xl font-bold text-white">{{ selectedVariant?.name }}</h2>
                       <p class="text-gray-200 font-mono text-lg">{{ formatPrice(selectedVariant?.price) }}</p>
                    </div>
                 </div>
                 <button @click="showDetailModal = false" class="absolute top-4 right-4 bg-black/20 hover:bg-black/50 text-white rounded-full w-8 h-8 flex items-center justify-center transition-all">&times;</button>
              </div>

              <div class="p-6 overflow-y-auto">
                 <h3 class="font-bold text-gray-800 border-b pb-2 mb-4 flex items-center gap-2">
                   <i class="fas fa-cogs text-gray-500"></i> Thông số kỹ thuật
                 </h3>
                 <div class="grid grid-cols-2 gap-y-4 gap-x-8 text-sm">
                    <div class="flex justify-between border-b border-gray-100 pb-1">
                       <span class="text-gray-500">Dài x Rộng x Cao</span>
                       <span class="font-medium text-gray-900">{{ selectedVariant?.length || '-' }} x {{ selectedVariant?.width || '-' }} x {{ selectedVariant?.height || '-' }} mm</span>
                    </div>
                    <div class="flex justify-between border-b border-gray-100 pb-1">
                       <span class="text-gray-500">Chiều dài cơ sở</span>
                       <span class="font-medium text-gray-900">{{ selectedVariant?.wheelbase || '-' }} mm</span>
                    </div>
                    <div class="flex justify-between border-b border-gray-100 pb-1">
                       <span class="text-gray-500">Khoảng sáng gầm</span>
                       <span class="font-medium text-gray-900">{{ selectedVariant?.groundClearance || '-' }} mm</span>
                    </div>
                    <div class="flex justify-between border-b border-gray-100 pb-1">
                       <span class="text-gray-500">Động cơ/Pin</span>
                       <span class="font-medium text-gray-900">{{ selectedVariant?.battery ? selectedVariant.battery + ' kWh' : '-' }}</span>
                    </div>
                    <div class="flex justify-between border-b border-gray-100 pb-1">
                       <span class="text-gray-500">Công suất tối đa</span>
                       <span class="font-medium text-gray-900">{{ selectedVariant?.maxPower || '-' }} hp</span>
                    </div>
                    <div class="flex justify-between border-b border-gray-100 pb-1">
                       <span class="text-gray-500">Quãng đường (NEDC)</span>
                       <span class="font-medium text-gray-900 text-green-600 font-bold">{{ selectedVariant?.rangeNedc || '-' }} km</span>
                    </div>
                 </div>

                 <div v-if="selectedVariant?.images?.length > 0" class="mt-6">
                    <h3 class="font-bold text-gray-800 border-b pb-2 mb-3">Thư viện ảnh ({{ selectedVariant.images.length }})</h3>
                    <div class="flex gap-2 overflow-x-auto pb-2">
                       <img v-for="img in selectedVariant.images" :key="img.id" :src="getImageUrl(img.imageUrl)" class="h-20 w-28 object-contain rounded-lg border flex-shrink-0 cursor-pointer hover:opacity-80 bg-gray-50" @error="handleImageError">
                    </div>
                 </div>
                 <div v-else class="mt-6 text-sm text-gray-500 italic">Không có hình ảnh nào.</div>
              </div>
          </template>
          
          <div class="p-4 bg-gray-50 border-t flex justify-end">
             <button @click="showDetailModal = false" class="px-6 py-2 bg-gray-800 text-white rounded-lg hover:bg-gray-900 font-bold">Đóng</button>
          </div>
       </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import api from '@/utils/axios'
import { useRouter } from 'vue-router'
import VinfastLogo from "@/assets/image/icon_logo/VinFast-logo.svg";

const router = useRouter()

// --- State ---
const isSidebarOpen = ref(false) // Sidebar toggle
const variants = ref([])
const carModels = ref([])
const showModal = ref(false)
const showDetailModal = ref(false)
const isEditing = ref(false)
const isLoading = ref(false)
const isLoadingDetail = ref(false)
const isLoadingSubmit = ref(false)
const searchTerm = ref('')
const selectedVariant = ref(null)

const selectedFiles = ref([])
const imagePreviews = ref([])
const currentImages = ref([])

const form = reactive({
  id: null,
  name: '',
  price: 0,
  discountedPrice: 0,
  modelId: null,
  isActive: true,
})

// --- Computed ---
const filteredVariants = computed(() => {
  if (!searchTerm.value) return variants.value
  const term = searchTerm.value.toLowerCase()
  return variants.value.filter(v => 
    v.name.toLowerCase().includes(term) || 
    (v.modelName && v.modelName.toLowerCase().includes(term))
  )
})

// --- Helpers ---
const formatPrice = (value) => {
    if(!value) return '0 ₫';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}

const getImageUrl = (path) => {
    if (!path) return VinfastLogo; // Nếu null/rỗng -> trả về Logo mặc định
    if (path.startsWith('http')) return path; // Nếu là link online (http...) -> giữ nguyên
    
    // Nếu đường dẫn bắt đầu bằng '/', nối trực tiếp
    if (path.startsWith('/')) {
        return `http://localhost:8080${path}`;
    }
    
    // Nếu đường dẫn KHÔNG bắt đầu bằng '/' (trường hợp của bạn), thêm vào
    return `http://localhost:8080/${path}`;
}

const getDetailHeroImage = () => {
    if (selectedVariant.value && selectedVariant.value.images && selectedVariant.value.images.length > 0) {
        return getImageUrl(selectedVariant.value.images[0].imageUrl);
    }
    return VinfastLogo;
}

const handleImageError = (e) => {
    e.target.src = VinfastLogo;
}

// --- API ---
const fetchData = async () => {
  isLoading.value = true
  try {
    const [resVariants, resModels] = await Promise.all([
      api.get('/api/variants'),
      api.get('/api/models') 
    ])
    variants.value = resVariants.data.result || []
    carModels.value = resModels.data.result || []
  } catch (error) {
    console.error(error)
  } finally {
    isLoading.value = false
  }
}

const fetchVariantDetail = async (id) => {
    try {
        const res = await api.get(`/api/variants/${id}/details`);
        return res.data.result;
    } catch (error) {
        console.error("Lỗi lấy chi tiết:", error);
        alert("Không thể tải thông tin chi tiết.");
        return null;
    }
}

// --- Actions ---
const openDetailModal = async (id) => {
    showDetailModal.value = true
    isLoadingDetail.value = true
    const detail = await fetchVariantDetail(id);
    if (detail) selectedVariant.value = detail;
    else showDetailModal.value = false;
    isLoadingDetail.value = false;
}

const openCreateModal = () => {
    isEditing.value = false
    Object.assign(form, { id: null, name: '', price: 0, discountedPrice: 0, modelId: null, isActive: true })
    currentImages.value = []
    selectedFiles.value = []
    imagePreviews.value = []
    showModal.value = true
}

const openEditModal = async (id) => {
    isEditing.value = true
    Object.assign(form, { id: null, name: 'Đang tải...', price: 0, discountedPrice: 0, modelId: null, isActive: true })
    currentImages.value = []
    showModal.value = true
    
    const detail = await fetchVariantDetail(id);
    if (detail) {
        form.id = detail.id
        form.name = detail.name
        form.price = detail.price
        form.discountedPrice = detail.discountedPrice
        form.modelId = detail.carModel ? detail.carModel.id : null
        if(!form.modelId && detail.modelName) {
            const m = carModels.value.find(x => x.name === detail.modelName)
            if(m) form.modelId = m.id
        }
        form.isActive = detail.isActive
        currentImages.value = detail.images || []
    } else {
        closeModal();
    }
}

const closeModal = () => {
  showModal.value = false
  imagePreviews.value.forEach(url => URL.revokeObjectURL(url))
  imagePreviews.value = []
  selectedFiles.value = []
}

const handleFileSelect = (e) => {
  const files = Array.from(e.target.files)
  if(!files.length) return
  selectedFiles.value = files
  imagePreviews.value = files.map(file => URL.createObjectURL(file))
}

const submitForm = async () => {
  if (!form.name || !form.price || !form.modelId) return alert("Thiếu thông tin bắt buộc")
  
  isLoadingSubmit.value = true
  try {
    let variantId = form.id
    const payload = { ...form }

    if (isEditing.value) {
      await api.put(`/api/variants/${variantId}`, payload)
    } else {
      const res = await api.post('/api/variants', payload)
      variantId = res.data.result.id 
    }

    if (selectedFiles.value.length > 0 && variantId) {
      const uploadPromises = selectedFiles.value.map(file => {
        const formData = new FormData()
        formData.append('file', file)
        return api.post(`/api/images/upload/${variantId}`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
      })
      await Promise.all(uploadPromises)
    }

    await fetchData()
    closeModal()
    alert(isEditing.value ? "Cập nhật thành công!" : "Thêm mới thành công!")
  } catch (error) {
    alert("Lỗi: " + (error.response?.data?.message || error.message))
  } finally {
    isLoadingSubmit.value = false
  }
}

const deleteImage = async (id) => {
  if (!confirm("Xóa ảnh này?")) return
  try {
    await api.delete(`/api/images/${id}`)
    currentImages.value = currentImages.value.filter(i => i.id !== id)
  } catch (e) { alert("Lỗi xóa ảnh") }
}

const deleteVariant = async (id) => {
  if (!confirm("Xóa xe này?")) return
  try {
    await api.delete(`/api/variants/${id}`)
    fetchData()
  } catch (e) { alert("Lỗi xóa xe") }
}

const handleLogout = () => {
  localStorage.removeItem('authToken')
  router.push('/login')
}

onMounted(fetchData)
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