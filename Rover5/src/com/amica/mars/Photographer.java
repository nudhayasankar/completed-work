package com.amica.mars;

public class Photographer extends ReportingRover {
    private final String SEND_PHOTO = "A new photo has been uploaded!";

    public Photographer(int id) {
        super(id);
    }

    public Photographer(int x, int y, Rover.Direction direction, int id) {
        super(x, y, direction, id);
    }

    @Override
    public void move() {
        super.move();
        report(SEND_PHOTO);
    }

    @Override
    public void turnLeft() {
        super.turnLeft();
        report(SEND_PHOTO);
    }

    @Override
    public void turnRight() {
        super.turnRight();
        report(SEND_PHOTO);
    }

}
