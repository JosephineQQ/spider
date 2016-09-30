package cn.smq.spider.repository;

public interface Repository {

	String poll();

	void add(String urlstr);

	void addHigh(String urlstr);
}
