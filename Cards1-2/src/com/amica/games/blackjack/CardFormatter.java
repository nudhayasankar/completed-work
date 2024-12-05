package com.amica.games.blackjack;

public class CardFormatter {
    public static String abbreviationOf(Card.SPOT spot){
        String spotStr = spot.name();
        spotStr.replace("_", "");
        char abbr = spotStr.charAt(0);
        String abbrStr = abbr == '1' ? new String("10") : new String(new char[]{abbr});
        return abbrStr;
    }

    public static String abbreviationOf(Card card){
        String suitStr = card.getSuit().name();
        return abbreviationOf(card.getSpot()) + suitStr.charAt(0);
    }

    public static String capitalize(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String nameOf(Card.SUIT suit){
        return capitalize(suit.name());
    }

    public static String nameOf(Card.SPOT spot){
        String spotName = spot.name().replace("_", "");
        return capitalize(spotName);
    }

    public static String nameOf(Card card){
        return String.format("%s of %s", nameOf(card.getSpot()), nameOf(card.getSuit()));
    }

}

