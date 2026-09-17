# 🏨 Hotel Management System

A **Java SE hotel management system** designed to manage users, rooms, reservations, payments, invoices, cancellations, refunds, and hotel performance indicators.

The project is built with a **layered architecture** and applies several software engineering principles and design patterns, including the **Repository Pattern**, **Singleton Pattern**, and **Strategy Pattern**.

---

## 📌 Overview

The system provides two main roles:

* **ADMIN** — manages rooms, reservations, payments, invoices, and hotel statistics.
* **CLIENT** — searches available rooms, creates reservations, makes payments, views invoices, and cancels reservations.

The application runs through a **CLI (Command-Line Interface)** and persists data in **PostgreSQL** using **JDBC**.

### Main architecture

```text
┌─────────────────────────────┐
│          CLI Layer          │
│  Menus / Input / Validation │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│       Service Layer         │
│ Business Logic / Rules      │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│      Repository Layer       │
│ CRUD / Queries / Mapping    │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│        JDBC Layer           │
│ Connections / Transactions  │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│        PostgreSQL           │
│       Persistent Data       │
└─────────────────────────────┘
```

---

## ✨ Features

### 👤 Authentication

* User registration
* User login
* Password hashing with SHA-256
* Random password salt
* Role-based authorization
* Custom authentication exceptions

### 🛏️ Room Management

Administrators can:

* Create rooms
* Update rooms
* Delete rooms
* Change room status
* View all rooms
* Search available rooms
* Filter rooms by:

    * Type
    * Capacity
    * Availability
    * Price

### 📅 Reservation Management

Clients can:

* Search available rooms
* Select check-in and check-out dates
* Create reservations
* View reservation details
* View reservation history
* Cancel reservations

The system prevents overlapping reservations for the same room.

### 💰 Dynamic Pricing

Room prices are calculated dynamically according to several business rules:

| Rule                           | Adjustment |
| ------------------------------ | ---------: |
| High season — July/August      |       +30% |
| Low season — November/February |       -15% |
| Friday/Saturday night          |       +15% |
| Stay ≥ 7 nights                |       -10% |
| Stay ≥ 14 nights               |       -15% |
| Booking ≥ 30 days in advance   |        -5% |
| Booking ≤ 3 days in advance    |       +10% |

Pricing is implemented using the **Strategy Pattern**, making the pricing algorithm replaceable without modifying the reservation logic.

### 💳 Payments

Supported payment methods:

* Cash
* Card

Payment statuses include:

* Pending
* Completed
* Failed
* Refunded

Reservation, payment, and invoice creation are handled inside a **JDBC transaction** to guarantee atomicity.

### 🧾 Invoices

The system supports:

* Invoice generation
* Unique invoice numbers
* HT amount calculation
* VAT calculation
* TTC calculation
* Invoice retrieval

VAT:

```text
TVA = 20%

TTC = HT + TVA
```

### 🔄 Cancellation & Refund

Refund policies depend on the time remaining before check-in:

| Cancellation time | Refund | Penalty |
| ----------------- | -----: | ------: |
| More than 14 days |   100% |      0% |
| 7–14 days         |    70% |     30% |
| 48h–7 days        |    50% |     50% |
| Less than 48h     |     0% |    100% |

The refund policy is implemented using the **Strategy/Policy Pattern**.

### 📊 Reports & KPIs

Administrators can access:

* Occupancy rate
* Total revenue
* Average booking value
* Number of reservations per room
* Revenue per room
* Most requested rooms

---

# 🛠️ Tech Stack

| Technology     | Purpose                       |
| -------------- | ----------------------------- |
| **Java 17+**   | Application development       |
| **Maven**      | Dependency & build management |
| **PostgreSQL** | Relational database           |
| **JDBC**       | Database access               |
| **Docker**     | Database infrastructure       |
| **pgAdmin**    | PostgreSQL administration     |
| **JUnit 5**    | Testing                       |
| **Mockito**    | Mocking                       |
| **Git**        | Version control               |
| **GitHub**     | Source code hosting           |

---

# 🏗️ Project Structure

```text
hotel-management-system/
│
├── docker/
│   └── docker-compose.yml
│
├── sql/
│   ├── schema.sql
│   ├── indexes.sql
│   ├── constraints.sql
│   └── seed.sql
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   │
│   │   │   ├── config/
│   │   │   ├── db/
│   │   │   │   └── DatabaseConnection.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Room.java
│   │   │   │   ├── Reservation.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Invoice.java
│   │   │   │   └── enums/
│   │   │   │
│   │   │   ├── dto/
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── RoomRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── InvoiceRepository.java
│   │   │   │   └── jdbc/
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── RoomService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   ├── PricingService.java
│   │   │   │   └── ReportService.java
│   │   │   │
│   │   │   ├── strategy/
│   │   │   │   ├── PricingStrategy.java
│   │   │   │   └── DynamicPricingStrategy.java
│   │   │   │
│   │   │   ├── policy/
│   │   │   │   ├── RefundPolicy.java
│   │   │   │   └── DefaultRefundPolicy.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │
│   │   │   ├── util/
│   │   │   │
│   │   │   └── cli/
│   │   │       ├── MainMenu.java
│   │   │       ├── AuthMenu.java
│   │   │       ├── ClientMenu.java
│   │   │       └── AdminMenu.java
│   │   │
│   │   └── resources/
│   │       └── db.properties.example
│   │
│   └── test/
│       └── java/
│
├── docs/
│   ├── architecture.md
│   ├── database.md
│   ├── business-rules.md
│   ├── design-patterns.md
│   ├── installation.md
│   └── testing.md
│
├── .env.example
├── .gitignore
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# 🗄️ Database

The application uses PostgreSQL.

### Main entities

```text
USER
 │
 │ 1
 │
 │ N
 ▼
RESERVATION
 │
 ├──────────────► ROOM
 │
 ├──────────────► PAYMENT
 │
 └──────────────► INVOICE
```

### Tables

```text
users
rooms
reservations
payments
invoices
```

### Relationships

```text
users          1 ─────── N reservations

rooms          1 ─────── N reservations

reservations   1 ─────── N payments

reservations   1 ─────── 0..1 invoices
```

An invoice belongs to a reservation, while a reservation can have multiple payment records.

---

# 🔐 Database Security & Integrity

The database uses:

* Primary keys
* Foreign keys
* Unique constraints
* `NOT NULL`
* `CHECK` constraints
* Indexes
* Referential integrity
* PostgreSQL transactions

Overlapping reservations for the same room are prevented at the database level.

Adjacent reservations are allowed:

```text
Reservation A
[───────)

Reservation B
        [───────)
```

But overlapping reservations are rejected:

```text
Reservation A
[────────────)

Reservation B
      [────────────)
```

---

# 🧩 Design Patterns

## Repository Pattern

The Repository layer abstracts database operations from business logic.

```text
Service
   │
   ▼
Repository Interface
   │
   ▼
JDBC Repository
   │
   ▼
PostgreSQL
```

Example:

```java
public interface RoomRepository {

    Optional<Room> findById(Long id);

    List<Room> findAll();

    void save(Room room);

    void update(Room room);

    void delete(Long id);
}
```

The Service layer does not contain SQL.

---

## Singleton Pattern

`DatabaseConnection` uses a **thread-safe Singleton with Double-Checked Locking**.

```text
Application
     │
     ▼
DatabaseConnection
     │
     └── single shared instance
```

The Singleton controls access to the database connection infrastructure.

---

## Strategy Pattern

Pricing algorithms are separated from the reservation workflow.

```text
             PricingStrategy
                    ▲
                    │
        ┌───────────┴───────────┐
        │                       │
DynamicPricingStrategy     FutureStrategy
```

The reservation service depends on the strategy abstraction instead of a concrete pricing algorithm.

---

## Policy Pattern

Refund calculations are separated from cancellation logic.

```text
             RefundPolicy
                  ▲
                  │
       DefaultRefundPolicy
```

This allows different refund policies to be introduced later without rewriting `ReservationService`.

---

# 💵 Pricing Calculation

The pricing system uses `BigDecimal` for monetary calculations.

Example:

```text
Base price
    ↓
Season adjustment
    ↓
Weekend adjustment
    ↓
Long-stay adjustment
    ↓
Early / last-minute adjustment
    ↓
Final room price
```

The exact order of applying adjustments is documented and covered by tests.

---

# 🔄 Transaction Management

Reservation creation is an atomic operation.

```text
BEGIN TRANSACTION
       │
       ├── Create reservation
       │
       ├── Create payment
       │
       ├── Create invoice
       │
       ├── COMMIT
       │
       ▼
    SUCCESS
```

If one operation fails:

```text
BEGIN TRANSACTION
       │
       ├── Create reservation
       ├── Create payment
       ├── Invoice fails ❌
       │
       ▼
    ROLLBACK
       │
       ▼
All changes cancelled
```

This ensures that the database does not contain partially completed business operations.

---

# 🚀 Getting Started

## Prerequisites

Make sure you have installed:

* Java 17 or newer
* Maven
* Docker
* Docker Compose
* Git

Optional:

* pgAdmin
* IntelliJ IDEA

Verify your installation:

```bash
java -version
mvn -version
docker --version
docker compose version
git --version
```

---

## 1. Clone the repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd hotel-management-system
```

---

## 2. Configure environment variables

Create your local environment file:

```bash
cp .env.example .env
```

Example:

```env
POSTGRES_DB=hotel_db
POSTGRES_USER=hotel_user
POSTGRES_PASSWORD=your_password

PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=your_password
```

> Never commit `.env` or real credentials to GitHub.

---

## 3. Start PostgreSQL

```bash
docker compose up -d
```

Check the containers:

```bash
docker compose ps
```

View PostgreSQL logs:

```bash
docker compose logs postgres
```

---

## 4. Configure the application

Create your local database configuration from the example:

```text
src/main/resources/db.properties.example
```

Example:

```properties
db.url=jdbc:postgresql://localhost:5432/hotel_db
db.username=hotel_user
db.password=your_password
```

The local configuration file containing credentials should not be committed.

---

## 5. Initialize the database

Execute the SQL scripts in the following order:

```text
1. schema.sql
2. indexes.sql
3. constraints.sql
4. seed.sql
```

The database can also be initialized automatically depending on the configured application setup.

---

## 6. Build the project

```bash
mvn clean install
```

---

## 7. Run tests

```bash
mvn test
```

---

## 8. Run the application

```bash
mvn exec:java
```

Or run:

```text
src/main/java/Main.java
```

directly from your IDE.

---

# 🖥️ CLI

After launching the application:

```text
================================
      HOTEL MANAGEMENT SYSTEM
================================

1. Login
2. Register
3. Exit

Choose an option:
```

### Client

```text
================================
          CLIENT MENU
================================

1. Search available rooms
2. Make a reservation
3. My reservations
4. Cancel reservation
5. View invoice
6. Logout
```

### Admin

```text
================================
           ADMIN MENU
================================

1. Manage rooms
2. View reservations
3. View payments
4. View invoices
5. View hotel KPIs
6. View room statistics
7. Logout
```

---

# 🧪 Testing

The project includes several levels of testing.

### Unit Tests

Business logic is tested independently:

* Authentication
* Password hashing
* Pricing
* Refund calculation
* Date validation
* Reservation rules
* Input validation

### Integration Tests

Database operations are tested against PostgreSQL:

* User repository
* Room repository
* Reservation repository
* Payment repository
* Invoice repository
* Transaction behavior

### Critical Scenarios

The application tests:

```text
✓ Successful registration
✓ Successful login
✓ Invalid credentials
✓ Room availability
✓ Overlapping reservations
✓ Adjacent reservations
✓ Dynamic pricing
✓ Payment creation
✓ Invoice creation
✓ Transaction rollback
✓ Cancellation
✓ Refund calculation
✓ KPI calculation
```

---

# 📚 Documentation

Additional technical documentation is available in:

```text
docs/
├── architecture.md
├── database.md
├── business-rules.md
├── design-patterns.md
├── installation.md
└── testing.md
```

Documentation includes:

* System architecture
* Database design
* Business rules
* Design patterns
* Installation guide
* Testing strategy
* UML diagrams
* ERD
* Sequence diagrams

---

# 🌿 Git Workflow

The project follows a simple Git workflow:

```text
feature/*
     │
     ▼
 develop
     │
     ▼
   main
```

Example:

```bash
git checkout develop

git checkout -b feature/reservation-management

git add .

git commit -m "feat: implement reservation management"

git push origin feature/reservation-management
```

After review:

```text
feature/reservation-management
            ↓
         develop
            ↓
           main
```

---

# 📋 Development Roadmap

* [x] Project initialization
* [x] Maven configuration
* [x] GitHub repository
* [ ] Docker infrastructure
* [ ] PostgreSQL database
* [ ] Domain models
* [ ] DTO layer
* [ ] JDBC connection
* [ ] Repository layer
* [ ] Authentication
* [ ] Room management
* [ ] Dynamic pricing
* [ ] Reservation management
* [ ] Payment management
* [ ] Invoice generation
* [ ] Cancellation & refunds
* [ ] Reporting & KPIs
* [ ] CLI
* [ ] Unit tests
* [ ] Integration tests
* [ ] Documentation
* [ ] Final integration
* [ ] Release `v1.0.0`

---

# 🎯 Project Objectives

This project aims to demonstrate practical application of:

* Object-Oriented Programming
* Clean architecture principles
* Layered architecture
* Repository Pattern
* Singleton Pattern
* Strategy Pattern
* DTO pattern
* JDBC
* PostgreSQL
* Transaction management
* Exception handling
* Authentication
* Data validation
* Automated testing
* Git workflow
* Docker-based development environments

---

# 👨‍💻 Author

**Oussama Ait Youss**

Java / Full Stack Developer — Student at **YouCode**

### Technologies of interest

```text
Java • Spring Boot • Angular • Laravel • React
PostgreSQL • Docker • Git • REST APIs
```

---

# 📄 License

This project is developed for **educational and training purposes**.

Add an appropriate open-source license if the project is later distributed publicly.
