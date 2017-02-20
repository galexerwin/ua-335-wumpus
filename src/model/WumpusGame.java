/* Author: Alex Erwin
 * Purpose: This is the main GUI interface for the Wumpus game.
 * 
 */
// package definition
package model;
// import classes
import java.awt.*;
import controller.WumpusObserver;

// WumpusGame class extends our observer
public class WumpusGame extends WumpusObserver {
	// instance variables
	private int size;
	private WumpusGameSpace[][] board;	
	private Point wumpusLocation;
	private Point hunterLocation;
	private Point[] pitLocations;
	// constructor
	public WumpusGame() {
		// set the game size
		this.size = 10;
		// initialize the board
		initializeBoard();
	}
	// overloaded constructor for testing
	public WumpusGame(boolean testing) {
		// set the game size
		this.size = 10;		
	}
	// start a new game from the menu
	public void startNewGame() {
		// initialize the game board
		initializeBoard();
	}
	// initialize the board
	public void initializeBoard() {
		// clear the game board
		clearGameBoard();
		// set the location of the pits and associated slime
		setLocationOfSlimePits();
		// set the location of the wumpus and associated blood
		setLocationOfWumpus();
		// set the location of the hunter
		setLocationOfHunter();
	}
	// set the board to empty
	public void clearGameBoard() {
		// create a new object to hold the board details
		this.board = new WumpusGameSpace[this.size][this.size];
		// set the entire board to empty
		for (int row = 0; row < this.size; row++)
			for (int col = 0; col < this.size; col++)
				this.board[row][col] = new WumpusGameSpace(' ');
	}
	// set the wumpus's location
	public void setLocationOfWumpus() {
		// variables
		boolean success = false;
		int row = -1, col = -1;
		// check if the board is empty, which would occur only if no pits have been set
		if (this.pitLocations == null) {
			// set the row and col to a random
			row = randomInt(0, (this.size - 1));
			col = randomInt(0, (this.size - 1));
		} else {
			// we have to guarantee the spot we pick is empty
			do {
				// set the row and col to a random
				row = randomInt(0, (this.size - 1));
				col = randomInt(0, (this.size - 1));	
				// check if the spot is empty
				if (this.board[row][col].isFreeSpace())
					success = true;	// success
			} while(!success);
		}
		// call overloaded
		setLocationOfWumpus(new Point(row,col));
	}
	// set the wumpus's location manually
	public void setLocationOfWumpus(Point wumpusLocation) {
		// variables
		int wumpusRow = wumpusLocation.x;
		int wumpusCol = wumpusLocation.y;
		// sets the Wumpus's location to a point specified
		this.wumpusLocation = wumpusLocation;
		// check board state
		if (this.board == null)
			clearGameBoard();		
		// iterate over the entire the board and set the details
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {				
				// check what should be in the point
				if (wumpusRow == row && wumpusCol == col) {
					// set the Wumpus here
					this.board[row][col].setOccupant('W');
				} else if (row == wumpusRow) {
					// if we are in the same row as the wumpus
					if (((wumpusCol - 3) < col && col < (wumpusCol + 3))) {
						// set any spaces that are between the markers but do not wrap around
						setBIfFreeOrG(row,col);
					} else if (wumpusCol == (this.size - 1) && (col == 0 || col == 1)) {
						// set any spaces that are wrap around to the right
						setBIfFreeOrG(row,col);
					} else if (wumpusCol == 0 && (col == (this.size - 1) || col == (this.size - 2))) {
						// set any spaces that are wrap around to the left
						setBIfFreeOrG(row,col);
					} else if ((wumpusCol - 2) < 0 && col == ((wumpusCol - 2) + this.size)) {
						// special case when the wampus is at an odd interval that only has one space above or below
						setBIfFreeOrG(row,col);					
					}
				} else if (col == wumpusCol) {
					// if we are in the same column as the wumpus
					if (((wumpusRow - 3) < row && row < (wumpusRow + 3))) {
						// set any spaces that are between the markers but do not wrap around
						setBIfFreeOrG(row,col);
					} else if (wumpusRow == (this.size - 1) && (row == 0 || row == 1)) {
						// set any spaces that are wrap around to the right
						setBIfFreeOrG(row,col);
					} else if (wumpusRow == 0 && (row == (this.size - 1) || row == (this.size - 2))) {
						// set any spaces that are wrap around to the left
						setBIfFreeOrG(row,col);
					} else if ((wumpusRow - 2) < 0 && row == ((wumpusRow - 2) + this.size)) {
						// special case when the wampus is at an odd interval that only has one space above or below
						setBIfFreeOrG(row,col);
					}
				} else if ((row == (wumpusRow - 1) || row == (wumpusRow + 1)) && (col == (wumpusCol + 1) || col == (wumpusCol - 1))) {
					// case if we are in the rows and columns immediately surrounding the wumpus
					setBIfFreeOrG(row,col);
				} else if (((wumpusRow - 1) < 0 || (wumpusRow + 1) > (this.size - 1)) && (row == (this.size - 1) || row == 0) && (col == (wumpusCol - 1) || col == (wumpusCol + 1))) {
					// special case for wrap around to the top or bottom
					setBIfFreeOrG(row,col);
				} else if (((wumpusCol - 1) < 0 || (wumpusCol + 1) > (this.size - 1)) && (col == (this.size - 1) || col == 0) && (row == (wumpusRow - 1) || row == (wumpusRow + 1))) {
					// special case for wrap around to the left or right
					setBIfFreeOrG(row,col);				
				}
			}
		}		
	}
	// set the pits and slime
	public void setLocationOfSlimePits() {
		// variables
		int max = randomInt(3, 5); // spec says that it can vary
		Point[] pits = new Point[max]; // create an array of points large enough to fit 3 - 5
		// iterate over the max
		for (int i = 0; i < max; i++)
			pits[i] = new Point(randomInt(0,(this.size - 1)), randomInt(0,(this.size - 1))); // create a new point for a pit
		// call overloaded
		setLocationOfSlimePits(pits);
	}
	// set the pits and slime, overloaded with pits array
	public void setLocationOfSlimePits(Point[] pits) {
		// variables
		int row = 0, col = 0;
		// store the pits
		this.pitLocations = pits;
		// check board state
		if (this.board == null)
			clearGameBoard();
		// iterate over all the pits given to us and set the board that correspond to the points as pit occupants
		for(int i = 0; i < pits.length; i++) {
			// store row and col
			row = pits[i].x;
			col = pits[i].y;
			// set the occupant at row and col
			this.board[row][col].setOccupant('P');
			// check left
			if (col == 0) { // if this is a wrap around
				if (this.board[row][this.size - 1].isFreeSpace()) // if the adjacent is free
					this.board[row][this.size - 1].setOccupant('S'); // set the slime
			} else if (this.board[row][col - 1].isFreeSpace()) { // if this isn't a wrap around, check adjacent is free
				this.board[row][col - 1].setOccupant('S'); // set the slime
			}
			// check right
			if (col == (this.size - 1)) { // if this is a wrap around
				if (this.board[row][0].isFreeSpace()) // if the adjacent is free
					this.board[row][0].setOccupant('S'); // set the slime
			} else if (this.board[row][col + 1].isFreeSpace()) { // if this isn't a wrap around, check adjacent is free
				this.board[row][col + 1].setOccupant('S'); // set the slime
			}
			// check top
			if (row == 0) { // if this is a wrap around
				if (this.board[this.size - 1][col].isFreeSpace()) // if the adjacent is free
					this.board[this.size - 1][col].setOccupant('S'); // set the slime
			} else if (this.board[row - 1][col].isFreeSpace()) { // if this isn't a wrap around, check adjacent is free
				this.board[row - 1][col].setOccupant('S'); // set the slime
			}
			// check bottom
			if (row == (this.size - 1)) { // if this is a wrap around
				if (this.board[0][col].isFreeSpace()) // if the adjacent is free
					this.board[0][col].setOccupant('S'); // set the slime
			} else if (this.board[row + 1][col].isFreeSpace()) { // if this isn't a wrap around, check adjacent is free
				this.board[row + 1][col].setOccupant('S'); // set the slime
			}			
		}
	}
	// set the hunter's location (this depends on a free space)
	public void setLocationOfHunter() {
		// variables
		boolean success = false;
		int row = -1, col = -1;
		// check if the board is empty
		if (this.pitLocations == null && this.wumpusLocation == null) {
			// set the row and col to a random
			row = randomInt(0, (this.size - 1));
			col = randomInt(0, (this.size - 1));
		} else {
			// we have to guarantee the spot we pick is empty
			do {
				// set the row and col to a random
				row = randomInt(0, (this.size - 1));
				col = randomInt(0, (this.size - 1));	
				// check if the spot is empty
				if (this.board[row][col].isFreeSpace())
					success = true;	// success
			} while(!success);
		}
		// store the starting point of the hunter 
		this.hunterLocation = new Point(row, col);
		// move them to the space
		this.board[row][col].setOccupant('H');
		// uncover from fog of war
		this.board[row][col].setRevealed();
	}
	// return the whole board
	public WumpusGameSpace[][] getBoard() {
		return this.board;
	}
	// return the board with fog of war applied
	public char[][] getFogOfWarMap() {
		// variables
		char[][] map = new char[this.size][this.size];
		// iterate over the board and apply tokens to the map
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				// check if the space has been uncovered
				if (this.board[row][col].revealed) {
					// send back the space
					map[row][col] = this.board[row][col].getOccupant();
				} else {
					map[row][col] = 'X';
				}
			}
		}
		// return
		return map;
	}
	// return the board with pieces on it
	public char[][] getAllTokensMap() {
		// variables
		char[][] map = new char[this.size][this.size];
		// iterate over the board and apply tokens to the map
		for (int row = 0; row < this.size; row++)
			for (int col = 0; col < this.size; col++) 
				map[row][col] = this.board[row][col].getOccupant();
		// return
		return map;		
	}
	// return the size
	public int getSize() {
		return this.size;
	}
	// return the wumpus's location
	public Point getLocationOfWumpus() {
		return this.wumpusLocation;
	}
	// return the hunter's location
	public Point getLocationOfHunter() {
		return this.hunterLocation;
	}
	// return the pit locations
	public Point[] getLocationOfSlimePits() {
		return this.pitLocations;
	}
	// helper to set the occupant
	private void setBIfFreeOrG(int row, int col) {
		// perform the check
		if (this.board[row][col].isFreeSpace()) // totally free space
			this.board[row][col].setOccupant('B'); // set blood
		else if (!(this.board[row][col].getOccupant() == 'P')) // space doesn't have pit already present
			this.board[row][col].setOccupant('G'); // set goop because Slime is already present
	}
	// randomizer credit to Rick Mercer
	private int randomInt(int low, int high) {
		return low + (int) (Math.random() * (high - low + 1));
	}
	// toString method
	public String toString() {
		// variables
		String theGameBoard = "";
		// iterate over the entire the board and set the details
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				// retrieve the occupant
				theGameBoard += "[" + String.valueOf(this.board[i][j].getOccupant()) + "]";
			}
			// add a new row
			theGameBoard += "\n";
		}
		// return
		return theGameBoard;
	}
}
