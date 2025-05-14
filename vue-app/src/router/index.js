import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ProductList from '../views/ProductList.vue'
import Cart from '../views/Cart.vue'

const routes = [
    { path: '/', redirect: '/products' },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/products', component: ProductList },
    { path: '/cart', component: Cart }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
