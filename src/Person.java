import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class Person {
	
	protected VendingMachine vending;
	protected List<Callable<Object>> tasks;
	protected static ScheduledExecutorService service;
	
	public Person(VendingMachine vending){
		this.vending = vending;
		tasks = new ArrayList<Callable<Object>>();  
		service =  Executors.newScheduledThreadPool(10);
	}
	
	public Runnable createRequest(final boolean cookies, final String sucessMessage, final String failureMessage, final int amount){
		return new Runnable() {
	        public void run() {
	        	boolean ret = false;
	        	//If we want cookies
	            if(cookies){
	            	ret = vending.getCookies(amount);
	            }
	            //We want candy bars
	            else{
	            	ret = vending.getCandyBars(amount);
	            }
	            
	            if(ret){
	            	System.out.println(sucessMessage);
	            }else{
	            	System.out.println(failureMessage);
	            }
	            
	          }
	        };
	}
	
	public void shutdown(){
		try {
			service.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		vending.vendingMachineOutOfStock();
	}

}
