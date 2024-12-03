package com.amica.mars;

/**
 * Slightly more involved test script for the {@link Rover},
 * illustrating two rovers moving independently and concurrently.
 *
 * @author Will Provost
 */
public class TestProgram2 {

	public static void main(String[] args) {

		Rover rover1 = new Rover();
		rover1.receiveCommands("4R2R1L2");

		Rover rover2 = new Rover();
		rover2.receiveCommands("L4R2L1");

		System.out.format("Rover 1 is at (%d,%d), and facing %s.%n",
				rover1.getX(), rover1.getY(), rover1.getDirection());
		System.out.format("Rover 2 is at (%d,%d), and facing %s.%n",
				rover2.getX(), rover2.getY(), rover2.getDirection());
		while (rover1.isBusy() || rover2.isBusy()) {
			rover1.takeNextStep();
			rover2.takeNextStep();
			System.out.println("Rover 1: " + rover1.getStatus());
			System.out.println("Rover 2: " + rover2.getStatus());
		}
	}
	
}
