package cn.smq.spider;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import cn.smq.spider.process.Processable;
import cn.smq.spider.utils.HtmlUtils;
import cn.smq.spider.utils.PageUtils;

public class Spider {
	private Downloadable downloadable;
	private Processable processable;
	

	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}

	/*public void start() {
		//download();
		//process();
		//store();
		
	}*/
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
	
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}
}
