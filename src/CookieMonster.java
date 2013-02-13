import java.util.concurrent.TimeUnit;


public class CookieMonster extends Person{

	public CookieMonster(VendingMachine vending) {
		super(vending);
		
		new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < 15; i++){
					service.schedule(createRequest(true, "\tMe love cookies", "\tMe hungry", 1), 500 + i, TimeUnit.MILLISECONDS);
					service.schedule(createRequest(true, "\tMe love cookies", "\tMe hungry", 1), 1 + i, TimeUnit.SECONDS);
				}
		
				shutdown();
			}
		}).start();
	}

}
