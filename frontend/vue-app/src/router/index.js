import { createRouter, createWebHistory } from 'vue-router'

// Views Public
import HomeView from '../views/HomeView.vue'
import InstallmentView from '../views/InstallmentView.vue'
import ConsultationForm from '@/views/ConsultationForm.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import DepositView from '../views/DepositView.vue'
import PaymentResult from '../views/PaymentResult.vue'
// Views Admin (Đã có)
import CarManager from '../views/CarManager.vue'
import Dashboard from '../views/Dashboard.vue'
import EmployeeManager from '../views/EmployeeManager.vue'

// Views Admin (MỚI - Cần tạo file tương ứng ở bước 3)
const CustomerManager = () => import('../views/CustomerManager.vue')
const ConsultationManager = () => import('../views/ConsultationManager.vue')
const OrderManager = () => import('../views/OrderManager.vue')
const ContractManager = () => import('../views/ContractManager.vue')
const NhatkyManager = () => import('../views/NhatKyManager.vue')

const routes = [
  // Public Routes
  { path: '/', name: 'home', component: HomeView },
  { path: '/installment', name: 'installment', component: InstallmentView },
  { path: '/consultation', name: 'ConsultationForm', component: ConsultationForm },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  {
    path: '/payment-callback',
    name: 'PaymentResult',
    component: PaymentResult,
    meta: { hideHeaderFooter: true } // Ẩn header/footer nếu muốn trang kết quả gọn
  },
  { path: '/deposit', name: 'Deposit', component: DepositView, meta: { hideHeaderFooter: true } },

  // Admin Routes
  {
    path: '/admin',
    redirect: '/admin/dashboard', // Mặc định vào dashboard
    meta: { requiresAuth: true, hideHeaderFooter: true },
    children: [
      { path: 'dashboard', name: 'Dashboard', component: Dashboard },
      { path: 'cars', name: 'CarManager', component: CarManager },
      { path: 'employee', name: 'EmployeeManager', component: EmployeeManager },

      // 🆕 CÁC ROUTE MỚI THEO USE CASE
      { path: 'customers', name: 'CustomerManager', component: CustomerManager }, // Cập nhật trạng thái KH
      { path: 'consultations', name: 'ConsultationManager', component: ConsultationManager }, // Xử lý yêu cầu tư vấn
      { path: 'orders', name: 'OrderManager', component: OrderManager }, // Tiếp nhận đặt cọc/Yêu cầu
      { path: 'contracts', name: 'ContractManager', component: ContractManager },
      { path: 'nhatky', name: 'NhatkyManager', component: NhatkyManager }, // Tạo hợp đồng
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth',
      }
    }
    if (savedPosition) {
      return savedPosition
    }
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('authToken');
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  } else {
    next();
  }
});

export default router