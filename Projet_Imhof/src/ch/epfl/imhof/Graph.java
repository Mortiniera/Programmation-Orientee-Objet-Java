package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * A graph of nodes
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Graph<N> {

    private final Map<N, Set<N>> neighbors;
    /**
     * 
     * @param neighbors
     *          a map linking a node to his neighbors
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N,Set<N>> map = new HashMap<>();
        for(Map.Entry<N, Set<N>> entry:neighbors.entrySet()){
            Set<N> nodeSet=Collections.unmodifiableSet(new HashSet<N>(entry.getValue()));
            map.put(entry.getKey(), nodeSet);
        }
        this.neighbors = Collections.unmodifiableMap(map);
    }
    /**
     * 
     * @return
     *          The Set of nodes present in the graph
     */
    public Set<N> nodes() {
        return neighbors.keySet();
    }
    /**
     * 
     * @param node
     *          a node from the graph
     * @return  node's neighbors
     */
    public Set<N> neighborsOf(N node) {
        if (!neighbors.containsKey(node)) 
            throw new IllegalArgumentException("Invalid Node");

        return neighbors.get(node);        
    }
    /**
     * a to be built graph
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static final class Builder<N> {
        private Map<N, Set<N>> neighbors = new HashMap<>();
        /**
         * 
         * @param n
         *          a node
         *          adds the node n to the graph
         */
        public void addNode(N n) {
            if (!neighbors.containsKey(n)) {
                Set<N> newSet = new HashSet<>();
                neighbors.put(n, newSet);
            }
        }
        /**
         * 
         * @param n1
         *  a first node
         * @param n2
         *      a second node
         *      adds an edge between n1 and n2
         */
        public void addEdge(N n1, N n2){
            if(!neighbors.containsKey(n1)){
                throw new IllegalArgumentException("does not contain n1");
            }
            if(! neighbors.containsKey(n2)){
                throw new IllegalArgumentException("does not contain n2");
            }
            neighbors.get(n1).add(n2);
            neighbors.get(n2).add(n1);
        }
        /**
         * 
         * @return
         * a new built graph
         */
        public Graph<N> build(){
            return new Graph<N>(neighbors);
        }
    }
}
