package com.moutum.mtplatform.system.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.moutum.mtplatform.system.model.SysUserRole;

/**
 * <p>
  * 用户角色关联表 Mapper 接口
 * </p>
 * @author junyang.liu
 */
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {

	List<SysUserRole> selectByUserId(@Param("userId") String userId);

    List<String> selectRoleIdListByUserId(@Param("userId") String userId);
}