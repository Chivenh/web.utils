package com.fhtiger.utils.web.taskmanager;

import java.io.Serializable;
import java.util.Map;

/**
 * Task 项级类
 *
 * @author LFH
 * @since 2018年10月12日 09:46
 */
public final class TaskBean<T extends Serializable> implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskId;//任务Id
	private String taskName;//任务
	private String taskGroup;//任务所在类全名称
	private String taskTrigger;//任务执行的方法名
	private String taskMethod;//任务执行的方法名
	private String taskExpression;//任务频率 和cron语法保持一致
	private Object[] taskParams={};//执行任务方法的参数
	private T taskSource;//任务来源
	private Map<String,Object> taskAddons;//附加信息

	public Map<String, Object> getTaskAddons() {
		return taskAddons;
	}

	public void setTaskAddons(Map<String, Object> taskAddons) {
		this.taskAddons = taskAddons;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	public String getTaskTrigger() {
		return taskTrigger;
	}

	public void setTaskTrigger(String taskTrigger) {
		this.taskTrigger = taskTrigger;
	}

	public String getTaskMethod() {
		return taskMethod;
	}

	public void setTaskMethod(String taskMethod) {
		this.taskMethod = taskMethod;
	}

	public String getTaskExpression() {
		return taskExpression;
	}

	public void setTaskExpression(String taskExpression) {
		this.taskExpression = taskExpression;
	}

	public Object[] getTaskParams() {
		return taskParams;
	}

	public void setTaskParams(Object[] taskParams) {
		if(taskParams!=null){
			this.taskParams = taskParams;
		}
	}

	public T getTaskSource() {
		return taskSource;
	}

	public void setTaskSource(T taskSource) {
		this.taskSource = taskSource;
	}
}
