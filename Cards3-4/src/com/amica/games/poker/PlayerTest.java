package com.amica.games.poker;

import static com.amica.games.CardFormatter.*;

import java.util.ArrayList;
import java.util.List;

import com.amica.games.Card;
import com.amica.games.Card.Spot;
import com.amica.games.Card.Suit;
import com.amica.games.poker.Player.Rank;

/**
 * Test for the {@link Player} class.
 *
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
	 * Helper to deal prepared cards to the player and check that it
	 * reports the expected rank. 
	 */
	public static boolean testRanking(List<Card> deck, Rank expectedInitialRank) {
	
		List<Card> initialHand = deck.subList(0,5);
		System.out.print("Testing ranking of");
		for (Card card : initialHand) {
			System.out.print(" " + abbreviationOf(card));
		}
		System.out.println();
			
		boolean passed = true;
		Player hand = new Player(initialHand);
		
		if (hand.getRank() != expectedInitialRank) {
			System.out.format("    Initial rank should be %s but is %s.%n",
					expectedInitialRank, hand.getRank());
			passed = false;
		}
		
		System.out.println();
		return passed;
	}
	
	/**
	 * Helper to deal prepared cards to the player and check that it
	 * reports the expected rank, exchanges the right cards, and reports again. 
	 */
	public static boolean testPlay(List<Card> deck, Rank expectedInitialRank,
			int expectedDraw, Rank expectedFinalRank) {
			
		List<Card> initialHand = deck.subList(0,5);
		System.out.print("Testing play of");
		for (Card card : initialHand) {
			System.out.print(" " + abbreviationOf(card));
		}
		System.out.println();
			
		boolean passed = true;
		Player hand = new Player(initialHand);
		
		if (hand.getRank() != expectedInitialRank) {
			System.out.format("    Initial rank should be %s but is %s.%n",
					expectedInitialRank, hand.getRank());
			passed = false;
		}
		
		int draw = hand.getNumberToDraw();
		if (draw != expectedDraw) {
			System.out.format("    Draw should be %d cards but is %d.%n",
					expectedDraw, draw);
			passed = false;
		}
		
		List<Card> cardsDrawn = deck.subList(5, 5 + draw);
		System.out.print("    Drawing");
		for (Card card : cardsDrawn) {
			System.out.print(" " + abbreviationOf(card));
		}
		System.out.println();

		hand.draw(cardsDrawn);
		if (hand.getRank() != expectedFinalRank) {
			System.out.format("    Final rank should be %s but is %s.%n",
					expectedFinalRank, hand.getRank());
			passed = false;
		}
		
		System.out.println();
		return passed;
		
	}
	
	public static boolean testNoPair() {
		return testRanking(deckOf(
				new Card(Suit.HEARTS, Spot._8),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._6),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._7),
				new Card(Suit.HEARTS, Spot._9)
			), Rank.NO_PAIR);
	}
	
	public static boolean testOnePair() {
		return testRanking(deckOf(
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._7)
			), Rank.ONE_PAIR);
	}
	
	public static boolean testTwoPair() {
		return testRanking(deckOf(
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._2)
			), Rank.TWO_PAIR);
	}
	
	public static boolean testThreeOfAKind() {
		return testRanking(deckOf(
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._5)
			), Rank.THREE_OF_A_KIND);
	}
	
	public static boolean testStraight() {
		return testRanking(deckOf(
				new Card(Suit.HEARTS, Spot._8),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._6),
				new Card(Suit.HEARTS, Spot._4),
				new Card(Suit.SPADES, Spot._7)
			), Rank.STRAIGHT);
	}
	
	public static boolean testFlush() {
		return testRanking(deckOf(
				new Card(Suit.SPADES, Spot._2),
				new Card(Suit.SPADES, Spot._5),
				new Card(Suit.SPADES, Spot.QUEEN),
				new Card(Suit.SPADES, Spot._10),
				new Card(Suit.SPADES, Spot._7)
			), Rank.FLUSH);
	}
	
	public static boolean testFullHouse() {
		return testRanking(deckOf(
				new Card(Suit.CLUBS, Spot.ACE),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.ACE),
				new Card(Suit.SPADES, Spot._5)
			), Rank.FULL_HOUSE);
	}
	
	public static boolean testFourOfAKind() {
		return testRanking(deckOf(
				new Card(Suit.CLUBS, Spot._5),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._5)
			), Rank.FOUR_OF_A_KIND);
	}
	
	public static boolean testStraightFlush() {
		return testRanking(deckOf(
				new Card(Suit.SPADES, Spot._8),
				new Card(Suit.SPADES, Spot._5),
				new Card(Suit.SPADES, Spot._6),
				new Card(Suit.SPADES, Spot._4),
				new Card(Suit.SPADES, Spot._7)
			), Rank.STRAIGHT_FLUSH);
	}
	
	public static boolean testOnePairToThreeOfAKind() {
		return testPlay(deckOf(
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._7),
				new Card(Suit.HEARTS, Spot._9),
				new Card(Suit.CLUBS, Spot._5),
				new Card(Suit.SPADES, Spot._3)
			), Rank.ONE_PAIR, 3, Rank.THREE_OF_A_KIND);
	}
	
	public static boolean testTwoPairToFullHouse() {
		return testPlay(deckOf(
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._5),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._2),
				new Card(Suit.CLUBS, Spot._2),
				new Card(Suit.CLUBS, Spot._5)
			), Rank.TWO_PAIR, 1, Rank.FULL_HOUSE);
	}
	
	public static boolean testPossibleStraight() {
		return testPlay(deckOf(
				new Card(Suit.HEARTS, Spot._8),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot._6),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._7),
				new Card(Suit.HEARTS, Spot._9)
			), Rank.NO_PAIR, 1, Rank.STRAIGHT);
	}
	
	public static boolean testPossibleFlush() {
		return testPlay(deckOf(
				new Card(Suit.HEARTS, Spot._8),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.DIAMONDS, Spot.QUEEN),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._7),
				new Card(Suit.HEARTS, Spot._2),
				new Card(Suit.HEARTS, Spot._9)
			), Rank.NO_PAIR, 2, Rank.FLUSH);
	}
	
	public static boolean testFlushOverStraight() {
		return testPlay(deckOf(
				new Card(Suit.HEARTS, Spot._8),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.HEARTS, Spot._6),
				new Card(Suit.HEARTS, Spot.KING),
				new Card(Suit.SPADES, Spot._7),
				new Card(Suit.HEARTS, Spot._2)
			), Rank.NO_PAIR, 1, Rank.FLUSH);
	}
	
	public static boolean testStraightOverFlush() {
		return testPlay(deckOf(
				new Card(Suit.HEARTS, Spot._8),
				new Card(Suit.HEARTS, Spot._5),
				new Card(Suit.HEARTS, Spot._6),
				new Card(Suit.CLUBS, Spot.KING),
				new Card(Suit.SPADES, Spot._7),
				new Card(Suit.HEARTS, Spot._9)
			), Rank.NO_PAIR, 1, Rank.STRAIGHT);
	}
	
	/**
	 * Runs a battery of tests and summarizes results to the console.
	 */
	public static void main(String[] args) {
		
		boolean allPassed = true;

		allPassed &= testNoPair();		
		allPassed &= testOnePair();		
		allPassed &= testTwoPair();		
		allPassed &= testThreeOfAKind();		
		allPassed &= testStraight();		
		allPassed &= testFlush();
		allPassed &= testFullHouse();		
		allPassed &= testFourOfAKind();		
		allPassed &= testStraightFlush();		
		
		allPassed &= testOnePairToThreeOfAKind();
		allPassed &= testTwoPairToFullHouse();
		allPassed &= testPossibleStraight();
		allPassed &= testPossibleFlush();
		allPassed &= testFlushOverStraight();
		allPassed &= testStraightOverFlush();
		
		System.out.println();
		System.out.println(allPassed ? "ALL TESTS PASSED." : "SOME TESTS FAILED.");
	}
}
