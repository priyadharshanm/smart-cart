<template>
  <Header
    :is-logged-in="isLoggedIn"
    @search="updateSearch"
    @logout="handleLogout"
  />
  <router-view :search-query="searchQuery" />
</template>

<script setup>
import Header from './components/Header.vue'
import { useRouter } from 'vue-router'
import { ref, watchEffect } from 'vue'
import { isLoggedIn, logout } from './stores/auth'

const router = useRouter()
const searchQuery = ref('')

const updateSearch = (query) => {
  searchQuery.value = query
}

const handleLogout = () => {
  logout()
  router.push('/login')
}

// react to changes in other tabs or manual storage changes
window.addEventListener('storage', () => {
  isLoggedIn.value = !!localStorage.getItem('token')
})
</script>
