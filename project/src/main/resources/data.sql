INSERT INTO `authorities` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_TEACHER');

INSERT INTO `user` VALUES ('Student',1,'Pera','Peric','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student','12345678'),
('Teacher',2,'Mara','Maric','$2a$10$7xQWygfQbTs5xnMBVZIOrenFVFHySZ5TjH7GuWh8ywYdxYjceGQgK','teacher',NULL);

INSERT INTO `user_authority` VALUES (1,1),(2,2);

INSERT INTO `course` VALUES (1,'Matematika'),(2,'Istorija'),(3,'Fizika'),(4,'Hemija');

INSERT INTO `student_course` VALUES (1,1),(1,2),(1,3);

INSERT INTO `teacher_course` VALUES (2,1),(2,2),(2,3),(2,4);

INSERT INTO `test` VALUES (1,true,'Test iz mate',1,2),(2,true,'Test iz mate2',1,2);

INSERT INTO `done_test` VALUES (1,1,2);

INSERT INTO `section` VALUES (1,'Polinomi',1),(2,'Integrali',1);

INSERT INTO `question` VALUES (1,'Koliko je (a+b)^2',1),(2,'Koliko je (a+b)^3',1),(3,'Koliko integral od x',2),(4,'Koliko je integral od x^2',2),(5,'Koliko je integral od x^3',2);

INSERT INTO `answer` VALUES (1,true,'Tacno',1),(2,false,'Netacno1',1),
(3,false,'Netacno',2),(4,false,'Netacno1',2),(5,true,'Tacno tacno tacnooo',2),
(6,true,'Tacno',3),(7,false,'Netacno1',3),
(8,true,'Tacno',4),(9,false,'Netacno1',4),
(10,true,'Tacno',5),(11,false,'Netacno1',5),(12,true,'Tacno1',5),(13,true,'Tacno2',5);

