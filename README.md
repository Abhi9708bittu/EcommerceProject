# Ecommerce Project

A simple Ecommerce backend application built with Spring Boot. It provides RESTful APIs for product management, user authentication, cart operations, and order processing.

## Features
- User registration and login
- Product listing and management
- Shopping cart (add, view, clear)
- Order checkout

## API Endpoints

### Authentication
- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login with username and password

### Products
- `GET /api/products` — List all products
- `GET /api/products/{id}` — Get product details by ID
- `POST /api/products` — Add a new product

### Cart
- `GET /api/cart` — View current cart
- `POST /api/cart/add` — Add an item to the cart
- `POST /api/cart/clear` — Clear the cart

### Orders
- `POST /api/orders/checkout` — Place an order (checkout)

## Getting Started

### Prerequisites
- Java 17 or above
- Maven

### Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/Abhi9708bittu/EcommerceProject.git
   cd EcommerceProject
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

The server will start on `http://localhost:8080` by default.

## Project Structure
- `controller/` — REST API controllers
- `model/` — Data models (User, Product, CartItem, Order)
- `repository/` — Data access layer
- `service/` — Business logic

## License
This project is open source and available under the [MIT License](LICENSE). 