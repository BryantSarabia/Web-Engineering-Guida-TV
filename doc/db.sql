CREATE DATABASE IF NOT EXISTS `guida-tv`;
USE `guida-tv`;


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

DROP TABLE IF EXISTS `ruoli`;
CREATE TABLE `ruoli`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`nome` VARCHAR(40) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY(`id`),
UNIQUE KEY `nome_unique` (`nome`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `ruoli` WRITE;
/*!40000 ALTER TABLE `ruoli` DISABLE KEYS */;
INSERT INTO `ruoli` VALUES (1,'Utente',0),(2,'Amministratore',0);
/*!40000 ALTER TABLE `ruoli` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `utenti`;
CREATE TABLE `utenti`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_ruolo` INT(10) DEFAULT NULL,
`nome` VARCHAR(40) NOT NULL,
`cognome` VARCHAR(50) NOT NULL,
`email` VARCHAR(100) NOT NULL,
`password` VARCHAR(256) NOT NULL,
`send_email` TINYINT DEFAULT '0',
`email_verified_at` DATE DEFAULT NULL,
`token` VARCHAR(32) DEFAULT NULL,
`exp_date` DATE DEFAULT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
CONSTRAINT utente_ruolo FOREIGN KEY (`id_ruolo`) REFERENCES `ruoli` (`id`) ON DELETE SET NULL ,
UNIQUE KEY `email_unique` (`email`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,2,'admin','admin','admin@admin.it','$2a$10$H49DGXOiNJh3jkWQSbTIfe0oNLi69x2oRE62HEG8ege5XEfZBXOM.',0, NOW(), null, null,0);
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;




DROP TABLE IF EXISTS `canali`;
CREATE TABLE `canali`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`numero` int(10) NOT NULL,
`nome` VARCHAR(40) NOT NULL,
`logo` VARCHAR(100) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE KEY `numero_unique` (`numero`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `canali` WRITE;
/*!40000 ALTER TABLE `canali` DISABLE KEYS */;
INSERT INTO `canali` VALUES (1,1,'Rai1','img_tv/canali/small/1.png',0),(2,2,'Rai 2','img_tv/canali/small/2.png',0),(3,3,'Rai 3','img_tv/canali/small/3.png',0),(4,4,'Rete 4','img_tv/canali/small/4.png',0),(5,5,'Canale 5','img_tv/canali/small/5.png',0),(6,6,'Italia 1','img_tv/canali/small/6.png',0),(7,7,'LA 7','img_tv/canali/small/7.png',0),(8,21,'Rai 4','img_tv/canali/small/8.png',0),(9,23,'Rai 5','img_tv/canali/small/9.png',0),(10,25,'Rai Premium','img_tv/canali/small/10.png',0),(11,24,'Rai Movie','img_tv/canali/small/11.png',0),(12,29,'LA 7d','img/canali/small/12.png',0),(13,26,'Cielo','img_tv/canali/small/13.png',0);
/*!40000 ALTER TABLE `canali` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `generi`;
CREATE TABLE `generi`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`nome` VARCHAR(30) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE KEY `nome_unique` (`nome`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `generi` WRITE;
/*!40000 ALTER TABLE `generi` DISABLE KEYS */;
INSERT INTO `generi` VALUES (1,'Animazione',0),(2,'Avventura',0),(3,'Azione',0),(4,'Commedia',0),(5,'Documentario',0),(6,'Drammatico',0),(7,'Fantascienza',0),(8,'Giallo',0),(9,'Guerra',0),(10,'Horror',0),(11,'Storico',0),(12,'Programma Televisivo',0);
/*!40000 ALTER TABLE `generi` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `programmi`;
CREATE TABLE `programmi`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`titolo` VARCHAR(60) NOT NULL,
`descrizione` TEXT,
`img` VARCHAR(256),
`link_ref` VARCHAR(256),
`durata` INT(10) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `programmi` WRITE;
/*!40000 ALTER TABLE `programmi` DISABLE KEYS */;
INSERT INTO `programmi` VALUES (1,'Reazione a Catena','descrizione','img_tv/progs/prog_1.jpg','https://it.wikipedia.org/wiki/Reazione_a_catena_-_L%27intesa_vincente',120,0),(2,'L\'occhio del ciclone','descrizione','img_tv/progs/prog_2.jpg','https://it.wikipedia.org/wiki/L%27occhio_del_ciclone_-_In_the_Electric_Mist',120,0),(3,'Presa Diretta','descrizione','img_tv/progs/prog_3.jpg','https://it.wikipedia.org/wiki/Presa_diretta_(programma_televisivo)',120,0),(4,'Telegiornale','descrizione','img_tv/progs/prog_4.jpg','https://it.wikipedia.org/wiki/TG4',120,0),(5,'The Wall','descrizione','img_tv/progs/prog_5.jpg','https://it.wikipedia.org/wiki/The_Wall_(programma_televisivo_italiano)',120,0),(6,'Il commisario Cordier','descrizione','img_tv/progs/prog_7.jpg','https://it.wikipedia.org/wiki/Il_commissario_Cordier_(serie_televisiva_1992)',60,0);
/*!40000 ALTER TABLE `programmi` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `serie`;
CREATE TABLE `serie`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_programma` INT(10) NOT NULL,
`stagione` INT(10) NOT NULL,
`episodio` INT(10) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
CONSTRAINT `id_serie_programma` FOREIGN KEY (`id_programma`) REFERENCES `programmi` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `serie` WRITE;
/*!40000 ALTER TABLE `serie` DISABLE KEYS */;
INSERT INTO `serie` VALUES(1,1,2,20,0),(2,3,1,12,0),(3,5,1,4,0),(4,6,3,12,0);
/*!40000 ALTER TABLE `serie` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `films`;
CREATE TABLE `films`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_programma` INT(10) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
CONSTRAINT `id_film_programma` FOREIGN KEY (`id_programma`) REFERENCES `programmi` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `films` WRITE;
/*!40000 ALTER TABLE `films` DISABLE KEYS */;
INSERT INTO `films` VALUES(1,2,0);
/*!40000 ALTER TABLE `films` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `programmazioni`;
CREATE TABLE `programmazioni`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_programma` INT(10) NOT NULL,
`id_canale` INT(10) NOT NULL,
`start_time` TIMESTAMP NOT NULL,
`durata` INT(10) NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
CONSTRAINT `id_programmazione_programma` FOREIGN KEY (`id_programma`) REFERENCES `programmi` (`id`) ON DELETE CASCADE,
CONSTRAINT `id_programmazione_canale` FOREIGN KEY (`id_canale`) REFERENCES `canali` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `programmazioni` WRITE;
/*!40000 ALTER TABLE `programmazioni` DISABLE KEYS */;
INSERT INTO `programmazioni` VALUES (1,1,1,'2020-08-15 18:00:00',120,0),(2,2,2,'2020-08-15 20:00:00',120,0),(3,3,3,'2020-08-15 16:00:00',120,0),(4,4,4,'2020-08-15 14:00:00',120,0),(5,5,5,'2020-08-15 18:00:00',120,0),(6,6,7,'2020-08-15 22:00:00',120,0);
/*!40000 ALTER TABLE `programmazioni` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `interessa`;
CREATE TABLE `interessa`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_canale` INT(10) NOT NULL,
`id_utente` INT(10) NOT NULL,
`start_time` DATE NOT NULL,
`end_time` DATE NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `id_interessa_canale` FOREIGN KEY (`id_canale`) REFERENCES `canali` (`id`) ON DELETE CASCADE,
CONSTRAINT `id_interessa_utente` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ricerca`;
CREATE TABLE `ricerca`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_utente` INT(10) NOT NULL,
`query` TEXT NOT NULL,
`version` BIGINT(20) UNSIGNED DEFAULT '0',
PRIMARY KEY (`id`),
CONSTRAINT `id_ricerca_utente` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `programma_ha_generi`;
CREATE TABLE `programma_ha_generi`(
`id` INT(10) NOT NULL AUTO_INCREMENT,
`id_programma` INT(10) NOT NULL,
`id_genero` INT(10) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `id_programma_genero` FOREIGN KEY (`id_programma`) REFERENCES `programmi` (`id`) ON DELETE CASCADE,
CONSTRAINT `id_genero_programma` FOREIGN KEY (`id_genero`) REFERENCES `generi` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `programma_ha_generi` WRITE;
/*!40000 ALTER TABLE `programma_ha_generi` DISABLE KEYS */;
INSERT INTO `programma_ha_generi` VALUES(1,1,12),(2,2,3),(3,2,7),(4,3,12),(5,5,12),(6,6,4),(7,6,5);
/*!40000 ALTER TABLE `programma_ha_generi` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `utente_ha_ruoli`;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;