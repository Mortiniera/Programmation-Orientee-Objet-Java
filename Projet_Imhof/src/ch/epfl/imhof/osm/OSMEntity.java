package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
/**
 * an OSM entity
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public abstract class OSMEntity {
    private final long id;
    private final Attributes attributes;
    /**
     * @param id
     *          entity's identifier
     * @param attributes
     *          entity's attributes
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }
    /**
     * @return
     *          entity's identifier
     */
    public long id() {
        return id;
    }
    /**
     * @return entity's attributes (Map)
     */
    public Attributes attributes() {
        return attributes;
    }
    /**
     * @param key
     *          a key possibly contained in attributes
     * @return  true if attributes effectively contains "key"
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }
    /**
     * @param key
     *      a key
     * @return
     *      the value associated to the key "key"
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }
    /**
     * a to be Built OSM entity
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public abstract static class Builder {
        protected final long id;
        private boolean incomplete;
        protected final Attributes.Builder attributesBuilder;
        
        /**
         * @param id
         *      entity's identifier
         */
        public Builder(long id) {
            this.id = id;
            incomplete = false;
            attributesBuilder = new Attributes.Builder();
        }
        /**
         * @param key
         *          a key
         * @param value
         *          add's/replaces the associated value at key "key"
         */
        public void setAttribute(String key, String value) {
            attributesBuilder.put(key, value);
        }
        /**
         * current entity becomes incomplete
         */
        public void setIncomplete() {
            incomplete = true;
        }
     /**
      * @return
      *         true if current entity is incomplete
      */
        public boolean isIncomplete() {
            return incomplete;
        }
    }
}
