package ch.epfl.imhof;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;

public class Interlaken {
    public Map map = null;
    CH1903Projection projection = new CH1903Projection();
    OSMToGeoTransformer tranformer = new OSMToGeoTransformer(projection);

    public Interlaken() {
        try {
            map = tranformer.transform(OSMMapReader.readOSMFile(getClass()
                    .getResource("/interlaken.osm.gz").getFile(), true));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        long t = System.currentTimeMillis();
        
        Interlaken interlakenPainter = new Interlaken();
        Painter painter = SwissPainter.painter();

        // La toile
        Point bl = new Point(628590, 168210);
        Point tr = new Point(635660, 172870);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800, 530, 72, Color.WHITE);
        
        //Dessin de la carte et stockage dans un fichier
        painter.drawMap(interlakenPainter.map, canvas);
        ImageIO.write(canvas.image(), "png", new File("Interlaken.png"));
        System.out.println((System.currentTimeMillis()-t)/1000 + "s");
    }
}
