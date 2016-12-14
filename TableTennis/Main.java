package application;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new TableTennis());
		t1.start();
	}

}
