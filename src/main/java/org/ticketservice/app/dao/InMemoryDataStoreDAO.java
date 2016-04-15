package org.ticketservice.app.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.ticketservice.app.AppConfig;
import org.ticketservice.app.dos.Customer;
import org.ticketservice.app.dos.SeatHold;
import org.ticketservice.app.dos.SeatReservation;
import org.ticketservice.app.dos.VenueSeat;

public class InMemoryDataStoreDAO implements TicketServiveDataStoreDAO {

	private final static Logger logger = Logger.getLogger(InMemoryDataStoreDAO.class);
	private Set<Customer> registeredCustomers;
	private List<SeatReservation> seatReservations = new ArrayList<SeatReservation>();
	private List<VenueSeat> venueSeats;
	private final Lock readLock;
	private final Lock writeLock;

	/**
	 * Constructor
	 */
	public InMemoryDataStoreDAO() {
		this.venueSeats = AppConfig.getVenueSeatList();
		this.registeredCustomers = AppConfig.getRegisteredCustomers();
		ReadWriteLock rwLock = new ReentrantReadWriteLock();
		this.readLock = rwLock.readLock();
		this.writeLock = rwLock.writeLock();
	}

	@Override
	public void expireSeatHolds() {
		long holdTimeInMillis = AppConfig.getSeatHoldExpiration();
		List<SeatReservation> seatHoldsToExpire = null;
		writeLock.lock();
		try {
			// get the list of seatholds and filter the ones whose hold duration has expired
			seatHoldsToExpire = seatReservations.stream().map(sh -> (SeatHold) sh)
					.filter(sh -> sh.getReservationCode() == null)
					.filter((sh -> sh.getHoldTime() < System.currentTimeMillis() - holdTimeInMillis))
					.collect(Collectors.toList());

			logger.info("Found " + (seatHoldsToExpire != null ? seatHoldsToExpire.size() : 0) + " seat holds to expire");
			// remove them from the list of reservations
			seatReservations.removeAll(seatHoldsToExpire);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public String reserveSeat(int seatHoldId, String customerEmail) {
		Optional<SeatReservation> seatReservation;
		String reservationCode;
		writeLock.lock();
		try
		{
			//get the seat reservations based on SeatHold id and customer email. Both must match 
			seatReservation = seatReservations.stream().filter(sr -> sr.getReservationId() == seatHoldId)
					.filter(sr -> sr.getCustomer().getCustomerEmailAddress() == customerEmail).findFirst();
			
			if(seatReservation.isPresent())
			{
				seatReservation.get().setReservationCode(UUID.randomUUID().toString());
				reservationCode = seatReservation.get().getReservationCode();
			}
			else
			{
				reservationCode = "";
			}
		}
		finally
		{
			writeLock.unlock();
		}
		
		return reservationCode;
	}

	@Override
	public Optional<Customer> getCustomer(String emailAddress) {
		//get registered customers
		return registeredCustomers.stream().filter(rc -> emailAddress.equals(rc.getCustomerEmailAddress())).findFirst();
	}

	@Override
	public List<VenueSeat> getAvailableSeats(Set<Integer> levels) {
		List<VenueSeat> availableSeats = null;
		readLock.lock();
		try {
			//get the available seats
			// remove reserved and hold seat ids from list of seats in the venue
			Set<Integer> reservedAndHoldSeatIds = seatReservations.stream().map(s -> s.getSeats())
					.flatMap(vs -> vs.stream()).filter(vs -> levels.contains(vs.getVenueLevelId()))
					.map(vs -> vs.getSeatId()).collect(Collectors.toSet());

			availableSeats = venueSeats.stream().filter(vs -> levels.contains(vs.getVenueLevelId()))
					.filter(vs -> !reservedAndHoldSeatIds.contains(vs.getSeatId())).collect(Collectors.toList());
		} finally {
			readLock.unlock();
		}

		return availableSeats;
	}

	@Override
	public Optional<SeatHold> findAndHoldSeats(int numSeats, Set<Integer> levels, Customer customer) {

		List<VenueSeat> seatsToHold = null;
		SeatReservation seatReservation = null;
		writeLock.lock();
		try {
			// find and hold seats
			seatsToHold = getAvailableSeats(levels).stream()
					.sorted((v1, v2) -> Integer.compare(v1.getVenueLevelId(), v2.getVenueLevelId())).limit(numSeats)
					.collect(Collectors.toList());
			seatReservation = new SeatHold(customer, seatsToHold);
			// get a reservation id to return to caller
			seatReservation.setReservationId(getReservationId());
			seatReservations.add(seatReservation);
		} finally {
			writeLock.unlock();
		}
		return Optional.of((SeatHold) seatReservation);
	}

	/**
	 * a helper method to get the max reservation id.
	 * @return
	 */
	private int getReservationId() {
		int maxReservationId = seatReservations.stream().map(s -> s.getSeats()).flatMap(vs -> vs.stream())
				.map(vs -> vs.getSeatId()).max(Comparator.naturalOrder()).orElse(1);
		return maxReservationId;
	}
}
