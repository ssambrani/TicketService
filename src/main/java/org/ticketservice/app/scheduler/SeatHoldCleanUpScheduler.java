package org.ticketservice.app.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ticketservice.app.AppConfig;

/**
 * the scheduler service will run at specified intervals and invoke the clean up task
 * @author Sudhir Sambrani
 *
 */
public class SeatHoldCleanUpScheduler {

	private ScheduledExecutorService scheduler = null;

	/**
	 * set up scheduler to run at fixed intervals
	 */
	public void initialize(SeatHoldCleanUpTask seatHoldCleanUpTask) {
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(seatHoldCleanUpTask, 0, AppConfig.getSeatHoldCleanupSchedulerInterval(),
				TimeUnit.SECONDS);
	}

}
