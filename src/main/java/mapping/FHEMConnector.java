package mapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.Authenticator;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import io.swagger.HomeAutomationServerConnector;
import io.swagger.exception.HomeAutomationServerNotReachableException;
import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.get.JsonParser;
import mapping.get.WebsocketParser;
import mapping.set.FHEMCommandBuilder;

@Component
public class FHEMConnector implements HomeAutomationServerConnector, ApplicationListener<ContextClosedEvent> {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String url;
	private final int port;
	
	private final String authUsername;
	private final String authPassword;
	private final int fhemConnectionTimeout;
	
	private WebSocketClient websocket;
	private WebsocketParser websocketParser;
	
	private JsonParser jsonParser;
	private FHEMCommandBuilder commandBuilder;
	
	// Using Hashtable rather than HashMap because Hashtable is threadsafe, HashMap is not!
	private Map <String, Device> deviceMap = new Hashtable<>();
	
	@Autowired
	public FHEMConnector(
			@Value("${fhem.url}") String fhemUrl, 
			@Value("${fhem.port}") int fhemPort, 
			@Value("${fhem.timeout}") int fhemConnectionTimeout, 
			@Value("${fhem.username}") String authUsername,
			@Value("${fhem.password}") String authPassword) {
		
		this.url = fhemUrl;
		this.port = fhemPort;
		this.fhemConnectionTimeout = fhemConnectionTimeout;
		
		this.authUsername = authUsername;
		this.authPassword = authPassword;
		
		this.websocketParser = new WebsocketParser();
		
		this.jsonParser = new JsonParser();
		this.commandBuilder = new FHEMCommandBuilder();
		
		if (isFHEMReachable()) {
			log.info("Accessing FHEM @ "+fhemUrl+":"+fhemPort);
			reload();		
		} else {
			log.error("FHEM is NOT reachable @ "+fhemUrl+":"+fhemPort);
			/*
			 * TODO: !!!
			 *
			
			log.error("FHEM is NOT reachable @ "+fhemUrl+":"+fhemPort);
			waitForFHEMisReachable();
			log.info("FHEM is now accessable @ "+fhemUrl+":"+fhemPort);
			// Then 
			reload(); // start
			*/
		}
	} 
	
	private void startWebsocket(long now) {
		
		String since = null;
		String filter = ".*";
		String nowString = Long.toString(now);
		FHEMConnector itsMe = this;
		
		String query = "?XHR=1&inform=type=status;addglobal=1;filter="+filter+";since="+since+";fmt=JSON;&timestamp="+nowString;
		
		int connectionTimeout = 1000;
		
		Map<String, String> httpHeader = new HashMap<>();
		
		if (useCredentials()) {
			String encoded = Base64.getEncoder().encodeToString((authUsername + ':' + authPassword).getBytes(StandardCharsets.UTF_8));
			httpHeader.put("Authorization", "Basic "+encoded);
		}
		
		try {
			URI uri = new URI("ws://"+url+":"+port+"/fhem.pl"+query);
			
			websocket = new WebSocketClient(uri, new Draft_17(), httpHeader, connectionTimeout) {

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					 log.info("Opened new websocket connection to FHEM");
					// TODO
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					String msg = "Closed websocket with exit code " + code;
					if (!reason.isEmpty()) {
						msg += ", additional info: "+reason;
					}
					log.warn(msg);
					// TODO
				}

				@Override
				public void onMessage(String message) {
					websocketParser.update(message, deviceMap, itsMe);
				}

				@Override
				public void onError(Exception ex) {
					log.error("Error at websocket connection to FHEM", ex);
					// TODO
				}
			};
			websocket.connect();
		} catch (Exception e) {
			log.error("Something went wrong with the websocket", e);
		}
	}
	
	private void closeWebsocket() {
		if (websocket != null) websocket.close();
		websocket = null;
	}
	
	private String sendFhemCommand(String command) throws HomeAutomationServerNotReachableException {
		
		if (command == null || command.isEmpty()) {
			log.warn("Command to get sent to FHEM is null, ignoring");
			return null;
		}
		
		log.info("Sending command to FHEM: '"+command+"'");
		
		int responseCode = 0;
		try {
			String commandEnc = URLEncoder.encode(command, "UTF-8");
			String fullUrl = "http://"+url+":"+port+"/fhem?cmd="+commandEnc+"&XHR=1";
			URL obj = new URL(fullUrl);
			
			if (useCredentials()) {
				Authenticator.setDefault (new Authenticator() {
				    protected PasswordAuthentication getPasswordAuthentication() {
				        return new PasswordAuthentication (authUsername, authPassword.toCharArray());
				    }
				});
			}
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setConnectTimeout(fhemConnectionTimeout);
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
			
		} catch (SocketTimeoutException e0) {
			log.error("Error on FHEM connection: "+e0.getMessage());
			throw new HomeAutomationServerNotReachableException(e0.getMessage());
		} catch (ConnectException e1) {
			log.error("Error on FHEM connection: "+e1.getMessage());
			throw new HomeAutomationServerNotReachableException(e1.getMessage());
		} catch (IOException e2) {
			switch(responseCode) {
			case 400:
				log.error("FHEM connection returned 401: Bad Request");
				break;
			case 401:
				log.error("FHEM connection returned 401: Unauthorized");
				break;
			default:
				log.error("Unknown FHEM connection error", e2);
			}
			throw new HomeAutomationServerNotReachableException(e2.getMessage());
		}
	}
	
	private boolean isFHEMReachable() {
	    try {
	        try (Socket soc = new Socket()) {
	            soc.connect(new InetSocketAddress(url, port), fhemConnectionTimeout);
	        }
	        return true;
	    } catch (IOException ex) {
	        return false;
	    }
	}
	
	private void waitForFHEMisReachable() {
		
		int maxSeconds = 60*1; // 5 minutes
		
		long startTime = System.currentTimeMillis();
		while(true){
			
			/*
			if ((System.currentTimeMillis()-startTime) > maxSeconds*1000) {
				log.info("FHEM was not reachable for "+maxSeconds+" seconds, shutting application down");
				
				// TODO
			}*/
			
			if (!isFHEMReachable()) {
				log.error("Can not connect to FHEM, trying again ...");
		        try {
		            Thread.sleep(3000); // 3 seconds
		        } catch(InterruptedException ie){
		            log.error("While sleeping for next reachability test on FHEM an error occured: "+ie.getMessage());
		        }
		    }  else {
		    	return;
		    }
		}
	}
	
	private boolean useCredentials() {
		if (authUsername!=null && !authUsername.isEmpty() && authPassword!=null && !authPassword.isEmpty()) return true;
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 * 
	 * Closes connections before Bean gets destroyed
	 * 
	 */
	@Override
	public void onApplicationEvent(ContextClosedEvent arg0) {
		log.info("Closing websocket connection to FHEM, software is shutting down");
		closeWebsocket();
	}
	
	// --------------------------------------------------------------------------------- //
	

	
	public boolean reload() {
		
		closeWebsocket();
		
		String jsonlist2 = null;
		
		try {
			jsonlist2 = sendFhemCommand("jsonlist2");
		} catch (HomeAutomationServerNotReachableException e) {
			
			// TODO: handle exception
			
			log.error("While requesting 'jsonlist2Ä' from FHEM an error occured "+e.getMessage());
		}
		long now = System.currentTimeMillis() / 1000l;
		
		if (jsonlist2 == null || jsonlist2.isEmpty()) {
			return false;
		}
		
		startWebsocket(now);
				
		List<Device> devices = jsonParser.parse(jsonlist2);
		deviceMap = devices.stream().collect(Collectors.toMap(Device::getDeviceId, Device -> Device));
		
		log.info("Retrieved "+deviceMap.size()+" devices from FHEM via jsonlist2");
		return true;
	}
	
	public List<Device> getDevices() throws HomeAutomationServerNotReachableException {	
		if (isFHEMReachable()) {
			return new ArrayList<Device>(deviceMap.values());
		} else {
			throw new HomeAutomationServerNotReachableException();
		}
	}
	
	public boolean setDevices(List<Device> devices, Function functionValuesToSet) throws HomeAutomationServerNotReachableException {
		try {
			String  command = commandBuilder.buildCommand(devices, functionValuesToSet);
			sendFhemCommand(command);
			return true;
		} catch (NullPointerException e2) {
			// TODO: handle exception
			log.error("CommandBuilder returned null", e2);
			return false;
		}	
	}
	
	/*
	 * Just for testing purposes
	 */
	public String getJsonlist2MockupAsStringFromFile() {
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
				log.error("Reading testdata from file failed", e2);
				return "";
			}
		}
	}
}
