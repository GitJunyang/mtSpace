package com.moutum.mtplatform.activiti.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moutum.mtplatform.activiti.model.TUserTask;
import com.moutum.mtplatform.activiti.service.ActivitiService;
import com.moutum.mtplatform.activiti.service.TUserTaskService;
import com.moutum.mtplatform.activiti.vo.CommentVo;
import com.moutum.mtplatform.activiti.vo.ProcessDefinitionVo;
import com.moutum.mtplatform.activiti.vo.TaskVo;
import com.moutum.mtplatform.application.model.TVacation;
import com.moutum.mtplatform.application.service.TVacationService;
import com.moutum.mtplatform.common.base.BaseController;
import com.moutum.mtplatform.common.operlog.SysLog;
import com.moutum.mtplatform.common.shiro.ShiroUser;
import com.moutum.mtplatform.common.utils.ConstantUtils;
import com.moutum.mtplatform.common.utils.DateUtils;
import com.moutum.mtplatform.common.utils.PageInfo;
import com.moutum.mtplatform.system.model.SysUser;
import com.moutum.mtplatform.system.service.SysUserService;

@Controller
@RequestMapping("/activiti")
public class ActivitiController extends BaseController{
	Logger logger = Logger.getLogger(ActivitiController.class);
	
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private IdentityService identityService;
	@Autowired 
	private TVacationService tVacationService;
	@Autowired
    private SysUserService sysUserService;
	@Autowired
    private TUserTaskService tUserTaskService;
	@Autowired
	private HistoryService historyService;
	
	
	/**
     * 部署流程定义页
     * @return
     */
    @GetMapping("/deployPage")
    public String deployPage() {
        return "activiti/processdefDeploy";
    }
	
	/**
     * 流程部署(压缩包方式)
     * @param 
     * @return
     */
    @SysLog(value="流程部署")
    @PostMapping("/deploy")
    @ResponseBody
    public Object deployZipResource(@RequestParam(value = "file", required = false)MultipartFile deployFile) {
		try {
			repositoryService.createDeployment().name("请假流程")
					.addZipInputStream(new ZipInputStream(deployFile.getInputStream())).deploy();
			return renderSuccess("部署成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("部署失败！");
		}
    }
    
    
    
    /**
     * 流程部署管理页
     * @return
     */
    @GetMapping("/processdefManager")
    public String processdefManager() {
        return "activiti/processdef";
    }
    
    /**
     * 查询流程定义
     * @param processDefinitionVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @SysLog(value="查询流程定义")
    @PostMapping("/processdefDataGrid")
    @ResponseBody
    public PageInfo dataGrid(ProcessDefinitionVo processDefinitionVo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows);
    	activitiService.selectProcessDefinitionDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 我的任务管理页
     * @return
     */
    @GetMapping("/taskManager")
    public String taskManager() {
        return "activiti/task";
    }
    
    /**
     * 查询我的任务
     * @param taskVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @SysLog(value="查询我的任务")
    @PostMapping("/taskDataGrid")
    @ResponseBody
    public PageInfo taskDataGrid(TaskVo taskVo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows);
    	activitiService.selectTaskDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 我的已办任务管理页
     * @return
     */
    @GetMapping("/hisTaskManager")
    public String hisTaskManager() {
        return "activiti/hisTask";
    }
    
    /**
     * 查询我的已办任务(历史任务)
     * @param taskVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/hisTaskDataGrid")
    @ResponseBody
    public PageInfo hisTaskDataGrid(TaskVo taskVo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows);
    	activitiService.selectHisTaskDataGrid(pageInfo);
        return pageInfo;
    }
    
    
    /**
     * 办理页面(请假业务)
     * @return
     */
    @GetMapping("/complateTaskPage")
    public String complateTaskPage(Model model,String id) {
    	Task task = taskService.createTaskQuery().taskId(id).singleResult();
    	String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		EntityWrapper<TVacation> wrapper =new EntityWrapper<TVacation>();
		wrapper.where("proc_inst_id = {0}", pi.getId());
		TVacation vacation= tVacationService.selectOne(wrapper);
		List<CommentVo> comments = new ArrayList<CommentVo>();
		List<Comment> commentList= taskService.getProcessInstanceComments(processInstanceId);
		for(Comment comment : commentList){
			CommentVo vo = new CommentVo();
			SysUser user= sysUserService.selectById(comment.getUserId());
			vo.setCommentUser(user.getUserName());
			vo.setCommentTime(DateUtils.formatDateToString(comment.getTime()));
			vo.setCommentContent(comment.getFullMessage());
			comments.add(vo);
		}
		
		model.addAttribute("vacation", vacation);
		model.addAttribute("task", task);
		model.addAttribute("comments", comments);
        return "activiti/taskComplate";
    }
    
    /**
     * 办理任务(完成任务)
     * @param vacationId
     * @param taskId
     * @param commentContent
     * @param commentResult
     * @return
     */
    @SysLog(value="办理任务")
    @RequestMapping("/complateTask")
    @ResponseBody
    public Object complateTask( @RequestParam("vacationId") String vacationId,
					    		@RequestParam("taskId") String taskId,
					    		@RequestParam("commentContent") String commentContent,
					    		@RequestParam("commentResult") Integer commentResult){
    	
    	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    	String processInstanceId = task.getProcessInstanceId();
    	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    	//添加意见
    	ShiroUser shiroUser= (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	identityService.setAuthenticatedUserId(shiroUser.getId());
    	taskService.addComment(task.getId(), processInstance.getId(), commentContent);
    	//完成任务
    	Map<String, Object> variables = new HashMap<String, Object>();
    	TVacation vacation = tVacationService.selectById(vacationId);
    	if(ConstantUtils.vacationStatus.PASSED.getValue()==commentResult){
    		variables.put("isPass", true);
    	}else if(ConstantUtils.vacationStatus.NOT_PASSED.getValue()==commentResult){
    		variables.put("isPass", false);
    	}
    	
    	// 完成委派任务
    	if(DelegationState.PENDING == task.getDelegationState()){
    		this.taskService.resolveTask(taskId, variables);
    		return renderSuccess("办理委派任务成功！");
    	}
    	//完成正常办理任务
    	taskService.complete(task.getId(), variables);
    	
    	//更新请假审批状态
    	ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(vacation.getProcInstId()).singleResult();
    	if(pi==null){
    		//hisVar有值代表是会签任务，没值代表是其他任务
    		HistoricVariableInstance  hisVar= historyService.createHistoricVariableInstanceQuery()
    				.processInstanceId(task.getProcessInstanceId()).variableName("passCount")
    				.singleResult();
    		if(hisVar!=null){
    			return renderSuccess("办理成功！");
    		}else{
    			if(ConstantUtils.vacationStatus.PASSED.getValue()==commentResult){
            		vacation.setVacationStatus(ConstantUtils.vacationStatus.PASSED.getValue());
            	}else if(ConstantUtils.vacationStatus.NOT_PASSED.getValue()==commentResult){
            		vacation.setVacationStatus(ConstantUtils.vacationStatus.NOT_PASSED.getValue());
            	}
    			//更新请假业务
            	tVacationService.updateById(vacation);
    		}
    	}
    	return renderSuccess("办理成功！");
    }
    
    /**
     * 签收任务
     * @param id
     * @return
     */
    @SysLog(value="签收任务")
    @RequestMapping("/claimTask")
    @ResponseBody
    public Object claimTask(String id){
		try {
			ShiroUser user = getShiroUser();
			activitiService.claimTask(user.getId(), id);
			return renderSuccess("签收成功！");
		}catch (ActivitiObjectNotFoundException e){
			return renderError("此任务不存在！任务签收失败！");
		}catch (ActivitiTaskAlreadyClaimedException e) {
			return renderError("此任务已被其他组成员签收！请刷新页面重新查看！");
		}catch (Exception e) {
			return renderError("任务签收失败！请联系管理员！");
		} 
    }
    
    /**
     * 委派页面(与转办共用一个页面) 
     */
    @GetMapping("/taskDelegate")
    public String taskAssignee() {
        return "activiti/taskDelegate";
    }
    
    /**
     * 委派任务
     * @param taskId
     * @param userId
     * @return
     */
    @SysLog(value="委派任务")
    @RequestMapping("/delegateTask")
    @ResponseBody
    public Object delegateTask(String taskId , String userId){
    	try {
			activitiService.delegateTask(userId, taskId);
			return renderSuccess("委派任务成功！");
		} catch (ActivitiObjectNotFoundException e){
			return renderError("此任务不存在！委派任务失败！");
		} catch (Exception e) {
			return renderError("委派任务失败，系统错误！");
		}
    }
    
    /**
     * 转办任务
     * @param taskId
     * @param userId
     * @return
     */
    @SysLog(value="转办任务")
    @RequestMapping("/transferTask")
    @ResponseBody
    public Object transferTask(String taskId , String userId){
    	try {
			activitiService.transferTask(userId, taskId);
			return renderSuccess("转办任务成功！");
		} catch (ActivitiObjectNotFoundException e){
			return renderError("此任务不存在！转办任务失败！");
		} catch (Exception e) {
			return renderError("委派任务失败，系统错误！");
		}
    }
    
    
    /**
     * 任务跳转页面 
     */
    @GetMapping("/taskJump")
    public String taskJump(Model model,@RequestParam("id") String taskId) {
    	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    	//查询流程定义
    	ProcessDefinition pd= repositoryService.createProcessDefinitionQuery()
    	.processDefinitionId(task.getProcessDefinitionId()).singleResult();
    	//根据流程定义KEY查询用户任务
    	EntityWrapper<TUserTask> wrapper =new EntityWrapper<TUserTask>();
		wrapper.where("proc_def_key = {0}", pd.getKey());
		List<TUserTask> tasks= tUserTaskService.selectList(wrapper);
    	model.addAttribute("tasks",tasks);
        return "activiti/taskJump";
    }
    
    /**
     * 任务跳转
     * @param taskId
     * @param taskDefinitionKey
     * @return
     */
    @SysLog(value="任务跳转")
    @RequestMapping("/jumpTask")
    @ResponseBody
    public Object jumpTask(String taskId , String taskDefinitionKey){
    	try {
			activitiService.jumpTask(taskId, taskDefinitionKey);
			return renderSuccess("任务跳转成功！");
		} catch (Exception e) {
			return renderError("任务跳转失败！");
		}
    }
    
    /**
     * 获取流程资源文件
     */
    @RequestMapping("/getProcessResource")
    public void getProcessResource(
    		@RequestParam("type") String resourceType,
    		@RequestParam("pdid") String processDefinitionId, 
    		HttpServletResponse response){
    	try {
			InputStream in = activitiService.getProcessResource(resourceType, processDefinitionId);
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = in.read(b, 0, 1024)) != -1) {
			    response.getOutputStream().write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 挂起流程
     * @return
     */
    @SysLog(value="挂起流程")
    @RequestMapping("/sleep")
    @ResponseBody
    public Object sleep(String id){
    	try {
    		repositoryService.suspendProcessDefinitionById(id);
			return renderSuccess("流程挂起成功！");
		} catch (Exception e) {
			return renderError("流程挂起失败！");
		}
    }
    
    /**
     * 激活流程
     * @return
     */
    @SysLog(value="激活流程")
    @RequestMapping("/active")
    @ResponseBody
    public Object active(String id){
    	try {
    		repositoryService.activateProcessDefinitionById(id);
			return renderSuccess("流程激活成功！");
		} catch (Exception e) {
			return renderError("流程激活失败！");
		}
    }
    
}
