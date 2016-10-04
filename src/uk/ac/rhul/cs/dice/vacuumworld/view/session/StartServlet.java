package uk.ac.rhul.cs.dice.vacuumworld.view.session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.rhul.cs.dice.vacuumworld.view.JsonParser;
import uk.ac.rhul.cs.dice.vacuumworld.view.StateForView;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ModelMessagesEnum;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ModelUpdate;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

@WebServlet("/start")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = -8354753864303247869L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("start.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if(request.getSession().getAttribute("CONNECTED_FLAG") == null) {
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
			else {
				doWork(request, response);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			//ignore
		}
	}
	
	private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ClassNotFoundException {
		if(request.getParameterValues("INITIAL[]") != null) {
			startSystemFromUserDefinedData(request, response);
		}
		else if(request.getParameter("TEMPLATE_FILE") != null) {
			startSystemFromTemplateFile(request, response);
		}
		else {
			//ignore
		}
	}
	
	private void startSystemFromTemplateFile(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	//TODO refactor this
	private void startSystemFromUserDefinedData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ClassNotFoundException {
		String[] initialState = request.getParameterValues("INITIAL[]");
		
		if(initialState != null) {
			
			FileOutputStream fo = new FileOutputStream("/home/cloudstrife9999/workspace/VacuumWorldWeb/debug.txt", false);
			
			for(int i=0; i<initialState.length; i++) {
				if(initialState[i].contains("#")) {
					String locations = initialState[i].replaceAll("#", " ");
					fo.write(locations.getBytes());
				}
				else {
					fo.write(initialState[i].getBytes());
					
					if(i != initialState.length -1) {
						fo.write(" ".getBytes());
					}
				}
			}
			
			fo.flush();
			fo.close();
			
			request.getSession().setAttribute("TEMPLATE", true);
			
			ViewRequestsEnum requestType = ViewRequestsEnum.fromString("NEW");
			ViewRequest viewRequest = JsonParser.generateViewRequest(requestType, initialState);
			
			doFirstRequest(request, response, viewRequest);
		}
		else {
			request.setAttribute("ERROR", "null initial state");
			request.getRequestDispatcher("main.jsp").forward(request, response);
		}
	}
	
	private void doFirstRequest(HttpServletRequest request, HttpServletResponse response, ViewRequest viewRequest) throws IOException, ClassNotFoundException, ServletException {
		ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute("CONNECTION");
		connection.getOutput().writeObject(viewRequest);
		connection.getOutput().flush();
		
		Utils.freshLog(Utils.LOGS_PATH + "session.txt", "Waiting for model update");
		
		ModelUpdate update = (ModelUpdate) connection.getInput().readObject();
		
		Utils.log(Utils.LOGS_PATH + "session.txt", "Received model update");
		
		manageModelUpdate(update, request, response);
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
		ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute("CONNECTION");
		request.getSession().invalidate();
		request.getSession().setAttribute("CONNECTED_FLAG", true);
		request.getSession().setAttribute("CONNECTION", connection);
		response.setContentType("text");
		response.getWriter().println("index.jsp");
		response.getWriter().flush();
	}

	private boolean isErrorOrStop(ModelUpdate update) {
		return ModelMessagesEnum.BAD_INITIAL_STATE.equals(update.getCode()) || ModelMessagesEnum.STOP_FORWARD.equals(update.getCode());
	}

	private void manageUpdate(ModelUpdate update, HttpServletRequest request, HttpServletResponse response) {
		try {
			StateForView state = JsonParser.createStateDataForView(update);
			Utils.log(Utils.LOGS_PATH + "session.txt", "Parsed model update");
			
			request.getSession().setAttribute("GRID", state);
			
			response.setContentType("text");
			response.getWriter().println("grid.jsp");
			response.getWriter().flush();
			
			Utils.log(Utils.LOGS_PATH + "session.txt", "Sent response to new.js");
		}
		catch(Exception e) {
			Utils.log(Utils.LOGS_PATH + "session.txt", e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}
	}
}