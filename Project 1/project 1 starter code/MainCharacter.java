import processing.core.PImage;

import java.util.List;

public class MainCharacter extends AnimatedEntity
{

    public MainCharacter(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 2, 2);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createAnimationAction(2),
                this.getAnimationPeriod());
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }
}
