

/**
 * The class containing the main method as required for the project.
 * @author Brendon Yim, Bikram Singh, and Jason Chee
 * Professor Kim
 * CS151
 * 
 * Project solution
 * copyright 2015 
 * version 1
 */
public class MancalaTest
{
   public static void main(String[] args)
   {
      Model model = new Model();
      BoardFrame frame = new BoardFrame(model);
      model.attach(frame);
      
   }

}
