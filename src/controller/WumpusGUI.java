/* Author: Alex Erwin
 * Purpose: This is the main GUI interface for the Wumpus game.
 * 
 */
// package definition
package controller;
// import classes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import model.WumpusGame;
//import view.ButtonView;
//import view.GraphicView;
//import view.TextAreaView;
// 
public class WumpusGUI extends JFrame {
	// instance variables
	private WumpusGame theGame;
	//private ButtonView buttonView;
	//private TextAreaView textAreaView;
	//private GraphicView graphicView;
	private JPanel currentView;
	public static final int width = 300;
	public static final int height = 360;
	// start the game when called
	public static void main(String[] args) {
		WumpusGame g = new WumpusGame();

	}
	// class constructor
	public WumpusGUI() {
		// set the settings for the window
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(width, height);
	    this.setLocation(100, 40);
	    this.setTitle("Wumpus");
	    
	}
	
}
