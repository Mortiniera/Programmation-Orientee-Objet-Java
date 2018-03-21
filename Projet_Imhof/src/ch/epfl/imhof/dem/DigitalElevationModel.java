package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
/**
 * a digital elevation model
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public interface DigitalElevationModel extends AutoCloseable {
    /**
     * 
     * @param pointGeo
     *      a point in WGS84
     * @return the normal vector at this point
     */
    public Vector3 normalAt(PointGeo pointGeo);
}
