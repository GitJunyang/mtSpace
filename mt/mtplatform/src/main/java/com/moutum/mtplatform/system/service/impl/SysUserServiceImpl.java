package com.moutum.mtplatform.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.moutum.mtplatform.common.utils.BeanUtils;
import com.moutum.mtplatform.common.utils.PageInfo;
import com.moutum.mtplatform.common.utils.StringUtils;
import com.moutum.mtplatform.system.dao.SysUserDao;
import com.moutum.mtplatform.system.dao.SysUserRoleDao;
import com.moutum.mtplatform.system.model.SysUser;
import com.moutum.mtplatform.system.model.SysUserRole;
import com.moutum.mtplatform.system.service.SysUserService;
import com.moutum.mtplatform.system.vo.SysUserVo;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author junyang.liu
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public List<SysUser> selectByLoginName(SysUserVo userVo) {
		SysUser user = new SysUser();
		user.setLoginName(userVo.getLoginName());
		EntityWrapper<SysUser> wrapper =new EntityWrapper<SysUser>(user);
		if(userVo.getId() != null){
			wrapper.where("id != {0}", userVo.getId());
		}
		return this.selectList(wrapper);
	}

	@Override
	public void insertByVo(SysUserVo userVo) {
		SysUser user = BeanUtils.copy(userVo, SysUser.class);
		user.setRegisterTime(new Date());
		this.insert(user);
		
		String userId = user.getId();
		String[] roleIds = userVo.getRoleIds().split(",");
		SysUserRole userRole = new SysUserRole();
		
		for(String roleId : roleIds){
			userRole.setRoleId(roleId);
			userRole.setUserId(userId);
			sysUserRoleDao.insert(userRole);
		}
		
	}

	@Override
	public SysUserVo selectVoById(String id) {
		return sysUserDao.selectSysUserVoById(id);
	}

	@Override
	public void updateByVo(SysUserVo userVo) {
		SysUser user= BeanUtils.copy(userVo, SysUser.class);
		if(StringUtils.isBlank(userVo.getLoginPwd())){
			user.setLoginPwd(null);
		}
		this.updateById(user);
		
		String userId = userVo.getId();
		List<SysUserRole> userRoles= sysUserRoleDao.selectByUserId(userId);
		if(userRoles != null && !userRoles.isEmpty()){
			for(SysUserRole userRole : userRoles){
				sysUserRoleDao.deleteById(userRole.getId());
			}
		}
		
		String[] roleIds= userVo.getRoleIds().split(",");
		for(String roleId : roleIds){
			SysUserRole userRole = new SysUserRole();
			userRole.setRoleId(roleId);
			userRole.setUserId(userId);
			sysUserRoleDao.insert(userRole);
		}
	}

	@Override
	public void updatePwdByUserId(String userId, String md5Hex) {
		SysUser user = new SysUser();
		user.setId(userId);
		user.setLoginPwd(md5Hex);
		this.updateById(user);
	}

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<SysUserVo> page = new Page<SysUserVo>(pageInfo.getNowpage(), pageInfo.getSize());
        List<SysUserVo> list = sysUserDao.selectSysUserVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public void deleteUserById(String id) {
		this.deleteById(id);
		List<SysUserRole> userRoles= sysUserRoleDao.selectByUserId(id);
		if(userRoles != null && !userRoles.isEmpty()){
			for(SysUserRole userRole : userRoles){
				sysUserRoleDao.deleteById(userRole.getId());
			}
		}
	}
	
}
