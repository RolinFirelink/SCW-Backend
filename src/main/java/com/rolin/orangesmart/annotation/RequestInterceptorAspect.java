package com.rolin.orangesmart.annotation;

import com.rolin.orangesmart.util.CheckUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestInterceptorAspect {

  // 切入点：匹配带有@RequestInterceptor注解的类和方法
  @Around("@within(requestInterceptor) || @annotation(requestInterceptor)")
  public Object interceptRequest(ProceedingJoinPoint joinPoint,
                                 RequestInterceptor requestInterceptor) throws Throwable {

    // 获取方法上的注解（可能为空）
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    RequestInterceptor methodAnnotation = AnnotationUtils.getAnnotation(
        signature.getMethod(), RequestInterceptor.class);

    // 获取类上的注解（可能为空）
    RequestInterceptor classAnnotation = AnnotationUtils.findAnnotation(
        joinPoint.getTarget().getClass(), RequestInterceptor.class);

    // 执行顺序：先类级别注解，后方法级别注解
    if (classAnnotation != null) {
      processAnnotation(classAnnotation.value());
    }
    if (methodAnnotation != null) {
      processAnnotation(methodAnnotation.value());
    }

    // 继续执行原方法
    return joinPoint.proceed();
  }

  private void processAnnotation(int value) {
    CheckUtil.isPermit(value);
  }
}