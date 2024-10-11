package ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import exceptions.InvalidMapNodeTypeException;
import map.ClientFullMap;
import map.MapNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLI implements PropertyChangeListener {
	private final static Logger logger = LoggerFactory.getLogger(CLI.class);
	
	private MapNode[][] fullMap;
	private int[] myFortPosition;
	private int[] enemyFortPosition;
	private int[] myPosition;
	private int[] enemyPosition;
	private int[] myTreasurePosition;
	private boolean collectedTreasure;
	private static int roundNum=0;

    public static int getRoundNum() {
		return roundNum;
	}

	public static void setRoundNum(int roundNum) {
		CLI.roundNum = roundNum;
	}

	public CLI(ClientFullMap clientFullMap) {
    	this.fullMap = ClientFullMap.getFullMap();
        clientFullMap.addPropertyChangeListener(this);
    }
	
	public void displayFullMap() {
        String grass = "🌲"; 
        String water = "🌊"; 
        String mountain = "⛰️";  
        String me = "😃"; 
        String meWithTreasure = "🤑"; 
        String empty = "⛔️";
        String myFort = "🏰";
        String enemyFort = "🏯";
        String enemy = "😡";
        String treasure = "💰";
        String fight = "💥";
        String visited = "🏃";
        String climbed = "🧗";
        String visitedwater = "🏊‍♂️";
        String collected = "💸";
        
        System.out.println("Round number: " + roundNum);
        
        // Print the map to the console using the defined characters
        for (int i = 0; i < fullMap.length; i++){
            for (int j = 0; j < fullMap[i].length; j++){
            	int terrainType = fullMap[i][j].getType();
                String terrainColor;
                switch (terrainType) {
	                case 0:
	                    terrainColor = empty;
	                    break;
	                case 1:
	                    terrainColor = mountain;
	                    break;
	                case 2:
	                    terrainColor = grass;
	                    break;
	                case 3:
	                    terrainColor = water;
	                    break;
	                case 4:
	                    terrainColor = myFort;
	                    break;    
	                default:
	                	logger.error("The type is invalid: " + terrainType);
	                    throw new InvalidMapNodeTypeException("The type is invalid: " + terrainType);
	            }
                
                // treasure position
                if(myTreasurePosition != null && j == myTreasurePosition[0] && i == myTreasurePosition[1]) {
                		terrainColor = treasure;
                }
                
                // visited 
                if(fullMap[i][j].isVisited()==true) {
                	if(terrainType==1) {
                		terrainColor = climbed;
                	} else if(terrainType == 3) {
                		terrainColor = visitedwater;
                	} else terrainColor = visited;
                	
                	if(myTreasurePosition != null && j == myTreasurePosition[0] && i == myTreasurePosition[1]) {
                		terrainColor = treasure;
                }
                	
                	if(collectedTreasure) {
                		if(myTreasurePosition!=null && j == myTreasurePosition[0] && i == myTreasurePosition[1])
                			terrainColor = collected;
                	}
                }
                
                // my castle position
                if(myFortPosition != null && myFortPosition[0] == j && myFortPosition[1] == i)
                	terrainColor = myFort;
                
                // enemy castle position
                if(enemyFortPosition != null && enemyFortPosition[0] == j && enemyFortPosition[1] == i)
                	terrainColor = enemyFort;
                
                // my position (with and without treasure)
                if (j == myPosition[0] && i == myPosition[1]) {
                	if(collectedTreasure==true)
                		terrainColor = meWithTreasure;
                	else {
                		terrainColor = me;
                	}
                }
                
             // enemy and fight position 
                if (j == enemyPosition[0] && i == enemyPosition[1]) 
                	if(myPosition[0] == enemyPosition[0] && myPosition[1] == enemyPosition[1]) {
                		terrainColor = fight;
                	} else {
                		terrainColor = enemy;
                	}

                System.out.print(terrainColor);
            }
            //起着空行的作用
            System.out.println();
        }
    }
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object newValue = evt.getNewValue();

		switch (evt.getPropertyName()) {
		case "myFortPosition":
			myFortPosition = (int[]) newValue;
			break;
		case "enemyFortPosition":
			enemyFortPosition = (int[]) newValue;
			break;
		case "myPosition":
			myPosition = (int[]) newValue;
			break;
		case "enemyPosition":
			enemyPosition = (int[]) newValue;
			break;
		case "myTreasurePosition":
			myTreasurePosition = (int[]) newValue;
			break;
		case "collectedTreasure":
			collectedTreasure = (boolean) newValue;
		}
	}
	
	public static void displayHalfMap(MapNode[][] map) {
        String grass = "🌲"; 
        String water = "🌊";  
        String mountain = "⛰️";  
        String myFort = "🏰"; 
        String empty = "⛔";

        // Print the map to the console using the defined characters
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 10; i++) {
                int terrainType = map[i][j].getType();
                String terrainColor;
                switch (terrainType) {
	                case 0:
	                    terrainColor = empty;
	                    break;
	                case 1:
	                    terrainColor = mountain;
	                    break;
	                case 2:
	                    terrainColor = grass;
	                    break;
	                case 3:
	                    terrainColor = water;
	                    break;
	                case 4:
	                    terrainColor = myFort;
	                    break;    
	                default:
	                	logger.error("The type is invalid: " + terrainType);
	                    throw new InvalidMapNodeTypeException("The type is invalid: " + terrainType);
	            }
                System.out.print(terrainColor);
            }
            System.out.println();
        }
    }
}
