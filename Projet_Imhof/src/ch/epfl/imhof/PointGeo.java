package ch.epfl.imhof;
/** a WGS84 point
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class PointGeo {
    private final double longitude, latitude;
    /**
     * @param longitude
     *      longitude of the WGS84 point
     * @param latitude
     *      latitude of the WGS84 point
     *      Constructor initialize values
     *      
     * @throws IllegalArgumentException if longitude >PI or <-PI
     * 
     * @throws IllegalArgumentException if latitude< -PI/2 or > PI/2
     */
    public PointGeo(double longitude, double latitude) {
        if (Math.abs(longitude) > Math.PI) {
            throw new IllegalArgumentException("Invalid Longitude");
        }
        if (Math.abs(latitude) > (Math.PI / 2.0)) {
            throw new IllegalArgumentException("Invalid Latitude");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
/**
 * @return
 *      the point's longitude
 */
    public double longitude() {
        return longitude;
    }
/**
 * @return
 *      the point's latitude
 */
    public double latitude() {
        return latitude;
    }
}
