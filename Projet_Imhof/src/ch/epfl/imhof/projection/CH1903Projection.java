package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;
/** a CH1903 point
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class CH1903Projection implements Projection {
/**@param point
 *      a WGS84 point
 * @return
 *      the corresponding CH1903 point
 */
    public Point project(PointGeo point) {
        double onePerTenT = 1 / 10000d;
        double lambda1 = onePerTenT * ((Math.toDegrees(point.longitude()) * 3600) - 26782.5);
        double lambdaSquare = lambda1*lambda1;
        
        double phi1 = onePerTenT * ((Math.toDegrees(point.latitude()) * 3600) - 169028.66);
        double phiSquare = phi1*phi1;
        
        double x = 600072.37 + (211455.93 * lambda1)
                - (10938.51 * lambda1 * phi1) - (0.36 * lambda1 * phiSquare)
                - (44.54 * lambdaSquare * lambda1);
        
        double y = 200147.07 + (308807.95 * phi1)
                + (3745.25 * lambdaSquare) + (76.63 * phiSquare)
                - (194.56 * lambdaSquare * phi1)
                + (119.79 * phiSquare* phi1);
        
        return new Point(x, y);
    }
/**
 * @param point
 *      a CH1903 point
 * @return
 *      the corresponding WGS84 point
 */
    public PointGeo inverse(Point point) {
        double x1 = (point.x() - 600000) / 1000000d;
        double y1 = (point.y() - 200000) / 1000000d;
        double lambda0 = 2.6779094 + (4.728982 * x1) + (0.791484 * x1 * y1)
                + (0.1306 * x1 * y1 * y1) - (0.0436 * x1 * x1 * x1);
        double phi0 = 16.9023892 + (3.238272 * y1) - (0.270978 * x1 * x1)
                - (0.002528 * y1 * y1) - (0.0447 * x1 * x1 * y1)
                - (0.0140 * y1 * y1 * y1);
        double lambda = lambda0 * 100 / 36d;
        double phi = phi0 * 100 / 36d;
        return new PointGeo(Math.toRadians(lambda),Math.toRadians(phi));
    }
}
