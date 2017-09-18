<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache"> 
<meta http-equiv="Expires" content="0"> 
<title>登录页</title>
<link rel="stylesheet" type="text/css" href="/mtplatform/resource/system/login.css"/>
<script type="text/javascript" src="/mtplatform/resource/jeasyui/jquery.min.js"></script>
<script type="text/javascript" src="/mtplatform/resource/jeasyui/jquery.easyui.min.js"></script>
</head>
<body>
<div class="login" onkeydown="keylogin()">
    <div class="message">魔盾科技管理系统</div>
    <div id="darkbannerwrap"></div>
	<form name="loginform" id="loginform" method="post">
		<input name="loginName" placeholder="请输入用户名" required type="text">
		<hr class="hr15">
		<input name="loginPwd" placeholder="请输入密码" required type="password">
		<hr class="hr15">
		<input name="btn" value="登录" style="width:100%;" type="button" onclick="login()">
		<hr class="hr20">
		<div style="margin-top:18px;text-align: center"><span id="msg" style="color:red;"></span></div>
	</form>
</div>
<script type="text/javascript">
	//点击登录
	function login(){
		$("#loginform").form("submit",{
			url:"${pageContext.request.contextPath}/login",
			success:function(data){
				result=eval("("+data+")");
				if(result.success){
					window.location.href="${pageContext.request.contextPath}/";
				}else{
					$("#msg").text(result.msg);
				}
			}
		});
	}
	//回车登录
	function keylogin(){
		if (event.keyCode == 13){
		    event.returnValue=false;
			event.cancel = true;
			loginform.btn.click();
		}
	}
</script>
</body>
</html>