package com.xiafei.tools.springboot.aop;

import com.virgo.finance.fm.common.data.Code;
import com.virgo.finance.fm.common.data.Messages;
import com.virgo.finance.fm.common.exceptions.BizException;
import com.virgo.finance.fm.common.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <P>Description: 接口实现类aop. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/20</P>
 * <P>UPDATE DATE: 2017/11/20</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
@Aspect
@Component
public class AopConfig {

    /**
     * 定义拦截规则：拦截web.api.impl包下面的所有类返回值是Message并且不含有IgnoreAop注解的public方法.
     */
    @Pointcut("execution(!@com.xiafei.tools.springboot.aop.IgnoreAop public " +
            " com.virgo.finance.fm.common.data.Message " +
            "com.virgo.finance.fm.settlement.web.api.impl.*.*(..) )")
    public void apiImplPointcut() {
    }

    /**
     * 拦截所有api实现类的公共方法，catch异常并记录日志.
     *
     * @param point 切点信息对象
     * @return 接口返回内容
     */
    @Around("apiImplPointcut()")
    public Object interceptor(final ProceedingJoinPoint point) {
        final MethodSignature signature = (MethodSignature) point.getSignature();
        final Method method = signature.getMethod(); //获取被拦截的方法
        final String methodName = method.getName(); //获取被拦截的方法名
        final Object[] args = point.getArgs();
        log.info("{}.{}(),param={}", point.getTarget().getClass().getName(), methodName, JSONUtil.toJson(args));

        // 一切正常的情况下，继续执行被拦截的方法
        try {
            return point.proceed();
        } catch (BizException e) {
            return Messages.failed(e.getCode(), e.getMessage());
        } catch (Throwable throwable) {
            log.error("{},uncaughtException,param={}", methodName, JSONUtil.toJson(args), throwable);
            return Messages.failed(Code.SYSTEMEXCEPTION.getValue(), throwable.getMessage());
        }
    }
}
