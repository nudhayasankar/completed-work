package com.amica.games.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

import com.amica.games.Card;
import com.amica.games.Card.Spot;
import com.amica.games.Card.Suit;
import com.amica.games.Deck;
import com.amica.games.StackedDeck;
import com.amica.games.blackjack.Blackjack.Outcome;

/**
 * Test for the {@link Blackjack} class.
 * 
 * @author Will Provost
 */
public class BlackjackTest {

	/**
	 * Helper/factory for a card of a given spot value.
	 */
	public static Card spot(int spot) {
		return spot(Spot.values()[spot - 2]);
	}
	
	/**
	 * Helper/factory for a card of a given spot value.
	 */
	public static Card spot(Spot spot) {
		return new Card(Suit.HEARTS, spot);
	}

	/**
	 * Factory for stacked decks.
	 */
	public static class Stacker implements Supplier<Deck> {
		
		private Card[] cards;
		
		public Stacker(Card... cards) {
			this.cards = cards;
		}
		
		public Deck get() {
			List<Card> sequence = new ArrayList<>();
			for (Card card : cards) {
				sequence.add(card);
			}
			return new StackedDeck(sequence);
		}
	}
	
	/**
	 * Helper method that stacks a deck with the given card sequence,
	 * runs a round, and checks that the outcome was as expected and the
	 * player won or lost the right amount of money.
	 */
	public static boolean testSingleOutcome(Outcome outcome, Card... cards) {
		
		Blackjack game = new Blackjack(Level.FINE);
		Player player = game.getPlayer();
		int start = player.getBank();
		
		game.setDeckFactory(new Stacker(cards));
		List<Outcome> outcomes = game.playARound();
		boolean passed = outcomes.size() == 1;
		if (passed) {
			passed &= outcomes.get(0) == outcome;
			if (passed) {
				int expectedWinnings = 
						(int) (Player.DEFAULT_WAGER * outcome.getMultiplier());
				int actualWinnings = player.getBank() - start;
				if (actualWinnings != expectedWinnings) {
					System.out.format("    Expected win/lss of %d, but was %d.%n",
							expectedWinnings, actualWinnings);
					passed = false;
				}
			} else {
				System.out.format("    Expected %s, but was %s.%n",
						outcome, outcomes.get(0));
			}
		} else {
			System.out.format("    Expected a single outcome, but there were %d.%n",
					outcomes.size());
		}
		
		System.out.println();
		return passed;
	}
	
	/**
	 * Helper method that stacks a deck with the given card sequence,
	 * runs a round, and checks that the outcomes were as expected and the
	 * player won or lost the right amount of money.
	 */
	public static boolean testSplitOutcomes
			(List<Outcome> expectedOutcomes, Card... cards) {
		
		Blackjack game = new Blackjack(Level.FINE);
		Player player = game.getPlayer();
		int start = player.getBank();

		game.setDeckFactory(new Stacker(cards));
		List<Outcome> actualOutcomes = game.playARound();
		boolean passed = actualOutcomes.size() == expectedOutcomes.size();
		if (passed) {
			for (int i = 0; i < expectedOutcomes.size(); ++i) {
				if (actualOutcomes.get(i) != expectedOutcomes.get(i)) {
					System.out.format("    For hand %d, expected %s, but was %s.%n",
							i + 1, expectedOutcomes.get(i), actualOutcomes.get(i));
					passed = false;
				}
			}
			if (passed) {
				int expectedWinnings = 0;
				for (Outcome outcome : expectedOutcomes) {
					expectedWinnings += 
							(int) (Player.DEFAULT_WAGER * outcome.getMultiplier());
				}
				int actualWinnings = player.getBank() - start;
				if (actualWinnings != expectedWinnings) {
					System.out.format("    Expected win/lss of %d, but was %d.%n",
							expectedWinnings, actualWinnings);
					passed = false;
				}
			}
		} else {
			System.out.format("    Expected %d outcomes, but there were %d.%n",
					expectedOutcomes.size(), actualOutcomes.size());
		}
		
		System.out.println();
		return passed;
	}
	
	public static boolean testSingleBlackjack() {
		System.out.println("Testing single win ...");
		return testSingleOutcome(Outcome.BLACKJACK, spot(9), spot(9),
				spot(10), spot(Spot.ACE));
	}
	
	public static boolean testSingleBlackjackTie() {
		System.out.println("Testing single win ...");
		return testSingleOutcome(Outcome.BLACKJACK_TIE, spot(10), spot(Spot.ACE),
				spot(10), spot(Spot.ACE));
	}
	
	public static boolean testDealerBlackjack() {
		System.out.println("Testing single win ...");
		return testSingleOutcome(Outcome.LOSS, spot(10), spot(Spot.ACE),
				spot(2), spot(3));
	}
	
	public static boolean testSingleWin() {
		System.out.println("Testing single win ...");
		return testSingleOutcome(Outcome.WIN, spot(9), spot(9),
				spot(10), spot(Spot.JACK));
	}
	
	public static boolean testSingleLongerWin() {
		System.out.println("Testing single, longer win ...");
		return testSingleOutcome(Outcome.WIN, spot(9), spot(9), 
				spot(3), spot(4), spot(4), spot(10));
	}
	
	public static boolean testSingleBust() {
		System.out.println("Testing single bust ...");
		return testSingleOutcome(Outcome.BUST, spot(9), spot(9),
				spot(10), spot(5), spot(9));
	}

	public static boolean testSingleLoss() {
		System.out.println("Testing single loss ...");
		return testSingleOutcome(Outcome.LOSS, spot(10), spot(Spot.JACK), 
				spot(10), spot(9));
	}
	
	public static boolean testSingleLongerLoss() {
		System.out.println("Testing single, longer loss ...");
		return testSingleOutcome(Outcome.LOSS, spot(10), spot(10),
				spot(3), spot(4), spot(4), spot(8));
	}

	public static boolean testSinglePush() {
		System.out.println("Testing single win ...");
		return testSingleOutcome(Outcome.PUSH, spot(9), spot(9), spot(8),
				spot(10));
	}
	public static boolean testDoubleWin() {
		System.out.println("Testing doubled win ...");
		return testSingleOutcome(Outcome.DOUBLE_WIN, spot(9), spot(9), 
				spot(3), spot(8), spot(10));
	}
	
	public static boolean testDoubleLoss() {
		System.out.println("Testing doubled loss ...");
		return testSingleOutcome(Outcome.DOUBLE_LOSS, spot(9), spot(Spot.KING),
				spot(3), spot(8), spot(7));
	}
	
	public static boolean testSingleSurrender() {
		System.out.println("Testing single surrender ...");
		return testSingleOutcome(Outcome.SURRENDER, spot(9), spot(3),
				spot(10), spot(6));
	}
	
	public static boolean testSingleNoSurrender() {
		System.out.println("Testing single no-surrender ...");
		return testSingleOutcome(Outcome.BUST, spot(7), spot(7), 
				spot(10), spot(6), spot(7));
	}
	
	public static boolean testSplitWinAndLoss() {
		System.out.println("Testing split win and loss ...");
		
		List<Outcome> expectedOutcomes = new ArrayList<>();
		expectedOutcomes.add(Outcome.WIN);
		expectedOutcomes.add(Outcome.LOSS);
		
		return testSplitOutcomes(expectedOutcomes, spot(9), spot(9), 
				spot(6), spot(6), spot(8), spot(3), spot(5), spot(8));
	}
	
	public static boolean testSplitTwice() {
		System.out.println("Testing split twice, for a bust, push, and win ...");
		
		List<Outcome> expectedOutcomes = new ArrayList<>();
		expectedOutcomes.add(Outcome.BUST);
		expectedOutcomes.add(Outcome.PUSH);
		expectedOutcomes.add(Outcome.WIN);
		
		return testSplitOutcomes(expectedOutcomes, spot(9), spot(10),
				spot(6), spot(6), spot(8), spot(6), spot(7), spot(2),
				spot(Spot.JACK), spot(Spot.ACE), spot(5), spot(2), spot(10));
	}
	
	/**
	 * Runs a battery of tests and summarizes results to the console.
	 */
	public static void main(String[] args) {
		
		boolean allPassed = true;

		allPassed &= testSingleBlackjack();
		allPassed &= testSingleBlackjackTie();
		allPassed &= testDealerBlackjack();
		allPassed &= testSingleWin();
		allPassed &= testSingleLongerWin();
		allPassed &= testSingleBust();
		allPassed &= testSingleLoss();
		allPassed &= testSingleLongerLoss();
		allPassed &= testSinglePush();
		allPassed &= testDoubleWin();
		allPassed &= testDoubleLoss();
		allPassed &= testSingleSurrender();
		allPassed &= testSingleNoSurrender();
		allPassed &= testSplitWinAndLoss();
		allPassed &= testSplitTwice();
		
		System.out.println();
		System.out.println(allPassed ? "ALL TESTS PASSED." : "SOME TESTS FAILED.");
	}
}

