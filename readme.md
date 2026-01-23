# 🛒 E-Commerce Backend Application (Spring Boot)

A **secure and scalable e-commerce backend application** built using **Spring Boot**.  
This project implements core e-commerce functionalities such as **User Management, Product Catalog, Cart, Orders**, along with **JWT-based authentication**, **role-based authorization**, **pagination**, and **sorting**.

Designed following **real-world backend best practices** and clean architecture.

---

## 🚀 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Maven**
- **Postman (API Testing)**

---

## ✨ Features Implemented

### 👤 User Module
- User registration
- User login with JWT authentication
- Password encryption using BCrypt
- Role-based access (`USER`, `ADMIN`)
- Get user by ID
- Get all users (Admin only)
- Delete user (Admin only)

---

### 🗂 Category Module
- Create category (Admin only)
- Update category (Admin only)
- Delete category (Admin only)
- Get category by ID
- Get all categories

---

### 📦 Product Module
- Add product (Admin only)
- Get products by category
- Pagination and sorting support

---

### 🛒 Cart Module
- Add product to cart (USER)
- View cart items (USER)
- Update cart item quantity (USER)
- Remove cart item (USER)
- Automatic cart creation
- Automatic cart total calculation

---

### 📑 Order Module
- Place order from cart (USER)
- Create order items automatically
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

## 🔄 Authentication Flow (JWT)

1. User registers → password stored using BCrypt
2. User logs in using `/login`
3. JWT token is generated on successful authentication
4. Client sends token in request headers:
   Authorization: Bearer <JWT_TOKEN>

yaml
Copy code
5. JWT filter validates token for secured APIs
6. Role-based access is enforced

---

## 📄 Common API Response Format

All APIs return a consistent response structure:

```json
{
"success": true,
"message": "Operation successful",
"data": {},
"timestamp": null
}
📊 Pagination & Sorting
Implemented using Spring Data Pageable.

Example:

bash
Copy code
GET /api/v1/users/list?page=0&size=10&sortBy=id&direction=asc
Default page size: 10

Supports sorting on multiple fields

⚙️ Configuration
application.properties
properties
Copy code
spring.application.name=ecommerce_backend
server.port=8087

spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
▶️ How to Run the Project
Clone the repository:

bash
Copy code
git clone https://github.com/aakilzubair/ecommerce-backend.git
Open the project in IntelliJ IDEA / VS Code

Create MySQL database:

sql
Copy code
CREATE DATABASE ecommerce;
Update database credentials in application.properties

Run the application:

bash
Copy code
mvn spring-boot:run
Application will run at:

arduino
Copy code
http://localhost:8087
🧪 API Testing
APIs tested using Postman

JWT token required for secured endpoints

Supports role-based API testing