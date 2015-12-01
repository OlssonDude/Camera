package client;

public class ServerMessage {
//	public static final byte IDLE_MESSAGE = 0;
//	public static final byte MOVIE_MESSAGE = 1;
//	public static final byte DISCONNECT_MESSAGE = 2;
//	public static final byte FORCE_NONE = 127;
//	public static final byte FORCE_MOVIE = 126;
//	public static final byte FORCE_IDLE = 125;

	private byte message;

	public ServerMessage(byte message) {
		this.message = message;
	}

	public byte toByte() {
		return message;
	}
}
