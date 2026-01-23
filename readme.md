# 🛒 E-Commerce Backend Application (Spring Boot)

A **secure, scalable, and production-style e-commerce backend application** built using **Spring Boot**.  
This project implements core e-commerce functionalities such as **User Management, Product Catalog, Cart, and Orders**, along with **JWT-based authentication**, **role-based authorization**, **pagination**, and **sorting**.

Designed following **real-world backend best practices**, clean architecture, and containerized using **Docker**.

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Docker & Docker Compose
- Postman (API Testing)

---

## ✨ Features Implemented

### 👤 User Module
- User registration & login
- JWT authentication
- Password encryption using BCrypt
- Role-based access (USER / ADMIN)
- Get user by ID
- Get all users (Admin only)
- Delete user (Admin only)

### 🗂 Category Module
- Create, update, delete category (Admin only)
- Get category by ID
- Get all categories

### 📦 Product Module
- Add product (Admin only)
- Get products by category
- Pagination & sorting

### 🛒 Cart Module
- Add product to cart
- View cart items
- Update cart item quantity
- Remove cart item
- Automatic cart creation & total calculation

### 📑 Order Module
- Place order from cart
- Automatic order item creation
- Clear cart after order placement
- Order status management

---

## 🔐 Security & Authentication

- JWT-based authentication
- Stateless session management
- Custom JWT filter
- Role-based authorization using `@PreAuthorize`
- Secured APIs with Spring Security Filter Chain

---

## 🔄 JWT Authentication Flow

1. User registers → password stored using BCrypt  
2. User logs in using `/login`  
3. JWT token generated  
4. Client sends token in header:

Authorization: Bearer <JWT_TOKEN>

yaml
Copy code

5. JWT filter validates token  
6. Role-based access enforced  

---

## 📄 Common API Response Format

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {},
  "timestamp": null
}
📊 Pagination & Sorting
Example:

bash
Copy code
GET /api/v1/users/list?page=0&size=10&sortBy=id&direction=asc
Default page size: 10

Supports sorting on multiple fields

🐳 Docker Support
Prerequisites
Docker

Docker Compose

▶️ Run Using Docker Compose
bash
Copy code
git clone https://github.com/aakilzubair/ecommerce-backend.git
cd ecommerce-backend
docker compose up
Application runs at:

arduino
Copy code
http://localhost:8087
⚙️ Environment Configuration
env
Copy code
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/ecommerce
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
🧪 API Testing
APIs tested using Postman

JWT required for secured endpoints

Supports USER / ADMIN role testing

🧠 Project Highlights
Clean layered architecture (Controller → Service → Repository)

Global exception handling

Jakarta validation

Pagination & sorting

Dockerized multi-service setup

👨‍💻 Author
Aakil Zubair
Backend Developer | Java | Spring Boot | Docker

