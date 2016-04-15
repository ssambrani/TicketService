package org.ticketservice.app.tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ticketservice.app.AppConfig;
import org.ticketservice.app.dos.SeatReservation;
import org.ticketservice.app.service.DefaultTicketService;
import org.ticketservice.app.service.TicketService;
import org.ticketservice.app.testutils.SeatHoldRequest;
import org.ticketservice.app.testutils.SeatReserveRequest;

public class LoadTest {

	private TicketService ticketService;

	@BeforeClass
	public static void setUpTestClass() {
		AppConfig.initializeAppParameters();
	}

	@Before
	public void setUpTestCase() {
		ticketService = new DefaultTicketService();
	}

	@Test
	public void loadTest() {

		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			Runnable worker = new SeatHoldRequest(ticketService);
			executor.execute(worker);
		}
		
		for (int i = 0; i < 50; i++) {
			Runnable worker = new SeatReserveRequest(ticketService);
			executor.execute(worker);
		}
		executor.shutdown();
		// wait till its done
		while (!executor.isTerminated());
		
		// wait for cleanup thread to kick in
		try {
			Thread.sleep((AppConfig.getSeatHoldCleanupSchedulerInterval() * 1000) + 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
