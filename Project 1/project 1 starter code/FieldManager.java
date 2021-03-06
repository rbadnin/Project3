/*
    //      This class is dedicated to keeping track of the current images in the background
    // along with the layout of the mine field
 */

import java.awt.font.ImageGraphicAttribute;

public class FieldManager {
    public static final int XOFFSET = 5; //Distance field starts from game border (X axis)
    public static final int YOFFSET = 5; //Distance field starts from game border (Y axis)
    private String[][] mineField;
    private int flagCount;

    private int placedFlagCount = 0;
    private int grassCount = 0;

    public FieldManager(Background[][] background, int flagCount) {
        mineField = new String[background[0].length / 2][background.length / 2];
        this.flagCount = flagCount;
    }

    //Method to update
    public String[][] CreateField() {
        for (int i = 0; i < flagCount; i++) {
            mineField[(int) (Math.random() * 29 + 1) + XOFFSET][(int) (Math.random() * 20) + XOFFSET] = "Mine";
        }
        this.caluclateNeighborValues();
        return mineField;
    }


    private String[][] caluclateNeighborValues() {
        String[][] field = mineField;
        int count;
        for (int i = 0; i < mineField.length; i++) {
            for (int k = 0; k < mineField[0].length; k++) {
                count = calculateSingleNeighbor(i, k);
                if((k - FieldManager.XOFFSET + 1 > 0 && k - FieldManager.XOFFSET < 20) &&
                        ((i - FieldManager.YOFFSET + 1 > 0 && i - FieldManager.YOFFSET < 30))) {
                    if (mineField[i][k] == null)
                        mineField[i][k] = "" + count;
                }
            }
        }
        return field;
    }

    private int calculateSingleNeighbor(int row, int col) {
        int count = 0;
        int rowStart  = Math.max( row - 1, 0 + YOFFSET);
        int rowFinish = Math.min( row + 1, mineField.length - 1 );
        int colStart  = Math.max( col - 1, 0 + XOFFSET   );
        int colFinish = Math.min( col + 1, mineField[0].length - 1 );


        for ( int curRow = rowStart; curRow <= rowFinish; curRow++ ) {
            for ( int curCol = colStart; curCol <= colFinish; curCol++ ) {
                if (mineField[curRow][curCol] != null && mineField[curRow][curCol].equals("Mine"))
                        count++;
            }
        }
        return count;
    }

    public void reveal(int row, int col, ImageStore imageStore, WorldModel world, boolean cameFromZero) {
        // Change the initial tile to correct texture

        if((col - FieldManager.XOFFSET + 1 > 0 && col - FieldManager.XOFFSET < 20) &&
                ((row - FieldManager.YOFFSET + 1 > 0 && row - FieldManager.YOFFSET < 30)))
        {
            if (world.backgroundType[row][col] != null && !world.backgroundType[row][col].equals("Mine")) {
                changeTileBackground(row, col, imageStore, world);
                mineField[row][col] = "searched";
            }

            if (cameFromZero) {
                int rowStart = Math.max(row - 1, 0 + YOFFSET);
                int rowFinish = Math.min(row + 1, mineField.length - 1);
                int colStart = Math.max(col - 1, 0 + XOFFSET);
                int colFinish = Math.min(col + 1, mineField[0].length - 1);

                for (int curRow = rowStart; curRow <= rowFinish; curRow += 1) {
                    for (int curCol = colStart; curCol <= colFinish; curCol += 1) {
                        if (mineField[curRow][curCol] != null && !mineField[curRow][curCol].equals("searched") && !mineField[curRow][curCol].equals("Mine")) {
                            int tileValue = Integer.parseInt(mineField[curRow][curCol]);
                            if (tileValue == 0)
                                reveal(curRow, curCol, imageStore, world, true);
                            else {
                                if (cameFromZero)
                                    reveal(curRow, curCol, imageStore, world, false);
                            }

                        }

                    }
                }
            }
        }

    }

    private void changeTileBackground(int row, int col, ImageStore imageStore, WorldModel world)
    {
        if (world.backgroundType[row][col] != null && !world.backgroundType[row][col].equals("Mine"))
        {
            int number = Integer.parseInt(world.backgroundType[row][col]);
            System.out.println(number);
            if(number == 0)
               world.background[col][row] = new Background("dirt", imageStore.getImageList("dirt"));
            else if (number == 1)
                world.background[col][row] = new Background("number1", imageStore.getImageList("number1"));
            else if (number == 2)
                world.background[col][row] = new Background("number2", imageStore.getImageList("number2"));
            else if (number == 3)
                world.background[col][row] = new Background("number3", imageStore.getImageList("number3"));
            else if (number == 4)
                world.background[col][row] = new Background("number4", imageStore.getImageList("number4"));
            else if (number == 5)
                world.background[col][row] = new Background("number5", imageStore.getImageList("number5"));
            else if (number == 6)
                world.background[col][row] = new Background("number6", imageStore.getImageList("number6"));
            else if (number == 7)
                world.background[col][row] = new Background("number7", imageStore.getImageList("number7"));
            else if (number == 8)
                world.background[col][row] = new Background("number8", imageStore.getImageList("number8"));
            else
                world.background[col][row] = new Background("dirt", imageStore.getImageList("dirt"));
        }
    }
}
