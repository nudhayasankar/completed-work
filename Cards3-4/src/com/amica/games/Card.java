package com.amica.games;

/**
 * Represents a single playing card from a traditional poker or bridge deck.
 *
 * @author Will Provost
 */
public class Card implements Comparable<Card> {
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
	public enum Spot 
			{ _2, _3, _4, _5, _6, _7, _8, _9, _10, JACK, QUEEN, KING, ACE }
	
    private Suit suit;
    private Spot spot;

    /**
     * Create a card by providing its suit and "spot" value,
     * where 1 represents the ace, 11 the jack, etc.
     */
    public Card (Suit suit, Spot spot) {
        this.suit = suit;
        this.spot = spot;
    }

    /**
     * Accessor for the suit.
     */
    public Suit getSuit() {
    	return suit;
    }

    /**
     * Accessor for the spot.
     */
    public Spot getSpot() {
    	return spot;
    }

    /**
     * String representation is the {@link #getSpotName spot name} and suit.
     */
    @Override
    public String toString() {
        return String.format ("%s of %s", spot.toString(), suit.toString());
    }

    private int getOrdinal() {
        return (this.suit.ordinal() * 13) + this.spot.ordinal();
    }

    @Override
    public boolean equals(Object card){
        if(!(card instanceof Card)){
            return false;
        }
        return this.getOrdinal() == ((Card) card).getOrdinal();
    }

    @Override
    public int hashCode(){
        return this.getOrdinal();
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.getOrdinal(), o.getOrdinal());
    }

}


