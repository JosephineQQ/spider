package cn.smq.spider.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.smq.spider.domain.Page;
/*
 * get web page content 
 * 获取页面内容
 */
public class PageUtils {
	static Logger logger = LoggerFactory.getLogger(PageUtils.class);
	public static String getContent(String url) {
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		String content = null;
		//Page page = new Page();
		HttpGet request = new HttpGet(url);
		try {
			long start_time = System.currentTimeMillis();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			//System.out.println(EntityUtils.toString(entity));
			//page.setContent(EntityUtils.toString(entity));
			content = EntityUtils.toString(entity);
			
			//********* 可以用来统计spider性能 can be used to calculate spider performance********
			logger.info("successfully loading current page, url is:{}, it takes:{}", url, System.currentTimeMillis()-start_time);
		} catch (ClientProtocolException e) {
			//e.printStackTrace();
			logger.error("loading current page failed,url:{},ClientProtocolException",url);
		} catch (IOException e) {
			logger.error("loading current page failed,url:{},IOException",url);
			//e.printStackTrace();
		}
		return content;
	}
}
