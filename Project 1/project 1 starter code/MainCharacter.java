import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class MainCharacter extends AnimatedEntity
{

    public String directionOfTravel = "right";
    private EventScheduler scheduler;

    public MainCharacter(String id, Point position, List<PImage> images)
    {
        super(id, position, images, 1, 1);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
        this.scheduler = scheduler;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    public void nextImage(String directionOfTravel) {
        int newImageIndex = 0;
        boolean closeLegs = (this.getImageIndex() % 2) == 0;
        switch (this.directionOfTravel) {
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
