package org.ticketservice.app;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ticketservice.app.dos.Customer;
import org.ticketservice.app.dos.VenueLevelSeatingAndPricingInfo;
import org.ticketservice.app.dos.VenueSeat;

/**
 * Read application configuration properties and make available to the app This class expects a
 * appConfig.properties file in the project root folder
 * 
 * @author Sudhir Sambrani
 *
 */
public class AppConfig {

	/**
	 * default values configure via properties file
	 */
	private static long SEAT_HOLD_EXPIRATION = 20;
	/**
	 * default values configure via properties file
	 */
	private static long SEAT_HOLD_CLEANUP_SCHEDULER_INTERVAL = 10;
	
	private static List<VenueLevelSeatingAndPricingInfo> venueLevelSeatingAndPricingInfo = new ArrayList<VenueLevelSeatingAndPricingInfo>();
	private static List<VenueSeat> venueSeats = new ArrayList<VenueSeat>();
	private static Set<Customer> registeredCustomers = new HashSet<Customer>();
	private final static Logger logger = Logger.getLogger(AppConfig.class);

	public static void initializeAppParameters() {
		
		try
		{
			InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(inputStream);
			
			SEAT_HOLD_EXPIRATION = Long.parseLong(prop.getProperty("seatHold.expiration"));
			SEAT_HOLD_CLEANUP_SCHEDULER_INTERVAL = Long.parseLong(prop.getProperty("cleanup.interval"));
			logger.info("Application Configuration properties were successfully loaded.");
		}
		catch(IOException e)
		{
			logger.error("Unable to load application config properties. Using default values");
		}
		
		venueLevelSeatingAndPricingInfo
				.add(new VenueLevelSeatingAndPricingInfo(1, 1, "Orchestra", new BigDecimal(100), 25, 50));
		venueLevelSeatingAndPricingInfo
				.add(new VenueLevelSeatingAndPricingInfo(1, 2, "Main", new BigDecimal(75), 20, 100));
		venueLevelSeatingAndPricingInfo
				.add(new VenueLevelSeatingAndPricingInfo(1, 3, "Balcony 1", new BigDecimal(50), 15, 100));
		venueLevelSeatingAndPricingInfo
				.add(new VenueLevelSeatingAndPricingInfo(1, 4, "Balcony 2", new BigDecimal(40), 15, 100));

		int seatId = 1;
		for (VenueLevelSeatingAndPricingInfo venueInfo : venueLevelSeatingAndPricingInfo) {
			for (int i = 0; i < venueInfo.getTotalSeats(); i++) {
				venueSeats.add(new VenueSeat(venueInfo.getVenueLevelId(), seatId++));
			}
		}

		// Register a few customers
		registeredCustomers.add(new Customer("s1@gmail.com"));
		registeredCustomers.add(new Customer("s2@gmail.com"));
		registeredCustomers.add(new Customer("s3@gmail.com"));
		registeredCustomers.add(new Customer("s4@gmail.com"));
		
		logger.info(" Completed Application Configuration Initialization");
	}

	/**
	 * The seat hold expiration time in seconds. This is the duration after which 
	 * a seat hold will expire
	 * @return
	 */
	public static long getSeatHoldExpiration() {
		return AppConfig.SEAT_HOLD_EXPIRATION;
	}

	/**
	 * The clean up interval duration. This is the duration  
	 * the scheduler is configured to invoke the cleaup thread 
	 * to remove all seat holds and make them available.
	 * @return
	 */
	public static long getSeatHoldCleanupSchedulerInterval() {
		return SEAT_HOLD_CLEANUP_SCHEDULER_INTERVAL;
	}

	public static List<VenueSeat> getVenueSeatList() {
		return venueSeats;
	}

	public static Set<Customer> getRegisteredCustomers() {
		return registeredCustomers;
	}

	/**
	 * register a customer.
	 * must be done prior to instantiating an instance of the SimpleTicketService
	 * @param newCustomer
	 */
	public static void registerCustomer(Customer newCustomer)
	{
		registeredCustomers.add(newCustomer);
	}
}
