package org.ticketservice.app.dos;

import java.util.List;

/**
 * This class represents a reservation made by a customer.
 * 
 * @author Sudhir Sambrani
 *
 */
public class SeatReservation { 
	
	/**
	 * reservation id auto generated
	 */
	private int reservationId;
	/**
	 * the registered customer who made this reservation
	 */
	private Customer customer;
	/**
	 * the list of seats on hold or reserved
	 */
	private List <VenueSeat> seats;
	/**
	 * the reservation code
	 */
	private String reservationCode = null;
	
	/**
	 * default constructor
	 */
	public SeatReservation()
	{
		
	}
	
	/**
	 * 
	 * @param customer the registered customer
	 * @param seats the seats reserved by the customer
	 */
	public SeatReservation(Customer customer, List<VenueSeat> seats)
	{
		this.customer = customer;
		this.seats = seats;
	}
	
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}
	public int getReservationId() {
		return reservationId;
	}
	public String getReservationCode() {
		return reservationCode;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public List<VenueSeat> getSeats() {
		return seats;
	}
	
		
	
}
