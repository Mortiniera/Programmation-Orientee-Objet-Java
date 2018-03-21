package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Un convertisseur de donnees OSM en carte
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OSMToGeoTransformer {

    private final Projection projection;
    private static final Set<String> closedAttributes = new HashSet<>(Arrays.asList(
            new String[] { "aeroway", "amenity", "building",
                    "harbour", "historic", "landuse", "leisure", "man_made",
                    "military", "natural", "office", "place", "power",
                    "public_transport", "shop", "sport", "tourism", "water",
                    "waterway", "wetland" }));
    private static final Set<String> polyLinesAttributes = new HashSet<>(Arrays.asList(
            new String[] { "bridge", "highway", "layer",
                    "man_made", "railway", "tunnel", "waterway" }));
    private static final Set<String> polygonsAttributes = new HashSet<>(Arrays.asList(
            new String[] { "building", "landuse", "layer",
                    "leisure", "natural", "waterway" }));

    /**
     * 
     * @param projection
     *            a projection Ch1903 or equirectangular
     * 
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * @param way
     *            an OSMWay
     * @return true if the way is said to be "Closed" (following the given
     *         definition)
     */
    private boolean isClosedWayAndArea(OSMWay way) {
        if (!way.isClosed()) {
            return false;
        } 
        else {
            boolean area = false;
            if (way.hasAttribute("area")) {
                if (way.attributeValue("area").equals("1")
                        || way.attributeValue("area").equals("true")
                        || way.attributeValue("area").equals("yes")) {
                    area = true;
                }
            }

            boolean hasAttribut = false;
            for (String attribut : closedAttributes) {
                if (way.hasAttribute(attribut)) {
                    if (way.attributeValue(attribut) != null)
                        hasAttribut = true;
                }
            }
            return area || hasAttribut;
        }
    }

    /**
     * 
     * @param way
     *            an OSMWay
     * @return  the list's points corresponding to the positions of the nodes in the way
     */
    private List<Point> wayToPointList(OSMWay way) {
        List<Point> points = new ArrayList<>();
        for (OSMNode node : way.nonRepeatingNodes()) {
            points.add(projection.project(node.position()));
        }
        return points;
    }

    /**
     * 
     * @param map
     *            an OSMMap
     * @return the projected OSMMap depending on the class'attribute's
     *         projection
     */
    public Map transform(OSMMap map) {
        Map.Builder mapB = new Map.Builder();
        List<Attributed<Polygon>> polygons = new ArrayList<>();

        /*
         * for each relation of the map
         *  if the current relation has the attribute "type", 
         *  and its value is "multipolygon", then we create a new map of attributes
         *  with the attributes to keep for the polygons with holes according 
         *  to the definition given
         *  and then, if this map of attributes is not empty, we create a new attributed polygon with holes
         *  and add it to the list of the polygons
         */
        for (OSMRelation relation : map.relations()) {
            if (relation.hasAttribute("type")) {
                if (relation.attributeValue("type").equals("multipolygon")) {

                    Attributes attributs = relation.attributes().keepOnlyKeys(
                            polygonsAttributes);
                    if (!attributs.isEmpty()) {
                        polygons.addAll(assemblePolygon(relation, attributs));
                    }
                }
            }
        }
        /*
         * we add all the polygons in the mapBuilder
         */
        for (Attributed<Polygon> polygon : polygons) {
            mapB.addPolygon(polygon);
        }
        List<OSMWay> ways = new ArrayList<>(map.ways());


        //for each way of the map
        for (OSMWay way : ways) {
            /*
             * if the current way is closed and its attributes indicates that it describse an area, 
             * then we create a new map of attributes with
             * the ones we need to keep according to the definition given
             * then if it is not empty, we add a new holeless polygon with current attributes to the mapBuilder
             */
            if (isClosedWayAndArea(way)) {
                Attributes attributs = way.attributes();
                if (!attributs.isEmpty()) {
                    mapB.addPolygon(new Attributed<Polygon>(new Polygon(
                            new ClosedPolyLine(wayToPointList(way))), attributs));
                }
                /*
                 * otherwise, if the current way isn't an area and its attributes are not empty
                 * then we need to check if this way is closed or not
                 * in the first case, we create a new polyline from a new attributed closedPolyline,
                 * and we add it to the map
                 * in the other case, we create and add to the map a new polyline from a
                 * new attributed openPolyline
                 */
            } else {
                Attributes attributs = way.attributes().keepOnlyKeys(
                        polyLinesAttributes);

                if (!attributs.isEmpty()) {
                    if (way.isClosed()) {
                        mapB.addPolyLine(new Attributed<PolyLine>(
                                new ClosedPolyLine(wayToPointList(way)),
                                attributs));
                    } else {
                        mapB.addPolyLine(new Attributed<PolyLine>(
                                new OpenPolyLine(wayToPointList(way)),
                                attributs));
                    }
                }
            }
        }
        /*
         * finally we build the current mapBuilder with all the polygons and polylines added
         * until now
         */
        return mapB.build();
    }

    /**
     * 
     * @param relation
     *            a relation
     * @param role
     *            a role: inner or outer
     * @return a ClosedPolyLine's List built with the argument's relation
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        Set<OSMWay> ways = new HashSet<>();
        /*
         * Building a Set of ways with role inner or outer
         */
        for (OSMRelation.Member member : relation.members()) {
            if (member.role().equals(role) && member.type().equals(OSMRelation.Member.Type.WAY)) {
                ways.add(((OSMWay) member.member()));
            }
        }
        // Building the graph by adding nodes and edges
        Graph.Builder<OSMNode> graphB = new Graph.Builder<>();
        for (OSMWay way : ways) {
            int size = way.nodesCount();
            for (OSMNode node : way.nodes()) {
                graphB.addNode(node);
            }
            for (int i = 0; i < size - 1; i++) {
                graphB.addEdge(way.nodes().get(i), way.nodes().get(i + 1));
            }
        }
        // graph is built, we now need to build rings
        Graph<OSMNode> graph = graphB.build();
        Iterator<OSMNode> nodeIt = graph.nodes().iterator();
        Set<OSMNode> visitedNodes = new HashSet<>();
        List<ClosedPolyLine> closedPolyList = new ArrayList<>();
        PolyLine.Builder polyB;
        // iteration over nodes from the graph
        for (OSMNode node : graph.nodes()) {
            // only care for the non-visited ones
            if (graph.neighborsOf(node).size() != 2) {
                return Collections.emptyList();
            }
        }
        // keep visiting while we haven't seen each node
        while (!visitedNodes.containsAll(graph.nodes()) && nodeIt.hasNext()) {

            OSMNode currentNode = nodeIt.next();
            polyB = new PolyLine.Builder();
            polyB.addPoint(projection.project(currentNode.position()));
            Set<OSMNode> currentNeighbors = new HashSet<>(graph.neighborsOf(currentNode));
            /*
             * the loop does nothing if the current node has already been
             * visited, in this case we'll take the next one till it's not
             * visited, or stop the loop if every node has been visited
             */
            if (!visitedNodes.contains(currentNode)) {

                visitedNodes.add(currentNode);
                /*
                 * from the 2 neighbors, for each node, there's at least one we
                 * haven't visited yet. if not, then we can create a ring because
                 * we found each node for it
                 */
                while (!visitedNodes.containsAll(currentNeighbors)) {

                    Set<OSMNode> intersection = new HashSet<>(visitedNodes);
                    // the intersection between the visited and the neighbors,
                    // which is meant
                    // to be the visited one
                    intersection.retainAll(currentNeighbors);
                    // currentNeighbors now contains the last nonVisited neighbor
                    currentNeighbors.removeAll(intersection);
                    OSMNode onlyNeighbor = currentNeighbors.iterator().next();
                    visitedNodes.add(onlyNeighbor);
                    polyB.addPoint(projection.project(onlyNeighbor.position()));
                    currentNode = onlyNeighbor;
                    currentNeighbors = new HashSet<>(graph.neighborsOf(currentNode));

                }
                closedPolyList.add(polyB.buildClosed());
                polyB = new PolyLine.Builder();
            }
        }
        return closedPolyList;
    }

    /**
     * 
     * @param relation
     *            an OSMRelation
     * @param attributes
     *            some attributes
     * @return an attributed Polygon's list
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,Attributes attributes) {

        List<ClosedPolyLine> outers = new ArrayList<>(ringsForRole(relation,"outer"));
        List<ClosedPolyLine> inners = new ArrayList<>(ringsForRole(relation,"inner"));
        List<Attributed<Polygon>> polygonList = new ArrayList<>();
        Comparator<ClosedPolyLine> areaComparator = new Comparator<ClosedPolyLine>() {
            /**
             * compares two ClosedPolyLine with their respective areas
             */
            @Override
            public int compare(ClosedPolyLine o1, ClosedPolyLine o2) {
                return Double.compare(o1.area(), o2.area());
            }

        };

        Collections.sort(outers, areaComparator);
        Collections.sort(inners, areaComparator);
        List<ClosedPolyLine> innersForOuter;
        // iterate over outers
        for (ClosedPolyLine outer : outers) {

            innersForOuter = new ArrayList<>();
            List<ClosedPolyLine> removedInners = new ArrayList<>();
            // iterate over inners for each outer
            for (int j = 0; j < inners.size(); j++) {
                if (outer.containsPoint(inners.get(j).firstPoint())) {
                    innersForOuter.add(inners.get(j));
                    removedInners.add(inners.get(j));
                }
            }
            // remove all inners that have already been added to an outer
            inners.removeAll(removedInners);
            polygonList.add(new Attributed<Polygon>(
                    new Polygon(outer,innersForOuter), attributes.keepOnlyKeys(polygonsAttributes)));
        }
        return polygonList;
    }
}
