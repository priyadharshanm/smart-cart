# 🛒 Smart Cart – AI-Powered Product Catalog

This project is a full-stack e-commerce demo featuring product browsing, cart management, and AI-assisted cart generation using OpenAI.

## 🔍 Features

- Product search with fuzzy matching
- Shopping cart with quantity controls
- User login/register with localStorage token
- AI-generated cart suggestions based on prompts (e.g., “Build a breakfast kit”)

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot**
- **MongoDB**
- **OpenAI GPT-3.5** (via REST integration)
- **Maven**

### Frontend
- **Vue 3 + Composition API**
- **Vite**
- **Axios**
- **LocalStorage-based auth**
- **Responsive UI with modern design**

## AI Integration

Natural language prompts are sent to the backend, where keywords are extracted using GPT. Products are matched via fuzzy search and suggested to the user.
