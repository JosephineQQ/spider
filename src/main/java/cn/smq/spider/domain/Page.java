package cn.smq.spider.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class Page {
	/*
	 * original content from web page
	 */
	private String content;
	//original url
	private String url;
	
	// goods id
	private String goodsId;
	
	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


	//store items info
	private Map<String, String> values = new HashMap<>();
	/*
	 * 临时存储列表页面中解析出来的url
	 * Temporarily store urls parsing from web pages
	 */
	private List<String> urls = new ArrayList<String>();

	public List<String> getUrls() {
		return urls;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getValues() {
		return values;
	}


	public void addField (String key, String value) {
		this.values.put(key, value);
	}

	public void addUrl(String url) {
		this.urls.add(url);
	}
}
