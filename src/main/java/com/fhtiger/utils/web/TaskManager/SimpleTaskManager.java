package com.fhtiger.utils.web.TaskManager;

import org.quartz.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务管理工具
 *
 * @author LFH
 * @date 2018年10月12日 10:11
 */
public interface SimpleTaskManager {
	ConcurrentHashMap<String, TaskBean> allTask = new ConcurrentHashMap<>();

	/**
	 * 获取 SchedulerFactory
	 * @return
	 */
	SchedulerFactory getSchedulerFactoryBean();

	/**
	 * 获取任务代理类
	 * @return
	 */
	Class<? extends JobExecuteModel> getJobExcueteClass();

	default List<TaskBean> getAllTask() {
		List<TaskBean> list = new ArrayList();
		list.addAll(allTask.values());
		return list;
	}

	default CronTrigger getTrigger(String trigger, String group) {
		CronTrigger cronTrigger = null;
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			cronTrigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(
					trigger, group));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return cronTrigger;
	}

	default TaskBean addTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(getJobExcueteClass())
					.withIdentity(task.getTaskName(), task.getTaskGroup()).build();
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(task.getTaskTrigger(), task.getTaskGroup())
					.startNow()
					.withSchedule(
							CronScheduleBuilder.cronSchedule(task
									.getTaskExpression())).build();
			Class<?> classzz = Class.forName(task.getTaskGroup());
			Class<?>[] c = new Class<?>[task.getTaskParams().length];
			for (int i = 0; i < task.getTaskParams().length; i++) {
				c[i] = task.getTaskParams()[i].getClass();
			}
			Method method = classzz.getMethod(task.getTaskMethod(), c);
			JobDataMap jobDataMap = trigger.getJobDataMap();
			jobDataMap.put(JobExecuteModel.JOB_NAME, task.getTaskName());
			jobDataMap.put(JobExecuteModel.JOB_GROUP, classzz);
			jobDataMap.put(JobExecuteModel.JOB_METHOD, method);
			jobDataMap.put(JobExecuteModel.JOB_TRIGGER, task.getTaskTrigger());
			jobDataMap.put(JobExecuteModel.JOB_TRIGGER_PARAM, task.getTaskParams());
			jobDataMap.put(JobExecuteModel.JOB_ADDONS, task.getTaskAddons());
			scheduler.scheduleJob(jobDetail, trigger);
			if (scheduler.isStarted()) {
				scheduler.start();
			}
			if (!allTask.containsKey(task.getTaskId())) {
				allTask.put(task.getTaskId(), task);
			}
		} catch (SchedulerException|ClassNotFoundException|NoSuchMethodException|SecurityException e) {
			e.printStackTrace();
		}
		return task;
	}

	default TaskBean reStartTask(TaskBean task) {
		try {
			CronTrigger cronTrigger = getTrigger(task.getTaskTrigger(),
					task.getTaskGroup());
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			cronTrigger = cronTrigger
					.getTriggerBuilder()
					.withIdentity(new TriggerKey(task.getTaskTrigger(), task.getTaskGroup()))
					.withSchedule(CronScheduleBuilder.cronSchedule(task.getTaskExpression())
							//解决重新启动任务时,任务会以上一个配置自动执行一次问题.
							.withMisfireHandlingInstructionDoNothing())
					.build();

			// 按新的trigger重新设置job执行

			scheduler.rescheduleJob(
					new TriggerKey(task.getTaskTrigger(), task.getTaskGroup()),
					cronTrigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return task;
	}

	default TaskBean deleteTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobKey jobKey = new JobKey(task.getTaskName(), task.getTaskGroup());
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {

			e.printStackTrace();
		}
		return task;
	}

	default TaskBean pauseTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobKey jobKey = new JobKey(task.getTaskName(), task.getTaskGroup());
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}

	default TaskBean resumeTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobKey jobKey = new JobKey(task.getTaskName(), task.getTaskGroup());
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}

	default void deleteTasks(Collection<TaskBean> scheduleTasks) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			List<JobKey> jobKeys = new ArrayList<JobKey>();
			JobKey jobKey;
			for (TaskBean scheduleTask : scheduleTasks) {
				jobKey = new JobKey(scheduleTask.getTaskName(),
						scheduleTask.getTaskGroup());
				jobKeys.add(jobKey);
			}
			scheduler.deleteJobs(jobKeys);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	default void clearAllTasks(){
		deleteTasks(allTask.values());
	}

	default void pauseAllTask() {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	default void resumeAllTask(){
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			scheduler.resumeAll();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
