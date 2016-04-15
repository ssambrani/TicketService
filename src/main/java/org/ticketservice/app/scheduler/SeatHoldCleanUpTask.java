package org.ticketservice.app.scheduler;

import org.apache.log4j.Logger;
import org.ticketservice.app.dao.TicketServiveDataStoreDAO;

/**
 * The SeatHoldCleanUpTask will expire seat holds by a customer
 * This Runnable is invoked by a scheduler at a fixed rate
 * 
 * @author Sudhir Sambrani
 *
 */
public class SeatHoldCleanUpTask implements Runnable{

	private final static Logger logger = Logger.getLogger(SeatHoldCleanUpTask.class);
	private final TicketServiveDataStoreDAO ticketReservationStoreDAO;
	
	public SeatHoldCleanUpTask(TicketServiveDataStoreDAO ticketReservationStoreDAO) {
		this.ticketReservationStoreDAO = ticketReservationStoreDAO;
	}
	public void run() {
		logger.info("********* Invoking Cleanup Thread.\n");
		ticketReservationStoreDAO.expireSeatHolds();
		logger.info("********* Completed Cleanup Thread.\n");
	}
}
