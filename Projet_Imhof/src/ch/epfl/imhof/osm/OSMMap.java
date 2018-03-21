package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * represents a map with OSM ways and OSM relations
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;
    /**
     * 
     * @param ways
     *          a way list
     * @param relations
     *          a relation list
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections.unmodifiableList(new ArrayList<>(relations));
    }
/**
 * 
 * @return
 *          a way list
 */
    public List<OSMWay> ways() {
        return ways;
    }
/**
 * 
 * @return
 *      a relation list
 */
    public List<OSMRelation> relations() {
        return relations;
    }
    /**
     * represents a to be built map
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static final class Builder {
        private final Map<Long, OSMNode> nodes = new HashMap<>();
        private final Map<Long, OSMWay>  ways = new HashMap<>();
        private final Map<Long,OSMRelation> relations = new HashMap<>();
/**
 * 
 * @param newNode
 *          a to be added node
 */
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }
/**
 * 
 * @param id
 *          node's id
 * @return
 *          the corresponding node
 */
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }
/**
 * 
 * @param newWay
 *          a to be added way
 */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }
/**
 * 
 * @param id
 *      the seeked way's id
 * @return
 *      the corresponding way
 */
        public OSMWay wayForId(long id) {
            return ways.get(id);
        }
/**
 * 
 * @param newRelation
 *          a to be added relation
 */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }
/**
 * 
 * @param id
 *          the seeked relation's id
 * @return
 *          the corresponding relation
 */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }
/**
 * @return
 *          a new OSM map freshly built with last current OSMMap.Builder object's
 */
        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }    
    }
}
