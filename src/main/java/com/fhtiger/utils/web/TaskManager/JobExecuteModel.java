package com.fhtiger.utils.web.TaskManager;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 任务基类
 *
 * @author LFH
 * @date 2018年10月12日 13:30
 */
public abstract class JobExecuteModel implements   Job {
	public static  String JOB_NAME="job_name";
	public static  String JOB_GROUP="job_group";
	public static  String JOB_METHOD="job_method";
	public  static  String JOB_TRIGGER="job_trigger";
	public static  String JOB_TRIGGER_PARAM="job_trigger_param";
	public static  String JOB_ADDONS="job_addons";

	/**
	 * 默认的获取任务执行参数的方法
	 * @param context
	 * @return
	 */
	protected JobOpts getJobOpts(JobExecutionContext context){
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
		String jobName=(String) jobDataMap.get(JOB_NAME);
		Class<?> classzz=(Class<?>) jobDataMap.get(JOB_GROUP);
		String groupName=classzz.getName();
		Method method=(Method) jobDataMap.get(JOB_METHOD);
		Object[] parames=(Object[]) jobDataMap.get(JOB_TRIGGER_PARAM);
		Map<String,Object> addons= (Map<String,Object>)jobDataMap.get(JOB_ADDONS);
		JobOpts jobOpts=new JobOpts();
		jobOpts.setJobName(jobName);
		jobOpts.setJobGroup(groupName);
		jobOpts.setTriggerClass(classzz);
		jobOpts.setMethod(method);
		jobOpts.setParams(parames);
		jobOpts.setAddons(addons);
		return jobOpts;
	}

	/**
	 * 任务时装配参数
	 */
	protected class JobOpts{
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
}
