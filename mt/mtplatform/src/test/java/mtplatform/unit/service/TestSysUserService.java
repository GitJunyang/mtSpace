package mtplatform.unit.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.moutum.mtplatform.system.service.SysUserService;
import com.moutum.mtplatform.system.vo.SysUserVo;

/**
 * @author liujunyang
 * 常用的断言:(参数[String message]不是必填项)
 * 1.assertEquals([String message],expected,actual)
 * 2.assertTrue ([String message],Boolean condition)
 * 3.assertFalse([String message],Boolean condition)
 * 4.assertNull([String message],Object object)
 * 5.assertNotNull([String message],Object object)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring-config.xml","classpath*:spring-mvc.xml"})
public class TestSysUserService {
	Logger logger = Logger.getLogger(TestSysUserService.class);
	
	@Autowired
	private SysUserService sysUserService;
	
	@Test
	@Transactional
	@Rollback(true)
	public void insertByVo(){
		SysUserVo user =  new SysUserVo();
		user.setId("001");
		user.setLoginName("test");
		user.setLoginPwd("123");
		user.setRoleIds("1");
		sysUserService.insertByVo(user);
	};
	

}
