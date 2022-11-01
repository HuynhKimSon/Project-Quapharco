package vn.com.irtech.core.framework.aspectj;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.enums.BusinessStatus;
import vn.com.irtech.core.common.json.JSON;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.framework.manager.AsyncManager;
import vn.com.irtech.core.framework.manager.factory.AsyncFactory;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.domain.SysOperLog;

/**
 * Operation log record processing
 * 
 * @author admin
 */
@Aspect
@Component
public class LogAspect {
	private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

	// Configure weaving point
	@Pointcut("@annotation(vn.com.irtech.core.common.annotation.Log)")
	public void logPointCut() {
	}

	/**
	 * Execute after processing the request
	 *
	 * @param joinPoint join point
	 */
	@AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
		handleLog(joinPoint, null, jsonResult);
	}

	/**
	 * Intercept abnormal operations
	 * 
	 * @param joinPoint cut point
	 * @param e         exception
	 */
	@AfterThrowing(value = "logPointCut()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
		handleLog(joinPoint, e, null);
	}

	protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
		try {
			// Get comments
			Log controllerLog = getAnnotationLog(joinPoint);
			if (controllerLog == null) {
				return;
			}

			// Get current user
			LoginUser currentUser = ShiroUtils.getLoginUser();

			// *========Database log=========*//
			SysOperLog operLog = new SysOperLog();
			operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
			// Requested address
			String ip = ShiroUtils.getIp();
			operLog.setOperIp(ip);
			// Return parameter
			operLog.setJsonResult(JSON.marshal(jsonResult));

			operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
			if (currentUser != null) {
				operLog.setOperName(currentUser.getLoginName());
//				if (StringUtils.isNotNull(currentUser.getDept())
//						&& StringUtils.isNotEmpty(currentUser.getDept().getDeptName())) {
//					operLog.setDeptName(currentUser.getDept().getDeptName());
//				}
			}

			if (e != null) {
				operLog.setStatus(BusinessStatus.FAIL.ordinal());
				operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
			}
			// Setting method name
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			operLog.setMethod(className + "." + methodName + "()");
			// Set request method
			operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
			// Process the parameters on the setting annotation
			getControllerMethodDescription(controllerLog, operLog);
			// Save database
			AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
		} catch (Exception exp) {
			// Record local exception log
			log.error("==Pre-notification exception==");
			log.error("Exception information: {}", exp.getMessage());
			exp.printStackTrace();
		}
	}

	/**
	 * Get the description of the method in the annotation for the Controller layer
	 * annotation
	 * 
	 * @param log     Log
	 * @param operLog Operation log
	 * @throws Exception
	 */
	public void getControllerMethodDescription(Log log, SysOperLog operLog) throws Exception {
		// Set action
		operLog.setBusinessType(log.businessType().ordinal());
		// Set title
		operLog.setTitle(log.title());
		// Set operator category
		operLog.setOperatorType(log.operatorType().ordinal());
		// Do you need to save request, parameters and values
		if (log.isSaveRequestData()) {
			// Get the parameter information and transfer it to the database.
			setRequestValue(operLog);
		}
	}

	/**
	 * Get the requested parameters and put them in the log
	 * 
	 * @param operLog Operation log
	 * @throws Exception abnormal
	 */
	private void setRequestValue(SysOperLog operLog) throws Exception {
		Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
		Map<String, String> newMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : map.entrySet()) {
			if (entry.getValue().length == 1) {
				newMap.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		String params = JSON.marshal(newMap);
		operLog.setOperParam(StringUtils.substring(params, 0, 2000));
	}

	/**
	 * Whether there is an annotation, get it if it exists
	 */
	private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(Log.class);
		}
		return null;
	}
}
