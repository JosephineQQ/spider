package cn.smq.spider.store;

import cn.smq.spider.domain.Page;

public class ConsoleStore implements Storeable{

	@Override
	public void store(Page page) {
		System.out.println(page.getUrl() + "---" + page.getValues().get("price"));
	}
	
}
