package clientNetwork;

import java.util.ArrayList;
import java.util.Collection;

import map.MapNode;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;

/**
 * Converter that convert Data to Network or Client version
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class NetworkConverter {
	
	// Initiate NetworkConverter
	public NetworkConverter() {
	}
	
	// Convert ClientHalfMap to NetworkHalfMap
	public Collection<PlayerHalfMapNode> convertToNetworkHalfMap(MapNode[][] clientObject) {
        Collection<PlayerHalfMapNode> allnodes = new ArrayList<>();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                int nodes = clientObject[x][y].getType();
                switch (nodes) {
                    case 1:
                        allnodes.add(new PlayerHalfMapNode(x, y, ETerrain.Mountain));
                        break;
                    case 2:
                        allnodes.add(new PlayerHalfMapNode(x, y, ETerrain.Grass));
                        break;
                    case 3:
                        allnodes.add(new PlayerHalfMapNode(x, y, ETerrain.Water));
                        break;
                    case 4:
                        allnodes.add(new PlayerHalfMapNode(x, y, true, ETerrain.Grass));
                        break;
                    default:
                        allnodes.add(new PlayerHalfMapNode(x, y, ETerrain.Grass));
                        break;
                }
            }
        }

        return allnodes;
    }
	
	// Convert NetworkFullMap to ClientFullMap
	public MapNode[][] convertToClientFullMap(Collection<FullMapNode> fullMapNodes) {
		int numRows = 0;
        int numCols = 0;

        for (FullMapNode node : fullMapNodes) {
            numRows = Math.max(numRows, node.getX());
            numCols = Math.max(numCols, node.getY());
        }

        numRows++;
        numCols++;

        MapNode[][] gameFullMap = new MapNode[numCols][numRows];

        for (FullMapNode node : fullMapNodes) {
            int x = node.getX();
            int y = node.getY();
            int value = 0;

            switch (node.getTerrain()) {
                case Mountain:
                    value = 1;
                    break;
                case Grass:
                    value = 2;
                    break;
                case Water:
                    value = 3;
                    break;
                default:
                    value = 0;
                    break;
            }

            gameFullMap[y][x] = new MapNode(value);
            if(node.getFortState()==EFortState.MyFortPresent) {
            	gameFullMap[y][x].setMyFort(true);
            }
            if(node.getFortState()==EFortState.EnemyFortPresent) {
            	gameFullMap[y][x].setEnemyFort(true);
            }
            if(node.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition || node.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition) {
            	gameFullMap[y][x].setMyPosition(true);
            } else {gameFullMap[y][x].setMyPosition(false);}
            
            if(node.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition || node.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition) {
            	gameFullMap[y][x].setEnemyPosition(true);
            } else gameFullMap[y][x].setEnemyPosition(false);
            
            if(node.getTreasureState() == ETreasureState.MyTreasureIsPresent){
            	gameFullMap[y][x].setMyTreasure(true);
            }
        }

        return gameFullMap;
    }
	
	// ClientMove to NetworkMove
	public EMove convertToNetworkMove(int[] move) {
		EMove MoveToSend = null;
        if (move[0] == -1 && move[1] == 0) {
            MoveToSend = EMove.Left;
        } else if (move[0] == 0 && move[1] == 1) {
            MoveToSend = EMove.Down;
        } else if (move[0] == 1 && move[1] == 0) {
            MoveToSend = EMove.Right;
        } else if (move[0] == 0 && move[1] == -1) {
            MoveToSend = EMove.Up;
        }

        return MoveToSend;
    }
	
}
