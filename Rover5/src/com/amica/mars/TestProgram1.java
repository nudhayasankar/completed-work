package com.amica.mars;

/**
 * Simple test script for the {@link Rover}.
 *
 * @author Will Provost
 */
public class TestProgram1 {

	public static void main(String[] args) {

		Rover rover = new Rover();
		rover.receiveCommands("4R2R1L2");
		//rover.receiveCommands("LL4R2R1L2");
		//rover.receiveCommands("R4LL2R17");

		System.out.format("The rover is now at (%d,%d), and facing %s.%n",
				rover.getX(), rover.getY(), rover.getDirection());
		while (rover.isBusy()) {
			rover.takeNextStep();
			System.out.println(rover.getStatus());
		}
	}
	
}
