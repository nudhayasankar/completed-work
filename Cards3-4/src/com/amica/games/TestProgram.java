package com.amica.games;

import com.amica.games.bridge.Player;

import java.util.List;

public class TestProgram {
    public static void main(String[] args){
        Card c1 = new Card(Card.Suit.CLUBS, Card.Spot.KING);
        Card c2 = new Card(Card.Suit.SPADES, Card.Spot.QUEEN);

        System.out.println(c1.hashCode());
        System.out.println(c2.hashCode());
//        System.out.println(c3.compareTo(c2));
//        System.out.println(c1.compareTo(c3));

//        Player player = new Player();
//        Deck deck = new Deck();
//        List<Card> dealtCards = deck.deal(13);
//        dealtCards.forEach(card -> {
//            player.acceptCard(card);
//        });
//
//        System.out.println(player.toString());
    }
}
