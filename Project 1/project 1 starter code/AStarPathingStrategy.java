import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PointNode implements Comparable<PointNode> {
    private final int g;
    private final int f;
    private final Point point;
    private final PointNode priorVertex;

    public PointNode(int g, int f, Point point, PointNode priorVertex){
        this.g = g;
        this.f = f;
        this.point = point;
        this.priorVertex = priorVertex;
    }

    @Override
    public int compareTo(PointNode other){
        if (this.getF() != other.getF())
            return Integer.compare(this.getF(), other.getF());
        return Integer.compare(this.getG(), other.getG());
    }

    public int getG() {
        return g;
    }

    public int getF() {
        return f;
    }

    public Point getPoint() {
        return point;
    }

    public PointNode getPriorVertex() {
        return priorVertex;
    }

}

class AStarPathingStrategy implements PathingStrategy
{

    //hashset for closed list
    //priority queue for open list


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        PriorityQueue<PointNode> openList = new PriorityQueue<>();
        Hashtable<Point, PointNode> closedList = new Hashtable<>(); // key is point, value is prior node

        // start open list with starting node
        PointNode firstNode = new PointNode(0, calculateF(start, end, 0), start, null);
        PointNode current = firstNode;
        openList.add(current);

        while (current != null && !withinReach.test(current.getPoint(), end)) {
            List<Point> neighbors =
                    potentialNeighbors.apply(current.getPoint())
                            .filter(canPassThrough)
                            .collect(Collectors.toList());

            for (Point p : neighbors) {

                int g = current.getG() + 1;
                PointNode pn = new PointNode(g, calculateF(p, end, g), p, current);

                boolean add = true;

                for (PointNode open : openList) {
                    if (open.getPoint().equals(pn.getPoint())) {
                        add = false;
                        break;
                    }
                }

                if (closedList.containsKey(p)){
                    PointNode nodeInList = closedList.get(p);
                    if (nodeInList.getG() + 1 <= g){
                        continue;
                    }
                }

                if (add){
                    openList.add(pn);
                }

            }

            if (current.getPriorVertex() == null) {
                closedList.put(current.getPoint(), firstNode);
            } else {
                closedList.put(current.getPoint(), current.getPriorVertex());
            }

            openList.remove(current);

            current = openList.poll();
        }


        List<Point> path = new LinkedList<>();
        Point next;

        if (current != null) {

            path.add(current.getPoint());
            next = current.getPriorVertex().getPoint();

            while (next != start) {
                path.add(next);
                next = closedList.get(next).getPoint();
            }

        }


        Collections.reverse(path);
        return path;
    }

    private int distance(Point start, Point end){
        return Math.abs(start.getX()-end.getX()) + Math.abs(start.getY()-end.getY());
    }

    private int calculateF(Point current, Point end, int g){
        return g + distance(current, end);
    }



}
