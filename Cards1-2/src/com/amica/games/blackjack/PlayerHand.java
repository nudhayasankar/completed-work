package com.amica.games.blackjack;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand {
    private Player owner;
    private int wager;
    private List<Card> cards = new ArrayList<>();
    private Card dealerCard;
    private int id;
    private boolean doubled;

    public PlayerHand(Player player, int playerWager, Card dealerCard, Card playerCard){
        this.owner = player;
        this.wager = playerWager;
        this.dealerCard = dealerCard;
        this.cards.add(playerCard);
    }

    public Player getOwner() {
        return owner;
    }

    public int getWager() {
        return wager;
    }

    public int getId() {
        return id;
    }

    public boolean isDoubled() {
        return doubled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blackjack.Action secondCard(Card secondCard) {
        cards.add(secondCard);
        int points = getPoints();
        int firstVal = cards.get(0).getSpot().getValue();
        int secondVal = cards.get(1).getSpot().getValue();
        if(firstVal != 10 && firstVal == secondVal){
            return Blackjack.Action.SPLIT;
        }
        if(points == 16 && dealerCard.getSpot().getValue() >= 9){
            return Blackjack.Action.SURRENDER;
        }
        if(points == 10 || points == 11){
            doubled = true;
            return Blackjack.Action.DOUBLE;
        }
        return Blackjack.Action.NONE;
    }

    public int getPoints(){
        int points = 0;
        int aceCount = 0;
        for(Card card : cards){
            if(card.getSpot().getValue() == 11){
                aceCount++;
            }
        }
        for(Card card : cards){
            if(card.getSpot().getValue() == 11){
                points += 1;
            } else {
                points += card.getSpot().getValue();
            }
        }
        while(aceCount > 0){
            if(points + 10 <= 21){
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
        int points = getPoints();
        if(points < 12){
            return true;
        } else if(points > 16){
            return false;
        } else if(dealerCard.getSpot().getValue() >= 7){
            return true;
        }
        return false;
    }

    public void hit(Card card){
        cards.add(card);
    }

    public void winOrLose(int earnings){
        owner.winOrLose(earnings);
    }

    public void dubble(Card card){
        cards.add(card);
    }

    public List<PlayerHand> split(){
        PlayerHand hand1 = new PlayerHand(owner, wager, dealerCard, cards.get(0));
        PlayerHand hand2 = new PlayerHand(owner, wager, dealerCard, cards.get(1));
        return new ArrayList<PlayerHand>(){{
            add(hand1);
            add(hand2);
        }};
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Player has %d in the bank and the following cards\n", owner.getBank()));
        cards.forEach(card -> {
            sb.append(CardFormatter.nameOf(card));
            sb.append("\n");
        });
        return sb.toString();
    }
}
