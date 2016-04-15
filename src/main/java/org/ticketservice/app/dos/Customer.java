package org.ticketservice.app.dos;

/**
 * Represents a customer who would make a reservation
 * The hashcode implementation ensures that registered customers will not contain duplicate email
 * 
 * @author Sudhir Sambrani
 *
 */
public class Customer {

	/**
	 * customer email address
	 */
	String customerEmailAddress;

	public Customer(String emailAddress)
	{
		this.customerEmailAddress = emailAddress;
	}
	
	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}
	
	@Override
	public int hashCode()
	{
		return customerEmailAddress.hashCode();
	}

}
