package com.knox.uranus.security.aspect;

import com.knox.uranus.security.annotation.CacheException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Redis缓存切面，防止Redis宕机影响正常业务逻辑
 *
 * @author knox
 * @date 2020/3/17
 */
@Aspect
@Component
@Order(2)
public class RedisCacheAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    /**
     * 匹配com.knox.uranus包及其子包下的所有方法
     * <p>
     * 缓存切入点：这里我们使用切点的形式
     * 通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   @Pointcut("execution(...)")
     * <p>
     * 当然也可以通过注解的形式
     * 注解表达式:  @Pointcut("@annotation(com.knox.xx.tiny.MyLog)")
     */
    @Pointcut("execution(public * com.knox.uranus.*.service.*CacheService.*(..))")
    public void cachePointcut() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("cachePointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 获取目标方法
        Method method = methodSignature.getMethod();
        Object result = null;
        try {
            // 前: 可获取缓存
            // 执行切面
            result = joinPoint.proceed();
            // 后: 可做数据缓存
        } catch (Throwable throwable) {
            //有CacheException注解的方法需要抛出异常
            if (method.isAnnotationPresent(CacheException.class)) {
                throw throwable;
            } else {
                LOGGER.error(throwable.getMessage());
            }
        }
        return result;
    }

}
