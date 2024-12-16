package com.xxxx.crm.exceptions;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理类
 */
@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    /**
     * 异常处理方法
     *  方法的返回值：
     *      1、返回视图
     *      2、返回json数据
     *  如何判断方法的返回值？
     *      可以通过方法上是否声明@ResponseBody注解（反射机制获取方法上的注解）
     *          如果未声明，则返回视图
     *          如何声明了，则返回json数据
     *
     * @param request request对象
     * @param response response对象
     * @param handler 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        e.printStackTrace();

        //判断是否抛出未登录异常
        if (e instanceof NoLoginException) {
            //重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        //设置默认的异常处理（返回数据）
        ModelAndView modelAndView = new ModelAndView("error");
        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请稍后再试....");

        //判断HandlerMethod 方法对象
        if(handler instanceof HandlerMethod) {
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上声明的ResponseBody注解对象（反射机制）
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断ResponseBody对象是否为空（如果对象为空，则返回视图，否则返回json数据）
            if(responseBody == null) {
                //返回视图

                //判断异常是否是自定义异常
                if(e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                } else if(e instanceof AuthException) { //认证异常
                    AuthException a = (AuthException) e;
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }
            } else {
                //返回json数据（resultInfo）
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请稍后重试");

                //判断异常是否是自定义异常
                if(e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                } else if(e instanceof AuthException) {
                    AuthException a = (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }

                //设置响应类型及编码格式
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter pw = null;
                try {
                    //获取字符输出流
                    pw = response.getWriter();
                    //将需要返回的resultInfo对象转换成json格式的字符串
                    String json = JSON.toJSONString(resultInfo);
                    //输出数据
                    pw.write(json);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } finally {
                    if(pw != null) {
                        pw.close();
                    }
                }
            }
        }
        return modelAndView;
    }
}
