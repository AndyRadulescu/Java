-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: sistem_bancar
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill` (
  `id_bill` int(11) NOT NULL AUTO_INCREMENT,
  `status` tinyint(4) NOT NULL,
  `fine_status` tinyint(4) NOT NULL,
  `id_client` int(11) NOT NULL,
  `id_provider` int(11) NOT NULL,
  `value` double NOT NULL,
  `fine_value` double NOT NULL,
  PRIMARY KEY (`id_bill`),
  KEY `client-bill_idx` (`id_client`),
  KEY `provider-bill_idx` (`id_provider`),
  CONSTRAINT `client-bill` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `provider-bill` FOREIGN KEY (`id_provider`) REFERENCES `provider` (`id_provider`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (120,0,0,24,7,383.18,0),(121,0,0,24,7,28.55,0),(122,0,0,24,7,497.5,0),(123,0,0,24,7,597.5,0),(124,0,0,24,7,697.5,0),(125,3,1,24,7,797.5,88.04);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `id_client` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(45) NOT NULL,
  `cnp` varchar(13) NOT NULL,
  `name` varchar(50) NOT NULL,
  `account_value` double NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id_client`),
  UNIQUE KEY `cnp_UNIQUE` (`cnp`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (24,'Decembrei 13','1234567890123','User1',482.88,'user1@yahoo.com','e10adc3949ba59abbe56e057f20f883e'),(25,'Avram iancu','1234567891324','User2',0,'user2@yahoo.com','e10adc3949ba59abbe56e057f20f883e'),(26,'Calea Bucuresti','1234567890126','User3',0,'user3@yahoo.com','e10adc3949ba59abbe56e057f20f883e');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice` (
  `id_notice` int(11) NOT NULL AUTO_INCREMENT,
  `id_client` int(11) NOT NULL,
  `id_provider` int(11) NOT NULL,
  `description` varchar(150) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `id_bill` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_notice`),
  KEY `client-notice_idx` (`id_client`),
  KEY `provider-notice_idx` (`id_provider`),
  KEY `bill-notice_idx` (`id_bill`),
  CONSTRAINT `bill-notice` FOREIGN KEY (`id_bill`) REFERENCES `bill` (`id_bill`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `client-notice` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `provider-notice` FOREIGN KEY (`id_provider`) REFERENCES `provider` (`id_provider`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (88,24,7,'Bill (121) la electrica cu valoarea totala de 28.55 a fost platita cu succes!',0,121),(89,24,7,'Bill (122) la electrica cu valoarea totala de 497.5 nu a putut fi platita, fonduri insuficiente!',0,122),(90,24,7,'Bill (123) la electrica cu valoarea totala de 597.5 nu a putut fi platita, fonduri insuficiente!',0,123),(91,24,7,'Bill (124) la electrica cu valoarea totala de 697.5 nu a putut fi platita, fonduri insuficiente!',0,124),(92,24,7,'Serviciile furnizorului electrica au fost suspendate din motive de neplata!',0,NULL);
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provider`
--

DROP TABLE IF EXISTS `provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provider` (
  `id_provider` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `fine_value` int(11) DEFAULT NULL,
  `account_value` double NOT NULL,
  `email` varchar(45) NOT NULL,
  `due_date` int(3) NOT NULL,
  PRIMARY KEY (`id_provider`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provider`
--

LOCK TABLES `provider` WRITE;
/*!40000 ALTER TABLE `provider` DISABLE KEYS */;
INSERT INTO `provider` VALUES (7,'electrica',10,5785.71,'electriace@gmail.com',7),(8,'orange',5,3835.8100000000004,'orange@gmail.com',3),(9,'voadofe',7,0,'voafaone@vod.ro',9),(10,'gazprom',1,836.0600000000001,'gazprom@yahoo.com',1),(11,'apa sa',5,0,'apa@gmail.com',5);
/*!40000 ALTER TABLE `provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_client`
--

DROP TABLE IF EXISTS `status_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status_client` (
  `id_status_client` int(11) NOT NULL AUTO_INCREMENT,
  `id_client` int(11) NOT NULL,
  `id_provider` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_status_client`),
  KEY `client-statusc_idx` (`id_client`),
  KEY `provider-statusc_idx` (`id_provider`),
  CONSTRAINT `client-statusc` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `provider-statusc` FOREIGN KEY (`id_provider`) REFERENCES `provider` (`id_provider`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_client`
--

LOCK TABLES `status_client` WRITE;
/*!40000 ALTER TABLE `status_client` DISABLE KEYS */;
INSERT INTO `status_client` VALUES (16,24,7,2);
/*!40000 ALTER TABLE `status_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'sistem_bancar'
--

--
-- Dumping routines for database 'sistem_bancar'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-11 15:24:31
