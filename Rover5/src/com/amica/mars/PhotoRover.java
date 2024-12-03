package com.amica.mars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PhotoRover extends Rover{
    private final String SEND_PHOTO = "A new photo has been uploaded";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PhotoRover(){
        super();
    }

    public PhotoRover(Receiver receiver){
        super(receiver);
    }

    public PhotoRover(int x, int y, Rover.Direction direction){
        super(x, y, direction);
    }

    @Override
    public void doSpecificAction(Receiver receiver){
        LocalDateTime ts = LocalDateTime.now();
        String formattedTS = formatter.format(ts);
        receiver.listenForMessages(formattedTS + " - " + SEND_PHOTO);
    }
}
