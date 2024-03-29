-- Brian Hutton
-- Breanna Burns
-- Uday Atragada

-- when Clock is updated, check to see if customer should receive gift points
-- on a customers birthday, the total number of points earned is increased by 10%
create or replace function updatePointsOnBirthday()
returns trigger as $$
declare
	currentDay char(2);
	currentMonth char(3);
begin
	currentDay := cast(extract(day from current_timestamp) as char(2));
	currentMonth := to_char(current_timestamp, 'Mon');
	update Customer
	set totalPointsEarned = totalPointsEarned * 1.1
	where birthMonth=currentMonth and birthDay=currentDay;
	return new;
end;
$$ language plpgsql;

drop trigger if exists clockUpdate on Clock;
create trigger clockUpdate
after insert or update on Clock
for each row
execute procedure updatePointsOnBirthday();

-- The final reward points of a coffee is the reward points of the coffee times the booster factor of the customer’s loyalty level

-- this function computes a customers reward points
drop function if exists updateCustomerPoints;
create or replace function updateCustomerPoints(custID int, coffID int)
returns float as
$$
declare
	boost real;
	coffee_points real;
	customer_points real;
	new_points real;
begin
	-- select boost factor of the customer
	select boostFactor
	into boost
	from Customer join LoyaltyLevel
	on Customer.loyaltyLevel = LoyaltyLevel.levelName
	where customerID = custID;
	--select reward points of the coffee purchased
	select rewardPoints
	into coffee_points
	from Coffee
	where coffeeID = coffID;
	--find customers earned points
	select totalPointsEarned
	into customer_points
	from Customer
	where customerID = custID;
	--computes the new points that a customer using coffee points, boost factor of their level, and their points before the transaction
	new_points := ((coffee_points * boost) + customer_points);
	return new_points;
end;
$$ language plpgsql;

-- when a new purchase is added, this trigger updates the customers points
create or replace function updateCustomerOnPurchase()
returns trigger as
$$
declare
	new_points real;
    coffee_points real;
begin
	select redeemPoints
	into coffee_points
	from Coffee
	where coffeeID = new.coffeeID;

	-- if customer is using reward points subtract the points of that coffee
	if new.redeemPortion > 0 and new.purchasePortion = 0 then
        new_points := new_points - coffee_points;
    else
	    -- get new computed points since customer is purchasing not redeeming
	    select into new_points updateCustomerPoints(new.customerID, new.coffeeID);
    end if;

	-- update Customers points
	update Customer
	set totalPointsEarned = new_points
	where customerID = new.customerID;
	return new;
end;
$$ language plpgsql;

drop trigger if exists newPurchase on Purchase;
create trigger newPurchase
after insert or update on Purchase
for each row
execute procedure updateCustomerOnPurchase();

-- removes coupon when expired
create or replace function removeExpiredPromo()
returns trigger as
$$
declare
	currTime real;
begin
	-- get time 
	select p_date
	into currTime
	from Clock;
	-- delete promotions with end date before time 
	DELETE FROM Promotion
	WHERE promotionEndDate < currTime;

	return new;
end;
$$ language plpgsql;

drop trigger if exists clockUpdate on Clock;
create trigger clockUpdate
after insert or update on Clock
for each row
execute procedure removeExpiredPromo();




	 
-- updates user's loyalty level
create or replace function updateUserLoyalty()
returns trigger as
$$
declare
	purchaseCount real;
	newLoyalty varchar(10);
begin
	-- get purchase count 
	select count(customerID)
	into purchaseCount
	from Purchase
	where customerID  = new.customerID;

	-- update user loyalty 
    if purchaseCount >= 10 and purchaseCount < 20 then
            newLoyalty := 'bronze';
	elsif purchaseCount >= 20 and purchaseCount < 30 then
            newLoyalty := 'silver';
	elsif purchaseCount >= 30 and purchaseCount < 40 then
            newLoyalty := 'gold';
	elsif purchaseCount >= 40 and purchaseCount < 50 then
            newLoyalty := 'platinum';
	elsif purchaseCount >= 50 then
            newLoyalty := 'diamond';
    else
        newLoyalty := 'basic';
    end if;

	Update Customer
	Set loyaltyLevel = newLoyalty
	Where customerID = new.customerID;

	return new;

end;

$$ language plpgsql;


drop trigger if exists newPurchase on Purchase;
create trigger newPurchase
after insert or update on Purchase
for each row
execute procedure updateUserLoyalty();