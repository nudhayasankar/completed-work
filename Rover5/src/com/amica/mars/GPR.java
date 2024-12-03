package com.amica.mars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GPR extends Rover{
    private final String SCAN_GROUND = "A new ground scan has been uploaded";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GPR(){
        super();
    }

    public GPR(Receiver receiver){
        super(receiver);
    }

    public GPR(int x, int y, Rover.Direction direction){
        super(x, y, direction);
    }

    @Override
    public void doSpecificAction(char command, Receiver receiver){
        if(command == 'S'){
            LocalDateTime ts = LocalDateTime.now();
            String formattedTS = formatter.format(ts);
            receiver.listenForMessages(formattedTS + " - " + SCAN_GROUND);
        }
    }
}
