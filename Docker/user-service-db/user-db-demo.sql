# ************************************************************
# Antares - SQL Client
# Version 0.7.30
# 
# https://antares-sql.app/
# https://github.com/antares-sql/antares
# 
# Host: localhost (MySQL Community Server - GPL 9.1.0)
# Database: userdb
# Generation time: 2025-02-09T14:23:44+01:00
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table admins
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admins`;

CREATE TABLE `admins` (
  `qualification` enum('ADMIN','SUPERADMIN') NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKanhsicqm3lc8ya77tr7r0je18` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;

INSERT INTO `admins` (`qualification`, `id`) VALUES
	("ADMIN", 1),
	("ADMIN", 15);

/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `birthday` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL,
  `status` enum('ACTIVE','BANNED','INACTIVE') NOT NULL,
  `telephone` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `photo_profile` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK2ty1xmrrgtn89xt7kyxx6ta7h` (`nickname`),
  UNIQUE KEY `UK2p58gbqhxvue2igoderm0gh2c` (`telephone`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `birthday`, `email`, `first_name`, `last_name`, `nickname`, `password`, `role`, `status`, `telephone`, `username`, `photo_profile`) VALUES
	(1, "1995-03-14", "amelia-1@example.com", "Amelia", "Rossi", "amy_ro", "$2a$10$ggxsRc4/7Yx5M5BJEQGFk..luPJTbAMyFRh1l/1lYfT1f6dj776n.", "ADMIN", "BANNED", "1234567891", "momo1", 1),
	(2, "1997-07-22", "sofia-2@example.com", "Sofia", "Bianchi", "sofy_bi", "$2a$10$bNgdPhwOzgJkfHq8f39v..pyx566GroFlHIy/oFqxqEJk2O/ZA5Ue", "USER", "ACTIVE", "1234567892", "momo2", 2),
	(3, "1996-12-09", "giulia-3@example.com", "Giulia", "Verdi", "julia_green", "$2a$10$.VpMOE7z7fViDLHainAMu.5FrUpoqwod.f4vwBcBIc.NOmnkTceOC", "USER", "ACTIVE", "1234567893", "momo3", 3),
	(4, "2000-05-04", "alessia-4@example.com", "Alessia", "Conti", "ale_conti", "$2a$10$MxMeiWoTQZZITwv4u0L23u3LtNykAq376pWysDECU4CU1Qj4QXR3.", "USER", "BANNED", "1234567894", "momo4", 4),
	(5, "1998-08-11", "martina-5@example.com", "Martina", "Russo", "marty_ru", "$2a$10$GQ.eThCqscmkx3ijdW06y.QjuV4NbRJqce7af3gniGeYsXwIWBrYe", "USER", "BANNED", "1234567895", "momo5", 5),
	(6, "2001-01-15", "chiara-6@example.com", "Chiara", "Ricci", "chiaricci", "$2a$10$Z20QKh0FpboDd6PLV5atfeTMvIOVPBO/JqEzgpGzGw3A6at4OanGS", "USER", "ACTIVE", "1234567896", "momo6", 6),
	(7, "1999-10-30", "valeria-7@example.com", "Valeria", "Gallo", "val_gallo", "$2a$10$Debs1hWrqMhlBzpFyDBOzO3tLSQ/Tm/444oYjW8fG0lVVuLXf8bvy", "USER", "INACTIVE", "1234567897", "momo7", 7),
	(8, "1995-02-14", "luca-8@example.com", "Luca", "Rossi", "luca_ro", "$2a$10$3LTcXcDw.GkH0WRFRbLENehEpayJfDEWfFrGPNSQh6mr8CQ8niVv2", "USER", "INACTIVE", "1234567898", "momo8", 8),
	(9, "1997-03-25", "marco-9@example.com", "Marco", "Bianchi", "marco_bi", "$2a$10$SYO.Fy5vW1prL/qMTgVOOe2hdEZxKOrwy6fIEXFKiJO6Jyp3wT7lK", "USER", "INACTIVE", "1234567899", "momo9", 9),
	(10, "1996-11-12", "andrea-10@example.com", "Andrea", "Verdi", "andrew_green", "$2a$10$5Ym89JbDM6SNUnUDavJvPOrlKk8f.hEI9Bp5UPHxucJGpZVe3YngC", "USER", "BANNED", "1234567800", "momo10", 11),
	(11, "2000-04-19", "daniele-11@example.com", "Daniele", "Conti", "daniel_c", "$2a$10$dvDzM3B/jScVkIv8yJwkI.h3EiWBXbSO7mtsp8bDmK2Z6NxPqNRwS", "USER", "ACTIVE", "1234567801", "momo11", 11),
	(12, "1998-06-23", "matteo-12@example.com", "Matteo", "Russo", "matte_rus", "$2a$10$2peaKazRxZ.VapYyN6jctuB5csoGkn0tZlPgvAFnUyDZt.DNgqqPa", "USER", "ACTIVE", "1234567802", "momo12", 12),
	(13, "2001-09-09", "giovanni-13@example.com", "Giovanni", "Ricci", "giov_ricci", "$2a$10$TQjdCLAsH7jT0g7fFICXi.T51wgjnuAsi1TWrqBkMmvgyl./tO3Gu", "USER", "ACTIVE", "1234567803", "momo13", 13),
	(14, "1999-05-16", "antonio-14@example.com", "Antonio", "Gallo", "ant_gallo", "$2a$10$YVD73PTa.DvRqltg.GwnAei6BqkTg4FiI2msHe7qzPLyCoaPexRSm", "USER", "ACTIVE", "1234567804", "momo14", 14),
	(15, "1995-07-28", "admin-15@example.com", "Admin", "Adams", "Adminator", "$2a$10$RlRn2bGLZtD.qa.fPFgYleyT3UX7M0fIl64x0/jo0U8yZrPuQq7QW", "ADMIN", "ACTIVE", "1234567805", "admin", 15),
	(16, "1996-01-23", "momone96@example.com", "Mohamed", "Ash", "Avalanche", "$2a$10$wLxGHiJuozwes4bNkViGu.jryoILQDrEqKzuaWnHQxueLM5xGmQsy", "USER", "ACTIVE", "3333352345", "momone", 29),
	(18, "1996-08-01", "youssefgabr@gmail.com", "youssef", "gabr ashour", "YLeon", "$2a$10$Je43W24EDG.cVItqQGb0OeqgJoh8QRCcuc0Tm01yjb8eEIKC.GtDa", "USER", "ACTIVE", "3561246749", "gabrleon", 124);

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of views
# ------------------------------------------------------------

# Creating temporary tables to overcome VIEW dependency errors


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

# Dump completed on 2025-02-09T14:23:44+01:00
