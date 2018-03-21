package ch.epfl.imhof.projection;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;
/** A point in no precise coordinate (cannot be instantiate then)
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public interface Projection {
    Point project(PointGeo point);
    PointGeo inverse(Point point);
}
