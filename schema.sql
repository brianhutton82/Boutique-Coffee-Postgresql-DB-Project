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

-- function used to update customers reward points on their birthday
-- this function will be used in a trigger
create or replace function updateRewardPointsOnBirthday(points real)
returns real
as
$$
begin
	return ((points * 0.1) + points);
end;
$$ language plpgsql;

-- this function will be used in a trigger
create or replace function isCustomersBirthday(birthMonth char(3), birthDay char(2))
returns boolean
as
$$
declare
currentDay char(2);
currentMonth char(3);
begin
	currentDay := cast(extract(day from current_timestamp) as char(2));
	currentMonth := to_char(current_timestamp, 'mon');
	return (currentDay=birthDay and currentMonth=birthMonth);
end;
$$ language plpgsql;

-- moved foreign key to Customer table
-- added check constraint to ensure boostFactor is non-negative
create table LoyaltyLevel (
	loyaltyLevelID integer not null,
	levelName loyalty_level,
	boostFactor real check(boostFactor >= 0),
	constraint loyaltyPK primary key(loyaltyLevelID)
);

-- added foreign key to loyalty level table
create table Customer (
	customerID integer not null,
	loyaltyID integer,
	customerFirstName varchar(20) not null,
	customerLastName varchar(20) not null,
	customerMiddleName char(1),
	birthDay char(2),
	birthMonth char(3),
	phoneNumber varchar(16),
	phoneType phone_type,
	totalPointsEarned real default 0,
	constraint customerPK primary key(customerID),
	constraint loyaltyFK foreign key(loyaltyID) references LoyaltyLevel(loyaltyLevelID) on delete set null
);

create table Store (
	storeNumber integer not null,
	storeName varchar(50) unique not null,
	storeType store_type,
	gpsLat real not null,
	gpsLong real not null,
	constraint storePK primary key(storeNumber)
);

-- added check constraint to ensure rewardPoints & redeemPoints are non-negative
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

-- added check constraint to ensure redeemPortion & purchasePortion are non-negative
-- added constraint so that customer cannot both use reward points and use cash in Purchase, only one or the other, not both
create table Purchase (
	purchaseID integer not null,
	customerID integer not null,
	storeNumber integer not null,
	purchaseTime time,
	coffeeID integer not null,
	redeemPortion real check(redeemPortion >= 0),
	purchasePortion real check(purchasePortion >= 0),
	constraint purchasePK primary key(purchaseID),
	constraint purchaseCustomerFK foreign key(customerID) references Customer(customerID) on delete cascade,
	constraint purchaseStoreFK foreign key(storeNumber) references Store(storeNumber) on delete cascade,
	constraint purchaseCoffeeFK foreign key(coffeeID) references Coffee(coffeeID) on delete cascade,
	constraint redeemOrPurchase check ((redeemPortion > 0 and purchasePortion > 0) = false)
);

-- removed not null requirement from promotionStartDate & promotionEndDate
-- added check constraint to ensure end_date is not before start_date & vice-versa
create table Promotion (
	promotionNumber integer not null,
	promotionName varchar(50),
	promotionStartDate date check(promotionStartDate <= promotionEndDate),
	promotionEndDate date check(promotionEndDate >= promotionStartDate),
	constraint promotionPK primary key(promotionNumber)
);

create table hasPromotion (
	promotionID integer not null,
	storeID integer,
	constraint hasPromotionPK primary key(promotionID, storeID),
	constraint promotionIDFK foreign key(promotionID) references Promotion(promotionNumber) on delete cascade,
	constraint storeIDFK foreign key(storeID) references Store(storeNumber) on delete cascade
);

create table promotionFor (
	promotionID integer not null,
	coffeeID integer not null,
	constraint promotionForPK primary key(promotionID, coffeeID),
	constraint promotionIDFK foreign key (promotionID) references Promotion(promotionNumber) on delete cascade,
	constraint coffeeIDFK foreign key(coffeeID) references Coffee(coffeeID) on delete cascade
);

create table offersCoffee (
	coffeeID integer not null,
	storeID integer not null,
	constraint offersCoffeePK primary key (coffeeID, storeID),
	constraint coffeeIDFK foreign key (coffeeID) references Coffee(coffeeID) on delete cascade,
	constraint storeIDFK foreign key (storeID) references Store(storeNumber) on delete cascade
);