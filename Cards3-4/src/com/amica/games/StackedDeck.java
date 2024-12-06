package com.amica.games;

import java.util.List;

/**
 * A subclass of {@link com.amica.games.Deck} that replaces the deck contents
 * with a prepared list.
 * 
 * @author Will Provost
 */
public class StackedDeck extends Deck {

	public StackedDeck(List<Card> contents) {
		super();
		cards = contents;
	}
	
	/**
	 * Don't allow shuffling -- stick with the prepared list.
	 */
	@Override
	public void shuffle() {
	}
}
