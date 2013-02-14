/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class Main {
  private static final VendingMachine vendingMachine = VendingMachine.create();
    
  public static void main(final String[] args) 
    throws InterruptedException, ExecutionException {
    
	  //Start up all the persons in this simulation
	  new CookieMonster(vendingMachine);
	  new WillieWonka(vendingMachine);
	  new FatAlbert(vendingMachine);
	  
	  Thread.sleep(30 * 1000);
	  
	  System.exit(0);
  }
}
