package serverNetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.EMapNodeAttribute;
import map.EMapNodeTerrain;
import map.MapNode;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import player.EServerPlayerGameState;
import player.Player;

public class NetworkConverter {
	private final static Logger logger = LoggerFactory.getLogger(NetworkConverter.class);
	private final Random random = new Random();
	
	public NetworkConverter() {};
	
	/**
     * Converts the server Game ID to a network Game ID.
     *
     * @param gameId The game ID in server format.
     * @return The game ID in network format.
     */
	public UniqueGameIdentifier convertToNetworkGameId(String gameId) {
		return new UniqueGameIdentifier(gameId);
	}
	
	/**
     * Converts the network Game ID to a server Game ID.
     *
     * @param gameID The game ID in network format.
     * @return The game ID in server format.
     */
	public String convertToServerGameId(UniqueGameIdentifier gameID) {
		return gameID.getUniqueGameID();
	}
	
	/**
     * Converts the network player registration details to a server format list of player information.
     *
     * @param playerRegistration The player registration details from the network.
     * @return A list of player information in server format.
     */
	public List<String> convertToServerPlayerRegistration(PlayerRegistration playerRegistration) {
		List<String> playerInfo = new ArrayList<>();
		playerInfo.add(playerRegistration.getStudentFirstName());
		playerInfo.add(playerRegistration.getStudentLastName());
		playerInfo.add(playerRegistration.getStudentUAccount());
		return playerInfo;
	}
	
	/**
     * Converts the server Player ID to a network Player ID.
     *
     * @param playerId The player ID in server format.
     * @return The player ID in network format.
     */
	public UniquePlayerIdentifier convertToNetworkPlayerId(String playerId) {
		return new UniquePlayerIdentifier(playerId);
	}
	
	/**
     * Converts the network Player ID to a server Player ID.
     *
     * @param playerID The player ID in network format.
     * @return The player ID in server format.
     */
	public String convertToServerPlayerId(UniquePlayerIdentifier playerID) {
		return playerID.getUniquePlayerID();
	}
	
	/**
     * Converts the network HalfMap to a server HalfMap in MapNode[][] format.
     *
     * @param halfMapNodes The collection of HalfMap nodes from the network.
     * @param isPlayerTwo  Indicates if the half map belongs to the second player.
     * @return The server HalfMap in MapNode[][] format.
     */
	public MapNode[][] convertToServerHalfMap(Collection<PlayerHalfMapNode> halfMapNodes, boolean isPLayerTwo) {
		int numRows = 0;
        int numCols = 0;

        for (PlayerHalfMapNode node : halfMapNodes) {
            numRows = Math.max(numRows, node.getX());
            numCols = Math.max(numCols, node.getY());
        }

        numRows++;
        numCols++;

        MapNode[][] gameHalfMap = new MapNode[numCols][numRows];

        for (PlayerHalfMapNode node : halfMapNodes) {
            int x = node.getX();
            int y = node.getY();
            
            gameHalfMap[y][x] = new MapNode(convertToEMapNodeTerrain(node.getTerrain()));

            // If it's the half map of the second player, set its fort and position as Enemy.
            if(node.isFortPresent()) {
            	if(isPLayerTwo) {
            		gameHalfMap[y][x].addAttribute(EMapNodeAttribute.ENEMY_FORT);
	            	gameHalfMap[y][x].addAttribute(EMapNodeAttribute.ENEMY_POSITION);
            	} else {
	            	gameHalfMap[y][x].addAttribute(EMapNodeAttribute.MY_FORT);
	            	gameHalfMap[y][x].addAttribute(EMapNodeAttribute.MY_POSITION);
            	}
            }
        }

        return gameHalfMap;
    }
	
	/**
     * Converts a list of server Player objects to a collection of network PlayerState objects.
     *
     * @param playerStates The list of Player objects in server format.
     * @return A collection of PlayerState objects in network format.
     */
	public Collection<PlayerState> convertToNetworkPayerState(List<Player> playerStates) {
		Collection<PlayerState> players = new ArrayList<>();
		
		for(Player player : playerStates) {
			EPlayerGameState state = convertToEPlayerGameState(player.getPlayerGameState());
			UniquePlayerIdentifier playerId = convertToNetworkPlayerId(player.getPlayerId());
			PlayerState playerState = new PlayerState(player.getFirstName(),player.getLastName(),player.getUAccount(),state,playerId,player.isCollectedTreasure());
			players.add(playerState);
		}
		
		return players;
	}
	
	/**
     * Converts the server FullMap to a collection of network FullMapNode objects.
     * If it is the second player, their position and fort need to be swapped.
     *
     * @param serverFullMap The FullMap in server format.
     * @param isPlayerTwo   Indicates if the map is for the second player.
     * @return A collection of FullMapNode objects in network format.
     */
    public Collection<FullMapNode> convertToNetworkFullMap(MapNode[][] serverFullMap, boolean isPlayerTwo) {
        Collection<FullMapNode> fullMapNodes = new ArrayList<>();

        for (int x = 0; x < serverFullMap.length; x++) {
            for (int y = 0; y < serverFullMap[0].length; y++) {
                FullMapNode fullMapNode = createFullMapNode(serverFullMap[x][y], isPlayerTwo, y, x);
                fullMapNodes.add(fullMapNode);
            }
        }

        reassignEnemyPosition(fullMapNodes);

        return fullMapNodes;
    }
    
    
	/* 
	 * From here are the private methods for the NetworkConverter class.
	 */

    /**
     * Creates a FullMapNode with proper attributes based on whether it is the second player.
     *
     * @param mapNode    The map node.
     * @param isPlayerTwo Indicates if it is for the second player.
     * @param y          The y-coordinate of the node.
     * @param x          The x-coordinate of the node.
     * @return A FullMapNode with appropriate attributes.
     */
    private FullMapNode createFullMapNode(MapNode mapNode, boolean isPlayerTwo, int y, int x) {
        EPlayerPositionState playerPos = convertToEPlayerPositionState(mapNode);
        ETreasureState treasure = convertToETreasureState(mapNode);
        EFortState fort = convertToEFortState(mapNode);
        ETerrain terrain = convertToETerrain(mapNode);

        if (isPlayerTwo) {
            playerPos = swapPlayerPosition(playerPos);
            fort = swapFortState(fort);
        }

        if (fort == EFortState.EnemyFortPresent) {
            fort = EFortState.NoOrUnknownFortState;
        }

        if (playerPos == EPlayerPositionState.EnemyPlayerPosition) {
            return new FullMapNode(terrain, EPlayerPositionState.NoPlayerPresent, treasure, fort, y, x);
        } else {
            return new FullMapNode(terrain, playerPos, treasure, fort, y, x);
        }
    }

    /**
     * Swaps the player position state for the second player.
     *
     * @param playerPos The original player position state.
     * @return The swapped player position state.
     */
    private EPlayerPositionState swapPlayerPosition(EPlayerPositionState playerPos) {
        if (playerPos == EPlayerPositionState.EnemyPlayerPosition) {
            return EPlayerPositionState.MyPlayerPosition;
        } else if (playerPos == EPlayerPositionState.MyPlayerPosition) {
            return EPlayerPositionState.EnemyPlayerPosition;
        }
        return playerPos;
    }

    /**
     * Swaps the fort state for the second player.
     *
     * @param fort The original fort state.
     * @return The swapped fort state.
     */
    private EFortState swapFortState(EFortState fort) {
        if (fort == EFortState.EnemyFortPresent) {
            return EFortState.MyFortPresent;
        } else if (fort == EFortState.MyFortPresent) {
            return EFortState.EnemyFortPresent;
        }
        return fort;
    }

    /**
     * Reassigns the enemy position to a new random position.
     *
     * @param fullMapNodes The collection of FullMapNodes.
     */
    private void reassignEnemyPosition(Collection<FullMapNode> fullMapNodes) {
        List<FullMapNode> nonEnemyPositions = new ArrayList<>(fullMapNodes);

        int randomIndex = random.nextInt(nonEnemyPositions.size());
        FullMapNode randomNonEnemyPosNode = nonEnemyPositions.get(randomIndex);
        fullMapNodes.remove(randomNonEnemyPosNode);
        fullMapNodes.add(new FullMapNode(randomNonEnemyPosNode.getTerrain(), EPlayerPositionState.EnemyPlayerPosition, randomNonEnemyPosNode.getTreasureState(), randomNonEnemyPosNode.getFortState(), randomNonEnemyPosNode.getX(), randomNonEnemyPosNode.getY()));
    }
	
    /**
     * Converts the server player game state to a network player game state.
     *
     * @param state The player game state in server format.
     * @return The player game state in network format.
     */
	private EPlayerGameState convertToEPlayerGameState(EServerPlayerGameState state) {
		EPlayerGameState playerGameState = null;
		
		switch (state) {
		case MustAct:
			playerGameState = EPlayerGameState.MustAct;
			break;
		case Won:
			playerGameState = EPlayerGameState.Won;
			break;
		case Lost:
			playerGameState = EPlayerGameState.Lost;
			break;
		case MustWait:
			playerGameState = EPlayerGameState.MustWait;	
		default:
			break;
		}
		
		return playerGameState;
	}
	
	/**
     * Converts the map node's attributes to a network player position state.
     *
     * @param mapNode The map node.
     * @return The player position state in network format.
     */
	private EPlayerPositionState convertToEPlayerPositionState(MapNode mapNode) {
		EPlayerPositionState playerPos = null;
		
    	if(mapNode.hasAttribute(EMapNodeAttribute.MY_POSITION) && mapNode.hasAttribute(EMapNodeAttribute.ENEMY_POSITION)) {
    		playerPos = EPlayerPositionState.BothPlayerPosition;
    	} else if(mapNode.hasAttribute(EMapNodeAttribute.MY_POSITION)) {
    		playerPos = EPlayerPositionState.MyPlayerPosition;
    	} else if(mapNode.hasAttribute(EMapNodeAttribute.ENEMY_POSITION)) {
    		playerPos = EPlayerPositionState.EnemyPlayerPosition;
    	} else {
    		playerPos = EPlayerPositionState.NoPlayerPresent;
    	}

		return playerPos;
	}
	
	/**
     * Converts the map node's attributes to a network treasure state.
     *
     * @param mapNode The map node.
     * @return The treasure state in network format.
     */
	private ETreasureState convertToETreasureState(MapNode mapNode) {
		ETreasureState treasure = null;

    	if(mapNode.hasAttribute(EMapNodeAttribute.MY_TREASURE)) {
    		treasure = ETreasureState.MyTreasureIsPresent;
    	} else {
    		treasure = ETreasureState.NoOrUnknownTreasureState;
    	}

		return treasure;
	}
	
	/**
     * Converts the map node's attributes to a network fort state.
     *
     * @param mapNode The map node.
     * @return The fort state in network format.
     */
	private EFortState convertToEFortState(MapNode mapNode) {
		EFortState fort = null;
		          	
    	if(mapNode.hasAttribute(EMapNodeAttribute.MY_FORT)) {
    		fort = EFortState.MyFortPresent;
    	} else if(mapNode.hasAttribute(EMapNodeAttribute.ENEMY_FORT)) {
    		fort = EFortState.EnemyFortPresent;
    	} 
    	else {
    		fort = EFortState.NoOrUnknownFortState;
    	}

		return fort;
	}
	
	/**
     * Converts the map node's terrain to a network terrain.
     *
     * @param mapNode The map node.
     * @return The terrain in network format.
     */
	private ETerrain convertToETerrain(MapNode mapNode) {
		ETerrain terrain = null;
          		
        EMapNodeTerrain nodes = mapNode.getTerrain();
        switch (nodes) {
            case MOUNTAIN:
            	terrain = ETerrain.Mountain;
                break;
            case GRASS:
            	terrain = ETerrain.Grass;
                break;
            case WATER:
            	terrain = ETerrain.Water;
                break;
            default:
                break;
        }

		return terrain;
	}
	
	/**
     * Converts the network terrain to a server map node terrain.
     *
     * @param terrain The terrain in network format.
     * @return The terrain in server format.
     */
    private EMapNodeTerrain convertToEMapNodeTerrain(ETerrain terrain) {
        switch (terrain) {
            case Mountain:
                return EMapNodeTerrain.MOUNTAIN;
            case Grass:
                return EMapNodeTerrain.GRASS;
            case Water:
                return EMapNodeTerrain.WATER;
            default:
                throw new IllegalArgumentException("Unknown terrain type: " + terrain);
        }
    }
	
}
