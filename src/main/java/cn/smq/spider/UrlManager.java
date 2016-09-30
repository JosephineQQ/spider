package cn.smq.spider;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

//responsible to dispatch url
public class UrlManager {
	public static void main (String[] args) {
		try {
			Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
			defaultScheduler.start();//start the scheduler
			
			String simpleName = UrlJob.class.getSimpleName();
			JobDetail jobDetail = new JobDetail(simpleName, Scheduler.DEFAULT_GROUP, UrlJob.class);
			
			
			// operate when 20:45 every day
			CronTrigger trigger = new CronTrigger(simpleName, Scheduler.DEFAULT_GROUP, "0 45 20 ? * * "); 
			defaultScheduler.scheduleJob(jobDetail, trigger );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
