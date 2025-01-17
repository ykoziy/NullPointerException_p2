package com.modfathers.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Log {
	private Logger log = LoggerFactory.getLogger(Log.class);

	public Log() {
	}

	// execution(access_specifier package_name class_name method_name(argument_list))
	@Pointcut("execution(* com.modfathers.service.*.*(..))")
	private void servicePointcut() {
	}

	@Around("servicePointcut()")
	public Object aroundLog(ProceedingJoinPoint jp) throws Throwable {
		String methodName = jp.getSignature().getName();
		String className = jp.getTarget().getClass().getSimpleName();

		CodeSignature cs = (CodeSignature) jp.getSignature();

		String[] paramNames = cs.getParameterNames();
		Class<?>[] paramTypes = cs.getParameterTypes();

		List<String> paramStr = new ArrayList<String>();
		List<String> argsStr = new ArrayList<String>();
		Object[] args = jp.getArgs();

		for (int i = 0; i < paramTypes.length; i++) {
			paramStr.add(paramTypes[i].getSimpleName().concat(" " + paramNames[i]));
		}

		for (int i = 0; i < paramTypes.length; i++) {
			argsStr.add(""+args[i]);
		}

		StringJoiner joiner = new StringJoiner(",");
		paramStr.forEach(item -> joiner.add(item));

		StringJoiner joinerB = new StringJoiner(",");
		argsStr.forEach(item -> joinerB.add(item));

		log.info("Invoking " + className + "." + methodName + "(" + joiner.toString() + ") with arguments: " + joinerB.toString());
		Object obj = jp.proceed();
		log.info(className + "." + methodName + "(" + joinerB.toString() + ") returned: " + obj.getClass().getTypeName());
		return obj;
	}

}
