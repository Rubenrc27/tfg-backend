CREATE DATABASE  IF NOT EXISTS `encuesta_app_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `encuesta_app_db`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: encuesta_app_db
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_index` int DEFAULT NULL,
  `option_text` varchar(255) NOT NULL,
  `question_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5bmv46so2y5igt9o9n9w4fh6y` (`question_id`),
  CONSTRAINT `FK5bmv46so2y5igt9o9n9w4fh6y` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
INSERT INTO `options` VALUES (5,1,'A veces / no mucho',2),(6,2,'si',2),(7,3,'muchisimo',2),(8,4,'Es un pesao',2),(11,1,'No lo conozco pero seguro que si',5),(12,2,'100% No',5),(13,3,'Claro',5),(14,4,'Luis y Andrés son la misma persona',5),(15,1,'Java',1),(16,2,'Python',1),(17,1,'1',7),(18,2,'2',7),(19,3,'3',7),(20,4,'4',7),(21,5,'5',7);
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_index` int DEFAULT NULL,
  `question_text` varchar(255) NOT NULL,
  `question_type` enum('MULTIPLE','OPEN','SINGLE') NOT NULL,
  `survey_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnf38uiy78c0g0tmo68btk3y0p` (`survey_id`),
  CONSTRAINT `FKnf38uiy78c0g0tmo68btk3y0p` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,1,'¿Lenguaje favorito?','SINGLE',1),(2,1,'¿Te cae bien Andrés?','SINGLE',2),(5,2,'¿Te cae mejor Luis?','SINGLE',2),(6,3,'Di quien es tu profesor favorito y la razón','OPEN',2),(7,1,'¿Como valoras el aprendizaje en Desarrollo de Aplicaciones?','SINGLE',4);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `responses`
--

DROP TABLE IF EXISTS `responses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `responses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `responded_at` datetime(6) DEFAULT NULL,
  `response_text` text,
  `question_id` bigint NOT NULL,
  `selected_option_id` bigint DEFAULT NULL,
  `survey_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrln3foyudvklybbdl1r5dmjvy` (`question_id`),
  KEY `FKod6laf0uvo4yrodaffgw94npl` (`selected_option_id`),
  KEY `FKemug20qu5fygiy69wj7sel8hc` (`survey_id`),
  KEY `FKqf8rt9h0wd5pmaxouhxqsoeuq` (`user_id`),
  CONSTRAINT `FKemug20qu5fygiy69wj7sel8hc` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`),
  CONSTRAINT `FKod6laf0uvo4yrodaffgw94npl` FOREIGN KEY (`selected_option_id`) REFERENCES `options` (`id`),
  CONSTRAINT `FKqf8rt9h0wd5pmaxouhxqsoeuq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrln3foyudvklybbdl1r5dmjvy` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `responses`
--

LOCK TABLES `responses` WRITE;
/*!40000 ALTER TABLE `responses` DISABLE KEYS */;
INSERT INTO `responses` VALUES (1,'2026-01-27 16:26:02.028080',NULL,2,6,2,NULL),(4,'2026-01-27 16:49:29.251686',NULL,2,6,2,NULL),(5,'2026-01-27 16:49:29.259609',NULL,5,12,2,NULL),(6,'2026-01-27 16:49:29.265609','David Corbaton',6,NULL,2,NULL),(7,'2026-01-28 19:31:37.456375',NULL,2,8,2,NULL),(8,'2026-01-28 19:31:37.620101',NULL,5,14,2,NULL),(9,'2026-01-28 19:31:37.638100','lamine',6,NULL,2,NULL);
/*!40000 ALTER TABLE `responses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveys`
--

DROP TABLE IF EXISTS `surveys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveys` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text,
  `duck_avatar` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveys`
--

LOCK TABLES `surveys` WRITE;
/*!40000 ALTER TABLE `surveys` DISABLE KEYS */;
INSERT INTO `surveys` VALUES (1,'2026-01-19 18:45:03.000000','¿Qué tipo de programador eres?',NULL,_binary '','Encuesta Dev 2026 ?'),(2,'2026-01-19 18:03:35.765048','trata de como valoramos a nuestros profesores','CLASSIC',_binary '','Valoración de Profes'),(4,'2026-02-02 15:09:55.986658','Saber como valoran los alumnos las asignaturas que se imparten en clase','CLASSIC',_binary '','Valoración de Asignaturas');
/*!40000 ALTER TABLE `surveys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@ducksurveys.com','$2a$10$B8FF48onifDVUfWle4IBv.Yq4JLe/pDjr8I4yRmVfUpbQzJYs46GG','ADMIN','admin');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-02 17:37:21

UPDATE users SET role = 'ROLE_ADMIN_SUPREMO' WHERE id = 1;