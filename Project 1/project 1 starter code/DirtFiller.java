import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class DirtFiller extends AnimatedEntity
{

    private SingleStepPathingStrategy strategy = new SingleStepPathingStrategy();
    private Point nextDirt = new Point(33,24);

    public int shoutCount;
    private EventScheduler scheduler;
    private String directionOfTravel = "crouch";

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
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
        this.scheduler = scheduler;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (this.shoutCount >= 20) {
            nextDirt = new Point(39, 29);
            this.shoutCount = 0;
        }
        Point startPoint = new Point(33, 24);
        List<Point> path;
        if (!world.canMove(this.getPosition())) {
            path = strategy.computePath(getPosition(), startPoint,
                    p -> true,
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
            if (path.size() != 0) {
                directionOfTravel = getDirectionOfTravel(path.get(0));
                if (!world.isOccupied(path.get(0)))
                    world.moveEntity(this, path.get(0));
            }
        } else {
            path = strategy.computePath(getPosition(), nextDirt,
                    p -> world.canMove(p),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
            if (path.size() == 0 || path.get(0).equals(nextDirt)) {
                directionOfTravel = "crouch";
                world.moveEntity(this, nextDirt);
                world.background[nextDirt.getY()][nextDirt.getX()] = new Background("grass", imageStore.getImageList("grass"));
                world.manager.grassCount += 1;
                world.backgroundType[nextDirt.getX()][nextDirt.getY()] = world.manager.mineField[nextDirt.getX()][nextDirt.getY()];
                nextDirt = world.findNextDirt(imageStore);
            } else {
                directionOfTravel = getDirectionOfTravel(path.get(0));
                if (!world.isOccupied(path.get(0)))
                    world.moveEntity(this, path.get(0));
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


    @Override
    public void nextImage(String directionOfTravel)
    {
        int newImageIndex = 0;
        boolean closeLegs = (this.getImageIndex() % 2) == 0;
        switch(this.directionOfTravel){
            case "right":
                if (closeLegs) newImageIndex = 1;
                else newImageIndex = 0;
                break;
            case "down":
                if (closeLegs) newImageIndex = 3;
                else newImageIndex = 2;
                break;
            case "left":
                if (closeLegs) newImageIndex = 5;
                else newImageIndex = 4;
                break;
            case "up":
                if (closeLegs) newImageIndex = 7;
                else newImageIndex = 6;
                break;
            case "crouch":
                newImageIndex = 8;
                break;
        }
        this.setImageIndex(newImageIndex % this.getImages().size());
        scheduler.scheduleEvent(this,
                this.createAnimationAction(1),
                this.getAnimationPeriod());
    }
}
