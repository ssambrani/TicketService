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
import org.ticketservice.app.dos.SeatHold;
import org.ticketservice.app.service.DefaultTicketService;
import org.ticketservice.app.service.TicketService;

public class TicketServiceTest {

	private TicketService ticketService;
	

	@BeforeClass
	public static void setUpTestClass() {
		AppConfig.initializeAppParameters();		
	}
	
	@Before
	public void setUpTestCase()
	{
		ticketService = new DefaultTicketService();
	}
	

	@Test
	@Ignore
	public void testInMemoryDataStoreIntialization() {
		assertEquals(ticketService.numSeatsAvailable(Optional.of(1)), 1250);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(2)), 2000);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(3)), 1500);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(4)), 1500);
	}

	@Test
	@Ignore
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
		SeatHold sh = ticketService.findAndHoldSeats(5000, Optional.of(1), Optional.of(4), "s1@gmail.com");
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
	@Ignore
	public void testSeatHoldWithInvalidParameters()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.ofNullable(null), "s1@gmail.com");
		assertNull(sh1);
	}
	
	@Test
	public void testSeatHoldWithOneLevelRequest()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.ofNullable(null), Optional.of(1), "s1@gmail.com");
		assertNotNull(sh1);
		// check all 1000 are from level 1
		assertEquals(sh1.getSeats().stream().filter(vs -> vs.getVenueLevelId() == 1).count(), 1000);
		
		
		SeatHold sh2 = ticketService.findAndHoldSeats(45, Optional.ofNullable(null), Optional.of(2), "s1@gmail.com");
		assertNotNull(sh2);
		// check all 45 are from level 2
		assertEquals(sh2.getSeats().stream().filter(vs -> vs.getVenueLevelId() == 2).count(), 45);
		
		try {
			Thread.sleep((AppConfig.getSeatHoldCleanupSchedulerInterval() * 1000) + 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(ticketService.numSeatsAvailable(Optional.of(1)),1250);
		assertEquals(ticketService.numSeatsAvailable(Optional.of(2)),2000);
	}
	
	@Test
	@Ignore
	public void testSeatHoldExpiration()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "s1@gmail.com");
		SeatHold sh2 = ticketService.findAndHoldSeats(250, Optional.of(1), Optional.of(2), "s2@gmail.com");
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
	@Ignore
	public void testSeatHoldReserveAndExpire()
	{
		SeatHold sh1 = ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "s1@gmail.com");
		SeatHold sh2 = ticketService.findAndHoldSeats(250, Optional.of(1), Optional.of(2), "s2@gmail.com");
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
		
	}
}
