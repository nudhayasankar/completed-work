package com.amica.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amica.games.Card.Spot;
import com.amica.games.Card.Suit;

/**
 * A deck of 52 {@link Card}s which are randomly shuffled and
 * which then can be dealt out, one at a time.
 *
 * @author wprovost
 */
public class Deck
{
    protected List<Card> cards;

    /**
     * Fill a list with one of each possible card suit-spot combination.
     * Then randomly "suffle the deck." Set an iterator to the start of
     * the list, such that "dealing" is just taking the next card in the
     * iteration.
     */
    public Deck() {
    	cards = new ArrayList<>(Suit.values().length * Spot.values().length);
        for (Suit suit : Suit.values()) {
            for (Spot spot : Spot.values()) {
                cards.add(new Card(suit, spot));
            }
        }
        shuffle();
    }
    
    /**
     * Shuffle the deck randomly.
     */
    public void shuffle() {
    	Collections.shuffle(cards);
    }


    /**
     * Remove the top card from the deck, and hand it over.
     */
    public Card deal() {
    	return cards.remove(0);
    }

    /**
     * Remove N cards from the top of the deck and return them in a list.
     */
    public List<Card> deal(int howMany) {
    	List<Card> results = new ArrayList<>(howMany);
    	for (int i = 0; i < howMany; ++i) {
    		results.add(deal());
    	}
    	
    	return results;
    }
    
    /**
     * Returns the number of cards left.
     */
    public int cardsLeft() {
    	return cards.size();
    }
    
    /**
     * Lets the caller know if there is still at least one card in the deck.
     */
    public boolean isCardAvailable() {
    	return !cards.isEmpty();
    }
}
