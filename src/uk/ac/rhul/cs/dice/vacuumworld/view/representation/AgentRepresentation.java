package uk.ac.rhul.cs.dice.vacuumworld.view.representation;

public class AgentRepresentation extends AvatarRepresentation {
	private int sensorsNumber;
	private int actuatorsNumber;
	
	public AgentRepresentation(String id, String name, String color, int sensorsNumber, int actuatorsNumber, int[] dimensions, String facingDirection) {
		super(id, name, color, dimensions, facingDirection);
		this.sensorsNumber = sensorsNumber;
		this.actuatorsNumber = actuatorsNumber;
	}

	public int getSensorsNumber() {
		return this.sensorsNumber;
	}

	public int getActuatorsNumber() {
		return this.actuatorsNumber;
	}
}