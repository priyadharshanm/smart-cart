<template>
  <header class="top-bar">
    <!-- Left: Menu icon + Logo -->
    <div class="header-left">
      <button class="menu-icon">☰</button>
      <router-link to="/" class="logo-link no-hover-style">
  <div class="logo-container">
    <img src="@/assets/smart_cart.png" alt="Smart Cart Logo" class="logo" />
    <span class="brand-text">Grocify</span>
  </div>
</router-link>

    </div>

    <!-- Search Bar -->
    <div class="search-wrapper" v-if="props.isLoggedIn">
      <div class="search-bar">
        <span class="search-icon">🔍</span>
        <input
          type="text"
          v-model="search"
          placeholder="Search products and stores"
          @input="emit('search', search)"
          class="search-input"
        />
      </div>
    </div>

    <!-- Auth Buttons -->
    <div class="auth-buttons">
      <template v-if="props.isLoggedIn">
        <router-link to="/cart" class="nav-link">Cart</router-link>
        <button class="logout" @click="emit('logout')">Logout</button>
      </template>
      <template v-else>
        <router-link class="login" to="/login">Log in</router-link>
        <router-link class="signup" to="/register">Sign up</router-link>
      </template>
    </div>
  </header>
</template>


<script setup>
import { ref } from 'vue'

const search = ref('')
const emit = defineEmits(['search', 'logout'])
const props = defineProps({
  isLoggedIn: Boolean
})

</script>


<style scoped>
.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 100;
  background: var(--primary);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding:0px 24px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.menu-icon {
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
}

.logo {
  height: 48px;
  width: auto;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

.search-wrapper {
  flex: 1;
  margin: 0 2rem;
}

.search-bar {
  display: flex;
  align-items: center;
  background-color: #f4f5f6;
  border: 1px solid #ccc;
  border-radius: 999px;
  padding: 8px 16px;
}

.search-icon {
  color: #777;
  margin-right: 8px;
}

.search-input {
  width: 100%;
  font-size: 16px;
  border: none;
  outline: none;
  background: transparent;
  color: #333;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.login,
.signup {
  padding: 8px 20px;
  border-radius: 999px;
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
}

.login {
  border: 1px solid #ccc;
  background: white;
  color: #333;
}

.signup {
  background: #029e3b;
  color: white;
  border: none;
}

.logout {
  background: transparent;
  border: 1px solid #333;
  color: black;
  padding: 8px 20px;
  border-radius: 999px;
  font-weight: bold;
  cursor: pointer;
}

.no-hover-style:hover {
  background-color: transparent !important;
  color: inherit !important;
  padding: 0 !important;
  border-radius: 0 !important;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo {
  height: 100px;
  width: auto;
  display: block;
}

.brand-text {
  font-weight: bold;
  font-size: 30px;
  color: #034d21;
  line-height: 1; /* Ensures clean vertical alignment */
}

</style>
