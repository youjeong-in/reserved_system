<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>General Main</title>
<script src="js/js.js"></script>
<script> let jsonMenuInfo= JSON.parse(JSON.stringify(${MenuInfo})); 
</script>
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body onLoad = "cMain_init()">
	<div id = "searchzone">
		<input type="text" name="word" placeholder="검색어를 입력해 주세요"/>
		<input type="button" value="검색" onClick="search()" />
	</div>
		${ReservedInfo}
		${list }
	<div id = "popup"></div>
</body>
</html>