/* Author: Alex Erwin
 * Purpose: This is the logic for the Wumpus game.
 * Uses WumpusGameSpace to control the multi-state occupants and the revealed flag
 */
// package definition
package model;
// import classes
import java.awt.*;
import java.util.Observable;
// WumpusGame class extends Observable
public class WumpusGame extends Observable {
	// instance variables
	private int size;
	private boolean gameOver;
	private WumpusGameSpace[][] board;	
	private Point wumpusLocation;
	private Point hunterLocation;
	private Point hunterPreviousLocation;
	private Point[] pitLocations;
	private String gameMessage;
	// constructor
	public WumpusGame() {
		// set the game size
		this.size = 10;
		// set the game over state
		this.gameOver = false;
		// set message to nothing
		this.gameMessage = "";
		// initialize the board
		initializeBoard();	
	}
	// overloaded constructor for testing
	public WumpusGame(boolean testing) {
		// set the game size
		this.size = 10;
		// set the game over state
		this.gameOver = false;
		// set message to nothing
		this.gameMessage = "";
	}
	// start a new game from the menu
	public void startNewGame() {
		// initialize the game board
		initializeBoard();
		// notify observers
		notifyObservers();		
	}
	// initialize the board
	public void initializeBoard() {
		// set the game over state to false
		this.gameOver = false;
		// set message to nothing
		this.gameMessage = "";		
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
					if (col == (this.size - 1) && wumpusCol == 1) {
						// set the top row that was missing
						setBIfFreeOrG(row,col);
					} else if (col == 0 && wumpusCol == (this.size - 2)) {
						// set the top row that was missing
						setBIfFreeOrG(row,col);
					} else if (((wumpusCol - 3) < col && col < (wumpusCol + 3))) {
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
					if (row == (this.size - 1) && wumpusRow == 1) {
						// set the top row that was missing
						setBIfFreeOrG(row,col);
					} else if (row == 0 && wumpusRow == (this.size - 2)) {
						// set the top row that was missing
						setBIfFreeOrG(row,col);
					} else if (((wumpusRow - 3) < row && row < (wumpusRow + 3))) {
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
		System.out.println(toString());
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
		int addOneResult = 0, subOneResult = 0;
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
			// calculate the add/sub one for the column tests
			addOneResult = Math.floorMod(col + 1, this.size);
			subOneResult = Math.floorMod(col - 1, this.size);
			// check left
			if (this.board[row][subOneResult].isFreeSpace())
				this.board[row][subOneResult].setOccupant('S'); // set the slime
			// check right
			if (this.board[row][addOneResult].isFreeSpace())
				this.board[row][addOneResult].setOccupant('S'); // set the slime			
			// calculate the add/sub one for the row tests
			addOneResult = Math.floorMod(row + 1, this.size);
			subOneResult = Math.floorMod(row - 1, this.size);
			// check top
			if (this.board[subOneResult][col].isFreeSpace())
				this.board[subOneResult][col].setOccupant('S'); // set the slime
			// check bottom
			if (this.board[addOneResult][col].isFreeSpace())
				this.board[addOneResult][col].setOccupant('S'); // set the slime							
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
		// also store the as the previous for the game play animation
		this.hunterPreviousLocation = new Point(row, col);
		// move them to the space
		this.board[row][col].setLinkAsInThisRoom();
		// uncover from fog of war
		this.board[row][col].setRevealed();
	}
	// move the hunter
	public void moveTheHunter(char direction) {
		// variables
		Point move = null;
		int oldRow = this.hunterLocation.x;
		int oldCol = this.hunterLocation.y;
		int newRow = -1, newCol = -1;
		char occupant;
		// check if the game is over
		if (gameOver)
			return;
		// determine what direction to move to
		switch (direction) {
		case 'N':// move the hunter up one
			newRow = Math.floorMod(oldRow - 1, this.size);
			newCol = oldCol;
			break;
		case 'S':// move the hunter down one
			newRow = Math.floorMod(oldRow + 1, this.size);
			newCol = oldCol;
			break;
		case 'E': // move the hunter one to the right
			newRow = oldRow;
			newCol = Math.floorMod(oldCol + 1, this.size);
			break;
		case 'W': // move the hunter one to the left
			newRow = oldRow;
			newCol = Math.floorMod(oldCol - 1, this.size);
			break;
		}
		// set the move
		move = new Point(newRow, newCol);
		// move the old position
		this.hunterPreviousLocation = this.hunterLocation;  // store the hunter's old location
		this.board[oldRow][oldCol].setLinkAsNotInThisRoom();// declare the hunter as no longer here
		// store the move
		this.hunterLocation = move;							// store the hunter's new location
		this.board[newRow][newCol].setLinkAsInThisRoom();	// declare the hunter is now here
		// uncover the space
		this.board[newRow][newCol].setRevealed();
		// determine if this room has blood, slime, or goop in it
		// get the occupant
		occupant = this.board[newRow][newCol].getOccupant();
		// test if one of the three
		if (occupant == 'B' || occupant == 'S' || occupant == 'G')
			this.gameMessage = "What an awful stench!";
		else 
			this.gameMessage = "";
		// determine if the game is over
		checkState(isGameOverFromMove());
	}
	// shoot the arrow
	public void shootTheArrow(char direction) {
		// determine the direction the arrow was fired in
		switch (direction) {
		case 'N':
		case 'S':
			// north or south direction
			// determine if the Wumpus is in this column
			if (this.hunterLocation.y == this.wumpusLocation.y) {
				// wumpus is in this column and the arrow kills the Wumpus
				// set the message
				this.gameMessage = "You have felled the foul beast. Way cool!";
				// notify observers
				checkState(true);
				// exit
				return;				
			} else {
				// wumpus is not in this column and the arrow automatically hits the hunter
				// set the message
				this.gameMessage = "You got shot by your own arrow. Not cool.";
				// notify observers
				checkState(true);
			}
			break;
		case 'E':
		case 'W':
			// east or west direction
			// determine if the Wumpus is in this column
			if (this.hunterLocation.x == this.wumpusLocation.x) {
				// wumpus is in this column and the arrow kills the Wumpus
				// set the message
				this.gameMessage = "You have felled the foul beast. Way cool!";
				// notify observers
				checkState(true);
				// exit
				return;				
			} else {
				// wumpus is not in this column and the arrow automatically hits the hunter
				// set the message
				this.gameMessage = "You got shot by your own arrow. Not cool.";
				// notify observers
				checkState(true);
			}			
			break;
		}
	}
	// call to see if game is over
	public boolean isGameOver() {
		return this.gameOver;
	}
	// get any messages
	public String getGameMessage() {
		return this.gameMessage;
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
				if (this.board[row][col].getRevealed()) {
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
				if (this.board[row][col].isLinkInHere())
					map[row][col] = 'H';
				else
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
	// return the hunter's previous location
	public Point getPreviousLocationOfHunter() {
		return this.hunterPreviousLocation;
	}
	// return the pit locations
	public Point[] getLocationOfSlimePits() {
		return this.pitLocations;
	}
	// determine if the game is over from moving the hunter
	public boolean isGameOverFromMove() {
		// test the deciding factors
		if (this.gameOver == true) // if the game is already over
			return true;
		// if the hunter was eaten by the Wumpus
		if (getLocationOfHunter().equals(getLocationOfWumpus())) { // if the hunter was eaten by the Wumpus
			// set the message
			this.gameMessage = "The Wumpus Ate Your Baby!";
			// return true
			return true;
		}
		// iterate over the slime pits and see if the hunter fell into one of them
		for (int i = 0; i < pitLocations.length; i++) {	
			if (getLocationOfHunter().equals(pitLocations[i])) {
				// set the message
				this.gameMessage = "You fell in a pit. Better luck next time.";
				// return true
				return true;
			}
		}		
		// return false
		return false;
	}
	// notify observers
	public void checkState(boolean endGame) {
		// always set changed
		setChanged();
		// if the game has ended
		if (endGame) {
			// set the game over state
			this.gameOver = true;
			// notify the observers that the game is over
			notifyObservers(true);			
		} else {
			// nothing to report
			notifyObservers(false);
		}
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
				if (this.board[i][j].isLinkInHere())
					theGameBoard += "[H]";
				else
					theGameBoard += "[" + String.valueOf(this.board[i][j].getOccupant()) + "]";
			}
			// add a new row
			theGameBoard += "\n";
		}
		// return
		return theGameBoard;
	}
}