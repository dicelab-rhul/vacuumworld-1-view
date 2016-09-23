package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.Serializable;

public class ViewRequest implements Serializable {
	private static final long serialVersionUID = 4938859981860461534L;
	private ViewRequestsEnum code;
	private Serializable payload;
	
	public ViewRequest(ViewRequestsEnum code, Serializable payload) {
		this.code = code;
		this.payload = payload;
	}

	public ViewRequestsEnum getCode() {
		return this.code;
	}

	public Object getPayload() {
		return this.payload;
	}
}