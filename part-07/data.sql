/*
 * htpasswd -bnBC 10 "" 123456 | tr -d ':\n' | sed 's/$2y/$2a/'
 * $2a$10$JEheCAJiQ2QMniuam7B4XuXFgmjvgzAjrDpnH2mkZ6WCMm6LiMg5O
 *
 * {noop}123456
 * {bcrypt}$2a$10$JEheCAJiQ2QMniuam7B4XuXFgmjvgzAjrDpnH2mkZ6WCMm6LiMg5O
 */
INSERT INTO credentials (email, password)
       VALUES ('admin@example.com', '{noop}abcdef'),
              ('user@example.com', '{noop}123456');
INSERT INTO authorities (email, grants)
       VALUES ('admin@example.com', 'ADMINISTRATOR,USER'),
              ('user@example.com', 'USER');
