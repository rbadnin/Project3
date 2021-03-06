import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    // variables
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;


    // constructor
    public Entity(String id, Point position, List<PImage> images, int actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }




    // accessor methods
    protected int getActionPeriod(){
        return actionPeriod;
    }

    protected int getImageIndex() {
        return imageIndex;
    }

    protected void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    protected List<PImage> getImages() {
        return images;
    }

    protected Point getPosition(){
        return position;
    }

    protected void setPosition(Point position) {
        this.position = position;
    }

    protected String getId() {
        return id;
    }


    // methods
    public PImage getCurrentImage() {return images.get(imageIndex);
    }

}
