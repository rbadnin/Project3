import processing.core.PImage;

import java.util.List;

public abstract class EatingEntity extends AnimatedEntity{

    // constructor
    public EatingEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    // abstract methods
    public abstract Point nextPosition(WorldModel world, Point destPos);

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
