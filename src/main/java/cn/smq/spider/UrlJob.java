package cn.smq.spider;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.smq.spider.utils.RedisUtils;

/*
 * put all entrance urls into url repository
 * only once executed every day
 */

public class UrlJob implements Job{
	
	RedisUtils redisUtils = new RedisUtils();

	@Override
	public void execute(JobExecutionContext content) throws JobExecutionException {
		//System.out.println("Woooooooow, timing scheduler execute");
		List<String> list = redisUtils.lrange(RedisUtils.start_url, 0, -1);
		
		for (String url : list) {
			redisUtils.add(RedisUtils.heightkey, url);
		}
	}
	
}
