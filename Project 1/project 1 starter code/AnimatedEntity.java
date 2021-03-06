import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActionEntity{

    // variables
    private final int animationPeriod;


    // constructor
    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }


    // accessor methods
    protected int getAnimationPeriod()
    {
        return animationPeriod;
    }


    // methods
    abstract void nextImage(String a);

    public Animation createAnimationAction(int repeatCount){ return new Animation(this, repeatCount);}

}
