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

import cn.smq.spider.domain.Page;
/*
 * get web page content 
 * 获取页面内容
 */
public class PageUtils {
	public static String getContent(String url) {
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		String content = null;
		//Page page = new Page();
		HttpGet request = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			//System.out.println(EntityUtils.toString(entity));
			//page.setContent(EntityUtils.toString(entity));
			content = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}
