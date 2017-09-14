<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/resource/common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>添加资源</title>
</head>
<body>
<div style="padding: 3px;">
    <form id="resourceAddForm" method="post">
        <table class="grid">
            <tr>
                <td>资源名称</td>
                <td><input name="resourceName" type="text" class="easyui-textbox" style="width:160;height:29" data-options="required:true" ></td>
                <td>资源类型</td>
                <td>
                    <select name="resourceType" class="easyui-combobox" style="width:160;height:29" data-options="panelHeight:'auto'">
                        <option value="0">菜单</option>
                        <option value="1">按钮</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>资源路径</td>
                <td><input name="resourceUrl" type="text" class="easyui-textbox" style="width:160;height:29" ></td>
                <!-- <td>打开方式</td>
                <td>
                    <select name="openMode" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                        <option value="ajax" selected="selected">ajax</option>
                        <option value="iframe">iframe</option>
                    </select>
                </td> -->
                <td>资源编码</td>
                <td><input name="resourceCode" class="easyui-textbox" style="width:160;height:29"/></td>
            </tr>
            <tr>
                <td>菜单图标</td>
                <td ><input name="resourceIcon" class="easyui-textbox" style="width:160;height:29"/></td>
                <td>排序</td>
                <td><input name="sequence" value="0"  class="easyui-numberspinner" style="width: 160px; height: 29px;" required="required" data-options="min:0,max:1000,editable:false"></td>
            </tr>
            <!-- <tr>
                <td>状态</td>
                <td colspan="3">
                    <select name="status" class="easyui-combobox" data-options="width:200,height:29,editable:false,panelHeight:'auto'">
                        <option value="0">正常</option>
                        <option value="1">停用</option>
                    </select>
                </td>
            </tr> -->
            <tr>
                <td>上级资源</td>
                <td colspan="3">
                    <select id="resourceAddPid" name="parentId" style="width: 160px; height: 29px;"></select>
                    <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#resourceAddPid').combotree('clear');" >清空</a>
                </td>
            </tr>
        </table>
    </form>
</div>
<script type="text/javascript">
    $(function() {
        $('#resourceAddPid').combotree({
            url : '${ctx}/sysResource/allTree',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });

        $('#resourceAddForm').form({
            url : '${ctx}/sysResource/add',
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success : function(result) {
                progressClose();
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
                    //parent.layout_west_tree.tree('reload');
                    parent.$.modalDialog.handler.dialog('close');
                }
            }
        });
    });
</script>
</body>
</html>