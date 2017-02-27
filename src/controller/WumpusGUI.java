/* Author: Alex Erwin
 * Purpose: This is the main GUI interface for the Wumpus game. It comes with a tab pane to switch the views
 */
// package definition
package controller;
import java.awt.BorderLayout;
// import classes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.WumpusGame;
import view.GraphicView;
import view.GridView;
// 
public class WumpusGUI extends JFrame {
	// instance variables
	private WumpusGame theGame;
	private GridView gridView;
	private GraphicView graphicView;
	private JPanel currentView;
	private JTabbedPane tabpane;
	public static final int width = 800;
	public static final int height = 1000;
	// start the game when called
	public static void main(String[] args) {
		// get the GUI
		WumpusGUI g = new WumpusGUI();
		// set this visible
		g.setVisible(true);
	}
	// class constructor
	public WumpusGUI() {
		// set the settings for the window
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(width, height);
	    this.setLocation(100, 40);
	    this.setTitle("Hunt The Wumpus");
	    // initialize
	    initializeOnOpen();
	    // added graphics view
	    graphicView = new GraphicView(theGame, width, height);
	    // added grid view
	    gridView = new GridView(theGame, width, height);
	    // add observers
	    addObservers();
	    // create a tabbed pane
	    tabpane = new JTabbedPane();
	    // add both views
	    tabpane.addTab("Graphics", null, graphicView, "Graphics View");
	    tabpane.addTab("Text", null, gridView, "Text Grid View");
	    // center it
	    this.add(tabpane, BorderLayout.CENTER);  
	}
	  // changed to add a second observer
	  private void addObservers() {
		 // add both the default and 2nd observer
		 theGame.addObserver(gridView);
		 theGame.addObserver(graphicView);
	  }	
	// initialize the game when the application is ran
	private void initializeOnOpen() {
		// load the game
		theGame = new WumpusGame();
	}
}