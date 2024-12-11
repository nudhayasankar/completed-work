package com.amica.games.poker;

import com.amica.games.Deck;

/**
 * Models a very simple, one-player game of draw poker: this is mostly
 * a test of the {@link Player} class.
 *
 */
public class Poker {

	public static void main(String[] args) {
		
		Deck deck = new Deck();
		
		Player hand = new Player(deck.deal(5));
		System.out.println("Deal: " + hand);
		System.out.println("Hand rank: " + hand.getRank());
		int numberToDraw = hand.getNumberToDraw();
		System.out.format("Drawing %d card(s).%n", numberToDraw);
		hand.draw(deck.deal(numberToDraw));
		System.out.println("Now: " + hand);
		System.out.println("Hand rank: " + hand.getRank());
	}
}
