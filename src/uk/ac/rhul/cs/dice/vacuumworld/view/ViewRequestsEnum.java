package uk.ac.rhul.cs.dice.vacuumworld.view;

public enum ViewRequestsEnum {
	NEW, LOAD_TEMPLATE, LOAD_TEMPLATE_FROM_FILE, GET_STATE, MOVE_OBSTACLE, MOVE_AVATAR, STOP;
	
	public static ViewRequestsEnum fromString(String value) {
		switch(value) {
		case "NEW":
			return NEW;
		case "LOAD_TEMPLATE_FROM_FILE":
			return LOAD_TEMPLATE_FROM_FILE;
		case "LOAD_TEMPLATE":
			return LOAD_TEMPLATE;
		case "MOVE_OBSTACLE":
			return MOVE_OBSTACLE;
		case "GET_STATE":
			return GET_STATE;
		case "MOVE_AVATAR":
			return MOVE_AVATAR;
		case "STOP":
			return STOP;
		default:
			throw new IllegalArgumentException("Bad string: " + value);
		}
	}
}