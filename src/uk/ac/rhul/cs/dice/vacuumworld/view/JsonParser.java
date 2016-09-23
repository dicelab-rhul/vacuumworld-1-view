package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;

import uk.ac.rhul.cs.dice.vacuumworld.view.representation.AgentRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.AvatarRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.DirtRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.FirstCoordinateRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.ObjectRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.SecondCoordinateRepresentation;

public class JsonParser {
	private JsonParser() {}
	
	public static StateForView createStateDataForView(ModelUpdate update) {
		JsonReader reader = Json.createReader(new StringReader((String) update.getPayload()));
		JsonObject state = reader.readObject();
		
		int size = state.getInt("width");
		JsonArray notableLocations = state.getJsonArray("notable_locations");
		
		//no need for now to get user and monitoring flags
		
		return createStateDataForView(size, notableLocations);
	}
	
	private static StateForView createStateDataForView(int size, JsonArray notableLocations) {
		List<String> imagesPaths = new ArrayList<>();
		
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				imagesPaths.add(fetchImagePath(i, j, notableLocations));
			}
		}
		
		return new StateForView(size, size, imagesPaths);
	}

	private static String fetchImagePath(int i, int j, JsonArray notableLocations) {
		for(JsonValue location : notableLocations) {
			if(location instanceof JsonObject) {
				if(((JsonObject) location).getInt("x") == i && ((JsonObject) location).getInt("x") == j) {
					return getImagePathFromLocation((JsonObject) location);
				}
			}
		}
		
		return "images/location.png";
	}

	private static String getImagePathFromLocation(JsonObject location) {
		if(location.containsKey("agent")) {
			JsonObject agent = location.getJsonObject("agent");
			String color = agent.getString("color");
			String direction = agent.getString("facing_direction");
			
			return getImagePathFromAgent(color, direction);
		}
		else if(location.containsKey("dirt")) {
			String color = location.getString("dirt");
			
			return getImageFromDirt(color);
		}
		else {
			return "images/location.png";
		}
	}

	private static String getImageFromDirt(String color) {
		return "images/" + color + "_dirt.png";
	}

	private static String getImagePathFromAgent(String color, String direction) {
		return "images/" + color + "_" + direction + ".png";
	}

	public static ViewRequest generateViewRequest(ViewRequestsEnum code, Object data) {
		switch(code) {
		case NEW:
			return createStartRequestFromUserDefinedTemplate(code, (String[]) data);
		case LOAD_TEMPLATE:
			return createStartRequestFromTemplate(code, data);
		case LOAD_TEMPLATE_FROM_FILE:
			return createStartRequestFromSavedTemplate(code, data);
		case GET_STATE:
			return new ViewRequest(code, null);
		case MOVE_OBSTACLE:
		case MOVE_AVATAR:
			return moveElement(code, (HttpServletRequest) data);
		case STOP:
			return sendStopSignal(code, (HttpServletRequest) data);
		default:
			throw new IllegalArgumentException("Bad view request code: " + code);
		}
	}

	private static ViewRequest createStartRequestFromSavedTemplate(ViewRequestsEnum code, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ViewRequest createStartRequestFromTemplate(ViewRequestsEnum code, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ViewRequest createStartRequestFromUserDefinedTemplate(ViewRequestsEnum code, String[] data) {
		if(data.length < 3) {
			return null;
		}
		
		int gridSize = Integer.parseInt(data[0]);
		boolean user = "yes".equals(data[1]) ? true : false;
		boolean monitoring = "yes".equals(data[2]) ? true : false;
		
		// begin_location|2|4|agent|orange|north|end_location
		// begin_location|2|9|dirt|orange|end_location
		
		List<List<ObjectRepresentation>> locationsList = new ArrayList<>();
		
		if(data.length == 4) {			
			String[] locations = data[3].split("#");
			
			for(String location : locations) {
				String[] info = location.split("|");
				locationsList.add(parseLocation(info));
			}
		}
		
		JsonObject initialState = createTemplate(gridSize, gridSize, locationsList, user, monitoring);
		dumpJson(initialState);
		
		return new ViewRequest(code, initialState.toString());
	}

	private static void dumpJson(JsonObject initialState) {
		try {
			JsonWriter writer = Json.createWriter(new FileOutputStream("/home/cloudstrife9999/workspace/VacuumWorldWeb/state.json"));
			writer.write(initialState);
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static List<ObjectRepresentation> parseLocation(String[] info) {
		List<ObjectRepresentation> toReturn = new ArrayList<>();
		
		if("begin_location".equals(info[0])) {
			parseLocation(toReturn, info);
		}
		
		return toReturn;
	}

	private static void parseLocation(List<ObjectRepresentation> toReturn, String[] info) {
		int x = Integer.parseInt(info[1]);
		int y = Integer.parseInt(info[2]);
		
		FirstCoordinateRepresentation first = new FirstCoordinateRepresentation(x);
		SecondCoordinateRepresentation second = new SecondCoordinateRepresentation(y);
		
		toReturn.add(first);
		toReturn.add(second);
		
		parseAdditionalContent(toReturn, info);
	}

	private static void parseAdditionalContent(List<ObjectRepresentation> toReturn, String[] info) {
		if("agent".equals(info[3])) {
			parseAgent(toReturn, info);
		}
		else if("dirt".equals(info[3])) {
			parseDirt(toReturn, info);
		}
	}

	private static void parseDirt(List<ObjectRepresentation> toReturn, String[] info) {
		String dirt = info[4];
		
		DirtRepresentation dirtRepresentation = new DirtRepresentation(dirt);
		toReturn.add(dirtRepresentation);
	}

	private static void parseAgent(List<ObjectRepresentation> toReturn, String[] info) {
		String id = UUID.randomUUID().toString();
		String name = id;
		String color = info[4];
		int sensorsNumber = 2;
		int actuatorsNumber = 2;
		int width = 1;
		int height = 1;
		String facingDirection = info[5];
		
		AgentRepresentation agentRepresentation = new AgentRepresentation(id, name, color, sensorsNumber, actuatorsNumber, new int[]{width, height}, facingDirection);
		toReturn.add(agentRepresentation);
	}

	private static ViewRequest sendStopSignal(ViewRequestsEnum code, HttpServletRequest request) {
		return new ViewRequest(code, null);
	}

	private static ViewRequest moveElement(ViewRequestsEnum code, HttpServletRequest request) {
		int oldX = (int) request.getAttribute("OLD_X");
		int oldY = (int) request.getAttribute("OLD_Y");
		int newX = (int) request.getAttribute("NEW_X");
		int newY = (int) request.getAttribute("NEW_Y");
		
		List<Integer> data = new ArrayList<>();
		data.add(oldX);
		data.add(oldY);
		data.add(newX);
		data.add(newY);
		
		return new ViewRequest(code, (Serializable) data);
	}

	private static ViewRequest createTemplate(ViewRequestsEnum code, HttpServletRequest request) {
		int width = (int) request.getAttribute("WIDTH");
		int height = (int) request.getAttribute("HEIGHT");
		boolean user = (boolean) request.getAttribute("USER");
		boolean monitoring = (boolean) request.getAttribute("MONITORING");
		
		Object locations = request.getAttribute("LOCATIONS");
		
		JsonObject json = createTemplate(width, height, locations, user, monitoring);
		return new ViewRequest(code, json.toString());
	}

	private static JsonObject createTemplate(int width, int height, Object locations, boolean user, boolean monitoring) {
		if(locations instanceof List<?>) {
			return createTemplateHelper(width, height, (List<?>) locations, user, monitoring);
		}
		else {
			return null;
		}
	}
	
	private static JsonObject createTemplateHelper(int width, int height, List<?> locations, boolean user, boolean monitoring) {
		List<List<?>> temp = new ArrayList<>();
		
		for(Object o : locations) {
			if(o instanceof List<?>) {
				temp.add((List<?>) o);
			}
		}
		
		return createTemplateHelperHelper(width, height, temp, user, monitoring);
	}
	
	private static JsonObject createTemplateHelperHelper(int width, int height, List<List<?>> locations, boolean user, boolean monitoring) {
		List<List<ObjectRepresentation>> temp = new ArrayList<>();
		
		for(List<?> o : locations) {
			List<ObjectRepresentation> tmp = new ArrayList<>();
			
			for(Object ob : o) {
				if(o instanceof ObjectRepresentation) {
					tmp.add((ObjectRepresentation) ob);
				}
			}
			
			if(!tmp.isEmpty()) {
				temp.add(tmp);
			}
			
		}
		
		return createTemplate(width, height, temp, user, monitoring);
	}

	private static JsonObject createTemplate(int width, int height, List<List<ObjectRepresentation>> locations, boolean user, boolean monitoring) {
		JsonArrayBuilder array = Json.createArrayBuilder();
		
		for(List<ObjectRepresentation> location : locations) {
			array.add(buildLocation(location));
		}
		
		JsonArray locationsArray = array.build();
		
		return Json.createObjectBuilder().add("width", width).add("height", height).add("user", user).add("monitoring", monitoring).add("notable_locations", locationsArray).build();
	}

	private static JsonObject buildLocation(List<ObjectRepresentation> location) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		for(ObjectRepresentation element : location) {
			if(element instanceof FirstCoordinateRepresentation) {
				builder.add("x", ((FirstCoordinateRepresentation) element).getCoordinate());
			}
			else if(element instanceof SecondCoordinateRepresentation) {
				builder.add("y", ((SecondCoordinateRepresentation) element).getCoordinate());
			}
			else if(element instanceof AgentRepresentation) {
				builder.add("agent", dumpAgent((AgentRepresentation) element));
			}
			else if(element instanceof AvatarRepresentation) {
				builder.add("avatar", dumpAvatar((AvatarRepresentation) element));
			}
			else if(element instanceof DirtRepresentation) {
				builder.add("dirt", ((DirtRepresentation) element).getColor());
			}
			else {
				continue;
			}
		}
		
		return builder.build();
	}

	private static JsonObject dumpAvatar(AvatarRepresentation avatarRepresentation) {
		String avatarId = avatarRepresentation.getId();
		String avatarName = avatarRepresentation.getName();
		String avatarColor = avatarRepresentation.getColor();
		int[] dimensions = avatarRepresentation.getDimensions();
		String avatarFacingDirection = avatarRepresentation.getFacingDirection();
		
		return Json.createObjectBuilder().add("id", avatarId).add("name", avatarName).add("color", avatarColor).add("width", dimensions[0]).add("height", dimensions[1]).add("facing_direction", avatarFacingDirection).build();
	}

	private static JsonObject dumpAgent(AgentRepresentation agentRepresentation) {
		String agentId = agentRepresentation.getId();
		String agentName = agentRepresentation.getName();
		String agentColor = agentRepresentation.getColor();
		int sensorsNumber = agentRepresentation.getSensorsNumber();
		int actuatorsNumber = agentRepresentation.getActuatorsNumber();
		int[] dimensions = agentRepresentation.getDimensions();
		String agentFacingDirection = agentRepresentation.getFacingDirection();
		
		return Json.createObjectBuilder().add("id", agentId).add("name", agentName).add("color", agentColor).add("sensors", sensorsNumber).add("actuators", actuatorsNumber).add("width", dimensions[0]).add("height", dimensions[1]).add("facing_direction", agentFacingDirection).build();
	}
}