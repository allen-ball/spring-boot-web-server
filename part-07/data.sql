INSERT INTO credentials (email, password)
       VALUES ('admin@example.com', '{noop}abcdef'),
              ('user@example.com', '{noop}123456');
INSERT INTO authorities (email, grants)
       VALUES ('admin@example.com', 'ADMINISTRATOR,USER'),
              ('user@example.com', 'USER');
