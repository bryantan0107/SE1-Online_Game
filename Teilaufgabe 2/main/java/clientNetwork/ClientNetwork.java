package clientNetwork;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import exceptions.NetworkCommunicationException;
import map.MapNode;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.*;
import reactor.core.publisher.Mono;

/**
 * The Network of the Client that communicate with the Server
 */
public class ClientNetwork {
	
	private final static Logger logger = LoggerFactory.getLogger(ClientNetwork.class);

    private String gameId;
    private String playerId;
    private WebClient baseWebClient;
    private NetworkConverter networkConverter;

    // Initiate ClientNetwork and connect to the Server
	public ClientNetwork(String serverBaseUrl, String gameId) {
		logger.info("Connecting to the Server...");
		
		this.gameId = gameId;
		baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
		
		networkConverter = new NetworkConverter();
		
		logger.info("Successfully connected to the Server.");
	}

	// Register Client
	public void registerClient(String firstName, String lastName, String studentId) {
		logger.info("Registering a new Client...");
		
		PlayerRegistration playerReg = new PlayerRegistration(firstName,lastName,studentId);
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromValue(playerReg)) 
				.retrieve().bodyToMono(ResponseEnvelope.class); 
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
		} else {
			UniquePlayerIdentifier uniqueID = resultReg.getData().get();
			playerId = uniqueID.getUniquePlayerID();
			logger.info("Successfully registered client: your playerId is " + playerId);
		}
	}
	
	// Get GameState, in order to get FullMap and PlayerState later
	public GameState getGameState() {
        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
                .uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class);
        ResponseEnvelope<GameState> requestResult = webAccess.block();

        if (requestResult.getState() == ERequestState.Error) {
            throw new NetworkCommunicationException(requestResult.getExceptionMessage());
        }
        
        return requestResult.getData().get();
    }
	
	// Get PlayerState, in order to get collectedTreasure and EPlayerGameState later
	public PlayerState getPlayerState() {
		PlayerState playerState = null;
        for (PlayerState player : getGameState().getPlayers()) {
            if (player.getUniquePlayerID().equals(playerId))
            	playerState = player;
        }
        return playerState;
    }

	// Check if it's Client turn to act
	public boolean isTurn() {
		//logger.info("Checking if it's my turn...");
		if (getPlayerState().getState().equals(EPlayerGameState.MustAct)) {
			//logger.info("It's your turn now.");
            return true;
		}
		if (getPlayerState().getState().equals(EPlayerGameState.MustWait)) {
			//logger.info("It's not you turn yet.");
		}
        return false;
	}
	
	// Check if Client has won the game
	public boolean hasWon() {
        if (getPlayerState().getState().equals(EPlayerGameState.Won)) {
        	logger.info("You have won the game.");
            return true;
        }
        return false;
    }

	// Check if Client has lost the game
    public boolean hasLost() {
        if (getPlayerState().getState().equals(EPlayerGameState.Lost)) {
        	logger.info("You have lost the game.");
            return true;
        }
        return false;
    }
    
    // Check if the treasure has been collected
    public boolean collectedTreasure() {
		if (getPlayerState().hasCollectedTreasure()) {
			logger.info("You have collected the treasure! Now go to find enemy castle!");
			return true;
		}
		logger.info("Haven't found treasure...");
		return false;
	}
	
	// Send HalfMap to the Server
    // Here will use networkConverter to convert ClientHalfMap to NetworkHalfMap first before send it to Server
	public void sendHalfMap(MapNode[][] gameHalfMap) {
        logger.info("Sending Halfmap to Server...");
        
        Collection<PlayerHalfMapNode> allnodes = networkConverter.convertToNetworkHalfMap(gameHalfMap);

        PlayerHalfMap halfmap = new PlayerHalfMap(playerId, allnodes);

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps/")
                .body(BodyInserters.fromValue(halfmap)).retrieve().bodyToMono(ResponseEnvelope.class);
        ResponseEnvelope<GameState> requestResult = webAccess.block();

        if (requestResult.getState() == ERequestState.Error) {
            logger.info("Failed to send the Halfmap to Server.");
            throw new NetworkCommunicationException(requestResult.getExceptionMessage());
        }
        
        logger.info("Successfully sent the Halfmap to Server.");
    }
    
	// Get FullMap from the Server
	// Here will use networkConverter to convert NetworkFullMap to ClientFullMap
    public MapNode[][] GetFullMap() {
        logger.info("Getting FullMap from Server...");
        
        Collection<FullMapNode> fullMapNodes = getGameState().getMap().getMapNodes();
        
        MapNode[][] gameFullMap = networkConverter.convertToClientFullMap(fullMapNodes);

        logger.info("Successfully got the FullMap from Server.");
        return gameFullMap;
    }
    
    // Send move to the Server
    // Here will use networkConverter to convert ClientMove to NetworkMove first before send it to Server
    public void sendMove(int[] move) {
        logger.info("Sending Move to Server...");
        
        EMove MoveToSend = networkConverter.convertToNetworkMove(move);
        
        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
                .body(BodyInserters.fromValue(PlayerMove.of(playerId, MoveToSend))).retrieve()
                .bodyToMono(ResponseEnvelope.class);
        ResponseEnvelope<GameState> requestResult = webAccess.block();

        if (requestResult.getState() == ERequestState.Error) {
            throw new NetworkCommunicationException(requestResult.getExceptionMessage());
        }
        
        logger.info("Successfully sent the Move to Server.");
    }
}
