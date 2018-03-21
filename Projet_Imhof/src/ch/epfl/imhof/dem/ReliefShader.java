package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * a relief shader, draws relief given a DigitalElevationModel and a projection
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class ReliefShader {
    private final Vector3 lightIncome;
    private final DigitalElevationModel model;
    private final Projection projection;

    /**
     * 
     * @param projection
     *            a projection
     * @param model
     *            a DigitalElevationModel
     * @param lightIncome
     *            a Vector3 lightIncome
     */
    public ReliefShader(Projection projection, DigitalElevationModel model,
            Vector3 lightIncome) {
        this.projection = projection;
        this.model = model;
        this.lightIncome = lightIncome;
    }

    /**
     * 
     * @param topR
     *            the image's top right point
     * @param bottomL
     *            the image's bottom left point
     * @param width
     *            the image's width
     * @param height
     *            the image's height
     * @param radius
     *            the gaussian'kernel's radius
     * @return a buffered image showing the relief given by the
     *         DigitalElevationModel
     */
    public BufferedImage shadedRelief(Point topR, Point bottomL, int width,
            int height, double radius) {
        Function<Point, Point> imageToPlan = Point.alignedCoordinateChange(
                new Point(0.0, height), bottomL, new Point(width, 0.0), topR);
        BufferedImage image = rawShadedRelief(width, height, imageToPlan);
        // we have a raw image
        return blurredShadedRelief(radius, image, getData(radius));
    }

    private BufferedImage blurredShadedRelief(double radius,
            BufferedImage image, float[] data) {
        // we create é kernels to blur horizontally and vertically
        Kernel kernel1 = new Kernel(data.length, 1, data);
        Kernel kernel2 = new Kernel(1, data.length, data);
        // we create 2 convolve
        ConvolveOp convOp1 = new ConvolveOp(kernel1, ConvolveOp.EDGE_NO_OP,
                null);
        ConvolveOp convOp2 = new ConvolveOp(kernel2, ConvolveOp.EDGE_NO_OP,
                null);
        // blur 2 times
        BufferedImage blurredImage = convOp2.filter(
                convOp1.filter(image, null), null);
        ; // null fait
        // expres
        int ceilR = (int) Math.ceil(radius);
        blurredImage.getSubimage(ceilR, ceilR, image.getWidth() - 2 * ceilR,
                image.getHeight() - 2 * ceilR);
        return blurredImage;
    }

    private Color colorAtRelief(Vector3 vec1, Vector3 lightIncome) {
        double cos = cos(vec1, lightIncome);
        return Color.rgb(0.5 * (cos + 1), 0.5 * (cos + 1),
                0.5 * (0.7 * cos + 1));
    }

    private double cos(Vector3 vec1, Vector3 vec2) {
        return vec1.scalarProduct(vec2) / (vec1.norm() * vec2.norm());
    }

    // creates the data needed for the kernels
    private float[] getData(double radius) {
        double sigma = radius / 3d;
        int n = (int) (2 * Math.ceil(radius) + 1);
        float[] data = new float[n];
        double sum = 0.0;
        for (int i = 0; i < data.length; i++) {
            int index = i - data.length / 2;
            data[i] = (float) Math.exp(-0.5
                    * ((index * index) / (2 * sigma * sigma)));
            sum += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] /= sum;
        }
        return data;
    }

    private BufferedImage rawShadedRelief(int width, int height,
            Function<Point, Point> imageToPlan) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // transfer point from image to plan
                Point pPlan = imageToPlan.apply(new Point(x, y));
                PointGeo pGeo = projection.inverse(pPlan);
                // gets the color for given point
                Color rgb = colorAtRelief(model.normalAt(pGeo).normalized(),
                        lightIncome);
                image.setRGB(x, y, rgb.convert().getRGB());
            }
        }
        return image;
    }
}
