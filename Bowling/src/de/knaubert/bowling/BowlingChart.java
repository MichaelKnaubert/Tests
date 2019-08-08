package de.knaubert.bowling;

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
		initFrameArray();
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
				else
					firstLine += "   X " + frameArray[i].additionalRoll + "|";
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
	
	public void addBonusForFrame(int index, int bonus)
	{
		frameArray[index].points += bonus;
	}
	
	

	private int getRandomRoll(int max) {
		return (int) (Math.random() * (max + 1));
	}

	public void startAutomaticPlay()
	{
		boolean isPreviousSpare = false;
		int isPreviousStrike = 0;
		int count=0;
		while (count<=9)
		{
			int zw=0;
			
			Frame currFrame = frameArray[count];
			
			//first roll
			int first = getRandomRoll(10);
			currFrame.firstRoll = first;
			
			//bonus previous roll was spare or strike?
			if (isPreviousSpare || (isPreviousStrike>0))
			{
				frameArray[count -1].points += first;
				if (isPreviousSpare)
					isPreviousSpare = false;
				if (isPreviousStrike>0)
					isPreviousStrike--;
			}
			
			//Strike!
			if (first == 10) 
			{
				isPreviousStrike =2;
				zw = 10;
			}
			else
			{
				zw = first;
				
				//second roll
				int second = getRandomRoll(10-first);
				currFrame.secondRoll = second;
				
				//bonus for previous strike
				if (isPreviousStrike >0)
				{
					frameArray[count -1].points += second;
					isPreviousStrike --;
				}				
				
				//Spare!
				if ((first + second) == 10)
					isPreviousSpare = true;
				else
					isPreviousSpare = false;
				zw = first + second;
			}
			
			currFrame.points = zw;
			
			//is not first roll -  add previous points...
			if (count > 0)
				currFrame.points += frameArray[count -1].points;
			count++;
		}
		
		//last frame
		if (isPreviousStrike > 0)
		{
			int third = getRandomRoll(10);
			frameArray[count-1].additionalRoll = third;
			frameArray[count-1].points = frameArray[count-1].points + third;
		}
	}
		
	
	public static void main(String[] args) {

		BowlingChart chart = new BowlingChart();

		// Test chart.ToString() with different numbers
//		for(int i = 0; i<=9; i++)
//		{
//			chart.addBonusForFrame(i, chart.getRandomRoll(300));
//		}		
		
		chart.startAutomaticPlay();
		
		
		System.out.println(chart.toString());
	}
}
