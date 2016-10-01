package uk.ac.rhul.cs.dice.vacuumworld.view.session;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.rhul.cs.dice.vacuumworld.view.JsonParser;
import uk.ac.rhul.cs.dice.vacuumworld.view.ModelUpdate;
import uk.ac.rhul.cs.dice.vacuumworld.view.StateForView;
import uk.ac.rhul.cs.dice.vacuumworld.view.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.view.ViewRequestsEnum;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;

@WebServlet("/grid")
public class GridServlet extends HttpServlet {
	private static final long serialVersionUID = -3078991556503103547L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("grid.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			sendRequest(request, response);
		}
		catch(Exception e) {
			//ignore
		}
	}

	private void sendRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
		String requestCode = request.getParameter("REQUEST_CODE");
		
		Utils.log(Utils.LOGS_PATH + "session.txt", "Received " + requestCode);
		
		if(requestCode != null) {
			manageRequestCode(request, response, requestCode);
		}
	}

	private void manageRequestCode(HttpServletRequest request, HttpServletResponse response, String requestCode) throws IOException, ClassNotFoundException {
		ViewRequest viewRequest = new ViewRequest(ViewRequestsEnum.fromString(requestCode), null);
		ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute("CONNECTION");
		Utils.log(Utils.LOGS_PATH + "session.txt", "Before sending the request");
		connection.getOutput().writeObject(viewRequest);
		connection.getOutput().flush();
		
		Utils.log(Utils.LOGS_PATH + "session.txt", "Before getting the response");
		
		waitForResponse(request, response, requestCode, connection);
	}

	private void waitForResponse(HttpServletRequest request, HttpServletResponse response, String requestCode, ConnectionWithController connection) throws IOException, ClassNotFoundException {
		if("STOP".equals(requestCode)) {
			stopSystem(request, response);
		}
		else {
			ModelUpdate update = (ModelUpdate) connection.getInput().readObject();
			
			Utils.log(Utils.LOGS_PATH + "session.txt", "After getting the response");
			
			StateForView state = JsonParser.createStateDataForView(update);
			
			Utils.log(Utils.LOGS_PATH + "session.txt", "After parsing");
			String data = "{ \"size\": " + state.getWidth() + ", \"images\": [" + getImagesList(state.getGridImagesPaths()) + "] }";
			
			Utils.log(Utils.LOGS_PATH + "session.txt", "Got " + data);
			
			response.setContentType("application/json");
			response.getWriter().print(data);
			response.getWriter().flush();
			
			Utils.log(Utils.LOGS_PATH + "session.txt", "Data sent to javascript");
		}
	}

	private void stopSystem(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		
		response.setContentType("text");
		response.getWriter().print("stopped");
		response.getWriter().flush();
		
		Utils.log(Utils.LOGS_PATH + "session.txt", "Data sent to javascript");
	}

	private String getImagesList(String[] gridImagesPaths) {
		StringBuilder builder = new StringBuilder();
		
		for(String path : gridImagesPaths) {
			builder.append("{ \"image\": \"" + path + "\"},");
		}
		
		return builder.toString().substring(0, builder.length() - 1); //to cut the last comma.
	}
}