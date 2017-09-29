package com.moutum.mtplatform.thirdparty.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.moutum.mtplatform.system.model.SysUser;
import com.moutum.mtplatform.system.service.SysResourceService;
import com.moutum.mtplatform.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/dingapi")
@Api(value="测试接口")
public class TestDingApiController {
	@Autowired
    private SysUserService sysUserService;
	@Autowired
    private SysResourceService resourceService;
	
	/**
	 * 用户信息查询
	 */
	@RequestMapping(value="/loginName/{name}",method=RequestMethod.GET)
	@ApiOperation(value="用户信息查询",notes="根据登录名进行查询")
    @ResponseBody
    public Object selectByLoginName(@PathVariable String name){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("login_name", name);
		List<SysUser> list = sysUserService.selectByMap(map);
		return list;
	}
    
	
	/**
     * 资源管理列表
     */
    @PostMapping("/treeGrid")
    @ApiOperation(value="资源管理列表",notes="资源管理列表")
    @ResponseBody
    public Object treeGrid() {
        return resourceService.selectAll();
    }
	

}
