package com.amica.mars;

public class GroundPounder extends ReportingRover {
    private final String SCAN_GROUND = "A new ground scan has been uploaded!";

    public GroundPounder(int id) {
        super(id);
    }

    public GroundPounder(int x, int y, Rover.Direction direction, int id) {
        super(x, y, direction, id);
    }

    @Override
    protected void execute(char command) {
        if (command == 'P') {
            report(SCAN_GROUND);
        } else {
            super.execute(command);
        }
    }
}
