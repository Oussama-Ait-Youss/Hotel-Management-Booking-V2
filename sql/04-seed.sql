-- 1. Nettoyage de la base de données (Permet de relancer le script à l'infini)
TRUNCATE TABLE refunds, invoices, payments, reservations, rooms, users RESTART IDENTITY CASCADE;

-- 2. Insertion des Utilisateurs (1 Admin, 14 Clients)
INSERT INTO users (first_name, last_name, email, password_hash, salt, role) VALUES
                                                                                ('Oussama', 'Ait Youss', 'admin@hotel-almadar.com', 'hashed_admin_pw_8932', 'salt_784', 'ADMIN'),
                                                                                ('Ahmed', 'Benali', 'ahmed.benali@example.com', 'hash_pw_1', 'salt_1', 'CLIENT'),
                                                                                ('Fatima', 'Zahra', 'fatima.z@example.com', 'hash_pw_2', 'salt_2', 'CLIENT'),
                                                                                ('Karim', 'Tazi', 'karim.tazi@example.com', 'hash_pw_3', 'salt_3', 'CLIENT'),
                                                                                ('John', 'Doe', 'john.doe@example.com', 'hash_pw_4', 'salt_4', 'CLIENT'),
                                                                                ('Sarah', 'Connor', 'sarah.c@example.com', 'hash_pw_5', 'salt_5', 'CLIENT'),
                                                                                ('Youssef', 'Alaoui', 'youssef.a@example.com', 'hash_pw_6', 'salt_6', 'CLIENT'),
                                                                                ('Leila', 'Mernissi', 'leila.m@example.com', 'hash_pw_7', 'salt_7', 'CLIENT'),
                                                                                ('Omar', 'Chraibi', 'omar.c@example.com', 'hash_pw_8', 'salt_8', 'CLIENT'),
                                                                                ('Nadia', 'Bennis', 'nadia.b@example.com', 'hash_pw_9', 'salt_9', 'CLIENT'),
                                                                                ('Amine', 'Kabbaj', 'amine.k@example.com', 'hash_pw_10', 'salt_10', 'CLIENT'),
                                                                                ('Rachid', 'El Fassi', 'rachid.e@example.com', 'hash_pw_11', 'salt_11', 'CLIENT'),
                                                                                ('Maria', 'Garcia', 'maria.g@example.com', 'hash_pw_12', 'salt_12', 'CLIENT'),
                                                                                ('Kenza', 'Tahiri', 'kenza.t@example.com', 'hash_pw_13', 'salt_13', 'CLIENT'),
                                                                                ('Hassan', 'Bennani', 'hassan.b@example.com', 'hash_pw_14', 'salt_14', 'CLIENT');

-- 3. Insertion des Chambres (30 Chambres au total avec statuts variés)
INSERT INTO rooms (room_number, type, capacity, base_price, status, description) VALUES
-- Singles (10)
('101', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room, courtyard view'),
('102', 'SINGLE', 1, 400.00, 'OCCUPIED', 'Standard single room, street view'),
('103', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room, quiet zone'),
('104', 'SINGLE', 1, 400.00, 'MAINTENANCE', 'Standard single room (AC repair)'),
('105', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('106', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('107', 'SINGLE', 1, 400.00, 'OCCUPIED', 'Standard single room'),
('108', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('109', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
('110', 'SINGLE', 1, 400.00, 'AVAILABLE', 'Standard single room'),
-- Doubles (15)
('201', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room, balcony'),
('202', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room, sea view'),
('203', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('204', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('205', 'DOUBLE', 2, 650.00, 'MAINTENANCE', 'Standard double room (Plumbing)'),
('206', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('207', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('208', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room'),
('209', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('210', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('211', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('212', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('213', 'DOUBLE', 2, 650.00, 'OCCUPIED', 'Standard double room'),
('214', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
('215', 'DOUBLE', 2, 650.00, 'AVAILABLE', 'Standard double room'),
-- Suites (5)
('301', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Luxury suite with panoramic view'),
('302', 'SUITE', 4, 1200.00, 'OCCUPIED', 'Executive suite with private terrace'),
('303', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Family suite, 2 bedrooms'),
('304', 'SUITE', 4, 1200.00, 'AVAILABLE', 'Luxury suite'),
('305', 'SUITE', 4, 1200.00, 'MAINTENANCE', 'Presidential suite (Painting)');

-- 4. Insertion des Réservations (25 Réservations)
-- Attention : Les check_out sont strictement supérieurs aux check_in
INSERT INTO reservations (user_id, room_id, check_in, check_out, status, total_amount) VALUES
                                                                                           (2, 1, '2026-10-01', '2026-10-05', 'COMPLETED', 1600.00), -- 4 nuits x 400
                                                                                           (3, 11, '2026-10-02', '2026-10-07', 'COMPLETED', 3250.00), -- 5 nuits x 650
                                                                                           (4, 26, '2026-10-10', '2026-10-12', 'COMPLETED', 2400.00), -- 2 nuits x 1200
                                                                                           (5, 2, '2026-10-15', '2026-10-20', 'CANCELLED', 2000.00), -- 5 nuits x 400
                                                                                           (6, 12, '2026-10-18', '2026-10-25', 'CONFIRMED', 4550.00), -- 7 nuits x 650
                                                                                           (7, 3, '2026-10-20', '2026-10-22', 'CONFIRMED', 800.00),
                                                                                           (8, 27, '2026-11-01', '2026-11-05', 'PENDING', 4800.00),
                                                                                           (9, 13, '2026-11-05', '2026-11-10', 'CONFIRMED', 3250.00),
                                                                                           (10, 4, '2026-11-10', '2026-11-15', 'CANCELLED', 2000.00),
                                                                                           (11, 28, '2026-11-12', '2026-11-14', 'COMPLETED', 2400.00),
                                                                                           (12, 14, '2026-11-20', '2026-11-22', 'PENDING', 1300.00),
                                                                                           (13, 5, '2026-12-01', '2026-12-10', 'CONFIRMED', 3600.00),
                                                                                           (14, 15, '2026-12-05', '2026-12-08', 'CONFIRMED', 1950.00),
                                                                                           (15, 29, '2026-12-10', '2026-12-15', 'PENDING', 6000.00),
                                                                                           (2, 16, '2026-12-20', '2026-12-26', 'CONFIRMED', 3900.00),
                                                                                           (3, 6, '2026-12-22', '2026-12-25', 'COMPLETED', 1200.00),
                                                                                           (4, 17, '2026-12-24', '2026-12-28', 'CONFIRMED', 2600.00),
                                                                                           (5, 30, '2026-12-29', '2027-01-02', 'CONFIRMED', 4800.00),
                                                                                           (6, 7, '2027-01-05', '2027-01-10', 'PENDING', 2000.00),
                                                                                           (7, 18, '2027-01-10', '2027-01-15', 'CONFIRMED', 3250.00),
                                                                                           (8, 8, '2027-01-12', '2027-01-14', 'CANCELLED', 800.00),
                                                                                           (9, 19, '2027-01-15', '2027-01-20', 'COMPLETED', 3250.00),
                                                                                           (10, 9, '2027-01-20', '2027-01-25', 'CONFIRMED', 2000.00),
                                                                                           (11, 20, '2027-02-01', '2027-02-05', 'PENDING', 2600.00),
                                                                                           (12, 10, '2027-02-10', '2027-02-15', 'CONFIRMED', 2000.00);

-- 5. Insertion des Paiements (20 Paiements liés aux réservations)
INSERT INTO payments (reservation_id, amount, method, status, paid_at) VALUES
                                                                           (1, 1600.00, 'CARD', 'COMPLETED', '2026-09-30 10:00:00'),
                                                                           (2, 3250.00, 'CARD', 'COMPLETED', '2026-10-01 14:30:00'),
                                                                           (3, 2400.00, 'CASH', 'COMPLETED', '2026-10-09 16:45:00'),
                                                                           (4, 2000.00, 'CARD', 'REFUNDED', '2026-10-05 09:15:00'), -- Réservation annulée plus tard
                                                                           (5, 4550.00, 'CARD', 'COMPLETED', '2026-10-15 11:20:00'),
                                                                           (6, 800.00, 'CASH', 'PENDING', NULL),
                                                                           (8, 3250.00, 'CARD', 'COMPLETED', '2026-10-25 08:30:00'),
                                                                           (9, 2000.00, 'CARD', 'REFUNDED', '2026-10-28 10:00:00'), -- Réservation annulée plus tard
                                                                           (10, 2400.00, 'CARD', 'COMPLETED', '2026-11-10 13:10:00'),
                                                                           (12, 3600.00, 'CARD', 'COMPLETED', '2026-11-15 15:00:00'),
                                                                           (13, 1950.00, 'CASH', 'PENDING', NULL),
                                                                           (15, 3900.00, 'CARD', 'COMPLETED', '2026-11-25 09:45:00'),
                                                                           (16, 1200.00, 'CARD', 'COMPLETED', '2026-12-20 18:20:00'),
                                                                           (17, 2600.00, 'CARD', 'COMPLETED', '2026-12-15 11:11:00'),
                                                                           (18, 4800.00, 'CARD', 'COMPLETED', '2026-12-20 14:05:00'),
                                                                           (20, 3250.00, 'CARD', 'COMPLETED', '2027-01-05 10:30:00'),
                                                                           (21, 800.00, 'CARD', 'REFUNDED', '2027-01-02 12:00:00'), -- Réservation annulée plus tard
                                                                           (22, 3250.00, 'CARD', 'COMPLETED', '2027-01-10 16:40:00'),
                                                                           (23, 2000.00, 'CASH', 'PENDING', NULL),
                                                                           (25, 2000.00, 'CARD', 'COMPLETED', '2027-02-05 09:10:00');

-- 6. Insertion des Factures (Uniquement pour les paiements COMPLETED : ids 1, 2, 3, 5, 7, 9, 10, 12, 13, 14, 15, 16, 18, 20)
-- Calcul basé sur 20% de TVA (TTC = HT + TAX). Ex: TTC 1600 = HT 1333.33 + TAX 266.67
INSERT INTO invoices (payment_id, invoice_number, subtotal_ht, tax_amount, total_ttc) VALUES
                                                                                          (1, 'INV-2026-0001', 1333.33, 266.67, 1600.00),
                                                                                          (2, 'INV-2026-0002', 2708.33, 541.67, 3250.00),
                                                                                          (3, 'INV-2026-0003', 2000.00, 400.00, 2400.00),
                                                                                          (5, 'INV-2026-0004', 3791.67, 758.33, 4550.00),
                                                                                          (7, 'INV-2026-0005', 2708.33, 541.67, 3250.00),
                                                                                          (9, 'INV-2026-0006', 2000.00, 400.00, 2400.00),
                                                                                          (10, 'INV-2026-0007', 3000.00, 600.00, 3600.00),
                                                                                          (12, 'INV-2026-0008', 3250.00, 650.00, 3900.00),
                                                                                          (13, 'INV-2026-0009', 1000.00, 200.00, 1200.00),
                                                                                          (14, 'INV-2026-0010', 2166.67, 433.33, 2600.00),
                                                                                          (15, 'INV-2026-0011', 4000.00, 800.00, 4800.00),
                                                                                          (16, 'INV-2027-0001', 2708.33, 541.67, 3250.00),
                                                                                          (18, 'INV-2027-0002', 2708.33, 541.67, 3250.00),
                                                                                          (20, 'INV-2027-0003', 1666.67, 333.33, 2000.00);

-- 7. Insertion des Remboursements (Pour les paiements en statut REFUNDED : ids 4, 8, 17)
INSERT INTO refunds (payment_id, amount, status, reason, processed_at) VALUES
                                                                           (4, 2000.00, 'COMPLETED', 'Annulation par le client 48h avant', '2026-10-06 14:00:00'),
                                                                           (8, 2000.00, 'COMPLETED', 'Erreur de date lors de la réservation', '2026-10-29 11:30:00'),
                                                                           (17, 800.00, 'PENDING', 'Demande d annulation en cours de traitement', NULL);