package org.ticketservice.app.testutils;

import java.util.Optional;

import org.ticketservice.app.dos.SeatHold;
import org.ticketservice.app.service.TicketService;

public class SeatHoldRequest implements Runnable {

	TicketService ticketService;
	public SeatHoldRequest(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	@Override
	public void run() {
		
		SeatHold sh = ticketService.findAndHoldSeats(2, Optional.of(1), Optional.of(4), "s3@gmail.com");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
		}
		ticketService.reserveSeats(sh.getReservationId(), "s3@gmail.com");
	}

}
