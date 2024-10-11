package map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.InvalidMapNodeTypeException;

class MapNodeTest {
    private MapNode mapNode;

    @BeforeEach
    void setUp() {
        mapNode = new MapNode(2); // Grass by default
    }

    @Test
    void testInitialType() {
        assertEquals(2, mapNode.getType(), "Initial type should be 2 (Grass)");
    }

    @Test
    void testSetType() {
        mapNode.setType(1); // Change to mountain
        assertEquals(1, mapNode.getType(), "Type should be 1 (Mountain)");
    }

    @Test
    void testSetTypeInvalid() {
        Exception exception = assertThrows(InvalidMapNodeTypeException.class, () -> {
            mapNode.setType(5); // Invalid type
        });
        assertEquals("The type is invalid: 5", exception.getMessage());
    }

    @Test
    void testConstructorInvalidType() {
        Exception exception = assertThrows(InvalidMapNodeTypeException.class, () -> {
            new MapNode(5); // Invalid type
        });
        assertEquals("The type is invalid: 5", exception.getMessage());
    }

    @Test
    void testVisited() {
        assertFalse(mapNode.isVisited(), "Initially, the node should not be visited");
        mapNode.setVisited(true);
        assertTrue(mapNode.isVisited(), "After setting, the node should be visited");
    }

    @Test
    void testMyFort() {
        assertFalse(mapNode.getMyFort(), "Initially, the node should not be a myFort");
        mapNode.setMyFort(true);
        assertTrue(mapNode.getMyFort(), "After setting, the node should be a myFort");
    }

    @Test
    void testEnemyFort() {
        assertFalse(mapNode.getEnemyFort(), "Initially, the node should not be an enemyFort");
        mapNode.setEnemyFort(true);
        assertTrue(mapNode.getEnemyFort(), "After setting, the node should be an enemyFort");
    }

    @Test
    void testMyMapArea() {
        assertFalse(mapNode.getMyMapArea(), "Initially, the node should not be in myMapArea");
        mapNode.setMyMapArea(true);
        assertTrue(mapNode.getMyMapArea(), "After setting, the node should be in myMapArea");
    }

    @Test
    void testMyPosition() {
        assertFalse(mapNode.getMyPosition(), "Initially, the node should not be myPosition");
        mapNode.setMyPosition(true);
        assertTrue(mapNode.getMyPosition(), "After setting, the node should be myPosition");
    }

    @Test
    void testEnemyPosition() {
        assertFalse(mapNode.getEnemyPosition(), "Initially, the node should not be enemyPosition");
        mapNode.setEnemyPosition(true);
        assertTrue(mapNode.getEnemyPosition(), "After setting, the node should be enemyPosition");
    }

    @Test
    void testMyTreasure() {
        assertFalse(mapNode.getMyTreasure(), "Initially, the node should not be myTreasure");
        mapNode.setMyTreasure(true);
        assertTrue(mapNode.getMyTreasure(), "After setting, the node should be myTreasure");
    }
}
