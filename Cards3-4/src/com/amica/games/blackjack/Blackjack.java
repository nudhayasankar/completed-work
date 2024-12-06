package com.amica.games.blackjack;

import static com.amica.games.CardFormatter.nameOf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amica.games.Card;
import com.amica.games.Deck;

/**
 * Models a round of blackjack, dealing from a single deck.
 * Runs the game with the help of a {@link Player} and {@Link PlayerHand},
 * makes appropriate payouts, and tallies up outcomes.
 * 
 * @author Will Provost
 */
public class Blackjack {
	
	public enum Action { DOUBLE, SPLIT, SURRENDER, NONE }
	
	/**
	 * Stateful enumeration of all possible hand oucomes, also holding
	 * the multiplier for appriate payout (or loss) of wager.
	 * 
	 * @author Will Provost
	 */
	public enum Outcome { 
		DOUBLE_BUST(-2), 
		DOUBLE_LOSS(-2), 
			BUST(-1), 
			LOSS(-1), 
			SURRENDER(-.5), 
			PUSH(0), 
			DOUBLE_PUSH(0), 
			WIN(1), 
			BLACKJACK_TIE(1), 
			BLACKJACK(1.5), 
			DOUBLE_WIN(2);
		
			private double multiplier;
			
			private Outcome(double multiplier) {
				this.multiplier = multiplier;
			}
			
			public double getMultiplier() {
				return multiplier;
			}
		
		}
	
	private static final Logger log = Logger.getLogger(Blackjack.class.getName());

	private Deck deck;
	private Player player;
	private int nextID;
	private Level level;
	private Supplier<Deck> deckFactory;
	private List<Outcome> outcomes;
	
	/**
	 * Create with a logging level. INFO will show in the IDE by default;
	 * FINE will not; and a null value here will cause the class to "log"
	 * by printing directly to standard output.
	 */
	public Blackjack(Level level) {
		player = new Player();
		this.level = level;
	}
	
	/**
	 * A hook to allow tests to supply stacked decks. 
	 */
	public void setDeckFactory(Supplier<Deck> deckFactory) {
		this.deckFactory = deckFactory;
	}
	
	/**
	 * Accessor for the game player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Log, or print, the given message, as per our configuration. 
	 */
	private void log(String message) {
		if (level != null) {
			log.log(level, message);
		} else {
			System.out.println(message);
		}
	}
	
	/**
	 * Helper that deals a card, logs it with a given label, and returns it.
	 */
	private Card dealAndLogAs(String label) {
		Card card = deck.deal();
		log(label + " a(n) " + nameOf(card.getSpot()));
		return card;
	}
	
	/**
	 * Helper to pay out to the player or to take the paler's wager,
	 * according to the given hand outcome. We also compile a list of 
	 * outcomes during the round, which can be inspected later. 
	 */
	private void payout(PlayerHand hand, Outcome outcome) {
		hand.winOrLose((int) (hand.getWager() * outcome.getMultiplier()));
		outcomes.add(outcome);
	}
	
	/**
	 * Recursive method that deals the second card to the given hand.
	 * We ask the player/hand for an initial action: surrender, double, split,
	 * or none (meaning ready to play normally from here). Surrendered hands
	 * lose half their wager and are done playing. Doubled hands are marked as
	 * such, and are dealt their one remaining card, and added to the list
	 * of hands to be evaluated later in the round. Split hands are created
	 * from the two current cards, and each is then passed to this function
	 * recursively to allow each to be acted upon by the player.
	 * It is possible to split on one or both of the split hands.
	 */
	private List<PlayerHand> dealSecondCard(PlayerHand hand) {
		hand.setID(++nextID);
		List<PlayerHand> result = new ArrayList<>();
		Action firstAction = hand.secondCard
				(dealAndLogAs("Hand " + hand.getID() + " gets"));
		if (firstAction == Action.SURRENDER) {
			payout(hand, Outcome.SURRENDER);
			log("Hand " + hand.getID() + " surrenders.");
		} else if (firstAction == Action.DOUBLE) {
			hand.dubble(dealAndLogAs("Hand " + hand.getID() + " doubles: "));
			result.add(hand);
		} else if (firstAction == Action.SPLIT) {
			log("Hand " + hand.getID() + " splits ...");
			for (PlayerHand splitHand : hand.split()) {
				result.addAll(dealSecondCard(splitHand));
			}
		} else {
			result.add(hand);
		}
		
		return result;
	}
	
	/**
	 * This is the master function that runs the round, basically carrying out
	 * the opereations as the dealer would in a real game.
	 * <ol>
	 *  <li>Dealer blackjack? Pay out as loss or push, and we're done.</li> 
	 *  <li>Let player surrender, double, or split. This results in a list of active hands -- could be none (surrender), one, or many (after a split).</li> 
	 *  <li>For all active hands, see if blackjack, or let them play. Blackjacks and bust hands are separated out.</li> 
	 *  <li>If there's at least one "live" hand, dealer hits until at 17 or higher, and may bust.</li> 
	 *  <li>Evaluate remaining hands tor win, loss, or push.</li> 
	 * </ol>
	 */
	public List<Outcome> playARound() {
		
		// Reset inagance state
		nextID = 0;
		outcomes = new ArrayList<>();

		if (deckFactory != null) {
			deck = deckFactory.get();
		} else {
			deck = new Deck();
		}
		
		// Set up dealer, and deal first card to player.
		Card dealerCard = dealAndLogAs("Dealer shows");
		DealerHand dealer = new DealerHand
				(dealerCard,  dealAndLogAs("Dealer's hold card is"));
		PlayerHand initialHand = 
				player.play(dealAndLogAs("Hand 1 gets"), dealerCard);
		
		// If dealer has blackjack, see if it's a push, or a loss, and quit here:
		if (dealer.isBlackjack()) {
			initialHand.secondCard(dealAndLogAs("Player gets"));
			log("Dealer has blackjack. " + (initialHand.isBlackjack() 
					? "So does the player -- push." : "Player loses."));
			payout(initialHand, initialHand.isBlackjack() 
					? Outcome.BLACKJACK_TIE : Outcome.LOSS);
			return outcomes;
		}
		
		// Deal second card to player. This recursive logic
		// also takes care of surrenders, doubles, and splits:
		List<PlayerHand> activeHands = dealSecondCard(initialHand);

		// For each resulting hand, se if it's a blackjack.
		// If not, play out the hand, and see if it's a bust.
		List<PlayerHand> blackjackHands = new ArrayList<>();
		List<PlayerHand> bustHands = new ArrayList<>();
		for (PlayerHand activeHand : activeHands) {
			if (activeHand.isBlackjack()) {
				log("Hand " + activeHand.getID() + " has blackjack.");
				payout(activeHand, Outcome.BLACKJACK);
				blackjackHands.add(activeHand);
			} else if (!activeHand.isDoubled()) {
				while (!activeHand.isBust() && activeHand.shouldHit()) {
					Card hitCard = deck.deal();
					log("Hand " + activeHand.getID() + 
							" hit: " + nameOf(hitCard.getSpot()));
					activeHand.hit(hitCard);
				}
				if (activeHand.isBust()) {
					payout(activeHand, activeHand.isDoubled() 
							? Outcome.DOUBLE_BUST : Outcome.BUST);
					bustHands.add(activeHand);
					log("Hand " + activeHand.getID() + " is bust.");
				}
			}
		}
		activeHands.removeAll(blackjackHands);
		activeHands.removeAll(bustHands);

		// If any hands are still alive, dealer plays, and all hands are scored
		if (!activeHands.isEmpty()) {
			
			log("Dealer has " + (dealer.isBlackjack() 
					? "blackjack." : ("" + dealer.getPoints() + " points.")));

			while (dealer.shouldHit()) {
				Card dealerHitCard = deck.deal(); 
				dealer.hit(dealerHitCard);
				log("Dealer hit: " + nameOf(dealerHitCard.getSpot()));
			}
			log(dealer.isBust() ? "Dealer is bust." 
					: "Dealer has " + dealer.getPoints() + " points.");
			
			for (PlayerHand activeHand : activeHands) {
				log("Hand " + activeHand.getID() + " has " + 
						activeHand.getPoints() + " points.");
				
				if (dealer.isBust() || dealer.getPoints() < activeHand.getPoints()) {
					payout(activeHand, activeHand.isDoubled() 
							? Outcome.DOUBLE_WIN : Outcome.WIN);
					log("Hand " + activeHand.getID() + " wins.");
				} else if (dealer.getPoints() > activeHand.getPoints()) {
					payout(activeHand, activeHand.isDoubled()
							? Outcome.DOUBLE_LOSS : Outcome.LOSS);
					log("Hand " + activeHand.getID() + " loses.");
				} else {
					payout(activeHand, activeHand.isDoubled()
							? Outcome.DOUBLE_PUSH : Outcome.PUSH);
					log("Hand " + activeHand.getID() + " is a push.");
				}
			}
		}
		
		return outcomes;
	}
	
	/**
	 * Run one round, printing log entries to standard output.
	 */
	public static void main(String[] args) {
		Blackjack game = new Blackjack(null);
		game.playARound();
	}
}
