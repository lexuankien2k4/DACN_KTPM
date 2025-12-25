<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// --- STATE ---
const bannerData = ref([])
const evCarsData = ref([])       // Xe điện
const gasolineCarsData = ref([]) // Xe xăng
const serviceCarsData = ref([])  // Xe dịch vụ
const accessoriesData = ref([])

// Index điều khiển slider
const currentBannerIndex = ref(0)
const evIndex = ref(0)
const gasIndex = ref(0)
const serIndex = ref(0)

let bannerInterval = null

// --- HELPER FETCH API ---
async function fetchData(url) {
  try {
    const response = await fetch(url)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    return await response.json()
  } catch (e) {
    console.error(`Failed to fetch ${url}:`, e);
    return []
  }
}

// --- COMPUTED PROPERTIES CHO TỪNG LOẠI XE ---
const currentEvCar = computed(() => evCarsData.value[evIndex.value] || {})
const currentGasCar = computed(() => gasolineCarsData.value[gasIndex.value] || {})
const currentSerCar = computed(() => serviceCarsData.value[serIndex.value] || {})

const featuredAccessories = computed(() => accessoriesData.value.slice(0, 4))

// --- ACTIONS ĐIỀU HƯỚNG ---
// Xe điện
const nextEv = () => { evIndex.value = (evIndex.value + 1) % evCarsData.value.length }
const prevEv = () => { evIndex.value = (evIndex.value - 1 + evCarsData.value.length) % evCarsData.value.length }

// Xe xăng
const nextGas = () => { gasIndex.value = (gasIndex.value + 1) % gasolineCarsData.value.length }
const prevGas = () => { gasIndex.value = (gasIndex.value - 1 + gasolineCarsData.value.length) % gasolineCarsData.value.length }

// Xe dịch vụ
const nextSer = () => { serIndex.value = (serIndex.value + 1) % serviceCarsData.value.length }
const prevSer = () => { serIndex.value = (serIndex.value - 1 + serviceCarsData.value.length) % serviceCarsData.value.length }

// Banner Autoplay
function startBannerAutoplay() {
  if (bannerData.value.length <= 1) return
  clearInterval(bannerInterval)
  bannerInterval = setInterval(() => {
    currentBannerIndex.value = (currentBannerIndex.value + 1) % bannerData.value.length
  }, 5000)
}

// --- LIFECYCLE ---
onMounted(async () => {
    // 1. Fetch Banner
    const imageData = await fetchData('/assets/js/image.json')
    if(imageData && imageData.banner) {
          bannerData.value = imageData.banner
          if (bannerData.value.length > 0) startBannerAutoplay()
    }

    // 2. Fetch dữ liệu từ Backend
    try {
      //nếu không chạy ngrol thì thay thành: const evRes = await fetch('http://localhost:8080/api/public/products/ev') 
      //tương tự với các api khác
        // A. Xe Điện
        const evRes = await fetch('/api/public/products/ev')
        const evJson = await evRes.json()
        if (evJson.code === 1000) evCarsData.value = evJson.result

        // B. Xe Xăng
        const gasRes = await fetch('/api/public/products/gasoline')
        const gasJson = await gasRes.json()
        if (gasJson.code === 1000) gasolineCarsData.value = gasJson.result

        // C. Xe Dịch Vụ
        const serRes = await fetch('/api/public/products/service')
        const serJson = await serRes.json()
        if (serJson.code === 1000) serviceCarsData.value = serJson.result

    } catch (e) {
        console.error("Lỗi tải dữ liệu backend", e)
    }

    // 3. Fetch Phụ kiện
    const accessoryData = await fetchData('/assets/js/phukien.json') 
    if (accessoryData) {
      accessoriesData.value = accessoryData.pk
    }

    // Scroll Animation
    const revealElements = document.querySelectorAll('.reveal');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1 });
    revealElements.forEach(el => observer.observe(el));
})

onUnmounted(() => {
  clearInterval(bannerInterval)
})
</script>

<template>
  <main>
    <section id="hero-slider">
      <img 
        v-for="(banner, index) in bannerData"
        :key="banner.image || index"
        :src="banner.image"
        :alt="`VinFast Banner ${index + 1}`"
        class="slider-image"
        :class="{ 'active': index === currentBannerIndex }"
      />
    </section>

    <section id="ev-showcase" class="py-20 md:py-28 overflow-hidden bg-white">
      <div class="container mx-auto px-6">
         <div class="text-center mb-16 reveal">
            <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Ô TÔ ĐIỆN THÔNG MINH</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">Thiết kế tinh xảo, công nghệ vượt trội, khẳng định vị thế dẫn đầu.</p>
        </div>
        
        <template v-if="evCarsData.length > 0">
          <div class="product-showcase grid grid-cols-1 lg:grid-cols-2 gap-y-12 lg:gap-x-8 items-center reveal">
            <div class="product-image">
              <img :src="currentEvCar.image" alt="Car Image" class="w-full rounded-lg shadow-2xl transition-transform duration-500 hover:scale-105">
            </div>
            <div class="product-details">
              <h3 class="text-4xl font-extrabold text-gray-900 mb-4">{{ currentEvCar.name }}</h3>
              <p class="text-3xl font-bold text-blue-600 mb-6">{{ currentEvCar.price ? currentEvCar.price.toLocaleString() + ' VNĐ' : 'Liên hệ' }}</p>
              
              <div class="grid grid-cols-2 gap-x-3 gap-y-4 text-lg mb-8">
                <div class="flex items-center text-gray-700"><i class="fas fa-road text-blue-500 w-6 mr-3"></i><span>{{ currentEvCar.range || 'N/A' }}</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-car-alt text-blue-500 w-6 mr-3"></i><span>{{ currentEvCar.type || 'SUV' }}</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-users text-blue-500 w-6 mr-3"></i><span>{{ currentEvCar.seats || '5' }} Chỗ</span></div>
              </div>
              
              <div class="flex flex-col sm:flex-row gap-4">
<router-link 
  :to="{ path: '/deposit', query: { id: currentEvCar.id, type: 'ev' } }" 
  class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-full text-center shadow-md">
  Đặt Cọc
</router-link>
                 <router-link :to="`/product/${currentEvCar.id}`" class="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-3 px-8 rounded-full text-center">Xem Chi Tiết</router-link>
              </div>
            </div>
          </div>
          <div v-if="evCarsData.length > 1" class="flex justify-center mt-12 space-x-4">
            <button @click="prevEv" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl shadow-md"><i class="fas fa-arrow-left"></i></button>
            <button @click="nextEv" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl shadow-md"><i class="fas fa-arrow-right"></i></button>
          </div>
        </template>
        <div v-else class="text-center p-12 text-gray-500">Đang tải dữ liệu xe điện...</div>
      </div>
    </section>

    <section id="gas-showcase" class="py-20 md:py-28 overflow-hidden bg-gray-50">
      <div class="container mx-auto px-6">
         <div class="text-center mb-16 reveal">
            <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">XE ĐỘNG CƠ XĂNG</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">Mạnh mẽ, sang trọng và đẳng cấp thời thượng.</p>
        </div>
        
        <template v-if="gasolineCarsData.length > 0">
          <div class="product-showcase grid grid-cols-1 lg:grid-cols-2 gap-y-12 lg:gap-x-8 items-center reveal">
            <div class="product-image order-last lg:order-first">
              <img :src="currentGasCar.image" alt="Car Image" class="w-full rounded-lg shadow-2xl transition-transform duration-500 hover:scale-105">
            </div>
            <div class="product-details">
              <h3 class="text-4xl font-extrabold text-gray-900 mb-4">{{ currentGasCar.name }}</h3>
              <p class="text-3xl font-bold text-red-600 mb-6">{{ currentGasCar.price ? currentGasCar.price.toLocaleString() + ' VNĐ' : 'Liên hệ' }}</p>
              
              <div class="grid grid-cols-2 gap-x-3 gap-y-4 text-lg mb-8">
                <div class="flex items-center text-gray-700"><i class="fas fa-gas-pump text-red-500 w-6 mr-3"></i><span>Xăng</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-car-side text-red-500 w-6 mr-3"></i><span>{{ currentGasCar.type }}</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-users text-red-500 w-6 mr-3"></i><span>{{ currentGasCar.seats || '5' }} Chỗ</span></div>
              </div>
              
              <div class="flex flex-col sm:flex-row gap-4">
                 <button disabled class="bg-gray-400 cursor-not-allowed text-white font-bold py-3 px-8 rounded-full text-center shadow-md">Đặt Cọc</button>
                 <router-link :to="`/product/${currentGasCar.id}`" class="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-3 px-8 rounded-full text-center">Xem Chi Tiết</router-link>
              </div>
            </div>
          </div>
          <div v-if="gasolineCarsData.length > 1" class="flex justify-center mt-12 space-x-4">
            <button @click="prevGas" class="bg-white hover:bg-red-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl shadow-md"><i class="fas fa-arrow-left"></i></button>
            <button @click="nextGas" class="bg-white hover:bg-red-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl shadow-md"><i class="fas fa-arrow-right"></i></button>
          </div>
        </template>
        <div v-else class="text-center p-12 text-gray-500">Đang tải dữ liệu xe xăng...</div>
      </div>
    </section>

    <section id="service-showcase" class="py-20 md:py-28 overflow-hidden bg-white">
      <div class="container mx-auto px-6">
         <div class="text-center mb-16 reveal">
            <h2 class="text-3xl md:text-5xl font-bold text-blue-700 mb-4">GIẢI PHÁP VẬN TẢI XANH</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">Dành cho doanh nghiệp kinh doanh dịch vụ vận tải tiên phong.</p>
        </div>
        
        <template v-if="serviceCarsData.length > 0">
          <div class="product-showcase grid grid-cols-1 lg:grid-cols-2 gap-y-12 lg:gap-x-8 items-center reveal">
            <div class="product-image">
              <img :src="currentSerCar.image" alt="Car Image" class="w-full rounded-lg shadow-2xl transition-transform duration-500 hover:scale-105">
            </div>
            <div class="product-details">
              <h3 class="text-4xl font-extrabold text-blue-800 mb-4">{{ currentSerCar.name }}</h3>
              <p class="text-3xl font-bold text-blue-600 mb-6">{{ currentSerCar.price ? currentSerCar.price.toLocaleString() + ' VNĐ' : 'Liên hệ' }}</p>
              
              <div class="grid grid-cols-2 gap-x-3 gap-y-4 text-lg mb-8">
                <div class="flex items-center text-gray-700"><i class="fas fa-bolt text-yellow-500 w-6 mr-3"></i><span>{{ currentSerCar.range || 'N/A' }}</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-taxi text-yellow-500 w-6 mr-3"></i><span>Taxi/Dịch vụ</span></div>
              </div>
              
              <div class="flex flex-col sm:flex-row gap-4">
                 <router-link to="/consultation" class="bg-blue-700 hover:bg-blue-800 text-white font-bold py-3 px-8 rounded-full text-center shadow-md">Liên Hệ Ngay</router-link>
              </div>
            </div>
          </div>
          <div v-if="serviceCarsData.length > 1" class="flex justify-center mt-12 space-x-4">
            <button @click="prevSer" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl shadow-md"><i class="fas fa-arrow-left"></i></button>
            <button @click="nextSer" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl shadow-md"><i class="fas fa-arrow-right"></i></button>
          </div>
        </template>
        <div v-else class="text-center p-12 text-gray-500">Đang tải dữ liệu xe dịch vụ...</div>
      </div>
    </section>

    <section id="accessories" class="py-20 md:py-28 bg-gray-50">
      <div class="container mx-auto px-6">
         <div class="text-center mb-16 reveal">
            <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Phụ Kiện Chính Hãng</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">Nâng tầm phong cách và cá nhân hóa trải nghiệm lái xe của bạn.</p>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div 
            v-for="(item, index) in featuredAccessories"
            :key="item.id || index" 
            class="bg-white rounded-lg shadow-md overflow-hidden reveal"
          >
            <div class="overflow-hidden aspect-square">
              <img :src="item.image" :alt="item.name" class="w-full h-full object-cover">
            </div>
            <div class="p-4">
              <h4 class="font-bold text-gray-800 truncate">{{ item.name }}</h4>
              <p class="text-blue-600 font-semibold">{{ item.gia }}</p>
            </div>
          </div>
        </div>
        <div class="text-center mt-12 reveal">
            <router-link to="/accessories" class="bg-slate-200 hover:bg-slate-300 text-slate-800 font-bold py-3 px-8 rounded-full text-center shadow-sm">Xem Thêm</router-link>
        </div>
      </div>
    </section>

    <section id="battery-charging" class="py-20 md:py-28 bg-white">
        <div class="container mx-auto px-6 grid lg:grid-cols-2 gap-12 items-center reveal">
            <div class="text-content">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-6">Pin & Trạm Sạc</h2>
                <p class="text-gray-600 text-lg mb-6">Hệ sinh thái xe điện toàn diện của VinFast đảm bảo mọi hành trình của bạn luôn tràn đầy năng lượng.</p>
                <ul class="space-y-4 text-gray-700">
                    <li class="flex items-start"><i class="fas fa-check-circle text-blue-500 mt-1 mr-3"></i><div><h4 class="font-bold">Trạm sạc toàn quốc</h4><p>Phủ khắp 63 tỉnh thành.</p></div></li>
                    <li class="flex items-start"><i class="fas fa-check-circle text-blue-500 mt-1 mr-3"></i><div><h4 class="font-bold">Công nghệ Pin LFP</h4><p>An toàn và tuổi thọ cao.</p></div></li>
                </ul>
            </div>
            <div class="image-content">
                <img src="/assets/image/pk/pin-tramsac-1_1660273470.png" alt="Trạm sạc VinFast" class="rounded-lg shadow-2xl w-full h-full object-cover">
            </div>
        </div>
    </section>

    <section id="after-sales" class="py-20 md:py-28 bg-gray-50">
        <div class="container mx-auto px-6">
            <div class="text-center mb-16 reveal">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Dịch Vụ Hậu Mãi Xuất Sắc</h2>
                <p class="text-gray-600 text-lg max-w-2xl mx-auto">Cam kết đồng hành cùng khách hàng trong suốt quá trình sử dụng xe.</p>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-10 text-center">
                <div class="bg-white p-8 rounded-lg shadow-lg reveal">
                    <div class="text-blue-500 mb-6"><i class="fas fa-shield-alt fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Bảo Hành 10 Năm</h3>
                    <p class="text-gray-600">Chính sách bảo hành dẫn đầu thị trường, mang lại sự an tâm tuyệt đối.</p>
                </div>
                <div class="bg-white p-8 rounded-lg shadow-lg reveal" style="transition-delay: 150ms;">
                    <div class="text-blue-500 mb-6"><i class="fas fa-tools fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Cứu Hộ 24/7</h3>
                    <p class="text-gray-600">Dịch vụ cứu hộ miễn phí trong suốt thời gian bảo hành, hỗ trợ mọi lúc mọi nơi.</p>
                </div>
                <div class="bg-white p-8 rounded-lg shadow-lg reveal" style="transition-delay: 300ms;">
                    <div class="text-blue-500 mb-6"><i class="fas fa-charging-station fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Sạc Lưu Động</h3>
                    <p class="text-gray-600">Dịch vụ sạc pin lưu động giúp bạn yên tâm trên mọi cung đường.</p>
                </div>
            </div>
        </div>
    </section>

    <section id="why-us" class="py-20 md:py-28 bg-white">
        <div class="container mx-auto px-6 grid lg:grid-cols-2 gap-12 items-center reveal">
             <div class="image-content order-last lg:order-first">
                <img src="/assets/image/backgroup/vinclub.webp" alt="VinFast Community" class="rounded-lg shadow-2xl w-full h-full object-cover">
            </div>
            <div class="text-content">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-6">Mãnh Liệt Tinh Thần Việt Nam</h2>
                <p class="text-gray-600 text-lg mb-6">VinFast không chỉ là một chiếc xe, mà là niềm tự hào của người Việt, vươn tầm thế giới.</p>
                <div class="space-y-6">
                    <div class="flex items-start">
                        <div class="bg-blue-100 p-3 rounded-full mr-4"><i class="fas fa-globe text-blue-600 text-xl"></i></div>
                        <div><h4 class="font-bold text-xl">Đẳng Cấp Quốc Tế</h4><p class="text-gray-600">Sản phẩm được công nhận tại các thị trường khó tính nhất như Mỹ, Châu Âu.</p></div>
                    </div>
                    <div class="flex items-start">
                        <div class="bg-green-100 p-3 rounded-full mr-4"><i class="fas fa-leaf text-green-600 text-xl"></i></div>
                        <div><h4 class="font-bold text-xl">Cam Kết Xanh</h4><p class="text-gray-600">Tiên phong trong cuộc cách mạng giao thông xanh vì một tương lai bền vững.</p></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

  </main>
</template>

<style scoped>
/* CSS cho Slider Banner */
.slider-image {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0;
  transition: opacity 1s ease-in-out;
}
.slider-image.active {
  opacity: 1;
}

/* Hiệu ứng cuộn Reveal */
.reveal {
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s ease-out;
}
.reveal.visible {
  opacity: 1;
  transform: translateY(0);
}
</style>