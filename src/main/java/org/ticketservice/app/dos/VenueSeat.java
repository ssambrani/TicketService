package org.ticketservice.app.dos;


/**
 * represents a seat in the venue
 * @author Sudhir Sambrani
 *
 */
public class VenueSeat {
	
	/**
	 * the venue level id defined by the VenueLevelSeatingAndPricing
	 */
	private int venueLevelId;
	/**
	 * the seat id. a unique identifier for the seat
	 */
	private int seatId;
	
	public VenueSeat(int venueLevelId, int seatId)
	{
		this.venueLevelId = venueLevelId;
		this.seatId = seatId;
	}
	
	public int getVenueLevelId() {
		return venueLevelId;
	}
		
	public int getSeatId() {
		return seatId;
	}
	
	@Override
	public int hashCode()
	{
		return seatId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return (seatId == ((VenueSeat)o).seatId);
	}
}
