<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// --- 1. Khai báo Trạng thái (từ data-loader.js) ---
const bannerData = ref([])
const carsData = ref([])
const bikesData = ref([])
const accessoriesData = ref([])

const currentBannerIndex = ref(0)
const currentCarIndex = ref(0)
const currentBikeIndex = ref(0)

let bannerInterval = null
let carInterval = null
let bikeInterval = null

// --- 2. Hàm Fetch Dữ liệu (từ data-loader.js) ---
// (Lưu ý: các file JSON phải nằm trong thư mục 'public' 
// hoặc import trực tiếp nếu bạn cấu hình Vite)
// Giả sử chúng nằm trong public/assets/js/
async function fetchJson(url) {
  try {
    const response = await fetch(url) // Đường dẫn tuyệt đối từ gốc
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    return await response.json()
  } catch (e) {
    console.error(`Failed to fetch ${url}:`, e);
    return null
  }
}

// --- 3. Dùng 'computed' để lấy sản phẩm hiện tại ---
const currentCar = computed(() => {
  if (!carsData.value || carsData.value.length === 0) return { specs: {} }
  return carsData.value[currentCarIndex.value]
})

const currentBike = computed(() => {
  if (!bikesData.value || bikesData.value.length === 0) return { specs: {} }
  return bikesData.value[currentBikeIndex.value]
})

const featuredAccessories = computed(() => {
  return accessoriesData.value.slice(0, 4)
})

// --- 4. Các hàm xử lý (thay cho event listeners) ---
function nextCar() {
  currentCarIndex.value = (currentCarIndex.value + 1) % carsData.value.length
  startCarAutoplay() // Reset interval
}
function prevCar() {
  currentCarIndex.value = (currentCarIndex.value - 1 + carsData.value.length) % carsData.value.length
  startCarAutoplay()
}

function nextBike() {
  currentBikeIndex.value = (currentBikeIndex.value + 1) % bikesData.value.length
  startBikeAutoplay()
}
function prevBike() {
  currentBikeIndex.value = (currentBikeIndex.value - 1 + bikesData.value.length) % bikesData.value.length
  startBikeAutoplay()
}

// --- 5. Logic Autoplay và Khởi tạo ---
function startCarAutoplay() {
  if (carsData.value.length <= 1) return
  clearInterval(carInterval)
  carInterval = setInterval(nextCar, 5000)
}
function startBikeAutoplay() {
  if (bikesData.value.length <= 1) return
  clearInterval(bikeInterval)
  bikeInterval = setInterval(nextBike, 5000)
}

function startBannerAutoplay() {
  if (bannerData.value.length <= 1) return
  clearInterval(bannerInterval)
  bannerInterval = setInterval(() => {
    currentBannerIndex.value = (currentBannerIndex.value + 1) % bannerData.value.length
  }, 5000)
}

// Dùng onMounted thay cho 'DOMContentLoaded'
onMounted(async () => {
  // Cập nhật đường dẫn tới file JSON
  const [productData, imageData, accessoryData] = await Promise.all([
    fetchJson('/assets/js/product.json'),
    fetchJson('/assets/js/image.json'),
    fetchJson('/assets/js/phukien.json')
  ])

  if (productData) {
    carsData.value = productData.cars.filter(car => car.show === 1)
    bikesData.value = productData.bikes.filter(bike => bike.show === 1)
    if (carsData.value.length > 0) startCarAutoplay()
    if (bikesData.value.length > 0) startBikeAutoplay()
  }
  if (imageData) {
    bannerData.value = imageData.banner
    if (bannerData.value.length > 0) startBannerAutoplay()
  }
  if (accessoryData) {
    accessoriesData.value = accessoryData.pk
  }
  
  // Logic Reveal-on-scroll (có thể giữ nguyên)
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

// Dọn dẹp interval khi rời khỏi trang
onUnmounted(() => {
  clearInterval(bannerInterval)
  clearInterval(carInterval)
  clearInterval(bikeInterval)
})
</script>
<template>
  <main>
    <section id="hero-slider">
      <img 
        v-for="(banner, index) in bannerData"
        :key="banner.image"
        :src="banner.image"
        :alt="`VinFast Banner ${index + 1}`"
        class="slider-image"
        :class="{ 'active': index === currentBannerIndex }"
      />
    </section>

    <section id="cars-showcase" class="py-20 md:py-28 overflow-hidden">
      <div class="container mx-auto px-6">
         <div class="text-center mb-16 reveal">
            <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Dòng Xe Ô Tô Đẳng Cấp</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">Thiết kế tinh xảo, công nghệ vượt trội, khẳng định vị thế dẫn đầu.</p>
        </div>
        
        <template v-if="carsData.length > 0">
          <div id="car-product-container" class="product-showcase grid grid-cols-1 lg:grid-cols-2 gap-y-12 lg:gap-x-8 items-center">
            <div class="product-image">
              <img id="car-image" :src="currentCar.image" alt="Car Image" class="w-full rounded-lg shadow-2xl">
            </div>
            <div class="product-details">
              <h3 id="car-name" class="text-4xl font-extrabold text-gray-900 mb-4">{{ currentCar.name }}</h3>
              <p id="car-price" class="text-3xl font-bold text-blue-600 mb-6">{{ currentCar.price }}</p>
              <div class="grid grid-cols-2 gap-x-3 gap-y-4 text-lg mb-8">
                <div class="flex items-center text-gray-700"><i class="fas fa-road text-blue-500 w-6 mr-3"></i><span id="car-range">{{ currentCar.specs.range || 'N/A' }}</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-car-alt text-blue-500 w-6 mr-3"></i><span id="car-type">{{ currentCar.category || 'N/A' }}</span></div>
                <div class="flex items-center text-gray-700"><i class="fas fa-users text-blue-500 w-6 mr-3"></i><span id="car-seats">{{ currentCar.specs.seats || 'N/A' }}</span></div>
              </div>
              <div class="flex flex-col sm:flex-row gap-4">
                 <a href="#" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105 shadow-md">Đặt Mua</a>
                <router-link 
                  id="car-detail-link" 
                  :to="`product-detail.html?id=${currentCar.id}&type=car`" 
                  class="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105">
                  Xem Chi Tiết
                </router-link>
                <router-link to="/all-products?type=car" class="bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105">Xem Tất Cả</router-link>
              </div>
            </div>
          </div>
          <div v-if="carsData.length > 1" class="flex justify-center mt-12 space-x-4">
            <button @click="prevCar" id="prev-car" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl transition-all duration-300 shadow-md"><i class="fas fa-arrow-left"></i></button>
            <button @click="nextCar" id="next-car" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl transition-all duration-300 shadow-md"><i class="fas fa-arrow-right"></i></button>
          </div>
        </template>
        <div v-else class="text-center p-12 text-gray-500">
          Đang tải dữ liệu xe...
        </div>
      </div>
    </section>

    <section id="bikes-showcase" class="py-20 md:py-28 bg-white overflow-hidden">
        <div class="container mx-auto px-6">
             <div class="text-center mb-16 reveal">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Xe Máy Điện Thông Minh</h2>
                <p class="text-gray-600 text-lg max-w-2xl mx-auto">Phong cách thời thượng, vận hành êm ái, vì một tương lai di chuyển xanh.</p>
            </div>
            
            <template v-if="bikesData.length > 0">
              <div id="bike-product-container" class="product-showcase grid grid-cols-1 lg:grid-cols-2 gap-y-12 lg:gap-x-8 items-center">
                 <div class="product-details lg:order-1">
                    <h3 id="bike-name" class="text-4xl font-extrabold text-gray-900 mb-4">{{ currentBike.name }}</h3>
                    <p id="bike-price" class="text-3xl font-bold text-blue-600 mb-6">{{ currentBike.price }}</p>
                    <div class="grid grid-cols-2 gap-x-3 gap-y-4 text-lg mb-8">
                        <div class="flex items-center text-gray-700"><i class="fas fa-road text-blue-500 w-6 mr-3"></i><span id="bike-range">{{ currentBike.specs.range || 'N/A' }}</span></div>
                        <div class="flex items-center text-gray-700"><i class="fas fa-motorcycle text-blue-500 w-6 mr-3"></i><span id="bike-type">Xe máy điện</span></div>
                        <div class="flex items-center text-gray-700"><i class="fas fa-users text-blue-500 w-6 mr-3"></i><span id="bike-seats">{{ currentBike.specs.seats || '2 Chỗ' }}</span></div>
                    </div>
                    <div class="flex flex-col sm:flex-row gap-4">
                         <a href="#" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105 shadow-md">Đặt Mua</a>
                        <router-link 
                          id="bike-detail-link" 
                          :to="`product-detail.html?id=${currentBike.id}&type=bike`" 
                          class="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105">
                          Xem Chi Tiết
                        </router-link>
                        <router-link to="/all-products?type=bike" class="bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105">Xem Tất Cả</router-link>
                    </div>
                </div>
                 <div class="product-image lg:order-2">
                    <img id="bike-image" :src="currentBike.image" alt="Bike Image" class="w-full rounded-lg shadow-2xl">
                </div>
              </div>
               <div v-if="bikesData.length > 1" class="flex justify-center mt-12 space-x-4">
                <button @click="prevBike" id="prev-bike" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl transition-all duration-300 shadow-md"><i class="fas fa-arrow-left"></i></button>
                <button @click="nextBike" id="next-bike" class="bg-white hover:bg-blue-600 text-gray-700 hover:text-white w-14 h-14 rounded-full text-2xl transition-all duration-300 shadow-md"><i class="fas fa-arrow-right"></i></button>
              </div>
            </template>
             <div v-else class="text-center p-12 text-gray-500">
              Đang tải dữ liệu xe máy...
            </div>
        </div>
    </section>
    
    <section id="accessories" class="py-20 md:py-28">
      <div class="container mx-auto px-6">
         <div class="text-center mb-16 reveal">
            <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Phụ Kiện Chính Hãng</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">Nâng tầm phong cách và cá nhân hóa trải nghiệm lái xe của bạn. </p>
        </div>
        <div id="accessories-grid" class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div 
            v-for="(item, index) in featuredAccessories"
            :key="item.id || index" 
            class="accessory-card bg-white rounded-lg shadow-md overflow-hidden reveal"
            :style="{ transitionDelay: `${index * 100}ms` }"
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
            <router-link to="/all-products?type=accessory" 
               class="bg-slate-200 hover:bg-slate-300 text-slate-800 
                      font-bold py-3 px-8 rounded-full text-center transition-transform duration-300 transform hover:scale-105 shadow-sm">
                Xem Thêm
            </router-link>
        </div>
      </div>
    </section>

    <section id="battery-charging" class="py-20 md:py-28 bg-white">
        <div class="container mx-auto px-6 grid lg:grid-cols-2 gap-12 items-center reveal">
            <div class="text-content">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-6">Pin & Trạm Sạc</h2>
                <p class="text-gray-600 text-lg mb-6">Hệ sinh thái xe điện toàn diện của VinFast đảm bảo mọi hành trình của bạn luôn tràn đầy năng lượng và sự an tâm tuyệt đối.</p>
                <ul class="space-y-4 text-gray-700">
                    <li class="flex items-start">
                        <i class="fas fa-check-circle text-blue-500 mt-1 mr-3"></i>
                        <div>
                            <h4 class="font-bold">Mạng lưới trạm sạc toàn quốc</h4>
                            <p>Hơn 150,000 cổng sạc phủ khắp 63 tỉnh thành, dễ dàng tìm kiếm và sử dụng qua ứng dụng VinFast.</p>
                        </div>
                    </li>
                    <li class="flex items-start">
                        <i class="fas fa-check-circle text-blue-500 mt-1 mr-3"></i>
                        <div>
                            <h4 class="font-bold">Công nghệ Pin LFP vượt trội</h4>
                            <p>An toàn, ổn định, tuổi thọ cao và thân thiện với môi trường, mang lại phạm vi hoạt động ấn tượng.</p>
                        </div>
                    </li>
                     <li class="flex items-start">
                        <i class="fas fa-check-circle text-blue-500 mt-1 mr-3"></i>
                        <div>
                            <h4 class="font-bold">Chính sách thuê pin linh hoạt</h4>
                            <p>Tối ưu chi phí ban đầu, không lo rủi ro về pin và được thay thế pin mới khi khả năng sạc-xả dưới 70%.</p>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="image-content">
                <img src="/assets/image/pk/pin-tramsac-1_1660273470.png" alt="Trạm sạc VinFast" class="rounded-lg shadow-2xl w-full h-full object-cover">
            </div>
        </div>
    </section>

    <section id="after-sales" class="py-20 md:py-28">
        <div class="container mx-auto px-6">
             <div class="text-center mb-16 reveal">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">Dịch Vụ Hậu Mãi Xuất Sắc</h2>
                <p class="text-gray-600 text-lg max-w-2xl mx-auto">Trải nghiệm của bạn không dừng lại sau khi mua xe. Chúng tôi luôn đồng hành cùng bạn.</p>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-10 text-center">
                <div class="bg-white p-8 rounded-lg shadow-lg reveal">
                    <div class="text-blue-500 mb-6"><i class="fas fa-car-mechanic fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Mobile Service</h3>
                    <p class="text-gray-600">Dịch vụ sửa chữa, bảo dưỡng lưu động chính hãng, thuận tiện và linh hoạt, phục vụ bạn mọi lúc mọi nơi.</p>
                </div>
                <div class="bg-white p-8 rounded-lg shadow-lg reveal" style="transition-delay: 150ms;">
                    <div class="text-blue-500 mb-6"><i class="fas fa-road-circle-check fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Cứu Hộ 24/7</h3>
                    <p class="text-gray-600">Dịch vụ cứu hộ pin và sửa chữa lưu động miễn phí trong suốt thời gian bảo hành, đảm bảo an tâm trên mọi nẻo đường.</p>
                </div>
                <div class="bg-white p-8 rounded-lg shadow-lg reveal" style="transition-delay: 300ms;">
                    <div class="text-blue-500 mb-6"><i class="fas fa-award fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Bảo Hành 10 Năm</h3>
                    <p class="text-gray-600">Chính sách bảo hành hàng đầu thị trường, áp dụng cho tất cả các dòng ô tô điện, khẳng định chất lượng toàn cầu.</p>
                </div>
            </div>
        </div>
    </section>

    <section id="why-us" class="py-20 md:py-28 bg-white">
        <div class="container mx-auto px-6">
             <div class="text-center mb-16 reveal">
                <h2 class="text-3xl md:text-5xl font-bold text-gray-900 mb-4">An Tâm Lựa Chọn VinAuto</h2>
                <p class="text-gray-600 text-lg max-w-2xl mx-auto">Chúng tôi không chỉ bán xe, chúng tôi mang đến sự tin cậy và trải nghiệm khác biệt.</p>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-10 text-center">
                <div class="bg-white p-8 rounded-lg shadow-lg reveal">
                    <div class="text-blue-500 mb-6"><i class="fas fa-shield-alt fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Bảo Hành Vượt Trội</h3>
                    <p class="text-gray-600">Chính sách bảo hành lên đến 10 năm, mang lại sự an tâm tuyệt đối trên mọi hành trình.</p>
                </div>
                <div class="bg-white p-8 rounded-lg shadow-lg reveal" style="transition-delay: 150ms;">
                    <div class="text-blue-500 mb-6"><i class="fas fa-cogs fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Công Nghệ Thông Minh</h3>
                    <p class="text-gray-600">Tích hợp trợ lý ảo và hệ thống tự hành tiên tiến, định nghĩa lại trải nghiệm lái xe.</p>
                </div>
                <div class="bg-white p-8 rounded-lg shadow-lg reveal" style="transition-delay: 300ms;">
                    <div class="text-blue-500 mb-6"><i class="fas fa-headset fa-4x"></i></div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">Dịch Vụ Tận Tâm 24/7</h3>
                    <p class="text-gray-600">Hệ thống xưởng dịch vụ rộng khắp và cứu hộ lưu động sẵn sàng hỗ trợ bạn.</p>
                </div>
            </div>
        </div>
    </section>

  </main>
</template>

