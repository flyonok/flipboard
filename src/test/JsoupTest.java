package test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dbTool.Resource;

public class JsoupTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsoupTest test = new JsoupTest();
		// test.testJsoup();
		// test.testSinaNews();
		// test.testSinaPageContent();
		// test.testSinaPicContent();
		// test.testSinaHuNan();
		// test.testSinaSports();
		// test.testHexunNews();
		// test.testHexunBlog();
		// test.testHexunGuoJi();
		// test.testSinaYuLe();
		// test.testSinaBlog();
		// test.testSinaCaiJing();
		// test.testSinaHuNanTravel();
		// test.testSinaTechPage();
		test.testSinaBlogImgPage();
		
	}
	
	// test function
	private void testJsoup() {
		/*
		 * // String html =
		 * "<li class=\"next\"><a target=\"_self\" href=\"http://bank.hexun.com/2012-07-11/143425349_1.html\">涓嬩竴椤�/a></li>"
		 * ; String html =
		 * "<p><strong>鍒嗘瀽璇勮</strong><a href=\"http://bank.hexun.com/2012-07-11/143429244.html\" target=\"_blank\"> </a></p>"
		 * ; org.jsoup.nodes.Document doc = Jsoup.parse(html); // Elements li_s
		 * = doc.select("li.next"); // System.out.println(li_s.outerHtml()); //
		 * Elements strong_s = doc.select("p > strong"); // Elements p_s =
		 * strong_s.parents(); Elements p_s = doc.select("p:contains(鍒嗘瀽璇勮)");
		 * Elements href = p_s.select("a"); // Elements p_s =
		 * doc.select("p > href"); System.out.println(p_s.first().text());
		 * System.out.println(p_s.outerHtml());
		 * System.out.println(href.outerHtml());
		 */

		try {
			/*org.jsoup.nodes.Document doc = Jsoup
					.connect("http://news.hexun.com/2012-09-27/146324525.html""http://news.hexun.com/2012-09-07/145585875.html")
					.timeout(10000).get();*/
			/*URL url = new URL("http://news.hexun.com/2012-09-27/146324525.html");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(url.openStream(), "gbk", "news.hexun.com");*/
			/*Elements els = doc.select("div#bigPicArea");
			System.out.println(els.html());
			Elements p_s = els.select("font.pic_count_max");
			Elements count_s = els.select("font.pic_count");
			System.out.println(Integer.parseInt(p_s.first().text()));
			// System.out.println((count_s.first().text()));
			String strCount = count_s.get(1).text();
			System.out.println(strCount);
			System.out.println(strCount.substring(strCount.indexOf('/') + 1,
					strCount.indexOf(')')));*/
			String content = getCotentFromUrl("http://news.hexun.com/2012-09-27/146324525.html", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements els = doc.select(/*"div#tab01_con1"*/"div#artibody");
			// els.select("table:has(form#theForm14322)").remove();
			// els.removeAll(tabs);
			// String content = tabs.html();
			// doc.outputSettings().charset("gb2312");
			System.out.println(els.html());
			// System.out.println(doc.outputSettings().charset().displayName());
		} /*catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}*/finally {
			
		}
	}
	
	private void testSinaNews()
	{
		try {
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select(/*"div#tab01_con1"*/"span#news_con_2");
			for (Element ele : contentEls) {
				Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					System.out.println(linkText);
				}
			}
		}finally {
			
		}
	}
	
	private void testHexunNews()
	{
		try {
			String content = getCotentFromUrl("http://www.hexun.com", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select(/*"div#tab01_con1"*/"div.b_rm02");
			for (Element conEl : contentEls) 
			{
				Elements listEls = conEl.select("div.sy_dot8x8");
				System.out.println(listEls.size());
				for (Element ele : listEls) {
					Elements links = ele.getElementsByTag("a");
					for (Element link : links) {
						String linkHref = link.attr("href");
						String linkText = link.text();
						System.out.println(linkHref);
						System.out.println(linkText);
					}
				}
			}
		}finally {
			
		}
	}
	
	private void testHexunBlog()
	{
		try {
			String content = getCotentFromUrl("http://www.hexun.com", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select(/*"div#tab01_con1"*/"div.b_rm03");
			for (Element conEl : contentEls) 
			{
				Elements listEls = conEl.select("ul.gb_ul14b");
				System.out.println(listEls.size());
				for (Element ele : listEls) {
					Elements links = ele.getElementsByTag("a");
					for (Element link : links) {
						String linkHref = link.attr("href");
						String linkText = link.text();
						System.out.println(linkHref);
						System.out.println(linkText);
					}
				}
			}
		}finally {
			
		}
	}
	
	private void testHexunGuoJi()
	{
		try {
			String content = getCotentFromUrl("http://www.hexun.com", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select(/*"div#tab01_con1"*/"div.b_rm03");
			for (Element conEl : contentEls) 
			{
				Elements listEls = conEl.select("ul.sy_ul14c");
				System.out.println(listEls.size());
				for (Element ele : listEls) {
					Elements links = ele.getElementsByTag("a");
					for (Element link : links) {
						String linkHref = link.attr("href");
						String linkText = link.text();
						System.out.println(linkHref);
						System.out.println(linkText);
					}
				}
			}
		}finally {
			
		}
	}
	
	private void testSinaHuNan()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			String sina = doc.html();
			System.out.println(sina.indexOf("http://hunan.sina.com.cn/news/s/2012-10-20/090221606.html"));
			// System.out.println(sina.substring(sina.indexOf("news_con_2") - 50, sina.indexOf("news_con_2") + 300));
			Elements contentEls = doc.select("span");
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				// System.out.println(ele.id());
				if (ele.id().trim().equals("news_con_2")) {
					System.out.println("find");
					System.out.println(ele.html());
				}
				/*Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					System.out.println(linkText);
					System.out.println(linkHref);
				}*/
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaSports()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("div#blk_sports_2"/*"span#news_con_2"*/);
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					System.out.println(linkText);
					System.out.println(linkHref);
				}
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaYuLe()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("div#ent_con_3"/*"span#news_con_2"*/);
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					System.out.println(linkText);
					System.out.println(linkHref);
				}
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaBlog()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("div#blog"/*"span#news_con_2"*/);
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					System.out.println(linkText);
					System.out.println(linkHref);
					testSinaBlogContent(linkHref);
				}
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaCaiJing()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("div#blk_finance_1"/*"span#news_con_2"*/);
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					// System.out.println(linkText);
					// System.out.println(linkHref);
					// testSinaBlogContent(linkHref);
					testSinaCaiJingContent(linkHref);
				}
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaHuNanTravel()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://www.sina.com.cn", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("div#blk_auto_1"/*"span#news_con_2"*/);
			// Elements childs = contentEls.select("div.ShCon2");
			System.out.println(contentEls.size());
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				Elements links = ele.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					System.out.println(linkText);
					System.out.println(linkHref);
					// testSinaBlogContent(linkHref);
					// testSinaCaiJingContent(linkHref);
				}
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaTechPage()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://blog.sina.com.cn/s/blog_6061bc970102eo67.html?tj=1", "utf-8");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("img"/*"span#news_con_2"*/);
			// Elements childs = contentEls.select("div.ShCon2");
			System.out.println(contentEls.size());
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				System.out.println(ele.outerHtml());
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaBlogImgPage()
	{
		try {
			/*String content = getCotentFromUrl("http://www.sina.com.cn");*/
			String content = getCotentFromUrl("http://blog.sina.com.cn/s/blog_6877ab470102edsg.html?tj=1?tj=2", "utf-8");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			Elements contentEls = doc.select("img"/*"span#news_con_2"*/);
			// Elements childs = contentEls.select("div.ShCon2");
			// System.out.println(contentEls.size());
			// System.out.println(contentEls.html());
			// System.out.println(contentEls.select("span#news_con_2").html());
			for (Element ele : contentEls) {
				String strImgSrc = ele.attr("src");
				String strImgRealSrc = ele.attr("real_src");
				// System.out.println(ele.outerHtml());
				System.out.println(strImgSrc);
				// System.out.println(strImgRealSrc);
				if (strImgRealSrc.length() > 0) {
					ele.attr("src", strImgRealSrc);
					System.out.println("after: " + ele.attr("src"));
				}
			}
		}finally {
			
		}
		System.out.println("end");
	}
	
	private void testSinaBlogContent(String linkRef) {
		try {
			String content = getCotentFromUrl(linkRef, "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			// Elements contentEls = doc.select(/*"div#tab01_con1"*/"div#sinashareto");
			Elements contentEls = doc.select("div#column_2");
			// contentEls.select("div#sinashareto").remove();
			if (contentEls.first() != null) {
				System.out.println("find!");
			} else {
				System.out.println("not find!");
			}
			/*Elements els = contentEls.select("div#sinashareto");
			if (els.first() != null) {
				System.out.println(contentEls.outerHtml());
			}
			contentEls.select("style").remove();
			contentEls.select("script").remove();
			System.out.println(contentEls.outerHtml());*/
		}finally {
			
		}
	}
	
	private void testSinaCaiJingContent(String linkRef) {
		try {
			String content = getCotentFromUrl(linkRef, "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			// Elements contentEls = doc.select(/*"div#tab01_con1"*/"div#sinashareto");
			// Elements contentEls = doc.select("div#artibody");
			Elements contentEls = doc.select("meta[http-equiv=Content-type]");
			// System.out.println(contentEls.size());
			// contentEls.select("div#sinashareto").remove();
			for (Element el : contentEls) {
				String charset = el.attr("content").trim();
				int charsetIndex = charset.indexOf("charset");
				String encode = "";
				if ( charsetIndex != -1) {
					int spaceIndex = charset.indexOf(' ', charsetIndex);
					if (spaceIndex != -1){
						encode = charset.substring(charsetIndex, spaceIndex);	
					}
					else {
						encode = charset.substring(charsetIndex);
					}
				}
				processCharset(encode);
			}
			/*Elements els = contentEls.select("div#sinashareto");
			if (els.first() != null) {
				System.out.println(contentEls.outerHtml());
			}
			contentEls.select("style").remove();
			contentEls.select("script").remove();
			System.out.println(contentEls.outerHtml());*/
		}finally {
			
		}
	}
	
	private void processCharset(String charset) {
		int equalIndex = charset.indexOf('=');
		String encode = "";
		if (equalIndex != -1) {
			encode = charset.substring(equalIndex+1);
		}
		
		if ( encode.toLowerCase().indexOf("gb") == -1 && encode != "gbk") {
			System.out.println(encode);
		}
	}
	
	private void testSinaNewsPageContent() {
		try {
			String content = getCotentFromUrl("http://tech.sina.com.cn/t/2012-10-10/09037688071.shtml", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			// Elements contentEls = doc.select(/*"div#tab01_con1"*/"div#sinashareto");
			Elements contentEls = doc.select("div.guessulike2");
			// contentEls.select("div#sinashareto").remove();
			System.out.println(contentEls.outerHtml());
			/*Elements els = contentEls.select("div#sinashareto");
			if (els.first() != null) {
				System.out.println(contentEls.outerHtml());
			}
			contentEls.select("style").remove();
			contentEls.select("script").remove();
			System.out.println(contentEls.outerHtml());*/
		}finally {
			
		}
	}
	
	private void testSinaYLPageContent() {
		try {
			String content = getCotentFromUrl("http://ent.sina.com.cn/s/m/2012-10-20/02593767997.shtml", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			// Elements contentEls = doc.select(/*"div#tab01_con1"*/"div#sinashareto");
			Elements contentEls = doc.select("div.blkContainerSblk");
			// contentEls.select("div#sinashareto").remove();
			System.out.println(contentEls.outerHtml());
			/*Elements els = contentEls.select("div#sinashareto");
			if (els.first() != null) {
				System.out.println(contentEls.outerHtml());
			}
			contentEls.select("style").remove();
			contentEls.select("script").remove();
			System.out.println(contentEls.outerHtml());*/
		}finally {
			
		}
	}
	
	private void testSinaPicContent()
	{
		try {
			String content = getCotentFromUrl("http://slide.news.sina.com.cn/c/slide_1_2841_27053.html", "gbk");
			org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
			// Elements contentEls = doc.select(/*"div#tab01_con1"*/"div#sinashareto");
			System.out.println(doc.html());
			Elements htmlContentEls = doc.select("div#d_picIntro");
			System.out.println(htmlContentEls.first().html());
			Elements contentEls = doc.select("div#eData");
			// contentEls.select("div#sinashareto").remove();
			// System.out.println(contentEls.outerHtml());
			Elements dls = contentEls.select("dl");
			for (Element dl : dls) {
				Elements dt = dl.select("dt");
				System.out.println(dt.first().html());
				Elements dds = dl.select("dd");
				System.out.println(dds.first().html());
				processPageImage(dds.first().html());
			}
			
		}finally {
			
		}
	}
	
	private void processPageImage(String url)
	{
		try {
			URL imgUrl = new URL(url);
			URLConnection connection = imgUrl.openConnection();
			connection.setConnectTimeout(100000);
			connection.setReadTimeout(100000);

			BufferedImage bufImage = ImageIO.read(connection
					.getInputStream());
			System.out.println("height:" + bufImage.getHeight());
			System.out.println("width:" + bufImage.getWidth());
			// media.setUrl(url);
		} catch (MalformedURLException e) {

			e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.out.println(url);
			
		}
	}
	
	private String getCotentFromUrl(String url, String charset) {
		// strBuf.delete(0, strBuf.length());
		String content = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams();
			HttpGet request = new HttpGet(url);
			System.out.println(request.getURI().getHost());
			// request.getParams()
			HttpResponse response = client.execute(request);
			byte[] bytes = EntityUtils.toByteArray(response.getEntity());
			content = new String(bytes, charset);
			/*// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				strBuf.append(line);
			}*/
		} catch(ClientProtocolException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch(IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return content;
	}
	
	StringBuilder strBuf = new StringBuilder();

}
