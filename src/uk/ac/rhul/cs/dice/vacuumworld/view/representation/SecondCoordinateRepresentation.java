package uk.ac.rhul.cs.dice.vacuumworld.view.representation;

public class SecondCoordinateRepresentation extends ObjectRepresentation {
private int coordinate;
	
	public SecondCoordinateRepresentation(int coordinate) {
		super(null);
		this.coordinate = coordinate;
	}
	
	public int getCoordinate() {
		return this.coordinate;
	}
	
	@Override
	public String toString() {
		return "[BEGIN SECOND COORDINATE] value: " + getCoordinate() + " [END SECOND COORDINATE]";
	}
}