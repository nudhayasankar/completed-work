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
        return getPoints() < 16;
    }

    public void hit(Card card){
        cards.add(card);
    }

    public void winOrLose(int earnings){
        owner.winOrLose(earnings);
    }

    public void dubble(Card card){

    }

    public List<PlayerHand> split(){
        return null;
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
