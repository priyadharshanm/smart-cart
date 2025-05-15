
<template>
  <div class="product-page">
    
    <h1>Product List</h1>
    <div class="ai-suggestions">
  <input
    v-model="prompt"
    placeholder="Describe what you need (e.g., breakfast kit)"
    class="ai-input"
  />
  <button @click="getCartSuggestions">Get Suggestions</button>
</div>
<Teleport to="body">
  <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <h2>Suggested Products</h2>
      <ul>
  <li v-for="product in suggestedProducts" :key="product.id" class="suggested-item">
    <div class="suggested-info">
      <span>{{ product.productName }}</span>
    </div>
    <div class="quantity-controls">
      <button @click="setQuantity(product.id, getSuggestedQuantity(product.id) - 1)">-</button>
      <span>{{ getSuggestedQuantity(product.id) }}</span>
      <button @click="setQuantity(product.id, getSuggestedQuantity(product.id) + 1)">+</button>
    </div>
  </li>
</ul>



<div class="modal-actions">
  <button @click="updateCartWithSuggestions">Update Cart</button>
  <button @click="closeModal">Close</button>
</div>

    </div>
  </div>
</Teleport>

    <div class="product-grid">
     <div v-for="product in products" :key="product.id" class="product-card">
       
        <img
          :src="product.imageUrl || placeholderImage"
          alt="Product"
          class="product-image"
        />
        <h3>{{ product.productName }}</h3>
        <p>{{ product.description || 'No description available.' }}</p>
        <div class="product-price">
          <strong>${{ product.priceCurrent ? product.priceCurrent.toFixed(2) : '0.00' }}</strong>

        </div>
        <div class="product-category">
          <span>{{ product.category }}</span>
        </div>
        <div class="product-rating">
          <span>Brand: {{ product.brand }}</span>
        </div>
        <div class="product-stock">
          <span v-if="product.stock > 0">In Stock</span>
          <span v-else>Out of Stock</span>
        </div>
        <div v-if="getQuantity(product.id) === 0">
          <button @click="addToCart(product.id)">Add to Cart</button>
        </div>
        <div v-else class="quantity-controls">
          <button @click="changeQuantity(product.id, getQuantity(product.id) - 1)">-</button>
          <span>{{ getQuantity(product.id) }}</span>
          <button @click="changeQuantity(product.id, getQuantity(product.id) + 1)">+</button>
        </div>
      </div>
    </div>
    <div class="pagination-controls">
  <button @click="goToPrevPage" :disabled="currentPage === 0">Previous</button>
  <span>Page {{ currentPage + 1 }}</span>
  <button @click="goToNextPage">Next</button>
</div>



  </div>
</template>


<script setup>
import { ref, onMounted, watch } from 'vue'
import placeholderImage from '@/assets/grocery-placeholder.svg'
import api from '../services/api'



const products = ref([])
const cart = ref([])

const currentPage = ref(0)
const pageSize = 20

const fetchProducts = async () => {
  const params = new URLSearchParams({
    page: currentPage.value,
    size: pageSize,
  })

  if (props.searchQuery) {
  params.append('search', props.searchQuery)
}


  const res = await api.get(`/products?${params.toString()}`)
  products.value = res.data
}

const fetchCart = async () => {
  const res = await api.get('/cart')
  cart.value = res.data
}

const getQuantity = (productId) => {
  const item = cart.value.find(ci => ci.product.id === productId)
  return item ? item.quantity : 0
}

const addToCart = async (productId) => {
  await api.post(`/cart/add/${productId}`)
  await fetchCart()
}

const changeQuantity = async (productId, quantity) => {
  if (quantity < 1) {
    await api.delete(`/cart/remove/${productId}`)
  } else {
    await api.put(`/cart/set-quantity/${productId}?quantity=${quantity}`)
  }
  await fetchCart()
}

const prompt = ref('')
const suggestedProducts = ref([])

const showModal = ref(false)
const suggestionQuantities = ref({})

const setQuantity = (productId, quantity) => {
  suggestionQuantities.value[productId] = Math.max(0, quantity)
}



const getCartSuggestions = async () => {
  try {
    const res = await api.post('/ai/cart-suggestions', { prompt: prompt.value })
    suggestedProducts.value = res.data

    // Initialize suggestionQuantities with current cart quantities
    suggestionQuantities.value = {}
    for (const product of suggestedProducts.value) {
      const item = cart.value.find(ci => ci.product.id === product.id)
      suggestionQuantities.value[product.id] = item ? item.quantity : 0
    }

    showModal.value = true
  } catch (err) {
    console.error('Failed to fetch suggestions:', err)
  }
}

const closeModal = () => {
  suggestedProducts.value = []
  showModal.value = false
}


const updateCartWithSuggestions = async () => {
  for (const product of suggestedProducts.value) {
    const qty = getSuggestedQuantity(product.id)

    if (qty < 1) {
      await api.delete(`/cart/remove/${product.id}`)
    } else {
      await api.put(`/cart/set-quantity/${product.id}?quantity=${qty}`)
    }
  }

  await fetchCart()
  closeModal()
}



const getSuggestedQuantity = (productId) => {
  console.log('Updating cart with:', suggestionQuantities.value)

  return suggestionQuantities.value[productId] || 0

}

const addSuggestedToCart = async (productId) => {
  const qty = getSuggestedQuantity(productId)
  if (qty > 0) {
    await api.put(`/cart/set-quantity/${productId}?quantity=${qty}`)
    await fetchCart()
    suggestionQuantities.value[productId] = 0 // reset
  }
}

const goToNextPage = async () => {
  currentPage.value++
  await fetchProducts()
}

const goToPrevPage = async () => {
  if (currentPage.value > 0) {
    currentPage.value--
    await fetchProducts()
  }
}
const props = defineProps({
  searchQuery: String
})

watch(() => props.searchQuery, () => {
  currentPage.value = 0
  fetchProducts()
})

onMounted(() => {
  fetchProducts()
  fetchCart()
})

</script>


<style scoped>
.product-page {
  max-width: 900px;
  margin: 40px auto;
  padding: 1rem;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1rem;
}

.product-card {
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

button {
  margin-top: 10px;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.product-image {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 6px;
  margin-bottom: 10px;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
  gap: 1rem;
}
.ai-suggestions {
  margin: 2rem 0;
  display: flex;
  gap: 10px;
}

.ai-input {
  flex: 1;
  padding: 10px 14px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid #ccc;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1.5rem;
}
.suggested-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

</style>
