package cn.smq.spider.repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.commons.lang.StringUtils;

public class QueueRepository implements Repository{
	//low priority , high priority
	private Queue<String> low_queue = new ConcurrentLinkedDeque<String>();
	private Queue<String> high_queue = new ConcurrentLinkedDeque<String>();

	@Override
	public String poll() {
		// retrieve from high priority queue, if not exist then retrieve from low priority queue
		String url = high_queue.poll();
		if (StringUtils.isEmpty(url)) {
			url = low_queue.poll();
		}
		return url;
	}

	@Override
	public void add(String urlstr) {
		this.low_queue.add(urlstr);
		
	}

	@Override
	public void addHigh(String urlstr) {
		this.high_queue.add(urlstr);
		
	}

}
