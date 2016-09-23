package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * 
 * 
 * WARNING: obsolete class
 * 
 * 
 * 
 */
public class SessionRunnable implements Runnable {
	private Navigation currentViewRequest;
	private Navigation newViewRequest;
	private static ObjectInputStream i;
	private static ObjectOutputStream o;
	
	public void setNewViewRequest(Navigation newRequest) {
		this.newViewRequest = newRequest;
	}
	
	@Override
	public void run() {
		try {
			this.currentViewRequest = this.newViewRequest;
			
			String code = (String) this.currentViewRequest.getRequest().getAttribute("TYPE");
			ViewRequestsEnum requestCode = ViewRequestsEnum.fromString(code);
			Serializable payload = createPayload(requestCode);
			ViewRequest viewRequest = new ViewRequest(requestCode, payload);
			
			this.o.writeObject(viewRequest);
			this.o.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			this.currentViewRequest = null;
			this.newViewRequest = null;
		}
	}

	private Serializable createPayload(ViewRequestsEnum requestCode) {
		// TODO Auto-generated method stub
		return null;
	}
}