package uk.ac.rhul.cs.dice.vacuumworld.view.connection;

import java.io.IOException;
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
		try {
			Utils.initConfigDataIfNecessary(request);
			Utils.forward(request, response, ConfigData.getIndexPage());
		}
		catch(Exception e) {
			Utils.fakeLog(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			Utils.initConfigDataIfNecessary(request);
			checkAlreadyConnected(request);			
			doWork(request, response);
		}
		catch(AlreadyConnectedException e) {
			Utils.fakeLog(e);
			Utils.forward(request, response, ConfigData.getMainPage());
		}
		catch(HandshakeException e) {
			Utils.fakeLog(e);
			request.setAttribute(Utils.ERROR, e.getMessage());
			Utils.forward(request, response, ConfigData.getIndexPage());
		}
		catch(Exception e) {
			Utils.log(e);
			request.setAttribute(Utils.ERROR, "Internal error: " + e.getMessage());
			Utils.forward(request, response, ConfigData.getIndexPage());
		}
	}

	private void checkAlreadyConnected(HttpServletRequest request) {		
		if(request.getSession().getAttribute(Utils.CONNECTED_FLAG) != null) {
			throw new AlreadyConnectedException();
		}
	}

	private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException, HandshakeException, ServletException {
		try {
			ConnectionWithController connection = new ConnectionWithController();
			connection.setSocketWithController(new Socket(ConfigData.getControllerIp(), ConfigData.getControllerPort()));
			ObjectOutputStream output = new ObjectOutputStream(connection.getSocketWithController().getOutputStream());
			ObjectInputStream input = new ObjectInputStream(connection.getSocketWithController().getInputStream());
					
			if(Handshake.attemptHandshake(output, input)) {
				connection.setSocketWithControllerIOStreams(output, input);
				
				request.getSession().setAttribute(Utils.CONNECTION, connection);
				request.getSession().setAttribute(Utils.CONNECTED_FLAG, true);
				Utils.forward(request, response, ConfigData.getMainPage());
			}
		}
		catch(Exception e) {
			Utils.log(e);
		}
	}
}