# Ecommerce Project

A simple Ecommerce backend application built with Spring Boot. It provides RESTful APIs for product management, user authentication, cart operations, and order processing.

## Features
- User registration and login
- Product listing and management
- Shopping cart (add, view, update, remove, clear)
- Order checkout with payment method (UPI/COD)
- Order summary after checkout
- Responsive, modern frontend with color theme
- Logout functionality

## Frontend Features
- **Responsive Design:** Works on all screen sizes using Bootstrap grid and utilities.
- **Colorful UI:** Gradient navbar, colored cards, tables, and alerts for a modern look.
- **Cart Management:**
  - Add to cart from product list
  - View cart with editable quantities
  - Update or remove items directly in the cart
  - Clear the entire cart
- **Authentication:** Register, login, and logout with clear feedback.
- **Consistent Layout:** All pages use a unified, attractive design.

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
- `POST /api/cart/checkout` — Checkout with payment method
- `GET /api/cart/summary` — View order summary

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

## Quick Setup & Testing with Postman

To quickly test the API and populate sample data, use the provided Postman collection:

1. **Import the Collection**
   - Open Postman.
   - Click `Import` and select the file `Ecommerce_Postman_Collection.json` from the project root.

2. **Run the Requests**
   - The collection includes requests to:
     - Register a user
     - Add 10 products
     - Add a product to the cart
     - View the cart
     - Checkout with payment method
     - View order summary
   - Click each request and hit `Send` to execute.

This is a fast way to get your backend populated and ready for frontend testing or further development.

## License
This project is open source and available under the [MIT License](LICENSE). 