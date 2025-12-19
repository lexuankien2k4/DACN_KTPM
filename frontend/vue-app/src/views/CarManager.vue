<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <div class="max-w-7xl mx-auto">
      <div class="flex justify-between items-center mb-8">
        <h1 class="text-3xl font-bold text-gray-800">Quản lý Xe & Hình ảnh</h1>
        <div class="flex gap-4">
             <button @click="logout" class="text-red-600 font-semibold hover:underline">Đăng xuất</button>
             <button @click="openModal()" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg shadow">+ Thêm Mới</button>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow overflow-hidden">
        <table class="w-full text-left">
          <thead class="bg-gray-100 border-b">
            <tr>
              <th class="p-4 font-semibold text-gray-600">ID</th>
              <th class="p-4 font-semibold text-gray-600">Tên Phiên bản</th>
              <th class="p-4 font-semibold text-gray-600">Hình ảnh</th>
              <th class="p-4 font-semibold text-gray-600 text-right">Thao tác</th>
            </tr>
          </thead>
          <tbody class="divide-y">
            <tr v-for="variant in variants" :key="variant.id" class="hover:bg-gray-50">
              <td class="p-4 text-gray-500">#{{ variant.id }}</td>
              <td class="p-4 font-medium">{{ variant.name }}</td>
              <td class="p-4">
                <div class="flex gap-2">
                  <img v-for="img in (variant.images || []).slice(0, 3)" :key="img.id" 
                       :src="getImageUrl(img.imageUrl)" class="h-10 w-10 rounded object-cover border" />
                </div>
              </td>
              <td class="p-4 text-right space-x-2">
                <button @click="openModal(variant)" class="text-blue-600 hover:underline">Sửa</button>
                <button @click="deleteVariant(variant.id)" class="text-red-600 hover:underline">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
        <div class="bg-white rounded-xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
          <div class="p-6 border-b flex justify-between items-center">
            <h2 class="text-xl font-bold">{{ isEditing ? 'Cập nhật' : 'Thêm mới' }} Xe</h2>
            <button @click="closeModal" class="text-2xl">&times;</button>
          </div>

          <div class="p-6 space-y-6">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium mb-1">Tên phiên bản</label>
                <input v-model="form.name" class="w-full border rounded p-2" placeholder="VD: VF 8 Eco" />
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Model ID (Cha)</label>
                <input v-model.number="form.carModelId" type="number" class="w-full border rounded p-2" />
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Giá niêm yết</label>
                <input v-model.number="form.listPrice" type="number" class="w-full border rounded p-2" />
              </div>
               <div>
                <label class="block text-sm font-medium mb-1">Giá khuyến mãi</label>
                <input v-model.number="form.discountedPrice" type="number" class="w-full border rounded p-2" />
              </div>
            </div>

            <div class="border-t pt-4">
              <h3 class="font-semibold mb-3">Hình ảnh</h3>
              <div v-if="isEditing" class="flex gap-2 mb-4 flex-wrap">
                <div v-for="img in currentImages" :key="img.id" class="relative group">
                    <img :src="getImageUrl(img.imageUrl)" class="h-20 w-24 object-cover rounded border" />
                    <button @click="deleteImage(img.id)" class="absolute -top-2 -right-2 bg-red-500 text-white rounded-full w-5 h-5 flex items-center justify-center opacity-0 group-hover:opacity-100 transition">&times;</button>
                </div>
              </div>
              
              <input type="file" ref="fileInput" multiple accept="image/*" @change="handleFileSelect" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100" />
            </div>
          </div>

          <div class="p-6 border-t bg-gray-50 flex justify-end gap-3">
            <button @click="closeModal" class="px-4 py-2 text-gray-600 hover:bg-gray-200 rounded">Hủy</button>
            <button @click="submitForm" :disabled="isLoading" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 disabled:bg-blue-300">
              {{ isLoading ? 'Đang xử lý...' : 'Lưu lại' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/utils/axios' // Sử dụng axios có token
import { useRouter } from 'vue-router'

const router = useRouter()
const variants = ref([])
const showModal = ref(false)
const isLoading = ref(false)
const isEditing = ref(false)
const selectedFiles = ref([])
const currentImages = ref([])

const form = ref({ id: null, name: '', listPrice: 0, discountedPrice: 0, carModelId: null })

// Helpers
const getImageUrl = (path) => `http://localhost:8080${path}`

// Load danh sách
const loadVariants = async () => {
  try {
    const res = await api.get('/api/variants') // API GET thường public, nhưng dùng api instance vẫn ok
    variants.value = res.data.result || []
  } catch (e) { console.error(e) }
}

const openModal = (variant = null) => {
  isEditing.value = !!variant
  selectedFiles.value = []
  if (variant) {
    form.value = { ...variant }
    currentImages.value = variant.images || []
  } else {
    form.value = { id: null, name: '', listPrice: 0, discountedPrice: 0, carModelId: null }
    currentImages.value = []
  }
  showModal.value = true
}

const closeModal = () => showModal.value = false
const handleFileSelect = (e) => selectedFiles.value = Array.from(e.target.files)

const deleteImage = async (id) => {
    if(!confirm('Xóa ảnh?')) return;
    await api.delete(`/api/images/${id}`);
    currentImages.value = currentImages.value.filter(i => i.id !== id);
}

const submitForm = async () => {
  isLoading.value = true
  try {
    let variantId = form.value.id
    
    // 1. Lưu thông tin xe (Cần Token -> api instance tự lo)
    if (isEditing.value) {
      await api.put(`/api/variants/${variantId}`, form.value)
    } else {
      const res = await api.post('/api/variants', form.value)
      variantId = res.data.result.id
    }

    // 2. Upload ảnh (Cần Token)
    if (selectedFiles.value.length > 0) {
      const promises = selectedFiles.value.map(file => {
        const formData = new FormData()
        formData.append('file', file)
        return api.post(`/api/images/upload/${variantId}`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
      })
      await Promise.all(promises)
    }
    
    alert('Thành công!')
    closeModal()
    loadVariants()
  } catch (e) {
    alert('Lỗi: ' + (e.response?.data?.message || e.message))
  } finally {
    isLoading.value = false
  }
}

const deleteVariant = async (id) => {
    if(!confirm('Xóa xe?')) return
    try {
        await api.delete(`/api/variants/${id}`)
        loadVariants()
    } catch(e) { alert('Lỗi xóa') }
}

const logout = () => {
    localStorage.removeItem('authToken');
    router.push('/login');
}

onMounted(loadVariants)
</script>