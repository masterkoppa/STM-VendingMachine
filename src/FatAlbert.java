import java.util.Random;
import java.util.concurrent.TimeUnit;


public class FatAlbert extends Person{
	
	public FatAlbert(VendingMachine vending) {
		super(vending);
		
		//Set up the schedule
		new Thread(new Runnable(){
			public void run(){
				Random r = new Random();
				
				for(int i = 0; i < 15; i++){
					//Make at least two random requests today
					service.schedule(createRequest(), r.nextInt(1000) + i*1000, TimeUnit.MILLISECONDS);
					service.schedule(createRequest(), r.nextInt(1000) + i*1000, TimeUnit.MILLISECONDS);
					
					//Make another?
					if(r.nextBoolean()){
						service.schedule(createRequest(), r.nextInt(1000) + i*1000, TimeUnit.MILLISECONDS);
					}
					
					//Make another?
					if(r.nextBoolean()){
						service.schedule(createRequest(), r.nextInt(1000) + i*1000, TimeUnit.MILLISECONDS);
					}
				}
				
				shutdown();
			}
			
			
		}).start();
		
		
	}

	/**
	 * Same purpose as the super class method with the same name, but
	 * modified for Fat Alberts special requests.
	 * @return
	 */
	public Runnable createRequest(){
		return new Runnable() {
	        public void run() {
	        	
	        	//We want cookies
	            
	        	boolean ret1 = vending.getCookies(1);
	            
	            //We want candy bars
	            boolean ret2 = vending.getCandyBars(1);
	            
	            
	            if(ret1 && ret2){
	            	System.out.println("\t\t\tHey, hey hey!");
	            }else if(ret1){
	            	System.out.println("\t\t\tAt least I got a Cookie");
	            }else if(ret2){
	            	System.out.println("\t\t\tAt least I got a Candy Bar");
	            }else{
	            	System.out.println("\t\t\tNo food for me today");
	            }
	            
	          }
	        };
	}

}
