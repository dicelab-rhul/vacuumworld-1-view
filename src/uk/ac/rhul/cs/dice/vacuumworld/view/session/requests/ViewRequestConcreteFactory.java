package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;

import uk.ac.rhul.cs.dice.vacuumworld.view.representation.AgentRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.AvatarRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.DirtRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.FirstCoordinateRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.ObjectRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.representation.SecondCoordinateRepresentation;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.ConfigData;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public class ViewRequestConcreteFactory implements ViewRequestsVisitorInterface {

	@Override
	public ViewRequest generate(NewRequest request) {
		return createStartRequestFromUserDefinedTemplate(request.getCode(), (String[]) request.getData());
	}

	@Override
	public ViewRequest generate(LoadTemplateRequest request) {
		return null;
	}

	@Override
	public ViewRequest generate(LoadTemplateFromFileRequest request) {
		return null;
	}

	@Override
	public ViewRequest generate(GetStateRequest request) {
		return new ViewRequest(request.getCode(), null);
	}

	@Override
	public ViewRequest generate(MoveObstacleRequest request) {
		return moveElement(request.getCode(), (HttpServletRequest) request.getData());
	}

	@Override
	public ViewRequest generate(MoveAvatarRequest request) {
		return moveElement(request.getCode(), (HttpServletRequest) request.getData());
	}

	@Override
	public ViewRequest generate(StopControllerRequest request) {
		return sendStopSignal(request.getCode());
	}

	@Override
	public ViewRequest generate(StopForwardRequest request) {
		return sendStopSignal(request.getCode());
	}
	
	private static ViewRequest createStartRequestFromUserDefinedTemplate(ViewRequestsEnum code, String[] data) {
		Utils.log(ConfigData.getLogsPath() + "initial_state.txt", false, data);
		
		if(data.length < 3) {
			return null;
		}
		else {
			return createStartRequest(code, data);
		}
	}
	
	private static ViewRequest createStartRequest(ViewRequestsEnum code, String[] data) {
		int gridSize = Integer.parseInt(data[0]);
		boolean user = "yes".equals(data[1]) ? true : false;
		boolean monitoring = "yes".equals(data[2]) ? true : false;
		
		List<List<ObjectRepresentation>> locationsList = new ArrayList<>();
		
		if(data.length == 4) {
			addLocation(locationsList, data);
		}
		
		JsonObject initialState = createTemplate(gridSize, gridSize, locationsList, user, monitoring);
		dumpJson(initialState);
		
		return new ViewRequest(code, initialState.toString());
	}

	private static void addLocation(List<List<ObjectRepresentation>> locationsList, String[] data) {
		if(data[3].length() > 0) {
			String[] locations = data[3].split("#");
			
			for(String location : locations) {
				String[] info = location.split("\\|");
				locationsList.add(parseLocation(info));
			}
		}
	}

	private static void dumpJson(JsonObject initialState) {
		try(JsonWriter writer = Json.createWriter(new FileOutputStream("logs/initial_state_view.json"))) {
			writer.write(initialState);
		}
		catch(Exception e) {
			Utils.log(e);
		}
	}

	private static List<ObjectRepresentation> parseLocation(String[] info) {
		List<ObjectRepresentation> toReturn = new ArrayList<>();
		
		if(Utils.BEGIN_LOCATION.equals(info[0])) {
			toReturn = parseLocation(toReturn, info);
		}
		else {
			throw new IllegalArgumentException(info[0]);
		}
		
		return toReturn;
	}

	private static List<ObjectRepresentation> parseLocation(List<ObjectRepresentation> toReturn, String[] info) {
		int x = Integer.parseInt(info[1]);
		int y = Integer.parseInt(info[2]);
		
		FirstCoordinateRepresentation first = new FirstCoordinateRepresentation(x);
		SecondCoordinateRepresentation second = new SecondCoordinateRepresentation(y);
		
		toReturn.add(first);
		toReturn.add(second);
		
		return parseAdditionalContent(toReturn, info);
	}

	private static List<ObjectRepresentation> parseAdditionalContent(List<ObjectRepresentation> toReturn, String[] info) {
		if(Utils.AGENT.equals(info[3])) {
			return parseAgent(toReturn, info);
		}
		else if(Utils.DIRT.equals(info[3])) {
			return parseDirt(toReturn, info);
		}
		else {
			return toReturn;
		}
	}

	private static List<ObjectRepresentation> parseDirt(List<ObjectRepresentation> toReturn, String[] info) {
		String dirt = info[4];
		
		DirtRepresentation dirtRepresentation = new DirtRepresentation(dirt);
		toReturn.add(dirtRepresentation);
		
		return toReturn;
	}

	private static List<ObjectRepresentation> parseAgent(List<ObjectRepresentation> toReturn, String[] info) {
		String id = "Agent-" + UUID.randomUUID().toString();
		String name = id;
		String color = info[4];
		int sensorsNumber = 2;
		int actuatorsNumber = 2;
		int width = 1;
		int height = 1;
		String facingDirection = info[5];
		
		AgentRepresentation agentRepresentation = new AgentRepresentation(id, name, color, sensorsNumber, actuatorsNumber, new int[]{width, height}, facingDirection);
		toReturn.add(agentRepresentation);
		
		return toReturn;
	}

	private static JsonObject createTemplate(int width, int height, List<List<ObjectRepresentation>> locations, boolean user, boolean monitoring) {
		JsonArrayBuilder array = Json.createArrayBuilder();
		
		for(List<ObjectRepresentation> location : locations) {
			array.add(buildLocation(location));
		}
		
		JsonArray locationsArray = array.build();
		
		return Json.createObjectBuilder().add(Utils.WITDH, width).add(Utils.HEIGHT, height).add(Utils.USER, user).add(Utils.MONITORING, monitoring).add(Utils.NOTABLE_LOCATIONS, locationsArray).build();
	}

	private static JsonObject buildLocation(List<ObjectRepresentation> location) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		for(ObjectRepresentation element : location) {
			if(element instanceof FirstCoordinateRepresentation) {
				builder.add(Utils.X, ((FirstCoordinateRepresentation) element).getCoordinate());
			}
			else if(element instanceof SecondCoordinateRepresentation) {
				builder.add(Utils.Y, ((SecondCoordinateRepresentation) element).getCoordinate());
			}
			else if(element instanceof AgentRepresentation) {
				builder.add(Utils.AGENT, dumpAgent((AgentRepresentation) element));
			}
			else if(element instanceof AvatarRepresentation) {
				builder.add(Utils.AVATAR, dumpAvatar((AvatarRepresentation) element));
			}
			else if(element instanceof DirtRepresentation) {
				builder.add(Utils.DIRT, ((DirtRepresentation) element).getColor());
			}
			else {
				continue;
			}
		}
		
		Utils.log(ConfigData.getLogsPath() + "locations.txt", true, "\n\n"); //clear log
		
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

	private static ViewRequest sendStopSignal(ViewRequestsEnum code) {
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
}