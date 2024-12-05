package com.amica.games.blackjack;

public class Player {
    private int bank;

    public Player(int bank){
        this.bank = bank;
    }

    public Player(){
        this.bank = 10000;
    }

    public int getBank() {
        return bank;
    }

    public void winOrLose(int earnings){
        this.bank += earnings;
    }

    public PlayerHand play(Card firstCard, Card dealerCard){
        PlayerHand hand = new PlayerHand(this, 10, dealerCard, firstCard);
        return hand;
    }
}
