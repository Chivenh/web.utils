package com.fhtiger.utils.web.TaskManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * @since 2018年10月12日 10:11
 */
public interface SimpleTaskManager {

	Logger logger= LogManager.getLogger(SimpleTaskManager.class);

	ConcurrentHashMap<String, TaskBean> allTask = new ConcurrentHashMap<>();

	/**
	 * 获取 SchedulerFactory
	 * @return {@link SchedulerFactory}
	 */
	SchedulerFactory getSchedulerFactoryBean();

	/**
	 * 获取任务代理类
	 * @return {@link JobExecuteModel}
	 */
	Class<? extends JobExecuteModel> getJobExcueteClass();

	/**
	 * 获取全部任务
	 * @return 获取全部任务
	 */
	default List<TaskBean> getAllTask() {
		return new ArrayList<>(allTask.values());
	}

	/**
	 * 获取对应名称的 CronTrigger
	 * @param trigger trigger名称
	 * @param group trigger分组名
	 * @return {@link CronTrigger}
	 */
	default CronTrigger getTrigger(String trigger, String group) {
		CronTrigger cronTrigger = null;
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			cronTrigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(
					trigger, group));
		} catch (SchedulerException e) {
			logger.error("error:{0}",e);
		}
		return cronTrigger;
	}

	/**
	 * 添加任务
	 * @param task {@link TaskBean} 任务
	 * @return 返回添加成功的任务
	 */
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
			logger.error("error:{0}",e);
		}
		return task;
	}

	/**
	 * 重启任务
	 * @param task {@link TaskBean} 任务
	 * @return 返回重启成功的任务
	 */
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
			logger.error("error:{0}",e);
		}

		return task;
	}

	/**
	 * 删除任务
	 * @param task {@link TaskBean}
	 * @return 返回删除的任务
	 */
	default TaskBean deleteTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobKey jobKey = new JobKey(task.getTaskName(), task.getTaskGroup());
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("error:{0}",e);
		}
		return task;
	}

	/**
	 * 暂停任务
	 * @param task {@link TaskBean}
	 * @return 返回被暂停的任务
	 */
	default TaskBean pauseTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobKey jobKey = new JobKey(task.getTaskName(), task.getTaskGroup());
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("error:{0}",e);
		}
		return task;
	}

	/**
	 * 恢复被暂停的任务
	 * @param task {@link TaskBean}
	 * @return 返回恢复的任务
	 */
	default TaskBean resumeTask(TaskBean task) {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			JobKey jobKey = new JobKey(task.getTaskName(), task.getTaskGroup());
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("error:{0}",e);
		}
		return task;
	}

	/**
	 * 批量删除任务
	 * @param scheduleTasks 要批量删除的任务
	 */
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
			logger.error("error:{0}",e);
		}
	}

	/**
	 * 清除所有任务
	 * @see #deleteTasks(Collection)
	 */
	default void clearAllTasks(){
		deleteTasks(allTask.values());
	}

	/**
	 * 暂停所有任务
	 */
	default void pauseAllTask() {
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			logger.error("error:{0}",e);
		}
	}

	/**
	 * 恢复所有被暂停的任务
	 */
	default void resumeAllTask(){
		try {
			Scheduler scheduler = getSchedulerFactoryBean().getScheduler();
			scheduler.resumeAll();
		} catch (SchedulerException e) {
			logger.error("error:{0}",e);
		}
	}
}
