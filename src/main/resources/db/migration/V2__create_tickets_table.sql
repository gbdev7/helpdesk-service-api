CREATE TABLE tickets (
                         id BIGSERIAL PRIMARY KEY,
                         title VARCHAR(150) NOT NULL,
                         description TEXT NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         priority VARCHAR(20) NOT NULL,
                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITHOUT TIME ZONE,
                         sla_due_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         customer_id BIGINT NOT NULL,
                         technician_id BIGINT,

                         CONSTRAINT fk_tickets_customer FOREIGN KEY (customer_id) REFERENCES tb_users(id),
                         CONSTRAINT fk_tickets_technician FOREIGN KEY (technician_id) REFERENCES tb_users(id)
);