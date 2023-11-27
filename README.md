# web_expense_tracker

This is a repository for a simple coded Expense Tracker. Made by Liam Hogenkamp, Matt Hicks, and Lucas Johnson from Creighton University.

It was developed on spring boot using basic java and HTML.

The code is a locally run expense tracker that one could use and implement on their own computer if wanted.

# File System
The file system includes a multitude of different sections: 
* A Controller
* An Application Runner
* Multiple java files
* Multiple HTML files

# Data
In order for the Expense Tracker to function properly it must be connected locally to a database.
To create the database we have provided the code below.
Code:
  CREATE TABLE Login (
     login_id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
     user_name VARCHAR(50) NOT NULL,
     password VARCHAR(50) NOT NULL
  )ENGINE = INNODB;

  CREATE TABLE Expense (
     expense_id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
     category VARCHAR(50) NOT NULL,
     amount double NOT NULL,
     purchase_date DATE,
     descriptions VARCHAR(250),
     fk_login_id int NOT NULL,
     INDEX par_ind (fk_login_id),
     FOREIGN KEY (fk_login_id) REFERENCES Login(login_id)
     ON DELETE CASCADE
  )ENGINE = INNODB;

  CREATE TABLE personal_info(
     personal_key int PRIMARY KEY NOT NULL AUTO_INCREMENT,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     email VARCHAR(50) NOT NULL,
     credit_card BIGINT NOT NULL,
     fk_login_id int NOT NULL,
     INDEX par_ind (fk_login_id),
     FOREIGN KEY (fk_login_id) REFERENCES Login(login_id)
     ON DELETE CASCADE
  )ENGINE = INNODB;




