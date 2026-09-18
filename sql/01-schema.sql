CREATE TYPE user_role AS ENUM (
    'ADMIN',
    'CLIENT'
);

CREATE TYPE room_type AS ENUM (
    'SINGLE',
    'DOUBLE',
    'SUITE'
);

CREATE TYPE room_status AS ENUM (
    'AVAILABLE',
    'OCCUPIED',
    'MAINTENANCE'
);

CREATE TYPE reservation_status AS ENUM (
    'PENDING',
    'CONFIRMED',
    'CANCELLED',
    'COMPLETED'
);

CREATE TYPE payment_method AS ENUM (
    'CASH',
    'CARD'
);

CREATE TYPE payment_status AS ENUM (
    'PENDING',
    'COMPLETED',
    'REFUNDED',
    'FAILED'
);

CREATE TYPE refund_status AS ENUM (
    'PENDING',
    'COMPLETED',
    'FAILED'
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       salt VARCHAR(255) NOT NULL,
                       role user_role NOT NULL DEFAULT 'CLIENT',
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rooms (
                       id BIGSERIAL PRIMARY KEY,
                       room_number VARCHAR(20) NOT NULL UNIQUE,
                       type room_type NOT NULL,
                       capacity INTEGER NOT NULL,
                       base_price DECIMAL(12, 2) NOT NULL,
                       status room_status NOT NULL DEFAULT 'AVAILABLE',
                       description TEXT
);

CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              room_id BIGINT NOT NULL,
                              check_in DATE NOT NULL,
                              check_out DATE NOT NULL,
                              status reservation_status NOT NULL DEFAULT 'PENDING',
                              total_amount DECIMAL(12, 2) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_reservation_user
                                  FOREIGN KEY (user_id) REFERENCES users(id),

                              CONSTRAINT fk_reservation_room
                                  FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          reservation_id BIGINT NOT NULL,
                          amount DECIMAL(12, 2) NOT NULL,
                          method payment_method NOT NULL,
                          status payment_status NOT NULL DEFAULT 'PENDING',
                          paid_at TIMESTAMP,

                          CONSTRAINT fk_payment_reservation
                              FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE invoices (
                          id BIGSERIAL PRIMARY KEY,
                          payment_id BIGINT NOT NULL UNIQUE,
                          invoice_number VARCHAR(50) NOT NULL UNIQUE,
                          subtotal_ht DECIMAL(12, 2) NOT NULL,
                          tax_amount DECIMAL(12, 2) NOT NULL,
                          total_ttc DECIMAL(12, 2) NOT NULL,
                          issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_invoice_payment
                              FOREIGN KEY (payment_id) REFERENCES payments(id)
);

CREATE TABLE refunds (
                         id BIGSERIAL PRIMARY KEY,
                         payment_id BIGINT NOT NULL,
                         amount DECIMAL(12, 2) NOT NULL,
                         status refund_status NOT NULL DEFAULT 'PENDING',
                         reason VARCHAR(255),
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         processed_at TIMESTAMP,

                         CONSTRAINT fk_refund_payment
                             FOREIGN KEY (payment_id) REFERENCES payments(id)
);