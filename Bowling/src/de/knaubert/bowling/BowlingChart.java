package de.knaubert.bowling;

import java.util.Scanner;

/*
 * Object to generate bowling chart
 * 
 * @author M.Knaubert
 */

public class BowlingChart {

	int frameArraySize = 10;
	Frame[] frameArray = new Frame[frameArraySize];
	String cRlF = System.getProperty("line.separator");

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
		System.out.println(cRlF);
		System.out.println("Ready to start game (automatic)???");
		this.waitForKeyPressed("Press ENTER to continue!");

		this.startPlay(false); //true = automatic, false = interactive
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
		return cRlF + firstLine + cRlF + secondLine;
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
		//return 10; // max test... result must be 300
	}

	/*
	 * User enters number to roll
	 */
	@SuppressWarnings("resource")
	private int getNumberFromConsole(int frameNr, int rollNr, int max) {
		int number = 0;
		String message = "Enter points for roll '" + rollNr + "' in frame '" + (frameNr+1) + "' (0-" + max + "):";
		Scanner in = new Scanner(System.in);
		do
		{		
			System.out.println(message);
			number = in.nextInt();	
		}
		while(number > max);	
		//in.close();	
		if (number == 10)
			System.out.println("STRIKE !!!");
		return number;
	}
	
	
	/*
	 * Start automatic bowling with random values
	 */
	public void startPlay(boolean isAutomatic) {
		boolean isPreviousSpare = false;
		int isPreviousStrike = 0;
		int count = 0;
		//Iterate through all frames
		while (count <= 9) {
			int zw = 0;

			// current frame
			Frame currFrame = frameArray[count];

			// first roll
			int first = 0;
			if (isAutomatic)
				first = getRandomRoll(10);
			else
				first = getNumberFromConsole(count, 1, 10);
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
				int second = 0;
				
				if (isAutomatic)
					second = getRandomRoll(10 - first);
				else
					second = getNumberFromConsole(count, 2, 10-first);
				
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
				int second = 0;
				if (isAutomatic)
					second = getRandomRoll(10);
				else
					second = getNumberFromConsole(count-1, 2, 10);
				frameArray[count - 1].secondRoll = second;
				addBonusForFrame(count -2, second);
				addBonusForFrame(count -1, second);					
			}
			int third = 0;
			if (isAutomatic)
				third = getRandomRoll(10);
			else
				third = getNumberFromConsole(count-1, 3, 10);
			frameArray[count -1].additionalRoll = third;
			frameArray[count -1].points += third; 	//added normal number
			addBonusForFrame(count -1, third); 		//added as bonus
		}
		if (isPreviousSpare)
		{
			int third = 0;
			if (isAutomatic)
				third = getRandomRoll(10);
			else
				third = getNumberFromConsole(count-1, 3, 10);
			frameArray[count -1].additionalRoll = third;
			addBonusForFrame(count -1, third);
		}
	}


	/*
	 * Wait for keyboard entry in console
	 */
	@SuppressWarnings("resource")
	public void waitForKeyPressed(String message) {
		Scanner scan = new Scanner(System.in);
		System.out.println(message);
		scan.nextLine();
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
