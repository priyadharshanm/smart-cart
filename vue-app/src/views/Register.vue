<template>
  <div class="auth-page">
    <div class="form-container">
      <h2>Register</h2>
      <form @submit.prevent="register">
        <label>Email</label>
        <input type="email" v-model="email" placeholder="Enter your email" required />

        <label>Password</label>
        <input type="password" v-model="password" placeholder="Choose a password" required />

        <button type="submit">Register</button>
      </form>
      <p v-if="error" style="color: red;">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

const email = ref('')
const password = ref('')
const error = ref('')
const router = useRouter()

const register = async () => {
  try {
    const response = await api.post('/auth/register', {
      username: email.value,
      password: password.value
    })
    localStorage.setItem('token', response.data)
    router.push('/products')
  } catch (err) {
    error.value = 'Registration failed.'
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
