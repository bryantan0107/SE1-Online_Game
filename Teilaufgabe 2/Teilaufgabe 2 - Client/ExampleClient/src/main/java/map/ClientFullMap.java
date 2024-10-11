package map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Model of the Client, save all the important data
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class ClientFullMap {
    private final static Logger logger = LoggerFactory.getLogger(ClientFullMap.class);
    private PropertyChangeSupport support;

    private static MapNode[][] fullMap;
    private int[] myPosition;
    private int[] enemyPosition;
    private int[] myFortPosition;
    private int[] enemyFortPosition;
    private int[] myTreasurePosition;
    private boolean collectedTreasure=false;
    private String myMapDirection;

    // Initiate ClientFullMap 
    public ClientFullMap(MapNode[][] gameFullMap) {
    	if(gameFullMap == null) {
    		logger.error("The gameFullMap is null!");
    		throw new IllegalArgumentException("The gameFullMap is null!");
    	}
    	
    	
        ClientFullMap.fullMap = new MapNode[gameFullMap.length][gameFullMap[0].length];
        support = new PropertyChangeSupport(this);

        for (int y = 0; y < gameFullMap.length; y++) {
            for (int x = 0; x < gameFullMap[0].length; x++) {
            	fullMap[y][x] = new MapNode(gameFullMap[y][x].getType());

            	if (gameFullMap[y][x].getMyFort()) {
                    myFortPosition = new int[]{x, y};
                    support.firePropertyChange("myFortPosition", null, myFortPosition);
                }
                if (gameFullMap[y][x].getEnemyFort()) {
                    enemyFortPosition = new int[]{x, y};
                    support.firePropertyChange("enemyFortPosition", null, enemyFortPosition);
                }
                if (gameFullMap[y][x].getMyPosition()) {
                    myPosition = new int[]{x, y};
                    support.firePropertyChange("myPosition", null, myPosition);
                }
                if (gameFullMap[y][x].getEnemyPosition()) {
                    enemyPosition = new int[]{x, y};
                    support.firePropertyChange("enemyPosition", null, enemyPosition);
                }
                if (gameFullMap[y][x].getMyTreasure()) {
                    myTreasurePosition = new int[]{x, y};
                    support.firePropertyChange("myTreasurePosition", null, myTreasurePosition);
                }
            }
        }
        this.myMapDirection="";
    }
    
    // Update the attributes (data) of the ClientFullMap
    public void updateClientFullMap(MapNode[][] gameFullMap) {

        for (int y = 0; y < gameFullMap.length; y++) {
            for (int x = 0; x < gameFullMap[0].length; x++) {

                if (gameFullMap[y][x].getMyFort()) {
                	int[] oldmyFortPosition = myFortPosition;
                    myFortPosition = new int[]{x, y};
                    support.firePropertyChange("myFortPosition", oldmyFortPosition, myFortPosition);
                }
                if (gameFullMap[y][x].getEnemyFort()) {
                	int[] oldenemyFortPosition = enemyFortPosition;
                    enemyFortPosition = new int[]{x, y};
                    support.firePropertyChange("enemyFortPosition", oldenemyFortPosition, enemyFortPosition);
                }
                if (gameFullMap[y][x].getMyPosition()) {
                	int[] oldmyPosition = myPosition;
                    myPosition = new int[]{x, y};
                    support.firePropertyChange("myPosition", oldmyPosition, myPosition);
                }
                if (gameFullMap[y][x].getEnemyPosition()) {
                	int[] oldenemyPosition = enemyPosition;
                    enemyPosition = new int[]{x, y};
                    support.firePropertyChange("enemyPosition", oldenemyPosition, enemyPosition);
                }
                if (gameFullMap[y][x].getMyTreasure()) {
                	int[] oldmyTreasurePosition = myTreasurePosition;
                	myTreasurePosition = new int[]{x, y};
                	support.firePropertyChange("myTreasurePosition", oldmyTreasurePosition, myTreasurePosition);
                }
            }
        }
        logger.info("My new position: [" + myPosition[0] + "," + myPosition[1] + "]");
    }

    public static MapNode[][] getFullMap() {
        return fullMap;
    }
    
    public void setMyPosition(int[] myPosition) {
    	this.myPosition=myPosition;
    }
    
    public int[] getMyPosition() {
    	return myPosition;
    }
    
    public void setEnemyPosition(int[] enemyPosition) {
    	this.enemyPosition=enemyPosition;
    }
    
    public int[] getEnemyPosition() {
    	return enemyPosition;
    }
    
    public void setMyTreasurePosition(int[] myTreasurePosition) {
    	this.myTreasurePosition=myTreasurePosition;
    }
    
    public int[] getMyTreasurePosition() {
    	return myTreasurePosition;
    }
    
    public int[] getMyFortPosition() {
    	return myFortPosition;
    }
    
    public int[] getEnemyFortPosition() {
    	return enemyFortPosition;
    }
    
    public void setCollectedTreasure(boolean collectedTreasure) {
    	this.collectedTreasure=collectedTreasure;
    	if(collectedTreasure)
    		support.firePropertyChange("collectedTreasure", null, collectedTreasure);
    }
    
    public boolean getCollectedTreasure() {
    	return collectedTreasure;
    }
    
    public String getMyMapDirection() {
    	return myMapDirection;
    }
    
    // Check myMap direction by seeing where myPosition is
    public void checkMyMapDirection(int[] myPosition) {
    	int mapWidth = fullMap[0].length;
        int mapHeight = fullMap.length;
        
        // If the FullMap's size is 20x5
    	if (mapWidth == 20) {
            if (myPosition[0] < mapWidth / 2) {
            	myMapDirection="LEFT";
            } else {
            	myMapDirection="RIGHT";
            }
         // If the FullMap's size is 10x10    
        } else if (mapHeight == 10) {
            if (myPosition[1] < mapHeight / 2) {
            	myMapDirection="UP";
            } else {
            	myMapDirection="DOWN";
            }
        }
    	setMyMapArea();
    }
    
    // Set myMap areas into true
    public void setMyMapArea() {
        int mapWidth = fullMap[0].length;
        int mapHeight = fullMap.length;

        // Determine the starting coordinates for myMap
        int startX=0, startY=0;

        // Determine the starting coordinates of myMap based on myMapDirection
        if (myMapDirection=="LEFT") {
        	startX=0;
        	startY=0;
        } else if (myMapDirection=="RIGHT") {
        	startX=0;
        	startY=mapWidth / 2;
        } else if (myMapDirection=="UP") {
        	startX=0;
        	startY=0;
        } else if (myMapDirection=="DOWN") {
        	startX=mapHeight / 2;
        	startY=0;
        }

        // Set myMap areas into true
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (i + startX < mapHeight && j + startY < mapWidth) {
                    fullMap[i + startX][j + startY].setMyMapArea(true);
                }
            }
        }
    }
    
    // add PropertyChangeListener
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // remove PropertyChangeListener
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
 
}
