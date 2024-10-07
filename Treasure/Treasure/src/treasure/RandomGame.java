package treasure;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Application that drives the Island and Game APIs through a random game.
 * You must fill in the setup and turn-playing logic so that you win the game,
 * even without knowing the island's layout in advance.
 * 
 * @author Will Provost
 */
public class RandomGame {

	/**
	 * Creates a random 6x6 island, and sets up the game viewer.
	 * You take it from there ...
	 */

	static int[][] boardStateP1;
	static int[][] boardStateP2;
	static int[] player1Coords;
	static int[] player2Coords;
	static int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
	static boolean p1StateReset = false;
	static int[] treasureCoord = new int[]{-1, -1};
	static boolean wizardPaid = false;
	static int coinsCollected = 0;
	static Queue<int[]> p1Next;
	static Queue<int[]> p2Next;

	/**
	 * Using the following states to share info between the two players
	 * 0 - square not searched
	 * 1- visited by P1
	 * 2 - visited by P2
	 * 3 - square has coin
	 * 4 - square has wizard
	 * 5 - square has treasure
	 * -1 - square is empty
	 *
	 * */
	public static void main(String[] args) {
		Island island = new Island();
		Game game = new Game(island);

		// Set up initial states
		setupGame(game, island);

		// Play each turn
		game.onEachTurnCall(() -> playOneTurn(island, game));
	}

	public static void setupGame(Game game, Island island){
		// player 1 enters the first cell
		player1Coords = new int[] {0, 0};
		p1Next = new LinkedList<>();
		p1Next.add(new int[] {0, 0});
		boardStateP1 = new int[island.getHeight()][island.getWidth()];
		// player 2 enters the last cell
		player2Coords = new int[] {island.getHeight() - 1, island.getWidth() - 1};
		p2Next = new LinkedList<>();
		p2Next.add(new int[] {island.getHeight() - 1, island.getWidth() - 1});
		boardStateP2 = new int[island.getHeight()][island.getWidth()];

	}

	public static boolean playOneTurn(Island island, Game game) {
		return playerAction(island, game);
	}

	public static boolean playerAction(Island island, Game game){
		if(coinsCollected < island.getCoinsForWizard()){
			game.playerLeaves(player1Coords[0], player1Coords[1]);
			collectCoins(island, game);
		}
		if(coinsCollected >= island.getCoinsForWizard() && !wizardPaid){
			if(!p1StateReset){
				p1Next = new LinkedList<>();
				p1Next.add(new int[]{player1Coords[0], player1Coords[1]});
				boardStateP1 = new int[island.getHeight()][island.getWidth()];
				p1StateReset = true;
			}
			game.playerLeaves(player1Coords[0], player1Coords[1]);
			findAndPayWizard(island, game);
		}
		if(treasureCoord[0] == -1 && treasureCoord[1] == -1){
			game.playerLeaves(player2Coords[0], player2Coords[1]);
			findTreasure(island, game);
		}
		if(wizardPaid){
			game.showMessage("Claimed the treasure!");
			return false;
		}
		return true;
	}

	public static void collectCoins(Island island, Game game){
		if(p1Next.peek() != null){
			int[] currCoord = p1Next.poll();
			int currRow = currCoord[0];
			int currCol = currCoord[1];
			player1Coords[0] = currRow;
			player1Coords[1] = currCol;
			boardStateP1[currRow][currCol] = 1;
			game.playerEnters(currRow, currCol);
			game.showContents(currRow, currCol);
			if(island.getContents(currRow, currCol) == Island.Contents.COIN){
				coinsCollected++;
				game.showMessage("Found a coin.");
			}
			for(int[] direction : directions){
				int nextRow = currRow + direction[0];
				int nextCol = currCol + direction[1];
				if(nextRow >= 0 && nextRow < island.getHeight()
						&& nextCol >= 0 && nextCol < island.getWidth()
						&& boardStateP1[nextRow][nextCol] == 0
						&& nextRow != treasureCoord[0] && nextCol != treasureCoord[1]){
					p1Next.add(new int[] {nextRow, nextCol});
				}
			}
		}
	}

	public static void findAndPayWizard(Island island, Game game){
		if(p1Next.peek() != null){
			int[] currCoord = p1Next.poll();
			int currRow = currCoord[0];
			int currCol = currCoord[1];
			player1Coords[0] = currRow;
			player1Coords[1] = currCol;
			boardStateP1[currRow][currCol] = 1;
			game.playerEnters(currRow, currCol);
			game.showContents(currRow, currCol);
			if(island.getContents(currRow, currCol) == Island.Contents.WIZARD){
				wizardPaid = true;
				return;
			}
			for(int[] direction : directions){
				int nextRow = currRow + direction[0];
				int nextCol = currCol + direction[1];
				if(nextRow >= 0 && nextRow < island.getHeight()
						&& nextCol >= 0 && nextCol < island.getWidth()
						&& boardStateP1[nextRow][nextCol] == 0
						&& nextRow != treasureCoord[0] && nextCol != treasureCoord[1]){
					p1Next.add(new int[] {nextRow, nextCol});
				}
			}
		}
	}

	public static void findTreasure(Island island, Game game){
		if(p2Next.peek() != null){
			int[] currCoord = p2Next.poll();
			int currRow = currCoord[0];
			int currCol = currCoord[1];
			player2Coords[0] = currRow;
			player2Coords[1] = currCol;
			boardStateP2[currRow][currCol] = 1;
			game.playerEnters(currRow, currCol);
			game.showContents(currRow, currCol);
			if(island.getContents(currRow, currCol) == Island.Contents.TREASURE){
				treasureCoord[0] = currRow;
				treasureCoord[1] = currCol;
				game.showMessage("Found the treasure!");
				return;
			}
			for(int[] direction : directions){
				int nextRow = currRow + direction[0];
				int nextCol = currCol + direction[1];
				if(nextRow >= 0 && nextRow < island.getHeight()
						&& nextCol >= 0 && nextCol < island.getWidth()
						&& boardStateP2[nextRow][nextCol] == 0){
					p2Next.add(new int[] {nextRow, nextCol});
				}
			}
		}
	}
}
