CREATE INDEX idx_reservations_user_id
    ON reservations(user_id);

CREATE INDEX idx_reservations_room_id
    ON reservations(room_id);

CREATE INDEX idx_reservations_dates
    ON reservations(check_in, check_out);

CREATE INDEX idx_reservations_status
    ON reservations(status);

CREATE INDEX idx_payments_reservation_id
    ON payments(reservation_id);

CREATE INDEX idx_refunds_payment_id
    ON refunds(payment_id);

CREATE INDEX idx_refunds_status
    ON refunds(status);