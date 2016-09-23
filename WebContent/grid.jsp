<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<link rel="stylesheet" type="text/css" href="style.css">
<title>VacuumWorld</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
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
		
		%>
		<p style="text-align: center;">TODO: show the grid</p>
		<%
		//show the grid
		//do ajax requests to refresh the grid
	}
	else if(session.getAttribute("GRID") == null) {
		//do a post request to /grid to get the current state
	}
	else {
		%>
			<div class="vwname centered_div">VacuumWorld</div>
			<div id="grid" class="centered_div">
				<%
					Object template = session.getAttribute("TEMPLATE");
		
					if(template instanceof String) {
					
					%>
						<h1>Selected template <%=(String) template %></h1>
					<%
					}
					else {
						session = null;
						%>
							<jsp:forward page="index.jsp" />
						<%
					}
				%>
			</div>
		<%
	}
		%>

</body>
</html>