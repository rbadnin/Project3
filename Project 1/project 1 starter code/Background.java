import java.util.List;
import processing.core.PImage;

final class Background
{
   // variables
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final String BGND_KEY = "background";
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   // variables
   private String id;
   private final List<PImage> images;
   private int imageIndex;


   // constructor
   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }


   // methods
   public PImage getCurrentImage() {
      return images.get(imageIndex);
   }
}
