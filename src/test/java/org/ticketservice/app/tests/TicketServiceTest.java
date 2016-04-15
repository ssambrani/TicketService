package org.ticketservice.app.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.ticketservice.app.AppConfig;
import org.ticketservice.app.dos.Customer;
import org.ticketservice.app.dos.SeatHold;
import org.ticketservice.app.service.DefaultTicketService;
import org.ticketservice.app.service.TicketService;

public class TicketServiceTest {

	private TicketService ticketService;
	

	@BeforeClass
	public static void setUpTestClass() {
		
		//. register additional customers for testing
		AppConfig.registerCustomer(new Customer("a@gmail.com"));
		AppConfig.registerCustomer(new Customer("b@gmail.com"));
		AppConfig.initializeAppParameters();		
	}
	
	@Before
	public void setUpTestCase()
	{
		ticketService = new DefaultTicketService();
	}
	

	@Test
	public void testInMemoryDataStoreIntialization() {
		assertEquals(ticketService.numSeatsAvailable(Optional.of(1)), 1250);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(2)), 2000);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(3)), 1500);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(4)), 1500);
	}

	@Test
	public void testSeatHoldForOneLevelSeats() {
		int numSeatsBeforeHold= ticketService.numSeatsAvailable(Optional.of(1));
		SeatHold sh = ticketService.findAndHoldSeats(500, Optional.of(1), Optional.of(2), "s1@gmail.com");
		int numSeatsAfterHold = ticketService.numSeatsAvailable(Optional.of(1));
		assertEquals(sh.getSeats().size(),500);
		assertEquals(numSeatsBeforeHold,(numSeatsAfterHold +500));
	}

	@Test
	@Ignore
	public void testSeatHoldForTwoLevelSeats()
	{
		int numSeatsBeforeHoldLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numSeatsBeforeHoldLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		SeatHold sh = ticketService.findAndHoldSeats(1500, Optional.of(1), Optional.of(2), "s1@gmail.com");
		int numSeatsAfterHoldLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numSeatsAfterHoldLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		assertEquals(sh.getSeats().size(),1500);
		assertEquals(numSeatsBeforeHoldLevel1, 1250);
		assertEquals(numSeatsBeforeHoldLevel2, 2000);
		assertEquals(numSeatsAfterHoldLevel1, 0);
		assertEquals(numSeatsAfterHoldLevel2, 1750);
	}
	
	@Test
	@Ignore
	public void testSeatHoldForThreeLevels()
	{
		int numSeatsBeforeHoldLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numSeatsBeforeHoldLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		int numSeatsBeforeHoldLevel3 = ticketService.numSeatsAvailable(Optional.of(3));
		SeatHold sh = ticketService.findAndHoldSeats(3500, Optional.of(1), Optional.of(3), "s1@gmail.com");
		int numSeatsAfterHoldLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numSeatsAfterHoldLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		int numSeatsAfterHoldLevel3 = ticketService.numSeatsAvailable(Optional.of(3));
		assertEquals(sh.getSeats().size(),3500);
		assertEquals(numSeatsBeforeHoldLevel1, 1250);
		assertEquals(numSeatsBeforeHoldLevel2, 2000);
		assertEquals(numSeatsBeforeHoldLevel3, 1500);
		assertEquals(numSeatsAfterHoldLevel1, 0);
		assertEquals(numSeatsAfterHoldLevel2, 0);
		assertEquals(numSeatsAfterHoldLevel3, 1250);
	}
	
	@Test
	@Ignore
	public void testSeatHoldForFourLevels()
	{
		int numSeatsBeforeHoldLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numSeatsBeforeHoldLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		int numSeatsBeforeHoldLevel3 = ticketService.numSeatsAvailable(Optional.of(3));
		int numSeatsBeforeHoldLevel4 = ticketService.numSeatsAvailable(Optional.of(4));
		SeatHold sh = ticketService.findAndHoldSeats(5000, Optional.of(1), Optional.of(4), "s3@gmail.com");
		int numSeatsAfterHoldLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numSeatsAfterHoldLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		int numSeatsAfterHoldLevel3 = ticketService.numSeatsAvailable(Optional.of(3));
		int numSeatsAfterHoldLevel4 = ticketService.numSeatsAvailable(Optional.of(4));
		assertEquals(sh.getSeats().size(),5000);
		assertEquals(numSeatsBeforeHoldLevel1, 1250);
		assertEquals(numSeatsBeforeHoldLevel2, 2000);
		assertEquals(numSeatsBeforeHoldLevel3, 1500);
		assertEquals(numSeatsBeforeHoldLevel4, 1500);
		assertEquals(numSeatsAfterHoldLevel1, 0);
		assertEquals(numSeatsAfterHoldLevel2, 0);
		assertEquals(numSeatsAfterHoldLevel3, 0);
		assertEquals(numSeatsAfterHoldLevel4, 1250);
	}
	
	
	@Test
	public void testSeatHoldWithInvalidParameters()
	{
		assertNull(ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.ofNullable(null), "s1@gmail.com"));
		assertNull(ticketService.findAndHoldSeats(-1, Optional.of(1), Optional.of(-1), "s2@gmail.com"));
		assertNull(ticketService.findAndHoldSeats(251, Optional.of(1), Optional.of(-1), "a@gmail.com"));
		assertNull(ticketService.findAndHoldSeats(1000, Optional.of(-1), Optional.of(3), "b@gmail.com"));
	}
	
	
	
	@Test
	public void testSeatHoldExpiration()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "s1@gmail.com");
		SeatHold sh2 = ticketService.findAndHoldSeats(250, Optional.of(1), Optional.of(2), "s3@gmail.com");
		int numSeatsAfterHold = ticketService.numSeatsAvailable(Optional.of(1));
		assertEquals(sh1.getSeats().size(),1000);
		assertEquals(sh2.getSeats().size(),250);
		assertEquals(numSeatsAfterHold,0);
		
		try {
			Thread.sleep((AppConfig.getSeatHoldCleanupSchedulerInterval() * 1000) + 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int numSeatsAfterExpiration = ticketService.numSeatsAvailable(Optional.of(1));
		assertEquals(numSeatsAfterExpiration,1250);
	}
	
	@Test
	public void testSeatHoldReserveAndExpire()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "s1@gmail.com");
		SeatHold sh2 = ticketService.findAndHoldSeats(250, Optional.of(1), Optional.of(2), "b@gmail.com");
		int numSeatsAfterHold = ticketService.numSeatsAvailable(Optional.of(1));
		assertEquals(sh1.getSeats().size(),1000);
		assertEquals(sh2.getSeats().size(),250);
		assertEquals(numSeatsAfterHold,0);
		String reservationCode = ticketService.reserveSeats(sh1.getReservationId(), "s1@gmail.com");
		assertNotNull(reservationCode);
		try {
			Thread.sleep((AppConfig.getSeatHoldCleanupSchedulerInterval() * 1000) + 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int numSeatsAfterExpiration = ticketService.numSeatsAvailable(Optional.of(1));
		assertEquals(numSeatsAfterExpiration,250);
	}
	
	@Test
	public void testInvalidReserveSeatParameters()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "a@gmail.com");
		String reservationCode = ticketService.reserveSeats(sh1.getReservationId() + 5, sh1.getCustomer().getCustomerEmailAddress());
		assertEquals(reservationCode,"");
	}
	
	@Test
	public void testInvalidNumSeatsAvailableParameters()
	{
		assertEquals(ticketService.numSeatsAvailable(Optional.of(-1)), 0);
		assertEquals(ticketService.numSeatsAvailable(Optional.ofNullable(null)), 0);
	}
	
	@Test
	public void reserveAllSeats()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "s1@gmail.com");
		SeatHold sh2 = ticketService.findAndHoldSeats(250, Optional.of(1), Optional.of(2), "s2@gmail.com");
		SeatHold sh3 = ticketService.findAndHoldSeats(1500, Optional.of(1), Optional.of(2), "s3@gmail.com");
		SeatHold sh4 = ticketService.findAndHoldSeats(2000, Optional.of(2), Optional.of(3), "s4@gmail.com");
		SeatHold sh5 = ticketService.findAndHoldSeats(1000, Optional.of(3), Optional.of(4), "a@gmail.com");
		SeatHold sh6 = ticketService.findAndHoldSeats(1000, Optional.of(3), Optional.of(4), "b@gmail.com");
		
		
		String reservationCode1 = ticketService.reserveSeats(sh1.getReservationId(), "s1@gmail.com");
		String reservationCode2 = ticketService.reserveSeats(sh2.getReservationId(), "s2@gmail.com");
		String reservationCode3 = ticketService.reserveSeats(sh3.getReservationId(), "s3@gmail.com");
		String reservationCode4 = ticketService.reserveSeats(sh4.getReservationId(), "s4@gmail.com");
		String reservationCode5 = ticketService.reserveSeats(sh5.getReservationId(), "a@gmail.com");
		String reservationCode6 = ticketService.reserveSeats(sh6.getReservationId(), "b@gmail.com");
		
		
		assertNotNull(reservationCode1);
		assertNotNull(reservationCode2);
		assertNotNull(reservationCode3);
		assertNotNull(reservationCode4);
		assertNotNull(reservationCode5);
		assertNotNull(reservationCode6);
	
		
		assertEquals(ticketService.numSeatsAvailable(Optional.of(1)),0);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(2)),0);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(3)),0);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(4)),0);
		
	}
}
