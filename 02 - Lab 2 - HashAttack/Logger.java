package HashAttack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	private long totalDuration;
	private long testDuration;
	BufferedWriter writer;
	
	public Logger() {
		try {
			totalDuration = 0;
			testDuration = 0;
			writer = new BufferedWriter(new FileWriter("output.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(String inputText) {
		try {
			writer.write(inputText);
			System.out.println(inputText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newLine() {
		try {
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startTestTimer() {
		testDuration = System.nanoTime();
	}
	
	public void stopTestTimer() {
		testDuration = System.nanoTime() - testDuration;
	}
	
	public double getTestNanoSeconds() {
		return testDuration;
	}
	
	public double getTestSeconds() {
		double seconds = (double)testDuration / 1000000000.0;
		return seconds;
	}
	
	
	public void startTotalTimer() {
		totalDuration = System.nanoTime();
	}
	
	public void stopTotalTimer() {
		totalDuration = System.nanoTime() - totalDuration;
	}
	
	public double getTotalNanoSeconds() {
		return totalDuration;
	}
	
	public double getTotalSeconds() {
		double seconds = (double)totalDuration / 1000000000.0;
		return seconds;
	}

}
