package com.amica.games.bridge;

import com.amica.games.Card;

import java.util.*;
import java.util.stream.Collectors;

import com.amica.games.CardFormatter;

public class Player {
    private Map<Card.Suit, SortedSet<Card>> cards;
    private final String SEPARATOR = " -- ";
    private final String OF = "of ";

    public Player(){
        cards = new TreeMap<>();
        Comparator c = Collections.reverseOrder();
        for(Card.Suit s : Card.Suit.values()){
            cards.put(s, new TreeSet<>(c));
        }
    }

    public void acceptCard(Card card){
        cards.get(card.getSuit()).add(card);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Card.Suit s : cards.keySet()){
            String suit = s.name().toLowerCase();
            for(Card card : cards.get(s)){
                sb.append(CardFormatter.abbreviationOf(card.getSpot()));
                sb.append(" ");
            }
            sb.append(OF);
            sb.append(suit);
            sb.append(SEPARATOR);
        }
        sb.append("EOL");
        return sb.toString().replace("-- EOL", "");
    }

    public Card play(Trick trick) {
        if (trick.isEmpty()) {
            int longestSuitLen = cards.values().stream().map(c -> c.size()).max(($,c) -> -c).get();
            SortedSet<Card> highCards = new TreeSet<>(Comparator.comparing(Card::getSpot));
            for (Card.Suit s : cards.keySet()) {
                if(!cards.get(s).isEmpty() && cards.get(s).size() >= longestSuitLen)
                    highCards.add(cards.get(s).first());
            }
            Card toPlay = highCards.last();
            removeCard(toPlay);
            return toPlay;
        } else {
            Card trickWinner = trick.getWinningCard();
            Card.Suit winningSuit = trickWinner.getSuit();
            SortedSet<Card> suitCards = cards.get(winningSuit);
            if (suitCards.isEmpty()) {
                SortedSet<Card> lowCards = new TreeSet<>();
                for (Card.Suit s : cards.keySet()) {
                    SortedSet<Card> fetchedCards = cards.get(s);
                    if(!fetchedCards.isEmpty())
                        lowCards.add(fetchedCards.last());
                }
                Card toPlay = lowCards.first();
                removeCard(toPlay);
                return toPlay;
            } else {
                for (Card c : suitCards) {
                    if (c.compareTo(trickWinner) > 0) {
                        removeCard(c);
                        return c;
                    }
                }
                Card toPlay = suitCards.last();
                removeCard(toPlay);
                return toPlay;
            }
        }
    }

    private void removeCard(Card card) {
        cards.get(card.getSuit()).remove(card);
    }
}
