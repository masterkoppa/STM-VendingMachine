/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
 ***/

import akka.stm.Ref;
import akka.stm.Atomic;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VendingMachine {

	private final long MAXCANDYBARS = 6;
	private final long MAXCOOKIES = 6;
	final Ref<Long> candyBars = new Ref<Long>(MAXCANDYBARS);
	final Ref<Long> cookies = new Ref<Long>(MAXCOOKIES);
	final Ref<Long> cookiesServed = new Ref<Long>(0L);
	final Ref<Long> candyBarsServed = new Ref<Long>(0L);
	final Ref<Boolean> keepRunning = new Ref<Boolean>(true);
	private static final ScheduledExecutorService replenishTimer = Executors
			.newScheduledThreadPool(10);

	private VendingMachine() {
	}

	private void init() {
		replenishTimer.schedule(new Runnable() {
			public void run() {
				replenish();
				if (keepRunning.get()) {
					// Replentish the machine every 3 days
					replenishTimer.schedule(this, 3, TimeUnit.SECONDS);
				} else {
					replenishTimer.shutdownNow();
				}

			}
		}, 1, TimeUnit.SECONDS);
	}

	/**
	 * Generate a vending machine instance
	 * @return A new Vending Machine instance
	 */
	public static VendingMachine create() {
		final VendingMachine energySource = new VendingMachine();
		energySource.init();
		return energySource;
	}

	/**
	 * Mark the machine as unavailable, part of the shutdown process
	 */
	public void vendingMachineOutOfStock() {
		keepRunning.swap(false);
	}

	public long getCandyBarsAvailable() {
		return candyBars.get();
	}

	public long getCandyBarsServed() {
		return candyBarsServed.get();
	}

	public long getCookiesAvailable() {
		return cookies.get();
	}

	public long getCookiesServed() {
		return cookiesServed.get();
	}
	
	/**
	 * Buy a x number of cookies from the machine
	 * @param units The number of cookies to buy
	 * @return If you are able to buy them or not, depends on availability
	 */
	public boolean getCookies(final long units) {
		return new Atomic<Boolean>() {
			public Boolean atomically() {
				long currentLevel = cookies.get();
				if (units > 0 && currentLevel >= units) {
					cookies.swap(currentLevel - units);
					cookiesServed.swap(cookiesServed.get() + 1);
					return true;
				} else {
					return false;
				}
			}
		}.execute();
	}

	/**
	 * Buy a x number of candy bars from the machine
	 * @param units The number of candy bars to buy
	 * @return If you are able to buy them or not, depends on availability
	 */
	public boolean getCandyBars(final long units) {
		return new Atomic<Boolean>() {
			public Boolean atomically() {
				long currentLevel = candyBars.get();
				if (units > 0 && currentLevel >= units) {
					candyBars.swap(currentLevel - units);
					candyBarsServed.swap(candyBarsServed.get() + 1);
					return true;
				} else {
					return false;
				}
			}
		}.execute();
	}

	/**
	 * Stock back up the vending machine
	 */
	private void replenish() {
		new Atomic() {
			public Object atomically() {
				long currentLevel = cookies.get();
				if (currentLevel < MAXCOOKIES)
					cookies.swap(currentLevel + 1);

				currentLevel = candyBars.get();
				if (currentLevel < MAXCANDYBARS)
					candyBars.swap(currentLevel + 1);
				return null;
			}
		}.execute();
	}
}
