INSERT INTO `authorities` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_TEACHER');

INSERT INTO `user` VALUES ('Student',1,'Pera','Peric','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student','12345678'),
('Teacher',2,'Mara','Maric','$2a$10$7xQWygfQbTs5xnMBVZIOrenFVFHySZ5TjH7GuWh8ywYdxYjceGQgK','teacher',NULL),
('Student',3,'Jovan','Jovic','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student1','12345678'),
('Student',4,'Pera','Jovic','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student2','12345678');

INSERT INTO `user_authority` VALUES (1,1),(2,2), (3,1), (4,1);

INSERT INTO `domain` VALUES (1, 'Domen - matematika');
INSERT INTO `knowledge_space` VALUES (1, 'EXPECTED', 1);
INSERT INTO `problem` VALUES (1, 'Problem 1' , 1), (2, 'Problem 2' , 1), (3, 'Problem 3' , 1), (4, 'Problem 4' , 1), (5, 'Problem 5' , 1);
INSERT INTO `relation` VALUES (1, 1 , 2, 1), (2, 1 , 3, 1), (3, 2 , 3, 1), (4, 2 , 4, 1), (5, 2 , 5, 1);

INSERT INTO `course` VALUES (1,'Matematika', 1),(2,'Istorija', null),(3,'Fizika', null),(4,'Hemija', null);

INSERT INTO `student_course` VALUES (1,1),(1,2),(1,3),(3,1),(3,2),(3,3),(4,1),(4,2),(4,3);

INSERT INTO `teacher_course` VALUES (2,1),(2,2),(2,3),(2,4);

INSERT INTO `test` VALUES (1,true,'Test iz mate',1,2),(2,true,'Test iz mate2',1,2);

INSERT INTO `done_test` VALUES (1,1,2),(2,1,1), (3,3,1), (4,1,1), (5,1,1) ;

INSERT INTO `section` VALUES (1,'Polinomi',1),(2,'Integrali',1);

INSERT INTO `question` (id, text, problem_id, section_id) VALUES (1,'Koliko je (a+b)^2', 2 ,1),(2,'Koliko je (a+b)^3', 1,1),
(3,'Koliko integral od x', 3,2),(4,'Koliko je integral od x^2', 5,2),(5,'Koliko je integral od x^3', 4,2);

INSERT INTO `answer` VALUES (1,true,'Tacno',1),(2,false,'Netacno1',1),
(3,false,'Netacno',2),(4,false,'Netacno1',2),(5,true,'Tacno tacno tacnooo',2),
(6,true,'Tacno',3),(7,false,'Netacno1',3),
(8,true,'Tacno',4),(9,false,'Netacno1',4),
(10,true,'Tacno',5),(11,false,'Netacno1',5),(12,true,'Tacno1',5),(13,true,'Tacno2',5);

INSERT INTO `chosen_answer` VALUES (1, 1, 2), (2, 4, 2), (3, 11, 2), (4, 7, 2), (5, 9, 2), (6, 10, 2), (7, 12, 2),
(8, 1, 3), (9, 5, 3), (10, 7, 3), (11, 9, 3), (13, 10, 3), (14, 11, 3), (15, 13, 3),
(16, 1, 4), (17, 5, 4), (18, 6, 4), (19, 9, 4), (20, 10, 4), 
(21, 1, 5), (22, 5, 5), (23, 6, 5), (24, 8, 5), (25, 10, 5);

--INSERT INTO chosen_answer VALUES (1, 1, 2), (2, 5, 2), (3, 5, 2), (4, 6, 2), (5, 8, 2), (6, 10, 2), (7, 12, 2),
--(8, 1, 3), (9, 5, 3), (10, 7, 3), (11, 8, 3), (13, 11, 3), (14, 12, 3), (15, 13, 3);

--INSERT INTO chosen_answer VALUES (1, 1, 2), (2, 4, 2), (3, 5, 2), (4, 6, 2), (5, 8, 2), (6, 10, 2), (7, 12, 2),
--(8, 1, 3), (9, 5, 3), (10, 7, 3), (11, 9, 3), (13, 11, 3), (14, 12, 3), (15, 13, 3);






