<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reserve step1</title>
<script src="js/js.js"></script>
<script> let menuCheck;
</script>
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div id="reservezone">
 ${dayList }

 <div id ="menuList"></div></div>
 <div id ="orderList"></div>
<input type = "button" value = "예약하기" onClick = "callData()"/>
</body>
</html>