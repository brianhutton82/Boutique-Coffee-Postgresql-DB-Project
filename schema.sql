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
drop table if exists hasPromotion cascade;
drop table if exists promotionFor cascade;
drop table if exists buysCoffee cascade;
drop table if exists offersCoffee cascade;

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
	constraint storeIDFK foreign key (storeID) references Store(storeNumber) on delete cascade
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

insert into Purchase values(5000,4,'6:00:00',1,0);
insert into Purchase values(5001,7,'6:10:00',1,0);
insert into Purchase values(5002,12,'6:20:00',1,0);
insert into Purchase values(5003,1,'6:30:00',1,0);
insert into Purchase values(5004,10,'6:40:00',1,0);
insert into Purchase values(5005,4,'6:50:00',1,0);
insert into Purchase values(5006,5,'7:00:00',1,0);
insert into Purchase values(5007,3,'7:10:00',1,0);
insert into Purchase values(5008,2,'7:20:00',1,0);
insert into Purchase values(5009,3,'7:30:00',1,0);
insert into Purchase values(5010,2,'7:40:00',1,0);
insert into Purchase values(5011,10,'7:50:00',1,0);
insert into Purchase values(5012,5,'8:00:00',1,0);
insert into Purchase values(5013,4,'8:10:00',1,0);
insert into Purchase values(5014,3,'8:20:00',1,0);
insert into Purchase values(5015,12,'8:30:00',1,0);
insert into Purchase values(5016,2,'8:40:00',1,0);
insert into Purchase values(5017,11,'8:50:00',1,0);
insert into Purchase values(5018,1,'9:00:00',1,0);
insert into Purchase values(5019,9,'9:10:00',1,0);
insert into Purchase values(5020,7,'9:20:00',1,0);
insert into Purchase values(5021,8,'9:30:00',1,0);
insert into Purchase values(5022,1,'9:40:00',1,0);
insert into Purchase values(5023,7,'9:50:00',1,0);
insert into Purchase values(5024,8,'10:00:00',1,0);
insert into Purchase values(025,1,'10:10:00',1,0);
insert into Purchase values(5026,9,'10:20:00',1,0);
insert into Purchase values(5027,3,'10:30:00',1,0);
insert into Purchase values(5028,4,'10:40:00',1,0);
insert into Purchase values(5029,4,'10:50:00',1,0);
insert into Purchase values(5030,12,'11:00:00',1,0);
insert into Purchase values(5031,6,'11:10:00',1,0);
insert into Purchase values(5032,7,'11:20:00',1,0);
insert into Purchase values(5033,3,'11:30:00',1,0);
insert into Purchase values(5034,2,'11:40:00',1,0);
insert into Purchase values(5035,2,'11:50:00',1,0);
insert into Purchase values(5036,6,'12:00:00',1,0);
insert into Purchase values(5037,1,'12:10:00',1,0);
insert into Purchase values(5038,7,'12:20:00',1,0);
insert into Purchase values(5039,8,'12:30:00',1,0);
insert into Purchase values(5040,10,'12:40:00',1,0);
insert into Purchase values(5041,10,'12:50:00',1,0);
insert into Purchase values(5042,8,'13:00:00',1,0);
insert into Purchase values(5043,10,'13:10:00',1,0);
insert into Purchase values(5044,3,'13:20:00',1,0);
insert into Purchase values(5045,3,'13:30:00',1,0);
insert into Purchase values(5046,6,'13:40:00',1,0);
insert into Purchase values(5047,10,'13:50:00',1,0);
insert into Purchase values(5048,2,'14:00:00',1,0);
insert into Purchase values(5049,5,'14:10:00',1,0);