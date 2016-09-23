<%@page import="uk.ac.rhul.cs.dice.vacuumworld.view.StateForView"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<link rel="stylesheet" type="text/css" href="style.css">
<title>VacuumWorld</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/jquery-ui.min.js"></script>
<script src="grid.js"></script>
</head>
<body>
	<%
		if(session.getAttribute("CONNECTED_FLAG") == null) {
			session = null;
			%>
				<jsp:forward page="index.jsp" />
			<%
		}
		else if(session.getAttribute("TEMPLATE") == null) {
			%>
				<jsp:forward page="main.jsp" />
			<%
		}
		else if(session.getAttribute("GRID") != null) {
			String error = (String) request.getAttribute("ERROR");
			
			if(error != null) {
				%>
					<div class="centered_div">ERROR: <%=error%></div>
				<%
				session.removeAttribute("ERROR");
			}

			StateForView state = (StateForView) session.getAttribute("GRID");
			int size = state.getWidth();
			String[] imagesPaths = state.getGridImagesPaths();
			
			%>
			<script>populateGridAndRefresh(<%=size%>, <%=imagesPaths%>);</script>
			<%
			
			
			%>
				<div class="centered_div" id="grid"></div>
				<div id="grid_options">
					<form id="main_menu_options_form">
						<input type="button" class="main_menu_button" id="stop_button" name="stop" value="STOP" />
					</form>
				</div>
			<%
		}
		else if(session.getAttribute("GRID") == null) {
			//do a post request to /grid to get the current state
		}
	%>

</body>
</html>