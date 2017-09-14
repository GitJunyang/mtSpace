package com.moutum.mtplatform.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.moutum.mtplatform.common.result.Tree;
import com.moutum.mtplatform.system.dao.SysDepartmentDao;
import com.moutum.mtplatform.system.model.SysDepartment;
import com.moutum.mtplatform.system.service.SysDepartmentService;


/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author junyang.liu
 * @since 2017-08-09
 */
@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentDao, SysDepartment> implements SysDepartmentService {

	@Autowired
	private SysDepartmentDao sysDepartmentDao;
	
	@Override
	public List<Tree> selectTree() {
        List<SysDepartment> departmentList = selectTreeGrid();

        List<Tree> trees = new ArrayList<Tree>();
        if (departmentList != null) {
            for (SysDepartment department : departmentList) {
                Tree tree = new Tree();
                tree.setId(department.getId());
                tree.setText(department.getDepartmentName());
                tree.setIconCls(department.getDepartmentIcon());
                tree.setPid(department.getParentId());
                trees.add(tree);
            }
        }
        return trees;
    }

	@Override
	public List<SysDepartment> selectTreeGrid() {
        EntityWrapper<SysDepartment> wrapper = new EntityWrapper<SysDepartment>();
        wrapper.orderBy("sequence", true);
        return sysDepartmentDao.selectList(wrapper);
    }
	
}
