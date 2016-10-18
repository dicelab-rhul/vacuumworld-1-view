package uk.ac.rhul.cs.dice.vacuumworld.view.session;

import java.io.IOException;

import javax.servlet.ServletException;
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

@WebServlet("/start")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = -8354753864303247869L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			Utils.initConfigDataIfNecessary(request);
			Utils.forward(request, response, ConfigData.getMainPage());
		}
		catch(Exception e) {
			Utils.fakeLog(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			Utils.initConfigDataIfNecessary(request);
			
			if(request.getSession().getAttribute(Utils.CONNECTED_FLAG) == null) {
				Utils.forward(request, response, ConfigData.getIndexPage());
			}
			else {
				doWork(request, response);
			}
		}
		catch(Exception e) {
			Utils.log(e);
		}
	}
	
	private void doWork(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(request.getParameterValues(Utils.INITIAL_STATE_ARRAY) != null) {
				startSystemFromUserDefinedData(request, response);
			}
			else {
				//ignore
			}
		}
		catch(Exception e) {
			Utils.log(e);
			
			request.setAttribute(Utils.ERROR, e.getMessage());
		}
	}

	private void startSystemFromUserDefinedData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ClassNotFoundException {
		String[] initialState = request.getParameterValues(Utils.INITIAL_STATE_ARRAY);
		
		if(initialState != null) {			
			request.getSession().setAttribute(Utils.TEMPLATE, true);
			
			ViewRequestsEnum requestType = ViewRequestsEnum.fromString(Utils.NEW);
			ViewRequest viewRequest = JsonParser.generateViewRequestForController(requestType, initialState);
			
			doFirstRequest(request, response, viewRequest);
		}
		else {
			request.setAttribute(Utils.ERROR, "null initial state");
			Utils.forward(request, response, ConfigData.getMainPage());
		}
	}
	
	private void doFirstRequest(HttpServletRequest request, HttpServletResponse response, ViewRequest viewRequest) {
		try {
			ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute(Utils.CONNECTION);
			connection.getOutput().writeObject(viewRequest);
			connection.getOutput().flush();
			
			ModelUpdate update = (ModelUpdate) connection.getInput().readObject();
			
			manageModelUpdate(update, request, response);
		}
		catch(Exception e) {
			Utils.log(e);
			
			request.setAttribute(Utils.ERROR, e.getMessage());
		}
	}

	private void manageModelUpdate(ModelUpdate update, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if(isErrorOrStop(update)) {
			manageErrorOrStop(update, request, response);
		}
		else {
			manageUpdate(update, request, response);
		}
	}

	private void manageErrorOrStop(ModelUpdate update, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		switch(update.getCode()) {
		case BAD_INITIAL_STATE:
		case STOP_FORWARD:
			manageErrorOrStop(request, response);
			break;
		default:
			return;
		}
	}

	private void manageErrorOrStop(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute(Utils.CONNECTION);
		request.getSession().invalidate();
		request.getSession().setAttribute(Utils.CONNECTED_FLAG, true);
		request.getSession().setAttribute(Utils.CONNECTION, connection);
		response.setContentType("text");
		response.getWriter().println(ConfigData.getIndexPage());
		response.getWriter().flush();
	}

	private boolean isErrorOrStop(ModelUpdate update) {
		return ModelMessagesEnum.BAD_INITIAL_STATE.equals(update.getCode()) || ModelMessagesEnum.STOP_FORWARD.equals(update.getCode());
	}

	private void manageUpdate(ModelUpdate update, HttpServletRequest request, HttpServletResponse response) {
		try {
			StateForView state = JsonParser.createStateDataForView(update);
			
			request.getSession().setAttribute(Utils.GRID, state);
			
			response.setContentType("text");
			response.getWriter().println(ConfigData.getGridPage());
			response.getWriter().flush();
		}
		catch(Exception e) {
			Utils.log(e);
		}
	}
}