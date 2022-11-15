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
	constraint customerPK primary key(customerID),
	-- needs foreign key to LoyaltyLevel
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
	rewardPoints real check(rewardPoints >= 0),
	redeemPoints real check(redeemPoints >= 0),
	constraint coffeePK primary key(coffeeID)
);

/* Since Customer:Purchase is a binary 1:N relationship
   the primary key of of Customer should be included as
   a foreign key in Purchase */

-- delete purchase history if customer is deleted

create table Purchase (
	purchaseID integer not null,
	customerID integer not null,
	storeNumber integer not null,
	purchaseTime time,
	coffeeID integer not null,
	redeemPortion real,
	purchasePortion real check(purchasePortion >= 0),
	constraint purchasePK primary key(purchaseID),
	constraint purchaseCustomerFK foreign key(customerID) references Customer(customerID) on delete cascade,
	constraint purchaseStoreFK foreign key(storeNumber) references Store(storeNumber) on delete cascade,
	constraint purchaseCoffeeFK foreign key(coffeeID) references Coffee(coffeeID) on delete cascade
);

create table Promotion (
	promotionNumber integer not null,
	promotionName varchar(50),
	promotionStartDate date check(promotionStartDate < promotionEndDate),
	promotionEndDate date check(promotionEndDate > promotionStartDate),
	constraint promotionPK primary key(promotionNumber)
);

-- LoyaltyLevel is a weak entity type because it does not have its own primary key
-- if customer is deleted, remove information about loyalty level
create table LoyaltyLevel (
	customerID integer not null,
	levelName loyalty_level,
	boostFactor real check(boostFactor >= 0),
	constraint loyaltyPK primary key(customerID),
	constraint loyaltyFK foreign key(customerID) references Customer(customerID) on delete cascade
);

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

-- if store or coffee is deleted, delete entry
create table offersCoffee (
	coffeeID integer not null,
	storeID integer not null,
	constraint offersCoffeePK primary key (coffeeID, storeID),
	constraint coffeeIDFK foreign key (coffeeID) references Coffee(coffeeID) on delete cascade,
	constraint storeIDFK foreign key (storeID) references Store(storeNumber) on delete cascade
);