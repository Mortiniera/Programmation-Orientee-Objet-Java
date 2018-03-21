package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * a RoadPainterGenerator
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public class RoadPainterGenerator {

    /**
     * a road specification, different for tunnels, bridges, intern bridges,
     * etc.
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static class RoadSpec {
        private final Painter internBridgeP, borderBridgeP, internRoadsP,
                borderRoadsP, tunnelsP;

        /**
         * 
         * @param predicate
         *            a predicate
         * @param internWidth
         *            roads'intern width
         * @param internColor
         *            roads'intern color
         * @param borderWidth
         *            roads'border width
         * @param borderColor
         *            roads' border color
         */
        public RoadSpec(Predicate<Attributed<?>> predicate, float internWidth,
                Color internColor, float borderWidth, Color borderColor) {

            Predicate<Attributed<?>> tunnelP = Filters.tagged("tunnel");
            Predicate<Attributed<?>> bridgeP = Filters.tagged("bridge");
            LineStyle internLinestyle = new LineStyle(internWidth, internColor,
                    LineStyle.LineCap.Round, LineStyle.LineJoin.Round);
            LineStyle borderBridgeLinestyle = new LineStyle(internWidth + 2
                    * borderWidth, borderColor, LineStyle.LineCap.Butt,
                    LineStyle.LineJoin.Round);

            this.internBridgeP = Painter.line(internLinestyle).when(
                    predicate.and(Filters.tagged("bridge")));

            this.borderBridgeP = Painter.line(borderBridgeLinestyle).when(
                    predicate.and(bridgeP));

            this.internRoadsP = Painter.line(internLinestyle).when(
                    predicate.and(bridgeP.negate().and(tunnelP.negate())));

            this.borderRoadsP = Painter
                    .line(borderBridgeLinestyle
                            .withLineCap(LineStyle.LineCap.Round)).when(
                            predicate.and(bridgeP.negate()
                                    .and(tunnelP.negate())));

            this.tunnelsP = Painter.line(
                    internWidth / 2f,
                    borderColor,
                    LineStyle.LineCap.Butt,
                    LineStyle.LineJoin.Round,
                    new float[] { (float) (2 * internWidth),
                            (float) (2 * internWidth) }).when(
                    predicate.and(tunnelP));
        }
    }

    private RoadPainterGenerator() {
    }

    /**
     * 
     * @param roadSpecs
     *            an unknown number of road specifications
     * @return a painter that draws all roads
     */
    public static Painter painterForRoads(RoadSpec... roadSpecs) {

        Painter painter = roadSpecs[0].internBridgeP;

        for (RoadSpec roadspec : Arrays.asList(roadSpecs)) {
            painter = painter.above(roadspec.internBridgeP);
        }
        for (RoadSpec roadspec : Arrays.asList(roadSpecs)) {
            painter = painter.above(roadspec.borderBridgeP);
        }
        for (RoadSpec roadspec : Arrays.asList(roadSpecs)) {
            painter = painter.above(roadspec.internRoadsP);
        }
        for (RoadSpec roadspec : Arrays.asList(roadSpecs)) {
            painter = painter.above(roadspec.borderRoadsP);
        }
        for (RoadSpec roadspec : Arrays.asList(roadSpecs)) {
            painter = painter.above(roadspec.tunnelsP);
        }
        return painter;

    }
}
