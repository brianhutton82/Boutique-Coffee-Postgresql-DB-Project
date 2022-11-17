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