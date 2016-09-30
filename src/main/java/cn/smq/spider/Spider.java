package cn.smq.spider;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.smq.spider.domain.Page;
import cn.smq.spider.download.Downloadable;
import cn.smq.spider.download.HttpClientDownload;
import cn.smq.spider.process.JdProcess;
import cn.smq.spider.process.Processable;
import cn.smq.spider.repository.QueueRepository;
import cn.smq.spider.repository.Repository;
import cn.smq.spider.store.ConsoleStore;
import cn.smq.spider.store.HbaseStore;
import cn.smq.spider.store.Storeable;
import cn.smq.spider.utils.HtmlUtils;
import cn.smq.spider.utils.PageUtils;

public class Spider {
	private Downloadable downloadable;
	private Processable processable;
	private Storeable storeable;
	private Repository repository;
	
	//private Queue<String> queue = new ConcurrentLinkedDeque<String>();
	

	public void start() {
		//download();
		//process();
		//store();
		while (true) {
			String url = repository.poll();
			if (StringUtils.isNotBlank(url)){
				Page page = this.download(url);
				this.process(page);
				List<String> urls = page.getUrls();
				for (String nextUrl : urls) {
					//parse page url first then parse each items price
					//先解析列表页面再解析商品页面
					if (nextUrl.startsWith("http://item.jd.com/")) {
						repository.add(nextUrl);
					} else {
						repository.addHigh(nextUrl);
					}
					

				}
				
				if (url.startsWith("http://item.jd.com/")) {
					this.store(page);
				} else {
					System.out.println("This is a new page ----------------->" + url);
				}
			}
		}
	}
	
	
	/******************************************
	 * download function
	 * @return 
	 */
	public Page download(String url) {
		Page page = this.downloadable.download(url);
		return page;
	}

	/******************************************
	 * process parser
	 * @param page 
	 */
	public void process(Page page) {
		this.processable.process(page);
	}
	
	/*******************************************
	 * store data
	 * @param page 
	 */
	public void store(Page page) {
		//System.out.println(page.getUrl() + "---" + page.getValues().get("price"));
		this.storeable.store(page);
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}
	
	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}


	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}
	
	public void setSeedUrl (String url) {
		this.repository.add(url);
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	public static void main (String[] args) {
		Spider spider = new Spider();
		String url = "http://list.jd.com/list.html?cat=9987%2C653%2C655&go=0";
		//spider.setSeedUrl(url);  pointer null
		
		spider.setDownloadable(new HttpClientDownload());
		spider.setProcessable(new JdProcess());
		spider.setStoreable(new ConsoleStore());
		spider.setRepository(new QueueRepository());
		spider.setSeedUrl(url);
		
		spider.start();
	}
}