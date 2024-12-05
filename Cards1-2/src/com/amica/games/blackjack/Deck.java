package com.amica.games.blackjack;

import java.util.*;

public class Deck {
    private List<Card> cards;

    public Deck(){
        cards = new ArrayList<>();
        populateCards();
        shuffle();
    }

    private void populateCards(){
        Arrays.stream(Card.SUIT.values()).forEach(suit -> {
            Arrays.stream(Card.SPOT.values()).forEach(spot ->{
                cards.add(new Card(suit, spot));
            });
        });
    }

    private void shuffle(){
        Collections.shuffle(cards, new Random());
    }

    public Card deal(){
        Card card = cards.get(0);
        cards.remove(card);
        return card;
    }

    public List<Card> deal(int numCards){
        List<Card> dealtCards = new ArrayList<>();
        while(numCards > 0){
            Card currentCard = cards.get(0);
            cards.remove(currentCard);
            dealtCards.add(currentCard);
            numCards--;
        }
        return dealtCards;
    }

    public boolean isCardAvailable(){
        return !cards.isEmpty();
    }

    public int cardsLeft(){
        return cards.size();
    }
}
