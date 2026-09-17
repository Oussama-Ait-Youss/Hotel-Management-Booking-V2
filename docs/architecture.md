# System Architecture

## 1. Overview

The Hotel Management System follows a **layered architecture** designed to separate user interaction, business logic, data access, and database infrastructure.

The main communication flow is:

```text
┌─────────────────────────────┐
│            CLI              │
│   User interaction/input    │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          SERVICE            │
│      Business Logic         │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│        REPOSITORY            │
│   Data Access Abstraction   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│            JDBC             │
│   Database Communication    │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│         PostgreSQL          │
│       Persistent Data       │
└─────────────────────────────┘
```

The objective is to ensure that each layer has a **single and well-defined responsibility**.

---

# 2. Architecture Goals

The architecture is designed around the following principles:

* Separation of concerns
* Single Responsibility Principle
* Low coupling
* High cohesion
* Maintainability
* Testability
* Reusability
* Clear dependency direction
* Centralized business rules
* Centralized database access

The architecture should make it possible to modify one part of the system without unnecessarily affecting the others.

For example:

* Changing the CLI should not require changing SQL.
* Changing PostgreSQL queries should not require changing the CLI.
* Changing a pricing rule should not require modifying the repository.
* Replacing the CLI with another interface should not require rewriting the business logic.

---

# 3. Layered Architecture

## 3.1 CLI Layer

### Responsibility

The CLI is responsible for **user interaction**.

It handles:

* Displaying menus
* Reading user input
* Calling services
* Displaying results
* Displaying user-friendly error messages
* Basic input validation

Example components:

```text
cli/
├── MainMenu.java
├── AuthMenu.java
├── ClientMenu.java
├── AdminMenu.java
└── InputUtils.java
```

### Example

```java
String email = inputUtils.readString("Email: ");
String password = inputUtils.readString("Password: ");

authService.login(email, password);
```

The CLI should delegate business operations to the Service layer.

### The CLI must NOT:

* Execute SQL
* Open database connections
* Implement reservation business rules
* Calculate complex prices
* Implement refund policies
* Directly manipulate database entities

The CLI is concerned with:

> **"How does the user interact with the system?"**

---

# 4. Service Layer

## Responsibility

The Service layer contains the **business logic and application use cases**.

It coordinates repositories, strategies, policies, validations, and transactions to perform complete business operations.

Example services:

```text
service/
├── AuthService.java
├── RoomService.java
├── ReservationService.java
├── PricingService.java
├── PaymentService.java
├── InvoiceService.java
└── ReportService.java
```

### Example

Creating a reservation may require:

1. Validate dates
2. Check room availability
3. Calculate the price
4. Create the reservation
5. Process payment
6. Generate invoice
7. Commit the transaction

This orchestration belongs to the Service layer.

```text
ReservationService
        │
        ├── Validate dates
        │
        ├── Check availability
        │
        ├── PricingService
        │
        ├── ReservationRepository
        │
        ├── PaymentRepository
        │
        └── InvoiceRepository
```

The Service layer answers:

> **"What should the application do?"**

---

# 5. Repository Layer

## Responsibility

The Repository layer provides an abstraction over persistent data.

It is responsible for:

* CRUD operations
* Queries
* Retrieving entities
* Persisting entities
* Mapping database records to Java objects

Example:

```text
repository/
├── UserRepository.java
├── RoomRepository.java
├── ReservationRepository.java
├── PaymentRepository.java
└── InvoiceRepository.java
```

Implementations:

```text
repository/
└── jdbc/
    ├── JdbcUserRepository.java
    ├── JdbcRoomRepository.java
    ├── JdbcReservationRepository.java
    ├── JdbcPaymentRepository.java
    └── JdbcInvoiceRepository.java
```

### Example

```java
public interface RoomRepository {

    Optional<Room> findById(Long id);

    List<Room> findAll();

    List<Room> findAvailableRooms(
        LocalDate checkIn,
        LocalDate checkOut
    );

    void save(Room room);

    void update(Room room);

    void delete(Long id);
}
```

The Service layer uses the interface:

```text
RoomService
     │
     ▼
RoomRepository
     │
     ▼
JdbcRoomRepository
```

This keeps the Service layer independent from the concrete database implementation.

### The Repository must NOT:

* Display CLI menus
* Read user input
* Contain UI logic
* Decide application workflows
* Implement unrelated business rules

The Repository answers:

> **"How do we store and retrieve this data?"**

---

# 6. JDBC Layer

## Responsibility

JDBC is the technology used to communicate with PostgreSQL.

It handles:

* Opening database connections
* Creating `PreparedStatement`
* Executing SQL
* Reading `ResultSet`
* Handling database transactions
* Closing resources

The project uses a centralized database connection component:

```text
db/
└── DatabaseConnection.java
```

The connection management uses a **thread-safe Singleton with Double-Checked Locking**.

### JDBC flow

```text
Repository
    │
    ▼
DatabaseConnection
    │
    ▼
JDBC Driver
    │
    ▼
PostgreSQL
```

### Example

```java
String sql = """
    SELECT id, room_number, type, capacity, base_price
    FROM rooms
    WHERE id = ?
""";

try (PreparedStatement statement =
         connection.prepareStatement(sql)) {

    statement.setLong(1, roomId);

    try (ResultSet resultSet = statement.executeQuery()) {
        // Map ResultSet to Room
    }
}
```

### JDBC rules

The project must:

* Use `PreparedStatement`
* Use try-with-resources
* Avoid SQL injection
* Properly handle `SQLException`
* Close database resources
* Use transactions for atomic business operations

JDBC answers:

> **"How does Java communicate with PostgreSQL?"**

---

# 7. PostgreSQL Layer

PostgreSQL is responsible for **persistent data storage and database-level integrity**.

The database contains:

```text
users
rooms
reservations
payments
invoices
```

PostgreSQL is also responsible for enforcing database constraints such as:

* Primary keys
* Foreign keys
* Unique constraints
* `NOT NULL`
* `CHECK`
* Referential integrity
* Reservation overlap protection

Example:

```text
Application
     │
     ▼
PostgreSQL
     │
     ├── PK
     ├── FK
     ├── UNIQUE
     ├── CHECK
     └── Constraints
```

The database answers:

> **"How is persistent data stored and protected?"**

---

# 8. Dependency Direction

The dependency direction follows:

```text
CLI
 ↓
Service
 ↓
Repository
 ↓
JDBC
 ↓
PostgreSQL
```

Higher-level layers should not bypass the architecture.

For example, this is **not allowed**:

```text
CLI ───────────────────► PostgreSQL
```

or:

```text
Service ────────────────► JDBC SQL
```

Instead:

```text
CLI
 ↓
Service
 ↓
Repository
 ↓
JDBC
 ↓
PostgreSQL
```

This prevents different parts of the application from accessing the database in uncontrolled ways.

---

# 9. Separation of Responsibilities

| Layer      | Main Responsibility     | Must Not Do           |
| ---------- | ----------------------- | --------------------- |
| CLI        | User interaction        | SQL / database access |
| Service    | Business logic          | Direct SQL            |
| Repository | Data access abstraction | UI logic              |
| JDBC       | Database communication  | Business decisions    |
| PostgreSQL | Persistence & integrity | Application workflows |

A simple mental model:

```text
CLI
"What does the user want?"

Service
"What should the application do?"

Repository
"How do we retrieve/store the data?"

JDBC
"How do we communicate with the database?"

PostgreSQL
"How do we persist and protect the data?"
```

---

# 10. Example: Creating a Reservation

The following example demonstrates how the layers collaborate.

## Step 1 — CLI

The client chooses a room and provides dates.

```text
Room: 101
Check-in: 2026-10-10
Check-out: 2026-10-15
```

The CLI calls:

```java
reservationService.createReservation(request);
```

---

## Step 2 — Service

`ReservationService` coordinates the operation.

```text
ReservationService
       │
       ├── Validate dates
       │
       ├── Check room availability
       │
       ├── Calculate price
       │
       ├── Create reservation
       │
       ├── Process payment
       │
       └── Generate invoice
```

---

## Step 3 — Repository

The Service requests the required data:

```java
roomRepository.findAvailableRooms(checkIn, checkOut);
```

and:

```java
reservationRepository.save(reservation);
```

---

## Step 4 — JDBC

The repository executes SQL through JDBC.

```sql
INSERT INTO reservations (
    user_id,
    room_id,
    check_in,
    check_out,
    status,
    total_amount
)
VALUES (?, ?, ?, ?, ?, ?);
```

---

## Step 5 — PostgreSQL

PostgreSQL:

1. Executes the SQL
2. Validates constraints
3. Stores the reservation
4. Returns the result

The response travels back:

```text
PostgreSQL
    ↑
   JDBC
    ↑
Repository
    ↑
 Service
    ↑
   CLI
    ↑
  User
```

---

# 11. Business Logic Placement

Business rules must be located in the appropriate layer.

### Example: Dynamic Pricing

Incorrect:

```text
CLI
 └── calculate 30% high-season increase
```

Correct:

```text
CLI
 ↓
ReservationService
 ↓
PricingService
 ↓
PricingStrategy
```

### Example: Refund

Incorrect:

```text
CLI
 └── calculate refund percentage
```

Correct:

```text
CLI
 ↓
ReservationService
 ↓
RefundPolicy
```

This keeps business rules independent from the user interface.

---

# 12. Design Patterns Used

The architecture incorporates several design patterns.

## Repository Pattern

Used to abstract data persistence.

```text
Service
   ↓
Repository Interface
   ↓
JDBC Repository
```

---

## Singleton Pattern

Used for centralized database connection management.

```text
DatabaseConnection
        │
        ▼
Single shared instance
```

---

## Strategy Pattern

Used for dynamic pricing.

```text
PricingStrategy
       ▲
       │
DynamicPricingStrategy
```

The reservation system can use another pricing strategy without modifying the reservation workflow.

---

## Policy Pattern

Used for refund rules.

```text
RefundPolicy
      ▲
      │
DefaultRefundPolicy
```

This separates cancellation rules from reservation management.

---

# 13. DTO Layer

DTOs are used to transfer structured data between application layers without exposing unnecessary internal details.

Examples:

```text
dto/
├── RoomSearchCriteria.java
├── AvailableRoomDTO.java
├── ReservationSummaryDTO.java
├── HotelKpiDTO.java
└── RoomStatisticsDTO.java
```

For example:

```text
Database Entity
      │
      ▼
Reservation
      │
      ▼
ReservationSummaryDTO
      │
      ▼
CLI
```

DTOs are especially useful for search results, summaries, statistics, and reports.

---

# 14. Exception Handling

The application uses custom exceptions to represent business and technical errors.

Example:

```text
exception/
├── HotelException.java
├── AuthenticationException.java
├── AuthorizationException.java
├── ReservationException.java
├── InvalidReservationException.java
├── RoomNotAvailableException.java
├── PaymentException.java
└── InvoiceException.java
```

The Service layer detects business errors.

The CLI converts those errors into user-friendly messages.

Example:

```text
Service
   │
   └── RoomNotAvailableException
                │
                ▼
              CLI
                │
                ▼
"Room 101 is not available for the selected dates."
```

Technical details such as stack traces should not normally be displayed to end users.

---

# 15. Transaction Management

Some operations require multiple database modifications to succeed together.

For example:

```text
Reservation
Payment
Invoice
```

These operations are handled using a database transaction.

```text
BEGIN
  │
  ├── INSERT reservation
  │
  ├── INSERT payment
  │
  ├── INSERT invoice
  │
  └── COMMIT
```

If any operation fails:

```text
BEGIN
  │
  ├── INSERT reservation
  ├── INSERT payment
  ├── INSERT invoice ❌
  │
  └── ROLLBACK
```

This guarantees that the system does not leave partially completed operations.

---

# 16. Architecture Rules

The following rules must be respected during development.

### Rule 1 — No SQL in the Service layer

❌

```java
String sql = "SELECT * FROM rooms";
```

inside a Service.

✅

```java
roomRepository.findAll();
```

---

### Rule 2 — No database access in the CLI

❌

```java
Connection connection = ...
```

inside a menu.

✅

```java
roomService.findAvailableRooms(...);
```

---

### Rule 3 — Business rules belong in Services / Strategies / Policies

Pricing, cancellation, refund, reservation validation, and authorization logic should not be duplicated inside the CLI or repositories.

---

### Rule 4 — Repositories handle persistence

Repositories are responsible for:

```text
CRUD
Queries
Mapping
Persistence
```

They should not decide the overall application workflow.

---

### Rule 5 — Use abstractions

Services should depend on repository interfaces where possible:

```java
private final RoomRepository roomRepository;
```

instead of directly depending on:

```java
private final JdbcRoomRepository repository;
```

This improves testability and reduces coupling.

---

# 17. Final Architecture

The complete architecture can be summarized as:

```text
                         USER
                          │
                          ▼
                 ┌─────────────────┐
                 │       CLI       │
                 │ Menus / Inputs  │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │     SERVICE     │
                 │ Business Logic  │
                 │  Use Cases      │
                 └────────┬────────┘
                          │
             ┌────────────┼────────────┐
             │            │            │
             ▼            ▼            ▼
       PricingStrategy  RefundPolicy  DTO
             │
             └────────────┐
                          ▼
                 ┌─────────────────┐
                 │   REPOSITORY    │
                 │ Data Access API │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │      JDBC       │
                 │ SQL / Connection│
                 │  Transactions   │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │   PostgreSQL    │
                 │ Persistent Data │
                 │   Constraints   │
                 └─────────────────┘
```

---

# 18. Conclusion

The layered architecture provides a clear separation between:

* **Presentation** — CLI
* **Application/business logic** — Services
* **Persistence abstraction** — Repositories
* **Database communication** — JDBC
* **Data persistence and integrity** — PostgreSQL

The main principle is:

> **Each layer should focus on its own responsibility and communicate with the layer below it through clear abstractions.**

This structure makes the Hotel Management System easier to:

* Develop
* Test
* Maintain
* Extend
* Debug
* Refactor

It also provides a foundation for replacing or extending individual components in the future without rewriting the entire application.
