-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema guida-tv
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema guida-tv
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `guida-tv` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `guida-tv` ;

-- -----------------------------------------------------
-- Table `guida-tv`.`canali`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`canali` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(40) NOT NULL,
  `numero` INT(10) NOT NULL,
  `logo` VARCHAR(100) NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `numero_unique` (`numero` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`programmi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`programmi` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `titolo` VARCHAR(60) NOT NULL,
  `descrizione` TEXT NULL DEFAULT NULL,
  `img` VARCHAR(256) NULL DEFAULT NULL,
  `link_ref` VARCHAR(256) NULL DEFAULT NULL,
  `durata` INT(10) NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`films`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`films` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `id_programma` INT(10) NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `id_film_programma` (`id_programma` ASC) VISIBLE,
  CONSTRAINT `id_film_programma`
    FOREIGN KEY (`id_programma`)
    REFERENCES `guida-tv`.`programmi` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`generi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`generi` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(30) NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nome_unique` (`nome` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`ruoli`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`ruoli` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(40) NOT NULL,
  `descrizione` TEXT NULL DEFAULT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`utenti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`utenti` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(40) NOT NULL,
  `cognome` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `email_verified_at` DATE NULL DEFAULT NULL,
  `token` VARCHAR(32) NULL DEFAULT NULL,
  `exp_date` DATE NULL DEFAULT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  `ruolo_id` INT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_unique` (`email` ASC) VISIBLE,
  INDEX `ruolo_id_idx` (`ruolo_id` ASC) VISIBLE,
  CONSTRAINT `ruolo_id`
    FOREIGN KEY (`ruolo_id`)
    REFERENCES `guida-tv`.`ruoli` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`interessa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`interessa` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `id_canale` INT(10) NOT NULL,
  `id_utente` INT(10) NOT NULL,
  `start_time` DATE NOT NULL,
  `end_time` DATE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_interessa_canale` (`id_canale` ASC) VISIBLE,
  INDEX `id_interessa_utente` (`id_utente` ASC) VISIBLE,
  CONSTRAINT `id_interessa_canale`
    FOREIGN KEY (`id_canale`)
    REFERENCES `guida-tv`.`canali` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `id_interessa_utente`
    FOREIGN KEY (`id_utente`)
    REFERENCES `guida-tv`.`utenti` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`programma_ha_generi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`programma_ha_generi` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `id_programma` INT(10) NOT NULL,
  `id_genero` INT(10) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_programma_genero` (`id_programma` ASC) VISIBLE,
  INDEX `id_genero_programma` (`id_genero` ASC) VISIBLE,
  CONSTRAINT `id_genero_programma`
    FOREIGN KEY (`id_genero`)
    REFERENCES `guida-tv`.`generi` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `id_programma_genero`
    FOREIGN KEY (`id_programma`)
    REFERENCES `guida-tv`.`programmi` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`programmazioni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`programmazioni` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `id_programma` INT(10) NOT NULL,
  `id_canale` INT(10) NOT NULL,
  `start_time` TIMESTAMP NOT NULL,
  `durata` INT(10) NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `id_programmazione_programma` (`id_programma` ASC) VISIBLE,
  INDEX `id_programmazione_canale` (`id_canale` ASC) VISIBLE,
  CONSTRAINT `id_programmazione_canale`
    FOREIGN KEY (`id_canale`)
    REFERENCES `guida-tv`.`canali` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `id_programmazione_programma`
    FOREIGN KEY (`id_programma`)
    REFERENCES `guida-tv`.`programmi` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`ricerca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`ricerca` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `id_utente` INT(10) NOT NULL,
  `query` TEXT NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `id_ricerca_utente` (`id_utente` ASC) VISIBLE,
  CONSTRAINT `id_ricerca_utente`
    FOREIGN KEY (`id_utente`)
    REFERENCES `guida-tv`.`utenti` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `guida-tv`.`serie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guida-tv`.`serie` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `id_programma` INT(10) NOT NULL,
  `stagione` INT(10) NOT NULL,
  `episodio` INT(10) NOT NULL,
  `version` BIGINT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `id_serie_programma` (`id_programma` ASC) VISIBLE,
  CONSTRAINT `id_serie_programma`
    FOREIGN KEY (`id_programma`)
    REFERENCES `guida-tv`.`programmi` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
