package cn.qblank.logdemo.interceptor;

import cn.qblank.logdemo.annotion.LoggerProfile;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用AOP对方法进行日志记录
 * @author evan_qb
 * @date 2018/10/18
 */
@Slf4j
@Aspect
@Component
public class LoggerProfileInterceptor {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(cn.qblank.logdemo.annotion.LoggerProfile)")
    public void controllerAspect(){

    }

    /**
     * 执行日志
     * 在执行操作执行
     * 用于拦截Controller层记录用户操作
     * @param joinPoint
     */
    @After("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip)){
            ip = request.getRemoteAddr();
        }
        try {
            log.info(ip + "在 {} 时访问了:{},url:{}" ,sdf.format(new Date()),getControllerMethodDescription(joinPoint),request.getRequestURI());
        }catch (Exception e){
            log.error("错误,{}",e);
        }
    }

    /**
     * 记录异常信息
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取请求ip,由于服务器做了集群，使用了nginx反向代理所以request.getRemoteAddr并不能获取客户端真实地址，而是得到127.0.0.1
        String ip = request.getHeader("x-forwarded-for");
        if(StringUtils.isEmpty(ip)){
            ip = request.getRemoteAddr();
        }
        try {
            //目前是打印信息  项目中可以将这些操作存入日志表中
            log.error("用户" + ip + "访问:" + request.getRequestURI() + "," + getControllerMethodDescription(joinPoint) + "时出现了异常，" +
                    "异常代码:" + e.getClass() + ",异常信息:" + e.getMessage() + ",异常方法:"
                    + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()" +
                    "具体请查看日志");
        }catch (Exception e2){
            log.error("错误,{}",e2);
        }

    }

    /**
     * 获取注解中对方法的描述信息  用于Controller注解
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception{
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods){
            if (method.getName().equals(methodName)){
                Class<?>[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length){
                    //获取注解参数中的值
                    if (method.getAnnotation(LoggerProfile.class) != null){
                        description = method.getName() + "(" + method.getAnnotation(LoggerProfile.class).description() + ")";
                    }
                    break;
                }
            }
        }
        return description;
    }
}
