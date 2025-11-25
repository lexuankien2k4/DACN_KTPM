<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center p-4 font-sans">
    <div class="bg-white w-full max-w-6xl rounded-xl shadow-2xl overflow-hidden flex flex-col md:flex-row">
      
      <div class="w-full md:w-1/2 bg-gray-100 relative flex items-center justify-center p-6 group">
        <div class="relative w-full h-full flex items-center justify-center">
           <img 
            :src="displayImage" 
            @error="handleImageError"
            alt="VinFast Car" 
            class="w-full h-auto object-contain transition-transform duration-700 transform group-hover:scale-105 z-10"
          >
          <div class="absolute bottom-10 w-3/4 h-4 bg-black opacity-20 blur-xl rounded-[100%]"></div>
        </div>
        
        <div v-if="selectedVariantName" class="absolute top-6 left-6 bg-white/90 backdrop-blur-sm px-4 py-2 rounded-lg shadow-sm border border-gray-100">
          <p class="text-xs text-gray-500 uppercase tracking-wider font-semibold">Dòng xe</p>
          <p class="text-lg font-bold text-gray-800">{{ selectedVariantName }}</p>
        </div>
      </div>

      <div class="w-full md:w-1/2 p-8 md:p-12 overflow-y-auto max-h-[90vh]">
        
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-2 uppercase tracking-wide">ĐĂNG KÝ LÁI THỬ</h2>
        <p class="text-center text-sm text-gray-500 mb-8">
          Trải nghiệm đẳng cấp VinFast ngay hôm nay.
        </p>

        <div class="flex border-b border-gray-200 mb-8">
          <button class="w-1/2 py-3 text-center font-semibold text-blue-600 border-b-2 border-blue-600 focus:outline-none transition-colors">Xe ô tô</button>
          <button class="w-1/2 py-3 text-center font-medium text-gray-400 hover:text-gray-600 focus:outline-none transition-colors">Xe máy điện</button>
        </div>

        <form @submit.prevent="submitForm" class="space-y-6">
          
          <section>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-4 tracking-wider flex items-center gap-2">
              <span class="w-1.5 h-1.5 rounded-full bg-blue-600"></span> Thông tin khách hàng
            </h3>
            <div class="space-y-5">
              <div class="relative group">
                <input type="text" v-model="form.customerName" placeholder=" " 
                  class="peer w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none transition-colors bg-transparent pt-4" required>
                <label class="absolute left-0 top-0 text-gray-400 text-xs transition-all peer-placeholder-shown:text-base peer-placeholder-shown:top-2 peer-focus:top-0 peer-focus:text-xs peer-focus:text-blue-600 pointer-events-none">Họ và tên Quý khách *</label>
              </div>
              
              <div class="grid grid-cols-2 gap-6">
                <div class="relative group">
                  <input type="tel" v-model="form.phoneNumber" placeholder=" " pattern="[0-9]{10,11}"
                    class="peer w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none transition-colors bg-transparent pt-4" required>
                  <label class="absolute left-0 top-0 text-gray-400 text-xs transition-all peer-placeholder-shown:text-base peer-placeholder-shown:top-2 peer-focus:top-0 peer-focus:text-xs peer-focus:text-blue-600 pointer-events-none">Số điện thoại *</label>
                </div>
                <div class="relative group">
                  <input type="email" v-model="form.email" placeholder=" " 
                    class="peer w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none transition-colors bg-transparent pt-4">
                  <label class="absolute left-0 top-0 text-gray-400 text-xs transition-all peer-placeholder-shown:text-base peer-placeholder-shown:top-2 peer-focus:top-0 peer-focus:text-xs peer-focus:text-blue-600 pointer-events-none">Email (Tùy chọn)</label>
                </div>
              </div>
            </div>
          </section>

          <section>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-4 tracking-wider flex items-center gap-2">
              <span class="w-1.5 h-1.5 rounded-full bg-blue-600"></span> Lựa chọn mẫu xe
            </h3>
            <div class="space-y-5">
              <div class="relative">
                <select v-model="selectedModelId" class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer" required>
                  <option :value="null" disabled>Chọn Dòng xe *</option>
                  <option v-for="car in allCars" :key="car.id" :value="car.id">{{ car.name }}</option>
                </select>
                <div class="absolute right-0 top-3 pointer-events-none text-gray-400 text-xs">▼</div>
              </div>

              <div class="relative" v-if="selectedModelId">
                <select v-model="form.variantId" :disabled="isLoadingVariants" class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer disabled:text-gray-400" required>
                  <option :value="null" disabled>Chọn Phiên bản *</option>
                  <option v-if="isLoadingVariants" disabled>Đang tải...</option>
                  <option v-for="v in allVariants" :key="v.id" :value="v.id">{{ v.name }}</option>
                </select>
                <div class="absolute right-0 top-3 pointer-events-none text-gray-400 text-xs">▼</div>
              </div>
            </div>
          </section>

          <section>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-4 tracking-wider flex items-center gap-2">
              <span class="w-1.5 h-1.5 rounded-full bg-blue-600"></span> Lựa chọn địa điểm
            </h3>
            <div class="space-y-5">
              <div class="relative">
                <select v-model="selectedProvince" class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer" required>
                  <option :value="null" disabled>Tỉnh / Thành phố *</option>
                  <option v-for="prov in provinces" :key="prov" :value="prov">{{ prov }}</option>
                </select>
                <div class="absolute right-0 top-3 pointer-events-none text-gray-400 text-xs">▼</div>
              </div>

              <div class="relative">
                <select v-model="selectedShowroom" :disabled="!selectedProvince || isLoadingShowrooms" class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer disabled:text-gray-400" required>
                  <option :value="null" disabled>Showroom gần nhất *</option>
                  <option v-if="isLoadingShowrooms" disabled>Đang tìm kiếm...</option>
                  <option v-if="selectedProvince && !isLoadingShowrooms && showrooms.length === 0" disabled>Không tìm thấy showroom nào</option>
                  <option v-for="room in showrooms" :key="room.id" :value="room">
                    {{ room.name }} - {{ room.address }}
                  </option>
                </select>
                <div class="absolute right-0 top-3 pointer-events-none text-gray-400 text-xs">▼</div>
              </div>
            </div>
          </section>

          <section v-if="selectedShowroom">
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-4 tracking-wider flex items-center gap-2">
              <span class="w-1.5 h-1.5 rounded-full bg-blue-600"></span> Lựa chọn thời gian
            </h3>
            <div class="grid grid-cols-2 gap-6">
              <div class="relative">
                <label class="text-xs text-gray-400 mb-1 block">Ngày hẹn</label>
                <input type="date" v-model="form.scheduleDate" :min="todayStr" class="w-full border-b border-gray-300 py-1 focus:border-blue-600 focus:outline-none text-gray-700" required>
              </div>
              
              <div class="relative">
                <label class="text-xs text-gray-400 mb-1 block">Giờ hẹn</label>
                <input type="time" v-model="form.scheduleTime" class="w-full border-b border-gray-300 py-1 focus:border-blue-600 focus:outline-none text-gray-700" required>
              </div>
            </div>
          </section>

          <section>
             <h3 class="text-xs font-bold text-gray-400 uppercase mb-4 tracking-wider flex items-center gap-2">
              <span class="w-1.5 h-1.5 rounded-full bg-blue-600"></span> Ghi chú thêm
            </h3>
            <textarea v-model="form.note" rows="2" placeholder="Ví dụ: Tôi muốn lái thử vào cuối tuần..." class="w-full border border-gray-200 rounded-lg p-3 text-sm focus:border-blue-600 focus:ring-1 focus:ring-blue-600 outline-none transition-all resize-none"></textarea>
          </section>

          <div v-if="statusMsg.text" 
               class="text-sm p-4 rounded-lg font-medium flex items-center gap-3 transition-all duration-300"
               :class="statusMsg.success ? 'bg-green-50 text-green-700 border border-green-100' : 'bg-red-50 text-red-700 border border-red-100'">
            <span class="text-xl">{{ statusMsg.success ? '🎉' : '⚠️' }}</span>
            {{ statusMsg.text }}
          </div>

          <div class="pt-4">
            <button type="submit" :disabled="isSubmitting" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 px-6 rounded shadow-lg hover:shadow-xl transition-all uppercase tracking-widest flex justify-center items-center disabled:opacity-70 disabled:cursor-not-allowed">
              <span v-if="isSubmitting" class="mr-3">
                <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              </span>
              {{ isSubmitting ? 'ĐANG GỬI YÊU CẦU...' : 'GỬI ĐĂNG KÝ' }}
            </button>
          </div>

        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue';
import axios from 'axios';

// --- 1. CẤU HÌNH API ---
const API_URL = 'http://localhost:8080/api'; // Đổi port nếu cần
const apiClient = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
});

// --- 2. STATE DỮ LIỆU ---
const allCars = ref([]);       // GET /models/active
const allVariants = ref([]);   // GET /variants?modelId=...
const provinces = ref([]);     // GET /showrooms/provinces
const showrooms = ref([]);     // GET /showrooms?province=...

// State Lựa chọn
const selectedModelId = ref(null);
const selectedProvince = ref(null);
const selectedShowroom = ref(null); // Object Showroom

// State UI
const isLoadingVariants = ref(false);
const isLoadingShowrooms = ref(false);
const isSubmitting = ref(false);
const statusMsg = reactive({ text: '', success: false });
const selectedCarImage = ref(null);
const selectedVariantName = ref('');

// Form Model (Khớp với CreateConsultationRequest.java)
const form = reactive({
  customerName: '',
  phoneNumber: '',
  email: '',
  note: '',
  variantId: null,
  scheduleDate: '',
  scheduleTime: ''
});

// --- 3. COMPUTED ---
const todayStr = computed(() => new Date().toISOString().split('T')[0]);
const DEFAULT_IMAGE = new URL('@/assets/image/icon_logo/VinFast-logo.svg', import.meta.url).href;

const displayImage = computed(() => selectedCarImage.value || DEFAULT_IMAGE);

// --- 4. LOGIC LOAD DỮ LIỆU ---

onMounted(async () => {
  try {
    // Load Model & Tỉnh song song để nhanh hơn
    const [carsRes, provRes] = await Promise.all([
      apiClient.get('/models/active'),
      apiClient.get('/showrooms/provinces')
    ]);
    
    allCars.value = carsRes.data.result;
    provinces.value = provRes.data.result;
  } catch (e) {
    console.error("Init Error:", e);
    statusMsg.text = "Không thể tải dữ liệu. Vui lòng tải lại trang.";
  }
});

// --- 5. WATCHERS (Logic phụ thuộc) ---

// Khi chọn Dòng xe -> Load Phiên bản
watch(selectedModelId, async (newVal) => {
  allVariants.value = [];
  form.variantId = null;
  selectedCarImage.value = null;
  selectedVariantName.value = '';

  if (newVal) {
    isLoadingVariants.value = true;
    try {
      const res = await apiClient.get(`/variants?modelId=${newVal}`);
      allVariants.value = res.data.result;
      
      // Tự chọn bản đầu tiên để UX tốt hơn
      if (allVariants.value.length > 0) {
        const first = allVariants.value[0];
        form.variantId = first.id;
        selectedCarImage.value = first.firstImageUrl || first.image;
        selectedVariantName.value = first.name;
      }
    } catch (e) { console.error(e); }
    finally { isLoadingVariants.value = false; }
  }
});

// Khi đổi Phiên bản -> Đổi ảnh & tên
watch(() => form.variantId, (newId) => {
  const variant = allVariants.value.find(v => v.id === newId);
  if (variant) {
    selectedCarImage.value = variant.firstImageUrl || variant.image;
    selectedVariantName.value = variant.name;
  }
});

// Khi chọn Tỉnh -> Load Showroom
watch(selectedProvince, async (newVal) => {
  showrooms.value = [];
  selectedShowroom.value = null;
  
  if (newVal) {
    isLoadingShowrooms.value = true;
    try {
      const res = await apiClient.get('/showrooms', { params: { province: newVal } });
      showrooms.value = res.data.result;
    } catch (e) { console.error(e); }
    finally { isLoadingShowrooms.value = false; }
  }
});

// --- 6. XỬ LÝ ẢNH LỖI ---
const handleImageError = (e) => {
  e.target.src = DEFAULT_IMAGE;
};

// --- 7. SUBMIT FORM ---
const submitForm = async () => {
  statusMsg.text = '';
  statusMsg.success = false;

  // Validate cơ bản phía Client
  if (!selectedShowroom.value) {
    statusMsg.text = "Vui lòng chọn Showroom để chúng tôi phục vụ.";
    return;
  }

  isSubmitting.value = true;

  try {
    // 1. Chuẩn bị dữ liệu ngày giờ (ISO format cho LocalDateTime)
    const scheduledAt = `${form.scheduleDate}T${form.scheduleTime}:00`;

    // 2. Tạo Payload đúng chuẩn DTO Backend
    const payload = {
      customerName: form.customerName,
      phoneNumber: form.phoneNumber,
      email: form.email,
      province: selectedProvince.value,
      note: form.note,
      
      variantId: form.variantId,
      showroomId: selectedShowroom.value.id, // Lấy ID
      scheduledAt: scheduledAt
    };

    // 3. Gọi API
    await apiClient.post('/consultations', payload);

    // 4. Thành công
    statusMsg.success = true;
    statusMsg.text = "Đăng ký thành công! Tư vấn viên sẽ liên hệ xác nhận lịch lái thử.";
    
    // Reset các trường thông tin cá nhân
    form.customerName = '';
    form.phoneNumber = '';
    form.email = '';
    form.note = '';
    
  } catch (error) {
    statusMsg.success = false;
    // Lấy message lỗi từ Backend trả về (nếu có)
    const msg = error.response?.data?.message || "Gửi yêu cầu thất bại. Vui lòng thử lại.";
    statusMsg.text = msg;
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
/* Animation nhẹ cho input */
input:focus ~ label, input:not(:placeholder-shown) ~ label {
  /* Tailwind replacements: text-blue-600, text-xs, top-0 */
  color: #2563eb;
  font-size: 0.75rem;
  top: 0;
}
</style>