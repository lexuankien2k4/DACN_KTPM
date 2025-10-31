<script setup>
import { ref, computed, onMounted } from 'vue'

// --- 1. Khai báo Trạng thái (từ data-loader-iment.js) ---
const allCars = ref([])
const allBanks = ref([])
const scheduleData = ref([]) // Dữ liệu cho modal

// Trạng thái của các lựa chọn (dùng v-model)
const selectedCarId = ref('')
const priceType = ref('list') // 'list' hoặc 'custom'
const customPriceInput = ref('') // Giá trị thô của input
const batteryChoice = ref('exclude')
const selectedLoanDuration = ref(1) // Mặc định 1 năm
const selectedDownPaymentPercent = ref(0.2) // Mặc định 20%
const selectedBankRate = ref(0)
const interestRateInput = ref(0) // Input lãi suất tùy chỉnh

const isModalOpen = ref(false)
const carImageUrl = ref('../image/icon_logo/VinFast-logo.svg') // Ảnh mặc định

const BATTERY_COST = 90000000
const currencyFormatter = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })

// --- 2. Hàm Helpers (từ data-loader-iment.js) ---
function parseFormattedNumber(string) {
  if (!string) return 0
  return parseFloat(string.toString().replace(/[^0-9]/g, '')) || 0
}

function formatCurrencyInput(value) {
  let num = parseFormattedNumber(value)
  return num > 0 ? num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.') : ''
}

// --- 3. Dùng 'computed' thay cho updateCalculations() ---
// Các 'computed' sẽ tự động tính toán lại khi 1 'ref' nó phụ thuộc thay đổi

const selectedCar = computed(() => {
  return allCars.value.find(car => car.id === selectedCarId.value) || null
})

const listPrice = computed(() => {
  if (!selectedCar.value) return 0
  return parseFormattedNumber(selectedCar.value.price)
})

// Giá trị input đã được format
const formattedCustomPrice = computed({
  get: () => formatCurrencyInput(customPriceInput.value),
  set: (val) => { customPriceInput.value = parseFormattedNumber(val) }
})

const currentBasePrice = computed(() => {
  if (priceType.value === 'custom') {
    return parseFormattedNumber(customPriceInput.value)
  }
  return listPrice.value
})

const batteryPrice = computed(() => {
  return batteryChoice.value === 'include' ? BATTERY_COST : 0
})

const totalCarPrice = computed(() => currentBasePrice.value + batteryPrice.value)
const downPayment = computed(() => totalCarPrice.value * selectedDownPaymentPercent.value)
const loanAmount = computed(() => totalCarPrice.value - downPayment.value)

const interestRate = computed(() => (parseFloat(interestRateInput.value) / 100) || 0)

// Tính toán lịch trả nợ (thay thế generateRepaymentSchedule)
const fullSchedule = computed(() => {
  const schedule = []
  if (loanAmount.value <= 0 || interestRate.value <= 0) return { schedule: [], totalInterest: 0 }

  const totalMonths = selectedLoanDuration.value * 12
  const monthlyPrincipal = loanAmount.value / totalMonths
  const monthlyRate = interestRate.value / 12
  
  let openingBalance = loanAmount.value
  let totalInterestPaid = 0

  for (let i = 1; i <= totalMonths; i++) {
    const monthlyInterest = openingBalance * monthlyRate
    totalInterestPaid += monthlyInterest
    const totalPayment = monthlyPrincipal + monthlyInterest
    const closingBalance = openingBalance - monthlyPrincipal

    schedule.push({
      term: i,
      openingBalance: Math.round(openingBalance),
      principal: Math.round(monthlyPrincipal),
      interest: Math.round(monthlyInterest),
      total: Math.round(totalPayment),
      closingBalance: Math.round(closingBalance < 0 ? 0 : closingBalance)
    })
    openingBalance = closingBalance
  }
  return { schedule, totalInterest: totalInterestPaid }
})

const totalInterest = computed(() => fullSchedule.value.totalInterest)
const totalLoanAndInterest = computed(() => loanAmount.value + totalInterest.value)

// --- 4. Hàm xử lý sự kiện (thay cho addEventListener) ---
function onCarChange() {
  if (selectedCar.value) {
    // Cập nhật ảnh (đường dẫn từ 'public')
    carImageUrl.value = "../" + selectedCar.value.image // Giả sử ảnh trong /public/image/...
  } else {
    carImageUrl.value = '../image/icon_logo/VinFast-logo.svg'
  }
  // Tự động chọn giá niêm yết khi đổi xe
  priceType.value = 'list'
}

function onBankChange() {
  // Cập nhật input lãi suất khi chọn ngân hàng
  interestRateInput.value = selectedBankRate.value 
}

function openModal() {
  scheduleData.value = fullSchedule.value.schedule // Gán dữ liệu cho modal
  isModalOpen.value = true
}

function downloadCSV() {
  // (Giữ nguyên logic downloadCSV, chỉ thay nguồn data)
  const headers = ["Ky han", "Du no dau ky", "Goc phai tra", "Lai phai tra", "Goc + Lai", "Du no cuoi ky"];
  let csvContent = "data:text/csv;charset=utf-8," + headers.join(",") + "\n";
  
  fullSchedule.value.schedule.forEach(row => {
      const values = Object.values(row).join(",");
      csvContent += values + "\n";
  });

  const encodedUri = encodeURI(csvContent);
  const link = document.createElement("a");
  link.setAttribute("href", encodedUri);
  link.setAttribute("download", "lich_tra_gop_chi_tiet.csv");
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// --- 5. Tải dữ liệu ban đầu (onMounted) ---
onMounted(async () => {
  // (Giả sử file JSON nằm trong /public/assets/js/)
  try {
    const pResponse = await fetch("../assets/js/product.json")
    const productData = await pResponse.json()
    allCars.value = productData.cars.filter(c => c.show === 1)
  } catch (e) { console.error("Lỗi tải product.json", e) }
  
  try {
    const bResponse = await fetch("../assets/js/banks.json")
    allBanks.value = await bResponse.json()
  } catch (e) { console.error("Lỗi tải banks.json", e) }
})

</script>

<template>
  <main class="py-20 md:py-28">
    <div class="container mx-auto px-6">
      <div class="text-center mb-12">
        <h1 class="text-3xl ...">Dự Toán Chi Phí Trả Góp</h1>
        <p class="text-gray-600 ...">Các giá trị dự toán này chỉ mang tính chất tham khảo...</p>
      </div>
      
      <div class="flex flex-col lg:flex-row gap-12">
        
        <div class="lg:w-1/2 flex flex-col">
          <div class="flex justify-center p-4">
            <img class="img-product h-64 object-contain" :src="carImageUrl" alt="Hình ảnh xe"/>
          </div>
          <div class="bg-white p-6 rounded-lg shadow-lg flex-grow">
            <h3 class="text-2xl ...">Thông tin dự toán</h3>
            <div class="space-y-4 text-gray-700">
              <div class="flex justify-between items-center">
                <span>Giá xe</span>
                <span class="font-semibold text-lg">{{ currencyFormatter.format(totalCarPrice) }}</span>
              </div>
              <hr>
              <div class="flex justify-between items-center">
                <span>Trả trước</span>
                <span class="font-semibold text-lg">{{ currencyFormatter.format(downPayment) }}</span>
              </div>
              <div class="flex justify-between items-center">
                <span>Tiền vay</span>
                <span class="font-semibold text-lg">{{ currencyFormatter.format(loanAmount) }}</span>
              </div>
              <div class="flex justify-between items-center">
                <span>Lãi suất (%/năm)</span>
                <span class="font-semibold text-lg">{{ (interestRate * 100).toFixed(1) }}%</span>
              </div>
              <div class="flex justify-between items-center">
                <span>Tổng tiền lãi</span>
                <span class="font-semibold text-lg text-red-600">{{ currencyFormatter.format(totalInterest) }}</span>
              </div>
              <hr>
              <div class="flex justify-between items-center font-bold text-xl ...">
                <span>Tổng Vay & Lãi</span>
                <span>{{ currencyFormatter.format(totalLoanAndInterest) }}</span>
              </div>
            </div>
            <div class="mt-6">
              <button 
                id="show-schedule-btn" 
                @click="openModal"
                v-if="loanAmount > 0 && interestRate > 0"
                class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors ">
                LỊCH TRẢ GÓP CHI TIẾT
              </button>
            </div>
          </div>
        </div>

        <div class="lg:w-1/2">
          <div class="bg-white p-8 rounded-lg shadow-lg space-y-6 h-full">
            <div>
              <label for="name" class="...">Mẫu xe</label>
              <select id="name" v-model="selectedCarId" @change="onCarChange" class="mt-1 block w-full ...">
                <option value="">Chọn mẫu xe</option>
                <option v-for="car in allCars" :key="car.id" :value="car.id">{{ car.name }}</option>
              </select>
            </div>

            <div>
              <label class="...">Tính theo giá</label>
              <div role="radiogroup" class="space-y-3">
                <div class="flex items-center">
                  <input id="price-type-list" name="price-type" type="radio" value="list" v-model="priceType" class="...">
                  <label for="price-type-list" class="ml-3 ...">
                    Giá xe niêm yết: 
                    <span id="list-price-display" class="font-semibold ...">
                      {{ listPrice > 0 ? currencyFormatter.format(listPrice) : 'Chưa chọn xe' }}
                    </span>
                  </label>
                </div>
                <div class="flex flex-wrap items-center gap-x-3 gap-y-2">
                  <div class="flex items-center">
                    <input id="price-type-custom" name="price-type" type="radio" value="custom" v-model="priceType" class="...">
                    <label for="price-type-custom" class="ml-3 ...">Giá xe sau ưu đãi:</label>
                  </div>
                  <div id="custom-price-container" v-show="priceType === 'custom'" class="flex-1 min-w-[200px]">
                    <input 
                      id="custom-price-input" 
                      type="text" 
                      inputmode="numeric" 
                      v-model="formattedCustomPrice"
                      placeholder="Ví dụ: 100.000.000" 
                      class="...">
                  </div>
                </div>
              </div>
            </div>

            <div>
              <label for="battery" class="...">Pin</label>
              <select id="battery" v-model="batteryChoice" class="mt-1 block w-full ...">
                <option value="include">Bao gồm pin (+90,000,000)</option>
                <option value="exclude">Không bao gồm pin</option>
              </select>
            </div>
            
            <div>
              <label class="...">Thời gian vay (năm)</label>
              <div class="mt-2 grid grid-cols-4 gap-2" id="loan-duration-buttons">
                <button v-for="year in [1, 2, 3, 4, 5, 6, 7, 8]" :key="year"
                  type="button" :data-value="year"
                  @click="selectedLoanDuration = year"
                  :class="{ 'bg-blue-600 text-white': selectedLoanDuration === year }"
                  class="px-3 py-2 border rounded-full ...">
                  {{ year }}
                </button>
              </div>
            </div>

            <div>
              <label class="...">Số tiền trả trước</label>
              <div class="mt-2 grid grid-cols-4 gap-2" id="down-payment-buttons">
                <button v-for="p in [0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9]" :key="p"
                  type="button" :data-value="p"
                  @click="selectedDownPaymentPercent = p"
                  :class="{ 'bg-blue-600 text-white': selectedDownPaymentPercent === p }"
                  class="px-3 py-2 border rounded-full ...">
                  {{ p * 100 }}%
                </button>
              </div>
            </div>

            <div>
              <label for="bank" class="...">Ngân hàng</label>
              <select id="bank" v-model="selectedBankRate" @change="onBankChange" class="mt-1 block w-full ...">
                <option value="0">Chọn ngân hàng</option>
                <option v-for="bank in allBanks" :key="bank.name" :value="bank.interestRate">{{ bank.name }}</option>
              </select>
            </div>
            <div>
              <label for="interest-rate-input" class="...">Lãi suất (%/năm)</label>
              <input id="interest-rate-input" type="number" step="0.1" v-model="interestRateInput" class="mt-1 block w-full ...">
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>

  <div id="schedule-modal" class="fixed inset-0 z-50 flex items-center justify-center p-4" :class="{ 'hidden': !isModalOpen }">
    <div id="schedule-modal-overlay" @click="isModalOpen = false" class="fixed inset-0 bg-black bg-opacity-60"></div>
    
    <div class="relative bg-white p-8 rounded-lg shadow-lg w-full max-w-4xl max-h-[90vh] flex flex-col">
      <div class="flex justify-between items-center mb-6 ...">
        <h2 class="text-2xl ...">Bảng Kế Hoạch Trả Nợ (Dự kiến)</h2>
        <div class="flex items-center gap-4">
          <button @click="downloadCSV" id="download-csv-btn" class="bg-green-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-green-700 transition-colors flex items-center gap-2">
            <i class="fas fa-download"></i> Tải về (CSV)
          </button>
          <button @click="isModalOpen = false" id="close-modal-btn" class="text-gray-500 hover:text-gray-800 text-2xl">
            <i class="fas fa-times"></i>
          </button>
        </div>
      </div>
      <div class="overflow-y-auto">
        <table class="w-full text-sm repayment-table">
          <thead>
            <tr>
              <th>Kỳ hạn</th>
              <th>Dư nợ đầu kỳ</th>
              <th>Gốc phải trả</th>
              <th>Lãi phải trả</th>
              <th>Gốc + Lãi</th>
              <th>Dư nợ cuối kỳ</th>
            </tr>
          </thead>
          <tbody id="schedule-body">
            <tr v-for="row in scheduleData" :key="row.term">
              <td>{{ row.term }}</td>
              <td>{{ currencyFormatter.format(row.openingBalance) }}</td>
              <td>{{ currencyFormatter.format(row.principal) }}</td>
              <td>{{ currencyFormatter.format(row.interest) }}</td>
              <td class="font-semibold">{{ currencyFormatter.format(row.total) }}</td>
              <td>{{ currencyFormatter.format(row.closingBalance) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Thêm CSS của trang installment (ví dụ: table) */
.repayment-table th, .repayment-table td {
    padding: 0.75rem;
    text-align: left;
    border-bottom: 1px solid #e5e7eb;
}
.repayment-table th {
    background-color: #f9fafb;
    font-weight: 600;
    position: sticky;
    top: 0;
}
/* Ẩn/hiện modal (thay vì v-if, bạn có thể dùng class) */
#schedule-modal.hidden {
  display: none;
}
</style>