<template>
  <main class="py-20 md:py-28">
    <div class="container mx-auto px-6">
      <div class="text-center mb-12">
        <h1 class="text-3xl font-bold text-gray-800">Dự Toán Chi Phí Trả Góp</h1>
        <p class="text-gray-600 mt-2">Các giá trị dự toán này chỉ mang tính chất tham khảo...</p>
      </div>
      
      <div class="flex flex-col lg:flex-row gap-12">
        
        <div class="lg:w-1/2 flex flex-col">
          <div class="flex justify-center p-4">
            <img class="img-product h-64 object-contain" :src="carImageUrl" alt="Hình ảnh xe"/>
          </div>
          <div class="bg-white p-6 rounded-lg shadow-lg flex-grow">
            <h3 class="text-2xl font-semibold mb-4">Thông tin dự toán</h3>
            <div class="space-y-4 text-gray-700">
              <div class="flex justify-between items-center">
                <span>Giá xe</span>
                <span class="font-semibold text-lg">{{ currencyFormatter.format(totalCarPrice) }}</span>
              </div>
              <hr>
              <div class="flex justify-between items-center">
                <span>Trả trước ({{ (selectedDownPaymentPercent * 100).toFixed(0) }}%)</span>
                <span class="font-semibold text-lg">{{ currencyFormatter.format(downPaymentAmount) }}</span>
              </div>
              <div class="flex justify-between items-center">
                <span>Tiền vay</span>
                <span class="font-semibold text-lg">{{ currencyFormatter.format(loanAmount) }}</span>
              </div>
              <div class="flex justify-between items-center">
                <span>Lãi suất (%/năm)</span>
                <span class="font-semibold text-lg">{{ interestRateInput ? interestRateInput.toFixed(1) : '0.0' }}%</span>
              </div>
              <div class="flex justify-between items-center">
                <span>Tổng tiền lãi (dự kiến)</span>
                <span class="font-semibold text-lg text-red-600">{{ currencyFormatter.format(totalInterest) }}</span>
              </div>
              <hr>
              <div class="flex justify-between items-center font-bold text-xl text-gray-800">
                <span>Tổng Vay & Lãi</span>
                <span>{{ currencyFormatter.format(totalLoanAndInterest) }}</span>
              </div>
            </div>
            <div class="mt-6">
              <button  id="show-schedule-btn" 
@click="calculateAndShowModal"
 :disabled="isLoading || totalCarPrice <= 0 || !interestRateInput"
 class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors disabled:bg-gray-400">
                {{ isLoading ? 'Đang tính toán...' : 'LỊCH TRẢ GÓP CHI TIẾT' }}
              </button>
            </div>
          </div>
        </div>

        <div class="lg:w-1/2">
          <div class="bg-white p-8 rounded-lg shadow-lg space-y-6 h-full">
            <div>
              <label for="model" class="block font-medium text-gray-700">Mẫu xe</label>
              <select id="model" v-model="selectedModelId" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
                <option :value="null" disabled>Chọn mẫu xe</option>
                <option v-for="car in allCars" :key="car.id" :value="car.id">{{ car.name }}</option>
              </select>
            </div>

            <div v-if="allVersions.length > 0">
              <label for="version" class="block font-medium text-gray-700">Phiên bản</label>
              <select id="version" v-model="selectedVersion" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
                <option :value="null" disabled>Chọn phiên bản</option>
                <option v-for="version in allVersions" :key="version.id" :value="version">{{ version.name }}</option>
              </select>
            </div>

            <div>
              <label class="block font-medium text-gray-700">Giá xe</label>
              <div role="radiogroup" class="space-y-3">
                <div class="flex items-center">
                  <input id="price-type-list" name="price-type" type="radio" value="list" v-model="priceType" class="h-4 w-4 text-blue-600 border-gray-300">
                  <label for="price-type-list" class="ml-3 block text-sm text-gray-700">
                    Giá niêm yết: 
                    <span id="list-price-display" class="font-semibold text-gray-900">
                      {{ listPrice > 0 ? currencyFormatter.format(listPrice) : 'Chưa chọn xe' }}
                    </span>
                  </label>
                </div>
                <div class="flex flex-wrap items-center gap-x-3 gap-y-2">
                  <div class="flex items-center">
                    <input id="price-type-custom" name="price-type" type="radio" value="custom" v-model="priceType" class="h-4 w-4 text-blue-600 border-gray-300">
                    <label for="price-type-custom" class="ml-3 block text-sm text-gray-700">Giá xe sau ưu đãi:</label>
                  </div>
                  <div id="custom-price-container" v-show="priceType === 'custom'" class="flex-1 min-w-[200px]">
                    <input 
                      id="custom-price-input" 
                      type="text" 
                      inputmode="numeric" 
                      v-model="customPriceInput"
                      @input="formatCustomPriceInput"
                      placeholder="Ví dụ: 1.000.000.000" 
                      class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
                  </div>
                </div>
              </div>
            </div>

            <div>
              <label for="battery" class="block font-medium text-gray-700">Pin</label>
              <select id="battery" v-model="batteryChoice" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
                <option value="include">Bao gồm pin (+90,000,000)</option>
                <option value="exclude">Không bao gồm pin</option>
              </select>
            </div>
            
            <div>
              <label class="block font-medium text-gray-700">Thời gian vay (năm)</label>
              <div class="mt-2 grid grid-cols-4 gap-2" id="loan-duration-buttons">
                <button v-for="year in [1, 2, 3, 4, 5, 6, 7, 8]" :key="year"
 type="button" :data-value="year"
 @click="selectedLoanDuration = year"
                  :class="[selectedLoanDuration === year ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 hover:bg-gray-50']"
                  class="px-3 py-2 border rounded-full text-sm font-medium transition-colors">
                  {{ year }}
                </button>
              </div>
            </div>

            <div>
              <label class="block font-medium text-gray-700">Số tiền trả trước</label>
              <div class="mt-2 grid grid-cols-4 gap-2" id="down-payment-buttons">
                <button v-for="p in [0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9]" :key="p"
 type="button" :data-value="p"
                  @click="selectedDownPaymentPercent = p"
                  :class="[selectedDownPaymentPercent === p ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 hover:bg-gray-50']"
 class="px-3 py-2 border rounded-full text-sm font-medium transition-colors">
                  {{ p * 100 }}%
                </button>
              </div>
            </div>
            
<div>
  <label for="bank" class="block font-medium text-gray-700">Ngân hàng</label>
  <select id="bank" v-model="selectedBank" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
    <option :value="null" disabled>Chọn ngân hàng</option>
    <option v-for="bank in allBanks" :key="bank.id" :value="bank">{{ bank.name }}</option>
  </select>
</div>

            <div v-if="selectedBank">
              <label for="policy" class="block font-medium text-gray-700">Gói Vay Ưu đãi</label>
              <select id="policy" v-model="selectedPolicy" :disabled="isLoadingPolicies" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
                <option :value="null" disabled>Chọn gói vay</option>
                <option v-if="isLoadingPolicies" :value="null" disabled>Đang tải chính sách...</option>
                <option v-for="policy in bankPolicies" :key="policy.id" :value="policy">
                  {{ policy.name }} ({{ policy.preferentialRatePercent }}% trong {{ policy.preferentialRateMonths }} tháng)
                </option>
              </select>
            </div>
            <div>
              <label for="interest-rate-input" class="block font-medium text-gray-700">Lãi suất (%/năm) *</label>
              <input id="interest-rate-input" type="number" step="0.1" v-model.number="interestRateInput" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm">
              <p class="text-xs text-gray-500 mt-1">* Lãi suất này được tự động điền từ gói vay, bạn có thể chỉnh sửa.</p>
            </div>

          </div>
        </div>
      </div>
    </div>
  </main> 

    <div id="schedule-modal" class="fixed inset-0 z-50 flex items-center justify-center p-4" :class="{ 'hidden': !isModalOpen }">
      <div id="schedule-modal-overlay" @click="isModalOpen = false" class="fixed inset-0 bg-black bg-opacity-60"></div>
      
      <div class="relative bg-white p-8 rounded-lg shadow-lg w-full max-w-4xl max-h-[90vh] flex flex-col">
        <div class="flex justify-between items-center mb-6 border-b pb-4">
          <h2 class="text-2xl font-semibold">Bảng Kế Hoạch Trả Nợ (Dự kiến)</h2>
          <div class="flex items-center gap-4">
            <button @click="downloadCSV" id="download-csv-btn" class="bg-green-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-green-700 transition-colors flex items-center gap-2">
              Tải về (CSV)
            </button>
            <button @click="isModalOpen = false" id="close-modal-btn" class="text-gray-500 hover:text-gray-800 text-2xl font-bold">
              &times;
            </button>
          </div>
        </div>
        <div class="overflow-y-auto">
          <div v-if="isLoading" class="text-center p-8">Đang tính toán...</div>
          <table v-if="scheduleData.length > 0" class="w-full text-sm repayment-table">
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
              <tr v-for="row in scheduleData" :key="row.month">
                <td class="text-center">{{ row.month }}</td>
                <td>{{ currencyFormatter.format(row.remainingBalance + row.principalPaid) }}</td>
                <td>{{ currencyFormatter.format(row.principalPaid) }}</td>
                <td>{{ currencyFormatter.format(row.interestPaid) }}</td>
                <td class="font-semibold">{{ currencyFormatter.format(row.totalPayment) }}</td>
                <td>{{ currencyFormatter.format(row.remainingBalance) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import axios from 'axios'
import VinfastLogo from "@/assets/image/icon_logo/VinFast-logo.svg";


// --- 1. Cấu hình API Client ---
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // Đảm bảo URL backend đúng
  headers: {
    'Content-Type': 'application/json'
  }
});

// --- 2. Khai báo Trạng thái ---
const allCars = ref([]) 
const allBanks = ref([]) 
const allVersions = ref([]) 
const scheduleData = ref([]) 

// 🔑 KHAI BÁO MỚI CHO POLICY
const bankPolicies = ref([]) // Danh sách chính sách của ngân hàng đã chọn
const selectedPolicy = ref(null) // Policy object đã chọn
const isLoadingPolicies = ref(false) // Trạng thái tải policy

// Trạng thái của các lựa chọn (dùng v-model)
const selectedModelId = ref(null) 
const selectedVersion = ref(null) 
const priceType = ref('list') 
const customPriceInput = ref('') 
const batteryChoice = ref('exclude')
const selectedLoanDuration = ref(7) 
const selectedDownPaymentPercent = ref(0.3) 
const selectedBank = ref(null) // TOÀN BỘ OBJECT ngân hàng
const interestRateInput = ref(9.5) // Input lãi suất tùy chỉnh

const isModalOpen = ref(false)
const carImageUrl = ref(VinfastLogo);
const isLoading = ref(false) 
const apiError = ref(null) 

const BATTERY_COST = 90000000 

// --- 3. Các hàm Computed (Tính toán động) ---
// (Giữ nguyên phần này)
const listPrice = computed(() => {
  return selectedVersion.value ? selectedVersion.value.price : 0
})

const customPrice = computed(() => {
  return parseFormattedNumber(customPriceInput.value)
})

const totalCarPrice = computed(() => {
  let basePrice = 0
  if (priceType.value === 'custom' && customPrice.value > 0) {
    basePrice = customPrice.value
  } else {
    basePrice = listPrice.value
  }
  
  if (batteryChoice.value === 'include') {
    return basePrice + BATTERY_COST
  }
  return basePrice
})

const downPaymentAmount = computed(() => {
  return totalCarPrice.value * selectedDownPaymentPercent.value
})

const loanAmount = computed(() => {
  return totalCarPrice.value - downPaymentAmount.value
})

const totalInterest = ref(0);
const totalLoanAndInterest = ref(0);

// --- 4. Logic Tải dữ liệu & Watchers ---

// Hàm Tải dữ liệu ban đầu (Giữ nguyên)
onMounted(async () => {
  isLoading.value = true
  apiError.value = null
  
  // 1. GỌI API DÒNG XE (Mẫu xe)
  try {
    const pResponse = await apiClient.get("/models/active"); 
    allCars.value = pResponse.data.result; 
  } catch (e) { 
    console.error("Lỗi gọi API /models/active:", e);
    apiError.value = "Không thể tải danh sách dòng xe."
  }
  
  // 2. GỌI API NGÂN HÀNG
  try {
    const bResponse = await apiClient.get("/banks/active"); 
    allBanks.value = bResponse.data.result;
  } catch (e) { 
    console.error("Lỗi gọi API /banks/active:", e);
    apiError.value = "Không thể tải danh sách ngân hàng."
  }
  
  isLoading.value = false
})

// WATCHER: Khi người dùng chọn 1 DÒNG XE (Giữ nguyên)
watch(selectedModelId, async (newModelId) => {
  allVersions.value = [] 
  selectedVersion.value = null 
  if (newModelId) {
    isLoading.value = true
    try {
      const vResponse = await apiClient.get(`/variants?modelId=${newModelId}`);
      allVersions.value = vResponse.data.result; 
      
      if (allVersions.value.length > 0) {
        selectedVersion.value = allVersions.value[0]; 
      }
    } catch (e) {
      console.error("Lỗi gọi API /variants:", e);
      apiError.value = "Không thể tải danh sách phiên bản."
    }
    isLoading.value = false
  }
})

// WATCHER: Khi người dùng chọn 1 PHIÊN BẢN (Giữ nguyên)
watch(selectedVersion, (newVersion) => {
  if (newVersion) {
    carImageUrl.value = newVersion.firstImageUrl || 'https://via.placeholder.com/600x400?text=' + newVersion.name;
  } else {
    carImageUrl.value = 'https://via.placeholder.com/600x400?text=Chọn+xe';
  }
  triggerCalculationSummary();
})

// 🔑 WATCHER: Khi người dùng chọn NGÂN HÀNG (Tải danh sách Policy)
watch(selectedBank, async (newBank) => {
    
    // 1. Reset các biến phụ thuộc (Policy)
    bankPolicies.value = []
    selectedPolicy.value = null
    
    // 2. Củng cố giá trị input quan trọng
    // Đặt lại lãi suất mặc định HỢP LỆ (Tránh để logic phụ thuộc vào Policy tải xong)
    interestRateInput.value = 7.5; // Dùng giá trị mặc định hợp lệ (> 0)
    
    // Đảm bảo selectedLoanDuration không bị reset về null
    if (!selectedLoanDuration.value || selectedLoanDuration.value <= 0) {
        selectedLoanDuration.value = 7; 
    }
    
    // 3. Xử lý logic tải Policy
    if (newBank) {
        isLoadingPolicies.value = true
        try {
            // 🔑 GỌI API MỚI: Lấy chính sách theo ID ngân hàng
            const pResponse = await apiClient.get(`/finance/policies/by-bank/${newBank.id}/active`);
            bankPolicies.value = pResponse.data.result;
            
            // Tự động chọn chính sách đầu tiên và điền lãi suất
            if (bankPolicies.value.length > 0) {
                 // Gán Policy, điều này sẽ kích hoạt watcher(selectedPolicy) để điền lãi suất chi tiết
                 selectedPolicy.value = bankPolicies.value[0]; 
            }
            
        } catch (e) { 
            console.error("Lỗi gọi API /policies:", e);
            apiError.value = "Không thể tải danh sách chính sách vay."
        }
        isLoadingPolicies.value = false
    }
});

// Dòng ~372: 🔑 WATCHER: Khi người dùng chọn CHÍNH SÁCH (Điền lãi suất, Kỳ hạn và Trả trước)
watch(selectedPolicy, (newPolicy) => {
    if (newPolicy) { 
        // 1. Cập nhật Lãi suất ưu đãi
        // Đảm bảo giá trị là số và lớn hơn 0
        if (newPolicy.preferentialRatePercent && newPolicy.preferentialRatePercent > 0) {
             // Sử dụng parseFloat để đảm bảo giá trị là số trước khi gán
             interestRateInput.value = parseFloat(newPolicy.preferentialRatePercent); 
        } else {
             interestRateInput.value = 7.5; // Giá trị dự phòng hợp lệ nếu data lỗi
        }
        
        // 2. Cập nhật KỲ HẠN VAY TỐI ĐA theo Policy
        if (newPolicy.maxLoanTermYears) {
            // Đảm bảo kỳ hạn vay không vượt quá kỳ hạn tối đa của chính sách
            selectedLoanDuration.value = Math.min(
                selectedLoanDuration.value, 
                newPolicy.maxLoanTermYears
            );
        }
        
        // 3. Cập nhật TỶ LỆ TRẢ TRƯỚC TỐI THIỂU theo Policy
        if (newPolicy.minDownPaymentRate && selectedDownPaymentPercent.value < newPolicy.minDownPaymentRate) {
             selectedDownPaymentPercent.value = newPolicy.minDownPaymentRate;
        }

} else {
        // KHI selectedPolicy là NULL: Đặt lãi suất về giá trị mặc định hợp lệ
        interestRateInput.value = 7.5; 
    }
})


// WATCHER: Tự động tính toán lại TÓM TẮT khi các giá trị thay đổi (Giữ nguyên)
watch([totalCarPrice, selectedDownPaymentPercent, selectedLoanDuration, interestRateInput], () => {
  triggerCalculationSummary();
})

// --- 5. Logic Tính toán (Giữ nguyên) ---

const triggerCalculationSummary = () => {
  if (loanAmount.value <= 0 || !interestRateInput.value || selectedLoanDuration.value <= 0) {
      totalInterest.value = 0;
      totalLoanAndInterest.value = totalCarPrice.value; 
      return;
  }
  
  const p = loanAmount.value;
  const r = (interestRateInput.value / 100) / 12;
  const n = selectedLoanDuration.value * 12;
  
  if (r <= 0) {
    totalInterest.value = 0;
    totalLoanAndInterest.value = p;
    return;
  }

  const monthlyPayment = (p * (r * Math.pow(1 + r, n))) / (Math.pow(1 + r, n) - 1);
  const totalPaid = monthlyPayment * n;
  
  totalInterest.value = Math.round(totalPaid - p);
  totalLoanAndInterest.value = Math.round(totalPaid);
}

// Được gọi khi nhấn nút "LỊCH TRẢ GÓP CHI TIẾT"
const calculateAndShowModal = async () => {
    
    // 1. Kiểm tra điều kiện bắt buộc (giữ nguyên và củng cố)
    if (totalCarPrice.value <= 0 || 
        typeof interestRateInput.value !== 'number' || 
        interestRateInput.value <= 0 || 
        selectedLoanDuration.value <= 0) 
    {
        alert("Vui lòng chọn xe và nhập đầy đủ thông tin vay (Lãi suất và Kỳ hạn phải lớn hơn 0).");
        return;
    }
    
    isLoading.value = true;
    apiError.value = null;
    scheduleData.value = []; 

    const isPolicySelected = selectedPolicy.value !== null;
    
    // --- 2. Chuẩn bị Dữ liệu Gửi lên Backend (Payload có điều kiện) ---
    
    const requestPayload = {
        carPrice: totalCarPrice.value,
        downPaymentRate: selectedDownPaymentPercent.value,
        loanTermYears: selectedLoanDuration.value,
        
        // Gán giá trị có điều kiện để tuân thủ Backend Validation: CHỈ 1 trong 2 được khác null
        policyId: isPolicySelected ? selectedPolicy.value.id : null, 
        customInterestRate: !isPolicySelected ? interestRateInput.value : null 
    };

    // Log payload để kiểm tra (có thể bỏ đi khi đã hoạt động)
    console.log("Payload gửi đi:", requestPayload);

    // --- 3. Gọi API ---
    try {
        // Đảm bảo interestRateInput được dùng để tính toán ở Backend nếu không có policy
        const response = await apiClient.post('/finance/calculate', requestPayload);
        
        // ... (Phần xử lý Response giữ nguyên) ...
        totalInterest.value = response.data.result.totalInterestPaid;
        totalLoanAndInterest.value = response.data.result.totalAmountPaid;
        scheduleData.value = response.data.result.paymentSchedule; 
        isModalOpen.value = true; 
    } catch (error) {
        apiError.value = 'Lỗi tính toán: ' + (error.response?.data?.message || error.message);
        console.error("AxiosError", error);
    } finally {
        isLoading.value = false;
    }
}


// --- 7. Hàm Helpers (Giữ nguyên) ---
function parseFormattedNumber(string) {
  if (!string) return 0
  return parseFloat(string.toString().replace(/[^0-9]/g, '')) || 0
}

function formatCustomPriceInput() {
    let rawValue = parseFormattedNumber(customPriceInput.value);
    customPriceInput.value = new Intl.NumberFormat('vi-VN').format(rawValue);
}

function downloadCSV() {
  if (scheduleData.value.length === 0) return;
  
  let csvContent = "data:text/csv;charset=utf-8,";
  csvContent += "Kỳ hạn,Dư nợ đầu kỳ,Gốc phải trả,Lãi phải trả,Gốc + Lãi,Dư nợ cuối kỳ\n";
  
  scheduleData.value.forEach(row => {
    const openingBalance = row.remainingBalance + row.principalPaid;
    const line = [
      row.month,
      openingBalance,
      row.principalPaid,
      row.interestPaid,
      row.totalPayment,
      row.remainingBalance
    ].join(",");
    csvContent += line + "\n";
  });
  
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement("a");
  link.setAttribute("href", encodedUri);
  link.setAttribute("download", "lich_tra_no_du_kien.csv");
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

function formatCurrency(value) {
    if (value === null || isNaN(value)) return '0 ₫';
    return new Intl.NumberFormat('vi-VN', { 
        style: 'currency', 
        currency: 'VND',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(value);
}
const currencyFormatter = { format: formatCurrency }; 

</script>
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