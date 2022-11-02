-- Schema for CS1555 BoutiqueCoffee Project
-- Brian Hutton
-- Uday Atragada

drop table if exists Customer cascade;
drop table if exists Store cascade;
drop table if exists Coffee cascade;
drop table if exists Purchase cascade;
drop table if exists Promotion cascade;
drop table if exists LoyaltyLevel cascade;
drop domain if exists store_type cascade;
drop domain if exists phone_type cascade;
drop domain if exists loyalty_level cascade;

create domain store_type as varchar(7)
check(value in ('sitting', 'kiosk'));

create domain phone_type as varchar(6)
check(value in ('Home', 'Mobile', 'Work', 'Other'));

create domain loyalty_level as varchar(10)
check(value in ('basic', 'bronze', 'silver', 'gold', 'platinum', 'diamond'));

create table Customer (
	customerID integer not null,
	customerFirstName varchar(20) not null,
	customerLastName varchar(20) not null,
	customerMiddleName char(1),
	birthDay char(2),
	birthMonth char(3),
	phoneNumber varchar(16),
	phoneType phone_type,
	totalPointsEarned real default 0,
	constraint customerPK primary key(customerID) 
);

create table Store (
	storeNumber integer not null,
	storeName varchar(50) unique not null,
	storeType store_type,
	gpsLat real not null,
	gpsLong real not null,
	constraint storePK primary key(storeNumber)
);

create table Coffee (
	coffeeID integer not null,
	coffeeName varchar(50) not null,
	description varchar(250),
	countryOfOrigin varchar(60),
	intensity integer check(intensity >= 1 and intensity <= 12),
	price real not null check(price > 0),
	rewardPoints real,
	redeemPoints real,
	constraint coffeePK primary key(coffeeID)
);

/* Since Customer:Purchase is a binary 1:N relationship
   the primary key of of Customer should be included as
   a foreign key in Purchase */

-- delete purchase history if customer is deleted

create table Purchase (
	purchaseID integer not null,
	customerID integer not null,
	purchaseTime time,
	redeemPortion real,
	purchasePortion real,
	constraint purchasePK primary key(purchaseID),
	constraint purchaseCustomerFK foreign key(customerID) references Customer(customerID) on delete cascade
);

create table Promotion (
	promotionNumber integer not null,
	promotionName varchar(50),
	promotionStartDate date,
	promotionEndDate date,
	constraint promotionPK primary key(promotionNumber)
);

-- LoyaltyLevel is a weak entity type because it does not have its own primary key
-- if customer is deleted, remove information about loyalty level
create table LoyaltyLevel (
	customerID integer not null,
	levelName loyalty_level,
	boostFactor real,
	constraint loyaltyPK primary key(customerID),
	constraint loyaltyFK foreign key(customerID) references Customer(customerID) on delete cascade
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

-- if store is deleted, promo should be removed as well
-- if there is no promotion, then there should be no entry in this table
create table hasPromotion (
	promotionID integer not null,
	storeID integer,
	constraint hasPromotionPK primary key(promotionID, storeID),
	constraint promotionIDFK foreign key(promotionID) references Promotion(promotionNumber) on delete cascade,
	constraint storeIDFK foreign key(storeID) references Store(storeNumber) on delete cascade
);

-- coffee has to exist for promotion to exist, so delete entry if coffee is deleted
create table promotionFor (
	promotionID integer not null,
	coffeeID integer not null,
	constraint promotionForPK primary key(promotionID, coffeeID),
	constraint promotionIDFK foreign key (promotionID) references Promotion(promotionNumber) on delete cascade,
	constraint coffeeIDFK foreign key(coffeeID) references Coffee(coffeeID) on delete cascade
);

-- delete entry if either coffee or purchase is deleted
create table buysCoffee (
	purchaseID integer not null,
	coffeeID integer not null,
	constraint buysCoffeePK primary key (purchaseID, coffeeID),
	constraint purchaseIDFK foreign key(purchaseID) references Purchase(purchaseID) on delete cascade,
	constraint coffeeIDFK foreign key(coffeeID) references Coffee(coffeeID) on delete cascade
);

-- if store or coffee is deleted, delete entry
create table offersCoffee (
	coffeeID integer not null,
	storeID integer not null,
	constraint offersCoffeePK primary key (coffeeID, storeID),
	constraint coffeeIDFK foreign key (coffeeID) references Coffee(coffeeID) on delete cascade,
	constraint storeIDFK foreign key (storeID) references Store(storeID) on delete cascade
);


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
