INSERT IGNORE INTO customer (id, name, document) VALUES (1, 'Augusto Gonçalves', '111.111-111');
INSERT IGNORE INTO customer (id, name, document) VALUES (2, 'Pedro Silva', '123123-1');
INSERT IGNORE INTO customer (id, name, document) VALUES (3, 'Maria José', '222.222.222-22');
INSERT IGNORE INTO customer (id, name, document) VALUES (4, 'Sara Gomes', '1.222.33-1');

INSERT IGNORE INTO booking (id, check_in, check_out, customer_id) VALUES (1, '2022-05-20', '2022-05-20', 1);
INSERT IGNORE INTO booking (id, check_in, check_out, customer_id) VALUES (2, '2022-05-21', '2022-05-23', 2);
INSERT IGNORE INTO booking (id, check_in, check_out, customer_id) VALUES (3, '2022-05-24', '2022-05-25', 3);