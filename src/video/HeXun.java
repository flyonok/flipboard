package video;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// import sun.org.mozilla.javascript.internal.NativeArray;

public class HeXun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getVideo("http://tv.hexun.com/2012-08-07/144444301.html");
		// testSplit();

	}

	private static void testSplit() {
		String strVal = "http://tv.hexun.com/2012-08-07/144444301.html|http://tv.hexun.com/2012-08-07/144444286.html|http://tv.hexun.com/2012-08-07/144444255.html|http://tv.hexun.com/2012-08-07/144444202.html|http://tv.hexun.com/2012-08-06/144400296.html|http://tv.hexun.com/2012-08-06/144399961.html|http://tv.hexun.com/2012-08-06/144396172.html|http://tv.hexun.com/2012-08-06/144396116.html|http://tv.hexun.com/2012-08-06/144396108.html|http://tv.hexun.com/2012-08-06/144396060.html|";
		String[] strArray = strVal.split("\\|");
		for (String val : strArray)
			System.out.println(val);
	}
	
	private static void getVideo(String videoUrl)
	{
		try
		{
			// Document doc = Jsoup.parse(new URL(videoUrl).openStream(), "utf8", videoUrl);
			Document doc = Jsoup.connect(videoUrl).get();
			Elements scriptElements = doc.select("script");
			// System.out.println(scriptElements.html());
			String strTvurl = get_tvurl_first(scriptElements);
			System.out.println("tvurl[0]: " + strTvurl);
			Video video = getVideos(scriptElements);
			System.out.println("videos: " + video);
			String html = getVedioHtml(strTvurl, video);
			System.out.println(html);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static String get_tvurl_first(Elements els)
	{
		String strTvurl = "";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		// Element scriptEls = els.select(":contains(tvurl=)").first();
		// String[] strArray = scriptEls.html().split("|");
		for (Element el : els)
		{
			String html = el.html();
			// System.out.println(html);
			if (html.indexOf("tvurl=") != -1)
			{
				try
				{
					Object obj = null;
					engine.eval(html);
					obj = engine.get("tvurl");
					
					String strVal = obj.toString();
					// System.out.println(strVal);
					String[] strArray = strVal.split("\\|");
					// System.out.println(strArray.toString());
					/*for (String val : strArray)
						System.out.println(val);*/
					strTvurl = strArray[0];
					videoCnt = strArray.length;
					break;
				} catch(ScriptException e)
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
			}
		}
		return strTvurl;
	}
	
	private static Video getVideos(Elements els)
	{
		String strVedios = "";
		Gson gson = new Gson();
		// ScriptEngineManager manager = new ScriptEngineManager();
		// ScriptEngine engine = manager.getEngineByName("JavaScript");
		for (Element el : els)
		{
			String html = el.html();
			// System.out.println(html);
			if (html.indexOf("videos") != -1)
			{
				strVedios = html.substring(html.indexOf("["), html.indexOf(";"));
				// System.out.println(strVedios);
				// JsonArray
				List<Video> videos = gson.fromJson(strVedios, new TypeToken<List<Video>>(){}.getType());
				// System.out.println(videos.size());
				for (Video video : videos) {
					if (video != null )
						// System.out.println(video.toString());
						return video;
				}
				break;
				
			}
		}
		
		return null;
	}
	
	private static String getVedioHtml(String tvurl, Video video)
	{
		StringBuilder strBuf = new StringBuilder();
		String strImgPrefix = "http://img.hexun.com"; 
		if (video == null)
			return strBuf.toString();
		strBuf.append("<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0\" name=\"playerv31\" id=\"playerv31\" width=\"504\" height=\"426\">");
		strBuf.append("\r\n");
		strBuf.append("<param name=\"movie\" value=\"");
		strBuf.append(strImgPrefix);
		strBuf.append("/swf/2010/player2010.swf?vid=");
		strBuf.append(video.getVideoid());
		strBuf.append("&pre=");
		strBuf.append(video.getDate());
		strBuf.append("&pic=");
		strBuf.append(video.getPic());
		strBuf.append("&auto=1&n=0&len=");
		strBuf.append(videoCnt);
		strBuf.append("'&su=");
		strBuf.append(tvurl);
		strBuf.append("&v12\"/>");
		strBuf.append("\r\n");
		strBuf.append("<param name=\"quality\" value=\"high\">");
		strBuf.append("\r\n");
		strBuf.append("<param name=\"bgcolor\" value=\"#000000\">");
		strBuf.append("\r\n");
		strBuf.append("<param name=\"allowScriptAccess\" value=\"always\" />");
		strBuf.append("\r\n");
		strBuf.append("<param name=\"allowFullScreen\" value=\"true\" />");
		strBuf.append("\r\n");
		strBuf.append("<param name=\"wmode\" value=\"transparent\" />");
		strBuf.append("\r\n");
		// embed
		strBuf.append("<embed name=\"playerv31\" id=\"playerv31\" wmode=\"transparent\" src=\"");
		strBuf.append(strImgPrefix);
		strBuf.append("/swf/2010/player2010.swf?vid=");
		strBuf.append(video.getVideoid());
		strBuf.append("&pre=");
		strBuf.append(video.getDate());
		strBuf.append("&pic=");
		strBuf.append(video.getPic());
		strBuf.append("&auto=1&n=0&len=");
		strBuf.append(videoCnt);
		strBuf.append("'&su=");
		strBuf.append(tvurl);
		strBuf.append("&v12\"");
		strBuf.append(" quality=\"high\" bgcolor=\"#000000\" allowScriptAccess=\"always\" allowFullScreen=\"true\" ");
		strBuf.append(" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"");
		strBuf.append(" width=\"504\" height=\"426\"></embed>");
		strBuf.append("\r\n");
		strBuf.append("</object>");
		return strBuf.toString();
	}

	// video length
	private static int videoCnt = 0;
}
