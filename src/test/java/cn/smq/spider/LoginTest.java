package cn.smq.spider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * simulate login
 * @author orz
 *
 */
public class LoginTest {
	@Test
	public void test() throws Exception {
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		HttpPost httpPost = new HttpPost("http://svn.jundie.net/user/login");
		
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("uid", "kirant"));
		parameters.add(new BasicNameValuePair("pwd", "www.sina.com"));
		HttpEntity entity = new UrlEncodedFormEntity(parameters);
		httpPost.setEntity(entity );
		CloseableHttpResponse response = client.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 302){
			Header[] headers = response.getHeaders("location");
			String directurl = "";
			if(headers.length > 0){
				Header header = headers[0];
				directurl = header.getValue();
			}
			httpPost.setURI(new URI("http://svn.jundie.net"+directurl));
			response = client.execute(httpPost);
			System.out.println(EntityUtils.toString(response.getEntity()));
		}
	}

}
