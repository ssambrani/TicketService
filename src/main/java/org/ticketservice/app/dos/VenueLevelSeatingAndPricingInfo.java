package org.ticketservice.app.dos;

import java.math.BigDecimal;

/**
 * Pricing for a venue defined by venue id
 * @author Sudhir Sambrani
 *
 */
public class VenueLevelSeatingAndPricingInfo {
	
	/**
	 * the venue level id
	 */
	private int venueLevelId;
	/**
	 * the level name
	 */
	private String levelName;
	/**
	 * the price
	 */
	private BigDecimal price;
	/**
	 * the rows in the level
	 */
	private int rows;
	/**
	 * number of seats in each row of the level
	 */
	private int seatsInRow;
	
	public VenueLevelSeatingAndPricingInfo(int venueId, int venueLevelId, String levelName, BigDecimal price, int rows, int seatsInRow)
	{
		this.venueLevelId = venueLevelId;
		this.levelName = levelName;
		this.price = price;
		this.rows = rows;
		this.seatsInRow = seatsInRow;
	}
	
		
	public int getTotalSeats()
	{
		return rows * seatsInRow;
	}

	public int getVenueLevelId() {
		return venueLevelId;
	}


	public String getLevelName() {
		return levelName;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public int getRows() {
		return rows;
	}


	public int getSeatsInRow() {
		return seatsInRow;
	}
}
