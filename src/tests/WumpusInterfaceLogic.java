/* Author: Alex Erwin
 * Purpose: This is the testing logic for the game gui and views
 */
// package definition
package tests;
// import classes
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import org.junit.Test;

import controller.WumpusGUI;
import model.WumpusGame;
import view.GraphicView;
import view.GridView;
// test suite for the Wumpus Interface's Logic
public class WumpusInterfaceLogic {
	// calol the graphic view
	@Test
	public void testGraphicView() {
		// construct a new game
		WumpusGame wg = new WumpusGame(true);	
		// get a new view
		GraphicView g = new GraphicView(wg, 800, 1000);
		// call update on the view with false
		g.update(wg, false);		
		// set the game to over
		wg.checkState(true);
		// call update again
		g.update(wg, true);		
	}
	// call the gridview
	@Test
	public void testGridView() {
		// construct a new game
		WumpusGame wg = new WumpusGame(true);	
		// get a new view
		GridView g = new GridView(wg, 800, 1000);
		// call update on the view with false
		g.update(wg, false);		
		// set the game to over
		wg.checkState(true);
		// call update again
		g.update(wg, true);
	}
	// call the constructor on the GUI, and the main method to test
	@Test
	public void testGUI() {
		// construct
		WumpusGUI g = new WumpusGUI();
		// call main
		WumpusGUI.main(null);	
	}
}