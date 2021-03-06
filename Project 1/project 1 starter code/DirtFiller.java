import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DirtFiller extends AnimatedEntity
{
    public DirtFiller(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 2, 2);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        SingleStepPathingStrategy strategy = new SingleStepPathingStrategy();
        Point nearestDirt = new Point (34,24);
        if (world.canMove(getPosition()))
        {
            nearestDirt = world.findNextDirt(this.getPosition(), imageStore);
        }
        List<Point> path = strategy.computePath(getPosition(), nearestDirt,
                           p -> world.canMove(p),
                        (p1, p2) -> neighbors(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        System.out.println(path.toString());
        world.moveEntity(this, path.get(0));
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
    }


}
