<%@page import="uk.ac.rhul.cs.dice.vacuumworld.view.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<link rel="stylesheet" type="text/css" href="style.css">
<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">
<title>VacuumWorld</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/jquery-ui.min.js"></script>
<script src="main.js"></script>
<!--
<script src="load.js"></script>
<script src="load_file.js"></script>
-->
<script src="new.js"></script>
<script src="error.js"></script>
</head>
<body>

<%
	if(session.getAttribute(Utils.TEMPLATE) != null) {
		%>
			<jsp:forward page="grid.jsp" />
		<%
	}
	else if(session.getAttribute(Utils.CONNECTED_FLAG) != null) {
		String error = (String) request.getAttribute(Utils.ERROR);
		
		if(error != null) {
			%>
				<div class="centered_div">ERROR: <%=error%></div>
			<%
			session.removeAttribute(Utils.ERROR);
		}
		
		%>
			<div id="main_menu" class="centered_div">
				<div id="unlocked">
					<img src="images/unlocked.png" />
				</div>
				<div id="main_menu_options">
					<form id="main_menu_options_form">
						<input type="button" class="main_menu_button" id="new_simulation_button" name="new" value="NEW" />
						<!--
						<input type="button" class="main_menu_button" id="load_button" name="load" value="LOAD TEMPLATE" />
						<input type="button" class="main_menu_button" id="load_file_button" name="load_file" value="LOAD TEMPLATE FROM FILE" />
						-->
					</form>
				</div>
			</div>
			<!--
			<div id="load_template" class="dialog">	
				<div id="templates" class="radio_choices">
					<form action="start" method="post">
						<input type="radio" name="template" value="t1" id="t1" checked="checked"/> Grid: 3x3 - green agents: 1 - orange agents: 0 - green dirt: 1 - orange dirt: 0<br />
						<input type="radio" name="template" value="t2" /> Grid: 3x3 - green agents: 0 - orange agents: 1 - green dirt: 0 - orange dirt: 1<br />
						<input type="radio" name="template" value="t3" /> Grid: 5x5 - green agents: 1 - orange agents: 0 - green dirt: 1 - orange dirt: 0<br />
						<input type="radio" name="template" value="t4" /> Grid: 5x5 - green agents: 0 - orange agents: 1 - green dirt: 0 - orange dirt: 1<br />
						<input type="radio" name="template" value="t5" /> Grid: 10x10 - green agents: 1 - orange agents: 1 - green dirt: 1 - orange dirt: 1<br />
						<br />
						<div class="centered_div">
							<input type="button" value="Load" id="load_template_ok_button"/>
							<input type="button" value="Cancel" id="cancel_template_upload" />
						</div>
					</form>
				</div>
			</div>
			
			<div id="load_file" class="dialog">
				<form id="load_file_form" action="" method="post">
					<input type="file" name="template_file" id="template_file" /><br /><br />
					<input type="button" value="Load" id="load_file_ok_button">
					<input type="button" value="Cancel" id="cancel_file_upload" />
				</form>
			</div>
			-->
		<%
	}
	else {
		session.invalidate();
		
		%>
			<jsp:forward page="index.jsp" />
		<%
	}
%>
</body>
</html>