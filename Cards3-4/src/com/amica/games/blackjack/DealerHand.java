package com.amica.games.blackjack;

import com.amica.games.Card;

/**
 * A blackjack hand that only evaluates points and hits on anything under 17.
 *
 * @author Will Provost
 */
public class DealerHand extends Hand {

	public DealerHand(Card card1, Card card2) {
		cards.add(card1);
		cards.add(card2);
	}
	
	public Card getFaceUpCard() {
		return cards.get(0);
	}
	
	@Override
	public boolean shouldHit() {
		return getPoints() < 17;
	}
}
