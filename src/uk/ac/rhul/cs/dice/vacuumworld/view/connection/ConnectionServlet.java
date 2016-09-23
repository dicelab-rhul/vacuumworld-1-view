package uk.ac.rhul.cs.dice.vacuumworld.view.connection;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.rhul.cs.dice.vacuumworld.view.utils.HandshakeException;
import uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils;

@WebServlet("/connect")
public class ConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 4242149065634215947L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			checkAlreadyConnected(request);
			doWork(request, response);
		}
		catch(AlreadyConnectedException e) {
			request.getRequestDispatcher("main.jsp").forward(request, response);
		}
		catch(HandshakeException e) {
			request.setAttribute("ERROR", e.getMessage());
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		catch(Exception e) {
			request.setAttribute("ERROR", "Internal error: " + e.getMessage());
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	private void checkAlreadyConnected(HttpServletRequest request) {		
		if(request.getSession().getAttribute("CONNECTED_FLAG") != null) {
			throw new AlreadyConnectedException();
		}
	}

	private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException, HandshakeException, ServletException {
		Socket socketWithController = Handshake.attemptHanshake(Utils.CONTROLLER_IP, Utils.CONTROLLER_PORT);
		
		if(socketWithController != null) {
			request.getSession().setAttribute("SOCKET_WITH_CONTROLLER", socketWithController);
			request.getSession().setAttribute("CONNECTED_FLAG", true);
			request.getRequestDispatcher("main.jsp").forward(request, response);
		}
		else {
			throw new HandshakeException("Null socket.");
		}
	}
}