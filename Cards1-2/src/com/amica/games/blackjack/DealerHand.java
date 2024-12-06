package com.amica.games.blackjack;

import java.util.ArrayList;
import java.util.List;

public class DealerHand {
    private List<Card> cards = new ArrayList<>();

    public DealerHand(Card firstCard, Card secondCard) {
        this.cards.add(firstCard);
        this.cards.add(secondCard);
    }

    public int getPoints() {
        int points = 0;
        int aceCount = 0;
        for (Card card : cards) {
            if (card.getSpot().getValue() == 11) {
                aceCount++;
            }
        }
        for (Card card : cards) {
            if (card.getSpot().getValue() == 11) {
                points += 1;
            } else {
                points += card.getSpot().getValue();
            }
        }
        while (aceCount > 0) {
            if (points + 10 <= 21) {
                points += 10;
            }
            aceCount--;
        }
        return points;
    }

    public boolean isBust() {
        return getPoints() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getPoints() == 21;
    }

    public boolean shouldHit() {
        return getPoints() < 17;
    }

    public void hit(Card card) {
        cards.add(card);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Dealer has the following cards\n"));
        cards.forEach(card -> {
            sb.append(CardFormatter.nameOf(card));
            sb.append("\n");
        });
        return sb.toString();
    }
}

