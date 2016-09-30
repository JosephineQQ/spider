package cn.smq.spider.download;
import cn.smq.spider.domain.Page;

public interface Downloadable {
	Page download(String url);
}
