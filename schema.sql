-- Schema for CS1555 BoutiqueCoffee Project
-- Brian Hutton
-- Uday Ar

drop table Customer cascade;
drop table Store cascade;
drop table Coffee cascade;
drop table Purchase cascade;
drop table Promotion cascade;
drop table LoyaltyLevel cascade;
drop domain store_type cascade;
drop domain phone_type cascade;
drop domain loyalty_level cascade;

create domain store_type as varchar(7)
check(value in ('sitting', 'kiosk'));

create domain phone_type as varchar(6)
check(value in ('Home', 'Mobile', 'Work', 'Other'));

create domain loyalty_level as varchar(10)
check(value in ('basic', 'bronze', 'silver', 'gold', 'platinum', 'diamond'));

create table Customer (
	customerID int not null,
	customerFirstName varchar(20),
	customerLastName varchar(20),
	customerMiddleName char(1),
	birthDay char(2),
	birthMonth char(3),
	phoneNumber varchar(16),
	phoneType phone_type,
	constraint customerPK primary key(customerID)
);

create table Store (
	storeNumber int not null,
	storeName varchar(50),
	storeType store_type,
	gpsLat float,
	gpsLong float,
	constraint storePK primary key(storeNumber)
);

create table Coffee (
	coffeeID int not null,
	coffeeName varchar(50),
	description varchar(250),
	countryOfOrigin varchar(60),
	intensity int check(intensity >= 1 and intensity <= 12),
	price float,
	rewardPoints float,
	redeemPoints float,
	constraint coffeePK primary key(coffeeID)
);

-- Since Customer:Purchase is a binary 1:N relationship
-- the primary key of of Customer should be included as
-- a foreign key in Purchase

create table Purchase (
	purchaseID int not null,
	customerID int not null,
	purchaseTime time,
	redeemPortion ?,
	purchasePortion ?,
	constraint purchasePK primary key(purchaseID),
	constraint purchaseCustomerFK foreign key(customerID) references Customer(customerID)
);

create table Promotion (
	promotionNumber int not null,
	promotionName varchar(50),
	promotionStartDate date,
	promotionEndDate date,
	constraint promotionPK primary key(promotionNumber)
);

-- LoyaltyLevel is a weak entity type because it does not have its own primary key
create table LoyaltyLevel (
	levelName loyalty_level,
	boostFactor float,
	customerID int not null,
	constraint loyaltyFK foreign key(customerID) references Customer(customerID)
);




-- MAPPING OF BINARY M:N RELATIONSHIP TYPES:
-- -----------------------------------------
-- For each binary M:N relationship type R
-- create a new relation S to represent R
-- Include as foreign key attributes in S
-- the primary keys of the relations that
-- represent the participating entity types
-- their combination will form the primary
-- key of S

INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (1,'Klara','Marquez','s',22,6,8102515651,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (2,'Abbi','Levine','s',25,2,3986732244,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (3,'Amos','Cervantes','v',10,5,1422231013,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (4,'Alanah','Whittington','z',3,1,7933470003,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (5,'Abbey','Whitley','d',13,5,6738218129,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (6,'Ehsan','Robbins','t',11,12,7729148702,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (7,'Alexis','Stewart','c',19,8,2037953296,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (8,'Bo','Frame','g',25,9,5125765103,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (9,'Bridie','Rice','z',4,3,7610457547,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (10,'Junayd','Shah','i',23,6,1563396212,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (11,'Violet','Morgan','i',27,3,2230713723,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (12,'Mitchell','Ewing','m',4,5,2210634228,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (13,'Everly','Myers','n',30,11,8041600088,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (14,'Arabella','Scott','k',28,7,6032834936,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (15,'David','Shepherd','b',29,1,1806106956,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (16,'Sumayyah','Thorne','k',8,11,9634640402,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (17,'Manahil','Jarvis','d',1,7,4963194063,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (18,'Maxime','Oneil','s',19,12,4438005836,'Work');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (19,'Troy','Farrell','k',20,11,5479364713,'Home');
INSERT INTO Customer(customerID,customerFirstName,customerLastName,customerMiddleName,birthDay,birthMonth,phoneNumber,phoneType) VALUES (20,'Mikaeel','Kendall','w',2,1,7003350306,'Work');

create table hasPromotion (
	promotionID int not null,
	storeID int,
	constraint hasPromotionPK primary key(promotionID, storeID),
	constraint promotionIDFK foreign key(promotionID) references Promotion(promotionNumber),
	constraint storeIDFK foreign key(storeID) references Store(storeNumber)
);


create table promotionFor (
	promotionID int not null,
	coffeeID int not null,
	constraint promotionForPK primary key(promotionID, coffeeID),
	constraint promotionIDFK foreign key (promotionID) references Promotion(promotionNumber),
	constraint coffeeIDFK foreign key(coffeeID) references Coffee(coffeeID)
);


create table buysCoffee (
	purchaseID int not null,
	coffeeID int not null,
	constraint buysCoffeePK primary key (purchaseID, coffeeID),
	constraint purchaseIDFK foreign key(purchaseID) references Purchase(purchaseID),
	constraint coffeeIDFK foreign key(coffeeID) references Coffee(coffeeID)
);

create table offersCoffee (
	coffeeID int not null,
	storeID int not null,
	constraint offersCoffee
);

select * from Customer;