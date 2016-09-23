package uk.ac.rhul.cs.dice.vacuumworld.view.representation;

public class AvatarRepresentation extends ObjectRepresentation {
	private String name;
	private String color;
	private int[] dimensions;
	private String facingDirection;
	
	public AvatarRepresentation(String id, String name, String color, int[] dimensions, String facingDirection) {
		super(id);
		this.name = name;
		this.color = color;
		this.dimensions = dimensions;
		this.facingDirection = facingDirection;
	}

	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
	}

	public int[] getDimensions() {
		return this.dimensions;
	}

	public String getFacingDirection() {
		return this.facingDirection;
	}
}