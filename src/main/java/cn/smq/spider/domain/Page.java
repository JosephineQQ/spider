package cn.smq.spider.domain;

import java.util.HashMap;
import java.util.Map;

public class Page {
	/*
	 * original content from web page
	 */
	private String content;
	//original url
	private String url;
	//store items info
	private Map<String, String> values = new HashMap<>();

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
}
