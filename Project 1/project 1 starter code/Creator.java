import processing.core.PImage;

import java.util.List;

public class Creator {

    public static Atlantis createAtlantis(String id, Point position, List<PImage> images)
    {
        return new Atlantis(id, position, images);
    }


    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static MainCharacter createMainCharacter(String id, Point position, List<PImage> images)
    {
        return new MainCharacter(id, position, images);
    }







}
