package uk.ac.rhul.cs.dice.vacuumworld.view.session;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VacuumWorldCommunicationSession {
	private Socket socketWithController;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private boolean sessionRequested;
	
	public VacuumWorldCommunicationSession() {
		this.sessionRequested = true;
	}
	
	public void setSocketWithController(Socket socket) throws IOException {
		this.socketWithController = socket;
		this.input = new ObjectInputStream(socket.getInputStream());
		this.output = new ObjectOutputStream(socket.getOutputStream());
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