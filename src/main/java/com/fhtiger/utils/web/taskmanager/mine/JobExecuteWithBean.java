package com.fhtiger.utils.web.taskmanager.mine;

import com.fhtiger.utils.web.taskmanager.JobExecuteModel;
import com.fhtiger.utils.web.taskmanager.JobOpts;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 实现注入spring bean,执行bean对应方法的执行器
 *
 * @author LFH
 * @since 2018年10月12日 13:50
 */
public final class JobExecuteWithBean extends JobExecuteModel {

	public static final String needBean="need_bean";

	@Override
	public void execute(JobExecutionContext context) {
		String jobName="",groupName="";
		try {
			JobOpts jobOpts=getJobOpts(context);
			jobName=jobOpts.getJobName();
			groupName=jobOpts.getJobGroup();
			Method method=jobOpts.getMethod();
			Object[] objects=jobOpts.getParams();
			Map<String,Object> addons=jobOpts.getAddons();
			method.invoke(addons.get(needBean), objects);
		} catch (Exception e) {
			logger.error("error:{0}",e);
			try {
				context.getScheduler().deleteJob(new JobKey(jobName,groupName));
			} catch (SchedulerException e1) {
				logger.error("error:{0}",e1);
			}
		}
	}
}
