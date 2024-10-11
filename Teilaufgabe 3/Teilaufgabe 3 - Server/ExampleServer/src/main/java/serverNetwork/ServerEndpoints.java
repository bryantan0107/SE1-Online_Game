package serverNetwork;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import game.GameController;
import map.HalfMapValidator;
import map.MapController;
import map.MapNode;
import map.ServerFullMap;
import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import player.PlayerController;
import rules.GameIdExistsRule;
import rules.PlayerCountRule;
import rules.PlayerIdExistsRule;
import rules.PlayerTurnRule;
import server.exceptions.GenericExampleException;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
	private final static Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);
	
	private final GameController gameController = new GameController();
	private final PlayerController playerController = new PlayerController();
	private final NetworkConverter networkConverter = new NetworkConverter();
	
	/**
     * Creates a new game for the client and generates a unique game ID.
     *
     * @param enableDebugMode        Enables debug mode if true.
     * @param enableDummyCompetition Enables dummy competition mode if true.
     * @return UniqueGameIdentifier for the client.
     */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		
		// Create a new Game in the Server and generate an unique gameId.
		String newGameID = gameController.createNewGame();
		
		logger.info("Created a new game with id: " + newGameID + ".");
		
		// Convert the gameId to network format and return it.
		return networkConverter.convertToNetworkGameId(newGameID);
	}

	/**
     * Registers a new player in the specified game and generates a unique player ID.
     *
     * @param gameID             The unique identifier of the game.
     * @param playerRegistration The registration details of the player.
     * @return ResponseEnvelope containing the UniquePlayerIdentifier for the client.
     */
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		
		// Convert the game ID and player registration details to server format.
		String gameId = networkConverter.convertToServerGameId(gameID);
		List<String> playerInfo = networkConverter.convertToServerPlayerRegistration(playerRegistration);
		
		// Validate the game ID and player count.
		try {
            new GameIdExistsRule(gameController).validate(gameId);
            new PlayerCountRule(gameController).validate(gameId);
        } catch (GenericExampleException e) {
        	throw e;
        }
		
		// Create a new player and generate a unique player ID.
		String newPlayerID = playerController.createNewPlayer(gameId,playerInfo);

		logger.info("Created a new player with id: " + newPlayerID + " under game: " + gameId + ".");
		
		// Convert the player ID to network format and return it inside the ResponseEnvelope.
		return new ResponseEnvelope<>(networkConverter.convertToNetworkPlayerId(newPlayerID));
	}
	
	/**
     * Receives the half-map from the player and processes it.
     *
     * @param gameID The unique identifier of the game.
     * @param halfMap The half-map sent by the player.
     * @return ResponseEnvelope indicating the result of the operation.
     */
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<Object> receiveHalfMap(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerHalfMap halfMap) {
		
		// Convert the game ID and player ID to server format.
		String gameId = networkConverter.convertToServerGameId(gameID);
		String playerId = halfMap.getUniquePlayerID();
		
		// Validate the game ID, player ID, and player turn.
		try {
            new GameIdExistsRule(gameController).validate(gameId);
            new PlayerIdExistsRule(playerController).validate(playerId);
            new PlayerTurnRule(playerController).validate(playerId);
        } catch (GenericExampleException e) {
        	throw e;
        }
		
		// Determine if it's the second player sending the half-map.
		boolean isPLayerTwo = gameController.isPlayerTwoHalfMap(gameId);
		
		// Convert the half-map to server format and validate it.
		MapNode[][] serverHalfMap = networkConverter.convertToServerHalfMap(halfMap.getMapNodes(),isPLayerTwo);
		try {
			HalfMapValidator halfMapValidator = new HalfMapValidator();
            halfMapValidator.isValid(serverHalfMap, playerId, gameId);
        } catch (GenericExampleException e) {
        	throw e;
        }
		
		// Add the half-map to the map controller and generate a new game state ID.
		MapController mapController = new MapController();
		mapController.addHalfMap(serverHalfMap,playerId,gameId);
		gameController.generateGameStateId(gameId);
		
		logger.info("Received halfMap from player with id: " + playerId + " under game: " + gameId + ".");
		
		// Return a ResponseEnvelope.
		return new ResponseEnvelope<Object>();
	}
	
	/**
     * Sends the current game state to the specified player.
     *
     * @param gameID  The unique identifier of the game.
     * @param playerID The unique identifier of the player.
     * @return ResponseEnvelope containing the current GameState.
     */
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> sendGameState(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @PathVariable UniquePlayerIdentifier playerID) {
		
		// Convert the game ID and player ID to server format.
		String gameId = networkConverter.convertToServerGameId(gameID);
		String playerId = networkConverter.convertToServerPlayerId(playerID);
		
		// Validate the game ID and player ID.
		try {
            new GameIdExistsRule(gameController).validate(gameId);
            new PlayerIdExistsRule(playerController).validate(playerId);
        } catch (GenericExampleException e) {
        	throw e;
        }
		
		// Retrieve and convert the player states to network format.
		Collection<PlayerState> players = networkConverter.convertToNetworkPayerState(playerController.getPlayerState(gameId,playerId));
		
		// Retrieve the current game state ID.
		String gameStateID = gameController.getGameStateId(gameId);
		
		// Retrieve the full map if available and return the game state. Determine if it's the game state for the second Player.
		boolean isPlayerTwo = gameController.isPlayerTwo(gameId,playerId);
		ServerFullMap fullMap = gameController.getFullMap(gameId);
				if(fullMap != null && fullMap.isComplete()) {
					FullMap networkFullMap = new FullMap(networkConverter.convertToNetworkFullMap(fullMap.getFullMap(),isPlayerTwo));
					return new ResponseEnvelope<GameState>(new GameState(networkFullMap, players, gameStateID));
				}

		// Return the game state without the full map.
		return new ResponseEnvelope<GameState>(new GameState(players,gameStateID));
	}

	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
