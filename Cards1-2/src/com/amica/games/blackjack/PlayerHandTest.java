package com.amica.games.blackjack;

import com.amica.games.blackjack.Card;
import com.amica.games.blackjack.Card.SPOT;
import com.amica.games.blackjack.Card.SUIT;
import com.amica.games.blackjack.Blackjack.Action;

/**
 * Test for the {@link PlayerHand} class.
 *
 * @author Will Provost
 *
 */
public class PlayerHandTest {

	public static Player player = new Player();
	
	/**
	 * Helper/factory for a card of a given spot value.
	 */
	public static Card spot(int spot) {
		return spot(SPOT.values()[spot - 2]);
	}
	
	/**
	 * Helper/factory for a card of a given spot value.
	 */
	public static Card spot(SPOT spot) {
		return new Card(SUIT.HEARTS, spot);
	}

	/**
	 * Helper methjod to build a new {@link PlayerHand} from the given cards.
	 */
	public static PlayerHand buildHand(Card dealerCard,
			Card card1, Card card2, Card... cards) {
		
		PlayerHand hand = player.play(card1, spot(2));
		hand.secondCard(card2);
		for (Card card : cards) {
			hand.hit(card);;
		}
		return hand;
	}
	
	/**
	 * Helper method that builds a hand from the given cards and checks that
	 * it correctly evaluates points. 
	 */
	public static boolean testPoints(int points, 
			Card card1, Card card2, Card... cards) {
		
		System.out.format("Testing for %d points ...%n", points);
		PlayerHand hand = buildHand(spot(2), card1, card2, cards);
		boolean passed = hand.getPoints() == points;
		if (!passed) {
			System.out.format("    Expected %d points, but was %d.%n",
					points, hand.getPoints());
		}
		
		System.out.println();
		return passed;
	}
	
	/**
	 * Helper method to create a hand from the given cards and 
	 * check that it correctly evaluates bust / not bust.
	 */
	public static boolean testBust(boolean bust, 
			Card card1, Card card2, Card... cards) {
		
		System.out.format("Testing for %s ...%n", bust ? "bust" : "not bust");
		PlayerHand hand = buildHand(spot(2), card1, card2, cards);
		boolean passed = hand.isBust() == bust;
		if (!passed) {
			System.out.format("    Expected %s.%n", bust ? "bust" : "not bust");
		}
		
		System.out.println();
		return passed;
	}
	
	/**
	 * Helper method to create a hand from the given cards and 
	 * check that it correctly evaluates blackjack / not blackjack.
	 */
	public static boolean testBlackjack(boolean blackjack, Card card1, Card card2) {
		System.out.println("Testing for blackjack ...");
		PlayerHand hand = player.play(card1, spot(2));
		Action action = hand.secondCard(card2);
		if (hand.isBlackjack() != blackjack) {
			System.out.format("    Expected %sto be a blackjack.%n",
					blackjack ? "" : "not ");
		} else if (action != Action.NONE) {
			System.out.format("    Expected no action, but was %s.%n", action);
		} else {
			System.out.println();
			return true;
		}
			
		System.out.println();
		return false;
	}
		
	/**
	 * Helper that builds a hand and continues to hit with each new card.
	 * The hand implementation should want each card, and say no after the
	 * final one. 
	 */
	public static boolean testHits(Card dealerCard, 
			Card card1, Card card2, Card... cards) {
		PlayerHand hand = player.play(card1, dealerCard);
		Action action = hand.secondCard(card2);
		boolean passed = action == Action.NONE;
		if (passed) {
			for (Card card : cards) {
				passed &= hand.shouldHit();
				if (passed) {
					hand.hit(card);
				} else {
					System.out.format("    Expected to hit on %s.%n", hand);
				}
			}
			passed &= !hand.shouldHit();
			if (!passed) {
				System.out.format("    Expected to stand on %s.%n", hand);
			}
		} else {
			System.out.format("    Expected no action, but was %s.%n", action);
		}
		System.out.println();
		return passed;
	}
		
	/**
	 * Helper that sets up a hand with its initial deal, and checks that
	 * the hand chooses the expected action.
	 */
	public static boolean testAction(Card card1, Card card2, 
			Card dealerCard, Action expectedAction) {
		PlayerHand hand = player.play(card1, dealerCard);
		Action actualAction = hand.secondCard(card2);
		boolean passed = actualAction == expectedAction;
		if (!passed) {
			System.out.format("    Expected %s, but was %s.%n",
					expectedAction, actualAction);
		}
		System.out.println();
		return passed;
	}
	
	public static boolean testHitOn9() {
		System.out.println("Testing hit up to 9 points, dealer showing a 6 ...");
		return testHits(spot(6), spot(2), spot(7), spot(7));
	}
	
	public static boolean testHitOn11() {
		System.out.println("Testing hit up to 11 points, dealer showing a 6 ...");
		return testHits(spot(6), spot(4), spot(2), spot(5), spot(SPOT.ACE));
	}

	public static boolean testHitOn16() {
		System.out.println("Testing hit up to 16 points, dealer showing a 6 ...");
		return testHits(spot(8), spot(2), spot(7), spot(7), spot(3));
	}
	
	public static boolean testHitOn15() {
		System.out.println("Testing hit up to 15 points, dealer showing a 6 ...");
		return testHits(spot(8), spot(4), spot(2), spot(5), spot(SPOT.ACE), spot(3), spot(SPOT.QUEEN));
	}

	public static boolean testDoubleOn10() {
		System.out.println("Testing double on 10 ...");
		return testAction(spot(3), spot(7), spot(2), Action.DOUBLE);
	}
	
	public static boolean testDoubleOn11() {
		System.out.println("Testing double on 11 ...");
		return testAction(spot(4), spot(7), spot(2), Action.DOUBLE);
	}
	
	public static boolean testSurrenderOn16() {
		System.out.println("Testing surrender on a 16 when dealder shows 9 ...");
		return testAction(spot(9), spot(7), spot(9), Action.SURRENDER);
	}
	
	public static boolean testDontSurrenderOn16() {
		System.out.println("Testing no surrender on a 16 when dealder shows 8 ...");
		return testAction(spot(9), spot(7), spot(8), Action.NONE);
	}
	
	public static boolean testSplitOn2s() {
		System.out.println("Testing split on a pair of 2s ...");
		return testAction(spot(2), spot(2), spot(8), Action.SPLIT);
	}
	
	public static boolean testSplitOn9s() {
		System.out.println("Testing split on a pair of 9s ...");
		return testAction(spot(9), spot(9), spot(8), Action.SPLIT);
	}
	
	public static boolean testDontSplitOnKings() {
		System.out.println("Testing no split on a pair of kings ...");
		return testAction(spot(SPOT.KING), spot(SPOT.KING), spot(8), Action.NONE);
	}
	
	public static boolean testSplitOnAces() {
		System.out.println("Testing split on a pair of aces ...");
		return testAction(spot(SPOT.ACE), spot(SPOT.ACE), spot(8), Action.SPLIT);
	}

	/**
	 * Runs a battery of tests and summarizes results to the console.
	 */
	public static void main(String[] args) {
		
		boolean allPassed = true;
		
		allPassed &= testPoints(9, spot(3), spot(6));
		allPassed &= testPoints(13, spot(3), spot(SPOT.QUEEN));
		allPassed &= testPoints(14, spot(3), spot(SPOT.ACE));
		allPassed &= testPoints(12, spot(SPOT.ACE), spot(SPOT.ACE));
		allPassed &= testPoints(21, spot(SPOT.ACE), spot(SPOT.ACE), spot(9));
		allPassed &= testPoints(12, spot(SPOT.ACE), spot(SPOT.JACK), spot(SPOT.ACE));
		allPassed &= testBust(false, spot(SPOT.ACE), spot(SPOT.ACE));
		allPassed &= testBust(true, spot(SPOT.KING), spot(3), spot(9));
		allPassed &= testBlackjack(true, spot(10), spot(SPOT.ACE));
		allPassed &= testBlackjack(false, spot(SPOT.ACE), spot(8));
		allPassed &= testHitOn9();
		allPassed &= testHitOn11();
		allPassed &= testDoubleOn10();
		allPassed &= testDoubleOn11();
		allPassed &= testSurrenderOn16();
		allPassed &= testDontSurrenderOn16();
		allPassed &= testSplitOn2s();
		allPassed &= testSplitOn9s();
		allPassed &= testDontSplitOnKings();
		allPassed &= testSplitOnAces();
		
		System.out.println();
		System.out.println(allPassed ? "ALL TESTS PASSED." : "SOME TESTS FAILED.");
	}
}

