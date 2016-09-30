package uk.ac.rhul.cs.dice.vacuumworld.view.utils;

import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
	public static final String CONTROLLER_IP = "127.0.0.1";
	public static final int CONTROLLER_PORT = 1890;
	public static final String LOGS_PATH = "/home/cloudstrife9999/workspace/VacuumWorldWeb/logs/";
	
	private Utils(){}
	
	public static void freshLog(String filename, String... toLog) {
		log(filename, false, toLog);
	}
	
	public static void log(String filename, String... toLog) {
		log(filename, true, toLog);
	}
	
	public static void log(String filename, boolean append, String... toLog) {
		try {
			FileOutputStream fo = new FileOutputStream(filename, append);
			log(fo, toLog);
			fo.close();
		}
		catch(IOException e) {
			e.printStackTrace(System.err);
		}
		
	}

	private static void log(FileOutputStream fo, String... toLog) throws IOException {
		for(String line : toLog) {
			fo.write(line.getBytes());
			fo.write("\n".getBytes());
		}
		
		fo.flush();
	}
}