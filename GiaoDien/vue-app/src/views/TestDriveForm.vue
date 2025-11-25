<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center p-4">
    <div class="bg-white w-full max-w-6xl rounded-xl shadow-2xl overflow-hidden flex flex-col md:flex-row">
      
      <div class="w-full md:w-1/2 bg-gray-100 relative flex items-center justify-center p-6">
        <img 
          src="https://shop.vinfastauto.com/on/demandware.static/-/Sites-app_vinfast_vn-Library/default/dw906c2893/images/pdp-vfe34/iris-vfe34.png" 
          alt="VinFast Car" 
          class="w-full h-auto object-contain hover:scale-105 transition-transform duration-500"
        >
        </div>

      <div class="w-full md:w-1/2 p-8 md:p-12">
        
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-2 uppercase tracking-wide">ĐĂNG KÝ LÁI THỬ</h2>
        <p class="text-center text-sm text-gray-500 mb-6">
          Để đăng ký lái thử, Quý khách cần cung cấp giấy phép lái xe cho VinFast
        </p>

        <div class="flex border-b border-gray-200 mb-6">
          <button 
            class="w-1/2 py-3 text-center font-semibold text-blue-600 border-b-2 border-blue-600 focus:outline-none"
          >
            Xe ô tô
          </button>
          <button 
            class="w-1/2 py-3 text-center font-medium text-gray-500 hover:text-gray-700 focus:outline-none"
          >
            Xe máy điện
          </button>
        </div>

        <form @submit.prevent="submitForm" class="space-y-6">
          
          <div>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-3 tracking-wider">Thông tin khách hàng</h3>
            <div class="space-y-4">
              <div class="relative">
                <input 
                  type="text" 
                  v-model="form.customerName" 
                  placeholder="Họ và tên Quý khách *" 
                  class="w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none transition-colors placeholder-gray-400"
                  required
                >
              </div>
              
              <div class="grid grid-cols-2 gap-4">
                <input 
                  type="tel" 
                  v-model="form.phoneNumber" 
                  placeholder="Số điện thoại *" 
                  class="w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none transition-colors placeholder-gray-400"
                  required
                >
                <input 
                  type="email" 
                  v-model="form.email" 
                  placeholder="Email *" 
                  class="w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none transition-colors placeholder-gray-400"
                  required
                >
              </div>
            </div>
          </div>

          <div>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-3 tracking-wider">Lựa chọn mẫu xe</h3>
            <div class="relative">
              <select 
                v-model="form.variantId" 
                class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer"
                required
              >
                <option :value="null" disabled>Chọn mẫu xe *</option>
                <option v-for="car in carVariants" :key="car.id" :value="car.id">
                  {{ car.name }}
                </option>
              </select>
              <div class="absolute right-0 top-3 pointer-events-none">
                <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
              </div>
            </div>
          </div>

          <div>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-3 tracking-wider">Lựa chọn thời gian</h3>
            <div class="grid grid-cols-2 gap-4">
              <div class="relative">
                <input 
                  type="date" 
                  v-model="form.scheduleDate" 
                  class="w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none text-gray-700"
                  required
                  :min="todayStr"
                >
              </div>
              <div class="relative">
                <input 
                  type="time" 
                  v-model="form.scheduleTime" 
                  class="w-full border-b border-gray-300 py-2 focus:border-blue-600 focus:outline-none text-gray-700"
                  required
                >
              </div>
            </div>
          </div>

          <div>
            <h3 class="text-xs font-bold text-gray-400 uppercase mb-3 tracking-wider">Lựa chọn địa điểm</h3>
            <div class="space-y-4">
              <div class="relative">
                <select 
                  v-model="selectedProvince" 
                  class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer"
                  required
                >
                  <option :value="null" disabled>Tỉnh thành *</option>
                  <option v-for="prov in provinces" :key="prov" :value="prov">{{ prov }}</option>
                </select>
                <div class="absolute right-0 top-3 pointer-events-none"><svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg></div>
              </div>

              <div class="relative">
                <select 
                  v-model="selectedShowroom" 
                  :disabled="!selectedProvince"
                  class="w-full border-b border-gray-300 py-2 bg-white focus:border-blue-600 focus:outline-none text-gray-700 appearance-none cursor-pointer disabled:bg-gray-50 disabled:text-gray-400"
                  required
                >
                  <option :value="null" disabled>Showroom / Địa chỉ chi tiết *</option>
                  <option v-for="room in showrooms" :key="room.id" :value="room">
                    {{ room.name }} - {{ room.address }}
                  </option>
                </select>
                <div class="absolute right-0 top-3 pointer-events-none"><svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg></div>
              </div>
            </div>
          </div>

          <div v-if="statusMsg.text" :class="`text-sm p-3 rounded ${statusMsg.success ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`">
            {{ statusMsg.text }}
          </div>

          <div class="pt-4">
            <button 
              type="submit" 
              :disabled="isSubmitting"
              class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 px-6 rounded shadow-lg hover:shadow-xl transition-all uppercase tracking-widest flex justify-center items-center"
            >
              <span v-if="isSubmitting" class="mr-2">
                <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              </span>
              {{ isSubmitting ? 'ĐANG GỬI...' : 'ĐĂNG KÝ LÁI THỬ' }}
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

// --- CẤU HÌNH API ---
const API_URL = 'http://localhost:8080/api';

// --- DATA STATE ---
const provinces = ref([]);
const showrooms = ref([]);
const carVariants = ref([]); // Danh sách xe để khách chọn

const selectedProvince = ref(null);
const selectedShowroom = ref(null);
const isSubmitting = ref(false);
const statusMsg = reactive({ text: '', success: false });

const form = reactive({
  customerName: '',
  phoneNumber: '',
  email: '',
  variantId: null,
  scheduleDate: '',
  scheduleTime: ''
});

const todayStr = computed(() => new Date().toISOString().split('T')[0]);

// --- FETCH DATA ---

onMounted(async () => {
  try {
    // 1. Load danh sách Tỉnh
    const provRes = await axios.get(`${API_URL}/showrooms/provinces`);
    provinces.value = provRes.data.result;

    // 2. Load danh sách Xe (Giả định bạn có API này, nếu chưa thì phải tạo)
    // Ví dụ: GET /api/cars/variants
    const carRes = await axios.get(`${API_URL}/cars/variants`); 
    carVariants.value = carRes.data.result; 
  } catch (e) {
    console.error("Lỗi tải dữ liệu ban đầu:", e);
  }
});

// Khi chọn Tỉnh -> Load Showroom
watch(selectedProvince, async (newVal) => {
  showrooms.value = [];
  selectedShowroom.value = null;
  if (newVal) {
    try {
      const res = await axios.get(`${API_URL}/showrooms`, { params: { province: newVal } });
      showrooms.value = res.data.result;
    } catch (e) { console.error(e); }
  }
});

// --- SUBMIT FORM ---
const submitForm = async () => {
  statusMsg.text = '';
  isSubmitting.value = true;

  try {
    // Ghép ngày giờ
    const scheduledAt = `${form.scheduleDate}T${form.scheduleTime}:00`;

    const payload = {
      customerName: form.customerName,
      phoneNumber: form.phoneNumber,
      email: form.email,
      province: selectedProvince.value,
      showroomId: selectedShowroom.value?.id,
      variantId: form.variantId,
      scheduledAt: scheduledAt,
      note: 'Khách đăng ký lái thử từ trang chủ'
    };

    await axios.post(`${API_URL}/consultations`, payload);

    statusMsg.success = true;
    statusMsg.text = "Đăng ký thành công! Chúng tôi sẽ liên hệ sớm.";
    
    // Reset form
    form.customerName = '';
    form.phoneNumber = '';
    form.email = '';
    
  } catch (error) {
    statusMsg.success = false;
    statusMsg.text = error.response?.data?.message || "Đăng ký thất bại.";
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
/* Tùy chỉnh thêm nếu Tailwind chưa đủ */
select {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;        /* standard property for compatibility */
  background-image: none;  /* helps remove the default dropdown arrow in some browsers */
}

/* hide default arrow in IE/Edge */
select::-ms-expand {
  display: none;
}
</style>