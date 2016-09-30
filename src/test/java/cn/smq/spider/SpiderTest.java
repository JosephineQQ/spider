package cn.smq.spider;

import org.junit.Test;

import cn.smq.spider.domain.Page;
import cn.smq.spider.download.HttpClientDownload;
import cn.smq.spider.process.JdProcess;

public class SpiderTest {
	@Test
	public void test() throws Exception {
		Spider spider = new Spider();
		//spider.start();
		spider.setDownloadable(new HttpClientDownload());
		spider.setProcessable(new JdProcess());
		String url = "http://item.jd.com/1861097.html";
		Page page = spider.download(url);
		//System.out.println(page.getContent());
		spider.process(page);
		System.out.println(page.getValues().get("spec"));
		spider.store(page);
	}
	
	
}
