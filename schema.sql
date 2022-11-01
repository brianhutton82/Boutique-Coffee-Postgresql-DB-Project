-- Schema for CS1555 BoutiqueCoffee Project
-- Brian Hutton
-- Uday Ar

drop table Customer cascade constraints;
drop table Store cascade constraints;
drop table Coffee cascade constraints;
drop table Purchase cascade constraints;
drop table Promotion cascade constraints;
drop table LoyaltyLevel cascade constraints;

create domain store_type as varchar(7)
check(value in ('sitting', 'kiosk'));

create domain phone_type as varchar(6)
check(value in ('home', 'mobile', 'work', 'other'));

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
	contraint loyaltyFK foreign key(customerID) references Customer(customerID)
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

create table hasPromotion (
	promotionID int not null,
	storeID int,
	constraint hasPromotionPK primary key(promotionID, storeID),
	constraint promotionIDFK foreign key(promotionID) references Promotion(promotionID),
	constraint storeIDFK foreign key(storeID) references Store(storeID)
);


create table promotionFor (
	promotionID int not null,
	coffeeID int not null,
	constraint promotionForPK primary key(promotionID, coffeeID),
	constraint promotionIDFK foreign key (promotionID) references Promotion(promotionID),
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