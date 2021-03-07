import processing.core.PImage;

import java.util.List;

public class Bird extends AnimatedEntity
{
    public Bird(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 2, 2);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), 250);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){

    }

    @Override
    void nextImage(String d) {

    }
}
