package uk.ac.rhul.cs.dice.vacuumworld.view.representation;

public class DirtRepresentation extends ObjectRepresentation {
	private String color;
	
	public DirtRepresentation(String color) {
		super(null);
		this.color = color;
	}
	
	public String getColor() {
		return this.color;
	}
	
	@Override
	public String toString() {
		return "[BEGIN DIRT] color: " + getColor() + " [END DIRT]";
	}
}