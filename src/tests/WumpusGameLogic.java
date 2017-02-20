package tests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;
import model.WumpusGame;
import model.WumpusGameSpace;
// test suite for the Wumpus Game's Logic
public class WumpusGameLogic {
	// test for absolute positioning of the Wumpus
	@Test
	public void WumpusPositioned() {
		// variables
		int row = 0, col = 0;
		char[][] board;
		// construct a new game
		WumpusGame wg = new WumpusGame(true);	
		// randomize points
		row = randomInt(0, 9);
		col = randomInt(0, 9);
		// set the location of the Wumpus
		wg.setLocationOfWumpus(new Point(row,col));
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		// assert that the position is correct
		assertEquals(board[row][col],'W');
	}
	// test for absolute positioning of the Pits
	@Test 
	public void SlimePitsPositioned() {
		// variables
		Point[] pits = new Point[4];
		char[][] board;;
		// construct a new game
		WumpusGame wg = new WumpusGame(true);		
		// set points
		pits[0] = new Point(3,2);
		pits[1] = new Point(9,4);
		pits[2] = new Point(5,7);
		pits[3] = new Point(9,9);
		// set the pits
		wg.setLocationOfSlimePits(pits);
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		// assert that the position is correct
		assertEquals(board[3][2],'P');
		assertEquals(board[9][4],'P');	
		assertEquals(board[5][7],'P');
		assertEquals(board[9][9],'P');
	}
	// test for wumpus wrap around
	@Test 
	public void testWumpusWrapAround() {
		// variables
		Point wumpusLocation;
		char[][] board;
		// construct a new game
		WumpusGame wg = new WumpusGame(true);
		// ASSERT at 0,0 that it wraps
		// clear board
		wg.clearGameBoard();
		// set a location
		wumpusLocation = new Point(0, 0);		
		// set the wumpus's location
		wg.setLocationOfWumpus(wumpusLocation);
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		// assert there is a blood point at wrap point
		assertEquals(board[9][0],'B');
		assertEquals(board[0][9],'B');
		// ASSERT at 9,9 that it wraps
		// clear board
		wg.clearGameBoard();		
		// set a location
		wumpusLocation = new Point(9, 9);		
		// set the wumpus's location
		wg.setLocationOfWumpus(wumpusLocation);
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		// assert there is a blood point at wrap point
		assertEquals(board[9][0],'B');
		assertEquals(board[0][9],'B');
		// ASSERT at 0,1 that it wraps
		// clear board
		wg.clearGameBoard();		
		// set a location
		wumpusLocation = new Point(0, 1);		
		// set the wumpus's location
		wg.setLocationOfWumpus(wumpusLocation);
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		System.out.println(wg.toString());
		// assert there is a blood point at wrap point
		assertEquals(board[0][9],'B');
		// ASSERT at 1,0 that it wraps
		// clear board
		wg.clearGameBoard();		
		// set a location
		wumpusLocation = new Point(1, 0);		
		// set the wumpus's location
		wg.setLocationOfWumpus(wumpusLocation);
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		System.out.println(wg.toString());
		// assert there is a blood point at wrap point
		assertEquals(board[9][0],'B');			
	}
	// test for absolute positioning of the Wumpus and Pits and that Goop is in a space 
	@Test
	public void testAllPositionedClosely() {
		// variables
		Point wumpusLocation = new Point(3, 0);
		Point[] pits = new Point[3];
		char[][] board;
		// construct a new game
		WumpusGame wg = new WumpusGame(true);	
		// set points for pits
		pits[0] = new Point(3,2);
		pits[1] = new Point(9,4);
		pits[2] = new Point(5,7);
		// set the wumpus and the pits
		wg.setLocationOfSlimePits(pits);		
		wg.setLocationOfWumpus(wumpusLocation);
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();
		// assert there is a goop point at 3,1
		assertEquals(board[3][1],'G');
	}
	// test that the hunter is present if none of the pits or wumpus has been set
	@Test
	public void testHunterPresentOnClearBoard() {
		// variables
		Point hunterLocation;
		char[][] board;
		// construct a new game
		WumpusGame wg = new WumpusGame(true);
		// clear the board
		wg.clearGameBoard();
		// assign the hunter
		wg.setLocationOfHunter();
		// get the location of the hunter
		hunterLocation = wg.getLocationOfHunter();
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();		
		// assert the hunter is on the board
		assertEquals(board[hunterLocation.x][hunterLocation.y], 'H');
	}
	// test that the Wumpus is present
	@Test
	public void testWumpusPresentOnClearBoard() {
		// variables
		Point wumpusLocation;
		char[][] board;
		// construct a new game
		WumpusGame wg = new WumpusGame(true);
		// clear the board
		wg.clearGameBoard();
		// assign the wumpus
		wg.setLocationOfWumpus();
		// get the location of the wumpus
		wumpusLocation = wg.getLocationOfWumpus();
		// get the board with all the pieces on it
		board = wg.getAllTokensMap();		
		// assert the hunter is on the board
		assertEquals(board[wumpusLocation.x][wumpusLocation.y], 'W');		
	}
	// test the getSize method
	@Test
	public void testGetSize() {
		// construct a new game
		WumpusGame wg = new WumpusGame();
		// assert that the size is 10
		assertEquals(wg.getSize(), 10);
	}
	// test the toString method
	@Test
	public void testToString() {
		// construct a new game
		WumpusGame wg = new WumpusGame();
		// assert that there is a string
		assertTrue(wg.toString().length() != 0);
	}
	// test the getFogOfWarMap method
	@Test
	public void testGetFogOfWarMap() {
		// construct a new game
		WumpusGame wg = new WumpusGame();
		// assert that there is a string
		assertTrue(wg.getFogOfWarMap().length != 0);		
	}
	// test the getBoard method
	@Test
	public void testGetBoard() {
		// construct a new game
		WumpusGame wg = new WumpusGame();
		// assert that there is a string
		assertTrue(wg.getBoard().length != 0);		
	}
	// test the startNewGame method
	@Test
	public void testStartNewGame() {
		// construct a new game
		WumpusGame wg = new WumpusGame(true);
		// start the game
		wg.startNewGame();
		// assert that the board isn't empty
		assertTrue(wg.getBoard().length != 0);
		// assert that the hunter has been set
		assertTrue(wg.getLocationOfHunter() != null);
		// assert that the wumpus has been set
		assertTrue(wg.getLocationOfWumpus() != null);
		// assert that the pits have been set
		assertTrue(wg.getLocationOfSlimePits().length > 2);			
	}	
	// tests for WumpusGameSpace
	// test getRevealed method
	@Test 
	public void testSetRevealed() {
		// construct a new space
		WumpusGameSpace ws = new WumpusGameSpace('W');
		// assert that the revealed state should be false
		assertEquals(ws.getRevealed(), false);
		// set the revealed state to true
		ws.setRevealed();
		// assert that the revealed state should be true
		assertEquals(ws.getRevealed(), true);		
	}
	// test getOccupant method
	@Test 
	public void testSetOccupant() {
		// construct a new space
		WumpusGameSpace ws = new WumpusGameSpace(' ');
		// assert that the occupant is an empty space
		assertEquals(ws.getOccupant(), ' ');
		// set the occupant to be a Slime
		ws.setOccupant('S');
		// assert that the occupant is now a slime
		assertEquals(ws.getOccupant(), 'S');
	}	
	// test getRevealed method
	@Test 
	public void testGetRevealed() {
		// construct a new space
		WumpusGameSpace ws = new WumpusGameSpace('W');
		// assert that the revealed state should be false
		assertEquals(ws.getRevealed(), false);
	}
	// test getOccupant method
	@Test 
	public void testGetOccupant() {
		// construct a new space
		WumpusGameSpace ws = new WumpusGameSpace('W');
		// assert that the occupant should be a wumpus
		assertEquals(ws.getOccupant(), 'W');
	}
	// test isFreeSpace method
	@Test 
	public void testIsFreeSpace() {
		// construct a new space
		WumpusGameSpace ws = new WumpusGameSpace(' ');
		// assert that this is a free space
		assertEquals(ws.isFreeSpace(), true);
		// change it up
		ws.setOccupant('W');
		// assert that this isn't a free space
		assertTrue(ws.isFreeSpace() == false);
	}		
	// function to randomize the events
	private int randomInt(int low, int high) {
		return low + (int) (Math.random() * (high - low + 1));
	}	
}
