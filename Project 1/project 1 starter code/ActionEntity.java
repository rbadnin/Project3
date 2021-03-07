import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class ActionEntity extends Entity{

    //variables
    public static final Random rand = new Random();
    private static final int PROPERTY_KEY = 0;

    // contructor
    public ActionEntity(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images, actionPeriod);
    }


    // abstract methods
    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);


    // methods
    public Activity createActivityAction(WorldModel world, ImageStore imageStore){
        return new Activity(this, world, imageStore);
    }

}
