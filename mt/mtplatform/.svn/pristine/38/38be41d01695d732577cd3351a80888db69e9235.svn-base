package com.moutum.mtplatform.common.workflow.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moutum.mtplatform.activiti.model.TUserTask;
import com.moutum.mtplatform.activiti.service.TUserTaskService;


/**
 * 多实例人员分配
 * @author liujunyang
 */
@Component("startListener")
public class StartListener implements ExecutionListener,Serializable{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private TUserTaskService tUserTaskService;
	@Autowired
    private RepositoryService repositoryService;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		//获取流程定义KEY
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(execution.getProcessDefinitionId())
				.singleResult();
		String processDefinitionKey = processDefinition.getKey();
		try {
			//获取流程对应的任务列表
			EntityWrapper<TUserTask> wrapper = new EntityWrapper<TUserTask>();
			wrapper.where("proc_def_key = {0}", processDefinitionKey);
			List<TUserTask> taskList = tUserTaskService.selectList(wrapper);
			for(TUserTask userTask : taskList){
				String taskKey = userTask.getTaskDefKey();
				String taskType = userTask.getTaskType();
				String ids = userTask.getCandidateIds();
				if("usertask1".equals(taskKey)){
					switch (taskType){
						case "counterSign" : {
							String[] userIds = ids.split(",");
							List<String> users = new ArrayList<String>();
							for(int i=0; i<userIds.length;i++){
								users.add(userIds[i]);
							}
							execution.setVariable("signUsers", users);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
