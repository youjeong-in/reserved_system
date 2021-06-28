<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Restaurant Main</title>
<script src="js/js.js"></script>
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body onLoad="rMain_init('${message}')">
	${WaitingList }
	${TodayReservedList }
</body>
</html>