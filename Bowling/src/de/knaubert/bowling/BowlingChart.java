package de.knaubert.bowling;

import java.util.Scanner;

/*
 * Object for bowling chart
 * 
 * @author M.Knaubert
 */

public class BowlingChart {

	int frameArraySize = 10;
	Frame[] frameArray = new Frame[frameArraySize];

	/*
	 * Constructor
	 */
	public BowlingChart() {
		init();
	}

	/*
	 * Initialization
	 */
	private void init() {
		initFrameArray();
		System.out.println("Bowling-Game");
		System.out.println("V 0.02");
		System.out.println("by M.Knaubert");
		System.out.println(System.getProperty("line.separator"));
		System.out.println("Ready to start game (automatic)???");
		this.waitForKeyPressed("Press ENTER to continue!");

		this.startAutomaticPlay();
	}

	/*
	 * Initialize FrameArray
	 */
	private void initFrameArray() {
		for (int i = 0; i < frameArraySize; i++) {
			frameArray[i] = new Frame();
			frameArray[i].setFirstRoll(0);
			frameArray[i].setSecondRoll(0);
			frameArray[i].setAdditionalRoll(0);
			frameArray[i].setPoints(0);
		}
	}

	/*
	 * Prints out BowlingChart object
	 */
	public String toString() {
		String firstLine = "|";
		String secondLine = "|";

		// loop through array
		for (int i = 0; i < frameArraySize; i++) {
			if (frameArray[i].firstRoll != 10) {
				if (frameArray[i].firstRoll + frameArray[i].secondRoll != 10) {
					firstLine += " " + frameArray[i].firstRoll + " |_" + frameArray[i].secondRoll + "_|";
				} else {
					firstLine += " " + frameArray[i].firstRoll + " |_/_|";
				}
			} else {
				if (i < 9)
					firstLine += "   |_X_|";
				else {
					String added = " X ";
					if (frameArray[i].secondRoll == 10) {
						added += "X ";
						String third = "X";
						if (frameArray[i].additionalRoll != 10)
							third = Integer.toString(frameArray[i].additionalRoll);
						added += third;
					} else
						added += frameArray[i].secondRoll;
					firstLine += added + " |";
				}
			}

			String tmpSecond = Integer.toString(frameArray[i].points).trim();
			switch (tmpSecond.length()) {
			case 1:
				tmpSecond = tmpSecond + " ";
			case 2:
				tmpSecond = " " + tmpSecond;
			case 3:
				tmpSecond = "  " + tmpSecond + "  ";
			}
			secondLine += tmpSecond + "|";
		}
		return firstLine + System.getProperty("line.separator") + secondLine;
	}

	/*
	 * Add bonus for specified frame
	 */
	public void addBonusForFrame(int index, int bonus) {
		frameArray[index].points += bonus;
	}

	/*
	 * Generate random number with max. value
	 */
	private int getRandomRoll(int max) {
		return (int) (Math.random() * (max + 1));
		// return 10;
	}

	/*
	 * Start automatic bowling with random values
	 */
	public void startAutomaticPlay() {
		boolean isPreviousSpare = false;
		int isPreviousStrike = 0;
		int count = 0;
		//Iterate through all frames
		while (count <= 9) {
			int zw = 0;

			// current frame
			Frame currFrame = frameArray[count];

			// first roll
			int first = getRandomRoll(10);
			currFrame.firstRoll = first;

			// bonus previous roll was spare?
			if (isPreviousSpare) {
				addBonusForFrame(count - 1, first);
				isPreviousSpare = false;
			} else {
				// bonus from previous strike
				if (isPreviousStrike > 0) {
					addBonusForFrame(count - 1, first);
					while ((isPreviousStrike > 2) && (count > 1)) {
						addBonusForFrame(count - 2, first);
						addBonusForFrame(count - 1, first);
						isPreviousStrike--;
					}
					isPreviousStrike--;
				}
			}

			// Strike!
			if (first == 10) {
				isPreviousStrike += 2;
				zw = 10;
			} else {
				// no strike
				zw = first;

				// second roll
				int second = getRandomRoll(10 - first);
				currFrame.secondRoll = second;

				// bonus for previous strike
				if (isPreviousStrike > 0) {
					addBonusForFrame(count - 1, second);
					isPreviousStrike--;
				}

				// Spare!
				if ((first + second) == 10)
					isPreviousSpare = true;
				else
					isPreviousSpare = false;
				zw = first + second;
			}

			currFrame.points = zw;

			// is not first roll - add previous points...
			if (count > 0)
				currFrame.points += frameArray[count - 1].points;
			count++;
		}
		// last frame
		if (isPreviousStrike > 0) {
			if (isPreviousStrike > 1) {
				int second = getRandomRoll(10);
				frameArray[count - 1].secondRoll = second;
				addBonusForFrame(count -2, second);
				addBonusForFrame(count -1, second);
			}
			int third = getRandomRoll(10);
			frameArray[count -1].additionalRoll = third;
			addBonusForFrame(count - 1, third);
			frameArray[count -1].points += third;
		}
	}

	/*
	 * Wait for keyboard entry in console
	 */
	public void waitForKeyPressed(String message) {
		Scanner scan = new Scanner(System.in);
		System.out.println(message);
		scan.nextLine();
		scan.close();
	}

	
	
	/*
	 * main method 
	 */
	public static void main(String[] args) {

		BowlingChart chart = new BowlingChart();
		System.out.println(chart.toString());
		System.out.println("This is your bowling chart!");

	}
}
