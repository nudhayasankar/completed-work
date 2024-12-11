package com.amica.games.poker;

import com.amica.games.Card;
import com.amica.games.CardFormatter;

import java.util.*;
import java.util.stream.Collectors;

public class Player extends TreeSet<Card> {
    public enum Rank {NO_PAIR, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND,
    STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH}

    public static class CompareBySpot implements Comparator<Card>{

        @Override
        public int compare(Card o1, Card o2) {
            if(o1.getSpot() == o2.getSpot()){
                return Integer.compare(o1.getSuit().ordinal(), o2.getSuit().ordinal());
            } else {
                return Integer.compare(o1.getSpot().ordinal(), o2.getSpot().ordinal());
            }
        }
    }

    public Player(Collection<Card> cards){
        super(new CompareBySpot());
        addAll(cards);
    }

    public Map<Card.Spot, Integer> getCounts(){
        Map<Card.Spot, Integer> counts = new HashMap<>();
        for (Card c : this){
            counts.put(c.getSpot(),
                    counts.getOrDefault(c.getSpot(), 0) + 1);
        }
        return counts;
    }

    public int getNumberToDraw() {
        List<Card> toRemove = getCardsToDraw();
        this.removeAll(toRemove);
        return toRemove.size();
    }

    public void draw(List<Card> cards){
        this.addAll(cards);
    }

    public Rank getRank(){
        Map<Card.Spot, Integer> spotsAndCounts = getCounts();
        Collection<Integer> counts = spotsAndCounts.values();
        if(counts.contains(4)){
            return Rank.FOUR_OF_A_KIND;
        }
        if(counts.contains(3)){
            if(counts.contains(2)){
                return Rank.FULL_HOUSE;
            } else {
                return Rank.THREE_OF_A_KIND;
            }
        }
        if(counts.contains(2)){
            if(counts.size() == 3){
                return Rank.TWO_PAIR;
            } else {
                return Rank.ONE_PAIR;
            }
        }
        boolean straight = false;
        List<Integer> cardValues = this.stream().map(c -> c.getSpot().ordinal()).collect(Collectors.toList());
        for(int i = 0; i < cardValues.size() - 4; i++){
            if(cardValues.get(i+4) - cardValues.get(i) == 4){
                straight = true;
                break;
            }
        }
        boolean flush = true;
        Map<Card.Suit, Long> suitCounts = this.stream()
                .map(c -> c.getSuit()).collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        flush = suitCounts.values().stream().anyMatch(v -> v == 5);
        if(straight && flush){
            return Rank.STRAIGHT_FLUSH;
        } else if(straight){
            return Rank.STRAIGHT;
        } else if(flush){
            return Rank.FLUSH;
        }
        return Rank.NO_PAIR;
    }

    public List<Card> getCardsToDraw() {
        List<Card> cardsToDraw = new ArrayList<>();
        Rank currentRank = getRank();
        Map<Card.Spot, Integer> spotsAndCounts = getCounts();
        switch(currentRank){
            case ONE_PAIR:
            case TWO_PAIR:
            case THREE_OF_A_KIND:
                cardsToDraw.addAll(this.stream().filter(c -> spotsAndCounts.get(c.getSpot()) == 1).collect(Collectors.toList()));
                break;
            case NO_PAIR:
                if(isStraightPossible() || isFlushPossible()){
                    Set<Card> straightCards = getStraightOrFlushCards();
                    cardsToDraw.addAll(this.stream().filter(c -> !straightCards.contains(c)).collect(Collectors.toSet()));
                } else {
                    List<Card> lowCards = this.stream().sorted(($,c) -> c.getSpot().ordinal())
                            .collect(Collectors.toList());
                    cardsToDraw.addAll(lowCards.subList(0, 3));
                }
            default:
                break;
        }
        return cardsToDraw;
    }

    public boolean isStraightPossible() {
        List<Integer> spotValues = this.stream().map(c -> c.getSpot().ordinal())
                .distinct().sorted().collect(Collectors.toList());
        for(int i = 0; i < spotValues.size() - 3; i++){
            if(spotValues.get(i+3) - spotValues.get(i) <= 3){
                return true;
            }
        }
        return false;
    }

    public boolean isFlushPossible() {
        Map<Card.Suit, Long> suitCount = this.stream().map(c -> c.getSuit())
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        return suitCount.values().stream().anyMatch(c -> c >= 3);
    }

    private Set<Card> getStraightOrFlushCards() {
        Set<Card> cardsToKeepFlush = new HashSet<>();
        Set<Card> cardsToKeepStraight = new HashSet<>();
        if(isFlushPossible()){
            Card.Suit flushSuit = this.stream().map(c -> c.getSuit())
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                    .entrySet().stream().filter(s -> s.getValue() >= 3)
                    .map(Map.Entry::getKey).findFirst().orElseThrow();
            cardsToKeepFlush.addAll(this.stream().filter(c -> c.getSuit() == flushSuit).collect(Collectors.toSet()));
        }
        if(isStraightPossible()){
            List<Integer> spotValues = this.stream().map(c -> c.getSpot().ordinal())
                    .distinct().sorted().collect(Collectors.toList());
            for(int i = 0; i < spotValues.size() - 3; i++){
                if(spotValues.get(i+3) - spotValues.get(i) <= 3){
                    int minVal = spotValues.get(i);
                    int maxVal = spotValues.get(i+3);
                    cardsToKeepStraight.addAll(this.stream().filter(c -> {
                        int v = c.getSpot().ordinal();
                        return v >= minVal && v <= maxVal;
                    }).collect(Collectors.toList()));
                }
            }
        }
        return cardsToKeepFlush.size() >= cardsToKeepStraight.size() ? cardsToKeepFlush : cardsToKeepStraight;
    }
}
