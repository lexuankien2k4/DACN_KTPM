<template>
  <div class="min-h-screen bg-gray-50 flex flex-col items-center justify-center">
      <div v-if="loading" class="text-xl text-blue-600 font-bold">Đang xử lý kết quả thanh toán...</div>
      
      <div v-else class="bg-white p-8 rounded-lg shadow-xl text-center max-w-md w-full">
          <div v-if="isSuccess">
              <div class="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i class="fas fa-check text-green-600 text-4xl"></i> <span v-if="!hasIcon">✓</span>
              </div>
              <h2 class="text-2xl font-bold text-green-700 mb-2">Thanh Toán Thành Công!</h2>
              <p class="text-gray-600 mb-6">Mã đơn hàng: <b>{{ txnRef }}</b></p>
          </div>
          
          <div v-else>
              <div class="w-20 h-20 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <span class="text-red-600 text-4xl">✕</span>
              </div>
              <h2 class="text-2xl font-bold text-red-700 mb-2">Thanh Toán Thất Bại</h2>
              <p class="text-gray-600 mb-6">Vui lòng thử lại hoặc liên hệ CSKH.</p>
          </div>

          <router-link to="/" class="block w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded transition-colors">
              Về Trang Chủ
          </router-link>
      </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const loading = ref(true)
const isSuccess = ref(false)
const txnRef = ref('')
const hasIcon = ref(false) // Biến check xem có icon library không

onMounted(async () => {
    // 1. Lấy tham số từ URL VNPAY trả về
    const query = route.query
    txnRef.value = query.vnp_TxnRef || ''

    try {
        // 2. Gọi Backend để cập nhật trạng thái DB (quan trọng)
        const queryString = new URLSearchParams(query).toString()
        const res = await fetch(`http://localhost:8080/api/deposits/vnpay-callback?${queryString}`)
        const data = await res.json()

        if (data.code === 1000 && data.result.code === '00') {
            isSuccess.value = true
        } else {
            isSuccess.value = false
        }
    } catch (e) {
        console.error(e)
        isSuccess.value = false
    } finally {
        loading.value = false
    }
})
</script>