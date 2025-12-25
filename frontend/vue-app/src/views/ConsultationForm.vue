<template>
  <div class="min-h-screen bg-slate-50 font-sans text-gray-900 pb-20">
    <!-- Header/Hero Section -->
    <div class="bg-blue-900 text-white py-16 px-6 text-center mb-12 shadow-lg">
      <h1 class="text-4xl md:text-5xl font-extrabold mb-4 uppercase tracking-tight italic">Đăng ký tư vấn & Lái thử</h1>
      <p class="text-blue-100 text-lg max-w-2xl mx-auto font-light">Khám phá công nghệ hiện đại và trải nghiệm cảm giác lái đẳng cấp từ VinFast.</p>
    </div>

    <div class="container mx-auto px-4 max-w-7xl">
      <div class="grid grid-cols-1 lg:grid-cols-12 gap-10">
        
        <!-- Left Column: Car Selection -->
        <div class="lg:col-span-8 space-y-10">
          
          <!-- Category Tabs -->
          <div class="flex items-center justify-center gap-4 bg-white p-2 rounded-2xl shadow-sm border border-gray-100 inline-flex mx-auto">
            <button 
              v-for="cat in categories" 
              :key="cat.id"
              @click="selectedCategory = cat.id"
              :class="['px-6 py-3 rounded-xl font-bold transition-all duration-300', selectedCategory === cat.id ? 'bg-blue-600 text-white shadow-md' : 'text-gray-500 hover:text-blue-600 hover:bg-blue-50']"
            >
              {{ cat.name }}
            </button>
          </div>

          <!-- Car Grid -->
          <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 animate-pulse">
            <div v-for="n in 6" :key="n" class="bg-gray-200 h-64 rounded-2xl"></div>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            <div 
              v-for="car in filteredCars" 
              :key="car.id"
              @click="form.variant_id = car.id"
              :class="['relative bg-white rounded-2xl border-2 transition-all duration-500 cursor-pointer overflow-hidden p-5 group', form.variant_id === car.id ? 'border-blue-600 ring-4 ring-blue-100' : 'border-gray-100 hover:border-blue-300 hover:shadow-xl']"
            >
              <!-- Selected Badge -->
              <div v-if="form.variant_id === car.id" class="absolute top-4 right-4 bg-blue-600 text-white w-8 h-8 rounded-full flex items-center justify-center shadow-lg z-20">
                <i class="fas fa-check text-sm"></i>
              </div>

              <!-- Car Image -->
              <div class="relative h-40 mb-4 flex items-center justify-center">
                <img :src="car.image" :alt="car.name" class="max-w-full max-h-full object-contain transform transition-transform duration-500 group-hover:scale-110">
                <div class="absolute bottom-0 w-3/4 h-2 bg-black/10 blur-lg rounded-full"></div>
              </div>

              <!-- Car Info -->
              <div class="space-y-2">
                <h3 class="font-extrabold text-xl group-hover:text-blue-600 transition-colors">{{ car.name }}</h3>
                <p class="text-blue-600 font-bold text-lg">{{ car.price ? car.price.toLocaleString() + ' VNĐ' : 'Liên hệ' }}</p>
                <div class="flex items-center gap-4 text-xs text-gray-500 pt-2 border-t border-gray-50">
                   <span class="flex items-center gap-1"><i class="fas fa-users"></i> {{ car.seats || 5 }} chỗ</span>
                   <span class="flex items-center gap-1"><i class="fas fa-bolt"></i> {{ car.range || 'N/A' }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <div v-if="!loading && filteredCars.length === 0" class="text-center py-20 bg-white rounded-2xl border border-gray-100">
             <i class="fas fa-car-side text-5xl text-gray-200 mb-4 block"></i>
             <p class="text-gray-500">Hiện không có xe thuộc danh mục này.</p>
          </div>
        </div>

        <!-- Right Column: Registration Form -->
        <div class="lg:col-span-4">
          <div class="bg-white rounded-3xl shadow-2xl p-8 sticky top-28 border border-gray-100">
            <h2 class="text-2xl font-black text-gray-800 mb-8 border-b pb-4 flex items-center gap-3">
              <i class="fas fa-paper-plane text-blue-600"></i>
              Thông Tin Đăng Ký
            </h2>

            <form @submit.prevent="submitForm" class="space-y-6">
              <!-- Personal Info -->
              <div class="space-y-4">
                <div class="group">
                  <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Họ và tên *</label>
                  <input 
                    v-model="form.customer_name" 
                    type="text" 
                    required 
                    placeholder="Nguyễn Văn A"
                    class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm"
                  >
                </div>

                <div class="group">
                  <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Số điện thoại *</label>
                  <input 
                    v-model="form.phone_number" 
                    type="tel" 
                    required
                    placeholder="09xx xxx xxx" 
                    class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm"
                  >
                </div>

                <div class="group">
                  <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Email</label>
                  <input 
                    v-model="form.email" 
                    type="email" 
                    placeholder="email@example.com"
                    class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm"
                  >
                </div>
              </div>

              <!-- Location & Variant -->
              <div class="space-y-4">
                <div class="group">
                  <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Tỉnh / Thành phố *</label>
                  <select 
                    v-model="form.province" 
                    required
                    class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm appearance-none cursor-pointer"
                  >
                    <option value="" disabled>Chọn Tỉnh thành</option>
                    <option v-for="prov in provinces" :key="prov" :value="prov">{{ prov }}</option>
                  </select>
                </div>

                <div class="group">
                  <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Showroom *</label>
                  <select 
                    v-model="form.showroom_id" 
                    required
                    :disabled="!form.province"
                    class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm appearance-none cursor-pointer disabled:opacity-50"
                  >
                    <option :value="null" disabled>Chọn Showroom</option>
                    <option v-for="room in filteredShowrooms" :key="room.id" :value="room.id">
                      {{ room.name }} - {{ room.address }}
                    </option>
                  </select>
                </div>
              </div>

              <!-- Time -->
              <div class="group">
                <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Lịch hẹn dự kiến</label>
                <input 
                  v-model="scheduledDateTime" 
                  type="datetime-local" 
                  class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm"
                >
              </div>

              <!-- Note -->
              <div class="group">
                <label class="block text-xs font-bold text-gray-400 uppercase mb-1 tracking-widest pl-1">Ghi chú yêu cầu</label>
                <textarea 
                  v-model="form.note" 
                  rows="3" 
                  placeholder="Tôi muốn lái thử vào cuối tuần..."
                  class="w-full bg-slate-50 border border-transparent focus:border-blue-500 focus:bg-white rounded-xl px-4 py-3 outline-none transition-all shadow-sm resize-none"
                ></textarea>
              </div>

              <!-- Submission -->
              <div class="pt-4">
                <button 
                  type="submit" 
                  :disabled="isSubmitting || !form.variant_id" 
                  class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-black py-4 rounded-xl shadow-xl hover:shadow-2xl transition-all uppercase tracking-widest flex items-center justify-center gap-3 transform hover:-translate-y-1 active:translate-y-0"
                >
                  <template v-if="isSubmitting">
                    <i class="fas fa-circle-notch animate-spin"></i> Đang gửi...
                  </template>
                  <template v-else>
                    Gửi yêu cầu ngay <i class="fas fa-chevron-right text-xs"></i>
                  </template>
                </button>
                <p v-if="!form.variant_id" class="text-center text-xs text-red-500 mt-2 font-medium italic">Vui lòng chọn xe ở danh sách bên cạnh!</p>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// --- DATA ---
const categories = [
  { id: 'ev', name: 'Ô tô điện' },
  { id: 'gasoline', name: 'Ô tô xăng' },
  { id: 'service', name: 'Xe dịch vụ' }
]

const selectedCategory = ref('ev')
const loading = ref(true)
const isSubmitting = ref(false)
const allCars = ref([])
const showroomsData = ref([])
const provinces = ref([])

// Form match DB schema (consultation_requests)
const form = reactive({
  customer_name: '',
  phone_number: '',
  email: '',
  province: '',
  variant_id: null,
  showroom_id: null,
  scheduled_at: '',
  note: ''
})

const scheduledDateTime = ref('')

// --- COMPUTED ---
const filteredCars = computed(() => {
  return allCars.value.filter(car => car.category === selectedCategory.value)
})

const filteredShowrooms = computed(() => {
  if (!form.province) return []
  return showroomsData.value.filter(s => s.province === form.province)
})

// --- METHODS ---
const fetchData = async (url) => {
  try {
    const res = await fetch(url)
    const json = await res.json()
    // Giả sử API trả về { code: 1000, result: [...] }
    return json.code === 1000 ? json.result : []
  } catch (e) {
    console.error(e)
    return []
  }
}

onMounted(async () => {
  loading.value = true
  try {
    // Fetch dữ liệu song song để tối ưu tốc độ
    const [evRes, gasRes, serRes, roomRes] = await Promise.all([
      fetchData('http://localhost:8080/api/public/products/ev'),
      fetchData('http://localhost:8080/api/public/products/gasoline'),
      fetchData('http://localhost:8080/api/public/products/service'),
      fetchData('http://localhost:8080/api/public/showrooms')
      // fetchData('/api/public/products/ev'),
      // fetchData('/api/public/products/gasoline'),
      // fetchData('/api/public/products/service'),
      // fetchData('/api/public/showrooms')
    ])

    const normalize = (list, cat) => list.map(c => ({
      id: c.id,
      name: c.name,
      price: c.price,
      image: c.image,
      category: cat,
      seats: c.seats,
      range: c.rangeNedc || c.range
    }))

    allCars.value = [
      ...normalize(evRes || [], 'ev'),
      ...normalize(gasRes || [], 'gasoline'),
      ...normalize(serRes || [], 'service')
    ]

    showroomsData.value = roomRes || []
    if (roomRes) {
      provinces.value = [...new Set(roomRes.map(s => s.province))]
    }

  } catch (err) {
    console.error("Lỗi tải dữ liệu ban đầu:", err)
  } finally {
    loading.value = false
  }
})

// --- ĐÃ SỬA: Đảm bảo khai báo hàm submitForm chính xác ---
const submitForm = async () => {
  if (!form.variant_id) {
    alert("Vui lòng chọn xe!")
    return
  }
  
  isSubmitting.value = true
  
  // Format scheduled_at for Backend (ISO string)
  if (scheduledDateTime.value) {
    form.scheduled_at = new Date(scheduledDateTime.value).toISOString()
  }

  try {
    //nếu không chạy ngrol thì thay thành: const evRes = await fetch('http://localhost:8080/api/public/products/ev') 
      //tương tự với các api khác
    const response = await fetch('/api/public/consultations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    })

    const result = await response.json()

    if (result.code === 1000) {
      alert("Đăng ký tư vấn thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.")
      // Reset form
      Object.assign(form, {
         customer_name: '', phone_number: '', email: '', province: '', 
         variant_id: null, showroom_id: null, scheduled_at: '', note: ''
      })
      router.push('/')
    } else {
      // Hiển thị message lỗi từ Backend trả về
      alert(result.message || "Gửi yêu cầu thất bại. Vui lòng thử lại.")
    }
  } catch (error) {
    console.error(error)
    alert("Lỗi kết nối server.")
  } finally {
    isSubmitting.value = false
  }
}

// Reset showroom if province changes
watch(() => form.province, () => {
  form.showroom_id = null
})
</script>

<style scoped>
/* Glassmorphism & Custom scroll */
.container {
  scrollbar-width: thin;
  scrollbar-color: #3b82f6 #f1f5f9;
}

::-webkit-datetime-edit-fields-wrapper { font-family: inherit; }

/* Animation for cards */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.variant-card {
  animation: fadeInUp 0.4s ease-out forwards;
}
</style>