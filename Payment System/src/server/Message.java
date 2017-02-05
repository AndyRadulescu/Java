package server;

import java.io.Serializable;
/**
 * 
 * This object is used for the transfer between the server and the client.
 *
 */
public class Message implements Serializable {//pt transfer server-client

	private static final long serialVersionUID = 1L;
	/**
	 * Command that is used to choose the specific operation( all commands can be found in the  SavedItems interface)
	 */
	private int action;
	/**
	 * Actual data that is exchanged between server and client
	 */
	private Object data;

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Message(int action, Object data) {
		this.action = action;
		this.data = data;
	}

	public Message() {

	}

}
