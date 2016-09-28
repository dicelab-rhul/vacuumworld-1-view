package uk.ac.rhul.cs.dice.vacuumworld.view.utils;

import java.io.FileOutputStream;

public class Utils {
	public static final String CONTROLLER_IP = "127.0.0.1";
	public static final int CONTROLLER_PORT = 1890;
	public static final String LOGS_PATH = "/home/cloudstrife9999/workspace/VacuumWorldWeb/logs/";
	
	private Utils(){}
	
	public static void log(String filename, boolean append, String... toLog) {
		try {
			FileOutputStream fo = new FileOutputStream(filename, append);
			
			for(String line : toLog) {
				fo.write(line.getBytes());
				fo.write("\n".getBytes());
			}
			
			fo.flush();
			fo.close();
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
}