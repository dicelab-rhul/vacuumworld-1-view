package uk.ac.rhul.cs.dice.vacuumworld.view.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import uk.ac.rhul.cs.dice.vacuumworld.view.utils.HandshakeCodes;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.HandshakeException;

public class Handshake {
	private static final String ERROR = "Bad handshake.";
	private static final int TIME_TO_WAIT = 10000;
	
	private Handshake(){}
	
	public static Socket attemptHanshake(String controllerIp, int controllerPort) throws HandshakeException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Future<Socket> future = executor.submit(new Callable<Socket>() {

			@Override
			public Socket call() throws Exception {
				return doHanshake(controllerIp, controllerPort);
			}
		});
		
		try {
			return future.get(TIME_TO_WAIT, TimeUnit.MILLISECONDS);
		}
		catch (Exception e) {
			throw new HandshakeException(e);
		}
	}

	private static Socket doHanshake(String controllerIp, int controllerPort) throws IOException, ClassNotFoundException {
		Socket socketWithController = new Socket(controllerIp, controllerPort);
		ObjectOutputStream output = new ObjectOutputStream(socketWithController.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(socketWithController.getInputStream());
		
		return doHanshake(socketWithController, input, output);
	}
	
	private static Socket doHanshake(Socket socketWithController, ObjectInputStream input, ObjectOutputStream output) throws IOException, ClassNotFoundException {
		output.writeObject(HandshakeCodes.VHVC);
		output.flush();
		
		Object o = input.readObject();
		
		if(o instanceof HandshakeCodes) {
			return continueHandshake(socketWithController, input, (HandshakeCodes) o);
		}
		else {
			throw new IOException(ERROR);
		}
	}

	private static Socket continueHandshake(Socket socketWithController, ObjectInputStream input, HandshakeCodes code) throws ClassNotFoundException, IOException {
		if(!HandshakeCodes.CHCV.equals(code) && !HandshakeCodes.CHMV.equals(code)) {
			throw new IllegalArgumentException(ERROR);
		}
		else {
			Object o = input.readObject();
			
			return continueHandshake(socketWithController, code, o);
		}
	}

	private static Socket continueHandshake(Socket socketWithController, HandshakeCodes firstCode, Object otherCode) throws IOException {
		if(!(otherCode instanceof HandshakeCodes)) {
			throw new IOException(ERROR);
		}
		else {
			return finalizeHandshake(socketWithController, firstCode, (HandshakeCodes) otherCode);
		}
	}

	private static Socket finalizeHandshake(Socket socketWithController, HandshakeCodes firstCode, HandshakeCodes secondCode) {
		if(HandshakeCodes.CHCV.equals(firstCode)) {
			return checkEquality(socketWithController, secondCode, HandshakeCodes.CHMV);
		}
		else if(HandshakeCodes.CHMV.equals(firstCode)) {
			return checkEquality(socketWithController, secondCode, HandshakeCodes.CHCV);
		}
		else {
			throw new IllegalArgumentException(ERROR);
		}
	}

	private static Socket checkEquality(Socket socketWithController, HandshakeCodes codeTocheck, HandshakeCodes value) {
		if(!codeTocheck.equals(value)) {
			throw new IllegalArgumentException(ERROR);
		}
		else {
			return socketWithController;
		}
	}
}