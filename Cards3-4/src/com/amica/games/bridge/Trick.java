package com.amica.games.bridge;

import java.util.ArrayList;

import com.amica.games.Card;
import com.amica.games.Card.Suit;

/**
 * Encapsulates a "trick" in the game of contract bridge.
 * This is an ordered list of up to 4 cards, and it can evaluate who
 * wins the trick, which is the player who plays the highest card in
 * the suit that was led. (We only consider no-trump play in this example!)
 *
 * @author Will Provost
 *
 */
public class Trick extends ArrayList<Card> {

	/**
	 * Evaluate the winner of the trick -- this could be for a complete trick,
	 * or of the trick as it stands partially played.
	 *
	 * @return The position of the winning card -- e.g. 0 means the player
	 * who led the trick, 1 is the player after that, up to 3 for a completed trick.
	 */
	public int getWinner() {
		Suit suit = get(0).getSuit();
		int highest = -1;
		int winner = 0;
		for (int position = 0; position < size(); ++position) {
			Card card = get(position);
			if (card.getSuit() == suit && card.getSpot().ordinal() > highest) {
				highest = card.getSpot().ordinal();
				winner = position;
			}
		}

		return winner;
	}

	/**
	 * Like {@link #getWinner getWinner} but returns the winning card rather than
	 * the index of the player who played it.
	 */
	public Card getWinningCard() {
		return get(getWinner());
	}
}
