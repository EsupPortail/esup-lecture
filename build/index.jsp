<html>
<head>
</head>
<body>
<%
response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
response.setHeader("Location", "stylesheets/welcome.faces");
%>
</body>
</html>
