package com.fhtiger.utils.web.taskmanager;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 任务装配参数
 */
public final class JobOpts {
	private String jobName;
	private String jobGroup;
	private Class<?> triggerClass;
	private Method method;
	private Object[] params;
	private Map<String,Object> addons;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public Class<?> getTriggerClass() {
		return triggerClass;
	}

	public void setTriggerClass(Class<?> triggerClass) {
		this.triggerClass = triggerClass;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Map<String, Object> getAddons() {
		return addons;
	}

	public void setAddons(Map<String, Object> addons) {
		this.addons = addons;
	}
}
