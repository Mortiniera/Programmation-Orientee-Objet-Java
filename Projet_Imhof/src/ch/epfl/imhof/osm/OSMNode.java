package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * Represents an OSMNode
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OSMNode extends OSMEntity {
    private final PointGeo position;

    /**
     * @param id
     *            node's id
     * @param position
     *            node's PointGeo position
     * @param attributes
     *            node's attributes
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * @return node's position
     */
    public PointGeo position() {
        return position;
    }

    /**
     * a to be built node
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static final class Builder extends OSMEntity.Builder {
        private final PointGeo position;

        /**
         * @param id
         *            node's id
         * @param position
         *            node's position
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * @return a new OSMNode built with last current to be built node
         * @throws IllegalArgumentException
         *             if node has been set incomplete
         */
        public OSMNode build() {
            if (isIncomplete()) 
                throw new IllegalStateException(" node is incomplete");

            return new OSMNode(id, position, attributesBuilder.build());

        }
    }
}
