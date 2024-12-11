package com.amica.games.bridge;

import static com.amica.games.CardFormatter.*;

import java.util.ArrayList;
import java.util.List;

import com.amica.games.Card;
import com.amica.games.Card.Spot;
import com.amica.games.Card.Suit;

/**
 * Test for the {@link Player} class.
 *
 *@author Will Provost
 */
public class PlayerTest {

	/**
	 * Prepare a list of cards as the deck from which to deal to the player. 
	 */
	public static List<Card> deckOf(Card... cards) {
		List<Card> result = new ArrayList<>();
		for (Card card : cards) {
			result.add(card);
		}
		return result;
	}
	
	/**
	 * Prepare a {@link Trick} containing the given cards. 
	 */
	public static Trick trickOf(Card... cards) {
		Trick result = new Trick();
		for (Card card : cards) {
			result.add(card);
		}
		return result;
	}
	
	/**
	 * Helper that sets up a hand with the given cards, asks it to play on 
	 * a given trick, and checks that it plays the expected card. 
	 */
	public static boolean testPlay(List<Card> hand, Trick trick, Card expected) {
		
		Player player = new Player();
		for (Card card : hand) {
			player.acceptCard(card);
		}
		Card played = player.play(trick);
		boolean passed = played.equals(expected);
		if (!passed) {
			System.out.format("    Expected the %s, but played the %s.%n",
					nameOf(expected), nameOf(played));
		}
		
		System.out.println();
		return passed;
	}
	
	public static boolean testLeadFromLongestAndStringest() {
		System.out.println("Test leading from longest and strongest suit ...");
		return testPlay(deckOf(
				new Card(Suit.CLUBS, Spot._3),
				new Card(Suit.CLUBS, Spot._7),
				new Card(Suit.CLUBS, Spot.JACK),
				new Card(Suit.CLUBS, Spot.QUEEN),
				new Card(Suit.CLUBS, Spot.KING),
				new Card(Suit.DIAMONDS, Spot._8),
				new Card(Suit.DIAMONDS, Spot._10),
				new Card(Suit.DIAMONDS, Spot.JACK),
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot.ACE),
				new Card(Suit.SPADES, Spot._4),
				new Card(Suit.SPADES, Spot._5),
				new Card(Suit.SPADES, Spot.QUEEN)
			), new Trick(), new Card(Suit.CLUBS, Spot.KING));
	}
	
	public static boolean testFollowSuitAndWin() {
		System.out.println("Test following suit, winning the trick ...");
		return testPlay(deckOf(
				new Card(Suit.CLUBS, Spot._3),
				new Card(Suit.CLUBS, Spot._7),
				new Card(Suit.CLUBS, Spot.JACK),
				new Card(Suit.CLUBS, Spot.QUEEN),
				new Card(Suit.CLUBS, Spot.KING),
				new Card(Suit.DIAMONDS, Spot._8),
				new Card(Suit.DIAMONDS, Spot._10),
				new Card(Suit.DIAMONDS, Spot.JACK),
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot.ACE),
				new Card(Suit.SPADES, Spot._4),
				new Card(Suit.SPADES, Spot._5),
				new Card(Suit.SPADES, Spot.QUEEN)
			), trickOf(new Card(Suit.DIAMONDS, Spot._9)), 
				new Card(Suit.DIAMONDS, Spot.JACK));
	}
	
	public static boolean testFollowSuitAndPlayingLow() {
		System.out.println("Test following suit, playing low ...");
		return testPlay(deckOf(
				new Card(Suit.CLUBS, Spot._3),
				new Card(Suit.CLUBS, Spot._7),
				new Card(Suit.CLUBS, Spot.JACK),
				new Card(Suit.CLUBS, Spot.QUEEN),
				new Card(Suit.CLUBS, Spot.KING),
				new Card(Suit.DIAMONDS, Spot._8),
				new Card(Suit.DIAMONDS, Spot._10),
				new Card(Suit.DIAMONDS, Spot.JACK),
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot.ACE),
				new Card(Suit.SPADES, Spot._4),
				new Card(Suit.SPADES, Spot._5),
				new Card(Suit.SPADES, Spot.QUEEN)
			), trickOf(new Card(Suit.DIAMONDS, Spot.KING)), 
				new Card(Suit.DIAMONDS, Spot._8));
	}

	public static boolean testPlayingLowWhenVoid() {
		System.out.println("Test playing low when void ...");
		return testPlay(deckOf(
				new Card(Suit.CLUBS, Spot._3),
				new Card(Suit.CLUBS, Spot._7),
				new Card(Suit.CLUBS, Spot.JACK),
				new Card(Suit.CLUBS, Spot.QUEEN),
				new Card(Suit.CLUBS, Spot.KING),
				new Card(Suit.DIAMONDS, Spot._8),
				new Card(Suit.DIAMONDS, Spot._10),
				new Card(Suit.DIAMONDS, Spot.JACK),
				new Card(Suit.SPADES, Spot._4),
				new Card(Suit.SPADES, Spot._5),
				new Card(Suit.SPADES, Spot.QUEEN)
			), trickOf(new Card(Suit.HEARTS, Spot.KING)), 
				new Card(Suit.CLUBS, Spot._3));
	}
	
	/**
	 * Runs a battery of tests and summarizes results to the console.
	 */
	public static void main(String[] args) {
		
		boolean allPassed = true;
		
		allPassed &= testLeadFromLongestAndStringest();
		allPassed &= testFollowSuitAndWin();
		allPassed &= testFollowSuitAndPlayingLow();
		allPassed &= testPlayingLowWhenVoid();
				
		System.out.println();
		System.out.println(allPassed ? "ALL TESTS PASSED." : "SOME TESTS FAILED.");
	}
}
