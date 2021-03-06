package cn.smq.spider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.smq.spider.domain.Page;
import cn.smq.spider.download.Downloadable;
import cn.smq.spider.download.HttpClientDownload;
import cn.smq.spider.process.JdProcess;
import cn.smq.spider.process.Processable;
import cn.smq.spider.repository.QueueRepository;
import cn.smq.spider.repository.RedisRepository;
import cn.smq.spider.repository.Repository;
import cn.smq.spider.store.ConsoleStore;
import cn.smq.spider.store.HbaseStore;
import cn.smq.spider.store.Storeable;
import cn.smq.spider.utils.Config;
import cn.smq.spider.utils.HtmlUtils;
import cn.smq.spider.utils.PageUtils;
import cn.smq.spider.utils.SleepUtils;

import java.net.InetAddress;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public class Spider {
	private Downloadable downloadable = new HttpClientDownload();
	private Processable processable;
	private Storeable storeable = new ConsoleStore();
	private Repository repository = new QueueRepository();
	Logger logger = LoggerFactory.getLogger(Spider.class);
	ExecutorService threadPool = Executors.newFixedThreadPool(Config.nThread);
	
	//private Queue<String> queue = new ConcurrentLinkedDeque<String>();
	
	//constructor, for zookeeper
	public Spider() {
		//get zookeeper connection
		String connectString = "192.168.1.171:2181,192.168.1.172:2181"; //connect to zookeeper cluster
		int sessionTimeoutMs = 5000;//response failed time, must between 4s and 40s
		int connectionTimeoutMs = 1000;//connection time out
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
		
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			String ip = localHost.getHostAddress();
			client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)//Ephemeral Node 
			.withACL(Ids.OPEN_ACL_UNSAFE)
			.forPath("/spider/"+ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	public void start() {
		//download();
		//process();
		//store();
		check();
		logger.info("####################start a spider ###################");
		while (true) {
			final String url = repository.poll();
			if (StringUtils.isNotBlank(url)){
				
				threadPool.execute(new Runnable() {
					public void run() {
						Page page = Spider.this.download(url);
						Spider.this.process(page);
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
							Spider.this.store(page);
						} else {
							System.out.println("This is a new page ----------------->" + url);
						}
						
						SleepUtils.sleep(Config.millis_1);
					}
				});
			} else {
				logger.info("currently do not have urls, sleep for a while");
				SleepUtils.sleep(Config.millis_5);
			}
		}
	}
	
	private void check() {
		if(processable==null){
			//没有设置默认解析类
			String message = "not set up default parser class";
			logger.error(message);
			throw new RuntimeException(message);
		}
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		logger.info("downloadable implementation class is:{}",downloadable.getClass().getName());
		logger.info("processable implementation class is:{}",processable.getClass().getName());
		logger.info("storeable implementation class is:{}",storeable.getClass().getName());
		logger.info("repository implementation class is:{}",repository.getClass().getName());
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
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
		
		//spider.setDownloadable(new HttpClientDownload());
		spider.setProcessable(new JdProcess());
		//spider.setStoreable(new ConsoleStore());
		//spider.setRepository(new QueueRepository());
		
		//spider.setRepository(new RedisRepository());
		spider.setSeedUrl(url);
		
		spider.start();
	}
}
