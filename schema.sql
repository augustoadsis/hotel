CREATE TABLE customer
(
    id         BIGINT AUTO_INCREMENT  NOT NULL,
    created_at datetime DEFAULT NOW() NULL,
    updated_at datetime DEFAULT NOW() NULL,
    deleted_at datetime               NULL,
    name       VARCHAR(255)           NULL,
    document   VARCHAR(255)           NULL,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE booking
(
    id          BIGINT AUTO_INCREMENT  NOT NULL,
    created_at  datetime DEFAULT NOW() NULL,
    updated_at  datetime DEFAULT NOW() NULL,
    deleted_at  datetime               NULL,
    check_in    date                   NULL,
    check_out   date                   NULL,
    customer_id BIGINT                 NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

ALTER TABLE booking
    ADD CONSTRAINT uc_booking_checkin UNIQUE (check_in);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);