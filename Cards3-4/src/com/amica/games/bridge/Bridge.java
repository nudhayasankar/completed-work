package com.amica.games.bridge;

import java.util.ArrayList;
import java.util.List;

import com.amica.games.CardFormatter;
import com.amica.games.Deck;

public class Bridge {
    private List<Player> players;
    private int leader;
    private int eastWest;
    private int northSouth;

    public Bridge() {
        players = new ArrayList<>(){{
            add(new Player());
            add(new Player());
            add(new Player());
            add(new Player());
        }};
        eastWest = 0;
        northSouth = 0;
    }

    public int next(int playerIndex) {
        int nextPlayerIndex = (playerIndex + 1) % 4;
        return nextPlayerIndex;
    }

    public void deal() {
        Deck deck = new Deck();
        while(deck.cardsLeft() != 0){
            int playerIndex = deck.cardsLeft() % 4;
            players.get(playerIndex).acceptCard(deck.deal());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Player p : players){
            sb.append(String.format("Player %d: ", count) + p.toString());
            sb.append("\n");
            count++;
        }
        return sb.toString();
    }

    public void play() {
        int currentTrick = 0;
        int currentPlayer = 0;
        while(currentTrick < 13){
            int currentLeader = leader;
            Trick trick = new Trick();
            currentLeader = play(currentPlayer, trick);
            currentPlayer = next(currentPlayer);
            while(currentPlayer != 0){
                currentLeader = play(currentPlayer, trick);
                currentPlayer = next(currentPlayer);
            }
            leader = currentLeader;
            System.out.print(String.format("Player %d leads ... ", leader));
            trick.forEach(c -> System.out.print(CardFormatter.nameOf(c) + ", "));
            System.out.println("\n");
            currentTrick++;
            if(currentLeader % 2 == 0){
                eastWest++;
            } else {
                northSouth++;
            }
        }
        System.out.println(String.format("Team 1 won %d tricks. \n Team 2 won %d tricks.", eastWest, northSouth));
    }

    public int play(int player, Trick trick) {
        Player p = players.get(player);
        trick.add(p.play(trick));
        int currentLeader = trick.getWinner();
        return currentLeader;
    }
}
