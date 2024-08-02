INSERT INTO customer (id, created, updated, full_name, email, phone, is_active)
VALUES (1, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Ivan Petrov', 'petrov@example.com', '+380501234567', TRUE),
       (2, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Maria Ivanova', 'ivanova@example.com', '+380502345678', TRUE),
       (3, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Oleksandr Kovalenko','oleksandr.kovalenko@example.com', NULL, TRUE),
       (4, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Nataliya Hryhorenko','hryhorenko@example.com', '+380503456789', TRUE),
       (5, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Dmytro Moroz', 'moroz@example.com', '+380504567890', TRUE),
       (6, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Olena Kostenko', 'kostenko@example.com', NULL, TRUE),
       (7, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Yuriy Koval', 'koval@example.com', '+380505678901', TRUE),
       (8, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Irina Sydorenko', 'sydorenko@example.com', '+380506789012', TRUE),
       (9, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Andriy Shevchenko', 'shevchenko@example.com', NULL, TRUE),
       (10, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()), 'Kateryna Levchenko', 'levchenko@example.com', '+380507890123', TRUE);
