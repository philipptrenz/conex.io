package mapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.get.JsonParser;
import mapping.get.WebsocketParser;
import mapping.set.FHEMCommandBuilder;

public class FHEMConnector {
	
	private static FHEMConnector instance = null;
	
	private String ipAddress;
	private int port;
	private float fhemVersion;
	
	private WebSocketClient websocket;
	private WebsocketParser websocketParser;
	
	private JsonParser jsonParser;
	private FHEMCommandBuilder commandBuilder;
	
	private Map <String, Device> deviceMap = new HashMap<>();
	
	private FHEMConnector(String ipAddress, int port, String fhemVersion) {
		
		this.ipAddress = ipAddress;
		this.port = port;
		this.fhemVersion = Float.parseFloat(fhemVersion);
		
		// TODO: add fhem version validation (websockets for >= 5.8)
		
		this.websocketParser = new WebsocketParser();
		
		this.jsonParser = new JsonParser();
		this.commandBuilder = new FHEMCommandBuilder(this);
		
		// TODO: Validate ipAddress and port
		
		reload();		
	}
	
	private void startWebsocket(long now) {
		
		String since = null;
		String filter = ".*";
		String nowString = Long.toString(now);
		FHEMConnector itsMe = this;
		
		String query = "?XHR=1&inform=type=status;addglobal=1;filter="+filter+";since="+since+";fmt=JSON;&timestamp="+nowString;
		
		try {
			URI uri = new URI("ws://"+ipAddress+":"+port+"/fhem.pl"+query);
			
			websocket = new WebSocketClient(uri) {

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					System.out.println("new connection opened");
					// TODO
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					System.out.println("closed with exit code " + code + " additional info: " + reason);
					// TODO
				}

				@Override
				public void onMessage(String message) {
					websocketParser.update(message, deviceMap, itsMe);
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
					// TODO
				}
			};
			websocket.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeWebsocket() {
		if (websocket != null) websocket.close();
		websocket = null;
	}
	
	private String sendFhemCommand(String command) throws IOException {
		
		String commandEnc = URLEncoder.encode(command, "UTF-8");
		String url = "http://"+ipAddress+":"+port+"/fhem?cmd="+commandEnc+"&XHR=1";
		URL obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
	// --------------------------------------------------------------------------------- //
	
	public static FHEMConnector getInstance(String ipAddress, int port, String fhemVersion) {
		if(instance == null) {
	         instance = new FHEMConnector(ipAddress, port, fhemVersion);
	      }
	      return instance;
	}
	
	public boolean reload() {
		
		closeWebsocket();
		
		String jsonlist2 = null;
		try {
			jsonlist2 = sendFhemCommand("jsonlist2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long now = System.currentTimeMillis() / 1000l;
		
		if (jsonlist2 == null || jsonlist2.isEmpty()) {
			System.err.println("No jsonlist2 data received! 'longpoll' has to be set to 'websocket' and since FHEM 5.8 'csrfToken' must be 'none'.");
			return false;
		}
		
		startWebsocket(now);
		
		List<Device> devices = jsonParser.parse(jsonlist2);
		deviceMap = devices.stream().collect(Collectors.toMap(Device::getDeviceId, Device -> Device));
		return true;
	}
	
	public List<Device> getDevices() {
		return (List<Device>) deviceMap.values();
	}
	
	public boolean setDevices(List<Device> devices, Function functionValuesToSet) {
	
		try {
			sendFhemCommand(commandBuilder.buildCommand(devices, functionValuesToSet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NullPointerException e2) {
			// TODO: handle exception
			e2.printStackTrace();
			return false;
		}	
		
		return true;
	}
	
	/*
	 * Just for testing purposes
	 */
	public static String getJsonlist2MockupAsStringFromFile() {
		byte[] encoded;
		try {
			String file;
			file = "jsonlist2_p.a.trick_sensitive.json";
			//file = "jsonlist2_cooltux_sensitive.json";
			
			encoded = Files.readAllBytes(Paths.get("testdata/"+file));
			return new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			try {
				encoded = Files.readAllBytes(Paths.get("testdata/jsonlist2_20170324.json"));
				return new String(encoded, StandardCharsets.UTF_8);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return "";
			}
		}
	}	
}
