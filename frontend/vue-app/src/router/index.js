import { createRouter, createWebHistory } from 'vue-router'


import HomeView from '../views/HomeView.vue'
import InstallmentView from '../views/InstallmentView.vue'
import ConsultationForm from '@/views/ConsultationForm.vue'


import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import CarManager from '../views/CarManager.vue' 

const routes = [

  { path: '/', name: 'home', component: HomeView },
  { path: '/installment', name: 'installment', component: InstallmentView },
  { path: '/consultation', name: 'ConsultationForm', component: ConsultationForm },


  { 
    path: '/login', 
    name: 'Login', 
    component: Login 
  },
  { 
    path: '/register', 
    name: 'Register', 
    component: Register 
  },

  { 
    path: '/admin/cars', 
    name: 'CarManager', 
    component: CarManager,
    meta: { requiresAuth: true,
      hideHeaderFooter: true } 
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
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