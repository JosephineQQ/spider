package cn.smq.spider.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;


import cn.smq.spider.domain.Page;
import cn.smq.spider.utils.HtmlUtils;
import cn.smq.spider.utils.PageUtils;

public class JdProcess implements Processable{

	@Override
	public void process(Page page) {
		String content = page.getContent();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		//parser web page
		TagNode rootNode = htmlCleaner.clean(content); 
		try {
			// get the head
			String title = HtmlUtils.getText(rootNode, "//*[@id=\"name\"]/h1");
			page.addField("title", title);
			//get image address
			String picurl = HtmlUtils.getAttributeByName(rootNode, "//*[@id=\"spec-img\"]/img", "src");
			page.addField("picurl", picurl);
			
			//price async 获取商品价格，异步记载
			String url = page.getUrl();
			Pattern compile = Pattern.compile("http://item.jd.com/([0-9]+).html");
			Matcher matcher = compile.matcher(url);
			String goodsId = "";
			if (matcher.find() == true) {
				goodsId = matcher.group(1);
			}
			
			String jsonResult = PageUtils.getContent("http://p.3.cn/prices/get?skuid=J_" + goodsId);
			JSONArray jsonArray = new JSONArray(jsonResult);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			String price = jsonObject.getString("p");
			page.addField("price", price);
			
			//features info
			Object[] evaluateXPath = rootNode.evaluateXPath("");
			JSONArray specJsonArray = new JSONArray();
			for (Object object : evaluateXPath) {
				TagNode trNode = (TagNode)object;
				if (!"".equals(trNode.getText().toString().trim())) {
					
					JSONObject jsonObj = new JSONObject();
					Object[] evaluateXPath_th = trNode.evaluateXPath("//th");
					
					if(evaluateXPath_th.length > 0) {
						TagNode thNode = (TagNode)evaluateXPath_th[0];
//System.out.println(thNode.getText().toString());
						jsonObj.put("name", thNode.getText().toString());
						jsonObj.put("value", "");

					} else {
						//td
						Object[] evaluateXPath_td = trNode.evaluateXPath("//td");
						if (evaluateXPath_td.length == 2) {
							TagNode tdNode1 = (TagNode)evaluateXPath_td[0];
							TagNode tdNode2 = (TagNode)evaluateXPath_td[1];
							jsonObj.put("name", tdNode1.getText().toString());
							jsonObj.put("value", tdNode2.getText().toString());
//System.out.println(tdNode1.getText().toString() + "||" + tdNode2.getText().toString());
						}
					}
					specJsonArray.put(jsonObj);
				}
			}
			page.addField("spec", specJsonArray.toString());
			
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
