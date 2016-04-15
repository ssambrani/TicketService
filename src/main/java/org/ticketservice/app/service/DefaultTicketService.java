package org.ticketservice.app.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.ticketservice.app.dao.InMemoryDataStoreDAO;
import org.ticketservice.app.dao.TicketServiveDataStoreDAO;
import org.ticketservice.app.dos.Customer;
import org.ticketservice.app.dos.SeatHold;
import org.ticketservice.app.scheduler.SeatHoldCleanUpScheduler;
import org.ticketservice.app.scheduler.SeatHoldCleanUpTask;

/**
 * A Default Implementation of the TicketService interface. Uses an in memory data store
 * 
 * @author Sudhir Sambrani
 *
 */
public class DefaultTicketService implements TicketService {

	TicketServiveDataStoreDAO ticketReservationStoreDAO;
	private final static Logger logger = Logger.getLogger(DefaultTicketService.class);


	public DefaultTicketService() {
		//create the InMemoryDataStore
		ticketReservationStoreDAO = new InMemoryDataStoreDAO();
		// create the scheduler and the runnable that will handle the cleanup.
		SeatHoldCleanUpTask seatHoldCleanUpTask = new SeatHoldCleanUpTask(ticketReservationStoreDAO);
		SeatHoldCleanUpScheduler cleanUpScheduler = new SeatHoldCleanUpScheduler();
		// initialize the scheduler
		cleanUpScheduler.initialize(seatHoldCleanUpTask);
	}

	/* (non-Javadoc)
	 * @see org.ticketservice.app.service.TicketService#numSeatsAvailable(java.util.Optional)
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		Set<Integer> level;
		int availableSeats = 0;
		
		// get the num seats after validating the parameters
		if(validatenumSeatsAvailableParameters(venueLevel))
		{
			level = new HashSet<Integer>();
			level.add(venueLevel.get());
			availableSeats = ticketReservationStoreDAO.getAvailableSeats(level).size();
		}
		return availableSeats;
	}

	
	/* (non-Javadoc)
	 * @see org.ticketservice.app.service.TicketService#findAndHoldSeats(int, java.util.Optional, java.util.Optional, java.lang.String)
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {

		SeatHold sh = null;
		// validate parameters and get seat holds
		if (validateFindAndHoldSeatsParameters(numSeats, minLevel, maxLevel)) {
			Set<Integer> levels = IntStream.rangeClosed(minLevel.orElse(maxLevel.get()), maxLevel.get()).boxed()
					.collect(Collectors.toSet());
			Customer c = ticketReservationStoreDAO.getCustomer(customerEmail).get();
			sh = ticketReservationStoreDAO.findAndHoldSeats(numSeats, levels, c).get();
		}
		return sh;
	}

	/* (non-Javadoc)
	 * @see org.ticketservice.app.service.TicketService#reserveSeats(int, java.lang.String)
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		return ticketReservationStoreDAO.reserveSeat(seatHoldId, customerEmail);
	}

	/**
	 * validate the seat hold parameters
	 * @param numSeats
	 * @param minLevel
	 * @param maxLevel
	 * @return
	 */
	private boolean validateFindAndHoldSeatsParameters(int numSeats, Optional<Integer> minLevel,
			Optional<Integer> maxLevel) {
		if ((!maxLevel.isPresent())) {
			logger.error(" Max Level cannot be null. See Ticket Service API usage in README.");
			return false;
		}
		else if (minLevel.orElse(0) > maxLevel.get()) {
			logger.error(" min level cannot be greater than max level. See Ticket Service API usage in README");
			return false;
		}
		else if (numSeats <= 0) {
			logger.error(" Invalid number of seats requested -> " + numSeats);
			return false;
		}
		
		return true;	
	}
	
	/**
	 * validate the parameters to numSeatsAvailable
	 * @param venueLevel
	 * @return
	 */
	private boolean validatenumSeatsAvailableParameters(Optional<Integer> venueLevel) {
		if(!venueLevel.isPresent())
			return false;
		else 
			return true;
	}
}
