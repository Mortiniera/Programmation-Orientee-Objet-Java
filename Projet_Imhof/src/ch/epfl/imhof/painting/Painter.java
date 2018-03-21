package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.PolyLine;

/**
 * Represents a Painter
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */

public interface Painter {
    /**
     * 
     * @param width
     *            : the line's width
     * @param colorLine
     *            : the line's color
     * @return a line's painter for the whole map given the line's width and the
     *         line's color
     */
    public static Painter line(double width, Color colorLine) {
        return (map, canvas) -> {
            map.polyLines().forEach(
                    polyline -> canvas.drawPolyLine(polyline.value(),
                            new LineStyle(width, colorLine)));
        };
    }

    /**
     * 
     * @param width
     *            : the line's width
     * @param colorLine
     *            : the line's color
     * @param lineCap
     *            : the line's cap
     * @param lineJoin
     *            : the line's join
     * @param dashingPattern
     *            : the line's dashing pattern
     * @return a line's painter for the whole map given the LineStyle
     */
    public static Painter line(double width, Color colorLine, LineCap lineCap,
            LineJoin lineJoin, float... dashingPattern) {
        return (map, canvas) -> {
            map.polyLines().forEach(
                    polyline -> canvas.drawPolyLine(polyline.value(),
                            new LineStyle(width, colorLine, lineCap, lineJoin,
                                    dashingPattern)));
        };
    }

    /**
     * 
     * @param lineStyle
     *            : the drawing lineStyle
     * @return a line's painter for the whole map
     */
    public static Painter line(LineStyle lineStyle) {
        return (map, canvas) -> {
            map.polyLines()
                    .forEach(
                            polyline -> canvas.drawPolyLine(polyline.value(),
                                    lineStyle));
        };
    }

    /**
     * 
     * @param width
     *            : the line's width
     * @param colorLine
     *            : the line's color
     * @return a polygon's periphery's painter given the line's width and line's
     *         color
     */
    public static Painter outline(double width, Color colorLine) {
        return (map, canvas) -> {
            map.polygons().forEach(polygon -> {
                // draws shell
                    canvas.drawPolyLine(polygon.value().shell(), new LineStyle(
                            width, colorLine));
                    // draws holes
                    polygon.value()
                            .holes()
                            .forEach(
                                    polyline -> canvas.drawPolyLine(polyline,
                                            new LineStyle(width, colorLine)));

                });
        };
    }

    /**
     * 
     * @param width
     *            : the line's width
     * @param colorLine
     *            : the line's color
     * @param lineCap
     *            : the line's cap
     * @param lineJoin
     *            : the line's join
     * @param dashingPattern
     *            : the line's dashing pattern
     * @return a polygon's periphery's painter given the lineStyle
     */
    public static Painter outline(double width, Color colorLine,
            float[] dashingPattern, LineCap lineCap, LineJoin lineJoin) {
        return (map, canvas) -> {
            map.polygons().forEach(polygon -> {
                // draws shell
                    canvas.drawPolyLine(polygon.value().shell(),
                            new LineStyle(width, colorLine, lineCap, lineJoin,
                                    dashingPattern));
                    // draws holes
                    polygon.value()
                            .holes()
                            .forEach(
                                    polyline -> canvas.drawPolyLine(polyline,
                                            new LineStyle(width, colorLine,
                                                    lineCap, lineJoin,
                                                    dashingPattern)));

                });
        };
    }

    /**
     * 
     * @param lineStyle
     *            : the line'style
     * @return a polygon's periphery painter
     */
    public static Painter outline(LineStyle lineStyle) {
        return (map, canvas) -> {
            map.polygons().forEach(polygon -> {
                // draws shell
                    canvas.drawPolyLine(polygon.value().shell(), lineStyle);
                    // draws holes
                    polygon.value()
                            .holes()
                            .forEach(
                                    polyline -> canvas.drawPolyLine(polyline,
                                            lineStyle));

                });
        };
    }

    /**
     * 
     * @param color
     *            : the drawing's color
     * @return a polygon's Painter
     */
    public static Painter polygon(Color color) {
        return (map, canvas) -> {
            map.polygons().forEach(
                    polygon -> canvas.drawPolygon(polygon.value(), color));
        };
    }

    /**
     * 
     * @param painter
     *            : another painter
     * @return a painter that stacks this painter on that painter
     */
    public default Painter above(Painter painter) {
        return (map, canvas) -> {
            painter.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    /**
     * 
     * @return a painter that stacks painters of entities depending on their
     *         "layer" attributeValue
     */
    public default Painter layered() {

        Painter painter = when(Filters.onLayer(-5));
        for (int i = -4; i < 6; i++) {
            painter = when(Filters.onLayer(i)).above(painter);
        }
        return painter;
    }

    /**
     * 
     * @param predicate
     *            : a predicate
     * @return a painter which behave like the one the method is apply to except
     *         it only draws Attributed Objects that satisfy the given predicate
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {

        return (map, canvas) -> {
            List<Attributed<Polygon>> polygonList = new ArrayList<>();
            List<Attributed<PolyLine>> polyLineList = new ArrayList<>();
            map.polygons().forEach(polygon -> {
                if (predicate.test(polygon)) {
                    polygonList.add(polygon);
                }
            });
            map.polyLines().forEach(polyline -> {
                if (predicate.test(polyline)) {
                    polyLineList.add(polyline);
                }
            });
            this.drawMap(new Map(polyLineList, polygonList), canvas);
        };
    }

    void drawMap(Map map, Canvas canvas);
}
