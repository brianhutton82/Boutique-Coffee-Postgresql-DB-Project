
import java.util.Properties;
import java.util.Scanner;
import java.util.Date;
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
				String getAllStoreNumbers = "select storeNumber from Store;";
				ResultSet allStoreNumbers = st.executeQuery(getAllStoreNumbers);
				// get last store number
				while(allStoreNumbers.next()){
					int currentStoreNumber = allStoreNumbers.getInt("storeNumber");
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
		        	String checkInsertWorked = "select storeNumber from Store where storeNumber = " + storeNumber + ";";
		        	ResultSet insertedStoreNumbers = st.executeQuery(checkInsertWorked);
		        	int resultStoreNumber = -1;
		        	while(insertedStoreNumbers.next()){
		        		resultStoreNumber = insertedStoreNumbers.getInt("storeNumber");
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
			String getPromoNums = "select promotionNumber from Promotion;";
			ResultSet allPromoNums = st.executeQuery(getPromoNums);
			while(allPromoNums.next()){
				int currentPromoNum = allPromoNums.getInt("promotionNumber");
				if(currentPromoNum > promotionNumber){
					promotionNumber = currentPromoNum;
				}
			}
			promotionNumber += 1;
			allPromoNums.close();

			String insertPromo = "insert into Promotion values(" + promotionNumber + ", '" + promotionName + "', '" + promotionStartDate + "', '" + promotionEndDate + "');";
			st.executeUpdate(insertPromo);

			// check to make sure Promo was inserted successfully
			String checkInsertWorked = "select promotionNumber from Promotion where promotionNumber = " + promotionNumber + ";";
			ResultSet insertedPromoIDs = st.executeQuery(checkInsertWorked);
			int resultPromoID = -1;
			while(insertedPromoIDs.next()){
				resultPromoID = insertedPromoIDs.getInt("promotionNumber");
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
			String checkPromo = "select promotionNumber from Promotion where promotionNumber = " + promotionID + ";";
			ResultSet promos = st.executeQuery(checkPromo);
			while(promos.next()){
				if(promos.getInt("promotionNumber") == promotionID){
					promoExists = true;
				}
			}
			promos.close();

			// check & make sure store exists
			boolean storeExists = false;
			String checkStore = "select storeNumber from Store where storeNumber = " + storeID + ";";
			ResultSet stores = st.executeQuery(checkStore);
			while(stores.next()){
				if(stores.getInt("storeNumber") == storeID){
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
					// (purchaseID, customerID, storeNumber, PurchaseTime, coffeeID, redeemPortion, purchasePortion)
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


	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		BoutiqueCoffee bc = new BoutiqueCoffee();
		String command = null;
		Scanner kbd = new Scanner(System.in);
		do {
			System.out.println("\n---List of Commands---\n\n• insertStore: inserts new store\n• removeStore: removes store\n• insertCoffee: adds new coffee\n• insertCustomer: inserts new customer\n• insertPurchase: insert new purhcase\n• insertPromotion: schedule a promotion for a coffee\n• addPromoToStore: add a promo to a store\n• ...\n• quit: closes DB connection and ends program");
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
					// <-------------------------------------------- HERE ---------------------------------
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