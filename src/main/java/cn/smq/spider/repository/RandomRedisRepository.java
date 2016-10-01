package cn.smq.spider.repository;

import java.util.HashMap;
import java.util.Random;

import cn.smq.spider.utils.DomainUtils;
import cn.smq.spider.utils.RedisUtils;

// distributed spider sharing common message queue 

public class RandomRedisRepository implements Repository{

	//key: domain name, value: key in redis ###### key == value (key in redis)
	//key：顶级域名 value：redis中列表的key
	HashMap<String, String> hashMap = new HashMap<String, String>();
	RedisUtils redisUtils = new RedisUtils();
	Random random = new Random();
	
	@Override
	public String poll() {
		String[] array = hashMap.keySet().toArray(new String[hashMap.size()]);
		int nextInt = random.nextInt(array.length);
		String key = hashMap.get(array[nextInt]);
		
		return redisUtils.poll(key);
	}

	@Override
	public void add(String urlstr) {
		String topDomain = DomainUtils.getTopDomain(urlstr);
		String value = hashMap.get(topDomain);
		
		if (value == null) {
			value = topDomain;
			hashMap.put(topDomain, value);
		}
		
		redisUtils.add(value, urlstr);
	}

	@Override
	public void addHigh(String urlstr) {
		
	}

}
