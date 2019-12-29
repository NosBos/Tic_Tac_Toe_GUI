/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_tac_toe;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
        
import java.io.*;
        
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author Harjap Gill
 */
public class Tic_Tac_Toe implements ActionListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Tic_Tac_Toe gui = new Tic_Tac_Toe(); 
    }
        /**
         * A class which allows 2 players to play a game of Tic Tac Toe through a 
         * visual JFrame GUI. Other features such as keeping score and music can 
         * be accessed through the options drop down
         * 
         * @author Harjap Gill
         * @version Nov 22, 2019
         */
        
       
           public static final String PLAYER_X = "X"; // player using "X"
           public static final String PLAYER_O = "O"; // player using "O"
           public static final String EMPTY = " ";  // empty cell
           public static final String TIE = "T"; // game ended in a tie
           
           protected String startingPlayer; //current player who should have first turn
         
           protected String player;   // current player (PLAYER_X or PLAYER_O)
        
           protected String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress
        
           protected int numFreeSquares; // number of squares still free
           
           protected String board[][]; // 3x3 array representing the board
           protected JButton boardGUI[][]; //3x3 array of JButtons for GUI
           
           int x_wins; // number of X wins
           int o_wins; // number of O wins
           int ties; // number of ties
            
           private JLabel status; // text area to print game status
           
           private JLabel score; //area to display the current score
           
           // JMenuItems to go in options
           JMenuItem Xfirst;
           JMenuItem Ofirst;
           JMenuItem randomFirst;
           
           JMenuItem newGame;
           JMenuItem resetScore;
           
           
           JMenuItem skyrim;
           JMenuItem police;
           JMenuItem noMusic;
           
           JMenuItem quit;
           
           boolean randomPlayer; // toggle for first player being random
           Random r; 
           String music;
           
           /** 
            * Constructs a new Tic-Tac-Toe boardDisplay and sets up the JFrame 
            * holds all of the needed JComponenets within it.
            */
           public Tic_Tac_Toe()
           { 
               // initialize the fields
               board = new String[3][3];
               boardGUI = new JButton[3][3];
              
               startingPlayer = PLAYER_X;
               player = PLAYER_X;
               winner = EMPTY;
               numFreeSquares = 9;
               
               x_wins = 0;
               o_wins = 0;
               ties = 0;
               
               r = new Random();
               randomPlayer = false;
               music = "";
              
               
               JFrame frame = new JFrame("Tic Tac Toes");
              
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
               JMenuBar menubar = new JMenuBar();
               frame.setJMenuBar(menubar);
               
               JMenu options = new JMenu("Options");
               menubar.add(options);
               
               JMenu first = new JMenu("Who goes first?");
               options.add(first);
               
               // add button for player x going first
               Xfirst = new JMenuItem("X goes First");
               first.add(Xfirst);
               Xfirst.addActionListener(this);
               
               // add button for player o going first
               Ofirst = new JMenuItem("O goes First");
               first.add(Ofirst);
               Ofirst.addActionListener(this);
               
               //add button for random player going first
               randomFirst = new JMenuItem("Random player");
               first.add(randomFirst);
               randomFirst.addActionListener(this);
               
               // button for new game
               newGame = new JMenuItem("New Game");
               options.add(newGame);
               newGame.addActionListener(this);
               
               // button for resetting score
               resetScore = new JMenuItem("Reset Score");
               options.add(resetScore);
               resetScore.addActionListener(this);
               
               // buttons for adding music
               JMenu music = new JMenu("Music");
               options.add(music);
               
               skyrim = new JMenuItem("Futuristic Skyrim");
               music.add(skyrim);
               skyrim.addActionListener(this);
               
               police = new JMenuItem("Cops and Robbers");
               music.add(police);
               police.addActionListener(this);
               
               noMusic = new JMenuItem("No Music");
               music.add(noMusic);
               noMusic.addActionListener(this);
               
               quit = new JMenuItem("Quit");
               options.add(quit);
               
               quit.addActionListener(new ActionListener()
               {// anonymous class for quitting the program
                   public void actionPerformed(ActionEvent event)
                   {
                       System.exit(0);
                    }
                });
                
                // shortcut for keyboard to make new game or quit game
                final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
                newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
                quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
                
               JPanel boardDisplay = new JPanel(new GridLayout(3,3)); // create JPanel 
               
               // loop that sets up all the buttons and the 3x3 grid
               for (int i = 0; i < 3; i++) {
                   for (int j = 0; j < 3; j++) {
                       board[i][j] = EMPTY;
                       boardGUI[i][j] = new JButton();
                       boardDisplay.add(boardGUI[i][j]);
                       boardGUI[i][j].addActionListener(this);
                    
                 }
              }
               
               // set labels that will display game data
               score = new JLabel("Player X: " + x_wins + "  Player O: " + o_wins + "  Ties: " + ties, SwingConstants.CENTER);
               status = new JLabel("Game in Progress. It is " + startingPlayer + "'s turn.", SwingConstants.CENTER);
               
               score.setFont(new Font("SansSerif", Font.BOLD, 25));
               status.setFont(new Font("SansSerif", Font.BOLD, 25));
               
               frame.getContentPane().add(score, BorderLayout.NORTH);
               frame.getContentPane().add(status, BorderLayout.SOUTH);
               
             
               frame.getContentPane().add(boardDisplay);
               
               
               frame.setVisible(true);
               frame.pack();
               frame.setSize(600,650);
               frame.setResizable(false);
               
        
            
                
            
            
        
        
       }
       
       /**
        * This is the action listener that is called when the user clicks most GUI buttons
        * 
        * @param e the button which was pressed
        */
        public void actionPerformed(ActionEvent e) 
        {  
            Object o = e.getSource();
            if (o instanceof JButton) {
                JButton button = (JButton) o;
                button.removeActionListener(this);
                play(button);
            } else if (o instanceof JMenuItem){
                JMenuItem item = (JMenuItem) o;
                if (item == newGame) {
                    newGame();
                } else if (item == resetScore) {
                    resetScore();
                } else if(item == noMusic) {
                    music = "";
                } else if (item == skyrim) {
                    music = "skyrim";
                } else if (item == police) {
                    music = "police";
                } else if (item == Xfirst) {
                    randomPlayer = false;
                    startingPlayer = PLAYER_X;
                    if (numFreeSquares == 9) {
                        status.setText("It is " + startingPlayer + "'s turn");
                        player = startingPlayer;
                    }
                    
                } else if (item == Ofirst) {
                    randomPlayer = false;
                    startingPlayer = PLAYER_O;
                    if (numFreeSquares == 9) {
                        status.setText("It is " + startingPlayer + "'s turn");
                        player = startingPlayer;
                    }
                }else if (item == randomFirst) {
                    randomPlayer = true;
                } else {
                }
 
            }
}
    
    /**
    * Clears the board and prepares the tic tac toe board to be played again
    */
    public void newGame() 
    {  
        winner = EMPTY;
        numFreeSquares = 9;
        // chooses a player to start by random
        if (randomPlayer) {
            int x = r.nextInt(2);
            if (x==0) {
                player = PLAYER_X;
            } else {
                player = PLAYER_O;
            }
        } else {
            player = startingPlayer;
        }
        status.setText("It is " + player + "'s turn");
        // removes the clicked status of all the button
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardGUI[i][j].getIcon() == null) {
                    boardGUI[i][j].removeActionListener(this);
                }
                board[i][j] = EMPTY;
                boardGUI[i][j].setIcon(null);
                boardGUI[i][j].addActionListener(this); // reinitalizes all the buttons to have listeners again
                
            
            }
      }
    }
    
    /**
    * Switches the player who will have the next turn
    */
    public void switchPlayers() 
    {  
        if (player.equals(PLAYER_X)) {
            player = PLAYER_O;
            //score.setText("It is " + player + "'s turn.");
        }else if (player.equals(PLAYER_O)){
            player = PLAYER_X;
            //score.setText("It is " + player + "'s turn.");
        } else {
            return;
        }
    }
    
    /**
    * After the a certain button has been pressed this method does checking 
    * for if there is a winner or not and what should happen accordingly.
    * 
    * @param button button that was pressed
    */
    public void play(JButton button) 
    {  
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (button == boardGUI[i][j]) { // finds the row and col of the pressed button
                        if (winner.equals(EMPTY)) {
                            numFreeSquares --; 
                            if (player.equals(PLAYER_X)) { 
                                board[i][j] = PLAYER_X;
                                button.setIcon(new ImageIcon("src/media/X.png")); // set the icon of button to X
                                
                                // play correct music
                                if (music.equals("skyrim")) {
                                    playMusic("src/media/skyrim1.wav");
                                }else if (music.equals("police")) {
                                    playMusic("src/media/police1.wav");
                                }
                            }else {
                                board[i][j] = PLAYER_O;
                                button.setIcon(new ImageIcon("src/media/O.png")); // set the icon of button to O
                                
                                //play correct music
                                if (music.equals("skyrim")) {
                                    playMusic("src/media/skyrim3.wav");
                                }else if (music.equals("police")) {
                                    playMusic("src/media/police2.wav");
                                }
                            }
                   
                            // checks if someone has won the game
                            if (haveWinner(i,j)) {
                                winner = player;
                                status.setText(winner + " has won the game");
                                updateScore();
                            } else if(numFreeSquares == 0) {
                                winner = TIE;
                                updateScore();
                                status.setText("The game was a tie.");
                            } else {
                                switchPlayers();
                                status.setText(" Game in progress. It is "+ player + "'s turn.");
                            }
                            
                            
                   
                            
                        }
                    }
                }
            }
    }
    
    /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise.
    *
    * @param int row of square just set
    * @param int col of square just set
    * 
    * @return true if we have a winner, false otherwise
    */
   public boolean haveWinner(int row, int col) 
   {
       // unless at least 5 squares have been filled, we don't need to go any further
       // (the earliest we can have a winner is after player X's 3rd move).

       if (numFreeSquares>4) return false;

       // Note: We don't need to check all rows, columns, and diagonals, only those
       // that contain the latest filled square.  We know that we have a winner 
       // if all 3 squares are the same, as they can't all be blank (as the latest
       // filled square is one of them).

       if ( board[row][0].equals(board[row][1]) &&
            board[row][0].equals(board[row][2]) ) return true;
       
       // check column "col"
       if ( board[0][col].equals(board[1][col]) &&
            board[0][col].equals(board[2][col]) ) return true;

       // if row=col check one diagonal
       if (row==col)
          if ( board[0][0].equals(board[1][1]) &&
               board[0][0].equals(board[2][2]) ) return true;

       // if row=2-col check other diagonal
       if (row==2-col)
          if ( board[0][2].equals(board[1][1]) &&
               board[0][2].equals(board[2][0]) ) return true;
       // no winner yet
       return false;
   }
   
   /**
    * Updates the score of the game depending on who has won, then updates
    * the label to this new information
    * 
    */
   private void updateScore()
   {
      
      if (winner.equals(PLAYER_X)) {
          x_wins ++;
        } else if (winner.equals(PLAYER_O)) {
          o_wins ++;
        } else {
          ties ++;
        }
      score.setText("Player X: " + x_wins + "  Player O: " + o_wins + "  Ties: " + ties);
   }
   
   /**
    * Resets the score. The board is then cleared and the label is updated
    */
   private void resetScore()
   {
      
      x_wins = 0;
      o_wins = 0;
      ties = 0;
      newGame();
      score.setText("Player X: " + x_wins + "  Player O: " + o_wins + "  Ties: " + ties);
   }
   
   /**
    * Plays music that needs to be played
    * 
    * @param soundName name of file that is to be played (ex, "sound.wav")
    */
   public void playMusic(String soundName)
 {
   try 
   {
       AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
       Clip clip = AudioSystem.getClip( );
       clip.open(audioInputStream);
       clip.start( );
   }
   catch(Exception ex)
   {
       System.out.println("Error with playing sound.");
       ex.printStackTrace( );
   }
 }
}
    

