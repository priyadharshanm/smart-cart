<template>
  <header class="top-bar">
    <div class="header-left">
      <router-link to="/" class="logo-link">
        <img src="@/assets/smart_cart.png" alt="Smart Cart Logo" class="logo" />
      </router-link>
      <router-link to="/cart" class="nav-link">Cart</router-link>

    </div>

    <div class="search-wrapper" >
      <input
        type="text"
        v-model="search"
        placeholder="Search products..."
        @input="emit('search', search)"
        class="search-input"
        v-if="props.isLoggedIn"
      />
    </div>

    <div class="auth-buttons">
  <template v-if="props.isLoggedIn">
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
  background: #f7f7f7;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  height: 48px;
  width: auto;
}

.search-wrapper {
  flex: 1;
  margin: 0 2rem;
}

.search-input {
  width: 100%;
  padding: 10px 16px;
  font-size: 16px;
  border-radius: 999px;
  border: 1px solid #ddd;
  outline: none;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.login, .signup {
  padding: 10px 16px;
  border-radius: 999px;
  text-decoration: none;
  font-weight: bold;
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
  padding: 10px 16px;
  border-radius: 999px;
  font-weight: bold;
  cursor: pointer;
}


</style>
