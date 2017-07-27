package develop;

import java.net.SocketException;

public class Main {

	public static void main(String[] args) throws InterruptedException, SocketException {
		Thread t1 = new Thread(new TableTennis());
		t1.start();
	}

}
