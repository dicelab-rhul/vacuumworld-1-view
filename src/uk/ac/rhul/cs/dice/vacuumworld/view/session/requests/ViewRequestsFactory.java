package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import java.util.EnumMap;
import java.util.Map;

import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public class ViewRequestsFactory {
	private static Map<ViewRequestsEnum, Class<? extends ViewRequestInterface>> requestsMap = initRequestsMap();
	
	private ViewRequestsFactory(){}
	
	private static Map<ViewRequestsEnum, Class<? extends ViewRequestInterface>> initRequestsMap() {
		Map<ViewRequestsEnum, Class<? extends ViewRequestInterface>> map = new EnumMap<>(ViewRequestsEnum.class);
		
		map.put(ViewRequestsEnum.NEW, NewRequest.class);
		map.put(ViewRequestsEnum.LOAD_TEMPLATE, LoadTemplateRequest.class);
		map.put(ViewRequestsEnum.LOAD_TEMPLATE_FROM_FILE, LoadTemplateFromFileRequest.class);
		map.put(ViewRequestsEnum.GET_STATE, GetStateRequest.class);
		map.put(ViewRequestsEnum.MOVE_OBSTACLE, MoveObstacleRequest.class);
		map.put(ViewRequestsEnum.MOVE_AVATAR, MoveAvatarRequest.class);
		map.put(ViewRequestsEnum.STOP_CONTROLLER, StopControllerRequest.class);
		map.put(ViewRequestsEnum.STOP_FORWARD, StopForwardRequest.class);
		
		return map;
	}

	public static ViewRequestInterface generateViewRequest(ViewRequestsEnum code, Object data) {
		try {
			return ViewRequestsFactory.requestsMap.get(code).getConstructor(ViewRequestsEnum.class, Object.class).newInstance(code, data);
		}
		catch(Exception e) {
			Utils.log(e);
			
			return new StopForwardRequest(code, data);
		}
	}
}