package client;

public class ServerMessage {
	public static final byte IDLE_MESSAGE = 0;
	public static final byte MOVIE_MESSAGE = 1;
	public static final byte DISCONNECT_MESSAGE = 2;
	
	private int id;
	private byte message;
	
	public ServerMessage(int id, byte message) {
		this.id = id;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public byte getMessage() {
		return message;
	}
}
