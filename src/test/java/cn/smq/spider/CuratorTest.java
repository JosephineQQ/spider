package cn.smq.spider;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.InetAddress;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;


public class CuratorTest {
	@Test
	public void test1() throws Exception {
		//curatorFrameworkFactory
		String connectString = "192.168.1.171:2181,192.168.1.172:2181";
		int sessionTimeoutMs = 5000;//response failed time, must between 4s and 40s
		int connectionTimeoutMs = 1000;//connection time out
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
		
		InetAddress localHost = InetAddress.getLocalHost();
		String ip = localHost.getHostAddress();
		client.create()
		.creatingParentsIfNeeded()
		.withMode(CreateMode.EPHEMERAL)//Ephemeral Node 
		.withACL(Ids.OPEN_ACL_UNSAFE)
		.forPath("/spider/"+ip);
	}		
}
