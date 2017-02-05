package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 * 
 * Main class of the server
 *
 */
public class ServerNetworkMain {//clasa main a serverului
	private ServerSocket ss = null;
	public static final int PORT = 10004;
	private volatile boolean cond = true;

	public static void main(String[] args) throws IOException {
		new ServerNetworkMain().startServer();

	}

	public void startServer() throws IOException {
		Semaphore sm = new Semaphore(1, true);
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("Sist_bancar");
		try {
			ss = new ServerSocket(PORT);
			ExecutorService ex = Executors.newCachedThreadPool();
			Thread autom = new Thread(new DbOnlyThread(sm,emf));
			System.out.println("Accepting connections on port " + PORT);
			ex.submit(autom);
			while (cond) {
				Socket sock = ss.accept();
				ex.execute(new Thread(new ClientServerThread(sock,sm,emf)));
			}
			ss.close();
			emf.close();
		} catch (BindException e) {
			System.out.println(PORT + " already in use");
		}

	}
}