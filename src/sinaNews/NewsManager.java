package sinaNews;

// import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

// <summary>
// 新闻项管理类
// </summary>
public class NewsManager {

	// <summary>
    // 根据输入的XML地址获取新闻列表。
    // </summary>
    // <param name="xmlUrl">新闻频道的XML地址</param>
    // <returns>NewsItem的结果集合</returns>
	public List<NewsItem> GetNewsItemList(URL xmlUrl)
    {
        List<NewsItem> _myNews = new ArrayList<NewsItem>();
        
        try{
        	SAXReader saxReader = new SAXReader();
        	Document document = saxReader.read(xmlUrl);
        	List<?> list = document.selectNodes("//channel/item" );
        	Iterator<?> iter=list.iterator();
            while( iter.hasNext()){
            	NewsItem node = new NewsItem();
            	Element item =(Element)iter.next();
            	node.setTitle(item.element("title").getTextTrim());
            	node.setLink(item.element("link").getTextTrim());
            	node.setAuthor(item.element("author").getTextTrim());
            	node.setPubDate(getLocalTime(item.element("pubDate").getTextTrim()));
            	node.setCategory(item.element("comments").getTextTrim());
            	node.setDescription(item.element("description").getTextTrim());
            	_myNews.add(node);
            	// test code
            	System.out.println("title:" + node.getTitle());
            	System.out.println("link:" + node.getLink());
            	System.out.println("author:" + node.getAuthor());
            	System.out.println("pubDate:" + node.getPubDate());
            	System.out.println("comments:" + node.getComments());
            	System.out.println("description:" + node.getDescription());
            }
        	
        }catch(DocumentException e){
            System.out.println(e.getMessage());
        }
//		catch(IOException e){
//			System.out.println(e.getMessage());
//		}
        return _myNews;
    }
	
	private String getLocalTime(String utcTime)
	{
		if (utcTime.length() <= 0)
			return utcTime;
		try{
	        SimpleDateFormat sf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US) ;
	        sf.setTimeZone(TimeZone.getTimeZone("GMT"));
	        Date date = sf.parse(utcTime) ;
	        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
	        // SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        sf1.setTimeZone(TimeZone.getTimeZone("Asia/ShangHai"));
	       return sf1.format(date);
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
}
