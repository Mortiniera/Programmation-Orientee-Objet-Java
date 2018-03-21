package ch.epfl.imhof;

/**
 * A T typed attributed Entity
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;

    /**
     * builds a new Attributed Object<T>
     * 
     * @param value
     *            The object type
     * @param attributes
     *            object's attributes
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * 
     * @return the value
     */
    public T value() {
        return value;
    }

    /**
     * 
     * @return object's attributes
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * 
     * @param attributeName
     *            the attribute's name
     * @return true if the object has an attribute called "attributeName"
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * 
     * @param attributeName
     *           the attribute's name
     * @return the attribute's value
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * 
     * @param attributeName
     *            the attribute's name
     * @param defaultValue
     *            any default value
     * @return the attribute's value if object has attribute, String default value in the other case
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * 
     * @param attributeName
     *            the attribute's name
     * @param defaultValue
     *            any default value
     * @return the attribute's value if object has attribute, int default value in the other case
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    };
}
