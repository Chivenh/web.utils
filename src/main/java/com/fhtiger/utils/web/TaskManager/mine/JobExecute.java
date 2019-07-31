package com.fhtiger.utils.web.TaskManager.mine;

import com.fhtiger.utils.web.TaskManager.JobExecuteModel;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 任务执行代理(默认实现)
 *
 * @author LFH
 * @date 2018年10月12日 10:02
 */
public class JobExecute extends JobExecuteModel {

	@Override
	public void execute(JobExecutionContext context) {
		String jobName="";
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			 jobName=(String) jobDataMap.get(JOB_NAME);
			Class<?> classzz=(Class<?>) jobDataMap.get(JOB_GROUP);
			Method method=(Method) jobDataMap.get(JOB_METHOD);
			Object[] objects=(Object[]) jobDataMap.get(JOB_TRIGGER_PARAM);
			method.invoke(classzz.newInstance(), objects);
		} catch (IllegalAccessException|InvocationTargetException|InstantiationException e) {
			e.printStackTrace();
		}
	}
}
