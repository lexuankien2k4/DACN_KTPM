import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import InstallmentView from '../views/InstallmentView.vue'

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/installment', name: 'installment', component: InstallmentView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
