package cn.smq.spider.utils;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class HtmlUtils {
	//get tag content 获取指定标签内容
	public static String getText (TagNode rootNode, String xpath) {
		String result = null;
		try {
			Object[] evaluateXPath = rootNode.evaluateXPath(xpath);
			if(evaluateXPath.length > 0) {
				TagNode titleNode = (TagNode) evaluateXPath[0];
				result = titleNode.getText().toString();
			}

		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	//get tag attribute 获取标签属性
	public static String getAttributeByName (TagNode rootNode, String xpath, String attr) {
		String result = null;
		try {
			Object[] evaluateXPath = rootNode.evaluateXPath(xpath);
			if(evaluateXPath.length > 0) {
				TagNode tagNode = (TagNode)evaluateXPath[0];
				result = tagNode.getAttributeByName(attr);
				
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
