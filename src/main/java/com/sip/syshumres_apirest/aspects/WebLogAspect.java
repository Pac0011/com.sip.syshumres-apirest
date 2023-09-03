package com.sip.syshumres_apirest.aspects;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.core.annotation.Order;

//import com.alibaba.fastjson.JSON;
//import org.springframework.core.annotation.Order;

@Aspect
@Component
@Order(3)
public class WebLogAspect {
	private final static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
	ThreadLocal<Long> timeThreadLocal = new ThreadLocal<Long>();
	
	@Pointcut("@annotation(LogWeb)")  
    public void logPointcut() {  
    }
	
	@Pointcut("@annotation(LogCreateEntity) && args(newEntity)")  
    public void logCreateEntityPointcut(Object newEntity) {  
    }
	
	@Pointcut("@annotation(LogEditEntity) && args(originalEntity, newEntity)")  
    public void logEditEntityPointcut(Object originalEntity, Object newEntity) {  
    } 
	
	@Before("logPointcut()")  
    public void logAllMethodCallsAdvice(JoinPoint joinPoint) {
		timeThreadLocal.set(System.currentTimeMillis());
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		logger.info("=========oo===========");
		logger.info("Before method: " + joinPoint.getSignature());
		logger.info("user:{}, ip:{}, url-{}", getUserName(), getIpAddr(request), request.getRequestURL().toString());
		Object[] signatureArgs = joinPoint.getArgs();
		   for (Object signatureArg: signatureArgs) {
			   logger.info("Arg: " + signatureArg);
		}
		//String qualifiedName = joinPoint.getSignature().getDeclaringTypeName().concat(".")
		//		+ joinPoint.getSignature().getName().concat("()");
		//logger.info("{},args:{}",qualifiedName, JSON.toJSONString(joinPoint.getArgs()));
		logger.info("================ooo=======================");
    }
	
	@Before("logCreateEntityPointcut(newEntity)")  
    public void logCreateAdvice(JoinPoint joinPoint, Object newEntity) {
		timeThreadLocal.set(System.currentTimeMillis());
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		logger.info("=========Create entity===========");
		logger.info("Before method: " + joinPoint.getSignature());
		logger.info("user:{}, ip:{}, url-{}", getUserName(), getIpAddr(request), request.getRequestURL().toString());
		logger.info("New: " + newEntity);
		logger.info("================End edit=======================");
    }
	
	@Before("logEditEntityPointcut(originalEntity, newEntity)")  
    public void logEditAdvice(JoinPoint joinPoint, Object originalEntity, Object newEntity) {
		timeThreadLocal.set(System.currentTimeMillis());
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		logger.info("=========Edit entity===========");
		logger.info("Before method: " + joinPoint.getSignature());
		logger.info("user:{}, ip:{}, url-{}", getUserName(), getIpAddr(request), request.getRequestURL().toString());
		logger.info("Original: " + originalEntity);
		logger.info("New: " + newEntity);
		logger.info("================End edit=======================");
    }
	
   /* @Before("execution(* com.sip.sysrh.hiring.controller.AuthorityController.*(..))")
	public void beforeAdvice(JoinPoint joinPoint) {
		
		System.out.println("\n====================");
		System.out.println("Before method: " + joinPoint.getSignature());
		System.out.println("User: " + this.getUserName() );
		Object[] signatureArgs = joinPoint.getArgs();
		   for (Object signatureArg: signatureArgs) {
		      System.out.println("Arg: " + signatureArg);
		    
		}
		System.out.println("=======================================\n");
	}*/
	
	@AfterReturning("logPointcut()")
	public void doAfterReturning(JoinPoint joinPoint) throws Throwable {
		logger.info("time: {}", (System.currentTimeMillis() - timeThreadLocal.get()) + "ms");
	}
	
	private String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			return auth.getName();
		}
		
		return null;
	}
	
	private String getIpAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress= inet.getHostAddress();
			}
		}
		if(ipAddress!=null && ipAddress.length()>15){
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress; 
	}

}
