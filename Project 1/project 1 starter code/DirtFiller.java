import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class DirtFiller extends AnimatedEntity
{

    private SingleStepPathingStrategy strategy = new SingleStepPathingStrategy();
    private Point nextDirt = new Point(33,24);

    public int shoutCount;

    public DirtFiller(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 2, 2);
        this.shoutCount = 0;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        if (this.getPosition().equals(new Point(39, 29)))
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), 10000);
        else
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), 700);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (this.shoutCount >= 20)
            nextDirt = new Point(39,29);
        Point startPoint = new Point(33, 24);
        List<Point> path;
        if (!world.canMove(this.getPosition())) {
            path = strategy.computePath(getPosition(), startPoint,
                    p -> true,
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
            if (path.size() != 0)
                world.moveEntity(this, path.get(0));
        } else {
            path = strategy.computePath(getPosition(), nextDirt,
                    p -> world.canMove(p),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
            if (path.size() == 0 || path.get(0).equals(nextDirt)) {
                world.moveEntity(this, nextDirt);
                world.background[nextDirt.getY()][nextDirt.getX()] = new Background("grass", imageStore.getImageList("grass"));
                world.manager.grassCount += 1;
                world.backgroundType[nextDirt.getX()][nextDirt.getY()] = world.manager.mineField[nextDirt.getX()][nextDirt.getY()];
                nextDirt = world.findNextDirt(imageStore);
            } else {
                world.moveEntity(this, path.get(0));
            }
        }
        scheduleActions(scheduler, world, imageStore);
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
    }


    @Override
    void nextImage(String d) {

    }
}
