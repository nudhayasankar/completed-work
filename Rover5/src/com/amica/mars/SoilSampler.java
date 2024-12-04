package com.amica.mars;

public class SoilSampler extends ReportingRover{
    private final String SEND_SOIL_SAMPLE = "Sampled some soil!";
    public SoilSampler(int id){
        super(id);
    }

    public SoilSampler(int x, int y, Rover.Direction direction, int id){
        super(x, y, direction, id);
    }

    @Override
    public void move(){
        super.move();
        report(SEND_SOIL_SAMPLE);
    }
}
