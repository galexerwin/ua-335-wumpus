/*
 * Author: Alex Erwin
 * Purpose: Text (GridView) is a simple drawString gridView.
 * Utilizes a private inner class to display the game. The game is in an upper pane and the controls are in a lower pane.
 */
// package definition
package view;
//import classes
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.*;
import model.WumpusGame;
//this has view has a text grid
public class GridView extends JPanel implements Observer {
	// instance variables
	private JPanel upper, lower;
	private JLabel gameMessageOut;
	private WumpusGame theGame;
	private int height, width;
	private int gameHeight, controlsHeight;
	// constructor
	public GridView(WumpusGame theGame, int width, int height) {
		// import the game
		this.theGame = theGame;
		// set dimensions
		this.width   = width;
		this.height  = height;
		// initialize the game board
		initializeGraphicPanel();		
	}
	// setup the panel with once needed settings
	public void initializeGraphicPanel() {
		// variables
		JPanel moveButtons, shootButtons;
		JLabel moveButtonLabel, shootButtonLabel;
		JButton moveNorth, moveSouth, moveEast, moveWest; // direction buttons for moving
		JButton shootNorth, shootSouth, shootEast, shootWest; // direction buttons for shooting
		GridBagConstraints c = new GridBagConstraints(); // gridbag setup
		// set the sizes
		this.gameHeight = (int)((double)height *.75);
		this.controlsHeight = (int)((double)height *.20);
		// initialize JPanels
		upper = new GameWindow(width, this.gameHeight); // for the game
		lower = new JPanel(); // for the controls
		moveButtons = new JPanel();
		shootButtons = new JPanel();
		// set dimensions
		upper.setPreferredSize(new Dimension(width, this.gameHeight));
		// set features for the lower panel
		lower.setPreferredSize(new Dimension(width, this.controlsHeight));
		lower.setLocation(0, 250);
		// set a grid layout for the lower half
		lower.setLayout(new GridBagLayout());
		// set border layout for button panels
		moveButtons.setLayout(new BorderLayout());
		shootButtons.setLayout(new BorderLayout());
		// set the message label
		this.gameMessageOut = new JLabel("", JLabel.CENTER);
		this.gameMessageOut.setForeground(Color.BLACK);
		this.gameMessageOut.setFont(new Font("Serif", Font.BOLD, 40));
		// new labels
		moveButtonLabel = new JLabel("Move Hunter (by Direction)");
		shootButtonLabel = new JLabel("Shoot Arrow (by Direction)");
		// create move buttons
		moveNorth = new JButton("N");
		moveSouth = new JButton("S");
		moveEast = new JButton("E");
		moveWest = new JButton("W");
		// create shoot buttons
		shootNorth = new JButton("N");
		shootSouth = new JButton("S");
		shootEast = new JButton("E");
		shootWest = new JButton("W");		
		// add an action listener to the move buttons
		moveNorth.addActionListener(new moveHunter('N'));
		moveSouth.addActionListener(new moveHunter('S'));
		moveEast.addActionListener(new moveHunter('E'));
		moveWest.addActionListener(new moveHunter('W'));
		// add an action listener to the shoot buttons
		shootNorth.addActionListener(new shootArrow('N'));
		shootSouth.addActionListener(new shootArrow('S'));
		shootEast.addActionListener(new shootArrow('E'));
		shootWest.addActionListener(new shootArrow('W'));			
		// add move buttons to moveButtons panel
		moveButtons.add(moveNorth, BorderLayout.NORTH);
		moveButtons.add(moveSouth, BorderLayout.SOUTH);
		moveButtons.add(moveEast, BorderLayout.EAST);
		moveButtons.add(moveWest, BorderLayout.WEST);
		// add shoot buttons to shootButtons panel
		shootButtons.add(shootNorth, BorderLayout.NORTH);
		shootButtons.add(shootSouth, BorderLayout.SOUTH);
		shootButtons.add(shootEast, BorderLayout.EAST);
		shootButtons.add(shootWest, BorderLayout.WEST);
		// defaults	
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		// add controls to lower
		// move label
		c.ipady = 20;
		c.gridx = 1;
		c.gridy = 0;
		lower.add(moveButtonLabel, c);
		// move buttons
		c.ipady = 0;
		c.gridx = 1;
		c.gridy = 1;				
		lower.add(moveButtons, c);
		// shoot label
		c.ipady = 20;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		lower.add(shootButtonLabel, c);
		// shoot buttons
		c.ipady = 0;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;				
		lower.add(shootButtons, c);
		// add panels to the main
		this.add(upper);
		this.add(lower);
	}		
	@Override
	public void update(Observable theWumpusGame, Object gameOver) {
		// repaint the upper section
		this.repaint();
	}
	// paint the game onto the canvas
	@Override
	public void paintComponent(Graphics g) {
		// repaint the main window
		super.paintComponent(g); 
		// repaint the upper section
		upper.repaint();
	}
	// private class to handle button inputs for movement
	private class moveHunter implements ActionListener {
		// instance variables
		private char direction;
		// constructor accepts the direction to move the character to
		public moveHunter(char direction) {
			this.direction = direction;
		}
		// perform action as required
		public void actionPerformed(ActionEvent e) {
			// update the game
			theGame.moveTheHunter(this.direction);
			// update the view
			upper.repaint();
		}
	}
	// private class to handle button inputs for shooting
	private class shootArrow implements ActionListener {
		// instance variables
		private char direction;
		// constructor accepts the direction to move the character to
		public shootArrow(char direction) {
			this.direction = direction;
		}
		// perform action as required
		public void actionPerformed(ActionEvent e) {
			// update the game
			theGame.shootTheArrow(this.direction);
			// update the view
			upper.repaint();
		}		
	}
	/* The GameWindow is to create an override especially for this window only
	 * so that the main stage contains a game panel and a button panel
	 */
	private class GameWindow extends JPanel {
		// instance variables
		private int gameSize; // placement dimensions
		private int cellWidth, cellHeight; // placement dimensions
		private Point[][] cells; // cell locations, not really important in this version
		// constructor
		public GameWindow(int width, int height) {
			// set the size of the game
			this.gameSize = theGame.getSize();
			// set the cell width and height
			this.cellWidth = ((width / gameSize));
			this.cellHeight = ((height / gameSize));		
			// load the game cells
			loadGameCells();			
		}
		// load game table cells
		public void loadGameCells() {
			// create the table
			cells = new Point[gameSize][gameSize];
			/* Fill a table that corresponds to the game board with Points
			 * The points are the lower right edges of each cell. Combined with cell boundaries
			 * the points determine what the area means to the game.
			 */
			for (int i = 0, row = 1; i < gameSize; i++, row++)
				for (int j = 0, col = 1; j < gameSize; j++, col++)
					cells[i][j] = new Point(((this.cellWidth * col)),((this.cellHeight * row)));
		}
		// paint the game onto the canvas
		@Override
		public void paintComponent(Graphics g) {
			// variables
			Point hunterCurrent = theGame.getLocationOfHunter(); 	// get the location
			char[][] board; 										// get the board
			int occupantX = 0, occupantY = 0;						// occupant X, Y
			// allow the UI to paint the background
			super.paintComponent(g); 
			// pass to the 2D subclass
			Graphics2D g2 = (Graphics2D) g;
			// determine which map to show
			if (theGame.isGameOver()) {
				board = theGame.getAllTokensMap();
			} else
				board = theGame.getFogOfWarMap();
			// fill the board
			for (int row = 0; row < this.gameSize; row++) {
				for (int col = 0; col < this.gameSize; col++) {
					// placement of the occupant is different than the ground
					occupantX = cells[row][col].x - this.cellWidth + (this.cellWidth / 4);
					occupantY = cells[row][col].y - this.cellHeight + (this.cellHeight / 4);
					// determine if the hunter should be in this spot
					if (hunterCurrent.equals(new Point(row, col)))
						g2.drawString("[O]", occupantX, occupantY);
					else
						g2.drawString("[" + Character.toString(board[row][col]) + "]", occupantX, occupantY);
				}
			}
			// get any messages
			gameMessageOut.setText(theGame.getGameMessage());
			// draw the game message out label onto the board
			this.add(gameMessageOut);
		}		
	}	
}
