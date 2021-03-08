import processing.core.PImage;

import java.util.List;

public class Scorpion extends AnimatedEntity{
    private AStarPathingStrategy strategy = new AStarPathingStrategy();
    private EventScheduler scheduler;
    public Point nextPosition;
    private List<Point> path = null;
    private String directionOfTravel = "up";
    public Point startingPos = new Point(0,0);
    public int scorpionRandomCount = 0;

    public Scorpion(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 2, 2);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), 50);
        scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
        this.scheduler = scheduler;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (this.getPosition().equals(startingPos)){
            this.setPosition(new Point(this.getPosition().getX() + 1, this.getPosition().getY()));
            world.tryAddEntity(this);
            startingPos = new Point(0,0);
            int randomX = (int) (Math.random() * 29 + 1) + world.manager.XOFFSET;
            int randomY = (int) (Math.random() * 20) + world.manager.XOFFSET;
            nextPosition =  new Point(randomY, randomX);
            path = strategy.computePath(getPosition(), nextPosition,
                    p -> (world.canMove(p) && (world.backgroundType[p.getX()][p.getY()] == null || !world.backgroundType[p.getX()][p.getY()].equals("Mine"))),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
        }

        else if (getPosition().equals(nextPosition) || path.size() == 0) {
            if (scorpionRandomCount < 10)
                nextPosition =  new Point((int) (Math.random() * 29), (int) (Math.random() * 39));
            scorpionRandomCount++;
            path = strategy.computePath(getPosition(), nextPosition,
                    p -> (world.canMove(p) && (world.backgroundType[p.getX()][p.getY()] == null || !world.backgroundType[p.getX()][p.getY()].equals("Mine"))),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
        }
        else if (path.size() > 0){
            directionOfTravel = getDirectionOfTravel(path.get(0));
            world.moveEntity(this, path.get(0));
            path.remove(path.get(0));


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
        boolean tailLeft = (getImageIndex() % 2) == 0;
        switch(this.directionOfTravel){
            case "right":
                if (tailLeft) newImageIndex = 1;
                else newImageIndex = 0;
                break;
            case "down":
                if (tailLeft) newImageIndex = 3;
                else newImageIndex = 2;
                break;
            case "left":
                if (tailLeft) newImageIndex = 5;
                else newImageIndex = 4;
                break;
            case "up":
                if (tailLeft) newImageIndex = 7;
                else newImageIndex = 6;
                break;
        }
        this.setImageIndex(newImageIndex);
        scheduler.scheduleEvent(this, this.createAnimationAction(1),
                this.getAnimationPeriod());
    }
}
