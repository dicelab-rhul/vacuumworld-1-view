package uk.ac.rhul.cs.dice.vacuumworld.view.session;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.rhul.cs.dice.vacuumworld.view.JsonParser;
import uk.ac.rhul.cs.dice.vacuumworld.view.StateForView;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.ConfigData;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ModelMessagesEnum;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ModelUpdate;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

@WebServlet("/grid")
public class GridServlet extends HttpServlet {
	private static final long serialVersionUID = -3078991556503103547L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			Utils.initConfigDataIfNecessary(request);
			Utils.forward(request, response, ConfigData.getGridPage());
		}
		catch(Exception e) {
			Utils.fakeLog(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			Utils.initConfigDataIfNecessary(request);
			sendRequest(request, response);
		}
		catch(Exception e) {
			Utils.log(e);
		}
	}

	private void sendRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
		String requestCode = request.getParameter(Utils.REQUEST_CODE);
		
		if(requestCode != null) {
			manageRequestCode(request, response, requestCode);
		}
	}

	private void manageRequestCode(HttpServletRequest request, HttpServletResponse response, String requestCode) throws IOException, ClassNotFoundException {
		ViewRequestsEnum requestCodeEnum = ViewRequestsEnum.fromString(requestCode);
		ViewRequest viewRequest = new ViewRequest(requestCodeEnum, null);
		ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute(Utils.CONNECTION);
		connection.getOutput().writeObject(viewRequest);
		connection.getOutput().flush();
		
		waitForResponse(request, response, requestCodeEnum, connection);
	}

	private void waitForResponse(HttpServletRequest request, HttpServletResponse response, ViewRequestsEnum requestCode, ConnectionWithController connection) throws IOException, ClassNotFoundException {
		if(ViewRequestsEnum.STOP_FORWARD.equals(requestCode)) {
			stopSystem(request, response);
		}
		else {
			ModelUpdate update = (ModelUpdate) connection.getInput().readObject();
			manageModelUpdate(update, request, response);
		}
	}

	private void manageModelUpdate(ModelUpdate update, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(isStopMessage(update)) {
			manageStop(update, request, response);
		}
		else {
			manageUpdate(update, response);
		}
	}
	
	private void manageStop(ModelUpdate update, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(ModelMessagesEnum.STOP_FORWARD.equals(update.getCode())) {
			stopSystem(request, response);
		}
	}

	private boolean isStopMessage(ModelUpdate update) {
		return ModelMessagesEnum.STOP_FORWARD.equals(update.getCode());
	}

	private void manageUpdate(ModelUpdate update, HttpServletResponse response) throws IOException {
		StateForView state = JsonParser.createStateDataForView(update);
		
		String data = "{ \"size\": " + state.getWidth() + ", \"images\": [" + getImagesList(state.getGridImagesPaths()) + "] }";
		
		response.setContentType("application/json");
		response.getWriter().print(data);
		response.getWriter().flush();
	}

	private void stopSystem(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		
		response.setContentType("text");
		response.getWriter().print("stopped");
		response.getWriter().flush();
	}

	private String getImagesList(String[] gridImagesPaths) {
		StringBuilder builder = new StringBuilder();
		
		for(String path : gridImagesPaths) {
			builder.append("{ \"image\": \"" + path + "\"},");
		}
		
		return builder.toString().substring(0, builder.length() - 1); //to cut the last comma.
	}
}