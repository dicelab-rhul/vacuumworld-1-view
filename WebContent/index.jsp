<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<link rel="stylesheet" type="text/css" href="style.css">
<title>VacuumWorld</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="index.js"></script>

</head>
<body>
<!-- 
<div class="vwname centered_div">VacuumWorld</div>
-->
<%
	if(session.getAttribute("TEMPLATE") != null) {
		%>
			<jsp:forward page="grid.jsp" />
		<%
	}
	else if(session.getAttribute("CONNECTED_FLAG") != null) {
		%>
			<jsp:forward page="main.jsp" />
		<%
	}
	else {
		String error = (String) request.getAttribute("ERROR");
		
		if(error != null) {
			%>
				<div class="centered_div">ERROR: <%=error%></div>
			<%
			session.removeAttribute("ERROR");
		}
		
		%>
			<div class="centered_div" id="connection_response"></div>
			<div class="centered_div" id="connection_form">
				<br />
				<form action="connect" method="post">
					<input id="connection_image" type="image" src="images/locked.png">
				</form>
			</div>
		<%
	}
%>
</body>
</html>