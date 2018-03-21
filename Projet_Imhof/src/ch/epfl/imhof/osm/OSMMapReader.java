package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * represents an xml files reader which are meant to contain OSMMap's
 * information
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OSMMapReader {
    private static final String ELEMENT_NODE = "node",
            ELEMENT_RELATION = "relation", ELEMENT_MEMBER = "member",
            ELEMENT_WAY = "way", ELEMENT_TAG = "tag", ELEMENT_ND = "nd";

    private OSMMapReader() {
    }

    /**
     * 
     * @param fileName
     *            file's name
     * @param unGZip
     *            a boolean which is set to true if the file is GZIP-compressed
     * @return an OSMMap object set with data contained in the xml file
     * @throws IOException
     *             in any case concerned in the general IOException throws
     *             (reading)
     * @throws SAXException
     *             when an error at reading occurs
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {
        OSMMap.Builder mapBuilder = new OSMMap.Builder();

        try (InputStream i = (unGZip ? new GZIPInputStream(
                new BufferedInputStream(new FileInputStream(fileName)))
                : new BufferedInputStream(new FileInputStream(fileName)))) {

            XMLReader r = XMLReaderFactory.createXMLReader();
            r.setContentHandler(new DefaultHandler() {
                // using an OSMEntity Builder permits us not to ask which object
                // a tag belongs to
                // when we begin reading one

                OSMNode.Builder nodeBuilder;
                OSMRelation.Builder relationBuilder;
                OSMWay.Builder wayBuilder;

                /**
                 * called when reaches "<...\>" or "\>"
                 */
                @Override
                public void endElement(String uri, String lName, String qName) {
                    if (qName.equals("relation") || qName.equals("way")
                            || qName.equals("node")) {
                        if (!getEntity().isIncomplete()) {
                            switch (qName) {
                            case ELEMENT_WAY:
                                mapBuilder.addWay(wayBuilder.build());
                                wayBuilder = null;
                                break;
                            case ELEMENT_RELATION:
                                mapBuilder.addRelation(relationBuilder.build());
                                relationBuilder = null;
                                break;
                            case ELEMENT_NODE:
                                mapBuilder.addNode(nodeBuilder.build());
                                nodeBuilder = null;
                                break;
                            }
                        }
                    }
                }

                /**
                 * called when reaches "<>"
                 */
                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                    case ELEMENT_NODE:
                        nodeBuilder = new OSMNode.Builder(Long.parseLong(atts
                                .getValue("id")), new PointGeo(Math
                                .toRadians(Double.parseDouble(atts
                                        .getValue("lon"))), Math
                                .toRadians(Double.parseDouble(atts
                                        .getValue("lat")))));

                        break;

                    case ELEMENT_WAY:
                        wayBuilder = new OSMWay.Builder(Long.parseLong(atts
                                .getValue("id")));
                        break;

                    case ELEMENT_ND:
                        if (mapBuilder.nodeForId(Long.parseLong(atts
                                .getValue("ref"))) == null) {
                            wayBuilder.setIncomplete();
                            break;
                        }
                        wayBuilder.addNode(mapBuilder.nodeForId(Long
                                .parseLong(atts.getValue("ref"))));
                        break;

                    case ELEMENT_RELATION:
                        relationBuilder = new OSMRelation.Builder(Long
                                .parseLong(atts.getValue("id")));
                        break;

                    case ELEMENT_MEMBER:
                        OSMEntity member;
                        OSMRelation.Member.Type type;
                        long ref = Long.parseLong(atts.getValue("ref"));
                        switch (atts.getValue("type")) {
                        case ELEMENT_WAY:
                            member = mapBuilder.wayForId(ref);
                            type = OSMRelation.Member.Type.WAY;
                            break;
                        case ELEMENT_NODE:
                            member = mapBuilder.wayForId(ref);
                            type = OSMRelation.Member.Type.NODE;

                            break;
                        case ELEMENT_RELATION:
                            member = mapBuilder.wayForId(ref);
                            type = OSMRelation.Member.Type.RELATION;

                            break;
                        default:
                            throw new SAXException();
                        }
                        if (member == null)
                            relationBuilder.setIncomplete();
                        else {
                            relationBuilder.addMember(type,
                                    atts.getValue("role"), member);
                        }
                        break;
                    case ELEMENT_TAG:
                        getEntity().setAttribute(atts.getValue("k"),
                                atts.getValue("v"));
                        break;
                    }
                }

                private OSMEntity.Builder getEntity() {
                    if (relationBuilder != null)
                        return relationBuilder;
                    return wayBuilder != null ? wayBuilder : nodeBuilder;
                }
            });
            r.parse(new InputSource(i));
        }
        return mapBuilder.build();
    }
}
