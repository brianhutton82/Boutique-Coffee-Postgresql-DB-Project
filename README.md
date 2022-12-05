# cs1555-BoutiqueCoffee-project
Repository for CS1555 Project

## Team 13:
* Brian Hutton - beh82@pitt.edu
* Breanna Burns - bnb51@pitt.edu
* Uday Atragada - uda1@pitt.edu

## Instructions:
* Run pgAdmin 4 to start the local PostgreSQL server
* In DataGrip, navigate to File → Data Sources, in the Data Sources and Drivers dialog, click + and select PosgreSQL.
* While still in DataGrip, if any driver files are missing click Download missing driver files.
* While still in DataGrip, go to the General tab and specify connection details
  * Host is **localhost**
  * Port is **5432**
  * User is **postgres**
  * Password is the one you used to set up postgresql locally
* After the database is up and running ensure everything is compiled using: `javac -cp ".;postgresql-42.5.0.jar" *.java`
* Then to run the driver program use: `java -cp ".;postgresql-42.5.0.jar" BCDriver`


## Due Dates:
* Phase 1: DB Design - 8:00PM, Nov 2, 2022
* Phase 2: DB Implementation & Interface - 8:00PM, Dec 1, 2022
* Phase 3: Driver & Optimizations - 8:00PM, Dec 10, 2022
