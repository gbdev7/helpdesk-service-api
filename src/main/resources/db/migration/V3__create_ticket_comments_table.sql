CREATE TABLE ticket_comments (
                                 id BIGSERIAL PRIMARY KEY,
                                 content TEXT NOT NULL,
                                 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 ticket_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,

                                 CONSTRAINT fk_comments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES tb_users(id)
);