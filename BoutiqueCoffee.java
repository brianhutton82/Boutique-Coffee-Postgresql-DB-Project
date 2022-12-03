import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDate;
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


	/*
		Task #14: List the IDs and names of all coffees of a specified intensity and each of which has two specified keywords in their names (i.e., both keywords in its name)
		• Ask the user to supply the intensity and both keywords.
		• Display coffee IDs of the provided intensity, each of which has both keywords in the coffee’s name, or a message (e.g., ‘No coffees satisfied these conditions’) if no coffees satisfy the provided intensity or contain both keywords in the coffee’s name.
	*/
	public ArrayList<String> listCoffeeWithIntensity(String keywordOne, String keywordTwo, int intensity){
		ArrayList<String> coffeeIntensityList = new ArrayList<String>();
		try {
			st = connection.createStatement();

			String getCoffees = "select coffeeID, coffeeName from Coffee where intensity = " + intensity + " and coffeeName like '%" + keywordOne + "%'" + " and coffeeName like '%" + keywordTwo + "%';";
			ResultSet coffees = st.executeQuery(getCoffees);
			while(coffees.next()){
				int id = coffees.getInt("coffeeID");
				String name = coffees.getString("coffeeName");
				StringBuilder sb = new StringBuilder("ID: " + String.valueOf(id) + ", coffee name: " + name);
				coffeeIntensityList.add(sb.toString());
			}
			coffees.close();

			st.close();
			connection.commit();
		} catch(Exception e){
			System.out.println("\n\t***Failed to fetch coffees with specified intensty & keywords***\n");
		}
		return coffeeIntensityList;
	}


	/*
		Task #15: List the IDs of the top K stores having the highest revenue for the past X months
		• Ask the user to supply: k the K in top K, and x the timespan of month(s).
		• Revenue is defined as the sum of money that the customer paid for the coffees.
		• 1 month is defined as 30 days counting back starting from the current date time.
		• Display the top K stores in the past X months in sorted order, with the store ID of the store having the highest revenue at the head.
		• If multiple stores have the same revenue, their order in the returned result can be arbitrary.
		• If multiple stores have the same revenue in the Kth highest revenue position, their store IDs should all be returned.
	*/
	public ArrayList<String> listTopKstores(int k, int x) {

		ArrayList<String> topKstores = new ArrayList<String>();
		Hashtable<Integer, Float> storeTotalRevenues = new Hashtable<Integer, Float>();
		Hashtable<Integer, String> storeNames = new Hashtable<Integer, String>();

		try {

			st = connection.createStatement();

			// get current date from Clock table
			LocalDate today = null;
			LocalDate dateLimit = null;
			String getCurrentDate = "select extract(month from(select p_date from Clock)) as month, extract(day from (select p_date from Clock)) as day, extract(year from (select p_date from Clock)) as year;";
			ResultSet currentDate = st.executeQuery(getCurrentDate);
			while(currentDate.next()){
				int year = currentDate.getInt("year");
				int month = currentDate.getInt("month");
				int day = currentDate.getInt("day");
				today = LocalDate.of(year, month, day);
			}
			currentDate.close();

			// if clock table was initialized, figure out how far back we should look
			if (today != null) {
				dateLimit = today.minusMonths(x);
			}

			// get all store names & IDs
			String getAllStores = "select storeID, storeName from Store;";
			ResultSet storeIDs = st.executeQuery(getAllStores);
			while(storeIDs.next()){
				int storeID = storeIDs.getInt("storeID");
				String storeName = storeIDs.getString("storeName");
				storeNames.put(storeID, storeName);
			}
			storeIDs.close();

			// compute total dollar value of all purchases within x months for each store
			String getStoreRevenues = "select storeID, sum(price * purchasePortion) as purchases from (Coffee natural join Purchase) as coffeePurchases natural join (select * from Purchase where purchaseTime >= '" + dateLimit.toString() + "') as purchasesAfter group by storeID order by purchases;";
			ResultSet storeRevenues = st.executeQuery(getStoreRevenues);
			while(storeRevenues.next()){
				int storeID = storeRevenues.getInt("storeID");
				float purchases = storeRevenues.getFloat("purchases");
				storeTotalRevenues.put(storeID, purchases);
			}
			storeRevenues.close();

			// construct string containing: storeID, storeName, totalRevenue
			Enumeration<Integer> e = storeTotalRevenues.keys();
			while (e.hasMoreElements()) {
				int storeID = e.nextElement();
				float revenue = storeTotalRevenues.get(storeID);
				String name = storeNames.get(storeID);
				StringBuilder sb = new StringBuilder("ID: " + storeID);
				sb.append(" name: " + name);
				sb.append(" revenue: " + revenue);
				topKstores.add(sb.toString());
			}

			st.close();
			connection.commit();
		} catch(Exception e) {
			System.out.println("\n\t***Failed to fetch the top " + String.valueOf(k) + " stores with the highest revenue!***\n");
		}
		return topKstores;
	}

	/*
		Task #16: List the IDs of the top K customers having spent the most money buying coffee in the past X months
		• Ask the user to supply: k the K in top K, and x the timespan of month(s).
		• 1 month is defined as 30 days counting back starting from the current date time.
		• Display the top K customers in the past X months in sorted order, with the customer ID of the customer having spent the most money at the head.
		• If multiple customers have the same spending, their order in the returned result can be arbitrary.
		• If multiple customers have the same sp
	*/


}