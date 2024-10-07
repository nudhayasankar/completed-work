package treasure;

import java.security.SecureRandom;
import java.util.Random;

/**
 * This is essentially the game board for the treasure-hunting game.
 * The island is a rectangular grid of squares. Each square can be
 * empty, or can contain only one of a coin, the wizard, or the treasure.
 * A player at a certain square can get the contents of that square,
 * and can collect coins when found; pay the wizard enough coins to 
 * learn the magic spell; or use the magic spell to claim the treasure.
 * 
 * @author Will Provost
 */
public class Island {

	public enum Contents { EMPTY, COIN, WIZARD, TREASURE };
	
	/**
	 * A simple set of row-column coordinates.
	 */
	public static class Coordinates {
		public int row;
		public int col;
		
		public Coordinates(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		@Override
		public boolean equals(Object other) {
			return other instanceof Coordinates &&
					((Coordinates) other).row == row &&
					((Coordinates) other).col == col;
		}
	}
	
	private Contents[][] space;
	private int coinsForWizard;
	private String magicSpell;
	
	/**
	 * Creates square island with coins, wizard, and treasure randomly
	 * distributed over the grid, and a randomly generated magic spell. 
	 */
	public Island() {
		coinsForWizard = 3;
		Random generator = new SecureRandom();
		
		final int size = 6;
		final int coins = 5;
		final int spellLength = 8;
		
		space = new Contents[size][];
		for (int row = 0; row < size; ++row) {
			space[row] = new Contents[size];
			for (int col = 0; col < size; ++col) {
				space[row][col] = Contents.EMPTY;
			}
		}
		boolean placedTreasure = false;
		boolean placedWizard = false;
		int coinsPlaced = 0;
		while (coinsPlaced != coins) {
			int row = generator.nextInt(size);
			int col = generator.nextInt(size);
			if (space[row][col] == Contents.EMPTY) {
				if (!placedTreasure) {
					space[row][col] = Contents.TREASURE;
					placedTreasure = true;
				} else if (!placedWizard) {
					space[row][col] = Contents.WIZARD;
					placedWizard = true;
				} else {
					space[row][col] = Contents.COIN;
					++coinsPlaced;
				}
			}
		}
		
		StringBuilder builder = new StringBuilder();
		for (int s = 0; s < spellLength; ++s) {
			builder.append((char) (generator.nextInt(26) + 65));
		}
		magicSpell = builder.toString();
	}
	
	/**
	 * Creates an island with the given grid and contents, fee 
	 * to pay the wizard, and magic spell.
	 */
	public Island(Contents[][] space, int coinsForWizard, String magicSpell) {
		this.space = space;
		this.coinsForWizard = coinsForWizard;
		this.magicSpell = magicSpell;
	}
	
	/**
	 * Accessor for the number of rows.
	 */
	public int getHeight() {
		return space.length;
	}

	/**
	 * Accessor for the number of columns.
	 */
	public int getWidth() {
		return space[0].length;
	}

	/**
	 * Accessor for the fee, or number of coins the wizard requires
	 * in order to divulge the magic spell.
	 */
	public int getCoinsForWizard() {
		return coinsForWizard;
	}
	
	/**
	 * Helper to do bounds checking on row-column coordinates.
	 */
	private void boundsCheck(int row, int col) {
		if (row < 0) {
			throw new IllegalArgumentException("Row too low");
		}
		if (row >= getHeight()) {
			throw new IllegalArgumentException("Row too high");
		}
		if (col < 0) {
			throw new IllegalArgumentException("Column too low");
		}
		if (col >= getWidth()) {
			throw new IllegalArgumentException("Column too high");
		}
	}
	
	/**
	 * Returns the contents of the indicated square. 
	 */
	public Contents getContents(int row, int col) {
		boundsCheck(row, col);
		return space[row][col];
	}
	
	/**
	 * "Gives" the caller the coin at the indicated square, 
	 * leaving the square empty. 
	 */
	public void takeCoin(int row, int col) {
		boundsCheck(row, col);
		if (space[row][col] != Contents.COIN) {
			throw new IllegalArgumentException(String.format
					("No coin to take at row=%d, col=%d", row, col));
		}
		space[row][col] = Contents.EMPTY;
	}
	
	/**
	 * Pays the wizard at the indicated square the given number of coins,
	 * and returns the magic spell to the caller. 
	 */
	public String payWizard(int row, int col, int coins) {
		boundsCheck(row, col);
		if (space[row][col] != Contents.WIZARD) {
			throw new IllegalArgumentException(String.format
					("No wizard to pay at row=%d, col=%d", row, col));
		}
		if (coins < coinsForWizard) {
			throw new IllegalArgumentException(String.format
					("No deal! You have %d coins but the wizard wants %d.", 
							coins, coinsForWizard));
		}
		return magicSpell;
	}
	
	/**
	 * Uses the given magic spell to open the treasure chest at the
	 * indicated square, thus winning the game. 
	 */
	public void claimTreasure(int row, int col, String spell) {
		boundsCheck(row, col);
		if (space[row][col] != Contents.TREASURE) {
			throw new IllegalArgumentException(String.format
					("No treasure to claim at row=%d, col=%d", row, col));
		}
		if (spell == null || !spell.equals(magicSpell)) {
			throw new IllegalArgumentException
					("Your magic spell did not work; no treasure for you!");
		}
		space[row][col] = Contents.EMPTY;
	}
}
