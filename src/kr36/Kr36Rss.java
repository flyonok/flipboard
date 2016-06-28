package kr36;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Kr36Rss {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// processXml("http://www.36kr.com/feed");
		processFileXml("E:\\private\\test\\Flipboard\\src\\flipboard.xml");
	}
	
	private static void processXml(String xmlUrl)
	{
		try {
			Map<String, String> map = new HashMap<String, String>();
	        map.put("content","http://purl.org/rss/1.0/modules/content/"); 
			SAXReader saxReader = new SAXReader();
			saxReader.getDocumentFactory().setXPathNamespaceURIs(map); 
        	Document document = saxReader.read(new URL(xmlUrl));
        	List<?> list = document.selectNodes("//item" );
        	Iterator<?> iter = list.iterator();
        	while(iter.hasNext()){
        		Element item = (Element)iter.next();
        		processItem(item);
        	}
		}catch(MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// return null;
		}catch(DocumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// return null;
		}
	}
	
	private static void processItem(Element el)
	{
		System.out.println("title:" + el.element("title").getTextTrim());
		System.out.println("link:"+ el.element("title").getTextTrim());
		System.out.println("description:"+ el.element("description").getTextTrim());
		System.out.println("encoded--"+ el.element("encoded").getTextTrim());
	}
	
	private static void processFileXml(String fileName)
	{
		File file = new File(fileName);
		try{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			List<?> list = document.selectNodes("//flipboard/hosts" );
        	Iterator<?> iter = list.iterator();
        	while (iter.hasNext())
        	{
        		Element host = (Element)iter.next();
        		processHostNode(host);
        		// System.out.println(host.getTextTrim());
        	}
			
		}catch(DocumentException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void processHostNode(Element host)
	{
		System.out.println("description:" + host.element("description").getTextTrim());
		System.out.println("url:" + host.element("url").getTextTrim());
		System.out.println("type:" + host.element("type").getTextTrim());
		System.out.println("getsMethod:" + host.element("getsMethod").getTextTrim());
		// System.out.println("getsMethod:" + host.element("getsMethod").getTextTrim());
		Element newsListNode = host.element("newsList");
		processNewsListNode(newsListNode);
		Element newsNode = host.element("newsProcess");
		processNewsNode(newsNode);
	}
	
	private static void processNewsListNode(Element newsListNode)
	{
		System.out.println("begin:" + newsListNode.element("begin").getTextTrim());
		System.out.println("excludePattern:" + newsListNode.element("excludePattern").getTextTrim());
		System.out.println("listTagPattern:" + newsListNode.element("listTagPattern").getTextTrim());
		System.out.println("newsUrlPattern:" + newsListNode.element("newsUrlPattern").getTextTrim());
		System.out.println("newsTitlePattern:" + newsListNode.element("newsTitlePattern").getTextTrim());
	}
	
	private static void processNewsNode(Element newsNode)
	{
		System.out.println("newsContentTag:" + newsNode.element("newsContentTag").getTextTrim());
		// process replace
		Element replace = newsNode.element("contentReplace");
		List<?> replaceItems = replace.elements("item");
		Iterator<?> replaceIter = replaceItems.iterator();
		while (replaceIter.hasNext())
		{
			Element item = (Element)replaceIter.next();
			System.out.println("orgPattern:" + item.element("orgPattern").getTextTrim());
			System.out.println("newContent:" + item.element("newContent").getTextTrim());
		}
		
		// related items
		Element related = newsNode.element("relatedItems");
		List<?> relatedItems = related.elements("item");
		Iterator<?> relatedIter = relatedItems.iterator();
		while (relatedIter.hasNext())
		{
			Element relItem = (Element)relatedIter.next();
			System.out.println("item:" + relItem.getTextTrim());
		}
		
		// pages
		Element pages = newsNode.element("pages");
		System.out.println("selectPattern:" + pages.element("selectPattern").getTextTrim());
		System.out.println("urlValue:" + pages.element("urlValue").getTextTrim());
		
	}
	

}
