-- Brian Hutton

insert into LoyaltyLevel values('basic', 1);
/*
insert into LoyaltyLevel values('bronze',2);
insert into LoyaltyLevel values('silver',3);
insert into LoyaltyLevel values('gold',4);
insert into LoyaltyLevel values('platinum',5);
insert into LoyaltyLevel values('diamond', 6);
*/

-- 20 customers
-- updated Customer insert statements to include LoyaltyID
INSERT INTO Customer VALUES (1,'basic','Klara','Marquez','s',22,6,8102515651,'Home');
INSERT INTO Customer VALUES (2,'basic','Abbi','Levine','s',25,2,3986732244,'Work');
INSERT INTO Customer VALUES (3,'basic','Amos','Cervantes','v',10,5,1422231013,'Home');
INSERT INTO Customer VALUES (4,'basic','Alanah','Whittington','z',3,1,7933470003,'Work');
INSERT INTO Customer VALUES (5,'basic','Abbey','Whitley','d',13,5,6738218129,'Home');
INSERT INTO Customer VALUES (6,'basic','Ehsan','Robbins','t',11,12,7729148702,'Work');
INSERT INTO Customer VALUES (7,'basic','Alexis','Stewart','c',19,8,2037953296,'Home');
INSERT INTO Customer VALUES (8,'basic','Bo','Frame','g',25,9,5125765103,'Work');
INSERT INTO Customer VALUES (9,'basic','Bridie','Rice','z',4,3,7610457547,'Home');
INSERT INTO Customer VALUES (10,'basic','Junayd','Shah','i',23,6,1563396212,'Work');
INSERT INTO Customer VALUES (11,'basic','Violet','Morgan','i',27,3,2230713723,'Home');
INSERT INTO Customer VALUES (12,'basic','Mitchell','Ewing','m',4,5,2210634228,'Work');
INSERT INTO Customer VALUES (13,'basic','Everly','Myers','n',30,11,8041600088,'Home', 12);
INSERT INTO Customer VALUES (14,'basic','Arabella','Scott','k',28,7,6032834936,'Work');
INSERT INTO Customer VALUES (15,'basic','David','Shepherd','b',29,1,1806106956,'Home');
INSERT INTO Customer VALUES (16,'basic','Sumayyah','Thorne','k',8,11,9634640402,'Work');
INSERT INTO Customer VALUES (17,'basic','Manahil','Jarvis','d',1,7,4963194063,'Home');
INSERT INTO Customer VALUES (18,'basic','Maxime','Oneil','s',19,12,4438005836,'Work');
INSERT INTO Customer VALUES (19,'basic','Troy','Farrell','k',20,11,5479364713,'Home');
INSERT INTO Customer VALUES (20,'basic','Mikaeel','Kendall','w',2,1,7003350306,'Work');

-- 3 stores
insert into Store values(2000, 'Udays Cafe', 'sitting', 40.440624, -79.995888);
insert into Store values(2001, 'Brians Cafe', 'kiosk', 50.440624, -79.995888);
insert into Store values(2002, 'Bres Cafe', 'sitting', 60.440624, -79.995888);

-- 12 Coffees
insert into Coffee values(1000, 'Café cubano', 'Cuban tradition is to drink coffee strong and sweet', 'Cuba', 1,3.95,1.975,19.75);
insert into Coffee values(1001, 'Caffè crema', 'Cuban tradition is to drink coffee strong and sweet', 'Italy', 2,3.49,1.745,17.45);
insert into Coffee values(1002, 'Ristretto', 'Ristretto is traditionally a short shot of espresso made with the normal amount of ground coffee but extracted with about half the amount of water.', 'Italy', 3,3,1.5,15);
insert into Coffee values(1003, 'café mocha', 'Like a latte, it is typically one third espresso and two thirds steamed milk, but a portion of chocolate is added, typically in the form of a chocolate syrup, although other vending systems use instant chocolate powder.', 'Italy', 4,4.95,2.475,24.75);
insert into Coffee values(1004, 'Freddo cappuccino', 'Freddo cappuccino is another variation of the original cappuccino and is as popular as the freddo espresso', 'Italy', 5,4.95,2.475,24.75);
insert into Coffee values(1005, 'Flat white', 'A flat white is an espresso with microfoam (steamed milk with small, fine bubbles and a glossy or velvety consistency).', 'Italy', 6,4.49,2.245,22.45);
insert into Coffee values(1006, 'Caffè americano', 'An americano is prepared by adding hot water to espresso, giving a similar strength to but different flavor from brewed coffee.', 'America', 7,2,1,10);
insert into Coffee values(1007, 'Latte', 'Coffee beverage of Italian origin made with espresso and steamed milk.', 'Italy', 8,4.5,2.25,22.5);
insert into Coffee values(1008, 'Doppio', 'Doppio is a double shot, served in a demitasse cup.', 'Italy', 9,3.95,1.975,19.75);
insert into Coffee values(1009, 'French press', 'A French press requires a coarser grind of coffee than a drip brew coffee filter, as finer grounds will seep through the press filter and into the coffee.', 'France', 10,3.49,5,17.45);
insert into Coffee values(1010, 'Cold brew', 'Cold brewing, also called cold water extraction or cold pressing, is the process of steeping coffee grounds in water at cool temperatures for an extended period.', 'America', 11,3.95,1.975,19.75);
insert into Coffee values(1011, 'Turkish', 'Turkish coffee is prepared by immersing the coffee grounds in water and heating until it just boils.', 'Turkey', 12,4.95,2.475,24.75);

-- 50 purchases
INSERT INTO Purchase VALUES (5000,9,2002,'9-01-2022',1004,0,1);
INSERT INTO Purchase VALUES (5001,4,2002,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5002,8,2001,'9-01-2022',1003,0,1);
INSERT INTO Purchase VALUES (5003,10,2002,'9-01-2022',1011,0,1);
INSERT INTO Purchase VALUES (5004,3,2002,'9-01-2022',1006,0,1);
INSERT INTO Purchase VALUES (5005,4,2002,'9-01-2022',1009,0,1);
INSERT INTO Purchase VALUES (5006,7,2002,'9-01-2022',1011,0,1);
INSERT INTO Purchase VALUES (5007,5,2000,'9-01-2022',1003,0,1);
INSERT INTO Purchase VALUES (5008,9,2000,'9-01-2022',1004,0,1);
INSERT INTO Purchase VALUES (5009,1,2001,'9-01-2022',1004,0,1);
INSERT INTO Purchase VALUES (5010,8,2002,'9-01-2022',1010,0,1);
INSERT INTO Purchase VALUES (5011,3,2000,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5012,2,2000,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5013,9,2001,'9-01-2022',1004,0,1);
INSERT INTO Purchase VALUES (5014,1,2000,'9-01-2022',1007,0,1);
INSERT INTO Purchase VALUES (5015,12,2002,'9-01-2022',1008,0,1);
INSERT INTO Purchase VALUES (5016,10,2000,'9-01-2022',1005,0,1);
INSERT INTO Purchase VALUES (5017,8,2000,'9-01-2022',1010,0,1);
INSERT INTO Purchase VALUES (5018,2,2001,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5019,5,2001,'9-01-2022',1001,0,1);
INSERT INTO Purchase VALUES (5020,10,2000,'9-01-2022',1001,0,1);
INSERT INTO Purchase VALUES (5021,3,2002,'9-01-2022',1008,0,1);
INSERT INTO Purchase VALUES (5022,8,2000,'9-01-2022',1008,0,1);
INSERT INTO Purchase VALUES (5023,12,2002,'9-01-2022',1010,0,1);
INSERT INTO Purchase VALUES (5024,5,2001,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5025,7,2000,'9-01-2022',1009,0,1);
INSERT INTO Purchase VALUES (5026,2,2000,'9-01-2022',1005,0,1);
INSERT INTO Purchase VALUES (5027,5,2002,'9-01-2022',1003,0,1);
INSERT INTO Purchase VALUES (5028,1,2002,'9-01-2022',1005,0,1);
INSERT INTO Purchase VALUES (5029,12,2000,'9-01-2022',1002,0,1);
INSERT INTO Purchase VALUES (5030,8,2001,'9-01-2022',1011,0,1);
INSERT INTO Purchase VALUES (5031,8,2000,'9-01-2022',1008,0,1);
INSERT INTO Purchase VALUES (5032,2,2001,'9-01-2022',1011,0,1);
INSERT INTO Purchase VALUES (5033,11,2002,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5034,9,2002,'9-01-2022',1009,0,1);
INSERT INTO Purchase VALUES (5035,12,2001,'9-01-2022',1000,0,1);
INSERT INTO Purchase VALUES (5036,1,2000,'9-01-2022',1007,0,1);
INSERT INTO Purchase VALUES (5037,6,2001,'9-01-2022',1002,0,1);
INSERT INTO Purchase VALUES (5038,9,2001,'9-01-2022',1004,0,1);
INSERT INTO Purchase VALUES (5039,2,2002,'9-01-2022',1005,0,1);
INSERT INTO Purchase VALUES (5040,2,2002,'9-01-2022',1005,0,1);
INSERT INTO Purchase VALUES (5041,3,2002,'9-01-2022',1006,0,1);
INSERT INTO Purchase VALUES (5042,4,2001,'9-01-2022',1008,0,1);
INSERT INTO Purchase VALUES (5043,10,2000,'9-01-2022',1004,0,1);
INSERT INTO Purchase VALUES (5044,2,2001,'9-01-2022',1001,0,1);
INSERT INTO Purchase VALUES (5045,7,2002,'9-01-2022',1009,0,1);
INSERT INTO Purchase VALUES (5046,2,2001,'9-01-2022',1010,0,1);
INSERT INTO Purchase VALUES (5047,3,2002,'9-01-2022',1010,0,1);
INSERT INTO Purchase VALUES (5048,6,2002,'9-01-2022',1011,0,1);
INSERT INTO Purchase VALUES (5049,1,2002,'9-01-2022',1009,1,0);

-- 3 promotions
insert into Promotion values(6000, 'Half-off Latte', '2022-11-15', '2022-11-20');
insert into Promotion values(6001, 'Free Cold brew', '2022-11-05', '2022-11-15');
insert into Promotion values(6002, 'Free Turkish', '2022-12-05', '2022-12-10');

insert into hasPromotion values(6000, 2001);
insert into promotionFor values(6000, 1000);
insert into offersCoffee values(1000, 2001);

select customerID, sum(price * purchasePortion) as purchases from (Coffee natural join Purchase) as coffeePurchases natural join (select * from Purchase where purchaseTime >= 'Sep 1, 2022') as purchasesAfter group by customerID order by purchases desc;