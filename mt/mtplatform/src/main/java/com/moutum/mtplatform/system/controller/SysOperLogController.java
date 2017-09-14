package com.moutum.mtplatform.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.moutum.mtplatform.common.base.BaseController;
import com.moutum.mtplatform.common.utils.PageInfo;
import com.moutum.mtplatform.system.model.SysOperLog;
import com.moutum.mtplatform.system.service.SysOperLogService;



/**
 * <p>
 * 系统操作日志  前端控制器
 * </p>
 * @author junyang.liu
 */
@Controller
@RequestMapping("/sysOperLog")
public class SysOperLogController extends BaseController {
    
    @Autowired
    private SysOperLogService sysOperLogService;
    
    @GetMapping("/manager")
    public String manager() {
        return "system/sysOperLog";
    }

    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(SysOperLog sysOperLog, Integer page, Integer rows, String sort,String order) {
        EntityWrapper<SysOperLog> wrapper = new EntityWrapper<SysOperLog>();
        wrapper.setEntity(sysOperLog);
        Page<SysOperLog> pages = getPage(page, rows, sort, order);
        pages = sysOperLogService.selectPage(pages,wrapper);
        return pageToPageInfo(pages);
    }
}
