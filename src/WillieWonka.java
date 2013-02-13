import java.util.concurrent.TimeUnit;
import java.util.Random;


public class WillieWonka extends Person{

	public WillieWonka(VendingMachine vending) {
		super(vending);
		
		new Thread(new Runnable(){
			public void run(){
				Random r = new Random();
				
				for(int i = 0; i < 15; i++){
					service.schedule(createRequest(true, "\t\tThe Candy Man Can", "\t\tViolet - you're turning violet", 1), r.nextInt(1000) + i*1000, TimeUnit.MILLISECONDS);
				}
				
				shutdown();
			}
		}).start();
	}

}
