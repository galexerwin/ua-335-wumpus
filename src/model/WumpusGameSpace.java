/* Author: Alex Erwin
 * Purpose: This is an object that represents the multiple states that the Wumpus Game can have.
 * @revealed is the Map Coverage -> Visited/Not Visit
 * @occupant represents the objects other than the hunter that can be in the room S,B,G,W,P
 */
// package definition
package model;
// class definition
public class WumpusGameSpace {
	// instance variables
	private char occupant;
	private boolean revealed;
	private boolean hunterIsHere;
	// constructor that takes ' ',S,B,G,W,P. Only used when starting a new game
	public WumpusGameSpace(char occupant) {
		// by default nothing is revealed
		this.revealed = false;
		// by default the hunter isn't in here
		this.hunterIsHere = false;
		// set the occupant
		this.occupant = occupant;
	}
	// set the occupant of this space
	public void setOccupant(char occupant) {
		this.occupant = occupant;
	}
	// get the occupant of this space.
	public char getOccupant() {
		return this.occupant;
	}
	// set the revealed state doesn't accept anything because it only can be true if being set
	public void setRevealed() {
		this.revealed = true;
	}
	// get the revealed state
	public boolean getRevealed() {
		return this.revealed;
	}
	// set the hunter is in this room
	public void setLinkAsInThisRoom() {
		this.hunterIsHere = true;
	}
	// set the hunter isn't in this room
	public void setLinkAsNotInThisRoom() {
		this.hunterIsHere = false;
	}
	// determine if the hunter is in this room
	public boolean isLinkInHere() {
		return this.hunterIsHere;
	}
	// determine if is free
	public boolean isFreeSpace() {
		return ((occupant == ' ' && hunterIsHere == false) ? true : false);
	}
}