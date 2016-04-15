package org.ticketservice.app.testutils;

import java.util.Optional;

import org.ticketservice.app.service.TicketService;

public class SeatReserveRequest implements Runnable {

	TicketService ticketService;
	public SeatReserveRequest(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	@Override
	public void run() {
		
		ticketService.findAndHoldSeats(2, Optional.of(1), Optional.of(4), "s1@gmail.com");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
