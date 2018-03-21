package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Represents a canvas
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public interface Canvas {
    /**
     * 
     * @param polygon : the polygon to be drawn 
     * @param color : the line's color
     */
    public abstract void drawPolygon(Polygon polygon, Color color);
    
    /**
     * 
     * @param polyline : the polyline to be drawn
     * @param linestyle the line'style
     */
    public abstract void drawPolyLine(PolyLine polyline, LineStyle linestyle);
}
