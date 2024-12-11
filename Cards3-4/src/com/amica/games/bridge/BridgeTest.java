package com.amica.games.bridge;

public class BridgeTest {
    public static void main(String[] args){
        Bridge bridge = new Bridge();
        bridge.deal();
        System.out.println("The deal:");
        System.out.println(bridge.toString());
        System.out.println(" ");
        bridge.play();
    }
}
