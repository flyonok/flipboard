package common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpHelper {
	
	public static String processUrlRedirect(Element head)
	{
		String redirectUrl = "";
		Elements scriptElements = head.select("script");
		// ScriptEngineManager manager = new ScriptEngineManager();
		// ScriptEngine engine = manager.getEngineByName("JavaScript");
		for (Element el : scriptElements)
		{
			String html = el.html();
			// System.out.println(html);
			int nFind = html.indexOf("window.location.href");
			if ( nFind != -1)
			{
				redirectUrl = html.substring(html.indexOf('\'', nFind)+ 1, html.indexOf(';', nFind) - 1);
				break;
				/*try
				{
					Object obj = null;
					engine.eval(html);
					obj = engine.get("window.location.href");
					
					redirectUrl = obj.toString();
					redirectUrl = html.substring(html.indexOf('\'')+ 1, html.indexOf(';') - 1);
					break;
				} catch(ScriptException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				} finally {
					return redirectUrl;
				}*/
				
			}
		}
		return redirectUrl;
	}
	
	// 获取网页最后更新时间	
	public static long getHtmlPageTime(String strUrl)
	{
		long time = 0;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.connect();
			time = con.getLastModified();
			/*if (time > 0)
			{
				System.out.println("getLastModified:" + new Date(time));
			}*/
			if (time == 0) {
				time = con.getDate();
				/*if (time > 0)
				{
					System.out.println("getDate:" + new Date(time));
				}*/
				if (time == 0) {
					time =  System.currentTimeMillis();
					// System.out.println("localDate:" + new Date(time));
				}
			}
			con.disconnect();
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return time;
	}

}
