package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;
/** an Equirectangular point
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class EquirectangularProjection implements Projection {

    /**
     * @param point
     *      a WGS84 point
     * @return
     *      the corresponding Equirectangular point
     */
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }
    /**
     * @param
     *      an Equirectangular point
     * @return
     *      the corresponding WGS84 point
     */
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
