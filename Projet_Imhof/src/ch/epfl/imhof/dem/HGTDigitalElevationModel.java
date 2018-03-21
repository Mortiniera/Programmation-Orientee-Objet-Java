package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import jdk.internal.org.xml.sax.SAXException;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * an HGTDigitalElevationModel, which is an ElevationModel Built using an HGT
 * file
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel,
        Earth {
    private final PointGeo bottomLeft;
    private final int latSW, longSW;
    private final double resolution;
    private final ShortBuffer shortB;
    private final int squareSide;

    /**
     * 
     * @param file
     *            an HGT file
     * @throws IOException
     *             for any reading error
     * @throws SAXException
     *             for any reading error sets class attributes using HGT file
     */
    public HGTDigitalElevationModel(File file) throws IOException, SAXException {

        if (!file.getName().matches("^[NS]\\d\\d[EW]\\d\\d\\d\\.hgt$"))
            throw new IllegalArgumentException("invalid fileName format");
        long fileLength = file.length();
        if (Math.sqrt(fileLength / 2d) % 1 == 0.0)
            this.squareSide = (int) Math.round(Math.sqrt(fileLength / 2d));
        else
            throw new IllegalArgumentException(
                    "resolution does not have an int sqrt value");
        this.resolution = Math.toRadians(1d / (squareSide - 1));
        FileInputStream stream = new FileInputStream(file);
        this.shortB = stream.getChannel().map(MapMode.READ_ONLY, 0, fileLength)
                .asShortBuffer();

        stream.close();
        String name = file.getName();
        int absLat = Integer.parseInt(name.substring(1, 3));
        this.latSW = name.charAt(0) == 'N' ? absLat : -absLat;
        int absLong = Integer.parseInt(name.substring(4, 7));
        this.longSW = name.charAt(3) == 'E' ? absLong : -absLong;
        this.bottomLeft = new PointGeo(Math.toRadians(longSW),
                Math.toRadians(latSW));

    }

    @Override
    public void close() throws Exception {
        this.close();
    }

    @Override
    public Vector3 normalAt(PointGeo pointGeo) {

        if (!correctPoint(pointGeo)) {
            throw new IllegalArgumentException(" Incorrect point coordinate");
        }
        double x = (pointGeo.longitude() - bottomLeft.longitude()) / resolution;
        double y = (pointGeo.latitude() - bottomLeft.latitude()) / resolution;
        int i = (int) Math.floor(x);
        int j = (int) Math.floor(y);

        short zIJ = valueAt(i, j);
        short zIJ1 = valueAt(i, j + 1);
        short zI1J = valueAt(i + 1, j);
        short zI1J1 = valueAt(i + 1, j + 1);

        // System.out.println("i " + i + " j " + j + " " + zIJ + "  " + zI1J +
        // " " + zIJ1 + " " + zI1J1);
        double s = RADIUS * resolution;
        return new Vector3((s / 2.0) * (zIJ - zI1J + zIJ1 - zI1J1), (s / 2.0)
                * (zIJ + zI1J - zIJ1 - zI1J1), s * s);
    }

    private boolean correctPoint(PointGeo point) {
        return (point.longitude() >= bottomLeft.longitude())
                && (point.longitude() <= bottomLeft.longitude()
                        + Math.toRadians(1))
                && (point.latitude() >= bottomLeft.latitude())
                && (point.latitude() <= bottomLeft.latitude()
                        + Math.toRadians(1));
    }

    private short valueAt(int x, int y) {
        return shortB.get(squareSide * (squareSide - y - 1) + x);
    }

}
