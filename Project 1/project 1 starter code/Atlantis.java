import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Atlantis extends AnimatedEntity{

    // variables
    public static final String ATLANTIS_KEY = "atlantis";
    public static final int ATLANTIS_NUM_PROPERTIES = 4;
    public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
    public static final int ATLANTIS_ID = 1;
    public static final int ATLANTIS_COL = 2;
    public static final int ATLANTIS_ROW = 3;
    public static final int ATLANTIS_ANIMATION_PERIOD = 70;


    // constructor
    public Atlantis(String id, Point position, List<PImage> images){
        super(id, position, images, 0, 0);
    }


    // methods from ActionEntity
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createAnimationAction(ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    @Override
    void nextImage(String d) {

    }
}
