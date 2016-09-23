package uk.ac.rhul.cs.dice.vacuumworld.view.representation;

public abstract class ObjectRepresentation {
	private String id;
	
	public ObjectRepresentation(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
}