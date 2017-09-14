package com.moutum.mtplatform.application.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.moutum.mtplatform.activiti.vo.CommonVo;
import com.moutum.mtplatform.application.dao.TVacationDao;
import com.moutum.mtplatform.application.model.TVacation;
import com.moutum.mtplatform.application.service.TVacationService;
import com.moutum.mtplatform.common.shiro.ShiroUser;
import com.moutum.mtplatform.common.utils.ConstantUtils;
import com.moutum.mtplatform.common.utils.DateUtils;


/**
 * <p>
 * 请假表  服务实现类
 * </p>
 * @author junyang.liu
 * @since 2017-08-18
 */
@Service
public class TVacationServiceImpl extends ServiceImpl<TVacationDao, TVacation> implements TVacationService {
    @Autowired private TVacationDao tVacationDao;
    @Autowired private RuntimeService runtimeService;
    
	@Override
	public void startVacation(TVacation tVacation) {
		//1.新增请假
    	tVacation.setApplyDate(new Date());
    	//获取Shiro中的用户信息
    	ShiroUser shiroUser= (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	tVacation.setUserId(shiroUser.getId());
    	tVacation.setVacationStatus(ConstantUtils.vacationStatus.APPROVING.getValue());
        tVacationDao.insert(tVacation);
        //2.添加流程变量
        CommonVo vo = new CommonVo();
    	vo.setBusinessType(ConstantUtils.VACATION);
    	vo.setBusinessKey(ConstantUtils.VACATION);
    	vo.setApplyUserId(shiroUser.getId());
    	vo.setApplyUserName(shiroUser.getName());
    	vo.setApplyTitle(shiroUser.getName()+"于 "+DateUtils.formatDateToString(tVacation.getApplyDate())+" 的请假申请");
    	Map<String,Object> variables=new HashMap<String,Object>();
		variables.put(ConstantUtils.MODEL_KEY, vo);
		//设置流程变量请假天数
		variables.put("days", tVacation.getWorkDays());
		//3.启动流程实例
		ProcessInstance processInstance= runtimeService.startProcessInstanceByKey(ConstantUtils.VACATION,tVacation.getId(),variables);
		tVacation.setProcInstId(processInstance.getId());
		tVacationDao.updateById(tVacation);
	}
    
    
}
