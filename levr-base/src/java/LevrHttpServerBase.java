import com.eduworks.levr.server.LevrHttpServer;

public class LevrHttpServerBase
{

	/**
	 * Activate the server on a specified port (9722 by default).
	 * 
	 * @param args
	 *            if present, the first is used as the port
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : LevrHttpServer.DEFAULT_PORT;
		LevrHttpServer.getInstance().setupMetadataServer(port);
		LevrHttpServer.getInstance().startMetadataServer();
	}
}
