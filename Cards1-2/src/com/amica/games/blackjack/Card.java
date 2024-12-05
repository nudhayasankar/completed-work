package com.amica.games.blackjack;

public class Card {
    public enum SUIT { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum SPOT {
        _2(2), _3(3), _4(4), _5(5), _6(6), _7(7), _8(8),
        _9(9), _10(10), JACK(10), QUEEN(10), KING(10), ACE(11);

        private int value;

        private SPOT(final int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }

    }

    private SUIT suit;
    private SPOT spot;

    public Card(SUIT suit, SPOT spot){
        this.suit = suit;
        this.spot = spot;
    }

    public SUIT getSuit() {
        return suit;
    }

    public SPOT getSpot() {
        return spot;
    }
}
