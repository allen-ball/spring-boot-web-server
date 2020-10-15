/*
 * htpasswd -bnBC 10 "" 123456 | tr -d ':\n' | sed 's/$2y/$2a/'
 * $2a$10$JEheCAJiQ2QMniuam7B4XuXFgmjvgzAjrDpnH2mkZ6WCMm6LiMg5O
 *
 * {noop}123456
 * {bcrypt}$2a$10$JEheCAJiQ2QMniuam7B4XuXFgmjvgzAjrDpnH2mkZ6WCMm6LiMg5O
 */
INSERT INTO credentials (email, password)
       VALUES ('user@example.com', '{bcrypt}$2a$10$JEheCAJiQ2QMniuam7B4XuXFgmjvgzAjrDpnH2mkZ6WCMm6LiMg5O');
