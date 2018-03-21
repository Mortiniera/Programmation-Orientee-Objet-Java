package ch.epfl.imhof.painting;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Represents a canvas in two dimensions
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */

public class Java2DCanvas implements Canvas {

    private static final float MITERLIMIT = 10f, DASHPHASE = 0f;
    private Graphics2D graphic;
    private final BufferedImage image;
    private final Function<Point, Point> transfer;

    /**
     * 
     * @param pbl
     *            bottom left point
     * @param ptr
     *            top right point
     * @param width
     *            : canvas' width
     * @param height
     *            : canvas' height
     * @param resolution
     *            : canvas'resolution
     * @param backgroundColor
     *            : canvas'background color
     * @throws IOException
     *             for any drawing error
     * @throws IllegalArgumentException
     *             if width or height are strictly negatives or resolution is
     *             negative or zero
     */
    public Java2DCanvas(Point pbl, Point ptr, int width, int height,
            int resolution, Color backgroundColor) throws IOException {
        if (width < 0 || height < 0 || resolution <= 0) {
            throw new IllegalArgumentException();
        }
        
        this.transfer = Point.alignedCoordinateChange(pbl, new Point(0.0,
                height / (resolution / 72d)), ptr, new Point(width
                / (resolution / 72d), 0.0));
        this.image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        
        this.graphic = image.createGraphics();
        graphic.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphic.setColor(backgroundColor.convert());
        graphic.fillRect(0, 0, width, height);
        graphic.scale(resolution / 72d, resolution / 72d);
    }

    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        Path2D path = pathFromPolyLine(polygon.shell(), transfer);
        Area area = new Area(path);

        for (ClosedPolyLine line : polygon.holes()) {
            area.subtract(new Area(pathFromPolyLine(line, transfer)));
        }
        graphic.setColor(color.convert());
        graphic.fill(area);
    }

    @Override
    public void drawPolyLine(PolyLine polyline, LineStyle linestyle) {
        BasicStroke bStroke;
        if (linestyle.dashingPattern().length == 0)
            bStroke = new BasicStroke((float) linestyle.width(),
                    getCap(linestyle.lineCap()), getJoin(linestyle.lineJoin()),
                    MITERLIMIT);
        else {
            bStroke = new BasicStroke((float) linestyle.width(),
                    getCap(linestyle.lineCap()), getJoin(linestyle.lineJoin()),
                    MITERLIMIT, linestyle.dashingPattern(), DASHPHASE);
        }
        Path2D path = pathFromPolyLine(polyline, transfer);
        graphic.setStroke(bStroke);
        graphic.setColor(linestyle.colorLine().convert());
        graphic.draw(path);
    }

    /**
     * 
     * @return the canvas' image
     */
    public BufferedImage image() {
        return image;
    }

    private int getCap(LineCap lineCap) {
        switch (lineCap.ordinal()) {
        case 0:
            return BasicStroke.CAP_BUTT;
        case 1:
            return BasicStroke.CAP_ROUND;
        case 2:
            return BasicStroke.CAP_SQUARE;
        default:
            throw new IllegalArgumentException("invalid LineCap");
        }
    }

    private int getJoin(LineJoin lineJoin) {
        switch (lineJoin.ordinal()) {
        case 0:
            return BasicStroke.JOIN_BEVEL;
        case 1:
            return BasicStroke.JOIN_MITER;
        case 2:
            return BasicStroke.JOIN_ROUND;
        default:
            throw new IllegalArgumentException("invalid LineJoin");
        }
    }

    // transforms a polyline into a path
    private Path2D pathFromPolyLine(PolyLine polyline,
            Function<Point, Point> transfer) {
        Path2D path = new Path2D.Double();

        path.moveTo(transfer.apply(polyline.firstPoint()).x(),
                transfer.apply(polyline.firstPoint()).y());
        for (Point point : polyline.points()) {
            path.lineTo(transfer.apply(point).x(), transfer.apply(point).y());
        }
        if (polyline.isClosed())
            path.closePath();
        return path;
    }
}
