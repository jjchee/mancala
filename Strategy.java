
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.RectangularShape;
/**
 * An implementation of Startegy interface
 * @author Brendon Yim, Bikram Singh, and Jason Chee
 * Professor Kim
 * CS151
 * 
 * Project solution
 * copyright 2015 
 * version 1
 */
public interface Strategy
{
   /**
    * returns shape of pit
    * @return shape of pit
    */
	public RectangularShape getPitShape();
	
	/**
	 * returns shape of mancala
	 * @return shape of mancala
	 */
	public RectangularShape getMancalaShape();
	
	/**
	 * returns shape of stone in correct position based on index of stone and 
	 * total number of stones belonging to pit or mancala.
	 * @param num index of stone
	 * @param total total number of stones in pit or mancala
	 * @param isMancala true if mancala false if pit
	 * @return shape of stone in correct x-y position
	 */
	public RectangularShape getStoneShape(int num, int total, boolean isMancala);
	
	/**
	 * returns color of the board
	 * @return color of the board
	 */
	public Color getBoardColor();
	
	/**
	 * returns color of the stone
	 * @return color of the stone
	 */
	public Color getStoneColor();
	
	/**
	 * returns color of the pit
	 * @return color of the pit
	 */
	public Color getPitColor();
	
	/**
	 * returns font object
	 * @return font object
	 */
   public Font getFont();
   
   /**
    * returns color of font
    * @return color of font
    */
   public Color getFontColor();
   
   /**
    * returns width of mancala
    * @return width of mancala
    */
   public int getManW();
   
   /**
    * returns height of mancala
    * @return height of mancala
    */
   public int getManH();
   
   /**
    * returns width of pit
    * @return width of pit
    */
   public int getPitW();
   
   /**
    * returns height of pit
    * @return height of pit
    */
   public int getPitH();
	
}
