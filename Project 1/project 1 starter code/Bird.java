import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Bird extends AnimatedEntity
{
    private DFSPathingStrategy strategy = new DFSPathingStrategy();
    private EventScheduler scheduler;
    private Point nextPosition = new Point(0,0);
    private String directionOfTravel = "right";
    private List<Point> path = null;

    public Bird(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 2, 2);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), 120);
        scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
        this.scheduler = scheduler;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (path == null || getPosition().equals(nextPosition) || path.size() == 0) {
            nextPosition =  new Point((int) (Math.random() * 29), (int) (Math.random() * 39));
            path = strategy.computePath(getPosition(), nextPosition,
                    p -> (world.backgroundType[p.getX()][p.getY()] == null || !world.backgroundType[p.getX()][p.getY()].equals("Mine")),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
        }
        if (path.size() > 0) {
            directionOfTravel = getDirectionOfTravel(path.get(0));
            if (!world.isOccupied(path.get(0))) {
                world.moveEntity(this, path.get(0));
                path.remove(path.get(0));
            }
        }
        scheduleActions(scheduler, world, imageStore);
    }

    private String getDirectionOfTravel(Point nextPoint)
    {
        if (nextPoint.getY() > this.getPosition().getY())
            return "down";
        else if(nextPoint.getY() < this.getPosition().getY())
            return "up";
        else if (nextPoint.getX() > this.getPosition().getX())
            return "right";
        else
            return "left";
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
    }

    public void nextImage(String directionOfTravel)
    {
        int newImageIndex = 0;
        boolean wingsUp = (this.getImageIndex() % 2) == 0;
        switch(this.directionOfTravel){
            case "right":
                if (wingsUp) newImageIndex = 1;
                else newImageIndex = 0;
                break;
            case "down":
                if (wingsUp) newImageIndex = 3;
                else newImageIndex = 2;
                break;
            case "left":
                if (wingsUp) newImageIndex = 5;
                else newImageIndex = 4;
                break;
            case "up":
                if (wingsUp) newImageIndex = 7;
                else newImageIndex = 6;
                break;
        }
        this.setImageIndex(newImageIndex);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(1),
                this.getAnimationPeriod());
    }
}
