import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDate;
import java.lang.StringBuilder;
import java.lang.Math;
import java.sql.*;
import java.util.Random;

import javax.lang.model.util.ElementScanner6;


public class BCBenchmark 
{
    static String getAlphaNumericString(int n)
    {
        
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        
        for (int i = 0; i < n; i++) {
        
        // generate a random number between
        // 0 to AlphaNumericString variable length
        int index
            = (int)(AlphaNumericString.length()
            * Math.random());
        
        // add Character one by one in end of sb
        sb.append(AlphaNumericString
            .charAt(index));
        }
        
        return sb.toString();
        
        
    }
    public static void main(String[] args)
    {
        BoutiqueCoffee bc = new BoutiqueCoffee();
        String query;
        int times = 0;
        Scanner sc = new Scanner(System.in);
    do{
        System.out.println("\n---List of Commands---\n\n• insertStore: inserts new store\n• listStoresWithPromos: list all stores with promotions\n• insertCoffee: adds new coffee\n• insertCustomer: inserts new customer\n• insertPurchase: insert new purhcase\n• insertPromotion: schedule a promotion for a coffee\n• addPromoToStore: add a promo to a store\n• checkStorePromos: check if a given store has promotions\n• getClosestStores: get closest stores to your lat & long\n• setLoyaltyLevel: add or update loyalty level\n• getLoyaltyPoints: get total loyalty points for customer\n• getRankedList: get ranked list of most loyal customers\n• listCoffeeMenu: list BoutiqueCoffee menu\n• listCoffeeIntensity: list IDs & names of coffees with specified intensity\n• listTopKstores: List top k stores having highest revenue for the past x months\n• listTopCustomerIDs: List top k customers who have spent the most money within x months\n• quit: closes DB connection and ends program");

        System.out.println("Which function would you like to test?"); 
        query = sc.nextLine();
        System.out.println("How mayn times would you like to test it?");
        times = sc.nextInt(); 

        switch(query)
        {
            case "insertStore":
            {
                for(int i = 0; i < times; i++)
                {
                String storeName = getAlphaNumericString(10);
                String storeType = "sitting";
                float gpsLat = (float) Math.random()*10+1;
                float gpsLong = (float) Math.random()*10+1;
                    bc.addNewStore(storeName, storeType, gpsLat, gpsLong);
                }
            }
            case "insertCoffee":
            {
                for(int i = 0; i < times; i++)
                {
                String coffeeName = getAlphaNumericString(10);
                String description = getAlphaNumericString(10);
                String countryOfOrigin = getAlphaNumericString(10);
                int intensity = (int) Math.random()*11 + 1;
                float price = (float) Math.random()*5+1;
                float rewardPoints = (float) Math.random()*10+1;
                float redeemPoints = (float) Math.random()*10+1;
                    bc.addNewCoffee(coffeeName, description, countryOfOrigin, intensity, price, rewardPoints, redeemPoints);
                }
            }
            case "insertCustomer":
            {

                for(int i = 0; i < times; i++)
                {
                    String customerFirstName = getAlphaNumericString(10);
					String customerLastName = getAlphaNumericString(10);
					char customerMiddleName = getAlphaNumericString(1).charAt(0);
					String birthDay = "10";
					String birthMonth = "Jan";
<<<<<<< HEAD
					String phoneNumber = "7610457547";
					String phoneType = getAlphaNumericString(10);
=======
					String phoneNumber = "1234567890";
					String phoneType = "Home";

>>>>>>> 1302765c7b5cf09f6bdac2a0f8437cad5bf903ed
                    bc.addNewCustomer(customerFirstName, customerLastName, customerMiddleName, birthDay, birthMonth, phoneNumber, phoneType);
                }
            }
            case "insertPromotion":
            {
                    for(int i = 0; i < times; i++)
                    {

                    String promotionName = getAlphaNumericString(10);
					String promotionStartDate = "2021-06-07";
					String promotionEndDate = "2022-05-32";
					int promoCoffeeID = (int) Math.random()*35;
<<<<<<< HEAD
                        bc.schedulePromotion(promotionName, promotionStartDate, promotionName, i);
=======
                        bc.schedulePromotion(promotionName, promotionStartDate, promotionName, promoCoffeeID);
>>>>>>> 1302765c7b5cf09f6bdac2a0f8437cad5bf903ed
                    }
            }
            case "addPromoToStore":
            {
                    for(int i = 0; i < times; i++)
                    {
                        int promoID = (int) Math.random()*10;
					    int storePromoID = (int) Math.random()*10;
                        bc.addPromoToStore(promoID, storePromoID);
                    }
            }
            case "listStoresWithPromos":
            {
                int coffeeID = 1005;
                for(int i = 0; i < times; i++)
                {
                    bc.getStoresWithPromotions(coffeeID);
                }
            }
            case "checkStorePromos":
            {
                int storepromoid = 2001;
                int coffeepromoID = 0;
                for(int i = 0; i < times; i++)
                {
                    bc.checkStoreForPromos(storepromoid, coffeepromoID);
                }
            }
            case "getClosestStores":
            {
                for(int i = 0; i < times; i++)
                {
                    float longitude = (float) Math.random()*50+100;
                    float latitude = (float) Math.random()*50+100;
                    int storePromoId = 0;
                    bc.getClosestStores(latitude, i, storePromoId);
                }
            }
            case "setLoyaltyLevel":
            {
                for(int i = 0; i < times; i++)
                {
                    String levelname = "basic";
                    float boostFactor = (float) Math.random()*5+1;
                    bc.addOrSetLoyaltyLevel(levelname, boostFactor);
                }
            }
            case "getLoyaltyPoints":
            {
                for(int i = 0; i < times; i++)
                {
                    int custIDforPoints = (int) Math.random()*19+1;
                    bc.getCustomerLoyaltyPoints(custIDforPoints);
                }
            }
            case "getRankedList":
            {
                for(int i = 0; i < times; i++)
                {
                    bc.getRankedCustomerList();
                }
            }
            case"listCoffeeMenu":
            {
                for(int i = 0; i < times; i++)
                {
                    bc.listCoffeeMenu();
                }
            }
            case "listCoffeeIntensity":
            {
                String keywordOne = "";
                String keywordTwo = "";
                int intense = 7;
                for(int i = 0; i < times; i++)
                {
                    bc.listCoffeeWithIntensity(keywordOne, keywordTwo, intense);
                }
            }
            case "listTopKstores":
            {
                for(int i = 0; i < times; i++)
                {
                    int k = (int) Math.random()*10+1;
                    int x = (int) Math.random()*11+1;
                    bc.listTopKstores(k, x);
                }
            }
            case "listTopCustomerIDs":
            {
                for(int i = 0; i < times; i++)
                {
                    int kCustomers = (int) Math.random()*15+1;
                    int xMonths = (int) Math.random()*11+1;
                    bc.listTopCustomerIDs(kCustomers, xMonths);
                }
            }
            case "quit": 
            {
                System.out.println("\n***Goodbye!***\n");
				break;
            }

        }
    } while(!query.equals("quit"));
    }

}
