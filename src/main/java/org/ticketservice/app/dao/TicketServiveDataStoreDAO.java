package org.ticketservice.app.dao;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.ticketservice.app.dos.Customer;
import org.ticketservice.app.dos.SeatHold;
import org.ticketservice.app.dos.VenueSeat;

/**
 * The DOA interface to perform operations on the data store.
 * @author Sudhir Sambrani
 *
 */
public interface TicketServiveDataStoreDAO {

	
	/**
	 * reserve a seat
	 * @param seatHoldId the id of the SeatHold from a previous findAndHoldSeats call
	 * @param customerEmail the customer email
	 * 
	 * @return the reservation confirmation number.
	 */
	public String reserveSeat(int seatHoldId, String customerEmail);
	
	/**
	 * find and mark requested seats in hold. The seats are requested for specific levels.
	 * The caller can request a set of levels to request tickets for. If there are multiple
	 * levels requested the implementation returns the best seats available. A best possible seat
	 * is the one with the best view. 	  
	 *  
	 * @param numSeats the number of seats requested to find and hold
	 * @param levels the seating levels requested.
	 * @param customer
	 * 
	 * @return the SeatHold object
	 */
	public Optional<SeatHold> findAndHoldSeats(int numSeats, Set<Integer> levels, Customer customer);
	
	/**
	 * Seats can be held by a customer for a certain duration. 
	 * Expire the seat holds once the duration specified in the application configuration property is elapsed
	 * 
	 */
	public void expireSeatHolds();
		
	/**
	 * Get a registered customer
	 * 
	 * @param emailAddress
	 * @return
	 */
	public Optional<Customer> getCustomer(String emailAddress);
	
	/**
	 * gets the available seats for the levels indicated.
	 * A list of levels can be provided and the implementation will return the best possible seats
	 * A best possible seat is the one with the best view.
	 *  	  
	 * @param levels
	 * @return
	 */
	public List<VenueSeat> getAvailableSeats(Set<Integer> levels);
	
}
