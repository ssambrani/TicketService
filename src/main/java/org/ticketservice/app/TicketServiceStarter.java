package org.ticketservice.app;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.ticketservice.app.service.DefaultTicketService;
import org.ticketservice.app.service.TicketService;

/**
 * @author Sudhir Sambrani
 * Sample code to use the Simple Ticket Service
 *
 */

public class TicketServiceStarter {

	private final static Logger logger = Logger.getLogger(TicketServiceStarter.class);
	
	public static void main(String[] args)
	{
		logger.info("Initializing Application data");
		AppConfig.initializeAppParameters();
		
		// instantiate ticket service after making the call to AppConfig.initializeAppParameters()
		TicketService ticketService = new DefaultTicketService();
		
		// 	use ticket service
		ticketService.numSeatsAvailable(Optional.of(1));
		ticketService.findAndHoldSeats(5, Optional.of(1), Optional.of(2), "s1@gmail.com");
	}	
}
