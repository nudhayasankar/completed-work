package com.amica.games.blackjack;

public class TestProgram {
    public static void main(String[] args){
//        Card card = new Card(Card.SUIT.DIAMONDS, Card.SPOT._2);
//        System.out.println(card.getSpot());
//        System.out.println(card.getSuit());
//        System.out.println(CardFormatter.nameOf(card));

        Deck deck = new Deck();
        System.out.println(String.format("Dealing %d cards", deck.cardsLeft()));
        while(deck.isCardAvailable()){
            Card dealtCard = deck.deal();
            System.out.println(String.format("Dealt Card - %s, Cards left - %d", CardFormatter.nameOf(dealtCard), deck.cardsLeft()));
        }
    }
}
