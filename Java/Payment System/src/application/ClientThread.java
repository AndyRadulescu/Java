package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import server.Message;
/**
 * 
 * Thread that is used to send/recive messages to/from server.
 *
 */
public class ClientThread implements Callable<Message> {//thread care trimite/primeste date la server
	private Message ms;

	@Override
	public Message call() throws Exception {
		Socket socket = new Socket(SavedItems.HOST, SavedItems.PORT);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		out.writeObject(ms);
		Message resp = (Message) in.readObject();
		socket.close();
		return resp;
	}

	public ClientThread(Message ms) {
		this.ms = ms;
	}

	public Message getMs() {
		return ms;
	}

	public void setMs(Message ms) {
		this.ms = ms;
	}
}
