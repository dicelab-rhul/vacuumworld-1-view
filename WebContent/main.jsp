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
<script src="load.js"></script>
<script src="load_file.js"></script>
<script src="new.js"></script>
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
						<input type="button" class="main_menu_button" id="load_button" name="load" value="LOAD TEMPLATE" />
						<input type="button" class="main_menu_button" id="load_file_button" name="load_file" value="LOAD TEMPLATE FROM FILE" />
					</form>
				</div>
			</div>
			
			<div id="select_size_dialog" class="dialog">
				<form name="grid_size" id="grid_size_form" action="" method="post">
					<input type="text" name="grid_size" id="grid_size" /><br /> <br />
					<input type="submit" value="Next" id="grid_size_ok_button">
					<input type="submit" value="Cancel" id="grid_size_cancel_button">
				</form>
			</div>
			
			<div id="user_choice_dialog" class="dialog">
				<form name="user_choice" id="user_choice" action="" method="post">
					<input type="checkbox" id="user_present" name="user_present" value="t1" checked="checked"/> Add random walking user<br />
					<input type="checkbox" id="monitoring_active" name="monitoring_active" value="t2" /> Activate monitoring<br /> <br />
					<input type="submit" value="Next" id="user_and_monitoring_ok_button">
					<input type="submit" value="Cancel" id="user_and_monitoring_cancel_button">
				</form>
			</div>
			
			<div id="add_agents_or_dirts_dialog" class="dialog">
				<div id="dialog_grid" class="centered_div"></div>
				<form name="add_agents_or_dirts" id="add_agents_or_dirts" action="" method="post">
					<div class="centered_div">
						<input type="submit" value="Next" id="add_agents_ok_button">
						<input type="submit" value="Cancel" id="add_agents_cancel_button">
					</div>
				</form>
			</div>
			
			<div id="add_agent_or_dirt_dialog" class="dialog">
				<div id="agent_or_dirt_choices" class="radio_choices">
					<form action="" method="post">
						<input type="radio" name="agent_or_dirt" value="green_north" checked="checked" /> Green agent facing North<br />
						<input type="radio" name="agent_or_dirt" value="green_south" /> Green agent facing South<br />
						<input type="radio" name="agent_or_dirt" value="green_west" /> Green agent facing West<br />
						<input type="radio" name="agent_or_dirt" value="green_east" /> Green agent facing East<br />
						<input type="radio" name="agent_or_dirt" value="orange_north" /> Orange agent facing North<br />
						<input type="radio" name="agent_or_dirt" value="orange_south" /> Orange agent facing South<br />
						<input type="radio" name="agent_or_dirt" value="orange_west" /> Orange agent facing West<br />
						<input type="radio" name="agent_or_dirt" value="orange_east" /> Orange agent facing East<br />
						<input type="radio" name="agent_or_dirt" value="white_north" /> White agent facing North<br />
						<input type="radio" name="agent_or_dirt" value="white_south" /> White agent facing South<br />
						<input type="radio" name="agent_or_dirt" value="white_west" /> White agent facing West<br />
						<input type="radio" name="agent_or_dirt" value="white_east" /> White agent facing East<br />
						<input type="radio" name="agent_or_dirt" value="green_dirt" /> Green dirt<br />
						<input type="radio" name="agent_or_dirt" value="orange_dirt" /> Orange dirt<br />
						<input type="radio" name="agent_or_dirt" value="location" /> Empty location<br />
						<br />
						<div class="centered_div">
							<input type="button" value="Add" id="add_agent_or_dirt_ok_button"/>
							<input type="button" value="Cancel" id="add_agent_or_dirt_cancel_button"/>
						</div>
					</form>
				</div>
			</div>
			
			<div id="recap_dialog" class="dialog">
				<form action="" method="post">
					<div class="centered_div">
						<p>OK to proceed, Save to store the template, Cancel to abort.</p>
						<input type="button" value="Ok" id="recap_ok_button" />
						<input type="button" value="Save Template" id="recap_save_button" />
						<input type="button" value="Cancel" id="recap_cancel_button"/>
					</div>
				</form>
			</div>
			
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