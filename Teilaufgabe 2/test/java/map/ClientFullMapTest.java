package map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

class ClientFullMapTest {
    private ClientFullMap clientFullMap;
    private MapNode[][] initialMap;

    @BeforeEach
    void setUp() {
        initialMap = new MapNode[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                initialMap[i][j] = new MapNode(2); // Grass by default
            }
        }

        // Set up some specific positions
        initialMap[0][0].setMyFort(true);
        initialMap[9][9].setEnemyFort(true);
        initialMap[1][1].setMyPosition(true);
        initialMap[8][8].setEnemyPosition(true);
        initialMap[5][5].setMyTreasure(true);

        clientFullMap = new ClientFullMap(initialMap);
    }

    @Test
    void testInitialPositions() {
        assertArrayEquals(new int[]{0, 0}, clientFullMap.getMyFortPosition(), "My fort position should be [0, 0]");
        assertArrayEquals(new int[]{9, 9}, clientFullMap.getEnemyFortPosition(), "Enemy fort position should be [9, 9]");
        assertArrayEquals(new int[]{1, 1}, clientFullMap.getMyPosition(), "My position should be [1, 1]");
        assertArrayEquals(new int[]{8, 8}, clientFullMap.getEnemyPosition(), "Enemy position should be [8, 8]");
        assertArrayEquals(new int[]{5, 5}, clientFullMap.getMyTreasurePosition(), "My treasure position should be [5, 5]");
    }

    @Test
    void testUpdatePositions() {
        MapNode[][] updatedMap = new MapNode[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                updatedMap[i][j] = new MapNode(2);
            }
        }

        updatedMap[2][2].setMyPosition(true);
        clientFullMap.updateClientFullMap(updatedMap);

        assertArrayEquals(new int[]{2, 2}, clientFullMap.getMyPosition(), "Updated my position should be [2, 2]");
    }

    @Test
    void testSetCollectedTreasure() {
        clientFullMap.setCollectedTreasure(true);
        assertTrue(clientFullMap.getCollectedTreasure(), "Collected treasure should be true");
    }

    @Test
    void testCheckMyMapDirection() {
        clientFullMap.checkMyMapDirection(new int[]{1, 1});
        assertEquals("UP", clientFullMap.getMyMapDirection(), "My map direction should be UP");
    }

    @Test
    void testPropertyChangeListeners() {
        TestPropertyChangeListener listener = new TestPropertyChangeListener();
        clientFullMap.addPropertyChangeListener(listener);

        MapNode[][] updatedMap = new MapNode[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                updatedMap[i][j] = new MapNode(2);
            }
        }

        updatedMap[2][2].setMyPosition(true);
        clientFullMap.updateClientFullMap(updatedMap);

        assertEquals(1, listener.getEvents().size(), "One property change event should have been fired");
        assertEquals("myPosition", listener.getEvents().get(0).getPropertyName(), "Property change event should be for 'myPosition'");
    }
    
    @Test
    void testCollectedTreasurePropertyChangeListeners() {
        TestPropertyChangeListener listener = new TestPropertyChangeListener();
        clientFullMap.addPropertyChangeListener(listener);

        clientFullMap.setCollectedTreasure(true);

        assertEquals(1, listener.getEvents().size(), "One property change event should have been fired");
        assertEquals("collectedTreasure", listener.getEvents().get(0).getPropertyName(), "Property change event should be for 'collectedTreasure'");
    }
    
    @Test
    void testGetMyMapDirection() {
        clientFullMap.checkMyMapDirection(new int[]{1, 1});
        assertEquals("UP", clientFullMap.getMyMapDirection(), "My map direction should be UP");

        clientFullMap.checkMyMapDirection(new int[]{6, 6});
        assertEquals("DOWN", clientFullMap.getMyMapDirection(), "My map direction should be DOWN");
    }

    private static class TestPropertyChangeListener implements PropertyChangeListener {
        private final List<PropertyChangeEvent> events = new ArrayList<>();

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            events.add(evt);
        }

        public List<PropertyChangeEvent> getEvents() {
            return events;
        }
    }
}

