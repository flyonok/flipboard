package sinaNews;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Sina {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();
		OutlineManager outlineManager = new OutlineManager();
		List<OutLine> outList = outlineManager.getCannels("http://rss.sina.com.cn/sina_news_opml.xml");
		for (OutLine outline : outList)
		{
			System.out.println("begin:" + outline.getTitle());
			processOutLine(outline);
			processChildList(outline.childrenOutline());
			System.out.println("end:" + outline.getTitle());
		}
		long endTime = System.currentTimeMillis();
		System.out.println("program consume time:" + (endTime - startTime) + "ms");		

	}
	
	private static void processChildList(List<OutLine> childList)
	{
		if (childList.isEmpty()) return;
		for (OutLine child : childList)
		{
			System.out.println("begin:" + child.getTitle() + "\t" + child.getXmlUrl());
			processOutLine(child);
			processChildList(child.childrenOutline());
			System.out.println("end:" + child.getTitle() + "\t" + child.getXmlUrl() );
		}
	}
	
	private static void processOutLine(OutLine outline)
	{
		if (outline.getXmlUrl() != null )
		{
			try {
				news.GetNewsItemList(new URL(outline.getXmlUrl()));
			}catch (MalformedURLException e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private static NewsManager news = new NewsManager();

}
