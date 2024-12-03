package com.amica.mars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SoilDigger extends Rover{
    private final String SEND_SOIL_SAMPLE = "A new soil sample has been uploaded";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public SoilDigger(){
        super();
    }

    public SoilDigger(int x, int y, Rover.Direction direction){
        super(x, y, direction);
    }

    public SoilDigger(Receiver receiver){
        super(receiver);
    }
    @Override
    public void doSpecificAction(char command, Receiver receiver){
        LocalDateTime ts = LocalDateTime.now();
        String formattedTS = formatter.format(ts);
        receiver.listenForMessages(formattedTS + " - " + SEND_SOIL_SAMPLE);
    }
}
