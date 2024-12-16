package com.xxxx.crm.aspect;

import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 切面类
 */
@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    /**
     * 切面会拦截指定包下指定注解
     *  会拦截com.xxxx.crm.annotation包下的RequiredPermission注解
     * @param pjp
     * @return
     */
    @Around(value = "@annotation(com.xxxx.crm.annotation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = new Object();
        //得到当前登录用户，拥有的权限（session作用域）
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        //判断用户是否拥有权限
        if(null == permissions || permissions.size() < 1) {
            //抛出权限认证异常
            throw new AuthException();
        }

        //得到对应的目标方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        //得到方法上的注解
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //判断注解上对应的状态码（判断用户的所有权限中是否包含该方法上注解指定的权限码）
        if(!(permissions.contains(requiredPermission.code()))) {
            //不包含
            throw new AuthException();
        }
        result = pjp.proceed();
        return result;
    }
}
