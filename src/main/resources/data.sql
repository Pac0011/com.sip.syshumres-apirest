INSERT INTO dinning_room(id, description, price, created) VALUES (1, 'Paquete 1', 45.00, now());
INSERT INTO dinning_room(id, description, price, created) VALUES (2,'Paquete 2', 55.00, now());
INSERT INTO dinning_room(id, description, price, created) VALUES (3,'Paquete Ejecutivo', 80.00, now());
INSERT INTO addresses_states(id, description, created) VALUES (1,'Aguascalientes', now());
INSERT INTO addresses_states(id, description, created) VALUES (2,'Baja California', now());
INSERT INTO addresses_states(id, description, created) VALUES (3,'Baja California Sur', now());
INSERT INTO addresses_states(id, description, created) VALUES (4,'Campeche', now());
INSERT INTO addresses_states(id, description, created) VALUES (5,'Coahuila', now());
INSERT INTO addresses_states(id, description, created) VALUES (6,'Colima', now());
INSERT INTO addresses_states(id, description, created) VALUES (7,'Chiapas', now());
INSERT INTO addresses_states(id, description, created) VALUES (8,'Chihuahua', now());
INSERT INTO addresses_states(id, description, created) VALUES (9,'CDMX', now());
INSERT INTO addresses_states(id, description, created) VALUES (10,'Durango', now());
INSERT INTO addresses_states(id, description, created) VALUES (11,'Guanajuato', now());
INSERT INTO addresses_states(id, description, created) VALUES (12,'Guerrero', now());
INSERT INTO addresses_states(id, description, created) VALUES (13,'Hidalgo', now());
INSERT INTO addresses_states(id, description, created) VALUES (14,'Jalisco', now());
INSERT INTO addresses_states(id, description, created) VALUES (15,'Estado de México', now());
INSERT INTO addresses_states(id, description, created) VALUES (16,'Michoacán', now());
INSERT INTO addresses_states(id, description, created) VALUES (17,'Morelos', now());
INSERT INTO addresses_states(id, description, created) VALUES (18,'Nayarit', now());
INSERT INTO addresses_states(id, description, created) VALUES (19,'Nuevo León', now());
INSERT INTO addresses_states(id, description, created) VALUES (20,'Oaxaca', now());
INSERT INTO addresses_states(id, description, created) VALUES (21,'Puebla', now());
INSERT INTO addresses_states(id, description, created) VALUES (22,'Querétaro', now());
INSERT INTO addresses_states(id, description, created) VALUES (23,'Quintana Roo', now());
INSERT INTO addresses_states(id, description, created) VALUES (24,'San Luis Potosí', now());
INSERT INTO addresses_states(id, description, created) VALUES (25,'Sinaloa', now());
INSERT INTO addresses_states(id, description, created) VALUES (26,'Sonora', now());
INSERT INTO addresses_states(id, description, created) VALUES (27,'Tabasco', now());
INSERT INTO addresses_states(id, description, created) VALUES (28,'Tamaulipas', now());
INSERT INTO addresses_states(id, description, created) VALUES (29,'Tlaxcala', now());
INSERT INTO addresses_states(id, description, created) VALUES (30,'Veracruz', now());
INSERT INTO addresses_states(id, description, created) VALUES (31,'Yucatán', now());
INSERT INTO addresses_states(id, description, created) VALUES (32,'Zacatecas', now());
INSERT INTO branch_offices_types(id, description, created) VALUES (1, 'SUC', now());
INSERT INTO branch_offices_types(id, description, created) VALUES (2, 'OOP', now());
INSERT INTO regions(id, description, created) VALUES (1, 'Centro', now());
INSERT INTO regions(id, description, created) VALUES (2, 'Norte', now());
INSERT INTO regions(id, description, created) VALUES (3, 'Sur', now());
INSERT INTO branch_offices(id, description, branch_office_type_id, created) VALUES (1, 'Corporativo', 1, now());
INSERT INTO branch_offices(id, description, branch_office_type_id, created) VALUES (2, 'Cancun', 1, now());
INSERT INTO managing_companies(id, description, created) VALUES (1,'Empresa sa de cv', now());
INSERT INTO managing_companies(id, description, created) VALUES (2,'Empresa Outsourcing 1', now());
INSERT INTO type_hiring(id, description, created) VALUES (1,'Empresa', now());
INSERT INTO type_hiring(id, description, created) VALUES (2,'Outsourcing', now());
INSERT INTO blood_type(id, description) VALUES (1, 'A+');
INSERT INTO blood_type(id, description) VALUES (2, 'B+');
INSERT INTO blood_type(id, description) VALUES (3, 'AB+');
INSERT INTO blood_type(id, description) VALUES (4, 'O+');
INSERT INTO blood_type(id, description) VALUES (5, 'A-');
INSERT INTO blood_type(id, description) VALUES (6, 'B-');
INSERT INTO blood_type(id, description) VALUES (7, 'AB-');
INSERT INTO blood_type(id, description) VALUES (8, 'O-');