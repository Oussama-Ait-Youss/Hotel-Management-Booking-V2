TRUNCATE TABLE refunds, invoices, payments, reservations, rooms, users RESTART IDENTITY CASCADE;

-- ---------------------------------------------------------
-- 1. USERS (1 Admin + 19 Explicit Clients + 80 Generated Clients)
-- Password for all clients: Client@123
-- ---------------------------------------------------------
INSERT INTO users (first_name, last_name, email, password_hash, salt, role) VALUES
                                                                                ('Oussama', 'Ait Youss', 'admin@hotel-almadar.com', 'tJ97zwT93G3IQUAcNrvg9gub91Me+Di5AfuII5OuHm0=', 'szS0/jjLP8h3kF9nj/JX0g==', 'ADMIN'),
                                                                                ('Ahmed', 'Benali', 'ahmed.benali@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Fatima', 'Zahra', 'fatima.z@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Karim', 'Tazi', 'karim.tazi@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('John', 'Doe', 'john.doe@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Sarah', 'Connor', 'sarah.c@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Youssef', 'Alaoui', 'youssef.a@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Leila', 'Mernissi', 'leila.m@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Omar', 'Chraibi', 'omar.c@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Nadia', 'Bennis', 'nadia.b@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Amine', 'Kabbaj', 'amine.k@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Rachid', 'El Fassi', 'rachid.e@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Maria', 'Garcia', 'maria.g@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Kenza', 'Tahiri', 'kenza.t@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Hassan', 'Bennani', 'hassan.b@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Amina', 'Rami', 'amina.r@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Tarik', 'Mansour', 'tarik.m@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Salma', 'Idrissi', 'salma.i@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Khalid', 'Amrani', 'khalid.a@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT'),
                                                                                ('Lina', 'Oufkir', 'lina.o@example.com', 'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=', 'MU6Y6sd4nsOtHZEibiXL0A==', 'CLIENT');

-- Generate exactly 80 more users to reach 100 total
INSERT INTO users (first_name, last_name, email, password_hash, salt, role)
SELECT
    'Client' || gs,
    'Demo' || gs,
    'client' || gs || '@example.com',
    'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=',
    'MU6Y6sd4nsOtHZEibiXL0A==',
    'CLIENT'
FROM generate_series(21, 100) AS gs;


-- ---------------------------------------------------------
-- 2. ROOMS (50 Rooms: 15 Single, 25 Double, 10 Suite)
-- ---------------------------------------------------------
INSERT INTO rooms (room_number, type, capacity, base_price, status, description) VALUES
-- Singles (1-15)
('101', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room, ground floor'),
('102', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room, courtyard view'),
('103', 'SINGLE', 1, 400.00, 'OCCUPIED', 'Standard single room, street view'),
('104', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('105', 'SINGLE', 1, 400.00, 'MAINTENANCE', 'Standard single room (AC repair)'),
('106', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('107', 'SINGLE', 1, 400.00, 'OCCUPIED', 'Standard single room'),
('108', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('109', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('110', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('111', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('112', 'SINGLE', 1, 400.00, 'OCCUPIED', 'Standard single room'),
('113', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('114', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('115', 'SINGLE', 1, 400.00, 'MAINTENANCE', 'Standard single room (Painting)'),
-- Doubles (16-40)
('201', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room, balcony'),
('202', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room, sea view'),
('203', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('204', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('205', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('206', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('207', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room'),
('208', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('209', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('210', 'DOUBLE', 2, 650.00, 'MAINTENANCE', 'Standard double room (Plumbing)'),
('211', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('212', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('213', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room'),
('214', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('215', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('216', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room, pool view'),
('217', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('218', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room'),
('219', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('220', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('221', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('222', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('223', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room'),
('224', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('225', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
-- Suites (41-50)
('301', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Luxury suite with panoramic view'),
('302', 'SUITE', 4, 1200.00, 'OCCUPIED', 'Executive suite with private terrace'),
('303', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Family suite, 2 bedrooms'),
('304', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Luxury suite'),
('305', 'SUITE', 4, 1200.00, 'MAINTENANCE', 'Presidential suite (Painting)'),
('306', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Suite with jacuzzi'),
('307', 'SUITE', 4, 1200.00, 'OCCUPIED', 'Corner suite'),
('308', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Business suite'),
('309', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Honeymoon suite'),
('310', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Penthouse suite');


-- ---------------------------------------------------------
-- 3. RESERVATIONS (40 Reservations with varied logic)
-- COMPLETED (1-10), CONFIRMED (11-20), PENDING (21-30), CANCELLED (31-40)
-- ---------------------------------------------------------
INSERT INTO reservations (user_id, room_id, check_in, check_out, status, total_amount) VALUES
-- COMPLETED (Past stays)
(2, 1, '2026-05-01', '2026-05-04', 'COMPLETED', 1200.00), -- 3 nights * 400
(3, 16, '2026-06-10', '2026-06-15', 'COMPLETED', 3250.00), -- 5 nights * 650
(4, 41, '2026-07-01', '2026-07-03', 'COMPLETED', 2400.00), -- 2 nights * 1200
(5, 2, '2026-07-15', '2026-07-20', 'COMPLETED', 2000.00), -- 5 nights * 400
(6, 17, '2026-08-01', '2026-08-10', 'COMPLETED', 5850.00), -- 9 nights * 650
(7, 42, '2026-08-15', '2026-08-18', 'COMPLETED', 3600.00), -- 3 nights * 1200
(8, 3, '2026-08-20', '2026-08-22', 'COMPLETED', 800.00),  -- 2 nights * 400
(9, 18, '2026-09-01', '2026-09-05', 'COMPLETED', 2600.00), -- 4 nights * 650
(10, 4, '2026-09-10', '2026-09-15', 'COMPLETED', 2000.00), -- 5 nights * 400
(11, 43, '2026-09-20', '2026-09-25', 'COMPLETED', 6000.00), -- 5 nights * 1200

-- CONFIRMED (Upcoming / Ongoing stays)
(12, 19, '2026-10-05', '2026-10-10', 'CONFIRMED', 3250.00),
(13, 5, '2026-10-12', '2026-10-15', 'CONFIRMED', 1200.00),
(14, 20, '2026-10-18', '2026-10-25', 'CONFIRMED', 4550.00),
(15, 44, '2026-11-01', '2026-11-05', 'CONFIRMED', 4800.00),
(16, 6, '2026-11-10', '2026-11-15', 'CONFIRMED', 2000.00),
(17, 21, '2026-11-20', '2026-11-22', 'CONFIRMED', 1300.00),
(18, 45, '2026-12-01', '2026-12-07', 'CONFIRMED', 7200.00),
(19, 7, '2026-12-10', '2026-12-15', 'CONFIRMED', 2000.00),
(20, 22, '2026-12-20', '2026-12-26', 'CONFIRMED', 3900.00),
(21, 46, '2026-12-24', '2026-12-28', 'CONFIRMED', 4800.00),

-- PENDING (Awaiting payment)
(22, 8, '2026-11-01', '2026-11-05', 'PENDING', 1600.00),
(23, 23, '2026-11-10', '2026-11-15', 'PENDING', 3250.00),
(24, 47, '2026-11-20', '2026-11-22', 'PENDING', 2400.00),
(25, 9, '2026-12-01', '2026-12-10', 'PENDING', 3600.00),
(26, 24, '2026-12-05', '2026-12-08', 'PENDING', 1950.00),
(27, 48, '2026-12-10', '2026-12-15', 'PENDING', 6000.00),
(28, 10, '2027-01-05', '2027-01-10', 'PENDING', 2000.00),
(29, 25, '2027-01-12', '2027-01-14', 'PENDING', 1300.00),
(30, 49, '2027-01-20', '2027-01-25', 'PENDING', 6000.00),
(31, 11, '2027-02-01', '2027-02-05', 'PENDING', 1600.00),

-- CANCELLED
(32, 26, '2026-09-15', '2026-09-20', 'CANCELLED', 3250.00),
(33, 50, '2026-10-01', '2026-10-05', 'CANCELLED', 4800.00),
(34, 12, '2026-10-10', '2026-10-12', 'CANCELLED', 800.00),
(35, 27, '2026-10-15', '2026-10-20', 'CANCELLED', 3250.00),
(36, 13, '2026-11-01', '2026-11-05', 'CANCELLED', 1600.00),
(37, 28, '2026-11-10', '2026-11-15', 'CANCELLED', 3250.00),
(38, 14, '2026-11-20', '2026-11-22', 'CANCELLED', 800.00),
(39, 29, '2026-12-01', '2026-12-10', 'CANCELLED', 5850.00),
(40, 15, '2026-12-24', '2026-12-28', 'CANCELLED', 1600.00),
(41, 30, '2027-01-10', '2027-01-15', 'CANCELLED', 3250.00);


-- ---------------------------------------------------------
-- 4. PAYMENTS (30 Payments linking to reservations)
-- 1-20 (COMPLETED), 31-40 (REFUNDED due to cancellation)
-- ---------------------------------------------------------
INSERT INTO payments (reservation_id, amount, method, status, paid_at) VALUES
-- Payments for COMPLETED / CONFIRMED reservations
(1, 1200.00, 'CARD', 'COMPLETED', '2026-04-28 10:00:00'),
(2, 3250.00, 'CARD', 'COMPLETED', '2026-06-05 14:30:00'),
(3, 2400.00, 'CASH', 'COMPLETED', '2026-07-01 16:45:00'),
(4, 2000.00, 'CARD', 'COMPLETED', '2026-07-10 09:15:00'),
(5, 5850.00, 'CARD', 'COMPLETED', '2026-07-28 11:20:00'),
(6, 3600.00, 'CARD', 'COMPLETED', '2026-08-10 14:00:00'),
(7, 800.00, 'CASH', 'COMPLETED', '2026-08-20 18:30:00'),
(8, 2600.00, 'CARD', 'COMPLETED', '2026-08-25 10:00:00'),
(9, 2000.00, 'CARD', 'COMPLETED', '2026-09-05 13:10:00'),
(10, 6000.00, 'CARD', 'COMPLETED', '2026-09-15 15:00:00'),
(11, 3250.00, 'CARD', 'COMPLETED', '2026-09-28 09:45:00'),
(12, 1200.00, 'CARD', 'COMPLETED', '2026-10-05 18:20:00'),
(13, 4550.00, 'CARD', 'COMPLETED', '2026-10-10 11:11:00'),
(14, 4800.00, 'CARD', 'COMPLETED', '2026-10-25 14:05:00'),
(15, 2000.00, 'CARD', 'COMPLETED', '2026-11-05 10:30:00'),
(16, 1300.00, 'CASH', 'COMPLETED', '2026-11-20 12:00:00'),
(17, 7200.00, 'CARD', 'COMPLETED', '2026-11-25 16:40:00'),
(18, 2000.00, 'CARD', 'COMPLETED', '2026-12-05 09:10:00'),
(19, 3900.00, 'CARD', 'COMPLETED', '2026-12-15 14:20:00'),
(20, 4800.00, 'CARD', 'COMPLETED', '2026-12-20 10:00:00'),

-- Payments that were made but later REFUNDED (for CANCELLED reservations 31-40)
(31, 3250.00, 'CARD', 'REFUNDED', '2026-09-01 10:00:00'),
(32, 4800.00, 'CARD', 'REFUNDED', '2026-09-15 14:30:00'),
(33, 800.00,  'CARD', 'REFUNDED', '2026-09-20 16:45:00'),
(34, 3250.00, 'CARD', 'REFUNDED', '2026-09-25 09:15:00'),
(35, 1600.00, 'CARD', 'REFUNDED', '2026-10-15 11:20:00'),
(36, 3250.00, 'CARD', 'REFUNDED', '2026-10-20 14:00:00'),
(37, 800.00,  'CARD', 'REFUNDED', '2026-11-01 18:30:00'),
(38, 5850.00, 'CARD', 'REFUNDED', '2026-11-10 10:00:00'),
(39, 1600.00, 'CARD', 'REFUNDED', '2026-11-20 13:10:00'),
(40, 3250.00, 'CARD', 'REFUNDED', '2026-12-05 15:00:00');


-- ---------------------------------------------------------
-- 5. INVOICES (Only for COMPLETED payments 1-20)
-- Subtotal HT = Total / 1.20 | Tax = Total - Subtotal
-- ---------------------------------------------------------
INSERT INTO invoices (payment_id, invoice_number, subtotal_ht, tax_amount, total_ttc, issued_at) VALUES
                                                                                                     (1, 'INV-2026-0001', 1000.00, 200.00, 1200.00, '2026-04-28 10:05:00'),
                                                                                                     (2, 'INV-2026-0002', 2708.33, 541.67, 3250.00, '2026-06-05 14:35:00'),
                                                                                                     (3, 'INV-2026-0003', 2000.00, 400.00, 2400.00, '2026-07-01 16:50:00'),
                                                                                                     (4, 'INV-2026-0004', 1666.67, 333.33, 2000.00, '2026-07-10 09:20:00'),
                                                                                                     (5, 'INV-2026-0005', 4875.00, 975.00, 5850.00, '2026-07-28 11:25:00'),
                                                                                                     (6, 'INV-2026-0006', 3000.00, 600.00, 3600.00, '2026-08-10 14:05:00'),
                                                                                                     (7, 'INV-2026-0007', 666.67,  133.33, 800.00,  '2026-08-20 18:35:00'),
                                                                                                     (8, 'INV-2026-0008', 2166.67, 433.33, 2600.00, '2026-08-25 10:05:00'),
                                                                                                     (9, 'INV-2026-0009', 1666.67, 333.33, 2000.00, '2026-09-05 13:15:00'),
                                                                                                     (10, 'INV-2026-0010', 5000.00, 1000.00, 6000.00, '2026-09-15 15:05:00'),
                                                                                                     (11, 'INV-2026-0011', 2708.33, 541.67, 3250.00, '2026-09-28 09:50:00'),
                                                                                                     (12, 'INV-2026-0012', 1000.00, 200.00, 1200.00, '2026-10-05 18:25:00'),
                                                                                                     (13, 'INV-2026-0013', 3791.67, 758.33, 4550.00, '2026-10-10 11:16:00'),
                                                                                                     (14, 'INV-2026-0014', 4000.00, 800.00, 4800.00, '2026-10-25 14:10:00'),
                                                                                                     (15, 'INV-2026-0015', 1666.67, 333.33, 2000.00, '2026-11-05 10:35:00'),
                                                                                                     (16, 'INV-2026-0016', 1083.33, 216.67, 1300.00, '2026-11-20 12:05:00'),
                                                                                                     (17, 'INV-2026-0017', 6000.00, 1200.00, 7200.00, '2026-11-25 16:45:00'),
                                                                                                     (18, 'INV-2026-0018', 1666.67, 333.33, 2000.00, '2026-12-05 09:15:00'),
                                                                                                     (19, 'INV-2026-0019', 3250.00, 650.00, 3900.00, '2026-12-15 14:25:00'),
                                                                                                     (20, 'INV-2026-0020', 4000.00, 800.00, 4800.00, '2026-12-20 10:05:00');


-- ---------------------------------------------------------
-- 6. REFUNDS (For REFUNDED payments 21-30)
-- ---------------------------------------------------------
INSERT INTO refunds (payment_id, amount, status, reason, processed_at) VALUES
                                                                           (21, 3250.00, 'COMPLETED', 'Client cancellation (>14 days)', '2026-09-03 12:00:00'),
                                                                           (22, 4800.00, 'COMPLETED', 'Client cancellation (>14 days)', '2026-09-17 10:30:00'),
                                                                           (23, 400.00,  'COMPLETED', 'Late cancellation penalty 50%', '2026-10-09 14:15:00'),
                                                                           (24, 2275.00, 'COMPLETED', 'Cancellation penalty 30%', '2026-10-08 09:00:00'),
                                                                           (25, 1600.00, 'COMPLETED', 'Client cancellation (>14 days)', '2026-10-18 11:45:00'),
                                                                           (26, 3250.00, 'PENDING',   'Awaiting processing', NULL),
                                                                           (27, 800.00,  'COMPLETED', 'Client cancellation (>14 days)', '2026-11-05 16:20:00'),
                                                                           (28, 5850.00, 'COMPLETED', 'Client cancellation (>14 days)', '2026-11-15 08:50:00'),
                                                                           (29, 0.00,    'FAILED',    'No refund policy (<48 hours)', '2026-12-23 15:30:00'),
                                                                           (30, 3250.00, 'PENDING',   'Awaiting processing', NULL);