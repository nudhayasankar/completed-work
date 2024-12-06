package com.amica.games.blackjack;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import com.amica.games.blackjack.Blackjack.Outcome;

public class Simulation {

	public static void main(String[] args) {
		
		final int SIMULATIONS = 1000;
		final int ROUNDS = 1000;
		
		int splitCount = 0;
		SortedMap<Outcome,Integer> outcomeCounts = new TreeMap<>();
		for (Outcome outcome : Outcome.values()) {
			outcomeCounts.put(outcome, 0);
		}
		
		int wonOrLost = 0;
		for (int i = 0; i < SIMULATIONS; ++i) {
			Blackjack game = new Blackjack(Level.FINE);
			Player player = game.getPlayer();
			wonOrLost -= player.getBank();
			for (int j = 0; j < ROUNDS; ++j) {
				List<Outcome> outcomes = game.playARound();
				if (outcomes.size() != 1) {
					++splitCount;
				}
				for (Outcome outcome : outcomes) {
					outcomeCounts.put(outcome, outcomeCounts.get(outcome) + 1);
				}
			}
			wonOrLost += player.getBank();
		}
		System.out.format("After %,d simulations, player averaged $%,d gain or loss over %,d hands.%n",
				SIMULATIONS, wonOrLost / SIMULATIONS, ROUNDS);
		System.out.format("There were %1.2f splits per %d rounds.%n", 
				(double) splitCount / SIMULATIONS, ROUNDS);
		System.out.println("Outcome frequency:");
		for (Outcome outcome : outcomeCounts.keySet()) {
			double average = outcomeCounts.get(outcome) * 100.0
					/ (SIMULATIONS * ROUNDS + splitCount);
			System.out.format("    %-16s %5.2f%%%n", outcome, average);
		}
	}
}
