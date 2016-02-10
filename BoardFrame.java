
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;

/**
 * A frame component that contains the view and controller components 
 * of the mancala game
 * @author .Brendon Yim, Bikram Singh, and Jason Chee
 * Professor Kim
 * CS151
 * 
 * Project solution
 * copyright 2015 
 * version 1
 *
 */
@SuppressWarnings("serial")


/**
 * The view and frame that shows the mancala game visually
 */
public class BoardFrame extends JFrame implements ChangeListener
{
   private Model model;
   private int[] data;
   private Strategy s;
   JPanel boardPanel;
   final JButton undoBtn;
   boolean gameOver;

   /**
    * Constructor that takes model as a parameter
    * @param model
    */
   public BoardFrame(Model model)
   {
      this.model = model;
      data = model.getData();
      gameOver = false;
      initScreen();
      setLayout(new BorderLayout());
      setLocation(300, 200);

      // undoBtn is a controller that resets the turn and last state of board 
      undoBtn = new JButton("Undo : "+ this.model.getUndoCounter());
      undoBtn.setPreferredSize(new Dimension(80, 50));
      
      undoBtn.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
        	 if(BoardFrame.this.model.getUndoCounter() != 0)
        	 {
        	    BoardFrame.this.model.undo();
	        	 undoBtn.setText("Undo : "+ BoardFrame.this.model.getUndoCounter());
        	 }else;
         }

      });
      JPanel undoPanel = new JPanel();
      undoPanel.add(undoBtn);
      // end of undoBtn code

      add(boardPanel, BorderLayout.CENTER);
      add(undoPanel, BorderLayout.SOUTH);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setResizable(false);
      setVisible(true);
   }

   /**
    * Is called from the model when data is changed, updates members and view
    * of this class. Checks if the game is over.
    * @param e ChangeEvent object
    */
   public void stateChanged(ChangeEvent e)
   {
	
	   // update the view: accessors and repaint
	   data = model.getData();
	   repaint();
	   if(model.isFinish() && !gameOver)
	   {
		   gameOver = true;
			JFrame frame = new JFrame("Declare Winner");
         if (model.declareWinner() == 1)
            JOptionPane.showMessageDialog(frame, "A is the winner!!!");
         else if (model.declareWinner() == 2)
            JOptionPane.showMessageDialog(frame, "B is the winner!!!");
         else
            JOptionPane.showMessageDialog(frame, "It is a tie!!!");
				  
	   }
   }

   /**
    * Initial Option Panes that lets user select style and number of stones
    */
   public void initScreen()
   {
      Object[] options = { "Style A", "Style B" };
      int input = JOptionPane.showOptionDialog(null, "Choose a Board Style:", "Board Styles", JOptionPane.DEFAULT_OPTION,
            JOptionPane.DEFAULT_OPTION, null, options, options[0]);
      if (input == 0)
      {
         boardPanel = boardContextDoWork(new StrategyGreen());
      } else if (input == 1)
      {
         boardPanel = boardContextDoWork(new StrategyBrown());// change later
      } else
      {
         System.exit(0);
      }
      Object[] optionStones = { "3", "4" };
      int inputStones = JOptionPane.showOptionDialog(null, "Choose Number of Stones:", "Initial Stones", 
    		  JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null,
            optionStones, optionStones[0]);

      if (inputStones == 0)
      {
         model.refreshBoard(3);
      } else if (inputStones == 1)
      {
         model.refreshBoard(4);
      } else
      {
         System.exit(0);
      }
   }

   /**
    * Returns a JPanel that represents the mancala board using strategy 
    * pattern to insert style.
    * @param strat concrete strategy
    * @return JPanel containing both users' pits as controllers
    */
   public JPanel boardContextDoWork(Strategy strat)
   {
      this.s = strat;
      Color boardColor = s.getBoardColor();
      Color fontColor = s.getFontColor();
      Font font = s.getFont();
      JPanel panCenter = new JPanel();
      JPanel panLeft = new JPanel();
      JPanel panRight = new JPanel();

      panCenter.setLayout(new GridLayout(2, 6, 10, 10));
      // B6 to B1 Controllers
      for (int i = 12; i > 6; i--)
      {
         final Pits temp = new Pits(i);
         final int pit = i;
         final JLabel tempLabel = new JLabel(temp);
         tempLabel.addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent e)
            {
               if (Model.player == 1)
               {
                  JFrame frame = new JFrame();
                  JOptionPane.showMessageDialog(frame, "Player A's turn!");
               } else if (model.data[pit] == 0)
               {
                  JFrame frame = new JFrame();
                  JOptionPane.showMessageDialog(frame, "Pit is Empty try another one.");
               } else
               {
                  if (temp.pitShape.contains(e.getPoint()))
                  {
                     model.move(pit); //mutator
                     undoBtn.setText("Undo : "+model.getUndoCounter());
                     model.display();
                  }
               }
            }
         });
         JPanel tempPanel = new JPanel(new BorderLayout());

         JTextPane textPane = new JTextPane();
         textPane.setEditable(false);
         textPane.setBackground(boardColor);
         textPane.setForeground(fontColor);
         textPane.setFont(font);
         textPane.setText("B" + (i - 6));
         StyledDocument doc = textPane.getStyledDocument();
         SimpleAttributeSet center = new SimpleAttributeSet();
         StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
         doc.setParagraphAttributes(0, doc.getLength(), center, false);
         tempPanel.add(textPane, BorderLayout.NORTH);
         tempPanel.add(tempLabel, BorderLayout.SOUTH);
         panCenter.add(tempPanel, BorderLayout.SOUTH);

         tempPanel.setBackground(boardColor);
      }
      //A1 to A6 Controllers
      for (int i = 0; i < 6; i++)
      {
         final Pits newPits = new Pits(i);
         JLabel label = new JLabel(newPits);
         final int pit = i;
         label.addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent e)
            {
               if (Model.player == 2)
               {
                  JFrame frame = new JFrame();
                  JOptionPane.showMessageDialog(frame, "Player B's turn!");
               } else if (model.data[pit] == 0)
               {
                  JFrame frame = new JFrame();
                  JOptionPane.showMessageDialog(frame, "Pit is Empty try another one.");
               } else
               {
                  if (newPits.pitShape.contains(e.getPoint()))
                  {
                     model.move(pit); //mutator
                     undoBtn.setText("Undo : "+model.getUndoCounter());
                     model.display();
                  }
               }
            }
         });
         JPanel tempPanel = new JPanel(new BorderLayout());

         tempPanel.add(label, BorderLayout.NORTH);
         JTextPane textPane = new JTextPane();
         textPane.setBackground(boardColor);
         textPane.setForeground(fontColor);
         textPane.setFont(font);
         textPane.setEditable(false);
         textPane.setText("A" + (i + 1));
         StyledDocument doc = textPane.getStyledDocument();
         SimpleAttributeSet center = new SimpleAttributeSet();
         StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
         doc.setParagraphAttributes(0, doc.getLength(), center, false);
         tempPanel.add(textPane, BorderLayout.SOUTH);
         tempPanel.setBackground(boardColor);
         panCenter.add(tempPanel, BorderLayout.SOUTH);
      }

      //left text pane
      JTextPane paneLeft = new JTextPane();
      paneLeft.setBackground(boardColor);
      paneLeft.setForeground(fontColor);
      paneLeft.setFont(font);
      paneLeft.setEditable(false);
      paneLeft.setText("M\nA\nN\nC\nA\nL\nA\n \nB");
      
      //right text pane
      JTextPane paneRight = new JTextPane();
      paneRight.setBackground(boardColor);
      paneRight.setForeground(fontColor);
      paneRight.setFont(font);
      paneRight.setEditable(false);
      paneRight.setText("M\nA\nN\nC\nA\nL\nA\n \nA");

      //Add text panes to left and right panels
      panLeft.setLayout(new BorderLayout());
      panRight.setLayout(new BorderLayout());
      panLeft.add(paneLeft, BorderLayout.WEST);
      panRight.add(paneRight, BorderLayout.EAST);
      panLeft.add(new JLabel(new Pits(13)), BorderLayout.EAST);
      panRight.add(new JLabel(new Pits(6)), BorderLayout.WEST);

      //add the 2 mancala panels and pit panel to larger displayPanel
      JPanel displayPanel = new JPanel();
      displayPanel.add(panLeft, BorderLayout.WEST);
      displayPanel.add(panCenter, BorderLayout.CENTER);
      displayPanel.add(panRight, BorderLayout.EAST);
      
      //set color
      panCenter.setBackground(boardColor);
      panLeft.setBackground(boardColor);
      panRight.setBackground(boardColor);
      displayPanel.setBackground(boardColor);
      
      // return display panel which contains the containers and elements created
      return displayPanel;
   }

   /**
    * Represents a pit object, knows if a mouse click is contained within the
    * pit shape. Painted with stones inside. 
    * Is called from the doWork() method, so utilizes the concrete strategy.
    * @author jasonchee
    *
    */
   private class Pits implements Icon
   {
      Shape pitShape;
      // Shape stoneShape = s.getStoneShape();
      int pitNum = 0;

      /**
       * constructor for pit Icon
       * @param pitNum index in data array
       */
      public Pits(int pitNum)
      {
         this.pitNum = pitNum;
      }

      /**
       * Checks if mouse click is inside the controller
       * @param point mouse location
       * @return does the pit shape contain mouse location
       */
      public boolean contains(Point2D point)
      {
         return pitShape.contains(point);
         // somehow magically make this work for mouse listener..
      }

      /**
       * returns the height of the icon
       * @return height of icon
       */
      public int getIconHeight()
      {
         if (pitNum == 6 || pitNum == 13)
         {
            return s.getManH();
         } else
         {
            return s.getPitH();
         }
      }

      /**
       * returns width of icon
       * @return width of icon
       */
      public int getIconWidth()
      {
         if (pitNum == 6 || pitNum == 13)
         {
            return s.getManW();
         } else
         {
            return s.getPitW();
         }
      }

      /**
       * icon is painted with correct number of stones in the pit/mancala
       * @param c properties for painting
       * @param g graphics object
       * @param x x location of icon
       * @param y y location of icon
       */
      public void paintIcon(Component c, Graphics g, int x, int y)
      {
         int iterateNum = data[pitNum];
         if (iterateNum > 5)
         {
            iterateNum = 5;
         }
         Graphics2D g2 = (Graphics2D) g;
         if (pitNum == 6 || pitNum == 13)
         {
            pitShape = s.getMancalaShape();
            g2.setColor(s.getPitColor());
            g2.fill(pitShape);
            for (int i = 0; i < data[pitNum]; i++)
            {
               RectangularShape temp = s.getStoneShape(i, data[pitNum], true);
               g2.setColor(s.getStoneColor());
               g2.draw(temp);
               g2.fill(temp);
            }
         } else
         {
            pitShape = s.getPitShape();
            g2.setColor(s.getPitColor());
            g2.fill(pitShape);
            for (int i = 0; i < data[pitNum]; i++)
            {
               RectangularShape temp = s.getStoneShape(i, data[pitNum], false);
               g2.setColor(s.getStoneColor());
               g2.draw(temp);
               g2.fill(temp);
            }
         }
      }
   }
}
