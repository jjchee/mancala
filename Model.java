
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An object modeling a game of mancala.
 * Contains a collection of changelisteners, follows MVC pattern
 * @author: Brendon Yim, Bikram Singh, and Jason Chee
 * Professor Kim
 * CS151
 * 
 * Project solution
 * copyright 2015 
 * version 1
 *
 */

public class Model
{
   int[] data;
   private int[] undoData;
   private boolean finish;
   public ArrayList<ChangeListener> listeners;
   static int player = 1;
   int previousPlayer = 2;
   boolean undoAllowed ;
   int[] undoTurns;
   boolean freeTurn;
   int undoCounter;

   /**
    * Constructor for a Model object
    */
   public Model()
   {
      listeners = new ArrayList<ChangeListener>(); //empty ArrayList
      data = new int[14]; // initialize to 14, 12 pits and 2 Mancala
      undoData = new int[14]; // Auxiliary data structure to save previous state of the game 
      finish = false; // Indicates when the game is over
      undoAllowed = false; // indicates if player has any undo moves left
      undoTurns = new int[]{3, 3, 3}; // intialized to 3, for player is allowed 3 undo moves in one turn
      freeTurn = false; // indicates if a player has any free turns after he is done with his move
      undoCounter = 3; // Auxiliary counter keeps the track of undo moves
   }
   
   /**
    * Method undo lets the player go to previous state of the game up to 3 times
    * per turn and not consecutively
    */
   public void undo()
   { 
	   if(freeTurn)
	   {
		   player++;
		   player = ((player % 2) != 0) ? 1 : 2 ;
		   freeTurn = false;
	   }
	   if(undoTurns[player] != 0 && undoAllowed)
	   {
		   data = Arrays.copyOf(undoData, undoData.length);
		   undoTurns[player]--;
		   undoCounter = undoTurns[player];
		   undoTurns[previousPlayer] = 3;
		   player = previousPlayer;
		   player = ((player % 2) != 0) ? 1 : 2 ;
		   
		   undoAllowed = false;
		   notifyListeners();
	   }
   }

   /**
    * Method refreshBoard takes number of stones to initialize the board
    * 
    * @param stones
    *           of starting stones per pit
    */
   public void refreshBoard(int stones)
   {
      for (int i = 0; i < data.length - 1; i++)
      {
         data[i] = stones;
         if (i == 6)
            data[i] = 0;
      }
      notifyListeners();
   }
   
   /**
    * accessor method getUndoCounter returns the value of undoCounter
    * 
    * @return value of undoCounter
    */
	public int getUndoCounter()
	{
		return undoCounter;
	}

	/**Method getFinish returns the value of instance variable finish
	 * @return true if the game is finish
	 */
	public boolean getFinish()
	{
		return finish;
	}// end getFinish 
   
	/**accessor method getData enables access to instance variable data
	 * @return data
	 */
	public int[] getData()
	{
		return data;
	}// end getData

	
	/**method attach is part of MVC which attaches listeners to the Model
	 * @param c takes change listener as parameter
	 */
	public void attach(ChangeListener c)
	{
		listeners.add(c);
	}// end attach

   
	/** Method notifyListeners is the notify part of the MVC
	 * It alerts all the listeners that state of model has been changed. 
	 */
	public void notifyListeners()
	{
		for (ChangeListener l : listeners)
		{
			l.stateChanged(new ChangeEvent(this));
		}
	}// end notifyListeners
   
   /**
    * Method move is mutator which listens to pits clicked and mutates the model
    * 
    * @param startPitNum is the value captured by clicking a pit
    */
   public void move(int startPitNum)
   {
      undoAllowed = true;
      freeTurn = false;
      if (previousPlayer != player)
      {
         undoCounter = 3;
      }
      undoData = Arrays.copyOf(data, data.length);
      int startIndex = 0;
      if ((player == 1) && (startIndex > 6))
         startIndex = 6;
      if ((player == 2) && (startIndex < 6))
         startIndex = 13;

      startIndex = startPitNum;
      int pitStones = data[startIndex];
      System.out.println("\nstartIndex : " + startIndex + " pitStones : "
            + pitStones);
      int endIndex = (startIndex + data[startIndex]) % (data.length);
      int endIndexData = data[endIndex];

      data[startIndex] = 0;
      for (int i = startIndex + 1; i <= pitStones + startIndex; i++)
      {
         int pitNum = i;
         if (player == 1 && pitNum == 13)
            pitNum++;
         if (player == 2 && pitNum == 6)
            pitNum++;
         if (pitNum >= data.length)
            pitNum = pitNum % (data.length);

         data[pitNum]++;
      }
      System.out.println("CurrentPlayer : " + player + " EndIndex : "
            + endIndex);

      // If the last stone you drop is your own Mancala, you get a free turn .
      if ((endIndex == 6 && player == 1) || (endIndex == 13 && player == 2))
      {
         System.out.println("You got another Turn!");
         // player = player + 1;
         undoTurns[player] = 3;
         freeTurn = true;
      }

      // If the last stone you drop is in an empty pit on your side,
      // you get to take that stone and all of your opponents stones that are in
      // the opposite pit.
      // Place all captured stones in your own Mancala.
      if (endIndex != 13)
      {
         if ((endIndexData == 0 && endIndex < 6) && player == 1)
         {
            if (data[12 - endIndex] != 0)// if opponent's across pit is empty
            {
               int mancalaStones = 1 + data[12 - endIndex];
               data[6] = data[6] + mancalaStones;
               data[endIndex] = 0;
               data[12 - endIndex] = 0;
            }
            undoTurns[player] = 3;
         }
         if ((endIndexData == 0 && endIndex > 6) && player == 2)
         {
            if (data[12 - endIndex] != 0)
            {
               int mancalaStones = 1 + data[12 - endIndex];
               data[13] = data[13] + mancalaStones;
               data[endIndex] = 0;
               data[12 - endIndex] = 0;
            }
            undoTurns[player] = 3;
         }
      }// harvesting other player's stones

      if (isFinish())
      {
         System.out.println("Game Over!");
         declareWinner();
      }

      previousPlayer = player;

      if (freeTurn)
      {
         // System.out.println("CurrentPlayer updated for free turn.");
         player++;
      }
      player++;
      player = ((player % 2) != 0) ? 1 : 2;
      notifyListeners();
   }// end move

   
/** Method isFinish check if the game is finished
 * @return true if the game is finish
 */
   public boolean isFinish() 
   {
      boolean result = false;
      int player1Pits = 0;
      int player2Pits = 0;
      for (int i = 0; i < data.length - 1; i++)
      {
         if (i < 6)
            player1Pits += data[i];

         if (i > 6)
            player2Pits += data[i];

      }

      if (player1Pits == 0 || player2Pits == 0)
      {
         data[6] = data[6] + player1Pits;
         data[13] = data[13] + player2Pits;
         for (int i = 0; i < data.length - 1; i++)
         {
            if (i == 6)
               ;
            else
               data[i] = 0;
         }
         result = true;
      }
      return result;
   }

/** Method declareWinner declare the winner after the game is finish
 * @return 1 if player 1 wins, 2 if player 2 wins, 3 if tie
 */
   public int declareWinner()
   {

      if (data[6] > data[13])
         System.out.println("Player A is the Winner!");
      else
         System.out.println("Player B is the Winner!");
      System.out.println("Player A got : " + data[6] + " stones");
      System.out.println("Player B got : " + data[13] + " stones");

      // return (data[6] > data[13]) ? 1 : 2;
      // add tie condition
      if (data[6] > data[13])
      {
         return 1;
      }
      if (data[6] < data[13])
      {
         return 2;
      }
      return 3;

   }

/**Method display the state of board in console for testing puposes. 
 * 
 */
   public void display()
   {
      System.out.println("");
      for (int i = 0; i < data.length; i++)
         System.out.printf("%02d|", data[i]);
      System.out.println("");
      for (int i = 0; i < data.length; i++)
         System.out.printf("%02d|", i);
      
      System.out.println("");
   }
}
