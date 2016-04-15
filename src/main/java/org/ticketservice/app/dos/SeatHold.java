package org.ticketservice.app.dos;

import java.util.List;

/**
 * Represents seat held by a customer. 
 * SeatHold is a special case of a seat reservation 
 * which is temporary and is defined by the hold time.
 * The hold will expire when hold time exceeds system time 
 * by the number of seconds specified by the appConfig property 
 * seatHold.expiration
 * @author Sudhir Sambrani
 *
 */
public class SeatHold extends SeatReservation{
	
	private long holdTime;

	public SeatHold()
	{
		super();		
	}
	
	public SeatHold(Customer customer, List<VenueSeat> seats)
	{
		super(customer, seats);
		holdTime =  System.currentTimeMillis();
	}

	public long getHoldTime() {
		return holdTime;
	}
}
