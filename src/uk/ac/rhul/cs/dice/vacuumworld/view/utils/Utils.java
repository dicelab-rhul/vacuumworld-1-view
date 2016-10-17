package uk.ac.rhul.cs.dice.vacuumworld.view.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.VacuumWorldLogFormatter;

public class Utils {
	private static final Logger LOGGER = initLogger();
	
	public static final String AGENT = "agent";
	public static final String COLOR = "color";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String DIRT = "dirt";
	public static final String AVATAR = "avatar";
	public static final String FACING_DIRECTION = "facing_direction";
	public static final String WITDH = "width";
	public static final String HEIGHT = "height";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String MONITORING = "monitoring";
	public static final String NOTABLE_LOCATIONS = "notable_locations";
	public static final String BEGIN_LOCATION = "begin_location";
	public static final String END_LOCATION = "end_location";
	public static final String SIZE = "size";
	public static final String CONNECTION = "CONNECTION";
	public static final String CONNECTED_FLAG = "CONNECTED_FLAG";
	public static final String TEMPLATE = "TEMPLATE";
	public static final String TEMPLATE_FILE = "TEMPLATE_FILE";
	public static final String GRID = "GRID";
	public static final String INITIAL_STATE_ARRAY = "INITIAL[]";
	public static final String REQUEST_CODE = "REQUEST_CODE";
	public static final String NEW = "NEW";
	public static final String ERROR = "ERROR";
	public static final String FIRST_CYCLE_DONE = "FIRST_CYCLE_DONE";
	
	private Utils(){}
	
	private static Logger initLogger() {
		Logger logger = Logger.getAnonymousLogger();
		logger.setUseParentHandlers(false);
		VacuumWorldLogFormatter formatter = new VacuumWorldLogFormatter();
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		
		return logger;
	}
	
	public static void log(String message) {
		log(Level.INFO, message);
	}
	
	public static void log(Exception e) {
		if(e.getMessage() != null) {
			log(e.getMessage(), e);
		}
	}
	
	public static void log(Exception e, String className) {
		if(e.getMessage() != null) {
			log(className + ": " + e.getMessage(), e);
		}
	}
	
	public static void fakeLog(Exception e) {
		log(e);
		//this exception does not need to be logged
	}

	public static void log(String message, Exception e) {
		log(Level.SEVERE, e.getClass().getCanonicalName() + ": " + message, e);
		log(Level.SEVERE, e.getClass().getCanonicalName() + ": " + e.getStackTrace(), e);
	}

	public static void log(Level level, String message) {
		Utils.LOGGER.log(level, message);
	}

	public static void log(Level level, String message, Exception e) {
		Utils.LOGGER.log(level, message, e);
	}
	
	public static void logWithClass(String source, String message) {
		log(source + ": " + message);
	}
	
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
			Utils.log(e);
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