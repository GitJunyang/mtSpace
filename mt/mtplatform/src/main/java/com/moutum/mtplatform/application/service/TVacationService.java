package com.moutum.mtplatform.application.service;

import com.baomidou.mybatisplus.service.IService;
import com.moutum.mtplatform.application.model.TVacation;

/**
 * <p>
 * 请假表  服务类
 * </p>
 *
 * @author junyang.liu
 * @since 2017-08-18
 */
public interface TVacationService extends IService<TVacation> {

	/**
	 * 启动请假流程
	 * @param tVacation
	 */
	void startVacation(TVacation tVacation);
	
	

}
