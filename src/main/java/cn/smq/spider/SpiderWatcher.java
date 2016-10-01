package cn.smq.spider;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

//Watcher is a backend process, need always running

public class SpiderWatcher implements Watcher{

	CuratorFramework client;
	List<String> children = new ArrayList<String>();
	
	
	public SpiderWatcher() {
		//get zookeeper connection
		String connectString = "192.168.1.171:2181,192.168.1.172:2181"; //connect to zookeeper cluster
		int sessionTimeoutMs = 5000;//response failed time, must between 4s and 40s
		int connectionTimeoutMs = 1000;//connection time out
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
		
		//watch specific Node, watch parent Node /spider 's children Nodes, when nodes changed invoke process()
		try {
			children = client.getChildren().usingWatcher(this).forPath("/spider");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//zookeeper watcher is One-time trigger
	//监控单次有效，重复使用需要重复注册
	@Override
	public void process(WatchedEvent event) {
		//nodes changed
		System.out.println("Nodes modified" + event);
		
		try {
			List<String> newChildren = client.getChildren().usingWatcher(this).forPath("/spider");
			for (String node : newChildren) {
				if(children.contains(node) == false) {
					System.out.println("******NEW NODE" + node + "******");
				}
			}
			
			for (String node : children) {
				if (newChildren.contains(node) == false) {
					System.out.println("!!!!!!DELETE NODE" + node + "!!!!!!" );
					/*
					 * can implement java mail functionality to send email to Administrator
					 * 
					 * TODO
					 */
				}
			}
			
			this.children = newChildren;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		SpiderWatcher spiderWatcher = new SpiderWatcher();
		spiderWatcher.run();
	}
	
	// make sure watcher always running
	private void run() {
		while(true) {
			; // do noting
		}
	}

}
