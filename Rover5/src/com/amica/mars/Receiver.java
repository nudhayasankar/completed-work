package com.amica.mars;

import java.util.ArrayList;
import java.util.List;

public class Receiver {
    List<String> results;
    public Receiver(){
        results = new ArrayList<>();
    }

    public void listenForMessages(String message){
        results.add(message);
    }

    public void processMessages(){
        results.forEach(result -> {
            System.out.println(result);
        });
    }

}
