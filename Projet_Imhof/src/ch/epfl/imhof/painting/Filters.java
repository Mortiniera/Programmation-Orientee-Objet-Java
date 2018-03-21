package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.function.Predicate;

import ch.epfl.imhof.*;
/**
 * Represents a Filter using a predicate
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Filters {

    private Filters() {}
/**
 * 
 * @param number an int number
 * @return a predicate which is true if the attribute object it's applied to has the attribute Layer with value "int"
 * equal to the parameter or 0
 */
    public static Predicate<Attributed<?>> onLayer(int number) {
        return entity -> entity.attributeValue("layer", 0)==number;
    }
/**
 * 
 * @param attribute: an attribute
 * @return a predicate which is true if the Attributed object it's applied to has the attribute
 */
    public static Predicate<Attributed<?>> tagged(String attribute) {
        return entity -> entity.hasAttribute(attribute);
    }
/**
 * 
 * @param attribute an attribute
 * @param values an unknown number of possible values
 * @return a predicate which is true if the attributed object it's applied to has the attribute with one of the given values
 */
    public static Predicate<Attributed<?>> tagged(String attribute,
            String... values) {
        
        if (values.length < 1)
            throw new IllegalArgumentException("pas de valeurs d attribut");
        return entity -> (entity.hasAttribute(attribute) && Arrays.asList(
                values).contains(entity.attributeValue(attribute)));
    }
}
