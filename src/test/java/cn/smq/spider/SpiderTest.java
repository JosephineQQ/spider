package cn.smq.spider;

import java.util.List;

import org.junit.Test;

import cn.smq.spider.domain.Page;
import cn.smq.spider.download.HttpClientDownload;
import cn.smq.spider.process.JdProcess;
import cn.smq.spider.store.ConsoleStore;
import cn.smq.spider.store.HbaseStore;

public class SpiderTest {
	@Test
	public void test() throws Exception {
		
		/*
		Spider spider = new Spider();
		//spider.start();
		spider.setDownloadable(new HttpClientDownload());
		spider.setProcessable(new JdProcess());
		//spider.setStoreable(new ConsoleStore());
		spider.setStoreable(new HbaseStore());
		
		String url = "http://item.jd.com/1861097.html";
		Page page = spider.download(url);
		//System.out.println(page.getContent());
		spider.process(page);
		List<String> urls = page.getUrls();
		for (String urlstr : urls) {
			
		}
		
		//System.out.println(page.getValues().get("spec"));
		spider.store(page); */
	}
	
	
}
