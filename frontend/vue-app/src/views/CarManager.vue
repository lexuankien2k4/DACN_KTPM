<template>
  <div class="flex h-screen bg-gray-100 font-sans">
    
    <aside :class="['w-64 bg-white shadow-xl flex-shrink-0 transition-all duration-300', isSidebarOpen ? 'translate-x-0' : '-translate-x-64', 'md:translate-x-0 fixed md:relative z-20 h-full']">
      <div class="h-full flex flex-col">
        <div class="flex items-center justify-center h-20 border-b">
          <img src="https://cdn-001.haui.edu.vn//img/logo-haui-size.png" alt="Logo" class="h-10 mr-2">
          <h2 class="text-xl font-bold text-gray-800">NHÓM 7</h2>
          <button @click="isSidebarOpen = false" class="md:hidden ml-auto mr-4 text-gray-600">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <nav class="flex-1 overflow-y-auto py-4">
          <router-link to="/admin/dashboard" class="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 transition-colors">
            <i class="fas fa-th-large w-6"></i>
            <span class="font-medium">Dashboard</span>
          </router-link>
          <router-link to="/admin/cars" class="flex items-center px-6 py-3 bg-blue-50 text-blue-600 border-r-4 border-blue-600">
            <i class="fas fa-car w-6"></i>
            <span class="font-medium">Quản lý Sản phẩm</span>
          </router-link>
        </nav>
        <div class="p-4 border-t">
          <button @click="handleLogout" class="flex items-center w-full px-6 py-3 text-red-600 hover:bg-red-50 rounded-lg transition-colors">
            <i class="fas fa-sign-out-alt w-6"></i>
            <span class="font-medium">Đăng xuất</span>
          </button>
        </div>
      </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden relative">
      <header class="h-20 bg-white shadow-sm flex items-center justify-between px-6 z-10">
        <div class="flex items-center">
          <button @click="isSidebarOpen = true" class="md:hidden text-gray-600 mr-4">
            <i class="fas fa-bars text-xl"></i>
          </button>
          <h1 class="text-2xl font-bold text-gray-800">Quản lý Sản phẩm</h1>
        </div>
        <div class="flex items-center gap-4">
          <div class="flex items-center gap-3">
            <div class="text-right hidden sm:block">
              <p class="text-sm font-bold text-gray-700">Admin</p>
              <small class="text-gray-500">Quản trị viên</small>
            </div>
            <img src="https://ui-avatars.com/api/?name=Admin&background=0D8ABC&color=fff" alt="Admin" class="h-10 w-10 rounded-full border">
          </div>
        </div>
      </header>

      <div class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-50 p-6">
        
        <div class="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4">
          <button @click="openCreateModal()" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg shadow-md transition-all flex items-center gap-2 font-medium">
            <i class="fas fa-plus"></i> Thêm Xe Mới
          </button>
          
          <div class="relative w-full sm:w-72">
            <input type="text" v-model="searchTerm" placeholder="Tìm kiếm sản phẩm..." class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none">
            <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-md overflow-hidden flex flex-col justify-between min-h-[500px]">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead class="bg-gray-100 text-gray-600 uppercase text-xs font-bold">
                <tr>
                  <th class="p-4 border-b text-center w-16">ID</th>
                  <th class="p-4 border-b w-32 text-center">Hình ảnh</th>
                  <th class="p-4 border-b">Tên Phiên Bản</th>
                  <th class="p-4 border-b">Dòng Xe</th>
                  <th class="p-4 border-b text-right">Giá Niêm Yết</th>
                  <th class="p-4 border-b text-center">Trạng thái</th>
                  <th class="p-4 border-b text-center w-40">Hành động</th>
                </tr>
              </thead>
              <tbody class="text-sm text-gray-700">
                <tr v-if="isLoading"><td colspan="7" class="p-8 text-center text-gray-500"><i class="fas fa-spinner fa-spin mr-2"></i> Đang tải dữ liệu...</td></tr>
                <tr v-else-if="filteredVariants.length === 0"><td colspan="7" class="p-8 text-center text-gray-500">Không tìm thấy dữ liệu.</td></tr>
                
                <tr v-for="variant in paginatedVariants" :key="variant.id" class="border-b hover:bg-blue-50 transition-colors group">
                  <td class="p-4 font-bold text-gray-500 text-center">#{{ variant.id }}</td>
                  <td class="p-4 flex justify-center">
                    <div class="relative h-16 w-24 rounded-lg overflow-hidden border bg-gray-100 shadow-sm group-hover:shadow-md transition-all">
                      <img :src="getImageUrl(variant.firstImageUrl)" class="h-full w-full object-contain bg-white" @error="handleImageError">
                    </div>
                  </td>
                  <td class="p-4 font-semibold text-gray-800">{{ variant.name }}</td>
                  <td class="p-4"><span class="bg-blue-100 text-blue-800 py-1 px-3 rounded-full text-xs font-bold">{{ variant.modelName || 'N/A' }}</span></td>
                  <td class="p-4 text-right font-mono font-medium text-blue-600">{{ formatPrice(variant.price) }}</td>
                  <td class="p-4 text-center">
                    <span :class="variant.isActive ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'" class="py-1 px-3 rounded-full text-xs font-bold">
                      {{ variant.isActive ? 'Active' : 'Hidden' }}
                    </span>
                  </td>
                  <td class="p-4 text-center">
                    <div class="flex items-center justify-center gap-2">
                        <button @click="openDetailModal(variant.id)" class="text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100 p-2 rounded-lg transition-all" title="Xem chi tiết">
                            <i class="fas fa-eye text-lg"></i>
                        </button>
                        <button @click="openEditModal(variant.id)" class="text-blue-600 hover:text-blue-800 hover:bg-blue-100 p-2 rounded-lg transition-all" title="Sửa">
                            <i class="fas fa-edit text-lg"></i>
                        </button>
                        <button @click="deleteVariant(variant.id)" class="text-red-600 hover:text-red-800 hover:bg-red-100 p-2 rounded-lg transition-all" title="Xóa">
                            <i class="fas fa-trash-alt text-lg"></i>
                        </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div class="p-4 border-t bg-gray-50 flex justify-between items-center text-xs text-gray-500">
            <span>Hiển thị {{ paginatedVariants.length }} / {{ filteredVariants.length }} kết quả</span>
            <div class="flex items-center gap-2">
              <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1" class="px-3 py-1 border rounded hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed bg-white">Trước</button>
              <span class="font-bold text-gray-700">Trang {{ currentPage }} / {{ totalPages || 1 }}</span>
              <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages" class="px-3 py-1 border rounded hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed bg-white">Sau</button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm" @click="closeModal"></div>
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-6xl max-h-[95vh] overflow-hidden flex flex-col relative z-10 animate-fade-in-up">
        
        <div class="px-6 py-4 border-b flex justify-between items-center bg-gray-50">
          <h2 class="text-xl font-bold text-gray-800">{{ isEditing ? 'Cập Nhật Sản Phẩm' : 'Thêm Sản Phẩm Mới' }}</h2>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600 text-2xl">&times;</button>
        </div>

        <div class="p-6 overflow-y-auto flex-1 bg-gray-50">
          <form @submit.prevent="submitForm" class="space-y-6">
            
            <div class="bg-white p-5 rounded-lg shadow-sm border border-gray-200">
                <h3 class="text-blue-800 font-bold mb-4 border-b pb-2 flex items-center gap-2">
                    <i class="fas fa-info-circle"></i> 1. Thông tin cơ bản & Giá
                </h3>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                    <div>
                        <label class="block text-sm font-semibold mb-1">Tên Phiên Bản <span class="text-red-500">*</span></label>
                        <input v-model="form.name" type="text" required class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
                    </div>
                    <div>
                        <label class="block text-sm font-semibold mb-1">Thuộc Dòng Xe <span class="text-red-500">*</span></label>
                        <select v-model="form.modelId" required class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none bg-white">
                            <option :value="null" disabled>-- Chọn dòng xe --</option>
                            <option v-for="model in carModels" :key="model.id" :value="model.id">{{ model.name }}</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold mb-1">Trạng Thái</label>
                        <select v-model="form.isActive" class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none bg-white">
                            <option :value="true">Đang bán</option>
                            <option :value="false">Ngừng bán</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold mb-1">Giá Niêm Yết <span class="text-red-500">*</span></label>
                        <div class="relative">
                            <input v-model.number="form.price" type="number" required min="0" class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
                            <span class="absolute right-3 top-2 text-gray-400 text-xs font-bold">VNĐ</span>
                        </div>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold mb-1">Giá Khuyến Mãi</label>
                        <div class="relative">
                            <input v-model.number="form.discountedPrice" type="number" min="0" class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
                            <span class="absolute right-3 top-2 text-gray-400 text-xs font-bold">VNĐ</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="bg-white p-5 rounded-lg shadow-sm border border-gray-200">
                <h3 class="text-blue-800 font-bold mb-4 border-b pb-2 flex items-center gap-2">
                    <i class="fas fa-cogs"></i> 2. Thông số kỹ thuật
                </h3>
                
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-x-6 gap-y-4 text-sm">
                    <div class="col-span-full font-bold text-gray-700 bg-gray-100 p-1.5 rounded mt-2">Kích thước & Khung gầm</div>
                    <div><label>Dài (mm)</label><input v-model.number="form.length" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Rộng (mm)</label><input v-model.number="form.width" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Cao (mm)</label><input v-model.number="form.height" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Chiều dài cơ sở (mm)</label><input v-model.number="form.wheelbase" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Khoảng sáng gầm (mm)</label><input v-model.number="form.groundClearance" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Mâm (inch)</label><input v-model.number="form.wheelSize" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    
                    <div class="col-span-full font-bold text-gray-700 bg-gray-100 p-1.5 rounded mt-2">Vận hành</div>
                    <div><label>Công suất (hp)</label><input v-model.number="form.maxPower" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Mô-men xoắn (Nm)</label><input v-model.number="form.maxTorque" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Pin (kWh)</label><input v-model.number="form.battery" type="number" step="0.1" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Quãng đường (km)</label><input v-model.number="form.rangeNedc" type="number" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Hệ dẫn động</label><input v-model="form.drivetrain" type="text" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Chế độ lái</label><input v-model="form.driveModes" type="text" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Thời gian sạc</label><input v-model="form.chargeTimeDetail" type="text" class="w-full px-2 py-1.5 border rounded"></div>
                    
                    <div class="col-span-full font-bold text-gray-700 bg-gray-100 p-1.5 rounded mt-2">Tiện nghi</div>
                    <div><label>Màn hình (inch)</label><input v-model.number="form.entertainmentScreen" type="number" step="0.1" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Loa</label><input v-model="form.speakerSystem" type="text" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Điều hòa</label><input v-model="form.airConditioningSystem" type="text" class="w-full px-2 py-1.5 border rounded"></div>
                    <div><label>Ghế lái</label><input v-model="form.driverSeatAdjustment" type="text" class="w-full px-2 py-1.5 border rounded"></div>
                </div>
            </div>

            <div class="bg-white p-5 rounded-lg shadow-sm border border-gray-200">
              <h3 class="text-blue-800 font-bold mb-4 border-b pb-2 flex items-center gap-2">
                  <i class="fas fa-images"></i> 3. Hình ảnh sản phẩm
              </h3>
              
              <div v-if="isEditing && currentImages.length > 0" class="mb-4">
                <p class="text-xs font-bold text-gray-600 mb-2 uppercase">Ảnh hiện tại:</p>
                <div class="flex flex-wrap gap-3">
                  <div v-for="img in currentImages" :key="img.id" class="relative group">
                    <img :src="getImageUrl(img.imageUrl)" class="h-24 w-32 object-cover rounded-lg border shadow-sm bg-gray-50" @error="handleImageError">
                    <button type="button" @click="deleteImage(img.id)" class="absolute -top-2 -right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center shadow-md opacity-0 group-hover:opacity-100 transition-all hover:bg-red-600 z-10">
                      <i class="fas fa-times text-xs"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div 
                class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-blue-500 hover:bg-blue-50 transition-all cursor-pointer relative"
                @click="$refs.fileInput.click()"
              >
                <input ref="fileInput" type="file" multiple accept="image/*" class="hidden" @change="handleFileSelect">
                
                <div v-if="selectedFiles.length === 0">
                  <i class="fas fa-cloud-upload-alt text-4xl text-blue-400 mb-3"></i>
                  <p class="font-medium text-gray-600">Nhấn để chọn ảnh từ máy tính</p>
                  <p class="text-xs text-gray-400">Hỗ trợ JPG, PNG, WEBP (Có thể chọn nhiều)</p>
                </div>

                <div v-else class="grid grid-cols-4 sm:grid-cols-6 gap-4">
                  <div v-for="(preview, index) in imagePreviews" :key="index" class="relative group">
                    <img :src="preview" class="h-24 w-full object-cover rounded-lg shadow-sm bg-white border">
                    <div class="absolute inset-0 bg-black/40 rounded-lg flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                        <span class="text-white text-xs font-bold">Mới</span>
                    </div>
                  </div>
                  <div class="flex items-center justify-center h-24 border rounded-lg text-blue-600 font-bold bg-white">
                    +{{ selectedFiles.length }} ảnh
                  </div>
                </div>
              </div>
            </div>

          </form>
        </div>

        <div class="px-6 py-4 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="closeModal" class="px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-100 font-medium transition-colors">Hủy bỏ</button>
          <button @click="submitForm" :disabled="isLoadingSubmit" class="px-6 py-2.5 rounded-lg bg-blue-600 text-white hover:bg-blue-700 font-bold shadow-md hover:shadow-lg transition-all flex items-center disabled:opacity-70 disabled:cursor-not-allowed">
            <i v-if="isLoadingSubmit" class="fas fa-spinner fa-spin mr-2"></i>
            {{ isLoadingSubmit ? 'Đang lưu...' : 'Lưu & Upload' }}
          </button>
        </div>

      </div>
    </div>

    <div v-if="showDetailModal" class="fixed inset-0 z-[60] flex items-center justify-center p-4">
       <div class="absolute inset-0 bg-black/70 backdrop-blur-sm" @click="showDetailModal = false"></div>
       <div class="bg-white rounded-xl shadow-2xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col relative z-10 animate-fade-in-up">
          
          <div v-if="isLoadingDetail" class="h-64 flex items-center justify-center">
             <i class="fas fa-spinner fa-spin text-3xl text-blue-600"></i>
          </div>

          <template v-else>
              <div class="relative h-64 bg-gray-100 flex justify-center items-center overflow-hidden">
                 <img :src="getDetailHeroImage()" class="h-full w-auto object-contain" @error="handleImageError">
                 <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent flex items-end p-6">
                    <div>
                       <span class="bg-blue-600 text-white text-xs font-bold px-2 py-1 rounded mb-2 inline-block">{{ selectedVariant?.modelName }}</span>
                       <h2 class="text-3xl font-bold text-white">{{ selectedVariant?.name }}</h2>
                       <p class="text-gray-200 font-mono text-2xl font-bold">{{ formatPrice(selectedVariant?.price) }}</p>
                    </div>
                 </div>
                 <button @click="showDetailModal = false" class="absolute top-4 right-4 bg-black/30 hover:bg-black/60 text-white rounded-full w-10 h-10 flex items-center justify-center transition-all">&times;</button>
              </div>

              <div class="p-8 overflow-y-auto">
                 <h3 class="font-bold text-xl text-gray-800 border-b pb-3 mb-6 flex items-center gap-2">
                   <i class="fas fa-info-circle text-blue-600"></i> Thông số kỹ thuật chi tiết
                 </h3>
                 
                 <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-y-4 gap-x-8 text-sm text-gray-700">
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Dài x Rộng x Cao</span> <b>{{ selectedVariant?.length }} x {{ selectedVariant?.width }} x {{ selectedVariant?.height }} mm</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Chiều dài cơ sở</span> <b>{{ selectedVariant?.wheelbase }} mm</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Khoảng sáng gầm</span> <b>{{ selectedVariant?.groundClearance }} mm</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Công suất tối đa</span> <b>{{ selectedVariant?.maxPower }} hp</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Mô-men xoắn</span> <b>{{ selectedVariant?.maxTorque }} Nm</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Dung lượng Pin</span> <b>{{ selectedVariant?.battery }} kWh</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Quãng đường (NEDC)</span> <b class="text-green-600">{{ selectedVariant?.rangeNedc }} km</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Hệ dẫn động</span> <b>{{ selectedVariant?.drivetrain }}</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Thời gian sạc</span> <b>{{ selectedVariant?.chargeTimeDetail }}</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Màn hình giải trí</span> <b>{{ selectedVariant?.entertainmentScreen }} inch</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Hệ thống loa</span> <b>{{ selectedVariant?.speakerSystem }}</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Điều hòa</span> <b>{{ selectedVariant?.airConditioningSystem }}</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Ghế lái</span> <b>{{ selectedVariant?.driverSeatAdjustment }}</b></div>
                    <div class="border-b pb-1"><span class="text-gray-500 block text-xs">Cốp xe</span> <b>{{ selectedVariant?.trunkAccess }}</b></div>
                 </div>

                 <div v-if="selectedVariant?.images?.length > 0" class="mt-8">
                    <h3 class="font-bold text-gray-800 border-b pb-2 mb-4">Thư viện ảnh ({{ selectedVariant.images.length }})</h3>
                    <div class="flex flex-wrap gap-3">
                       <img v-for="img in selectedVariant.images" :key="img.id" :src="getImageUrl(img.imageUrl)" class="h-24 w-auto object-contain border rounded-lg shadow-sm">
                    </div>
                 </div>
              </div>
          </template>
          
          <div class="p-4 bg-gray-50 border-t flex justify-end">
             <button @click="showDetailModal = false" class="px-6 py-2 bg-gray-800 text-white rounded font-bold hover:bg-gray-900">Đóng</button>
          </div>
       </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import api from '@/utils/axios'
import { useRouter } from 'vue-router'
import VinfastLogo from "@/assets/image/icon_logo/VinFast-logo.svg";

const router = useRouter()

// --- State ---
const isSidebarOpen = ref(false)
const variants = ref([])
const carModels = ref([])
const showModal = ref(false)
const showDetailModal = ref(false)
const isEditing = ref(false)
const isLoading = ref(false)
const isLoadingDetail = ref(false)
const isLoadingSubmit = ref(false)
const searchTerm = ref('')
const selectedVariant = ref(null)

// PHÂN TRANG
const currentPage = ref(1)
const itemsPerPage = ref(7)

// UPLOAD FILE
const selectedFiles = ref([])
const imagePreviews = ref([])
const currentImages = ref([])

const form = reactive({
  id: null, modelId: null, name: '', price: 0, discountedPrice: 0, isActive: true,
  length: null, width: null, height: null, wheelbase: null, groundClearance: null, wheelSize: null,
  maxPower: null, maxTorque: null, battery: null, rangeNedc: null,
  drivetrain: '', driveModes: '', entertainmentScreen: null, speakerSystem: '',
  airConditioningSystem: '', driverSeatAdjustment: '', suspensionSystem: '', brakeSystem: '',
  frontLightType: '', trunkAccess: '', chargeTimeDetail: ''
})

// --- Computed ---
const filteredVariants = computed(() => {
  if (!searchTerm.value) return variants.value
  const term = searchTerm.value.toLowerCase()
  return variants.value.filter(v => 
    v.name.toLowerCase().includes(term) || (v.modelName && v.modelName.toLowerCase().includes(term))
  )
})

const totalPages = computed(() => Math.ceil(filteredVariants.value.length / itemsPerPage.value) || 1)

const paginatedVariants = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage.value
    const end = start + itemsPerPage.value
    return filteredVariants.value.slice(start, end)
})

// Reset trang khi tìm kiếm
watch(searchTerm, () => currentPage.value = 1)

// --- Helpers ---
const changePage = (p) => { if (p >= 1 && p <= totalPages.value) currentPage.value = p }

const formatPrice = (v) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v || 0)

const getImageUrl = (path) => {
    if (!path) return VinfastLogo;
    if (path.startsWith('http') || path.startsWith('blob:')) return path;
    return `http://localhost:8080${path.startsWith('/') ? '' : '/'}${path}`;
}

const getDetailHeroImage = () => {
    if (selectedVariant.value?.images?.length) return getImageUrl(selectedVariant.value.images[0].imageUrl);
    return VinfastLogo;
}

const handleImageError = (e) => e.target.src = VinfastLogo;

// --- API ---
const fetchData = async () => {
  isLoading.value = true
  try {
    const [resVariants, resModels] = await Promise.all([
      api.get('/api/variants'),
      api.get('/api/models') 
    ])
    variants.value = resVariants.data.result || []
    carModels.value = resModels.data.result || []
  } catch (e) { console.error(e) } 
  finally { isLoading.value = false }
}

const fetchVariantDetail = async (id) => {
    try {
        const res = await api.get(`/api/variants/${id}/details`);
        return res.data.result;
    } catch (e) { return null; }
}

// --- Actions ---
const openCreateModal = () => {
    isEditing.value = false
    Object.keys(form).forEach(k => form[k] = (k==='isActive'?true : (k==='price'?0:null)));
    selectedFiles.value = []; imagePreviews.value = []; currentImages.value = [];
    showModal.value = true
}

const openEditModal = async (id) => {
    isEditing.value = true
    showModal.value = true
    const detail = await fetchVariantDetail(id);
    if (detail) {
        Object.assign(form, detail);
        form.modelId = detail.carModel?.id || null;
        if(!form.modelId && detail.modelName) {
             const m = carModels.value.find(x => x.name === detail.modelName)
             if(m) form.modelId = m.id
        }
        currentImages.value = detail.images || [];
        selectedFiles.value = []; imagePreviews.value = [];
    } else closeModal();
}

const openDetailModal = async (id) => {
    showDetailModal.value = true
    isLoadingDetail.value = true
    const detail = await fetchVariantDetail(id);
    if (detail) selectedVariant.value = detail;
    isLoadingDetail.value = false;
}

const closeModal = () => {
  showModal.value = false
  imagePreviews.value.forEach(url => URL.revokeObjectURL(url))
  imagePreviews.value = []
  selectedFiles.value = []
}

const handleFileSelect = (e) => {
  const files = Array.from(e.target.files)
  if(!files.length) return
  selectedFiles.value = [...selectedFiles.value, ...files]
  imagePreviews.value = [...imagePreviews.value, ...files.map(file => URL.createObjectURL(file))]
}

const submitForm = async () => {
  if (!form.name || !form.price || !form.modelId) return alert("Thiếu thông tin bắt buộc!")
  
  isLoadingSubmit.value = true
  try {
    let variantId = form.id
    const payload = { ...form }
    delete payload.images; // Không gửi images cũ
    
    // Create/Update Variant
    if (isEditing.value) {
      await api.put(`/api/variants/${variantId}`, payload)
    } else {
      const res = await api.post('/api/variants', payload)
      variantId = res.data.result.id 
    }

    // Upload Files Mới
    if (selectedFiles.value.length > 0 && variantId) {
      const uploadPromises = selectedFiles.value.map(file => {
        const formData = new FormData()
        formData.append('file', file)
        return api.post(`/api/images/upload/${variantId}`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
      })
      await Promise.all(uploadPromises)
    }

    await fetchData()
    closeModal()
    alert(isEditing.value ? "Cập nhật thành công!" : "Thêm mới thành công!")
  } catch (error) {
    alert("Lỗi: " + (error.response?.data?.message || error.message))
  } finally {
    isLoadingSubmit.value = false
  }
}

const deleteImage = async (id) => {
  if (!confirm("Xóa ảnh này?")) return
  try {
    await api.delete(`/api/images/${id}`)
    currentImages.value = currentImages.value.filter(i => i.id !== id)
  } catch (e) { alert("Lỗi xóa ảnh") }
}

const deleteVariant = async (id) => {
  if (!confirm("Xóa xe?")) return
  try {
    await api.delete(`/api/variants/${id}`)
    fetchData()
  } catch (e) { alert("Lỗi xóa xe") }
}

const handleLogout = () => {
  localStorage.removeItem('authToken')
  router.push('/login')
}

onMounted(fetchData)
</script>

<style scoped>
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fadeInUp 0.3s ease-out forwards;
}
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; }
::-webkit-scrollbar-thumb { background: #888; border-radius: 4px; }
::-webkit-scrollbar-thumb:hover { background: #555; }
</style>