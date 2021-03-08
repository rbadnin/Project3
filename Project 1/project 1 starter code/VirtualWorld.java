import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
        extends PApplet
{
    // variables
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 960;
    private static final int VIEW_HEIGHT = 720;
    private static final int TILE_WIDTH = 24;
    private static final int TILE_HEIGHT = 24;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    public MainCharacter character;
    public DirtFiller dirtFiller;
    public Bird bird;
    public Scorpion scorpion;
    public String page = "start";



    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private PImage StartScreenBackground;

    private long next_time;


    // methods
    public void settings()
    {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }


    /*
       Processing entry point for "sketch" setup.
    */
    public void setup()
    {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
                TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        this.character = EntityFactory.createMainCharacter("start", new Point(20,15), imageStore.getImageList("mainCharacter"));
        this.dirtFiller = EntityFactory.createDirtFiller("dirtFiller", new Point(39,29), imageStore.getImageList("dirtFiller"));
        this.bird = EntityFactory.createBird("bird", new Point(0,0), imageStore.getImageList("bird"));
        this.scorpion = EntityFactory.createScorpion("scorpion", new Point(1,0), imageStore.getImageList("scorpion"));




        //scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;

        world.tryAddEntity(character);
        world.tryAddEntity(dirtFiller);
        world.tryAddEntity(bird);
    }


    public void draw()
    {
        if(page.equals("start"))
        {
            clear();
            PImage img = loadImage("C:\\Users\\jbrun\\Intellij Projects\\Project3\\Project 1\\project 1 starter code\\images\\StartScreen.jpg");
            background(img);
        }

        if(page.equals("main"))
        {
            if (scorpion.scorpionRandomCount == 10) {
                page = "end";
            }

            long time = System.currentTimeMillis();
            if (time >= next_time) {
                try {
                    this.scheduler.updateOnTime(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                next_time = time + TIMER_ACTION_PERIOD;
            }
            view.drawViewport();
        }

        if(page.equals("end"))
        {
            PImage img = loadImage("C:\\Users\\jbrun\\Intellij Projects\\Project3\\Project 1\\project 1 starter code\\images\\EndScreen.png");
            background(img);
        }

        if(page.equals("win"))
        {
            PImage img = loadImage("C:\\Users\\jbrun\\Intellij Projects\\Project3\\Project 1\\project 1 starter code\\images\\WinScreen.bmp");
            background(img);
        }
    }



    public void keyPressed()
    {
        if (key == ' ')
        {
            if (page.equals("start"))
            {
                page = "main";
                dirtFiller.scheduleActions(scheduler, world, imageStore);
                character.scheduleActions(scheduler, world, imageStore);
                bird.scheduleActions(scheduler,world,imageStore);
            }
            else if (page.equals("main"))
            {
                if (world.backgroundType[character.getPosition().getX()][character.getPosition().getY()].equals("Mine")) {
                    if (world.manager.grassCount >= 600 - world.manager.flagCount - 3)
                        page = "end";
                    character.directionOfTravel = "crouch";
                    scorpion.setPosition(new Point(character.getPosition().getX(), character.getPosition().getY()));
                    scorpion.startingPos = new Point(character.getPosition().getX(), character.getPosition().getY());
                    scorpion.scheduleActions(scheduler, world, imageStore);
                    scheduler.unscheduleAllEvents(character);
                    scheduler.scheduleEvent(this.character, new Activity(this.character, world, imageStore), 100);
                    scheduler.unscheduleAllEvents(bird);
                    scheduler.unscheduleAllEvents(dirtFiller);
                }
                if((!world.backgroundType[character.getPosition().getX()][character.getPosition().getY()].equals("Mine")
                    && !world.backgroundType[character.getPosition().getX()][character.getPosition().getY()].equals("searched")
                        && !world.backgroundType[character.getPosition().getX()][character.getPosition().getY()].equals("flag"))) {
                    character.directionOfTravel = "crouch";
                    if (Integer.parseInt(world.backgroundType[character.getPosition().getX()][character.getPosition().getY()]) > 0)
                        world.manager.reveal(character.getPosition().getX(), character.getPosition().getY(), imageStore, world, false);
                    else
                        world.manager.reveal(character.getPosition().getX(), character.getPosition().getY(), imageStore, world, true);
                    world.manager.mineField = world.manager.caluclateNeighborValues();

                    if (world.manager.grassCount <= 0 && world.manager.flaggedSpotsRemaining <= 0) {
                        world.background[character.getPosition().getY()][character.getPosition().getX()] = new Background("grass", imageStore.getImageList("grass"));
                        page = "win";
                    }
                }


            }
            else if(page.equals("end") || page.equals("win"))
            {
                setup();
                page = "main";
                dirtFiller.scheduleActions(scheduler, world, imageStore);
                character.scheduleActions(scheduler, world, imageStore);
                bird.scheduleActions(scheduler, world, imageStore);
            }
        }
        if (key == CODED) {

            switch (keyCode) {

                case UP:
                    if (world.canMove(new Point(character.getPosition().getX(), character.getPosition().getY() - 1))) {
                        if (!world.isOccupied(new Point(character.getPosition().getX(), character.getPosition().getY() - 1)))
                            world.moveEntity(character, new Point(character.getPosition().getX(), character.getPosition().getY() - 1));
                        character.directionOfTravel = "up";
                    }
                        break;
                case DOWN:
                    if (world.canMove(new Point(character.getPosition().getX(), character.getPosition().getY() + 1))) {
                        if (!world.isOccupied(new Point(character.getPosition().getX(), character.getPosition().getY() + 1)))
                        world.moveEntity(character, new Point(character.getPosition().getX(), character.getPosition().getY() + 1));
                        character.directionOfTravel = "down";
                    }
                        break;
                case LEFT:
                    if (world.canMove(new Point(character.getPosition().getX() - 1, character.getPosition().getY()))) {
                        if (!world.isOccupied(new Point(character.getPosition().getX()-1, character.getPosition().getY())))
                            world.moveEntity(character, new Point(character.getPosition().getX() - 1, character.getPosition().getY()));
                        character.directionOfTravel = "left";
                    }
                        break;
                case RIGHT:
                    if (world.canMove(new Point(character.getPosition().getX() + 1, character.getPosition().getY()))) {
                        if (!world.isOccupied(new Point(character.getPosition().getX()+1, character.getPosition().getY())))
                            world.moveEntity(character, new Point(character.getPosition().getX() + 1, character.getPosition().getY()));
                        character.directionOfTravel = "right";
                    }
                    break;
                }

        }
        if (key == 's')
        {
            dirtFiller.shoutCount++;
        }

        if (key == '`')
        {
            page = "end";
        }

        if (key == 'f') {
            if (Optional.of(world.background[character.getPosition().getY()][character.getPosition().getX()].getCurrentImage())
                    .equals(Optional.of(imageStore.getImageList("grass").get(0)))) {
                world.background[character.getPosition().getY()][character.getPosition().getX()] = new Background("flag", imageStore.getImageList("flag"));
                world.manager.flaggedSpotsRemaining -=1;
            } else if (Optional.of(world.background[character.getPosition().getY()][character.getPosition().getX()].getCurrentImage())
                    .equals(Optional.of(imageStore.getImageList("flag").get(0)))) {
                world.background[character.getPosition().getY()][character.getPosition().getX()] = new Background("grass", imageStore.getImageList("grass"));
                world.manager.flaggedSpotsRemaining +=1;
            }
        }
    }

    private static Background createDefaultBackground(ImageStore imageStore)
    {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color)
    {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++)
        {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore, PApplet screen)
    {
        try
        {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(WorldModel world, String filename, ImageStore imageStore)
    {
        try
        {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
    }

//    private static void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
//    {
//        for (MainCharacter entity : world.getEntities())
//        {
//            //Only start actions for entities that include action (not those with just animations)
//            if (entity.getActionPeriod() > 0) {
//                ActionEntity e = (ActionEntity) entity;
//                e.scheduleActions(scheduler, world, imageStore);
//            }
//        }
//    }

    private static void parseCommandLine(String [] args)
    {
        for (String arg : args)
        {
            switch (arg)
            {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    // main method
    public static void main(String [] args)
    {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
