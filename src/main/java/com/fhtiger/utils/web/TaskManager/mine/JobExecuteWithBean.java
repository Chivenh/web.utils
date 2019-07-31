package com.fhtiger.utils.web.TaskManager.mine;

import com.fhtiger.utils.web.TaskManager.JobExecuteModel;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * ...
 *
 * @author LFH
 * @date 2018年10月12日 13:50
 */
public final class JobExecuteWithBean extends JobExecuteModel {

	public static String needBean="need_bean";

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
			e.printStackTrace();
			try {
				context.getScheduler().deleteJob(new JobKey(jobName,groupName));
			} catch (SchedulerException e1) {
				e1.printStackTrace();
			}
		}
	}
}
