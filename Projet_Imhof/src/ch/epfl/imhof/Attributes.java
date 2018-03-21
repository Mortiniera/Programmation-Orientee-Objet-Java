package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A map of attributes which links their reference to their values
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Attributes {

    private final Map<String, String> attributes;

    /**
     * builds a new map of attributes
     * 
     * @param attributes
     *            a given map of attributes
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * 
     * @return true if attributes is empty
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * 
     * @param key
     *            a key
     * @return true if attributes contains key
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * 
     * @param key
     *            a key
     * @return the value associated to key
     */
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * 
     * @param key
     *            a key
     * @param defaultValue
     *            a default value
     * @return the associated value if there's one and if attributes has key, a String default value elsecase
     */
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * 
     * @param key
     *            a key
     * @param defaultValue
     *            a default value
     * @return the associated value if there's one and if attributes has key, a int default value otherwise
     */
    public int get(String key, int defaultValue) {
        try {
            return Integer.parseInt(attributes.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 
     * @param keysToKeep
     *            the to be kept keySet
     * @return a filtered attributes map which contains only the to be kept pairs key/value
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Map<String, String> returnMap = new HashMap<>();
        for (String key : keysToKeep) {
            if (attributes.containsKey(key)) {
                returnMap.put(key, attributes.get(key));
            }
        }
        return new Attributes(returnMap);
    }

    public final static class Builder {
        /**
         * Attribute Class'Builder
         */
        private Map<String, String> attributes = new HashMap<>();

        /**
         * adds a new attributes
         * 
         * @param key
         *            a new key
         * @param value
         *            a new value
         * 
         */
        public void put(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * 
         * @return A new built Attributes
         */
        public Attributes build() {
            return new Attributes(attributes);
        }
    }
}
