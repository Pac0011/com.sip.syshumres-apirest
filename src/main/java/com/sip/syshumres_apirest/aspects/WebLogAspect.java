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


@Aspect
@Component
@Order(3)
public class WebLogAspect {
	private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
	private static final String MSG_BEFORE = "Before method: ";
	private static final String MSG_FORMAT_OUTPUT = "user:{}, ip:{}, url-{}";
	
	ThreadLocal<Long> timeThreadLocal = new ThreadLocal<>();
	
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
		if (joinPoint != null && joinPoint.getSignature() != null) {
		    logger.info(String.format("%1$s %2$s", MSG_BEFORE, joinPoint.getSignature()));
		}
		if (request.getRequestURL() != null) {
		    logger.info(MSG_FORMAT_OUTPUT, getUserName(), getIpAddr(request), request.getRequestURL().toString());
		}
		if (joinPoint != null && joinPoint.getArgs() != null) {
			Object[] signatureArgs = joinPoint.getArgs();
			for (Object signatureArg: signatureArgs) {
				logger.info(String.format("Arg: %1$s", signatureArg));
			}
		}
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
		logger.info(String.format("%1$s %2$s", MSG_BEFORE, joinPoint.getSignature()));
		if (request.getRequestURL() != null) {
		    logger.info(MSG_FORMAT_OUTPUT, getUserName(), getIpAddr(request)
		    		, request.getRequestURL().toString());
		}
		logger.info(String.format("New: %1$s", newEntity));
		logger.info("================End create=======================");
    }
	
	@Before("logEditEntityPointcut(originalEntity, newEntity)")  
    public void logEditAdvice(JoinPoint joinPoint, Object originalEntity, Object newEntity) {
		timeThreadLocal.set(System.currentTimeMillis());
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			logger.info("=========attributes == null===========");
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		logger.info("=========Edit entity===========");
		logger.info(String.format("%1$s %2$s", MSG_BEFORE, joinPoint.getSignature()));
		if (request.getRequestURL() != null) {
		    logger.info(MSG_FORMAT_OUTPUT, getUserName(), getIpAddr(request)
		    		, request.getRequestURL().toString());
		}
		logger.info(String.format("Original: %1$s", originalEntity));
		logger.info(String.format("New: %1$s", newEntity));
		logger.info("================End edit=======================");
    }
	
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
	
	private String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		String searchUnknown = "unknown";
		if (ipAddress == null || ipAddress.length() == 0 || searchUnknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || searchUnknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || searchUnknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					if (inet != null) {
						ipAddress= inet.getHostAddress();
				    }
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") >= 0) {
			ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
		}
		
		return ipAddress; 
	}

}
