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
			//do nothing
		}
		finally {
			response.setContentType("text");
			response.getWriter().print("grid.jsp");
			response.getWriter().flush();
		}
	}

	private void sendRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
		String requestCode = request.getParameter("REQUEST_CODE");
		
		if(requestCode != null) {
			ViewRequest viewRequest = new ViewRequest(ViewRequestsEnum.fromString(requestCode), null);
			ConnectionWithController connection = (ConnectionWithController) request.getSession().getAttribute("CONNECTION");
			connection.getOutput().writeObject(viewRequest);
			ModelUpdate update = (ModelUpdate) connection.getInput().readObject();
			
			StateForView state = JsonParser.createStateDataForView(update);
			String data = "{ \"size\": " + state.getWidth() + ", \"images\": [" + getImagesList(state.getGridImagesPaths()) + "] }";

			response.setContentType("application/json");
			response.getWriter().print(data);
		}
	}

	private String getImagesList(String[] gridImagesPaths) {
		String toReturn = "";
		
		for(String path : gridImagesPaths) {
			toReturn += "{ \"image\": " + path + "}";
		}
		
		return toReturn;
	}
}