package com.amica.games;

public class CardFormatter {

    /**
     * We could set up a map from spot value to character, but it's fairly clean
     * to do this algorihmically: strip any leading underscore from the
     * string representation of the enum value, and then take the first character.
     * Oh -- except for 10s ...
     */
	public static String abbreviationOf(Card.Spot spot) {
		String abbreviation = spot.toString().replace("_", "").substring(0, 1);
		if (abbreviation.equals("1")) {
			abbreviation = "10";
		}
		return abbreviation;
	}

    /**
     * Returns a short string of the abbreviations for spot and suit.
     */
	public static String abbreviationOf(Card card) {
		return abbreviationOf(card.getSpot()) + 
				card.getSuit().toString().substring(0, 1);
	}

	/**
	 * Helper to capitalize just the first letter of a symbol.
	 */
	private static String capitalize(String symbol) {
		StringBuilder builder = new StringBuilder();
		if (symbol.length() != 0) {
			builder.append(symbol.substring(0, 1).toUpperCase());
		}
		if (symbol.length() > 1) {
			builder.append(symbol.substring(1).toLowerCase());
		}
		return builder.toString();
	}
	
	/**
	 * Get a word-capitalized name for the given suit.
	 */
	public static String nameOf(Card.Suit suit) {
		return capitalize(suit.toString());
	}
	
	/**
	 * String representation is the {@link #getSpotName spot name} and suit.
	 */
	public static String nameOf(Card.Spot spot) {
		return capitalize(spot.toString().replace("_", ""));
	}
	
    /**
     * String representation is the {@link #getSpotName spot name} and suit.
     */
	public static String nameOf(Card card) {
		return nameOf(card.getSpot()) + " of " + nameOf(card.getSuit()); 
	}
}
