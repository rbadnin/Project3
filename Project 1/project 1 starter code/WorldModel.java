import processing.core.PImage;
import java.util.concurrent.TimeUnit;


import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{

   // variables
   private final int numRows;
   private final int numCols;
   public final Background[][] background;
   public final String[][] backgroundType;
   private final Entity[][] occupancy;
   private final Set<Entity> entities;
   public FieldManager manager;
   public long gameTime;


   private static final int PROPERTY_KEY = 0;


   // constructor
   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.gameTime = System.currentTimeMillis();
      this.numRows = numRows - 30;
      this.numCols = numCols-40;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }


      manager = new FieldManager(this.background, 55);
      this.backgroundType = manager.CreateField();


   }


   // accessor methods
   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   public Set<Entity> getEntities() {
      return entities;
   }

   public boolean canMove(Point point)
   {
      return point.getX() < 35 && point.getX() > 4
              && point.getY() < 25 && point.getY() >= 5;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public void removeEntity(Entity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }


   // private methods
   public Point findNextDirt(ImageStore imageStore)
   {
      Point nearest= new Point(0, 0);
      boolean foundDirt = false;
      while (!foundDirt)
      {
         nearest = new Point((int) (Math.random() * 29 + 1) + FieldManager.XOFFSET, (int) (Math.random() * 20) + FieldManager.XOFFSET);
         if (!Optional.of(this.background[nearest.getY()][nearest.getX()].getCurrentImage())
                 .equals(Optional.of(imageStore.getImageList("grass").get(0))) &&
                 !Optional.of(this.background[nearest.getY()][nearest.getX()].getCurrentImage())
                         .equals(Optional.of(imageStore.getImageList("flag").get(0))))
         {
            foundDirt = true;
         }
      }
      return nearest;
   }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < 30 &&
              pos.getX() >= 0 && pos.getX() < 40;
   }

   private Entity getOccupancyCell(Point pos)
   {
      return occupancy[pos.getY()][pos.getX()];
   }

   private void setOccupancyCell(Point pos, Entity entity)
   {
      occupancy[pos.getY()][pos.getX()] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return background[pos.getY()][pos.getX()];
   }

   private void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   private void setBackground(Point pos, Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public void tryAddEntity(Entity character)
   {
      if (isOccupied(character.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(character);
   }


   private boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case Background.BGND_KEY:
               return parseBackground(properties, imageStore);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Background.BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
                 Integer.parseInt(properties[Background.BGND_ROW]));
         String id = properties[Background.BGND_ID];
         setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }
      return properties.length == Background.BGND_NUM_PROPERTIES;
   }

//   public boolean parseMainChactacter(String [] properties, ImageStore imageStore)
//   {
//      if (properties.length == Octo.OCTO_NUM_PROPERTIES)
//      {
//         Point pt = new Point(Integer.parseInt(properties[Octo.OCTO_COL]),
//                 Integer.parseInt(properties[Octo.OCTO_ROW]));
//         Entity character = Creator.createMainCharacter(properties[Octo.OCTO_ID],
//                 pt, imageStore.getImageList(Octo.OCTO_KEY));
//         tryAddEntity(character);
//      }
//
//      return properties.length == Octo.OCTO_NUM_PROPERTIES;
//   }






}
