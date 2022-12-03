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
	select boostFactor
	into boost
	from Customer join LoyaltyLevel
	on Customer.loyaltyLevel = LoyaltyLevel.levelName
	where customerID = custID;

	select rewardPoints
	into coffee_points
	from Coffee
	where coffeeID = coffID;

	select totalPointsEarned
	into customer_points
	from Customer
	where customerID = custID;

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
begin
	-- get new computed points
	select into new_points updateCustomerPoints(new.customerID, new.coffeeID);

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