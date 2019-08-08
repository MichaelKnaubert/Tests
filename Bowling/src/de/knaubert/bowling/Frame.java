package de.knaubert.bowling;

/*
 * Object for a bowling frame
 * 
 * @author	M.Knaubert
 * 
 */


public class Frame {

	int firstRoll;		//first roll of a frame
	int secondRoll;
	int additionalRoll;
	int points;
		
	
	/*
	 * Getter and setter
	 */
	public int getFirstRoll() {
		return firstRoll;
	}
	public void setFirstRoll(int firstRoll) {
		this.firstRoll = firstRoll;
	}
	public int getSecondRoll() {
		return secondRoll;
	}
	public void setSecondRoll(int secondRoll) {
		this.secondRoll = secondRoll;
	}
	public int getAdditionalRoll() {
		return additionalRoll;
	}
	public void setAdditionalRoll(int additionalRoll) {
		this.additionalRoll = additionalRoll;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}		
}
