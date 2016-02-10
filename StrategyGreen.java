
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
/**
 * A concrete implementation of Strategy with green theme
 * @author Brendon Yim, Bikram Singh, and Jason Chee
 * Professor Kim
 * CS151
 * 
 * Project solution
 * copyright 2015 
 * version 1
 */
public class StrategyGreen implements Strategy
{
   final static int PIT_W = 100;
   final static int PIT_H = 100;
   final static int MAN_W = 100;
   final static int MAN_H = 200;
   final static int STONE_W = 10;
   final static int STONE_H = 10;
   final static int ARC_W = 40;
   final static int ARC_H = 40;
   
   /**
    * returns a round rectangle object
    * @return round rectangle object
    */
   public RectangularShape getPitShape()
   {
      return new RoundRectangle2D.Double(0, 0, PIT_W, PIT_H, ARC_W, ARC_H);
   }
   
   /**
    * returns shape of mancala which is an Ellipse2D
    * @return shape of mancala, Ellipse2D
    */
   public RectangularShape getMancalaShape()
   {
      return new Ellipse2D.Double(0, 0, MAN_W, MAN_H);
   }
   
   /**
    * returns shape of stone in correct position based on index of stone and 
    * total number of stones belonging to pit or mancala.
    * @param num index of stone
    * @param total total number of stones in pit or mancala
    * @param isMancala true if mancala false if pit
    * @return shape of stone in correct x-y position
    */
   public RectangularShape getStoneShape(int num, int total, boolean isMancala)
   {
      double x = 0;
      double y = 0;
      if (isMancala)
      {
         x += (double)MAN_W / 2 - (double)STONE_W / 2;
         y += (double)MAN_H / 2 - (double)STONE_H / 2;
      }
      else
      {
         x += (double)PIT_W / 2 - (double)STONE_W / 2;
         y += (double)PIT_H / 2 - (double)STONE_H / 2;
      }
      if (total != 3)
      {
         if (num == 0)
         {
            return new Rectangle2D.Double(x, y, STONE_W, STONE_H);
         }
         total--;
      }
      double degrees = 360 / total * (num + 1);
      //pythagorean theorem
      double diag = Math.sqrt(STONE_W * STONE_W + STONE_H * STONE_H);
      if (total >= 4)
      {
         //diag multiplier never less than 1
         diag = diag * Math.log(total) / Math.log(4);
      }
      //simple trig relationships to draw in a circle
      x +=  diag * Math.cos(Math.toRadians(degrees));
      y +=  diag * Math.sin(Math.toRadians(degrees));

      return new Rectangle2D.Double(x, y, STONE_W, STONE_H);
   }
   
   /**
    * returns color of the board
    * @return color of the board
    */
   public Color getBoardColor()
   {
      return new Color(50, 160, 50);
   }
   
   /**
    * returns color of the stone
    * @return color of the stone
    */
   public Color getStoneColor()
   {
      return new Color(160, 82, 45);
   }
   
   /**
    * returns color of the pit
    * @return color of the pit
    */
   public Color getPitColor()
   {
      return new Color(0, 100, 0);
   }
   
   /**
    * returns width of mancala
    * @return width of mancala
    */
   public int getManW()
   {
      return MAN_W;
   }
   
   /**
    * returns height of mancala
    * @return height of mancala
    */
   public int getManH()
   {
      return MAN_H;
   }
   
   /**
    * returns width of pit
    * @return width of pit
    */
   public int getPitW()
   {
      return PIT_W;
   }
   
   /**
    * returns height of pit
    * @return height of pit
    */
   public int getPitH()
   {
      return PIT_H;
   }
   
   /**
    * returns color of font
    * @return color of font
    */
   public Color getFontColor()
   {
      return Color.WHITE;
   }
   
   /**
    * returns font object
    * @return font object
    */
   public Font getFont()
   {
      return new Font("Arial", Font.BOLD, 20);
   }
}
