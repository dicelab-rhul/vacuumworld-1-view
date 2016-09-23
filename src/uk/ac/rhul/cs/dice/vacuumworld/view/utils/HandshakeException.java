package uk.ac.rhul.cs.dice.vacuumworld.view.utils;

public class HandshakeException extends Exception {
	private static final long serialVersionUID = -4624403516774599470L;
	
	public HandshakeException(Exception e) {
		this.initCause(e);
	}
	
	public HandshakeException(String message) {
		super(message);
	}
}