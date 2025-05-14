<template>
    <div class="cart-page">
      <h2>Your Cart</h2>
  
      <div v-if="items.length">
        <div class="cart-item" v-for="item in items" :key="item.id">
          <img
            :src="item.product.imageUrl || placeholderImage"
            alt="Product thumbnail"
            class="cart-thumbnail"
          />
  
          <div class="product-info">
            <strong>{{ item.product.productName }}</strong>
            <!-- <p>{{ item.product.description }}</p> -->
          </div>
  
          <div class="actions">
            <div class="quantity-controls">
              <button @click="changeQuantity(item.product.id, item.quantity - 1)" :disabled="item.quantity <= 1">-</button>
              <span>{{ item.quantity }}</span>
              <button @click="changeQuantity(item.product.id, item.quantity + 1)">+</button>
            </div>
            <button class="remove" @click="removeFromCart(item.product.id)">Remove</button>
          </div>
        </div>
      </div>
  
      <p v-else>Your cart is empty.</p>
    </div>
  </template>
  
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import api from '../services/api'
  import placeholderImage from '@/assets/grocery-placeholder.svg'

  const items = ref([])
  
  const fetchCart = async () => {
    const res = await api.get('/cart')
    items.value = res.data
  }
  
  const removeFromCart = async (productId) => {
    await api.delete(`/cart/remove/${productId}`)
    fetchCart()
  }
  
  const changeQuantity = async (productId, newQuantity) => {
    if (newQuantity < 1) return
    await api.put(`/cart/set-quantity/${productId}?quantity=${newQuantity}`)
    fetchCart()
  }
  
  onMounted(fetchCart)
  </script>
  
  <style scoped>
  .cart-page {
    max-width: 800px;
    margin: 2rem auto;
  }
  
  .cart-item {
    display: flex;
    justify-content: space-between;
    padding: 1rem;
    border-bottom: 1px solid #ddd;
    align-items: center;
  }
  
  .actions {
    text-align: right;
  }
  
  .quantity-controls {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .quantity-controls button {
    padding: 4px 10px;
    font-size: 16px;
    border: 1px solid #ccc;
    background: #f3f3f3;
    cursor: pointer;
  }
  
  .remove {
    margin-top: 8px;
    padding: 5px 10px;
    background-color: #ff5e57;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .cart-item {
  display: flex;
  justify-content: space-between;
  padding: 1rem;
  border-bottom: 1px solid #ddd;
  align-items: center;
  gap: 1rem;
}

.cart-thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.product-info {
  flex-grow: 1;
}

  </style>
  