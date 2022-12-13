import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDate;
import java.lang.StringBuilder;
import java.lang.Math;
import java.sql.*;

public class BCDriver {

	public static boolean validateInputs(ArrayList<String> s) {
		boolean result = true;
		for(String entry : s) {
			entry = entry.toLowerCase();
			if (entry.contains("drop") || entry.contains("delete") || entry.contains("update") || entry.contains("select")) {
				result = false;
			}
		}
		return result;
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		BoutiqueCoffee bc = new BoutiqueCoffee();
		String command = null;
		Scanner kbd = new Scanner(System.in);
		ArrayList<String> validate = new ArrayList<String>();

		do {

			System.out.println("\n---List of Commands---\n\n• insertStore: inserts new store\n• listStoresWithPromos: list all stores with promotions\n• insertCoffee: adds new coffee\n• insertCustomer: inserts new customer\n• insertPurchase: insert new purhcase\n• insertPromotion: schedule a promotion for a coffee\n• addPromoToStore: add a promo to a store\n• checkStorePromos: check if a given store has promotions\n• getClosestStores: get closest stores to your lat & long\n• setLoyaltyLevel: add or update loyalty level\n• getLoyaltyPoints: get total loyalty points for customer\n• getRankedList: get ranked list of most loyal customers\n• listCoffeeMenu: list BoutiqueCoffee menu\n• listCoffeeIntensity: list IDs & names of coffees with specified intensity\n• listTopKstores: List top k stores having highest revenue for the past x months\n• listTopCustomerIDs: List top k customers who have spent the most money within x months\n• quit: closes DB connection and ends program");

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

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(storeName);
					validate.add(storeType);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

					int storeNumber = bc.addNewStore(storeName, storeType, gpsLong, gpsLat);
					if(storeNumber > 0){
						System.out.println("\n\tAdded store " + storeName + " with store number " + storeNumber + " successfully!");
					} else {
						System.out.println("\n\tFailed to add " + storeName + " to Store table");
					}
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

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(coffeeName);
					validate.add(description);
					validate.add(countryOfOrigin);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

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

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(customerFirstName);
					validate.add(customerLastName);
					validate.add(birthDay);
					validate.add(birthMonth);
					validate.add(phoneNumber);
					validate.add(phoneType);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

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
					System.out.print("time of purchase (mm-dd-yyyy format): ");
					String purchaseTime = kbd.next();
					System.out.print("coffee ID: ");
					int purchaseCoffeeID = kbd.nextInt();
					System.out.print("redeem portion: ");
					float redeemPortion = kbd.nextFloat();
					System.out.print("purchase portion: ");
					float purchasePortion = kbd.nextFloat();

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(purchaseTime);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

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

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(promotionName);
					validate.add(promotionStartDate);
					validate.add(promotionEndDate);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

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

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(levelName);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

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

				case "listCoffeeIntensity":
					System.out.print("keyword one: ");
					String keywordOne = kbd.next();
					System.out.print("keyword two: ");
					String keywordTwo = kbd.next();
					System.out.print("intensity (1 ≤ n ≤ 12): ");
					int intense = kbd.nextInt();

					// ensure user input does not contain any keywords
					validate.clear();
					validate.add(keywordOne);
					validate.add(keywordTwo);
					if (!validateInputs(validate)) {
						System.out.println("\t***Your input contains a SQL keyword, exiting!***");
						System.exit(-1);
					}

					ArrayList<String> coffeeIntensityList = bc.listCoffeeWithIntensity(keywordOne, keywordTwo, intense);
					if(coffeeIntensityList.isEmpty()){
						System.out.println("\n\t\nNo coffees satisfied these conditions!\n");
					} else {
						System.out.println("\n\t--- Coffees with Intensity Level " + String.valueOf(intense) + " ---\n");
						for(String entry : coffeeIntensityList){
							System.out.println("\t• " + entry);
						}
					}
					break;

				case "listTopKstores":
					System.out.print("number of stores to list: ");
					int k = kbd.nextInt();
					System.out.print("number of months to span: ");
					int x = kbd.nextInt();
					ArrayList<String> topkstores = bc.listTopKstores(k, x);
					if(topkstores.isEmpty()){
						System.out.println("\n\t*** An error occurred when trying to list the top " + k + " stores!***\n");
					} else {
						System.out.println("\n---Top " + k + " Stores---");
						for(String entry : topkstores) {
							System.out.println("• " + entry);
						}
					}
					break;

				case "listTopCustomerIDs":
					System.out.print("number of customers to list: ");
					int kCustomers = kbd.nextInt();
					System.out.print("number of months to span: ");
					int xMonths = kbd.nextInt();
					ArrayList<String> topkcustomers = bc.listTopCustomerIDs(kCustomers, xMonths);
					if (topkcustomers.isEmpty()) {
						System.out.println("\n\t*** An error occurred when trying to list the top " + kCustomers + " customers!***\n");
					} else {
						System.out.println("\n---Top " + kCustomers + " Customers---");
						for(String cust : topkcustomers){
							System.out.println("• " + cust);
						}
					}
					break;

				case "quit":
					System.out.println("\n***Goodbye!***\n");
					break;
				default:
					System.out.println("\n***Entered invalid command, exiting!***\n");
					System.exit(-1);
			}
		} while(!command.equals("quit"));
	}
}