package treasure;

import static treasure.Island.Contents.COIN;
import static treasure.Island.Contents.EMPTY;
import static treasure.Island.Contents.TREASURE;
import static treasure.Island.Contents.WIZARD;

import treasure.Island.Contents;

/**
 * Application that drives the Island and Game APIs through a simple example
 * game. Two players work as a team, starting from opposite corners of the
 * island. They play by the rules, but with perfect foreknowledge of what
 * they will find at what spots.
 * 
 * @author Will Provost
 */
public class ExampleGame {
	
	public static Island island;
	public static Game game;
	
	public static int turn = 0;
	
	/**
	 * Put a player at each corner, and show the contents of those two spaces.
	 */
	public static void setUp() {
		game.playerEnters(0, 0); // to show the player icon
		game.showContents(0, 0); // to show the square's contents (empty in this case)
		game.playerEnters(2, 3);
		game.showContents(2, 3); // in this case, shows a coin
	}
	
	/**
	 * We script out actions on each of six turns.
	 */
	public static boolean playOneTurn() {
		++turn;
		switch(turn) {
		
			/*
			 * Player one moves toward one of the coins.
			 * Player two is already at a spot with a coin, so he takes it,
			 * and then moves toward the treasure.
			 */
			case 1:
				game.playerLeaves(0, 0); // to remove the player icon
				game.playerEnters(0, 1); // to show the icon in a new square
				game.showContents(0, 1);
				
				island.takeCoin(2, 3); // at the logic level, take the coin
				game.showContents(2, 3); // at the UI level, update the visible square
				game.showMessage("Found a coin."); // show a message in the UI as well
				
				game.playerLeaves(2, 3);
				game.playerEnters(2, 2);
				game.showContents(2, 2);
				return true;
				
			/*
			 * Player one gets a coin and moves to another one.
			 * Player two moves toward the treasure.
			 */
			case 2:
				island.takeCoin(0, 1);
				game.showContents(0, 1);
				game.showMessage("Found a coin.");
				
				game.playerLeaves(0, 1);
				game.playerEnters(1, 1);
				game.showContents(1, 1);
				
				game.playerLeaves(2, 2);
				game.playerEnters(1, 2);
				game.showContents(1, 2);
				return true;
			
			/*
			 * Player one now has enough coins to pay the wizard,
			 * so starts moving toward the wizard.
			 * Player two reaches the treasure and sits still.
			 */
			case 3:
				island.takeCoin(1, 1);
				game.showContents(1,  1);
				game.showMessage("Found a coin.");
				
				game.playerLeaves(1, 1);
				game.playerEnters(0, 1);
				game.showContents(0, 1);
				return true;
			
			/*
			 * Player one moves toward the wizard.
			 * Player two is idle.
			 */
			case 4:
				game.playerLeaves(0, 1);
				game.playerEnters(0, 2);
				game.showContents(0, 2);
				return true;
				
				/*
				 * Player one reaches the wizard.
				 * Player two is idle.
				 */
			case 5:
				game.playerLeaves(0, 2);
				game.playerEnters(0, 3);
				game.showContents(0, 3);
				return true;
			
				/*
				 * Player one pays the wizard for the magic spell.
				 * Player two can now use the spell to claim the treasure.
				 */
			case 6:
				String spell = island.payWizard(0, 3, 2);
				island.claimTreasure(1, 2, spell);
				game.showMessage("Claimed the treasure!");
				return false;
		}
		
		throw new IllegalStateException("Something went wrong; we should be done.");
	}
	
	/**
	 * Set up a small island with hard-coded contents.
	 * Call the setup function to put players on the island.
	 * Then tell the game to set a timer and to call the playing function
	 * once per second. 
	 */
	public static void main(String[] args) {
		
		Contents[][] space = {
				{ EMPTY, COIN,  EMPTY,    WIZARD },
				{ EMPTY, COIN,  TREASURE, EMPTY  },
				{ EMPTY, EMPTY, EMPTY,    COIN   }
			};
		island = new Island(space, 2, "Toil and trouble");
		game = new Game(island);
		
		setUp();
		game.onEachTurnCall(ExampleGame::playOneTurn);
	}
}

