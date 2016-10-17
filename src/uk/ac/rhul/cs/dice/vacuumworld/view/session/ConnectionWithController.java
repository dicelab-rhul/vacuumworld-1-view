package uk.ac.rhul.cs.dice.vacuumworld.view.session;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ConnectionWithController implements Serializable {
	private static final long serialVersionUID = -1708021650155359519L;
	private Socket socketWithController;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private boolean sessionRequested;
	
	public ConnectionWithController() {
		this.sessionRequested = true;
	}
	
	public void setSocketWithController(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
		this.socketWithController = socket;
		this.input = input;
		this.output = output;
	}

	public Socket getSocketWithController() {
		return this.socketWithController;
	}

	public ObjectInputStream getInput() {
		return this.input;
	}

	public ObjectOutputStream getOutput() {
		return this.output;
	}

	public boolean isSessionRequested() {
		return this.sessionRequested;
	}
}
