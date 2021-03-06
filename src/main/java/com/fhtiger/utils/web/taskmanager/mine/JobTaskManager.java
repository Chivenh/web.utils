package com.fhtiger.utils.web.taskmanager.mine;

import com.fhtiger.utils.web.taskmanager.JobExecuteModel;
import com.fhtiger.utils.web.taskmanager.SimpleTaskManager;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 创建任务管理器bean,以方便注入使用.
 *
 * @author LFH
 * @since 2018年10月12日 10:34
 */
public final class JobTaskManager implements SimpleTaskManager {

	private SchedulerFactory factory;

	@Override
	public Class<? extends JobExecuteModel> getJobExcueteClass() {
		return JobExecuteWithBean.class;
	}

	@Override
	public SchedulerFactory getSchedulerFactoryBean() {
		if(factory!=null){
			return factory;
		}
		 factory=new StdSchedulerFactory();
		try {
			factory.getScheduler().start();
		}catch (Exception e){
			e.printStackTrace();
		}
		return factory;
	}
}
