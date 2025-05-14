<template>
  <div class="auth-page">
    <div class="form-container">
      <h2>Login</h2>
      <form @submit.prevent="login">
        <label>Email</label>
        <input v-model="email" type="email" placeholder="Enter your email" required />

        <label>Password</label>
        <input v-model="password" type="password" placeholder="Enter your password" required />

        <button type="submit">Login</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

const email = ref('')
const password = ref('')
const router = useRouter()

const login = async () => {
  try {
    const response = await api.post('/auth/login', {
      username: email.value,
      password: password.value
    })
    localStorage.setItem('token', response.data)
    router.push('/products')
  } catch (error) {
    alert('Login failed. Please check your credentials.')
  }
}
</script>

<style scoped>
.auth-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}

.form-container {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

input, button {
  width: 100%;
  margin-top: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

button {
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 20px;
}
</style>
