
import java.util.Properties;
import java.util.Scanner;
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
		System.out.print("Enter username: ");
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

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		BoutiqueCoffee bc = new BoutiqueCoffee();
		String command = null;
		Scanner kbd = new Scanner(System.in);
		do {
			System.out.println("\n---List of Commands---\n\n• insertStore: inserts new store\n• removeStore: removes store\n• insertCoffee: adds new coffee\n• insertCustomer: inserts new customer\n• ...\n• quit: closes DB connection and ends program");
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
				case "quit":
					System.out.println("\n***Goodbye!***\n");
					break;
				default:
					System.out.println("\n***Entered invalid command, try again.***\n");
			}
		} while(!command.equals("quit"));
	}
}