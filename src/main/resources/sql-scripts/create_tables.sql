DROP SCHEMA IF EXISTS `recipes_db`;
CREATE SCHEMA IF NOT EXISTS `recipes_db` DEFAULT CHARACTER SET utf8;
USE `recipes_db`;

-- -----------------------------------------------------
-- Table `recipes_db`.`DishType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`DishType`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`DishType` (
  `ID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name`))
ENGINE = InnoDB;

insert into `recipes_db`.`DishType` (`Name`) values ("Суп");
insert into `recipes_db`.`DishType` (`Name`) values ("Салат");
insert into `recipes_db`.`DishType` (`Name`) values ("Горячее блюдо");
insert into `recipes_db`.`DishType` (`Name`) values ("Соус");
insert into `recipes_db`.`DishType` (`Name`) values ("Холодная закуска");
insert into `recipes_db`.`DishType` (`Name`) values ("Блины");
insert into `recipes_db`.`DishType` (`Name`) values ("Печенье");
insert into `recipes_db`.`DishType` (`Name`) values ("Паста");
insert into `recipes_db`.`DishType` (`Name`) values ("Пицца");
insert into `recipes_db`.`DishType` (`Name`) values ("Пирог");


-- -----------------------------------------------------
-- Table `recipes_db`.`IngredientMeasure`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`IngredientMeasure`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`IngredientMeasure` (
  `ID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name`))
ENGINE = InnoDB;

insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("шт");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("гр");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("л");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("мл");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("стаканов");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("чайных ложек");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("столовых ложек");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("по вкусу");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("щепоток");
insert into `recipes_db`.`IngredientMeasure` (`Name`) values ("зубчиков");


-- -----------------------------------------------------
-- Table `recipes_db`.`Component`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`Component`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`Component` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `recipes_db`.`Ingredient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`Ingredient`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`Ingredient` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `recipes_db`.`IngredientComponent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`IngredientComponent`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`IngredientComponent` (
  `Ingredient_ID` BIGINT(20) UNSIGNED NOT NULL,
  `Component_ID` BIGINT(20) UNSIGNED NOT NULL,
  CONSTRAINT `fk_Ingredient`
    FOREIGN KEY (`Ingredient_ID`)
    REFERENCES `recipes_db`.`Ingredient` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Component`
    FOREIGN KEY (`Component_ID`)
    REFERENCES `recipes_db`.`Component` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `recipes_db`.`Recipe`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`Recipe`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`Recipe` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `DishType_ID` INT UNSIGNED NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Description` text NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Recipe_DishType_idx` (`DishType_ID` ASC),
  UNIQUE INDEX `DishType_Name_UNIQUE` (`DishType_ID`, `Name`),
  CONSTRAINT `fk_Recipe_DishType`
    FOREIGN KEY (`DishType_ID`)
    REFERENCES `recipes_db`.`DishType` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `recipes_db`.`RecipeIngredient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `recipes_db`.`RecipeIngredient`;

CREATE TABLE IF NOT EXISTS `recipes_db`.`RecipeIngredient` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Recipe_ID` BIGINT(20) UNSIGNED NOT NULL,
  `Ingredient_ID` BIGINT(20) UNSIGNED NOT NULL,
  `Count` DOUBLE UNSIGNED NOT NULL,
  `IngredientMeasure_ID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_RecipeIngredient_Recipe_idx` (`Recipe_ID` ASC),
  INDEX `fk_RecipeIngredient_IngredientMeasure_idx` (`IngredientMeasure_ID` ASC),
  INDEX `fk_RecipeIngredient_Ingredient_idx` (`Ingredient_ID` ASC),
  CONSTRAINT `fk_RecipeIngredient_Recipe`
    FOREIGN KEY (`Recipe_ID`)
    REFERENCES `recipes_db`.`Recipe` (`ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_RecipeIngredient_IngredientMeasure`
    FOREIGN KEY (`IngredientMeasure_ID`)
    REFERENCES `recipes_db`.`IngredientMeasure` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RecipeIngredient_Ingredient`
    FOREIGN KEY (`Ingredient_ID`)
    REFERENCES `recipes_db`.`Ingredient` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

