package com.moutum.mtplatform.system.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.moutum.mtplatform.common.base.BaseController;
import com.moutum.mtplatform.common.operlog.SysLog;
import com.moutum.mtplatform.common.utils.DigestUtils;
import com.moutum.mtplatform.common.utils.StringUtils;


/**
 * Main
 * @author liu.junyang
 */
@Controller
public class MainController extends BaseController {
    /**
     * 首页
     * @return
     */
    @GetMapping("/")
    public String index() {
    	if (!SecurityUtils.getSubject().isAuthenticated()) {
            return "login";
        }
        return "index";
    }

    /**
     * GET 登录
     * @return {String}
     */
    @GetMapping("/login")
    public String login() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "index";
        }
        return "login";
    }

    /**
     * POST 登录 shiro 写法
     * @param username 用户名
     * @param password 密码
     * @return {Object}
     */
    @SysLog(value="用户登录")
    @PostMapping("/login")
    @ResponseBody
    public Object loginPost(HttpServletRequest request, String loginName, String loginPwd) {
        if (StringUtils.isBlank(loginName)) {
        	return renderError("用户名不能为空");
        }
        if (StringUtils.isBlank(loginPwd)) {
        	return renderError("密码不能为空");
        }
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, DigestUtils.md5Hex(loginPwd));
        try {
            user.login(token);
            return renderSuccess();
        } catch (UnknownAccountException e) {
        	return renderError("账号不存在！");
        } catch (DisabledAccountException e) {
        	return renderError("账号未启用！");
        } catch (IncorrectCredentialsException e) {
        	return renderError("密码错误！");
        } catch (Throwable e) {
        	return renderError("未知异常！");
        }
    }

    /**
     * 未授权
     * @return {String}
     */
    @GetMapping("/unauth")
    public String unauth() {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return "login";
        }
        return "error/unauth";
    }
    
    /**
     * 404
     * @return {String}
     */
    @GetMapping("/404")
    public String error404() {
        return "error/404";
    }
    
    /**
     * 500
     * @return {String}
     */
    @GetMapping("/500")
    public String error500() {
        return "error/500";
    }

    /**
     * 退出
     * @return {Result}
     */
    @PostMapping("/logout")
    @ResponseBody
    public Object logout() {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return "login";
        }
        SecurityUtils.getSubject().logout();
        return renderSuccess();
    }

    /**
     * 系统图标
     */
   @GetMapping("/icons")
    public String icons() {
        return "icons";
    }
}
