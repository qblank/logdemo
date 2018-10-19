package cn.qblank.logdemo.annotion;

import java.lang.annotation.*;

/**
 * 自定义注解拦截
 * @author evan_qb
 * @date 2018/10/18
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerProfile {
    String description() default "";
}
