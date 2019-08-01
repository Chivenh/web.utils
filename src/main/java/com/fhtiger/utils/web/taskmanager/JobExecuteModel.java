package com.fhtiger.utils.web.taskmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 任务基类
 *
 * @author LFH
 * @since 2018年10月12日 13:30
 */
public abstract class JobExecuteModel implements   Job {

	protected Logger logger= LogManager.getLogger(this.getClass());

	protected static  String JOB_NAME="job_name";
	protected static  String JOB_GROUP="job_group";
	protected static  String JOB_METHOD="job_method";
	protected  static  String JOB_TRIGGER="job_trigger";
	protected static  String JOB_TRIGGER_PARAM="job_trigger_param";
	protected static  String JOB_ADDONS="job_addons";

	/**
	 * 默认的获取任务执行参数的方法
	 * @param context 执行上下文
	 * @return 任务装配参数 {@link JobOpts}
	 */
	@SuppressWarnings("unchecked")
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

}
