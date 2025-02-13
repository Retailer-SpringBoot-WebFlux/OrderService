# Retailer Microservices (Spring WebFlux + JPA + MySQL)

## 📌 Overview
This project consists of four microservices:
- **Product Service**: Manages products
- **Order Service**: Handles customer orders
- **Customer Service**: Stores customer details
- **Cart Service**: Manages the shopping cart

## 🚀 Tech Stack
- **Spring Boot 3.2**
- **Spring WebFlux + WebClient**
- **Spring JPA + MySQL**
- **Gradle** as the build tool

## 🛠 Setup Instructions

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/retailer-microservices.git
cd retailer-microservices

CREATE DATABASE retailer_db;
USE retailer_db;

-- Databse Tables
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_name (name),  -- Index for quick name search
    INDEX idx_product_category (category)  -- Index for category-based queries
);
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_customer_cart (customer_id),  -- Index for retrieving cart items per customer
    INDEX idx_product_cart (product_id)  -- Index for looking up products in carts
);
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_customer_email (email),  -- Index for faster lookups by email
    INDEX idx_customer_phone (phone)  -- Index for searching customers by phone
);
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(50) DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
    INDEX idx_order_status (order_status),  -- Index for filtering orders by status
    INDEX idx_order_date (order_date),  -- Index for retrieving recent orders
    INDEX idx_customer_orders (customer_id),  -- Index to speed up customer orders query
    INDEX idx_product_orders (product_id)  -- Index to optimize product-based order lookups
);
//workflow
Microservices Communication
Each service communicates using WebClient:

Order Service → Product Service (to fetch product details)
Cart Service → Product & Customer Service (to validate cart items)


properties file 
JAVA_OPTS=-Xms256m -Xmx1024m
yml file
services:
  product-service:
    environment:
      - JAVA_OPTS=-Xms256m -Xmx1024m


1️⃣ Customer Service → Create customer.
2️⃣ Product Service → Get product details.
3️⃣ Cart Service → Add product to cart.
4️⃣ Order Service → Place order.
5️⃣ Product Service → Reduce stock quantity.
6️⃣ Order Service → Track order status.

Here’s how the end-to-end flow will work across all services:
Customer Service → Create/Get a customer.
Product Service → Get available products.
Cart Service → Add products to the cart.
Order Service → Place an order from the cart.
