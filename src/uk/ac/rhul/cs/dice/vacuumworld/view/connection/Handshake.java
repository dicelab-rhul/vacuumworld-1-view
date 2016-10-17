package uk.ac.rhul.cs.dice.vacuumworld.view.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import uk.ac.rhul.cs.dice.vacuumworld.view.utils.ConfigData;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.HandshakeCodes;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.HandshakeException;

public class Handshake {
	private static final String ERROR = "Bad handshake.";
	
	private Handshake(){}
	
	public static Boolean attemptHandshake(ObjectOutputStream output, ObjectInputStream input) throws HandshakeException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> future = executor.submit(() -> doHanshake(output, input));
		
		try {
			return future.get(ConfigData.getTimeoutInSeconds(), TimeUnit.SECONDS);
		}
		catch (Exception e) {
			throw new HandshakeException(e);
		}
	}
	
	private static Boolean doHanshake(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
		output.writeObject(HandshakeCodes.VHVC.toString());
		output.flush();
		
		Utils.logWithClass(Handshake.class.getSimpleName(), "Sent VHVC to controller");
		
		HandshakeCodes code = HandshakeCodes.fromString((String) input.readObject());
		Utils.logWithClass(Handshake.class.getSimpleName(), "Received " + (code == null ? null : code.toString()) + " from controller");
		
		if(code != null) {
			return continueHandshake(input, code);
		}
		else {
			throw new IOException(Handshake.ERROR);
		}
	}

	private static Boolean continueHandshake(ObjectInputStream input, HandshakeCodes code) throws ClassNotFoundException, IOException {
		if(!HandshakeCodes.CHCV.equals(code) && !HandshakeCodes.CHMV.equals(code)) {
			throw new IllegalArgumentException(Handshake.ERROR);
		}
		else {
			HandshakeCodes otherCode = HandshakeCodes.fromString((String) input.readObject());
			Utils.logWithClass(Handshake.class.getSimpleName(), "Received " + (otherCode == null ? null : otherCode.toString()) + " from controller");
			
			return continueHandshake(code, otherCode);
		}
	}

	private static Boolean continueHandshake(HandshakeCodes firstCode, HandshakeCodes otherCode) throws IOException {
		if(otherCode == null) {
			throw new IOException(Handshake.ERROR);
		}
		else {
			return finalizeHandshake(firstCode, otherCode);
		}
	}

	private static Boolean finalizeHandshake(HandshakeCodes firstCode, HandshakeCodes secondCode) {
		if(HandshakeCodes.CHCV.equals(firstCode)) {
			return checkEquality(secondCode, HandshakeCodes.CHMV);
		}
		else if(HandshakeCodes.CHMV.equals(firstCode)) {
			return checkEquality(secondCode, HandshakeCodes.CHCV);
		}
		else {
			throw new IllegalArgumentException(Handshake.ERROR);
		}
	}

	private static Boolean checkEquality(HandshakeCodes codeTocheck, HandshakeCodes value) {
		if(!codeTocheck.equals(value)) {
			throw new IllegalArgumentException(Handshake.ERROR);
		}
		else {
			return true;
		}
	}
}