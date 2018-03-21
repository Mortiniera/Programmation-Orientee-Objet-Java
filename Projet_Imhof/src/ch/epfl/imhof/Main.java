package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;

/**
 * a program that reads an HGT file and an OSM file ( compressed or not) and
 * extracts a map with it's reliefs
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public class Main {
    private final double BLURRADIUS = 1.7;
    private final PointGeo bottomL;
    private final int imageHeight;
    private final int imageWidth;
    private final double INCHPERMM = 0.03937007874016;
    private Map map = null;
    private final HGTDigitalElevationModel model;
    private final CH1903Projection PROJECTION = new CH1903Projection();

    private final int resolution;
    private final BufferedImage shadedImage;
    private final String targetFileName;
    private final PointGeo topR;

    private final OSMToGeoTransformer tranformer;

    /**
     * 
     * @param nameOSM
     *            the name of the OSM file
     * @param nameHGT
     *            the name of the HGT file
     * @param degreeLongitudeBL
     *            the degree longitude of the bottom left of the map in HGT file
     * @param degreeLatitudeBL
     *            the degree latitude of the bottom left of the map in HGT file
     * @param degreeLongitudeTR
     *            the degree longitude of the top right of the map in HGT file
     * @param degreeLatitudeTR
     *            the degree latitude of the top right of the map in HGT file
     * @param resolution
     *            the resolution
     * @param targetFileName
     *            the file in which we'll save the final image
     * @throws IOException
     *             for any reading/writing error
     * @throws jdk.internal.org.xml.sax.SAXException
     *             for any reading/writing error sets class attributes from the
     *             information given in files
     */
    public Main(String nameOSM, String nameHGT, double degreeLongitudeBL,
            double degreeLatitudeBL, double degreeLongitudeTR,
            double degreeLatitudeTR, int resolution, String targetFileName)
            throws IOException, jdk.internal.org.xml.sax.SAXException {
        this.bottomL = new PointGeo(Math.toRadians(degreeLongitudeBL),
                Math.toRadians(degreeLatitudeBL));
        this.topR = new PointGeo(Math.toRadians(degreeLongitudeTR),
                Math.toRadians(degreeLatitudeTR));
        this.resolution = resolution;
        this.targetFileName = targetFileName;
        this.imageHeight = (int) Math.round(Earth.RADIUS
                * (INCHPERMM * 1000 * resolution / 25000d)
                * (Math.toRadians(degreeLatitudeTR) - Math
                        .toRadians(degreeLatitudeBL)));
        Point projectedTR = PROJECTION.project(topR);
        Point projectedBL = PROJECTION.project(bottomL);
        this.imageWidth = (int) Math
                .round((imageHeight * (projectedTR.x() - projectedBL.x()) / (projectedTR
                        .y() - projectedBL.y())));
        // ++++++++++++++++++++++
        this.tranformer = new OSMToGeoTransformer(PROJECTION);
        try {
            this.map = tranformer.transform(OSMMapReader.readOSMFile(getClass()
                    .getResource("/" + nameOSM).getFile(), true));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        this.model = new HGTDigitalElevationModel(new File(getClass()
                .getResource("/" + nameHGT).getFile()));
        Vector3 lightIncome = new Vector3(-1.0, 1.0, 1.0);
        ReliefShader shader = new ReliefShader(PROJECTION, model, lightIncome);
        this.shadedImage = shader.shadedRelief(projectedTR, projectedBL,
                imageWidth, imageHeight, BLURRADIUS * resolution * INCHPERMM);
    }

    /**
     * 
     * @param args
     *            arguments given in run configurations
     * @throws NumberFormatException
     *             if some arguments that should be numbers are not
     * @throws IOException
     *             for any reading/writing error
     */
    public static void main(String[] args) throws NumberFormatException,
            IOException {
        Main main = null;
        try {
            main = new Main(args[0], args[1], Double.parseDouble(args[2]),
                    Double.parseDouble(args[3]), Double.parseDouble(args[4]),
                    Double.parseDouble(args[5]), Integer.parseInt(args[6]),
                    args[7]);
        } catch (jdk.internal.org.xml.sax.SAXException e) {
            System.out.println("unassigned main, unable to read all args");
            e.printStackTrace();
        }

        Painter painter = SwissPainter.painter();
        // create java2d canvas
        Java2DCanvas canvas = new Java2DCanvas(
                main.PROJECTION.project(main.bottomL),
                main.PROJECTION.project(main.topR), main.imageWidth,
                main.imageHeight, main.resolution, Color.WHITE);
        painter.drawMap(main.map, canvas);
        // get colored image without reliefs
        BufferedImage coloredImage = canvas.image();

        BufferedImage finalImage = new BufferedImage(main.imageWidth,
                main.imageHeight, BufferedImage.TYPE_INT_RGB);
        // mix colored and relief images
        for (int x = 0; x < finalImage.getWidth(); x++) {
            for (int y = 0; y < finalImage.getHeight(); y++) {
                Color finalColor = Color.rgb(main.shadedImage.getRGB(x, y))
                        .multiply(Color.rgb(coloredImage.getRGB(x, y)));
                finalImage.setRGB(x, y, finalColor.convert().getRGB());
            }
        }
        ImageIO.write(finalImage, "png", new File(main.targetFileName));
    }

}
