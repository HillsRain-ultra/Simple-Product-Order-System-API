# Simple Product & Order System API

---

## Table of Contents

1.  [Core Features Implemented](#1-core-features-implemented)
2.  [Tech Stack](#2-tech-stack)
3.  [How to Run & Test](#3-how-to-run--test)
    * [3.1. Prerequisites](#31-prerequisites)
    * [3.2. Running the Application](#32-running-the-application)
    * [3.3. Pre-filled Data (Demo Accounts)](#33-pre-filled-data-demo-accounts)
4.  [API Specification (Swagger UI)](#4-api-specification-swagger-ui)
5.  [Key Architectural & Design Decisions](#5-key-architectural--design-decisions)
    * [5.1. Database Design (EERD)](#51-database-design-eerd)
    * [5.2. Transaction Handling: Atomicity & Concurrency](#52-transaction-handling-atomicity--concurrency)
    * [5.3. Authentication & Authorization (JWT)](#53-authentication--authorization-jwt)

---

## 1. Core Features Implemented

* **Product Management:** Full CRUD operations for products (Create, Retrieve, Delete), protected by an `ADMIN` role.
* **Secure Order Management:** A robust order creation flow that handles real-time, concurrency-safe stock deduction.
* **Authentication & Authorization:** Full Sign Up and Sign In flow using Spring Security and JWT.
* **Role-Based Access Control (RBAC):** Differentiates between `CUSTOMER` and `ADMIN` roles using `@PreAuthorize`.
* **API Documentation:** Interactive API documentation via SpringDoc (Swagger UI) with full authentication support.
* **Automated Data Seeding:** A `CommandLineRunner` pre-fills the database with demo accounts and products for easy testing.

## 2. Tech Stack

* **Java 21**
* **Spring Boot 3.3.0**
* **Spring Security (OAuth2 Resource Server):** For JWT validation and endpoint protection.
* **Spring Data JPA (Hibernate):** For data persistence and ORM.
* **MySQL:** Relational database.
* **Nimbus JOSE + JWT:** For manual JWT generation during authentication.
* **SpringDoc (Swagger UI):** For API documentation.
* **Lombok:** To reduce boilerplate code.
* **Maven:** For dependency management.

## 3. How to Run & Test

### 3.1. Prerequisites

* Java 21 (JDK)
* Maven 3.x
* A running MySQL server

### 3.2. Running the Application

1.  Clone the repository.
2.  Update the `application.yaml` file with your MySQL username and password.
3.  Run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```

### 3.3. Pre-filled Data (Demo Accounts)

The application uses a `DataInitializer` to pre-fill the database with sample data.

| Role | Email (Username) | Password |
| :--- | :--- | :--- |
| **ADMIN** | `admin@gmail.com` | `admin123` |
| **CUSTOMER** | `customer@gmail.com` | `customer123` |

## 4. API Specification (Swagger UI)

An interactive API documentation (Swagger UI) is available. Once the application is running, you can access it here:

**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Quick Test Flow:

1.  Go to `1. Authentication Service` $\to$ `POST /api/auth/login`.
2.  Use the "Example Value" (or the demo accounts above) to log in and get a JWT **`token`**.
3.  **Copy** the token.
4.  Click the **"Authorize"** button (top right of the page).
5.  In the popup, paste the token into the "Value" field (it should be pre-filled with `Bearer `) and click "Authorize".
6.  You are now authenticated and can test the "locked" endpoints (e.g., `POST /api/orders`).

---

## 5. Key Architectural & Design Decisions

This section details the solutions for the core evaluation points of the assignment.

### 5.1. Database Design (EERD)

The database schema was designed to handle the **Many-to-Many (N:M)** relationship between `Orders` and `Products`. A standard N:M link was insufficient as the system must store the `quantity` and `pricePerUnit` *at the time of purchase*.

To solve this, I used a **Junction Table (Associative Entity)** called `order_item` (`HAS` in the diagram).

* `User (1)` $\to$ (N) `Order`
* `Order (1)` $\to$ (N) `OrderItem`
* `Product (1)` $\to$ (N) `OrderItem`
* `User (ADMIN)` also creates and updates `Product`.

![EERD Diagram](/docs/eerdLightVision.drawio.png)

### 5.2. Transaction Handling: Atomicity & Concurrency

"Transaction handling" was a primary evaluation point. My solution addresses it in two layers: Atomicity (ensuring the order is all-or-nothing) and Concurrency (preventing race conditions).

#### A. Atomicity & Rollbacks (The `@Transactional` part)

The `createOrder` method is a complex "Unit of Work". It must validate all items, calculate prices, and deduct stock. This entire process must be **Atomic**: it either *all* succeeds, or *nothing* is saved to the database.

1.  **`@Transactional`:** The entire `createOrder` method in `OrderService` is annotated with `@Transactional`.
2.  **Default Rollback Behavior:** By default, Spring's `@Transactional` will automatically trigger a **ROLLBACK** for any `RuntimeException` (hoặc `Error`).
3.  **Fail-Fast Validation:** The code is designed to throw an `IllegalArgumentException` (which is a `RuntimeException`) if stock is insufficient.
4.  **Result:** When this exception is thrown, the `@Transactional` annotation catches it and automatically rolls back the entire transaction. This prevents any partial data (like stock being deducted for item 1 but not item 2) from ever being committed to the database.

#### B. Concurrency (The "Deduct Stock" Problem)

Atomicity alone does not solve **race conditions** (e.g., two users buying the last item simultaneously).

1.  **The Problem:**
    * User A reads stock (1 item left).
    * User B reads stock (1 item left).
    * User A deducts stock (stock becomes 0) and commits.
    * User B deducts stock (stock becomes -1) and commits. $\to$ The system is broken.
2.  **The Solution (Pessimistic Locking):** To prevent this, I used `LockModeType.PESSIMISTIC_WRITE` in the `ProductRepository`.
    * When `createOrder` fetches a product to check its stock, it uses `findAndLockById()`.
    * The database places an exclusive **write lock** on that product's row.
    * Any other transaction (like User B) attempting to read/write that same row is forced to **WAIT** until User A's transaction is finished (committed or rolled back).
    * This guarantees that the stock check and deduction are a single, atomic operation, completely preventing overselling.

**Code Snippet (`ProductRepository.java`):**
```java
// ProductRepository.java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Product p WHERE p.id = :id")
Optional<Product> findAndLockById(@Param("id") String id);
```

### 5.3. Authentication & Authorization (JWT)

The system implements JWT-based security using the `spring-boot-starter-oauth2-resource-server` module.

- **Authentication Flow (Token Generation):**
  * A user calls `POST /api/auth/login` or `POST /api/auth/create`.
  * `AuthService` validates the user (using the injected `PasswordEncoder` bean) or creates a new one (hashing the password).
  * `AuthService` manually generates a signed JWT (using the **Nimbus JOSE + JWT library**) containing the user's ID (in the `sub` claim) and their `role` (e.g., `ROLE_ADMIN`).
- **Authorization Flow (Token Validation):**
  * The client sends the JWT in the `Authorization: Bearer <token>` header.
  * `SecurityConfig` (configured as an `oauth2ResourceServer`) intercepts the request.
  * It uses a `JwtDecoder` bean to validate the token's signature (using the shared `SIGNER_KEY`).
  * A custom `JwtAuthenticationConverter` bean reads the `role` claim from the token and converts it into a Spring Security `GrantedAuthority` (e.g., `ROLE_ADMIN`).
  * `@PreAuthorize` annotations (e.g., `@PreAuthorize("hasRole('ADMIN')")`) on the Controllers enforce the access rules.
- **Principal Extraction (Accessing User Data):**
  * For features like `GET /api/orders/my-orders`, the authenticated user's ID is securely extracted from the token.
  * Spring Security's `Authentication` object is injected into the controller. The `userId` (which was stored in the token's `subject`) is retrieved simply by calling `authentication.getName()`.
  * This `userId` is then passed to the `OrderService` to query the database (`orderRepository.findByUser_Id(userId)`).