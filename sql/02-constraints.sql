ALTER TABLE rooms
    ADD CONSTRAINT check_room_capacity
        CHECK (capacity > 0);

ALTER TABLE rooms
    ADD CONSTRAINT check_room_base_price
        CHECK (base_price >= 0);

ALTER TABLE reservations
    ADD CONSTRAINT check_reservation_dates
        CHECK (check_out > check_in);

ALTER TABLE reservations
    ADD CONSTRAINT check_reservation_total
        CHECK (total_amount >= 0);

ALTER TABLE payments
    ADD CONSTRAINT check_payment_amount
        CHECK (amount >= 0);

ALTER TABLE refunds
    ADD CONSTRAINT check_refund_amount
        CHECK (amount > 0);