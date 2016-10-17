package uk.ac.rhul.cs.dice.vacuumworld.view.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ConfigData {
	private static String defaultLocationImagePath;
	private static String controllerIp;
	private static int controllerPort;
	private static String logsPath;
	private static int timeoutInSeconds;
	
	private static String indexPage = "index.jsp";
	private static String mainPage = "main.jsp";
	private static String gridPage = "grid.jsp";
	
	private ConfigData() {}

	public static String getDefaultLocationImagePath() {
		return ConfigData.defaultLocationImagePath;
	}

	public static String getControllerIp() {
		return ConfigData.controllerIp;
	}

	public static int getControllerPort() {
		return ConfigData.controllerPort;
	}

	public static String getLogsPath() {
		return ConfigData.logsPath;
	}

	public static String getIndexPage() {
		return ConfigData.indexPage;
	}

	public static String getMainPage() {
		return ConfigData.mainPage;
	}

	public static String getGridPage() {
		return ConfigData.gridPage;
	}
	
	public static int getTimeoutInSeconds() {
		return ConfigData.timeoutInSeconds;
	}
	
	public static boolean initConfigData(InputStream input) {
		try(JsonReader reader = Json.createReader(new InputStreamReader(input))) {
			return initData(reader);
		}
		catch(Exception e) {
			Utils.log(e);
			
			return false;
		}
	}

	private static boolean initData(JsonReader reader) {
		JsonObject config = reader.readObject();
		
		ConfigData.defaultLocationImagePath = config.getString("default_location_image_path");
		ConfigData.controllerIp = config.getString("controller_ip");
		ConfigData.controllerPort = config.getInt("controller_port");
		ConfigData.logsPath = config.getString("logs_path");
		ConfigData.timeoutInSeconds = config.getInt("timeout_in_seconds");
		ConfigData.indexPage = config.getString("index_page");
		ConfigData.mainPage = config.getString("main_page");
		ConfigData.gridPage = config.getString("grid_page");
		
		return true;
	}
}