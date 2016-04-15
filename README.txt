The Simple Ticket Service is implemented using Java (JDK 1.8). 
The service is a stand alone service that uses an in memory database and does not persist
data to a disk. The Simple Ticket Service solution contains a pom.xml file to 
build test and package the solution


System Requirements
-----------------------
1) The system must have version 8 of Java installed.
2) Maven must be installed and version 3.3.9 was used to build test and package the solution.


Solution Dependencies
-----------------------
The packaged Ticket Service project will require the following additional jars
junit-4.12
log4j-1.2.17
hamcrest-core-1.3 (for junit)

Configuring the application to run
-----------------------------------
1) The following parameters need to be configured for the application to run.
seatHold.expiration,cleanup.interval
this can be done via the config.properties file in the resources folder.
The AppConfig class will read the necessary configuration properties and if the
file cannot be read default properties are used as shown below.
	/**
	 * default values configure via properties file in SECONDS
	 */
	private static long SEAT_HOLD_EXPIRATION = 20;
	/**
	 * default values configure via properties file in SECONDS
	 */
	private static long SEAT_HOLD_CLEANUP_SCHEDULER_INTERVAL = 10;
	
In order for the application to clean up expired SeatHolds efficiently the 
value of seatHold.expiration should be set to a value greater than cleanup.interval
	

2) Registered customer. The AppConfig file set up registered customers.

		registeredCustomers.add(new Customer("s1@gmail.com"));
		registeredCustomers.add(new Customer("s2@gmail.com"));
		registeredCustomers.add(new Customer("s3@gmail.com"));
		registeredCustomers.add(new Customer("s4@gmail.com"));
		

For testing purposes if additional users can be registered via setter exposed in the app config file
public static void registerCustomer(Customer newCustomer);

The above two configuration parameters must be set as required.


Starting the service
-----------------------------
Before starting the service the AppConfig properties must 
be set as described in "Configuring the application to run"
step.

Programatically the the AppConfig must be initialized prior to starting the SimpleTicketService as follows.

		AppConfig.initializeAppParameters();
		
		// instantiate ticket service after making the call to AppConfig.initializeAppParameters()
		TicketService ticketService = new DefaultTicketService();

The above step is mandatory and is needed to initialize the in memory database.


Building Testing and Packaging the solution.
-------------------------------------------
mvn compile - Build the solution

mvn test - Build and test the solution. The tests are defined in org.ticketservice.app.tests package.

mvn package - will package the solution as a jar. Default package name ticket-service-1.0.jar


Testing
-----------------------------
Sample unit and load test are defined in the following classes
			org.ticketservice.app.tests.TicketServiceTest
			org.ticketservice.app.tests.LoadTest
			
			
Note: Some test wait for the clean up task to execute and hence may take longer to execute

Simple Ticket Service API usage and assumptions
-----------------------------------
This section will explain the usage of the API, expected results and validations. 


1) int numSeatsAvailable(Optional<Integer> venueLevel)

Returns the number of seats available. If an invalid venueLevel is passed 0  is returned.


2) String reserveSeats(int seatHoldId, String customerEmail);

Returns the reservation code for a provided SeatHold id and customer email Id. If invalid 
values are passed null is returned as the reservation code. The seatHoldId and customerEmail 
must match a previous SeatHold request.


3) SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel,Optional<Integer> maxLevel, String customerEmail);

The minLevel and maxLevel parameters cannot be null.
The minLevel parameter cannot be less than the maxLevel parameters
The minLevel and maxLevel parameters cannot be less than 0.
The numSeats to find and hold cannot be less than 0;
The customerEmail must be of  registered user.

If invalid parameters are passed the return value is null. the log also will indicate a description of the error.


Logging
--------
The application is configured to use log4j to log messages to the console. 
Modify the log4j.properties to customize it.













