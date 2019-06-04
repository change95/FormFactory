<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>橙大郎QQ757720404</title>
</head>
<body>
<center>

<h1>各种一起提交,从二进制输入流进行分割</h1>
</center>


<div style="width: 300px; height: 600px; margin: 20px auto;">
<!-- 

	form  表单设置二进制流

 -->
<form action="/ua/t" method="post" enctype="multipart/form-data">
	用户名:<input type="text" name="username" realplace="用户名"><br>
	密码:<input type="password" name="userpassword" realplace="密码"><br>
	<br>
	性别:
	<input type="radio" name="sex" value="02"> 男
	<input type="radio" name="sex" value="01"> 女
	<br>
	喜爱: <input type="checkbox" name="pbq" value="乒乓球">乒乓球
		  <input type="checkbox" name="yx" value="游戏">游戏
		  <input type="checkbox" name="zq" value="足球">足球
	<br><br>
	<br>地点:
	<select name="place">
		<option value="beijing">北京</option>
		<option value="hhht">呼和浩特</option>
		<option value="erlian">二连</option>
	</select>
	<br><br>
	文件上传:<input name="wenjian" type="file">
	<br><br>
	<textarea rows="9" cols="30" name="dataarea">
	</textarea>
	<input type="submit" value="提交按钮" >
</form>
</div>
</body>
</html>