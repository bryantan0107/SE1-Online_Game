package map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.InvalidMapNodeTypeException;

/**
 * The single node of the map, contains all the necessary attributes
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class MapNode {
	private final static Logger logger = LoggerFactory.getLogger(MapNode.class);
	
	private int type=0;
    private boolean visited; 
    private boolean myFort;
    private boolean enemyFort;
    private boolean myMapArea;
    private boolean myPosition;
    private boolean enemyPosition;
    private boolean myTreasure;

    public MapNode(int type) {
    	if (type < 0 || type > 4) {
            logger.error("The type is invalid: " + type);
            throw new InvalidMapNodeTypeException("The type is invalid: " + type);
        }
    	
        this.type = type;
        this.visited = false;
        this.myFort = false;
        this.enemyFort = false;
        this.myMapArea=false;
        this.myPosition=false;
        this.enemyPosition=false;
        this.myTreasure=false;
    }

    public void setType(int type) {
    	if (type < 0 || type > 4) {
            logger.error("The type is invalid: " + type);
            throw new InvalidMapNodeTypeException("The type is invalid: " + type);
        }
    	this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    public boolean isVisited() {
        return visited;
    }
    
    public void setMyFort(boolean myFort) {
        this.myFort = myFort;
    }
    
    public boolean getMyFort() {
        return myFort;
    }
    
    public void setEnemyFort(boolean enemyFort) {
        this.enemyFort = enemyFort;
    }
    
    public boolean getEnemyFort() {
        return enemyFort;
    }
    
    public void setMyMapArea(boolean myMapArea) {
        this.myMapArea = myMapArea;
    }
    
    public boolean getMyMapArea() {
        return myMapArea;
    }
    
    public void setMyPosition(boolean myPosition) {
        this.myPosition = myPosition;
    }
    
    public boolean getMyPosition() {
        return myPosition;
    }
    
    public void setEnemyPosition(boolean enemyPosition) {
        this.enemyPosition = enemyPosition;
    }
    
    public boolean getEnemyPosition() {
        return enemyPosition;
    }
    
    public void setMyTreasure(boolean myTreasure) {
        this.myTreasure = myTreasure;
    }
    
    public boolean getMyTreasure() {
        return myTreasure;
    }
}
