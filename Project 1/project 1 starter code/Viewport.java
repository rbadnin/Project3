/*
Viewport ideally helps control what part of the world we are looking at for drawing only what we see
Includes helpful helper functions to map between the viewport and the real world
 */


final class Viewport
{
   // variables
   private int row;
   private int col;
   private final int numRows;
   private final int numCols;

   // constructor
   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }


   // accessor methods
   public int getRow() {
      return row;
   }

   public int getCol() {
      return col;
   }

   public int getNumCols() {
      return numCols;
   }

   public int getNumRows() {
      return numRows;
   }


   // methods
   public Point viewportToWorld(int col, int row)
   {
      return new Point(col + this.col, row + this.row);
   }

   public Point worldToViewport(int col, int row)
   {
      return new Point(col - this.col, row - this.row);
   }

   public boolean contains(Point p)
   {
      return p.getY() >= row && p.getY() < row + numRows &&
              p.getX() >= col && p.getX() < col + numCols;
   }

   public void shift(int col, int row)
   {
      this.col = col;
      this.row = row;
   }
}
