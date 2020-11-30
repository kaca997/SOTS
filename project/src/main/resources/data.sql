INSERT INTO `authorities` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_TEACHER');

INSERT INTO `course` VALUES (1, "Matematika"), (2, "Istorija");

INSERT INTO `user` VALUES ('Student',1,'Pera','Peric','$2a$10$ygBCftE2h6/2D0Eo7zsNGumJuFgcIJeXSK3lVBYoTmLTFm4hwqUAW','student','12345678'),
('Teacher',2,'Mara','Maric','$2a$10$7xQWygfQbTs5xnMBVZIOrenFVFHySZ5TjH7GuWh8ywYdxYjceGQgK','teacher',NULL);

INSERT INTO `user_authority` VALUES (1,1),(2,2);

INSERT INTO `teacher_course` VALUES (2, 1);

