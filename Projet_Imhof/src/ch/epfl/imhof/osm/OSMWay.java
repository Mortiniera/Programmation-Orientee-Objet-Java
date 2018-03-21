package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Represents an OSMWay which is a nodeList, Closed if the first is the same as the last
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OSMWay extends OSMEntity {
    /**
     * a to be built way
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static final class Builder extends OSMEntity.Builder {
        private final List<OSMNode> nodes;
        /**
         * @param id
         *            way's id
         */
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();

        }
        /**
         * @param newNode
         *            a to be added node
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }
        /**
         * @return A new OSMWay built with the last to be built object's
         */
        public OSMWay build() {
            if (isIncomplete()) 
                throw new IllegalStateException("way is incomplete");
            
            return new OSMWay(id, nodes, attributesBuilder.build());
        }

        /**
         * @return true if entity has been set incomplete or if node list'size <2
         */
        @Override
        public boolean isIncomplete() {
            return (super.isIncomplete()) || (nodes.size() < 2);
        }
    }

    private final List<OSMNode> nodes;

    /**
     * 
     * @param id
     *            way's id
     * @param nodes
     *            a node list
     * @param attributes
     *            way's attributes
     * @throws IllegalArgumentException
     *              if way contains less than 2 nodes
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) {
        super(id, attributes);
        if (nodes.size() < 2) 
            throw new IllegalArgumentException("List contains less than 2 elements");

        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * @return the first node
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * @return true if first node equals last node
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    /**
     * @return the last node
     */
    public OSMNode lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    /**
     * @return node list
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * @return list'size
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * @return a list of the way's nodes without repetition
     */
    public List<OSMNode> nonRepeatingNodes() {
        /**
         * TODO copie inutile de la liste, utiliser la methode sublist,
         * eviter de faire un appel à chaque iteration de la methode contains
         */
        return nodes.get(0).equals(nodes.get(nodes.size()-1))? nodes.subList(1, nodes.size()):nodes;
    }

}
