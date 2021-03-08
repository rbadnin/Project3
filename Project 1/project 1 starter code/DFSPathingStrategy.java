import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DFSPathingStrategy implements PathingStrategy{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> path = new ArrayList<>();
        List<Point> searched = new ArrayList<>();
        dfs(start, path, canPassThrough, withinReach, searched, end);

        if (path.size() > 0) {
            path.remove(0);
        }
        return path;

    }

    private boolean dfs(Point pos, List<Point> path, Predicate<Point> canPassThrough,
                        BiPredicate<Point, Point> withinReach, List<Point> searched, Point end)
    {
        boolean found = false;

        if (pos.getX() < 40 && pos.getX() >-1 &&
                pos.getY() < 30 && pos.getY() >-1) {
            if (!(canPassThrough.test(pos) && !searched.contains(pos)))
                found = false;

            else if (withinReach.test(pos, end))
                found = true;

            else {
                searched.add(pos);
                found = dfs(new Point(pos.getX() + 1, pos.getY()), path, canPassThrough, withinReach, searched, end) ||
                        dfs(new Point(pos.getX() - 1, pos.getY()), path, canPassThrough, withinReach, searched, end) ||
                        dfs(new Point(pos.getX(), pos.getY() + 1), path, canPassThrough, withinReach, searched, end) ||
                        dfs(new Point(pos.getX(), pos.getY() - 1), path, canPassThrough, withinReach, searched, end);

            }

            if (found)
                path.add(0, pos);
            return found;
        }
        return found;
    }



}
