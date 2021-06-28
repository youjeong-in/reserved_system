<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Access Page</title>
<script src="js/js.js"></script>
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body onLoad = "callMessage('${message}')">
	<div id="accesszone">
		<div class="title">Access Page</div>
		<div class="inputzone">
			<div id="general" class="choiceOn" onClick="callType(this)">General</div>
			<div id="restaurant" class="choiceOff" onClick="callType(this)">Restaurant</div>
			<input type="hidden" name="accessType" value="G" />
		</div>
		<div class="inputzone">
			<div class="item">User Code</div>
			<div class="content"><input type="text" name="uCode" class="box" placeholder="Your Id" onKeyUp="korCheck(this, event)"/></div>
		</div>
		<div class="inputzone">
			<div class="item">Access Code</div>
			<div class="content"><input type="password" name="aCode" class="box" placeholder="Your Password" /></div>
		</div>
		<div class="title" onClick="sendAccessInfo()">Submit</div>
	</div>
	<div style="text-align:center">아이디 분실 | 비밀번호 분실 | 
		<a href="http://192.168.0.19/JoinForm">회원가입</a>
	</div>
</body>
</html>