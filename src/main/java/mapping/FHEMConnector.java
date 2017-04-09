package mapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class FHEMConnector {

	public static String getJsonlist2Result() {

		// TODO: Implement real network connector to get jsonlist2
		// maybe: catch first time by jsonlist2, then wait on websockets
		// for pushed data via longpoll
		
		try {
			URI uri = new URI("ws://192.168.0.106:8083/fhem");
			WebSocketClient client = new WebSocketClient(uri) {

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					System.out.println("new connection opened");
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					System.out.println("closed with exit code " + code + " additional info: " + reason);
				}

				@Override
				public void onMessage(String message) {
					System.out.println("received message: " + message);
				}

				@Override
				public void onError(Exception ex) {
					System.err.println("an error occurred:" + ex);
				}
			};
			
			client.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return getJsonlist2MockupAsStringFromFile();
	}

	/*
	 * Just for testing purposes
	 */
	private static String getJsonlist2MockupAsStringFromFile() {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("testdata/jsonlist2_20170324.json"));
			return new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}	

	class EmptyClient extends WebSocketClient {

		public EmptyClient(URI serverUri, Draft draft) {
			super(serverUri, draft);
		}

		public EmptyClient(URI serverURI) {
			super(serverURI);
		}

		@Override
		public void onOpen(ServerHandshake handshakedata) {
			System.out.println("new connection opened");
		}

		@Override
		public void onClose(int code, String reason, boolean remote) {
			System.out.println("closed with exit code " + code + " additional info: " + reason);
		}

		@Override
		public void onMessage(String message) {
			System.out.println("received message: " + message);
		}

		@Override
		public void onError(Exception ex) {
			System.err.println("an error occurred:" + ex);
		}
	}
}
