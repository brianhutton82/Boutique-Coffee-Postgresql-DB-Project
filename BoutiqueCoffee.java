import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.lang.StringBuilder;
import java.lang.Math;
import java.sql.*;

public class BoutiqueCoffee {

	private Connection connection;
	private Properties props;
	private Statement st;
	private String url = "jdbc:postgresql://localhost:5432/";
	private String username;
	private String password;

	public BoutiqueCoffee(){
		Scanner kbd = new Scanner(System.in);
		System.out.print("\nEnter username: ");
		username = kbd.next();
		System.out.print("Enter password: ");
		password = kbd.next();
		try {
			Class.forName("org.postgresql.Driver");
			props = new Properties();
			props.setProperty("user", username);
			props.setProperty("password", password);
			connection = DriverManager.getConnection(url, props);
			connection.setAutoCommit(false);
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	/*
		Task #1: Add a new store
		• Ask the user to supply all the necessary fields for the new store: name, storeType, gpsLong, and gpsLat.
		• Your program must print the appropriate prompts so that the user supplies the information one field at a time.
		• Produce an error message if a store with the same name already exists.
		• Assign a unique store number for the new store.
		• Insert all the supplied information and the store number into the database.
		• Display the store number as a confirmation of successfully adding the new store in the data
	*/
	public int addNewStore(String storeName, String storeType, float gpsLat, float gpsLong){
		int result = -1;
		int storeNumber = 0; // new store number generated in this method

		// was a valid storeType provided?
		if(storeType.equals("sitting") || storeType.equals("kiosk")){
			try{
				st = connection.createStatement();

				// generate a new store number
				String getAllStoreNumbers = "select storeID from Store;";
				ResultSet allStoreNumbers = st.executeQuery(getAllStoreNumbers);
				// get last store number
				while(allStoreNumbers.next()){
					int currentStoreNumber = allStoreNumbers.getInt("storeID");
					if(currentStoreNumber > storeNumber){
						storeNumber = currentStoreNumber;
					}
				}
				storeNumber += 1; // new store number
				allStoreNumbers.close();

				// check to see if store name already exists
				String getAllStoreNames = "select storeName from Store;";
				ResultSet allStoreNames = st.executeQuery(getAllStoreNames);
				boolean storeExists = false;
				while(allStoreNames.next()){
					String name = allStoreNames.getString("storeName");
					if(storeName.equals(name)){
						storeExists = true;
					}
				}
				allStoreNames.close();

				if(storeExists){
					System.out.println("\n***Store with name: " + storeName + " already exists!***\n");
				} else {
					// if all values provided are legit, insert this new store
					String insertStore ="insert into Store values(" + storeNumber + ", '" + storeName + "', '" + storeType + "', " + gpsLat + ", " + gpsLong + ");";
		        	//st.executeQuery(insertStore);
		        	st.executeUpdate(insertStore);

		        	// check to make sure store was inserted successfully
		        	String checkInsertWorked = "select storeID from Store where storeID = " + storeNumber + ";";
		        	ResultSet insertedStoreNumbers = st.executeQuery(checkInsertWorked);
		        	int resultStoreNumber = -1;
		        	while(insertedStoreNumbers.next()){
		        		resultStoreNumber = insertedStoreNumbers.getInt("storeID");
		        	}
		        	if(resultStoreNumber == storeNumber){
		        		result = storeNumber;
		        	}
		        	insertedStoreNumbers.close();
				}
				st.close();
        		connection.commit();
			} catch(Exception ex){
				System.err.println("\n***Failed to add store!***\n");
				ex.printStackTrace();
			}

		}
		return result;
	}


	/*
		Task #2: Add a new coffee to the BoutiqueCoffee menu/catalog
		• Ask the user to supply the name, description, country of origin, intensity, price, award points, and redeem points.
		• Assign a unique coffee ID for the new coffee.
		• Insert all the supplied information and the coffee ID into the database.
		• Display the coffee ID as a confirmation of successfully adding the new coffee in the database.
		Coffee(coffeeID int, coffeeName String, description String, countryOfOrigin String, intensity int, price float, rewardPoints float, redeemPoints float)
	*/
	public int addNewCoffee(String coffeeName, String description, String countryOfOrigin, int intensity, float price, float rewardPoints, float redeemPoints){
		int coffeeID = 0;
		try {
				st = connection.createStatement();

				// generate a new coffee ID
				String getAllCoffeeIDs = "select coffeeID from Coffee;";
				ResultSet allCoffeeIDs = st.executeQuery(getAllCoffeeIDs);
				// get last coffeeID
				while(allCoffeeIDs.next()){
					int currentCoffeeID = allCoffeeIDs.getInt("coffeeID");
					if(currentCoffeeID > coffeeID){
						coffeeID = currentCoffeeID;
					}
				}
				coffeeID += 1; // new coffee ID
				allCoffeeIDs.close();

				// if all values provided are legit, insert this new coffee
				String insertCoffee ="insert into Coffee values(" + coffeeID + ", '" + coffeeName + "', '" + description + "', '" + countryOfOrigin + "', " + intensity + ", " + price + ", " + rewardPoints + ", " + redeemPoints + ");";
				//st.executeQuery(insertCoffee);
				st.executeUpdate(insertCoffee);

				// check to make sure coffee was inserted successfully
				String checkInsertWorked = "select coffeeID from Coffee where coffeeID = " + coffeeID + ";";
				ResultSet insertedCoffeeIDs = st.executeQuery(checkInsertWorked);
				int resultCoffeeID = -1;
				while(insertedCoffeeIDs.next()){
					resultCoffeeID = insertedCoffeeIDs.getInt("coffeeID");
				}
				if(resultCoffeeID != coffeeID){
					coffeeID = -1;
				}
				insertedCoffeeIDs.close();
				st.close();
        		connection.commit();

		} catch(Exception e){
			System.out.println("\n***Unable to add new coffee!***\n");
			e.printStackTrace();
		}
		return coffeeID;
	}


	/*
	Task #3: Schedule a promotion for a coffee for a given period
	• Ask the user to supply the promotion name, start date, end date, and coffee ID.
	• Assign a unique promotion ID for the new promotion.
	• Insert all the supplied information and the promotion ID into the database.
	• Display the promotion ID as a confirmation of successfully scheduling the new promotion in the database.
	*/
	public int schedulePromotion(String promotionName, String promotionStartDate, String promotionEndDate, int coffeeID){
		int promotionNumber = 0;
		// insert into Promotion
		try {
			st = connection.createStatement();

			// get new promotion number
			String getPromoNums = "select promotionID from Promotion;";
			ResultSet allPromoNums = st.executeQuery(getPromoNums);
			while(allPromoNums.next()){
				int currentPromoNum = allPromoNums.getInt("promotionID");
				if(currentPromoNum > promotionNumber){
					promotionNumber = currentPromoNum;
				}
			}
			promotionNumber += 1;
			allPromoNums.close();

			String insertPromo = "insert into Promotion values(" + promotionNumber + ", '" + promotionName + "', '" + promotionStartDate + "', '" + promotionEndDate + "');";
			st.executeUpdate(insertPromo);

			// check to make sure Promo was inserted successfully
			String checkInsertWorked = "select promotionID from Promotion where promotionID = " + promotionNumber + ";";
			ResultSet insertedPromoIDs = st.executeQuery(checkInsertWorked);
			int resultPromoID = -1;
			while(insertedPromoIDs.next()){
				resultPromoID = insertedPromoIDs.getInt("promotionID");
			}
			if(resultPromoID != promotionNumber){
				promotionNumber = -1;
			}
			insertedPromoIDs.close();

			// insert into promotionFor(promotionID, coffeeID)
			String insertPromoFor = "insert into promotionFor values(" + promotionNumber + ", " + coffeeID + ");";
			st.executeUpdate(insertPromoFor);

			// ensure promotion was inserted into promotionFor table
			String checkPromo = "select promotionID from promotionFor where promotionID = " + promotionNumber + ";";
			ResultSet promotionForEntries = st.executeQuery(checkPromo);
			boolean promotionInTable = false;
			while(promotionForEntries.next()){
				if(promotionForEntries.getInt("promotionID") == promotionNumber){
					promotionInTable = true;
				}
			}
			promotionForEntries.close();

			if(!promotionInTable){
				System.out.println("\n***promotion with ID " + promotionNumber + " not inserted into promotionFor table!***\n");
				promotionNumber = -1;
			}

			st.close();
	    	connection.commit();
		} catch(Exception e){
			System.out.println("\n***Unable to schedule new promotion!***\n");
			e.printStackTrace();
		}	
		return promotionNumber;
	}


	/*
		Task #4: Add an offer/promotion to a store
		• Ask the user to supply the promotion ID and store ID.
		• Insert all the supplied information into the database.
		• Display the store ID as a confirmation of successfully offering the promotion in the database.
	*/
	public int addPromoToStore(int promotionID, int storeID){
		int storeNumber = 0;
		try{
			st = connection.createStatement();
			// hasPromotion(promotionID integer not null, storeID integer)

			// check & make sure promotionID exists
			boolean promoExists = false;
			String checkPromo = "select promotionID from Promotion where promotionID = " + promotionID + ";";
			ResultSet promos = st.executeQuery(checkPromo);
			while(promos.next()){
				if(promos.getInt("promotionID") == promotionID){
					promoExists = true;
				}
			}
			promos.close();

			// check & make sure store exists
			boolean storeExists = false;
			String checkStore = "select storeID from Store where storeID = " + storeID + ";";
			ResultSet stores = st.executeQuery(checkStore);
			while(stores.next()){
				if(stores.getInt("storeID") == storeID){
					storeExists = true;
				}
			}
			stores.close();

			// if the storeID & promotionID are valid, add to hasPromotion table
			if(promoExists && storeExists){
				String insertIntoHasPromotion = "insert into hasPromotion values(" + promotionID + ", " + storeID + ");";
				st.executeUpdate(insertIntoHasPromotion);
				storeNumber = storeID;
			}

			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n***Unable to add promo to store!***\n");
			e.printStackTrace();
		}
		return storeNumber;
	}


	/*
	Task #5: List all the stores with promotions
	• Ask the user to specify if the query should list the stores promoting any coffee or for a specific coffee.
	• In the latter case, the user should supply the coffee ID.
	• Display the stores offering the promotion or a message (e.g., ‘No stores are currently offering any promotions’ or ‘No stores are currently promoting this coffee’) if no stores have the specified promotions.
	*/
	public ArrayList<String> getStoresWithPromotions(int coffeeID){
		ArrayList<String> storesWithPromos = new ArrayList<String>();
		// if coffeeID is not 0, then user wants to list stores with promo for that coffee
		if(coffeeID > 0){
			try {
				st = connection.createStatement();
				String getStoreWithCoffee = "select storeName from Store natural join hasPromotion natural join promotionFor where coffeeID=" + coffeeID + ";";
				ResultSet storesWithCoffee = st.executeQuery(getStoreWithCoffee);
				while(storesWithCoffee.next()){
					String name = storesWithCoffee.getString("storeName");
					storesWithPromos.add(name);
				}
				storesWithCoffee.close();

				st.close();
				connection.commit();
			} catch(Exception e){
				System.out.println("\n***Error listing all stores with promotions!\n");
				e.printStackTrace();
			}
		} else {
			// otherwise just get stores with promos
			try {
				st = connection.createStatement();

				// get storeIDs with promos
				ArrayList<Integer> arr = new ArrayList<Integer>();
				String getStoresWithPromos = "select storeID from hasPromotion";
				ResultSet storesPromos = st.executeQuery(getStoresWithPromos);
				while(storesPromos.next()){
					int store = storesPromos.getInt("storeID");
					arr.add(store);
				}
				storesPromos.close();

				// for each store ID get store name
				for(int id : arr){
					String getStoreNames = "select storeName from Store where storeID = " + id + ";";
					ResultSet storeNames = st.executeQuery(getStoreNames);
					while(storeNames.next()){
						String strName = storeNames.getString("storeName");
						storesWithPromos.add(strName);
					}
					
				}

				st.close();
				connection.commit();
			} catch(Exception e){
				System.out.println("\n***Error listing all stores with promotions!\n");
				e.printStackTrace();
			}
		}
		return storesWithPromos;
	}


	/*
		Task #6: Check if a given store has promotions
		• Ask the user to specify if the query should list any promotions at the specified store or only the offers for a specific coffee.
		• In the first case, the user should supply the store ID. In the second case, the user should supply the store ID and coffee ID.
		• Display the promotions offered at the specified store or a message (e.g., ‘No promotions are currently offered at this store’ or ‘No promotions for this coffee are currently offered at this store’) if the provided store does not have the specified promotions.
	*/
	ArrayList<String> checkStoreForPromos(int storeID, int coffeeID){
		// if coffeeID == 0, then user wants promos for a specific coffee, otherwise list all promos for given store
		ArrayList<String> promos = new ArrayList<String>();
		try {
			st = connection.createStatement();

			if(coffeeID == 0){
				// get promotions from store with storeID
				String getPromoNames = "select promotionName from Store natural join hasPromotion natural join Promotion;";
				ResultSet promosAtStore = st.executeQuery(getPromoNames);
				while(promosAtStore.next()){
					String promoName = promosAtStore.getString("promotionName");
					promos.add(promoName);
				}
				promosAtStore.close();
			} else {
				String getStoreCoffeePromos = "select promotionName from Store natural join hasPromotion natural join Promotion natural join promotionFor where coffeeID = " + coffeeID + ";";
				ResultSet promosWithCoffee = st.executeQuery(getStoreCoffeePromos);
				while(promosWithCoffee.next()){
					String promoname = promosWithCoffee.getString("promotionName");
					promos.add(promoname);
				}
				promosWithCoffee.close();
			}
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n***Failed to check for store promos!***\n");
			e.printStackTrace();
		}
		return promos;
	}


	/*
		Task #7: Find the closest store(s) for a given location (GPS)
		• Ask the user to specify if the query should list any closest store or the closest stores offering a specific promotion.
		• In the first case, the user should supply a GPS location (lat, lon).
		• In the second case, the user should supply a GPS location (lat, lon) and promotion ID.
		• In order to compute the closest store(s), you must use the Euclidean Distance:
			d(p, q) = sqrt((qlat-plat)² + (qlon-plon)²)
		• Display the closest store(s) for the provided GPS location.
		• In the event of a tie, equidistant stores, all of the stores that are equidistantly the closest should be displayed.
	*/
	public ArrayList<String> getClosestStores(float gpsLAT, float gpsLON, int promoID){
		ArrayList<String> closestStores = new ArrayList<String>();
		String closestStoreName = null;
		float distance = Float.MAX_VALUE;
		try {
			st = connection.createStatement();
			
			// if promoID == 0, then user does not care about promo
			if(promoID == 0){
				String getStores = "select * from Store;";
				ResultSet stores = st.executeQuery(getStores);
				while(stores.next()){
					float lon = stores.getFloat("gpsLong");
					float lat = stores.getFloat("gpsLat");
					float tempDistance = (float)(Math.sqrt(Math.pow((lat - gpsLAT), 2) + Math.pow((lon - gpsLON), 2)));
					if(tempDistance < distance){
						distance = tempDistance;
						closestStoreName = stores.getString("storeName");
					}
				}
				// if we did find a close store, add it to array list
				if(distance < Float.MAX_VALUE){
					closestStores.add(closestStoreName);
				}
				stores.close();

				// check to see if any other stores were equidistant, if so add them to array list
				String getStores2 = "select * from Store;";
				ResultSet stores2 = st.executeQuery(getStores2);
				while(stores2.next()){
					float lon = stores2.getFloat("gpsLong");
					float lat = stores2.getFloat("gpsLat");
					float tempDistance = (float)(Math.sqrt(Math.pow((lat - gpsLAT), 2) + Math.pow((lon - gpsLON), 2)));
					if(tempDistance == distance && !stores2.getString("storeName").equals(closestStoreName)){
						closestStores.add(stores2.getString("storeName"));
					}
				}
				stores2.close();
				
			} else {
				// otherwise if promotion != 0, then user wants closest store with specific promotion
				String getStores = "select * from Store natural join hasPromotion where promotionID = " + promoID + ";";
				ResultSet stores = st.executeQuery(getStores);
				while(stores.next()){
					float lon = stores.getFloat("gpsLong");
					float lat = stores.getFloat("gpsLat");
					float tempDistance = (float)(Math.sqrt(Math.pow((lat - gpsLAT), 2) + Math.pow((lon - gpsLON), 2)));
					if(tempDistance < distance){
						distance = tempDistance;
						closestStoreName = stores.getString("storeName");
					}
				}
				// if we did find a close store, add it to array list
				if(distance < Float.MAX_VALUE){
					closestStores.add(closestStoreName);
				}
				stores.close();

				// check to see if any other stores were equidistant, if so add them to array list
				String getStores2 = "select * from Store natural join hasPromotion where promotionID = " + promoID + ";";
				ResultSet stores2 = st.executeQuery(getStores2);
				while(stores2.next()){
					float lon = stores2.getFloat("gpsLong");
					float lat = stores2.getFloat("gpsLat");
					float tempDistance = (float)(Math.sqrt(Math.pow((lat - gpsLAT), 2) + Math.pow((lon - gpsLON), 2)));
					if(tempDistance == distance && !stores2.getString("storeName").equals(closestStoreName)){
						closestStores.add(stores2.getString("storeName"));
					}
				}
				stores2.close();
			}
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n***Error computing closest store!***\n");
			e.printStackTrace();
		}
		return closestStores;
	}


	/*
		Task #8: Add/Set a loyalty level
		• Ask the user to specify the required fields for creating or updating a loyalty level: loyalty level name and booster factor.
		• If the member level doesn’t exist, the member level and its booster factor are inserted.
		• If the member level exists, the booster factor is updated.
		• Display the name of this loyalty level as a confirmation of successfully inserting or updating the loyalty level in the database.

	*/
	public String addOrSetLoyaltyLevel(String levelName, float boostFactor){
		String loyaltyLevel = null;
		try {
			st = connection.createStatement();

			// first see if levelName already exists
			boolean resultSetEmpty = false;
			try {
				String getAllLoyaltyLevels = "select * from LoyaltyLevel where levelName = '" + levelName + "';";
				ResultSet getLoyaltyLevels = st.executeQuery(getAllLoyaltyLevels);

				if(getLoyaltyLevels.next() == false){
					// if getLoyaltyLevels.next() is false, that levelName did not exist in the LoyaltyLevel table
					resultSetEmpty = true;
				}

				getLoyaltyLevels.close();
			} catch(Exception ex){
				resultSetEmpty = true;
			}

			// if levelName did not exist, try inserting new levelName, otherwise update levelName entry
			if(resultSetEmpty){
				try {
					String insertLoyaltyLevel = "insert into LoyaltyLevel values('" + levelName + "', " + boostFactor + ");";
					st.executeUpdate(insertLoyaltyLevel);
					loyaltyLevel = levelName;
				} catch(Exception exc){
					System.out.println("\n\t***Level name must be 'basic', 'bronze', 'silver', 'gold', 'platinum', or 'diamond'!***\n");
				}

			} else {
				try {
					String updateLoyaltyLevel = "update LoyaltyLevel set boostFactor = " + boostFactor + " where levelName = '" + levelName + "';";
					st.executeUpdate(updateLoyaltyLevel);
					loyaltyLevel = levelName;
				} catch(Exception exce){
					System.out.println("\n\t***Level name must be 'basic', 'bronze', 'silver', 'gold', 'platinum', or 'diamond'!***\n");
				}	
			}
			st.close();
			connection.commit();
		} catch (Exception e){
			System.out.println("\n***Error adding or setting loyalty level!***\n");
			e.printStackTrace();
		}
		return loyaltyLevel;
	}


	/*
		Task #9: Add a new customer
		• Ask the user to provide the required fields for creating a new customer: first name, last name, middle initial, day of birth, month of birth, phone number and phone type.
		• The new user’s loyalty level should be set to ‘basic’ since no reward points have been earned yet.
		• Display the customer ID as a confirmationof successfully adding the new customer in the database.
	*/
	public int addNewCustomer(String customerFirstName, String customerLastName, char customerMiddleName, String birthDay, String birthMonth, String phoneNumber, String phoneType){
		int customerID = 0;
		try{
			st = connection.createStatement();

			// generate a new customer ID
			String getAllCustomerIDs = "select customerID from Customer;";
			ResultSet allCustomerIDs = st.executeQuery(getAllCustomerIDs);
			// get last customerID
			while(allCustomerIDs.next()){
				int currentCustomerID = allCustomerIDs.getInt("customerID");
				if(currentCustomerID > customerID){
					customerID = currentCustomerID;
				}
			}
			customerID += 1; // new customerID
			allCustomerIDs.close();

			// if all values provided are legit, insert this new customer
			String insertCustomer ="insert into Customer values(" + customerID + ", 'basic', '" + customerFirstName + "', '" + customerLastName + "', '" + customerMiddleName + "', '" + birthDay + "', '" + birthMonth + "', '" + phoneNumber + "', '" + phoneType + "');";
			st.executeUpdate(insertCustomer);

			// check to make sure customer was inserted successfully
			String checkInsertWorked = "select customerID from Customer where customerID = " + customerID + ";";
			ResultSet insertedCustomerIDs = st.executeQuery(checkInsertWorked);
			int resultCustomerID = -1;
			while(insertedCustomerIDs.next()){
				resultCustomerID = insertedCustomerIDs.getInt("customerID");
			}
			if(resultCustomerID != customerID){
				customerID = -1;
			}
			insertedCustomerIDs.close();
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n***Unable to insert Customer!***\n");
			e.printStackTrace();
		}
		return customerID;
	}


	/*
		Task #10: Show the loyalty points of a customer
		• Ask the user to supply the customer ID to display the total loyalty points for.
	*/
	public float getCustomerLoyaltyPoints(int customerID){
		float totalPointsEarned = 0;
		try {
			st = connection.createStatement();
			String getCustomer = "select totalPointsEarned from Customer where customerID = " + customerID + ";";
			ResultSet points = st.executeQuery(getCustomer);
			while(points.next()){
				totalPointsEarned = points.getFloat("totalPointsEarned");
			}
			points.close();
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n\t***Error fetching customer loyalty points!***\n");
		}
		return totalPointsEarned;
	}


	/*
		Task #11: Produce a ranked list of the most loyal customers
	*/
	public ArrayList<String> getRankedCustomerList(){
		ArrayList<String> rankedList = new ArrayList<String>();
		try {
			st = connection.createStatement();
			String getCustomers = "select customerID, customerFirstName, customerLastName, totalPointsEarned from Customer order by totalPointsEarned desc;";
			ResultSet customers = st.executeQuery(getCustomers);
			while(customers.next()){
				int customerID = customers.getInt("customerID");
				String customerFirstName = customers.getString("customerFirstName");
				String customerLastName = customers.getString("customerLastName");
				float totalPoints = customers.getFloat("totalPointsEarned");
				StringBuilder sb = new StringBuilder("ID: " + String.valueOf(customerID));
				sb.append(", " + customerFirstName);
				sb.append(" " + customerLastName);
				sb.append(", total points: " + String.valueOf(totalPoints));
				String customer = sb.toString();
				rankedList.add(customer);
			}
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n\n\t***Failed to get ranked list of customers!***\n");
		}

		return rankedList;
	}

	/*
		Task #12: Add a purchase.
		• Ask the user to supply the customer ID, store ID, time of purchase, purchased coffee ID(s), purchase quantitie(s), and redeem quantitie(s).
		• If the purchase uses points, the purchase should fail if the customer does not have enough.
		• Display the purchase ID of this purchase to as a confirmation of successfully completing the purchase in the database.
		Purchase (int purchaseID, int customerID, int storeNumber, time PurchaseTime, int coffeeID, float redeemPortion, float purchasePortion)
	*/
	public int addPurchase(int customerID, int storeNumber, String purchaseTime, int coffeeID, float redeemPortion, float purchasePortion){
		int purchaseID = 0;
		boolean canPurchase = false;
		try {
			st = connection.createStatement();

			// if customer is redeeming, check if they have enough points for coffee with coffeeID
			if(redeemPortion > 0){

				// first get this coffees required redeem points
				String getCoffeeRedeemPoints = "select reedeemPoints from Coffee where coffeeID = " + coffeeID + ";";
				ResultSet coffeeRedeemPoints = st.executeQuery(getCoffeeRedeemPoints);
				int coffeePoints = 0;
				while(coffeeRedeemPoints.next()){
					coffeePoints = coffeeRedeemPoints.getInt("redeemPoints");
				}
				coffeeRedeemPoints.close();

				// next get customers awarded points
				String getCustomerPoints = "select totalPointsEarned from Customer where customerID = " + customerID + ";";
				ResultSet customerPoints = st.executeQuery(getCustomerPoints);
				float customerRewardPoints = 0;
				while(customerPoints.next()){
					customerRewardPoints = customerPoints.getFloat("totalPointsEarned");
				}
				customerPoints.close();

				// if customer has enough points for the coffee then they can redeem it
				if(customerRewardPoints >= coffeePoints){
					canPurchase = true;
				}
			}  else if(purchasePortion > 0 && redeemPortion == 0){
					canPurchase = true;

			} else {
				System.out.println("\n***Customer can use either redeem points or purchase coffee, not both!***\n");
			}

			if(canPurchase){
				// compute new purchase ID
				String getPurchaseIDs = "select purchaseID from Purchase;";
				ResultSet allPurchaseIDs = st.executeQuery(getPurchaseIDs);
				while(allPurchaseIDs.next()){
					purchaseID = allPurchaseIDs.getInt("purchaseID");
				}
				purchaseID += 1;
				allPurchaseIDs.close();
				if(purchaseID > 0){
					// insert this purchase
					String insertPurchase = "insert into Purchase values(" + purchaseID + ", " + customerID + ", " + storeNumber + ", '" + purchaseTime + "', " + coffeeID + ", " + redeemPortion + ", " + purchasePortion + ");";
					st.executeUpdate(insertPurchase);
				}
			}
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n***Unable to insert purchase!***\n");
			e.printStackTrace();
		}
		return purchaseID;
	}


	/*
		Task #13: List the full coffee information in the BoutiqueCoffee menu/catalog
		• Display the full coffee information offered in the BoutiqueCoffee menu/catalog or a message (e.g., ‘No items are currently offered by BoutiqueCoffee’) if no coffees are currently offered.
	*/
	public ArrayList<String> listCoffeeMenu(){
		ArrayList<String> menu = new ArrayList<String>();
		try {
			st = connection.createStatement();
			String getCoffees = "select * from Coffee;";
			ResultSet coffees = st.executeQuery(getCoffees);
			while(coffees.next()){
				int coffeeID = coffees.getInt("coffeeID");
				String coffeeName = coffees.getString("coffeeName");
				//String description = coffees.getString("description");
				String countryOfOrigin = coffees.getString("countryOfOrigin");
				int intensity = coffees.getInt("intensity");
				float price = coffees.getFloat("price");
				float rewardPoints = coffees.getFloat("rewardPoints");
				float redeemPoints = coffees.getFloat("redeemPoints");
				StringBuilder sb = new StringBuilder("ID: " + String.valueOf(coffeeID) + ", ");
				sb.append("name: " + coffeeName + ", ");
				//sb.append("description: " + description + ", ");
				sb.append("country: " + countryOfOrigin + ", ");
				sb.append("intensity: " + String.valueOf(intensity) + ", ");
				sb.append("price: $" + String.valueOf(price) + ", ");
				sb.append(", reward points: " + String.valueOf(rewardPoints) + ", ");
				sb.append("redeem points: " + String.valueOf(redeemPoints));
				String coffeeMenuEntry = sb.toString();
				menu.add(coffeeMenuEntry);
			}
			coffees.close();
			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n\t***Error fetching Coffee Menu!***\n");
		}
		return menu;
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		BoutiqueCoffee bc = new BoutiqueCoffee();
		String command = null;
		Scanner kbd = new Scanner(System.in);
		do {
			System.out.println("\n---List of Commands---\n\n• insertStore: inserts new store\n• removeStore: removes store\n• listStoresWithPromos: list all stores with promotions\n• insertCoffee: adds new coffee\n• insertCustomer: inserts new customer\n• insertPurchase: insert new purhcase\n• insertPromotion: schedule a promotion for a coffee\n• addPromoToStore: add a promo to a store\n• checkStorePromos: check if a given store has promotions\n• getClosestStores: get closest stores to your lat & long\n• setLoyaltyLevel: add or update loyalty level\n• getLoyaltyPoints: get total loyalty points for customer\n• getRankedList: get ranked list of most loyal customers\n• listCoffeeMenu: list BoutiqueCoffee menu\n• ...\n• quit: closes DB connection and ends program");
			System.out.print("\nenter command: ");
			command = kbd.next();
			switch(command){
				case "insertStore":
					System.out.print("Store name: ");
					String storeName = kbd.next();
					System.out.print("Store type: ");
					String storeType = kbd.next();
					System.out.print("GPS Longitude: ");
					float gpsLong = kbd.nextFloat();
					System.out.print("GPS Latitutde: ");
					float gpsLat = kbd.nextFloat();
					int storeNumber = bc.addNewStore(storeName, storeType, gpsLong, gpsLat);
					if(storeNumber > 0){
						System.out.println("\n\tAdded store " + storeName + " with store number " + storeNumber + " successfully!");
					} else {
						System.out.println("\n\tFailed to add " + storeName + " to Store table");
					}
					break;
				case "removeStore":
					System.out.println("\n***PLACEHOLDER FOR REMOVE STORE!***\n");
					break;
				case "insertCoffee":
					System.out.print("Coffee name: ");
					String coffeeName = kbd.next();
					System.out.print("Coffee description: ");
					String description = kbd.next();
					System.out.print("Coffee country of origin: ");
					String countryOfOrigin = kbd.next();
					System.out.print("Intensity: ");
					int intensity = kbd.nextInt();
					System.out.print("Price: ");
					float price = kbd.nextFloat();
					System.out.print("Reward points: ");
					float rewardPoints = kbd.nextFloat();
					System.out.print("Redeem points: ");
					float redeemPoints = kbd.nextFloat();
					int coffeeID = bc.addNewCoffee(coffeeName, description, countryOfOrigin, intensity, price, rewardPoints, redeemPoints);
					if (coffeeID <= 0){
						System.out.println("\n\tFailed to add Coffee " + coffeeName + " to Coffee table!");
					} else {
						System.out.println("\n\tSuccessfully added coffee with name: " + coffeeName + " and ID: " + coffeeID + "!");
					}
					break;
				case "insertCustomer":
					System.out.print("first name: ");
					String customerFirstName = kbd.next();
					System.out.print("last name: ");
					String customerLastName = kbd.next();
					System.out.print("middle initial: ");
					char customerMiddleName = kbd.next().charAt(0);
					System.out.print("day of birth (number): ");
					String birthDay = kbd.next();
					System.out.print("month of birth ('Mon' format): ");
					String birthMonth = kbd.next();
					System.out.print("phone number (one string without hyphens): ");
					String phoneNumber = kbd.next();
					System.out.print("phone type: ");
					String phoneType = kbd.next();
					int customerID = bc.addNewCustomer(customerFirstName, customerLastName, customerMiddleName, birthDay, birthMonth, phoneNumber, phoneType);
					if (customerID <= 0){
						System.out.println("\n\t Failed to add customer: " + customerFirstName + ", to Customer table!\n");
					} else {
						System.out.println("\n\tSuccessfully added customer with name: " + customerFirstName + ", and ID: " + customerID +", to Customer table!");
					}
					break;
				case "insertPurchase":
					System.out.print("customer ID: ");
					int purchaseCustomerID = kbd.nextInt();
					System.out.print("store number: ");
					int purchaseStoreNumber = kbd.nextInt();
					System.out.print("time of purchase (hh:mm:ss format): ");
					String purchaseTime = kbd.next();
					System.out.print("coffee ID: ");
					int purchaseCoffeeID = kbd.nextInt();
					System.out.print("redeem portion: ");
					float redeemPortion = kbd.nextFloat();
					System.out.print("purchase portion: ");
					float purchasePortion = kbd.nextFloat();
					int purchaseID = bc.addPurchase(purchaseCustomerID, purchaseStoreNumber, purchaseTime, purchaseCoffeeID, redeemPortion, purchasePortion);
					if (purchaseID > 0){
						System.out.println("\n\tSuccessfully inserterted purchase with ID: " + purchaseID + "!");
					} else {
						System.out.println("\n\tFailed to insert purchase!");
					}
					break;

				case "insertPromotion":
					System.out.print("promotion name: ");
					String promotionName = kbd.next();
					System.out.print("promotion start date ('YYYY-MM-DD' format): ");
					String promotionStartDate = kbd.next();
					System.out.print("promotion end date ('YYYY-MM-DD' format): ");
					String promotionEndDate = kbd.next();
					System.out.print("coffee ID: ");
					int promoCoffeeID = kbd.nextInt();
					int promoNum = bc.schedulePromotion(promotionName, promotionStartDate, promotionEndDate, promoCoffeeID);
					if(promoNum <= 0){
						System.out.println("\n\tFailed to add promotion with ID: " + promoNum + "to Promotion table!");
					} else {
						System.out.println("\n\tSuccessfully added promotion with promo number: " + promoNum + " to Promotion table!");
					}
					break;
				case "addPromoToStore":
					System.out.print("promotion ID: ");
					int promoID = kbd.nextInt();
					System.out.print("store ID: ");
					int storePromoID = kbd.nextInt();
					int addedPromoToStore = bc.addPromoToStore(promoID, storePromoID);
					if(addedPromoToStore == storePromoID){
						System.out.println("\n\tSuccessfully added promotion ID: " + promoID + " to store with ID: " + addedPromoToStore + "!");
					} else {
						System.out.println("\n\tFailed to add promotion with ID: " + promoID + " to store with ID: " + storePromoID + "!");
					}
					break;
				case "listStoresWithPromos":
					System.out.print("enter coffee ID (0 if you do not want to list promo for specific coffee): ");
					int coffeePromoID = kbd.nextInt();
					ArrayList<String> storesWithPromos = bc.getStoresWithPromotions(coffeePromoID);
					if(storesWithPromos.isEmpty()){
						System.out.println("\n\tNo stores are currently offering any promotions!");
					} else {
						System.out.println("\n\t--- Stores with Promos ---\n");
						for(String strName : storesWithPromos){
							System.out.println("\t• " + strName);
						}
						System.out.println();
					}

					break;
				case "checkStorePromos":
					System.out.print("store ID: ");
					int storepromoid = kbd.nextInt();
					System.out.print("coffee ID (0 if you want all coffee promos): ");
					int coffeepromoid = kbd.nextInt();
					ArrayList<String> storespromos = bc.checkStoreForPromos(storepromoid, coffeepromoid);
					if(storespromos.isEmpty()){
						System.out.println("\n\tNo promotions at this store or for this coffee are currently offered!");
					} else {
						System.out.println("\n\t--- Promos at Store " + storepromoid + " ---\n");
						for(String strname : storespromos){
							System.out.println("\t• " + strname);
						}
						System.out.println();
					}
					break;

				case "getClosestStores":
					System.out.print("your gps longitude: ");
					float longitude = kbd.nextFloat();
					System.out.print("your gps latitude: ");
					float latitude = kbd.nextFloat();
					System.out.print("promotion ID (if not concerned about promotions, enter 0): ");
					int storepromo_id = kbd.nextInt();
					ArrayList<String> closeststores = bc.getClosestStores(latitude, longitude, storepromo_id);
					System.out.println("\n\t--- Closest Stores ---\n");
					for(String store : closeststores){
						System.out.println("\t• " + store);
					}
					break;
				case "setLoyaltyLevel":
					System.out.print("level name: ");
					String levelName = kbd.next();
					System.out.print("boost factor: ");
					Float boostFactor = kbd.nextFloat();
					String resultLevelName = bc.addOrSetLoyaltyLevel(levelName, boostFactor);
					if(resultLevelName != null){
						System.out.println("\n\tlevel name: " + levelName + " added/updated successfully!\n");
					} else {
						System.out.println("\n\tfailed to add: " + levelName + " to LoyaltyLevel table!\n");
					}
					
					break;
				case "getLoyaltyPoints":
					System.out.print("customer ID: ");
					int custIDforPoints = kbd.nextInt();
					float lpoints = bc.getCustomerLoyaltyPoints(custIDforPoints);
					System.out.println("\n\ttotal points for customer with ID: " + custIDforPoints + " = " + lpoints + " points\n");
					break;
				case "getRankedList":
					ArrayList<String> rankedList = bc.getRankedCustomerList();
					if(rankedList.isEmpty()){
						System.out.println("\n\t***No customers in Customers table!***\n");
					} else {
						System.out.println("\n\t--- List of Loyal Customers ---\n");
						for(String loyalCustomer : rankedList){
							System.out.println("\t• " + loyalCustomer);
						}
						System.out.println();
					}
					break;
				case "listCoffeeMenu":
					ArrayList<String> menu = bc.listCoffeeMenu();
					if(menu.isEmpty()){
						System.out.println("\n\tNo items are currently offered by BoutiqueCoffee!\n");
					} else {
						System.out.println("\n\t--- Boutique Coffee Menu ---\n");
						for(String menuEntry : menu){
							System.out.println("\t• " + menuEntry);
						}
					}
					break;
				case "quit":
					System.out.println("\n***Goodbye!***\n");
					break;
				default:
					System.out.println("\n***Entered invalid command, try again.***\n");
			}
		} while(!command.equals("quit"));
	}
}