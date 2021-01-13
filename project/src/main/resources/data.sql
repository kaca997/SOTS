INSERT INTO `authorities` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_TEACHER');

INSERT INTO `user` VALUES ('Student',1,'Pera','Peric','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student','12345678'),
('Teacher',2,'Mara','Maric','$2a$10$7xQWygfQbTs5xnMBVZIOrenFVFHySZ5TjH7GuWh8ywYdxYjceGQgK','teacher',NULL),
('Student',3,'Jovan','Jovic','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student1','12345678'),
('Student',4,'Pera','Jovic','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student2','12345678');

INSERT INTO `user_authority` VALUES (1,1),(2,2), (3,1), (4,1);

INSERT INTO `domain` VALUES (1, 'Domen - matematika');
INSERT INTO `knowledge_space` VALUES (1, 'EXPECTED', 1);
INSERT INTO `problem` VALUES (1, 'P1' , 1), (2, 'P2' , 1), (3, 'P3' , 1), (4, 'P4' , 1), (5, 'P5' , 1);
INSERT INTO `relation` VALUES (1, 1 , 2, 1), (2, 1 , 3, 1), (3, 2 , 3, 1), (4, 2 , 4, 1), (5, 2 , 5, 1);

INSERT INTO `course` VALUES (1,'Matematika', 1),(2,'Istorija', null),(3,'Fizika', null),(4,'Hemija', null);

INSERT INTO `student_course` VALUES (1,1),(1,2),(1,3),(3,1),(3,2),(3,3),(4,1),(4,2),(4,3);

INSERT INTO `teacher_course` VALUES (2,1),(2,2),(2,3),(2,4);

INSERT INTO `test` VALUES (1,true,'Test iz mate',1,2),(2,true,'Test iz mate2',1,2);

INSERT INTO `done_test` VALUES (1,1,2),(2,1,1), (3,3,1), (4,1,1), (5,1,1), (6,1,1), (7,1,1), (8,1,1), (9,1,1), (10,1,1) ;

INSERT INTO `section` VALUES (1,'Polinomi',1),(2,'Integrali',1);

INSERT INTO `question` (id, text, problem_id, section_id) VALUES (1,'Koliko je (a+b)^2', 1 ,1),(2,'Koliko je (a+b)^3', 2,1),
(3,'Koliko integral od x', 3,2),(4,'Koliko je integral od x^2', 4,2),(5,'Koliko je integral od x^3', 5,2);

INSERT INTO `answer` VALUES (1,true,'Tacno',1),(2,false,'Netacno1',1),
(3,false,'Netacno',2),(4,false,'Netacno1',2),(5,true,'Tacno tacno tacnooo',2),
(6,true,'Tacno',3),(7,false,'Netacno1',3),
(8,true,'Tacno',4),(9,false,'Netacno1',4),
(10,true,'Tacno',5),(11,false,'Netacno1',5),(12,true,'Tacno1',5),(13,true,'Tacno2',5);

INSERT INTO `chosen_answer` VALUES (1, 2, 2), (2, 4, 2), (3, 11, 2), (4, 7, 2), (5, 9, 2), (6, 10, 2), (7, 12, 2),
(8, 1, 3), (9, 4, 3), (10, 7, 3), (11, 9, 3), (13, 10, 3), (14, 11, 3), (15, 13, 3),
(16, 1, 4), (17, 5, 4), (18, 7, 4), (19, 9, 4), (20, 10, 4), 
(21, 1, 5), (22, 5, 5), (23, 6, 5), (24, 9, 5), (25, 10, 5),
(26, 1, 6), (27, 5, 6), (28, 6, 6), (29, 8, 6), (30, 10, 6),
(31, 1, 7), (32, 5, 7), (33, 6, 7), (34, 9, 7), (35, 10, 7), (36, 12, 7), (37, 13, 7),
(38, 1, 8), (39, 5, 8), (40, 6, 8), (41, 8, 8), (42, 10, 8), (43, 12, 8), (44, 13, 8),
(45, 1, 9), (46, 3, 9), (47, 7, 9), (48, 9, 9), (49, 10, 9),
(50, 1, 10), (51, 5, 10), (52, 7, 10), (53, 8, 10), (54, 10, 10);

--INSERT INTO chosen_answer VALUES (1, 1, 2), (2, 5, 2), (3, 5, 2), (4, 6, 2), (5, 8, 2), (6, 10, 2), (7, 12, 2),
--(8, 1, 3), (9, 5, 3), (10, 7, 3), (11, 8, 3), (13, 11, 3), (14, 12, 3), (15, 13, 3);

--INSERT INTO chosen_answer VALUES (1, 1, 2), (2, 4, 2), (3, 5, 2), (4, 6, 2), (5, 8, 2), (6, 10, 2), (7, 12, 2),
--(8, 1, 3), (9, 5, 3), (10, 7, 3), (11, 9, 3), (13, 11, 3), (14, 12, 3), (15, 13, 3);






