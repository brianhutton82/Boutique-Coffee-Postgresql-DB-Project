-- Brian Hutton: just started this, feel free to edit it
-- still need to map binary M:N relationship types to a relation
-- also still needs constraints and need to specify assumptions

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

create table Purchase (
	purchaseID int not null,
	purchaseTime time,
	redeemPortion ?,
	purchasePortion ?,
	constraint purchasePK primary key(purchaseID)
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

-- MAPPING BINARY 1:N RELATIONSHIP TYPES:
-- ---------------------------------------
-- for each regular binary relationship type R
-- identify the relation S that represents the
-- participating entity type at the N-side of
-- the relationship type
-- Include as foreign key in S the primary key
-- of the relation T that represents the
-- other entity type participating in R
-- each instance on the N-side is related
-- to at most one entity instance on the 1-side
-- of the relationship type

-- MAPPING OF BINARY M:N RELATIONSHIP TYPES:
-- -----------------------------------------
-- For each binary M:N relationship type R
-- create a new relation S to represent R
-- Include as foreign key attributes in S
-- the primary keys of the relations that
-- represent the participating entity types
-- their combination will form the primary
-- key of S