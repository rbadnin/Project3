import processing.core.PImage;

import java.util.List;

public class Creator {

    public static Atlantis createAtlantis(String id, Point position, List<PImage> images)
    {
        return new Atlantis(id, position, images);
    }

    public static OctoFull createOctoFull(String id, int resourceLimit, Point position, int actionPeriod,
                                  int animationPeriod, List<PImage> images)
    {
        return new OctoFull(id, position, images,
                actionPeriod, animationPeriod, resourceLimit, 0);
    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit, Point position, int actionPeriod,
                                           int animationPeriod, List<PImage> images)
    {
        return new OctoNotFull(id, position, images,
                actionPeriod, animationPeriod, resourceLimit, 0);
    }

    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static Fish createFish(String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Fish(id, position, images, actionPeriod);
    }

    public static Sgrass createSgrass(String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Sgrass(id, position, images, actionPeriod);
    }

    public static Crab createCrab(String id, Point position, int actionPeriod, int animationPeriod,
                              List<PImage> images)
    {
        return new Crab(id, position, images, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(position, images);
    }



}
