<script setup>
// (Không cần script gì đặc biệt cho footer)
</script>

<template>
  <footer id="contact" class="bg-[#F0F4F8] text-gray-700 pt-16 pb-8">
    <div class="container mx-auto px-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8 mb-10">
        
        <div>
          <h3 class="text-xl font-bold text-gray-900 mb-4 tracking-wider">
            Vin<span class="text-blue-600">Auto</span>
          </h3>
          <p class="text-gray-700">Cùng bạn kiến tạo một tương lai xanh hơn, thông minh hơn và an toàn hơn.</p>
          <div class="flex space-x-4 mt-6">
            <a href="#" class="text-gray-500 hover:text-gray-900 transition-colors"><i class="fab fa-facebook-f fa-lg"></i></a>
            <a href="#" class="text-gray-500 hover:text-gray-900 transition-colors"><i class="fab fa-youtube fa-lg"></i></a>
            <a href="#" class="text-gray-500 hover:text-gray-900 transition-colors"><i class="fab fa-instagram fa-lg"></i></a>
          </div>
        </div>
        
        <div>
          <h3 class="text-lg font-semibold text-gray-900 mb-4">Khám Phá</h3>
          <ul class="space-y-3">
            <li><a href="#" class="text-gray-600 hover:text-gray-900 transition-colors">Về chúng tôi</a></li>
            <li><router-link to="/#cars-showcase" class="text-gray-600 hover:text-gray-900 transition-colors">Sản phẩm ô tô</router-link></li>
            <li><router-link to="/#bikes-showcase" class="text-gray-600 hover:text-gray-900 transition-colors">Sản phẩm xe máy</router-link></li>
          </ul>
        </div>
        
        <div>
          <h3 class="text-lg font-semibold text-gray-900 mb-4">Hỗ Trợ</h3>
          <ul class="space-y-3">
            <li><a href="#" class="text-gray-600 hover:text-gray-900 transition-colors">Tìm Showroom</a></li>
            <li><a href="#" class="text-gray-600 hover:text-gray-900 transition-colors">Chính sách bảo hành</a></li>
            <li><a href="#" class="text-gray-600 hover:text-gray-900 transition-colors">Liên hệ</a></li>
          </ul>
        </div>

        <div>
          <h3 class="text-lg font-semibold text-gray-900 mb-4">Đăng Ký Nhận Tin</h3>
          <p class="text-gray-700 mb-4">Nhận thông tin mới nhất và các chương trình ưu đãi.</p>
          <form class="flex" @submit.prevent>
            <input type="email" placeholder="Email của bạn" class="w-full bg-white text-gray-900 px-4 py-2 rounded-l-md focus:outline-none focus:ring-2 focus:ring-blue-500 border border-gray-300">
            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white font-bold px-4 py-2 rounded-r-md"><i class="fas fa-paper-plane"></i></button>
          </form>
        </div>
      </div>
      
      <div class="border-t border-gray-200 pt-6 text-center text-gray-600">
        <p>&copy; {{ new Date().getFullYear() }} VinAuto Corporation. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
/* Không cần style riêng ở đây
  vì chúng ta đã dùng các class của Tailwind
  và các style toàn cục trong styles.css
*/
</style>