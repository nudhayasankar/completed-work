package com.amica.games.blackjack;

import com.amica.games.Card;

/**
 * Models a player at the blackjack table, with a "bank" of chips
 * and the ability to create new {@link PlayerHand} objects in order to 
 * start new games.
 *
 * @author Will Provost
 */
public class Player {
	
	public static final int DEFAULT_STAKE = 10000;
	public static final int DEFAULT_WAGER = 10;

	private int bank;

	/**
	 * Create a player with the default "take" as its starting bank.
	 */
	public Player() {
		this(DEFAULT_STAKE);
	}
	
	/**
	 * Create a player with the given "take" as its starting bank.
	 */
	public Player(int stake) {
		bank = stake;
	}
	
	/**
	 * Accessor for the current bank, or count of chips.
	 */
	public int getBank() {
		return bank;
	}

	/**
	 * Play a round by creating a hand. From this point forward the 
	 * {@link PlayerHand} object makes decisions, and ultimately reports
	 * back tot he player by calling {@link #winOrLose(int)} with results.
	 */
	public PlayerHand play(Card firstCard, Card dealerCard) {
		return new PlayerHand(this, DEFAULT_WAGER, firstCard, dealerCard);
	}
	
	/**
	 * Add or deduct from our bank.
	 */
	public void winOrLose(int gainOrLoss) {
		bank += gainOrLoss;
	}
}
