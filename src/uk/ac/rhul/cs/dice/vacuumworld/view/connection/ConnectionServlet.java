package uk.ac.rhul.cs.dice.vacuumworld.view.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.rhul.cs.dice.vacuumworld.view.session.ConnectionWithController;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.ConfigData;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.HandshakeException;

@WebServlet("/connect")
public class ConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 4242149065634215947L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(ConfigData.getIndexPage()).forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			checkAlreadyConnected(request);
			
			if(!initConfigData(request)) {
				throw new IllegalArgumentException("Could not parse configuration file.");
			}
			
			System.out.println(ConfigData.getControllerIp());
			System.out.println(ConfigData.getControllerPort());
			
			doWork(request, response);
		}
		catch(AlreadyConnectedException e) {
			Utils.fakeLog(e);
			
			request.getRequestDispatcher(ConfigData.getMainPage()).forward(request, response);
		}
		catch(HandshakeException e) {
			Utils.log(e);
			
			request.setAttribute(Utils.ERROR, e.getMessage());
			request.getRequestDispatcher(ConfigData.getIndexPage()).forward(request, response);
		}
		catch(Exception e) {
			Utils.log(e);
			
			request.setAttribute(Utils.ERROR, "Internal error: " + e.getMessage());
			request.getRequestDispatcher(ConfigData.getIndexPage()).forward(request, response);
		}
	}

	private boolean initConfigData(HttpServletRequest request) {
		InputStream input = request.getServletContext().getResourceAsStream("/view.json");
		
		return ConfigData.initConfigData(input);
	}

	private void checkAlreadyConnected(HttpServletRequest request) {		
		if(request.getSession().getAttribute(Utils.CONNECTED_FLAG) != null) {
			throw new AlreadyConnectedException();
		}
	}

	private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException, HandshakeException, ServletException {
		Socket socketWithController = new Socket(ConfigData.getControllerIp(), ConfigData.getControllerPort());
		ObjectOutputStream output = new ObjectOutputStream(socketWithController.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(socketWithController.getInputStream());
				
		if(Handshake.attemptHandshake(output, input)) {
			ConnectionWithController connection = new ConnectionWithController();
			connection.setSocketWithController(socketWithController, output, input);
			
			request.getSession().setAttribute(Utils.CONNECTION, connection);
			request.getSession().setAttribute(Utils.CONNECTED_FLAG, true);
			request.getRequestDispatcher(ConfigData.getMainPage()).forward(request, response);
		}
	}
}