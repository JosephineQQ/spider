package cn.smq.spider.repository;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import cn.smq.spider.utils.DomainUtils;

/*
 * random queue to avoid IP blocking
 * Map<Key, value> ---> key: ebay, amazon, jd, taobao etc. website, value : queue
 */

public class RandomQueueRepository implements Repository{
	
	HashMap<String, Queue<String>> hashMap = new HashMap<String, Queue<String>>();
	Random random = new Random();

	@Override
	public String poll() {
		String[] array = hashMap.keySet().toArray(new String[hashMap.size()]);
		int nextInt = random.nextInt(array.length);
		Queue<String> queue = hashMap.get(array[nextInt]);
		
		return queue.poll();
	}

	@Override
	public void add(String urlstr) {
		String topDomain = DomainUtils.getTopDomain(urlstr);
		Queue<String> queue = hashMap.get(topDomain);	
		if (queue == null) {
			queue = new ConcurrentLinkedDeque<String>();
		}
		queue.add(urlstr);
	}

	@Override
	public void addHigh(String urlstr) {
		add(urlstr);
	}

}
