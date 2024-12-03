package com.amica.mars;

/**
 * Slightly more involved test script for the {@link Rover},
 * illustrating two rovers moving independently and concurrently.
 *
 * @author Will Provost
 */
public class TestProgram3 {

	public static void main(String[] args) {

		Rover rover1 = new Rover();
		rover1.receiveCommands("4R2R1L2");

		Receiver receiver = new Receiver();

		Rover rover2 = new SoilDigger(receiver);
		rover2.receiveCommands("L4R2L1");

		Rover rover3 = new PhotoRover(receiver);
		rover3.receiveCommands("L5R2L1");

		Rover rover4 = new GPR(receiver);
		rover4.receiveCommands("LR2SL1S");

		System.out.format("Rover 1 is at (%d,%d), and facing %s.%n",
				rover1.getX(), rover1.getY(), rover1.getDirection());
		System.out.format("Rover 2 is at (%d,%d), and facing %s.%n",
				rover2.getX(), rover2.getY(), rover2.getDirection());
		while (rover1.isBusy() || rover2.isBusy() || rover3.isBusy() || rover4.isBusy()) {
			rover1.takeNextStep();
			rover2.takeNextStep();
			rover3.takeNextStep();
			rover4.takeNextStep();
			System.out.println("Rover 1: " + rover1.getStatus());
			System.out.println("SoilDigger: " + rover2.getStatus());
			System.out.println("PhotoRover: " + rover3.getStatus());
			System.out.println("GPR: " + rover4.getStatus());
		}

		receiver.processMessages();
	}
	
}
