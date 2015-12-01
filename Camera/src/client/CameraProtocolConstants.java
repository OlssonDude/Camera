package client;

public interface CameraProtocolConstants {
	public static final byte CLIENT_IDLE_MESSAGE = 0;
	public static final byte CLIENT_MOVIE_MESSAGE = 1;
	public static final byte CLIENT_DISCONNECT_MESSAGE = 2;
	public static final byte CLIENT_FORCE_NONE = 127;
	public static final byte CLIENT_FORCE_MOVIE = 126;
	public static final byte CLIENT_FORCE_IDLE = 125;

	public static final byte SERVER_MOTION_MESSAGE = 0;
	public static final byte SERVER_IMAGE_MESSAGE = 1;
	public static final int SERVER_IMAGE_HEADER_LENGTH = 13;
}
