package client.main;

import clientNetwork.ClientNetwork;
import map.ClientFullMap;
import map.HalfMapGenerator;
import ui.CLI;
import action.MoveGenerator;

/**
 * The Controller of the Client, run the whole process of the entire game
 */
public class MainClient {
	/**
	 * Main method, shows the whole process of the entire game
	 */
    public static void main(String[] args) throws InterruptedException {
        // Get command line parameters
        String serverBaseUrl = args[1];
        String gameId = args[2];

        // Create ClientNetwork object and register Client
        ClientNetwork myNetwork = new ClientNetwork(serverBaseUrl, gameId);
        myNetwork.registerClient("Bryan Yi Jue", "Tan", "bryanyijut00");

        // Wait until turn
        waitForTurn(myNetwork);

        // Geenrate HalfMap and send it to the Server
        myNetwork.sendHalfMap(HalfMapGenerator.generateHalfMap());

        // Wait until turn
        waitForTurn(myNetwork);

        // Get FullMap from the Server and create ClientFullMap object
        ClientFullMap fullMap = new ClientFullMap(myNetwork.GetFullMap());
        
        // Check myMap direction
        fullMap.checkMyMapDirection(fullMap.getMyPosition());
        
        // Create CLI object
        CLI cli = new CLI(fullMap);

        // Create MoveGenerator object
        MoveGenerator move = new MoveGenerator();

        // Start a game loop until find the treasure
        while (!myNetwork.collectedTreasure()) {
            // Update FullMap and display it
            updateFullMap(myNetwork, fullMap, move);
            cli.displayFullMap();

            // Generate and send move
            sendMoves(myNetwork, fullMap, move, cli);
        }

        // Update the collectedTreasure state after finding the treasure
        fullMap.setCollectedTreasure(true);
        move.setCollectedTreasure(true);
        
        // Set my treasure position in the FullMap
        fullMap.updateClientFullMap(myNetwork.GetFullMap());
    	fullMap.setMyTreasurePosition(fullMap.getMyPosition());

    	// Continue the game loop until win or lose
        while (!myNetwork.hasWon() || !myNetwork.hasLost()) {
        	// Update FullMap and display it
            updateFullMap(myNetwork, fullMap, move);
            cli.displayFullMap();

            // Generate and send move
            sendMoves(myNetwork, fullMap, move, cli);
        }
    }
    
    /**
	 * From here are the private mathods of the MainClient class, used in the main method
	 */

    // Wait until turn: Check if it's client turn, then check if client have won or lost the game
    private static void waitForTurn(ClientNetwork myNetwork) throws InterruptedException {
        while (!myNetwork.isTurn()) {
            if (myNetwork.hasLost()) {
            	System.exit(0);
            }
            if (myNetwork.hasWon()) {
            	System.exit(-1);
            }
        }
    }

    // Update FullMap: Update ClientFullMap and MoveGenerator
    private static void updateFullMap(ClientNetwork myNetwork, ClientFullMap fullMap, MoveGenerator move) {
        fullMap.updateClientFullMap(myNetwork.GetFullMap());
        int[] myPosition = fullMap.getMyPosition();
        
        if (fullMap.getMyTreasurePosition() != null) {
            move.setFoundTreasure(fullMap.getMyTreasurePosition());
        }
        if (fullMap.getEnemyFortPosition() != null) {
            move.setFoundEnemyFort(fullMap.getEnemyFortPosition());
        }
        
        move.updateVisitedArea(myPosition, ClientFullMap.getFullMap());
    }

    // Generate and send move: First generate move, then check number of rounds that need to be sent based on the terrain type
    private static void sendMoves(ClientNetwork myNetwork, ClientFullMap fullMap, MoveGenerator move, CLI cli) throws InterruptedException {
    	int[] savedMove = move.generateMove();
        int numMovesToSend = move.determineNumMovesToSend(savedMove);

        for (int i = 0; i < numMovesToSend; i++) {
            waitForTurn(myNetwork);
            CLI.setRoundNum(CLI.getRoundNum() + 1);
            myNetwork.sendMove(savedMove);
        }
    }
}
