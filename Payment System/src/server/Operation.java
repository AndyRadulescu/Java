package server;
/**
 * 
 * Interface used for chooseOperation() from ClientServerThread class(
 *
 */
public interface Operation {
	public Message doOperation(Message message);

}
