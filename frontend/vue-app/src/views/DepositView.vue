<template>
  <div class="h-screen bg-white font-sans text-gray-800 flex flex-col overflow-hidden">
    
    <header class="flex-none h-[80px] flex items-center justify-between px-8 py-4 bg-white z-50 shadow-sm">
      <router-link to="/" class="w-32 block cursor-pointer">
        <img src="@/assets/image/icon_logo/VinFast-logo.svg" alt="VinFast" class="h-8 md:h-10">
      </router-link>
      <router-link to="/login" class="text-sm font-medium hover:text-blue-600 cursor-pointer">Tài khoản</router-link>
    </header>

    <div class="grid grid-cols-12 flex-1 overflow-hidden">
      
      <aside class="col-span-2 border-r bg-gray-50 h-full overflow-y-auto py-8 hide-scrollbar">
        <div v-if="loading" class="text-center text-gray-500 mt-10 text-sm">Đang tải dữ liệu...</div>
        
        <div v-else class="flex flex-col gap-6 px-4">
          <div 
            v-for="car in filteredCars" 
            :key="car.id"
            @click="selectCar(car)"
            :class="['cursor-pointer text-center group transition-all duration-300', selectedCar?.id === car.id ? 'opacity-100 scale-105' : 'opacity-60 hover:opacity-100']"
          >
            <div class="relative mb-2">
                <img :src="car.thumbnail" class="w-full object-contain h-24 mx-auto transition-transform group-hover:-translate-y-1">
                <div v-if="selectedCar?.id === car.id" class="absolute -bottom-2 left-1/2 -translate-x-1/2 w-12 h-1 bg-blue-500 rounded-full"></div>
            </div>
            <h3 :class="['font-bold text-sm uppercase tracking-wider', selectedCar?.id === car.id ? 'text-black' : 'text-gray-500']">{{ car.name }}</h3>
          </div>
        </div>
      </aside>

      <main class="col-span-7 relative flex flex-col pt-8 h-full overflow-hidden">
        
        <div v-if="currentStep === 1" class="flex-none flex justify-center gap-16 mb-6 border-b mx-12">
            <button 
                @click="selectedCategory = 'ev'"
                :class="['pb-4 border-b-2 px-4 font-bold transition-colors', selectedCategory === 'ev' ? 'border-blue-600 text-blue-600' : 'border-transparent text-gray-400 hover:text-gray-600']"
            >
                Dòng xe cá nhân
            </button>
            <button 
                @click="selectedCategory = 'service'"
                :class="['pb-4 border-b-2 px-4 font-bold transition-colors', selectedCategory === 'service' ? 'border-blue-600 text-blue-600' : 'border-transparent text-gray-400 hover:text-gray-600']"
            >
                Dòng xe dịch vụ
            </button>
        </div>

        <div v-if="currentStep === 1 && selectedCar" class="flex-1 flex flex-col overflow-y-auto pb-4">
            <div class="flex-none flex flex-col items-center justify-center relative min-h-[350px]">
                <transition name="fade" mode="out-in">
                    <img :key="selectedCar.id + (selectedColor?.id || 'def')" :src="currentDisplayImage" class="max-w-[85%] max-h-[400px] object-contain drop-shadow-2xl z-10">
                </transition>
                <div class="absolute bottom-0 w-[80%] h-24 bg-black/10 blur-[50px] rounded-full scale-y-50"></div>
            </div>

            <div class="mt-auto px-16 pt-8">
                <div class="grid grid-cols-3 gap-8 text-center">
                    <div>
                        <h4 class="text-xs text-gray-500 mb-1">Công suất tối đa</h4>
                        <p class="text-xl font-bold">{{ selectedCar.specs.power }}</p>
                    </div>
                    
                    <div v-if="selectedCategory === 'ev'">
                        <h4 class="text-xs text-gray-500 mb-1">Quãng đường (NEDC)</h4>
                         <p class="text-xs text-gray-400 mb-1">lên tới</p>
                         <p class="text-xl font-bold">{{ selectedCar.specs.range }}</p>
                    </div>
                    <div v-else>
                        <h4 class="text-xs text-gray-500 mb-1">Loại xe</h4>
                        <p class="text-xl font-bold">{{ selectedCar.specs.type }}</p>
                    </div>

                    <div>
                        <h4 class="text-xs text-gray-500 mb-1">Số chỗ ngồi</h4>
                        <p class="text-xl font-bold">{{ selectedCar.specs.seats }}</p>
                    </div>
                </div>
                <p class="text-[10px] text-gray-400 text-center mt-8 italic max-w-2xl mx-auto leading-relaxed">
                    *Thông số kỹ thuật mang tính chất tham khảo và có thể thay đổi tùy theo cấu hình thực tế.
                </p>
            </div>
        </div>

        <div v-if="currentStep === 2" class="flex-1 px-16 py-8 overflow-y-auto">
            <h2 class="text-2xl font-bold mb-6 text-blue-900">Thông tin khách hàng</h2>
            <form @submit.prevent class="grid grid-cols-2 gap-6 pb-10"> 
                <div class="col-span-2 md:col-span-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên <span class="text-red-500">*</span></label>
                    <input 
                        v-model="customerInfo.fullName" 
                        @blur="validateInput('fullName')"
                        type="text" 
                        :class="['w-full border rounded px-3 py-2 outline-none focus:ring-2', errors.fullName ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500']"
                        placeholder="Nhập họ và tên"
                    >
                    <span v-if="errors.fullName" class="text-red-500 text-xs mt-1 block">{{ errors.fullName }}</span>
                </div>
                <div class="col-span-2 md:col-span-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại <span class="text-red-500">*</span></label>
                    <input 
                        v-model="customerInfo.phone"
                        @blur="validateInput('phone')"
                        type="tel" 
                        :class="['w-full border rounded px-3 py-2 outline-none focus:ring-2', errors.phone ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500']"
                        placeholder="Nhập số điện thoại"
                    >
                    <span v-if="errors.phone" class="text-red-500 text-xs mt-1 block">{{ errors.phone }}</span>
                </div>
                <div class="col-span-2 md:col-span-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Email <span class="text-red-500">*</span></label>
                    <input 
                        v-model="customerInfo.email" 
                        @blur="validateInput('email')"
                        type="email" 
                        :class="['w-full border rounded px-3 py-2 outline-none focus:ring-2', errors.email ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500']"
                        placeholder="Nhập email"
                    >
                     <span v-if="errors.email" class="text-red-500 text-xs mt-1 block">{{ errors.email }}</span>
                </div>
                 <div class="col-span-2 md:col-span-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Số CCCD/CMND</label>
                    <input v-model="customerInfo.citizenId" type="text" class="w-full border border-gray-300 rounded px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập số CCCD">
                </div>

                <div class="col-span-2 border-t pt-6 mt-2">
                    <h3 class="text-lg font-bold mb-4 text-gray-800">Showroom nhận xe</h3>
                </div>

                <div class="col-span-2 md:col-span-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Tỉnh thành <span class="text-red-500">*</span></label>
                    <select v-model="customerInfo.province" class="w-full border border-gray-300 rounded px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Chọn Tỉnh/Thành</option>
                        <option v-for="prov in uniqueProvinces" :key="prov" :value="prov">{{ prov }}</option>
                    </select>
                </div>
                
                <div class="col-span-2 md:col-span-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Showroom <span class="text-red-500">*</span></label>
                    <select v-model="customerInfo.showroomId" class="w-full border border-gray-300 rounded px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500" :disabled="!customerInfo.province">
                        <option value="">Chọn Showroom</option>
                         <option v-for="show in filteredShowrooms" :key="show.id" :value="show.id">
                            {{ show.name }} - {{ show.address }}, {{ show.district }}
                         </option>
                    </select>
                </div>
                 <div class="col-span-2">
                    <label class="block text-sm font-medium text-gray-700 mb-1">Ghi chú</label>
                    <textarea v-model="customerInfo.note" class="w-full border border-gray-300 rounded px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500" placeholder="Yêu cầu khác..."></textarea>
                </div>
            </form>
         </div>

         <div v-if="currentStep === 3" class="flex-1 px-16 py-8 overflow-y-auto">
             <h2 class="text-2xl font-bold mb-6 text-blue-900">Xác nhận đặt cọc</h2>
             
             <div class="bg-gray-50 p-6 rounded-lg border border-gray-200 mb-8 space-y-4">
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Dòng xe</span>
                     <span class="font-bold text-lg text-blue-900">{{ selectedCar.name }}</span>
                 </div>
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Giá trị xe (Niêm yết)</span>
                     <span class="font-bold">{{ selectedCar.priceFormatted }}</span>
                 </div>
                  <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Màu ngoại thất</span>
                     <span class="font-bold">{{ selectedColor.name }}</span>
                 </div>
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Khách hàng</span>
                     <span class="font-bold">{{ customerInfo.fullName }}</span>
                 </div>
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Số điện thoại</span>
                     <span class="font-bold">{{ customerInfo.phone }}</span>
                 </div>
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Email</span>
                     <span class="font-bold">{{ customerInfo.email }}</span>
                 </div>
                 <div v-if="customerInfo.citizenId" class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Số CCCD/CMND</span>
                     <span class="font-bold">{{ customerInfo.citizenId }}</span>
                 </div>
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Tỉnh thành</span>
                     <span class="font-bold">{{ customerInfo.province }}</span>
                 </div>
                 <div class="flex justify-between items-center pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Showroom nhận xe</span>
                     <span class="font-bold text-right max-w-[50%]">{{ getFullShowroomInfo(customerInfo.showroomId) }}</span>
                 </div>
                 <div v-if="customerInfo.note" class="flex flex-col gap-2 pb-4 border-b border-gray-200">
                     <span class="text-gray-600">Ghi chú:</span>
                     <p class="text-sm italic text-gray-700 bg-white p-3 rounded-md border border-dashed border-gray-300">{{ customerInfo.note }}</p>
                 </div>
             </div>

             <div class="mb-8">
                 <h3 class="font-bold text-lg mb-4">Hình thức thanh toán</h3>
                 <div class="border rounded-lg p-4 flex items-center gap-4 cursor-pointer hover:bg-blue-50 border-blue-500 bg-blue-50">
                     <div class="w-6 h-6 rounded-full border-2 border-blue-600 flex items-center justify-center p-1">
                         <div class="w-full h-full bg-blue-600 rounded-full"></div>
                     </div>
                     <img src="https://vnpayment.vn/images/logo-vnpayment.png" alt="VNPayment" class="h-8 object-contain">
                     <span class="font-medium text-blue-900">Thanh toán qua VNPAY-QR</span>
                 </div>
             </div>

              <div class="bg-blue-900 text-white p-6 rounded-lg flex justify-between items-center mb-10">
                  <div>
                      <p class="text-blue-200 text-sm mb-1">Số tiền đặt cọc</p>
                      <p class="text-3xl font-bold">{{ formatCurrency(depositAmount) }}</p>
                  </div>
              </div>
         </div>

      </main>

      <aside class="col-span-3 bg-white pl-8 pr-12 py-8 flex flex-col h-full overflow-y-auto border-l">
        
        <div class="flex-none flex items-center justify-between mb-12 text-xs font-bold text-gray-400">
            <div :class="['flex items-center gap-2', currentStep >= 1 ? 'text-blue-600' : '']">
                <span :class="['w-5 h-5 rounded-full flex items-center justify-center text-[10px]', currentStep >= 1 ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-500']">1</span>
                <span>Lựa chọn xe</span>
            </div>
            <span :class="['w-8 border-t', currentStep >= 2 ? 'border-blue-600' : 'border-gray-200']"></span>
            <div :class="['flex items-center gap-2', currentStep >= 2 ? 'text-blue-600' : '']">
                <span :class="['w-5 h-5 rounded-full flex items-center justify-center text-[10px]', currentStep >= 2 ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-500']">2</span>
                <span>Thông tin</span>
            </div>
            <span :class="['w-8 border-t', currentStep >= 3 ? 'border-blue-600' : 'border-gray-200']"></span>
             <div :class="['flex items-center gap-2', currentStep >= 3 ? 'text-blue-600' : '']">
                 <span :class="['w-5 h-5 rounded-full flex items-center justify-center text-[10px]', currentStep >= 3 ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-500']">3</span>
                 <span>Đặt cọc</span>
             </div>
        </div>

        <p v-if="currentStep === 1" class="flex-none text-sm text-gray-600 mb-8 italic leading-relaxed">
            Xin mời Quý khách vui lòng chọn phiên bản và màu sắc xe.
        </p>
        <p v-else-if="currentStep === 2" class="flex-none text-sm text-gray-600 mb-8 italic leading-relaxed">
            Vui lòng nhập đầy đủ thông tin để chúng tôi phục vụ Quý khách tốt nhất.
        </p>
         <p v-else-if="currentStep === 3" class="flex-none text-sm text-gray-600 mb-8 italic leading-relaxed">
            Xác nhận thông tin và tiến hành thanh toán đặt cọc an toàn.
        </p>

        <div v-if="currentStep === 1 && selectedCar" class="space-y-8 flex-1">
            <div>
                <div class="flex justify-between items-center mb-4">
                    <h3 class="font-medium text-lg">Ngoại thất</h3>
                    <span class="text-sm text-gray-500">{{ selectedColor.name }}</span>
                </div>
                
                <div class="flex flex-wrap gap-3 mb-6">
                    <div 
                        v-for="color in availableColors" 
                        :key="color.id"
                        @click="selectColor(color)" 
                        :class="['w-10 h-10 rounded-md cursor-pointer border-2 shadow-sm transition-all', selectedColor.id === color.id ? 'border-blue-600 scale-110' : 'border-transparent hover:scale-105']"
                        :style="{ backgroundColor: color.hex }"
                    ></div>
                </div>
            </div>

            <div class="border-t pt-6 space-y-3">
                <div class="flex justify-between text-sm text-gray-600">
                    <span>Giá niêm yết</span>
                    <span class="font-bold text-black">{{ selectedCar.priceFormatted }}</span>
                </div>
            </div>
        </div>
        
        <div v-else class="flex-1">
             <div class="bg-gray-50 p-4 rounded border">
                <div class="flex gap-4 mb-4">
                    <img :src="selectedCar?.thumbnail" class="w-20 object-contain bg-white rounded border">
                    <div>
                        <h4 class="font-bold text-sm">{{ selectedCar?.name }}</h4>
                        <p class="text-xs text-gray-500">{{ selectedColor.name }}</p>
                    </div>
                </div>
                 <div class="flex justify-between text-sm border-t pt-2 mt-2">
                    <span class="text-gray-600">Giá xe</span>
                    <span class="font-bold">{{ selectedCar?.priceFormatted }}</span>
                </div>
            </div>
        </div>

        <div class="mt-8 pt-6 border-t flex-none">
            <template v-if="currentStep === 1">
                <div class="flex justify-between items-center mb-4">
                    <span class="text-sm font-medium text-gray-600">Giá xe kèm pin</span>
                    <span class="text-lg font-bold">{{ selectedCar?.priceFormatted }}</span>
                </div>
                <button @click="nextStep" class="w-full bg-blue-600 text-white font-bold py-4 rounded hover:bg-blue-700 transition-all uppercase tracking-wide shadow-lg transform hover:-translate-y-0.5">
                    Bước tiếp theo
                </button>
            </template>

             <template v-if="currentStep === 2">
                <div class="flex gap-4">
                     <button @click="currentStep--" class="w-1/3 bg-gray-200 text-gray-700 font-bold py-4 rounded hover:bg-gray-300 transition-all uppercase tracking-wide">
                        Quay lại
                    </button>
                    <button @click="nextStep" class="w-2/3 bg-blue-600 text-white font-bold py-4 rounded hover:bg-blue-700 transition-all uppercase tracking-wide shadow-lg">
                        Tiếp tục
                    </button>
                </div>
            </template>

             <template v-if="currentStep === 3">
                <div class="flex gap-4">
                     <button @click="currentStep--" class="w-1/3 bg-gray-200 text-gray-700 font-bold py-4 rounded hover:bg-gray-300 transition-all uppercase tracking-wide">
                        Sửa
                    </button>
                    <button @click="handlePayment" class="w-2/3 bg-blue-600 text-white font-bold py-4 rounded hover:bg-blue-700 transition-all uppercase tracking-wide shadow-lg flex items-center justify-center gap-2">
                        <span>Thanh toán</span>
                    </button>
                </div>
            </template>
        </div>

      </aside>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

async function fetchData(url) {
    try {
        const res = await fetch(url)
        return await res.json()
    } catch (e) {
        console.error(e)
        return null
    }
}

// --- STATE ---
const loading = ref(true)
const currentStep = ref(1)
const selectedCategory = ref('ev')
const allCars = ref([])
const showroomsData = ref([])

const selectedCar = ref(null)

const availableColors = ref([
    { id: 'white', name: 'Brahminy White', hex: '#ffffff' },
    { id: 'red', name: 'Crimson Red', hex: '#dc2626' },
    { id: 'grey', name: 'Neptune Grey', hex: '#6b7280' },
    { id: 'blue', name: 'VinFast Blue', hex: '#2563eb' },
    { id: 'yellow', name: 'Sunset Yellow', hex: '#fbbf24' },
])
const selectedColor = ref(availableColors.value[0])

const customerInfo = reactive({
    fullName: '',
    phone: '',
    email: '',
    citizenId: '',
    province: '',
    showroomId: '',
    note: ''
})

const errors = reactive({ fullName: '', phone: '', email: '' })

// --- COMPUTED ---
const formatCurrency = (val) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val)

const currentDisplayImage = computed(() => selectedCar.value?.thumbnail || '')

const filteredCars = computed(() => allCars.value.filter(c => c.category === selectedCategory.value))

const depositAmount = computed(() => {
    if(!selectedCar.value) return 0;
    const price = selectedCar.value.rawPrice || 0;
    if(price < 400000000) return 10000000;
    if(price <= 800000000) return 15000000;
    return 50000000;
})

const uniqueProvinces = computed(() => {
    const p = showroomsData.value.map(s => s.province)
    return [...new Set(p)]
})

const filteredShowrooms = computed(() => {
    if(!customerInfo.province) return []
    return showroomsData.value.filter(s => s.province === customerInfo.province)
})

// Helper hiển thị thông tin showroom đầy đủ ở bước Xác nhận
const getFullShowroomInfo = (id) => {
    const s = showroomsData.value.find(x => x.id === id)
    if (!s) return ''
    return `${s.name} - ${s.address}, ${s.district}`
}

// --- MAIN ACTIONS ---
onMounted(async () => {
    loading.value = true
    try {
        const [evRes, gasRes, serRes, showroomRes] = await Promise.all([
            fetchData('http://localhost:8080/api/public/products/ev'),
            fetchData('http://localhost:8080/api/public/products/gasoline'),
            fetchData('http://localhost:8080/api/public/products/service'),
            fetchData('http://localhost:8080/api/public/showrooms')
        ])

        // Hàm normalize đã được cập nhật để bắt nhiều trường hợp tên biến
        const normalize = (car, cat) => ({
            id: car.id,
            name: car.name,
            rawPrice: car.price,
            priceFormatted: formatCurrency(car.price),
            thumbnail: car.image, 
            category: cat,
            specs: {
                // SỬA: Kiểm tra cả camelCase (mặc định Spring) và snake_case (nếu cấu hình)
                power: (car.maxPower || car.max_power || car.power) ? `${car.maxPower || car.max_power || car.power} kW` : 'N/A',
                range: (car.rangeNedc || car.range_nedc || car.range) ? `${car.rangeNedc || car.range_nedc || car.range} km` : 'N/A',
                seats: car.seats,
                type: car.body_type
            }
        })

        let combined = []
        if(evRes?.code === 1000) combined.push(...evRes.result.map(c => normalize(c, 'ev')))
        if(gasRes?.code === 1000) combined.push(...gasRes.result.map(c => normalize(c, 'ev'))) 
        if(serRes?.code === 1000) combined.push(...serRes.result.map(c => normalize(c, 'service')))

        allCars.value = combined

        if(showroomRes?.code === 1000) {
            showroomsData.value = showroomRes.result
        }

        const queryId = route.query.id
        const queryType = route.query.type
        
        if(queryType === 'service') selectedCategory.value = 'service'

        if(queryId) {
            const found = allCars.value.find(c => c.id == queryId)
            if(found) {
                selectedCar.value = found
                selectedCategory.value = found.category
            }
        } else {
            if(filteredCars.value.length > 0) selectedCar.value = filteredCars.value[0]
        }

    } catch (e) {
        console.error("Lỗi khởi tạo:", e)
    } finally {
        loading.value = false
    }
})

const selectCar = (car) => selectedCar.value = car
const selectColor = (color) => selectedColor.value = color

const validateInput = (field) => {
    if(field === 'fullName' && !customerInfo.fullName) errors.fullName = 'Vui lòng nhập họ tên'
    else errors.fullName = ''
    
    if(field === 'phone' && !/^\d{10,11}$/.test(customerInfo.phone)) errors.phone = 'SĐT không hợp lệ'
    else errors.phone = ''
    
    if(field === 'email' && !/^\S+@\S+\.\S+$/.test(customerInfo.email)) errors.email = 'Email không hợp lệ'
    else errors.email = ''
}

const nextStep = () => {
    if(currentStep.value === 1 && !selectedCar.value) return alert("Vui lòng chọn xe!")
    
    if(currentStep.value === 2) {
        validateInput('fullName'); validateInput('phone'); validateInput('email');
        if(errors.fullName || errors.phone || errors.email) return;
        if(!customerInfo.showroomId) return alert("Vui lòng chọn Showroom!")
    }
    currentStep.value++
}

// Tìm đến hàm handlePayment và thay thế bằng đoạn code này:

const handlePayment = async () => {
    // 1. Tạo chuỗi thông tin đơn hàng (VNPAY yêu cầu không dấu càng tốt, nhưng có dấu cũng được nếu đã encode)
    const orderInfoStr = `Dat coc ${selectedCar.value.name} - ${customerInfo.phone}`;

    const payload = {
        amount: depositAmount.value,
        // userId: "user-uuid-003", // Dòng này Backend đã tự xử lý, có thể để hoặc xóa
        
        // 👇 THÊM 4 DÒNG NÀY ĐỂ GỬI DỮ LIỆU FORM XUỐNG BACKEND
        fullName: customerInfo.fullName,
        phone: customerInfo.phone,
        email: customerInfo.email,
        citizenId: customerInfo.citizenId, // Backend đang map field này vào cccd

        variantId: selectedCar.value.id,
        showroomId: customerInfo.showroomId,
        selectedColor: selectedColor.value.name,
        paymentMethod: "VNPAY",
        note: customerInfo.note,
        orderInfo: orderInfoStr
    }

    try {
        loading.value = true;
        // 3. Gọi đúng Endpoint
        const res = await fetch('http://localhost:8080/api/deposits/create-payment', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })

        const data = await res.json();

        // 4. Xử lý kết quả trả về
        if (data.code === 1000 && data.result && data.result.paymentUrl) {
            // QUAN TRỌNG: Chuyển hướng người dùng sang trang thanh toán VNPAY
            window.location.href = data.result.paymentUrl;
        } else {
            alert(data.message || "Không thể tạo giao dịch. Vui lòng thử lại.");
        }
    } catch (e) {
        console.error(e)
        alert("Lỗi kết nối server.");
    } finally {
        loading.value = false;
    }
}

watch(selectedCategory, () => {
    if(!loading.value && filteredCars.value.length > 0) {
        if(selectedCar.value?.category !== selectedCategory.value) {
            selectedCar.value = filteredCars.value[0]
        }
    }
})
</script>

<style scoped>
.hide-scrollbar::-webkit-scrollbar { display: none; }
.hide-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>