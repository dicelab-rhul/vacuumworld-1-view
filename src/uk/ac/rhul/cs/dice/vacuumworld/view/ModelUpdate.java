package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.Serializable;

public class ModelUpdate implements Serializable {
	private static final long serialVersionUID = -1661560249889662249L;
	private Serializable payload;
	
	public ModelUpdate(Serializable payload) {
		this.payload = payload;
	}

	public Object getPayload() {
		return this.payload;
	}
}