package action;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.MapNode;

/**
 * The Move Generator of the Client
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class MoveGenerator {
	private final static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);
	
    private MapNode[][] fullMap;
    private int[] myLocation;
    private boolean collectedTreasure=false;
    private int[] foundTreasure;
    private int[] foundEnemyFort;
    
    public MoveGenerator() {
    }
	
	public void setFoundTreasure(int[] foundTreasure) {
    	this.foundTreasure=foundTreasure;
    }
	
	public void setFoundEnemyFort(int[] foundEnemyFort) {
    	this.foundEnemyFort=foundEnemyFort;
    }
    
    public void setCollectedTreasure(boolean collectedTreasure) {
    	this.collectedTreasure=collectedTreasure;
    }

    // update visited area beased on myPostion
    public void updateVisitedArea(int[] location, MapNode[][] map) {
        this.myLocation = location;
        this.fullMap = map;
        
        fullMap[myLocation[1]][myLocation[0]].setVisited(true);
        
        // For the case when myPosition is on the Mountain
        if(fullMap[myLocation[1]][myLocation[0]].getType()==1) { 
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int x = myLocation[1] + dx;
                    int y = myLocation[0] + dy;
                    if(collectedTreasure==false) {
	                    if (x >= 0 && x < fullMap.length && y >=0 && y < fullMap[0].length && fullMap[x][y].getMyMapArea()) {
	                        fullMap[x][y].setVisited(true);
	                    }
                    } else {
                    	if (x >= 0 && x < fullMap.length && y >=0 && y < fullMap[0].length) {
	                        fullMap[x][y].setVisited(true);
                    	}
                    }
                }
            }
		}
    }
    
    /**
	 * Main method, generate final bestMove
	 */
    public int[] generateMove() {
        int minDistance = Integer.MAX_VALUE;
        int[] bestMove = null;

        // First get all the possible moves (left, right, up, down)
        for (int[] move : getPossibleMoves()) {
            int[] newLocation = {myLocation[0] + move[0], myLocation[1] + move[1]};
            
            if(foundTreasure == null)
            	if(fullMap[newLocation[1]][newLocation[0]].getType()==1 && isNearMountainWithUnexploredGrass(newLocation)) 
	            	return move;
            
            if(collectedTreasure && !fullMap[myLocation[1]][myLocation[0]].getMyMapArea()) {
            	if(foundEnemyFort == null)
		            if(fullMap[newLocation[1]][newLocation[0]].getType()==1 && isNearMountainWithUnexploredGrass(newLocation)) {
		            	return move;
		            }
            }
            
            // Use heuristic method to find the shortest distance of the move to reach the next destination
            int distance = PathFinder.heuristic(newLocation, fullMap, collectedTreasure, foundTreasure, myLocation, foundEnemyFort);

            // Find the move with the shortest distance to reach the next destination
            if (distance < minDistance) {
                minDistance = distance;
                bestMove = move;
            }
        }
        
        logger.info("generateMove final bestMove: [" + bestMove[0] + "," + bestMove[1] + "]");
        
        return bestMove;
    }
    
    // Check if the node is adjacent to a mountain and there are at least three unexplored grass around it
    private boolean isNearMountainWithUnexploredGrass(int[] newLocation) {
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int count = 0;
        for (int[] dir : directions) {
            int newX = newLocation[0] + dir[0];
            int newY = newLocation[1] + dir[1];
            if (newX >= 0 && newX < fullMap[0].length && newY >= 0 && newY < fullMap.length) {
                if (fullMap[newY][newX].getType() == 2 && !fullMap[newY][newX].isVisited()) {
                	if(!collectedTreasure) {
                		if(fullMap[newY][newX].getMyMapArea())
                			count++;
                	} else {
                    count++;
                	}
                }
            }
        }
        //logger.info("Count: " + count);
        return (count >= 3);
    }

    // Get all the possible moves (left, right, up, down)
    protected List<int[]> getPossibleMoves() {
        List<int[]> possibleMoves = new ArrayList<>();
        int row = fullMap.length;
        int col = fullMap[0].length;
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        
        for (int i = 0; i < 4; i++) {
            int newLocationX = myLocation[0] + dx[i];
            int newLocationY = myLocation[1] + dy[i];

            if (isValidMove(newLocationX, newLocationY, row, col, fullMap)) {
            	int newMoveX = dx[i];
            	int newMoveY = dy[i];
            	//logger.info("Generated a possible Position: [" + newMoveX + "," + newMoveY + "]");
            	possibleMoves.add(new int[]{newMoveX, newMoveY});
            }
        }
        return possibleMoves;
    }
    
    // Check if the move (the newLocation) is valid
    protected boolean isValidMove(int x, int y, int row, int col, MapNode[][] map) {
    	if(x >= 0 && x < col && y >= 0 && y < row && map[y][x].getType() != 3) {
			if(collectedTreasure==false) { // haven't collected treasure
				if(map[y][x].getMyMapArea()==true) { // newLocation in myMap area
					return true;
				} else { // newLocation in enemyMap area
					return false;
				}
			} else { // have collected treasure
				if(fullMap[myLocation[1]][myLocation[0]].getMyMapArea()==false) { // myLocation in enemyMap area
					if(map[y][x].getMyMapArea()==false) { // newLocation in enemyMap area
						return true;
					} else { // newLocation in myMap area
						return false;
					}
				} else { // myLocation in myMap area
					return true;
				}
			}
    	}
    	return false;
    }
    
    // Determine the round number the move need to send
    public int determineNumMovesToSend(int[] savedMove) {
    	int nx = myLocation[0] + savedMove[0];
        int ny = myLocation[1] + savedMove[1];

        // From grass to mountain
        if (fullMap[myLocation[1]][myLocation[0]].getType() == 2 && fullMap[ny][nx].getType() == 1) {
            return 3;
        // From mountain to grass   
        } else if (fullMap[myLocation[1]][myLocation[0]].getType() == 1 && fullMap[ny][nx].getType() == 2) {
        	return 3;
        // From grass to grass  	
        } else if (fullMap[myLocation[1]][myLocation[0]].getType() == 2 && fullMap[ny][nx].getType() == 2) {
            return 2;
        // from mountain to mountain    
        } else if(fullMap[myLocation[1]][myLocation[0]].getType() == 1 && fullMap[ny][nx].getType() == 1){
            return 4;
        }
        return 1;
    }
}

