package map;

import java.util.EnumSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapNode {
    private final static Logger logger = LoggerFactory.getLogger(MapNode.class);
    
    private EMapNodeTerrain terrain;
    private Set<EMapNodeAttribute> attributes;
    
    public MapNode(EMapNodeTerrain terrain) {
        this.terrain = terrain;
        this.attributes = EnumSet.noneOf(EMapNodeAttribute.class);
    }
    
    // Sets the terrain type of the map node.
    public void setTerrain(EMapNodeTerrain terrain) {
        this.terrain = terrain;
    }
    
    // Retrieves the terrain type of the map node.
    public EMapNodeTerrain getTerrain() {
        return terrain;
    }

    // Adds an attribute to the map node.
    public void addAttribute(EMapNodeAttribute attribute) {
        attributes.add(attribute);
    }

    // Removes an attribute from the map node.
    public void removeAttribute(EMapNodeAttribute attribute) {
        attributes.remove(attribute);
    }

    // Checks if the map node has a specific attribute.
    public boolean hasAttribute(EMapNodeAttribute attribute) {
        return attributes.contains(attribute);
    }
}
