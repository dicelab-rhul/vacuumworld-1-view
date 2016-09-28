package uk.ac.rhul.cs.dice.vacuumworld.view.representation;

public class FirstCoordinateRepresentation extends ObjectRepresentation {
	private int coordinate;
	
	public FirstCoordinateRepresentation(int coordinate) {
		super(null);
		this.coordinate = coordinate;
	}
	
	public int getCoordinate() {
		return this.coordinate;
	}
	
	@Override
	public String toString() {
		return "[BEGIN FIRST COORDINATE] value: " + getCoordinate() + " [END FIRST COORDINATE]";
	}
}