-- MySQL Script generated by MySQL Workbench
-- Fri Jan  3 10:11:47 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Appointments_Service
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Appointments_Service
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Appointments_Service` DEFAULT CHARACTER SET utf8 ;
USE `Appointments_Service` ;

-- -----------------------------------------------------
-- Table `Appointments_Service`.`laboratoy_appointments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Appointments_Service`.`laboratory_appointments` (
  `id_appointments` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `start_hour` TIME NULL,
  `end_hour` TIME NULL,
  `cancelled` TINYINT(1) NULL,
  `assistance` TINYINT(1) NULL,
  `nss` VARCHAR(10) NULL,
  `id_medical_studies` INT NULL,
  `id_laboratories` INT NULL,
  PRIMARY KEY (`id_appointments`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Appointments_Service`.`consultory_appointments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Appointments_Service`.`consultory_appointments` (
  `id_appointments` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `start_hour` TIME NULL,
  `end_hour` TIME NULL,
  `cancelled` TINYINT(1) NULL,
  `assistance` TINYINT(1) NULL,
  `nss` VARCHAR(10) NULL,
  `id_consultories` INT NULL,
  PRIMARY KEY (`id_appointments`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Appointments_Service`.`qx_appointments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Appointments_Service`.`qx_appointments` (
  `id_appointments` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `start_hour` TIME NULL,
  `end_hour` TIME NULL,
  `cancelled` TINYINT(1) NULL,
  `assistance` TINYINT(1) NULL,
  `nss` VARCHAR(10) NULL,
  `id_surgical_intervention_cat` INT NULL,
  `id_qx` INT NULL,
  PRIMARY KEY (`id_appointments`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;