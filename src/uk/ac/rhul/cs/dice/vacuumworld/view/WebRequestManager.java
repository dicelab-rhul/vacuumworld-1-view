package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * 
 * 
 * WARNING: obsolete class
 * 
 * 
 * 
 */
public class WebRequestManager {
	private static Socket connectionWithController;
	private static InputStream input;
	private static OutputStream output;
	private static ObjectInputStream i;
	private static ObjectOutputStream o;
	private static WebRequestManager instance;
	private static boolean session;
	private static Navigation currentViewRequest;	
	
	private WebRequestManager() {
		session = false;
		currentViewRequest = null;
	}
	
	public static WebRequestManager getInstance(String controllerIp, int controllerPort) throws IOException {
		if(instance == null) {
			instance = new WebRequestManager();
		}
		
		if(connectionWithController != null) {
			tryConnection(controllerIp, controllerPort);
		}
		
		return instance;
	}

	private static void tryConnection(String controllerIp, int controllerPort) throws IOException {
		if(!connectionWithController.isConnected() || connectionWithController.isClosed()) {
			connectionWithController = new Socket(controllerIp, controllerPort);
			input = connectionWithController.getInputStream();
			output = connectionWithController.getOutputStream();
			i = new ObjectInputStream(input);
			o = new ObjectOutputStream(output);
		}
	}
	
	public static void doJob(HttpServletRequest request, HttpServletResponse response) {
		currentViewRequest = new Navigation(request, response);
	}
	
	public static void doAsynchronousJob(HttpServletRequest request, HttpServletResponse response) {
		currentViewRequest = new Navigation(request, response);
	}
	
	public static boolean isSession() {
		return session;
	}
}