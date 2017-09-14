<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/resource/common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>登录页</title>
</head>
<style type="text/css">
	.item{margin-top: 30px;text-align: center}
	.login-bg{background:url(${ctx}/resource/static/style/images/login-bg2.jpg) no-repeat; background-size: 100% 100%;}
	.panel{position:relative;top:30%;left:42%;}
</style>
<body class="login-bg">
	<div id="dlg" class="easyui-panel" title="登录面板" data-options="region:'center',border:false" style="width:400px;height: 250px">
		<form id="loginform" method="post">
			<div class="item">
				<label>登录名</label>
				<input type="text" name="loginName" class="easyui-textbox">
			</div>
			<div class="item">
				<label>密&nbsp;&nbsp;&nbsp;码</label>
				<input type="password" name="loginPwd" class="easyui-textbox">
			</div>
			<div class="item">
				<div id="dlg-linkbtn">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="login()">登录</a>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()">重置</a>
				</div>
			</div>
			<div style="margin-top:18px;text-align: center"><span id="msg" style="color:red;"></span></div>
		</form>
	</div>
	<script type="text/javascript">
		function login(){
			$("#loginform").form("submit",{
				url:"${ctx}/login",
				onSubmit:function(){

				},
				success:function(result){
					result=eval("("+result+")");
					if(result.success){
						window.location.href="${ctx}/";
					}else{
						//$.messager.show({title:"消息",msg:result.msg});
						$("#msg").text(result.msg);
					}
				}
			});
		}
		function reset(){
			document.getElementById("loginform").reset();
		}
	</script>
</body>
</html>