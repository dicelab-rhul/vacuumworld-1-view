package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import uk.ac.rhul.cs.dice.vacuumworld.view.session.requests.ViewRequestConcreteFactory;
import uk.ac.rhul.cs.dice.vacuumworld.view.session.requests.ViewRequestInterface;
import uk.ac.rhul.cs.dice.vacuumworld.view.session.requests.ViewRequestsFactory;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.ConfigData;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ModelUpdate;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public class JsonParser {
	public static final ViewRequestConcreteFactory requestsFactory = new ViewRequestConcreteFactory();
	
	private JsonParser() {}
	
	public static StateForView createStateDataForView(ModelUpdate update) throws FileNotFoundException {
		JsonReader reader = Json.createReader(new StringReader((String) update.getPayload()));
		JsonObject state = reader.readObject();
		
		reader.close();
		
		int size = state.getInt(Utils.SIZE);
		JsonArray notableLocations = state.getJsonArray(Utils.NOTABLE_LOCATIONS);
		
		return createStateDataForView(size, notableLocations);
	}
	
	private static StateForView createStateDataForView(int size, JsonArray notableLocations) {
		List<String> imagesPaths = new ArrayList<>();
		
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				imagesPaths.add(fetchImagePath(j, i, notableLocations));
			}
		}
		
		return new StateForView(size, size, imagesPaths);
	}

	private static String fetchImagePath(int i, int j, JsonArray notableLocations) {
		for(JsonValue location : notableLocations) {
			if(!(location instanceof JsonObject)) {
				continue;
			}
			if(((JsonObject) location).getInt(Utils.X) == i && ((JsonObject) location).getInt(Utils.Y) == j) {
				return getImagePathFromLocation((JsonObject) location);
			}
		}
		
		return ConfigData.getDefaultLocationImagePath();
	}

	private static String getImagePathFromLocation(JsonObject location) {
		if(location.containsKey(Utils.AGENT)) {
			JsonObject agent = location.getJsonObject(Utils.AGENT);
			String color = agent.getString(Utils.COLOR);
			String direction = agent.getString(Utils.FACING_DIRECTION);
			
			return getImagePathFromAgent(color, direction);
		}
		else if(location.containsKey(Utils.USER)) {
			JsonObject user = location.getJsonObject(Utils.USER);
			String direction = user.getString(Utils.FACING_DIRECTION);
			
			return getImagePathFromUser(direction);
		}
		else if(location.containsKey(Utils.DIRT)) {
			String color = location.getString(Utils.DIRT);
			
			return getImageFromDirt(color);
		}
		else {
			return ConfigData.getDefaultLocationImagePath();
		}
	}

	private static String getImagePathFromUser(String direction) {
		return "images/user_" + direction + ".png";
	}

	private static String getImageFromDirt(String color) {
		return "images/" + color + "_dirt.png";
	}

	private static String getImagePathFromAgent(String color, String direction) {
		return "images/" + color + "_" + direction + ".png";
	}

	public static ViewRequest generateViewRequestForController(ViewRequestsEnum code, Object data) {		
		ViewRequestInterface request = ViewRequestsFactory.generateViewRequest(code, data);
		
		return request.accept(JsonParser.requestsFactory);
	}
}