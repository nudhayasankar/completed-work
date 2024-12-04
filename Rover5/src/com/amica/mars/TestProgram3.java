package com.amica.mars;

import java.util.Random;

/**
 * Slightly more involved test script for the {@link Rover},
 * illustrating two rovers moving independently and concurrently.
 *
 * @author Will Provost
 */
public class TestProgram3 {

	public static void main(String[] args) {

		Random random = new Random();
		int soilSamplerID = random.nextInt(10000);
		int photographerID = random.nextInt(10000);
		int groundPounderID = random.nextInt(10000);

		Recorder recorder = new Recorder();
		SoilSampler soilSampler = new SoilSampler(soilSamplerID);
		Photographer photographer = new Photographer(photographerID);
		GroundPounder groundPounder = new GroundPounder(groundPounderID);

		soilSampler.setSubscriber(recorder);
		photographer.setSubscriber(recorder);
		groundPounder.setSubscriber(recorder);

		soilSampler.receiveCommands("4R2R1L2");
		photographer.receiveCommands("L4R2L1");
		groundPounder.receiveCommands("L5PR2PL1P");

		while (soilSampler.isBusy() || photographer.isBusy() || groundPounder.isBusy()) {
			soilSampler.takeNextStep();
			photographer.takeNextStep();
			groundPounder.takeNextStep();
		}

		// Printing all reports
		recorder.getReports().forEach(report -> {
			System.out.println(report);
		});

		System.out.println("**************************************");

		// Printing GroundPounder reports
		recorder.getReports().stream()
				.filter(report -> report.getSender() instanceof GroundPounder)
				.forEach(report -> {
					System.out.println(report);
				});

		System.out.println("**************************************");

		// Printing eastern half reports
		recorder.getReports().stream()
				.filter(report -> report.getX() > 0)
				.forEach(report -> {
					System.out.println(report);
				});
	}
	
}
