package hexun;
/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
*/

import common.HostConfig;
import common.HostConfig.*;
import common.HostConfigException;
import common.HttpHelper;
import dbTool.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.PatternSyntaxException;

// import org.jsoup.nodes.*;
// import org.jsoup.nodes.Element;

public class hexun implements Job{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		
		startSchedule();
		
		/*long startTime = System.currentTimeMillis();
		PropertyConfigurator.configure(hexun.class.getResource("/log4.properties"));
		// test.processCaiJing("http://news.hexun.com/");
		hexun test = new hexun();
		try {
			test.processFileXml("/flipboard.xml");
			test.setConfigInit(true);
		}catch (HostConfigException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ;
		}
		// test.processSpecialLink("http://funds.hexun.com/2012-09-29/146390810.html");
		test.startCrawl();
		
		// processCaiJing(strUrl);
		// processLink("http://bank.hexun.com/2012-07-11/143425349.html");
		// processLink("http://news.hexun.com/2012-07-24/143891628.html");
		// processLink("http://news.hexun.com/2012-07-24/143941196.html");
		long endTime = System.currentTimeMillis();
		System.out.println("program consume time:" + (endTime - startTime) + "ms");*/
		
		
		// getImageAttr("http://itv.hexun.com/lbi-html/ly/2012/20120101/neibuguanggao/021205/caijingrenwu/0502/250200.jpg");
		// getImageAttr("http://i1.sinaimg.cn/jc/focuspic/173/2009/0701/U1335P27T173D1F4433DT20120802091603.jpg");
		// test.testImage("http://itv.hexun.com/lbi-html/ly/2012/20120101/neibuguanggao/021205/caijingrenwu/0502/250200.jpg");
		
		
		/*
		PicParseService picSrv = new PicParseService();
		System.out.println(picSrv.getUrlPicture("http://itv.hexun.com/lbi-html/ly/2012/20120101/neibuguanggao/021205/caijingrenwu/0502/250200.jpg"));
		*/
		// System.out.println(File.separator);
		// System.out.println(getFileName("http://itv.hexun.com/lbi-html/ly/2012/20120101/neibuguanggao/021205/caijingrenwu/0502/250200.jpg"));
		
		
		
		// processLink("http://bank.hexun.com/2012-07-11/143425349.html");
		// testJsoup();
	}

	private static void startSchedule() {
		try {
			PropertyConfigurator.configure(hexun.class.getResource("/log4.properties"));
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			JobDetail job = JobBuilder.newJob(hexun.class)
					.withIdentity("hexun_news", "hexun").build();

			// Trigger the job to run now, and then repeat every 40 seconds
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("trigger_hexun", "group_tr_hx")
					.startNow()
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule()
									.withIntervalInHours(2).repeatForever())
					.build();

			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException
 {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();
		PropertyConfigurator.configure(hexun.class.getResource("/log4.properties"));
		// test.processCaiJing("http://news.hexun.com/");
		hexun test = new hexun();
		try {
			// test.processFileXml("/sina.xml");
			test.processFileXml("/flipboard.xml");
			// test.setConfigInit(true);
		}catch (HostConfigException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ;
		}catch(Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		// test.processSpecialLink("http://funds.hexun.com/2012-09-29/146390810.html");
		test.startCrawl();
		long endTime = System.currentTimeMillis();
		logger.info("program consume time:" + (endTime - startTime) + "ms");
	}

	
	public void startCrawl() {
		logger.info("start crawl.....");
		for (HostConfig item : hostConfigList) {
			try {
				curHostConfig = item;
				// strCharset = curHostConfig.getCharset();
				if (!saveHostToDb(item)) {
					logger.error("saveHostToDb failed! " + item.getHostDescription());
					continue;
				}
				logger.info(curHostConfig.getUrl());
				processHostUrl(curHostConfig.getUrl());
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				continue;
			}
			// test
			/*curNewsArea = item.getNewsAreaList().get(0);
			processSpecialLink("http://funds.hexun.com/2012-09-29/146390810.html");*/
			// test end
			// database file copy for debug
			// mysql must commented
			try {
				URL url = hexun.class.getResource(item.getDbFile());
				// for pc
				String dstFile = "E:\\php\\APMServ\\APMServ5.2.6\\www\\htdocs\\phptest\\" + item.getDbFile();
				// for notebook
				// String dstFile = "E:\\tools\\php\\APMServ5.2.6\\APMServ5.2.6\\www\\htdocs\\page\\phptest\\" + item.getDbFile();
				copyDb(url.getFile(), dstFile);
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			
		}
		logger.info("crawl end....");
	}
	
	private boolean saveHostToDb(HostConfig config) {
		boolean isOK = true;
		if (curApp == null)
			curApp = new Application();
		curApp.setDbFile(config.getDbFile());
		curApp.setDecription(config.getHostDescription());
		try {
			curApp.saveApplication();
		} catch (SqliteException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isOK = false;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isOK = false;
		}
		return isOK;
	}
	
	private boolean saveContentAreaToDb(ContentArea item) {
		boolean isOK = true;
		if (curPart == null)
			curPart = new PartManager();
		curPart.setTitle(item.getTitle());
		curPart.setNovelId(curApp.getID());
		curPart.setDbFile(curHostConfig.getDbFile());
		try {
			curPart.savePart();
		} catch (SqliteException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isOK = false;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isOK = false;
		}
		return isOK;
	}
	
	
	private boolean processHostUrl(String url) {

		logger.info("start crawl: " + url);
		String strWebContent = null;
		boolean isOK = true;
		try {
			strWebContent = getContentFromUrl(url, curHostConfig.getCharset());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isOK = false;
			return isOK;

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isOK = false;
			return isOK;
		}
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
		 */
		org.jsoup.nodes.Document doc = Jsoup.parse(strWebContent, baseUrl);
		// for (String tag : tagList) {
		for (NewsArea newsArea : curHostConfig.getNewsAreaList()) {
			// Elements caijing_news = doc.getElementsByClass("cjywMain");
			// Elements caijing_news = doc.select("div.cjywMain");
			// Elements caijing_news = doc.select(strBegin);
			curNewsArea = newsArea;
			for (ContentArea contentArea : curNewsArea.getContentSelectorList()) {
				curContentArea = contentArea;
				strCharset = contentArea.getPageCharset();
				if (!saveContentAreaToDb(contentArea)) {
					logger.error("saveContentAreaToDb failed! " + contentArea.getTitle());
					continue;
				}
				String tag = curContentArea.getContentSelector();
				logger.info("process news area tag:" + tag);
				Elements contentEles = doc.select(tag);
				// System.out.println(caijing_news.html());
				// String pattern = "^\\[(.+?)\\]";
				for (org.jsoup.nodes.Element ele : contentEles) {
					// Elements links = caijing.getElementsByTag("a");
					
					if (curContentArea.getMustHaveChild().length() > 0 ) {
						logger.info(curContentArea.getMustHaveChild());
						Elements childEles = ele.select(curContentArea.getMustHaveChild());
						if (childEles.first() == null) continue;
					}
					Elements links = ele.getElementsByTag(curNewsArea.getListTagPattern());
					// System.out.println(links.html());

					for (org.jsoup.nodes.Element link : links) {
						
						String linkHref = link.attr(curNewsArea.getNewsUrlPattern()).trim();
						String linkText = link.text().trim();
						if (linkText.length() == 0) {
							continue;
						}
						// for debug
						
						/*String linkHref = "http://jnoc.blog.hexun.com/80678372_d.html";
						String linkText = "中日旅游战中谁的损失最大";*/
						//debug end
						

						try {
							// if (!linkText.matches(pattern))
							// if (!linkText.contains("["))
							// logger.info(linkHref.matches(curNewsArea.getUrlLinkReg()));
							if (linkHref.matches(curNewsArea.getUrlLinkReg())
									&& ( ( curNewsArea.getExcludePattern().length() > 0 ? !linkText.matches(curNewsArea.getExcludePattern()) : true )) ) {
								// System.out.println("href=" + linkHref);
								// System.out.println("title=" + linkText);
								// logger.info("process sina....");
								if (Article.isContentExist(linkHref,curHostConfig.getDbFile())) {
									continue;
								}
								
								curArticle = new Article();
								curArticle.setTitle(linkText);
								curArticle.setOrgUrl(linkHref);
								curArticle.SetArtTime(getCrawlTime());
								curArticle.setArtSource(curHostConfig.getHostDescription());
								/*
								 * curArticle.setTime(HttpHelper
								 * .getHtmlPageTime(linkHref));
								 */
								logger.info(tag + ":" + linkHref);
								try {
									if (!processLink(linkHref)) {
										logger.error("process:" + linkHref
												+ " failed");
										// break;
										continue;
									}
								} catch (Exception e) {
									e.printStackTrace();
									logger.error(e.getMessage());
									continue;
									
								}
								
								// contList.add(curArticle);
								if (curArticle.containsText()) {
									curArticle.setHtml(text.toString());
								}
								curArticle.setDbFile(curHostConfig.getDbFile());
								curArticle.setSeasonId(curPart.getId());
								curArticle.saveArticle();
								// debug comment end
								// return; // for debug

							}
						} catch (PatternSyntaxException e) {
							e.printStackTrace();
							logger.error(e.getMessage());
							continue;
						} catch (SqliteException e) {
							e.printStackTrace();
							logger.error(e.getMessage());
							continue;
						} catch (SQLException e) {
							e.printStackTrace();
							logger.error(e.getMessage());
							continue;
						}
					}

				}
			}
		}

		logger.info("crawl: " + url + " end");
		return isOK;
	}
	
	private Boolean processLink(String url) {
		// Document doc = Jsoup.connect("http://www.hexun.com/").get();
		boolean isCorrect = false;
		try {
			if (text.length() > 0) {
				text.delete(0,text.length());
				// curCont.setHtml(text.toString());
			}
			
			if (relItemBuf_WithinNewsContTag.length() > 0) {
				relItemBuf_WithinNewsContTag.delete(0, relItemBuf_WithinNewsContTag.length());
			}
			
			if (relItemBuf_ExternalNewsContTag.length() > 0 ) {
				relItemBuf_ExternalNewsContTag.delete(0, relItemBuf_ExternalNewsContTag.length());
			}
		}catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isCorrect;
		}
		// curCont = new Content();
		// contList.add(curCont);
		String strWebContent = null;
		try {
			strWebContent = getContentFromUrl(url, curContentArea.getPageCharset());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isCorrect;

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isCorrect;
		}
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
		 */
		org.jsoup.nodes.Document doc = Jsoup.parse(strWebContent, baseUrl);
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
		 */
		// url redirect
		org.jsoup.nodes.Element head = doc.head();
		String strRedirectUrl = HttpHelper.processUrlRedirect(head);
		if ((strRedirectUrl.length() > 0)
				&& (!url.equalsIgnoreCase(strRedirectUrl)) && (strRedirectUrl.indexOf("http") != -1)) {
			url = strRedirectUrl;
			logger.info("redirect:" + url);
			try {
				strWebContent = getContentFromUrl(url, curContentArea.getPageCharset());
				doc = Jsoup.parse(strWebContent, baseUrl);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;

			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			}
		}
		// Elements els = doc.select("div#artibody");
		// process page charset
		try {
			strWebContent = processDocCharset(doc, curContentArea.getPageCharset(), url);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isCorrect;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isCorrect;
		}
		if (strWebContent.length() > 0 ) {
			doc = Jsoup.parse(strWebContent, baseUrl);
		}
		
		Elements newsContentEls = null;
		for (NewsPageProcess newsPageItem : curNewsArea.getPageProcessList() ) {
			curNewsPageItem = newsPageItem;
			// System.out.println("NewsContentTag: " + curNewsPageItem.getNewsContentTag());
			newsContentEls = doc.select(curNewsPageItem.getNewsContentTag());
			if (newsContentEls.first() != null) {
				logger.info("Found NewsContentTag: " + curNewsPageItem.getNewsContentTag());
				break;
			}
		}

		if (newsContentEls.first() != null) { // 正常网页处理
			pageList = curNewsArea.processHtmlPaging(doc, strCharset);
			processHtmlImgAttr(doc);
			// 元素替换处理
			for (ConfigItem eleRepItem : curNewsPageItem.getElementReplaceList()){
				// logger.info(eleRepItem.getFirst());
				// logger.info(els.select(eleRepItem.getFirst()).outerHtml());
				/*if (elementsRepMap.get(strRepKey).length() > 0 )*/ 
				if (eleRepItem.getSecond().length() > 0 ){
					Elements reps = newsContentEls.select(/*strRepKey*/eleRepItem.getFirst());
					for (org.jsoup.nodes.Element item : reps) {
						item.text(/*elementsRepMap.get(strRepKey)*/eleRepItem.getSecond());
					}
				}
				else {
					newsContentEls.select(/*strRepKey*/eleRepItem.getFirst()).remove();
				}
			}
			// 新闻相关处理
			for (RelatedItem relItem : curNewsPageItem.getRelatedItemList()) {
				// for (String strItem : relItemList) {
				curRelatedItem = relItem;
				String strItem = relItem.getRelatedPattern();
				Elements relElements = null;
				if (relItem.getRelatedItemsType().equals("1")) // 网页内容内部
					relElements = newsContentEls.select(strItem);
				else
					relElements = doc.select(strItem);

				org.jsoup.nodes.Element first = relElements.first();
				if (relElements.first() != null) {
					Elements hrefs = first.getElementsByTag("a");
					if (!hrefs.isEmpty()) {
						// logger.info(first.outerHtml());
						if (curRelatedItem.getRelatedItemsType().equals("1")) {
							// relItemBuf_WithinNewsContTag.append("<br/>");
							relItemBuf_WithinNewsContTag.append(first.outerHtml());
						}
						else {
							relItemBuf_ExternalNewsContTag.append("<br/>");
							relItemBuf_ExternalNewsContTag.append(first.outerHtml());
						}
						first.text("");

						// System.out.println(relItems.first().nextElementSibling().outerHtml());
					} else {
						if (curRelatedItem.getRelatedItemsType().equals("1")) {
							// relItemBuf_WithinNewsContTag.append("<br/>");
							relItemBuf_WithinNewsContTag.append(first.outerHtml());
						}
						else {
							relItemBuf_ExternalNewsContTag.append("<br/>");
							relItemBuf_ExternalNewsContTag.append(first.outerHtml());
						}
						first.text("");
						processNewsRelItem(first.nextElementSibling());
					}
				}

				// }
			}
			// System.out.println(p_s.outerHtml());
			text.append(getContent(newsContentEls));
			// process image
			// getAbstract(els);
			
			try {
				processPageImage(newsContentEls);
				processMorePages(doc, url);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			}
			text.append(relItemBuf_WithinNewsContTag.toString());
			text.append(relItemBuf_ExternalNewsContTag.toString());
			isCorrect = true;
			// for debug
			// curCont.setHtml(text.toString());
			// for debug end

		} else if (curNewsArea.processPicHtml(doc, curArticle)/*processPictureHtml(doc)*/) { // 图片网页处理
			logger.info("full picture html:" + url);
			isCorrect = true;
		} else {
			isCorrect = false;
		}

		// System.out.println(text.toString());
		// System.out.println(relItemBuf.toString());
		return isCorrect;
	}
	
	// @SuppressWarnings("finally")
	private String processDocCharset(org.jsoup.nodes.Document doc, String orgCharset, String url) 
			throws ClientProtocolException, IOException {
		Elements metaEls = doc.select("meta[http-equiv=Content-type]");
		String webContent = "";
		for (org.jsoup.nodes.Element el : metaEls) {
			String charset = el.attr("content").trim();
			String encode = getCharset(charset);
			if (encode.length() > 0) {
				if ( !encode.equals(orgCharset) ) {
					strCharset = encode;
					try {
						// logger.info("process charset:" + encode);
						webContent = getContentFromUrl(url, encode);
						// doc = Jsoup.parse(webContent, baseUrl);
						break;
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						/*e.printStackTrace();
						logger.error(e.getMessage());*/
						webContent = "";
						// logger.error("process url: " + url + "occurs ClientProtocolException");
						throw e;
						// return false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						/*e.printStackTrace();
						logger.error(e.getMessage());*/
						// logger.error("process url: " + url + "occurs IOException");
						webContent = "";
						throw e;
						// return false;
					}
				}
			}
			
		}
		return webContent;
	}
	
	private String getCharset(String content) {
		// String charset = "";
		int charsetIndex = content.indexOf("charset");
		String encode = "";
		if ( charsetIndex != -1) {
			int equalIndex = content.indexOf('=', charsetIndex);
			if ( equalIndex != -1) {
				int spaceIndex = content.indexOf(' ', equalIndex);
				if (spaceIndex != -1){
					encode = content.substring(equalIndex + 1, spaceIndex);	
				}
				else {
					encode = content.substring(equalIndex + 1);
				}
			}
		}
		return encode;
	}
	
	// 网页相关处理
	private void processNewsRelItem(org.jsoup.nodes.Element el) {
		if (el == null)
			return;
		Elements hrefs = el.getElementsByTag("a");
		if (hrefs.isEmpty() && (!curRelatedItem.isIgnoreTag(el.tagName())))
			return;
		else {
			if (curRelatedItem.getRelatedItemsType().equals("1")) {
				// relItemBuf_WithinNewsContTag.append("<br/>");
				relItemBuf_WithinNewsContTag.append(el.outerHtml());
			}
			else {
				relItemBuf_ExternalNewsContTag.append("<br/>");
				relItemBuf_ExternalNewsContTag.append(el.outerHtml());
			}
			el.text("");
			processNewsRelItem(el.nextElementSibling());
		}
	}
	
	private void clearNewsRelItem(org.jsoup.nodes.Element el) {
		if (el == null) 
		{
			return;
		}
		Elements hrefs = el.getElementsByTag("a");
		if (hrefs.isEmpty() && (!curRelatedItem.isIgnoreTag(el.tagName() ) ) )
			return;
		else {
			// relItemBuf.append("<br/>");
			// relItemBuf.append(el.outerHtml());
			el.text("");
			clearNewsRelItem(el.nextElementSibling());
		}
	}
	// 网页相关处理--end
	
	// get content from elements
	private String getContent(Elements els) {
		/*if (strRelItemType.equals("1"))*/
		// logger.info(els.html());
		for (RelatedItem relItem: curNewsPageItem.getRelatedItemList() ){
			/*for (String strItem : relItemList)*/
			curRelatedItem = relItem;
			if (relItem.getRelatedItemsType().equals("1")){
				Elements relItems = els.select(relItem.getRelatedPattern());
				for (org.jsoup.nodes.Element item : relItems )
				{
					Elements hrefs = item.getElementsByTag("a");
					if (!hrefs.isEmpty())
						item.text("");
					else
					{
						item.text("");
						if (item.nextElementSibling() != null) {
							clearNewsRelItem(item.nextElementSibling());
						}
					}
				}
				
			}
		}
		
		// elements replace
		/*for ( String strRepKey : elementsRepMap.keySet() )*/
		for (ConfigItem eleRepItem : curNewsPageItem.getElementReplaceList()){
			// logger.info(eleRepItem.getFirst());
			// logger.info(els.select(eleRepItem.getFirst()).outerHtml());
			/*if (elementsRepMap.get(strRepKey).length() > 0 )*/ 
			if (eleRepItem.getSecond().length() > 0 ){
				Elements reps = els.select(/*strRepKey*/eleRepItem.getFirst());
				for (org.jsoup.nodes.Element item : reps) {
					item.text(/*elementsRepMap.get(strRepKey)*/eleRepItem.getSecond());
				}
			}
			else {
				els.select(/*strRepKey*/eleRepItem.getFirst()).remove();
			}
		}
		// logger.info(els.outerHtml());
		String contents = els.outerHtml();
		// logger.info(contents);
		/*
		contents = contents.replaceAll("<script(?:[^<]++|<(?!/script>))*+</script>", "");
		while(contents.contains("</script>")){
			contents = contents.replaceAll("<script(?:[^<]++|<(?!/script>))*+</script>", "");
		}
		contents = contents.replaceAll("<select(?:[^<]++|<(?!/select>))*+</select>", "");
		*/
		/*for ( String strReplace : contentRepMap.keySet() )*/ 
		for (ConfigItem contentRepItem : curNewsPageItem.getContentReplaceList()){
			// logger.info(contentRepItem.getFirst());
			contents = contents.replaceAll(contentRepItem.getFirst(), contentRepItem.getSecond());
			// nested </script> process
			if ( contentRepItem.getFirst().contains("</script>") ) {
				while( contents.contains("</script>") ) {
					contents = contents.replaceAll(contentRepItem.getFirst(), contentRepItem.getSecond());
				}
			}
		}
		return contents;
	}
	
	// get abstact from html page
	@Deprecated
	private void getAbstract(Elements els) {
		// Elements p_s = els.select("p+p+p");
		Elements p_s = els.select(/*strAbstractPattern*/curNewsPageItem.getAbstractPattern());
		if ( p_s.first() != null ) {
			curArticle.setAbstract(p_s.first().text());
		}
		else {
			curArticle.setAbstract(curArticle.getTitle());
		}
	}
		
	// private void processMorePages(Elements els, String orgUrl) {
	private void processMorePages(org.jsoup.nodes.Document doc, String orgUrl) 
			throws ClientProtocolException, IOException {
		
		// List<String> pageList = curNewsArea.processHtmlPaging(doc, strCharset);
		if (pageList != null) {
			for (String url : pageList) {
				if ((url.length() > 0)
						&& (!url.equalsIgnoreCase(orgUrl))) {
					try {
						getPageContents(url);
					} catch (ClientProtocolException e) {
						throw e;
					} catch (IOException e) {
						throw e;
					}
				}
			}
		}
	}
	
	private boolean getPageContents(String url) throws ClientProtocolException, IOException {
		boolean isRight = true;

		String strWebContent = null;
		try {
			strWebContent = getContentFromUrl(url, curContentArea.getPageCharset());
		} catch (ClientProtocolException e) {
			/*e.printStackTrace();
			logger.error(e.getMessage());*/
			// logger.error("process url: " + url + "occurs ClientProtocolException");
			isRight = false;
			throw e;

		} catch (IOException e) {
			/*e.printStackTrace();
			logger.error(e.getMessage());*/
			isRight = false;
			// logger.error("process url: " + url + "occurs IOException");
			throw e;
		}
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
		 */
		org.jsoup.nodes.Document doc = Jsoup.parse(strWebContent, baseUrl);
		// process charset of html
		try { 
			strWebContent = processDocCharset(doc, curContentArea.getPageCharset(), url);
		} catch(ClientProtocolException e) {
			isRight = false;
			throw e;
		} catch(IOException e) {
			isRight = false;
			throw e;
		}
		if (strWebContent.length() > 0 ) {
			doc = Jsoup.parse(strWebContent, baseUrl);
		}
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(timeOutcurHostConfig.getTimeOut()).get();
		 */

		Elements els = doc.select(curNewsPageItem.getNewsContentTag());
		text.append(getContent(els));
		// images
		processPageImage(els);

		return isRight;
	}
		
	private void processPageImage(Elements els)	throws MalformedURLException,
		IOException {
		Elements images = els.select("img");
		for (org.jsoup.nodes.Element image : images) {
			String url = image.absUrl("src");
			
			logger.info("Img url: " + url);
			if (url.length() <= 0)
				continue;
			// logger.info(curArticle.getOrgUrl());
			// testImage(url);
			try {
				URL imgUrl = new URL(url);
				URLConnection connection = imgUrl.openConnection();
				connection.setConnectTimeout(curHostConfig.getTimeOut());
				connection.setReadTimeout(curHostConfig.getTimeOut());

				BufferedImage bufImage = ImageIO.read(connection.getInputStream());
				Resource media = new Resource();
				media.setDbFile(curHostConfig.getDbFile());
				media.setHeitht(bufImage.getHeight());
				media.setWidth(bufImage.getWidth());
				// media.setUrl(url);
				media.setResContent(url);
				media.setResType(Resource.IMGURL);
				// switch (curArticle.getType()) {
				// case Article.HTML:
				if (curArticle.containsText()) {
					String strAlt = image.attr("alt");
					if (strAlt.length() > 0) {
						// media.setTitle(strAlt);
						media.setResText(strAlt);
					} else {
						// media.setTitle(curArticle.getTitle());
						media.setResText(curArticle.getTitle());
					}
				}
					
				// case Article.PICTURE:
				/*if (curArticle.getArcType() == Article.ALLRESOURCE) {
					Elements subTitles = els.select("div.slide_subtitle"curNewsArea.getPicHandler().getPicSlide());
					if (subTitles.first() != null
							&& (subTitles.first().text().length() > 0)) {
						media.setTitle(subTitles.first().html());
						media.setResText(subTitles.first().html());
					} else {
						media.setTitle(curArticle.getTitle());
						String strAlt = image.attr("alt");
						if (strAlt.length() > 0) {
							// media.setTitle(strAlt);
							media.setResText(strAlt);
						} else {
							// media.setTitle(curArticle.getTitle());
							media.setResText(curArticle.getTitle());
						}
						// media.setResText(curArticle.getTitle());
					}
				}*/
				// default:
					// break;
				// }
				
				curArticle.addMedia(media);
				// System.out.println("height: " + bufImage.getHeight());
				// System.out.println("width: " + bufImage.getWidth());

			} catch (MalformedURLException e) {
				
				/*e.printStackTrace();
				logger.error(e.getMessage());
				continue;*/
				logger.error("process url: " + url + " occur MalformedURLException");
				throw e;
			} catch (IOException e) {
				/*e.printStackTrace();
				logger.error(e.getMessage());*/
				logger.error("process url:" + url + " occur IOExcption");
				throw e;
				// continue;
			}
		}
	}
	// image end
	
	private void processHtmlImgAttr(org.jsoup.nodes.Document doc) {
		Elements imgEls = doc.select("img"/*"span#news_con_2"*/);
		// Elements childs = contentEls.select("div.ShCon2");
		// System.out.println(contentEls.size());
		// System.out.println(contentEls.html());
		// System.out.println(contentEls.select("span#news_con_2").html());
		for (org.jsoup.nodes.Element ele : imgEls) {
			// String strImgSrc = ele.attr("src");
			String strImgRealSrc = ele.attr("real_src");
			// System.out.println(ele.outerHtml());
			// System.out.println(strImgSrc);
			// System.out.println(strImgRealSrc);
			if (strImgRealSrc.length() > 0) {
				ele.attr("src", strImgRealSrc);
				// System.out.println("after: " + ele.attr("src"));
			}
		}
	}
	
	private String getFileName(String url) {
		int last = url.indexOf("//");
		if (last == -1)
			return "";
		String subUrl = url.substring(last + 2);
		String[] urlArr = subUrl.split("/");
		String path = join(urlArr, File.separator);
		path = "e:" +  path;
		return path;
	}
	
	public String join(AbstractCollection<String> s, String delimiter) {
	    if (s == null || s.isEmpty()) return "";
	    Iterator<String> iter = s.iterator();
	    StringBuilder builder = new StringBuilder(iter.next());
	    while( iter.hasNext() )
	    {
	        builder.append(delimiter).append(iter.next());
	    }
	    return builder.toString();
	}
	
	public String join(String[] s, String delimiter) {
	    if (s.length == 0) return "";
	    // Iterator<String> iter = s.iterator();
	    StringBuilder builder = new StringBuilder();
	    // while( iter.hasNext() )
	    for (String str : s)
	    {
	        builder.append(delimiter).append(str);
	    }
	    return builder.toString();
	}

	
	// all crawl string
	private StringBuilder text = new StringBuilder();
	// related item for news -- within newsContentTag
	private StringBuilder relItemBuf_WithinNewsContTag = new StringBuilder();
	// related item for news -- external newsContentTag
	private StringBuilder relItemBuf_ExternalNewsContTag = new StringBuilder();
	// base url
	private String baseUrl = null;
	
	// maven
	// @requiresDependencyResolution runtime
	// private MavenProject project;
	// maven end
	
	// xml config
	
	private List<HostConfig> hostConfigList = null;
	private HostConfig curHostConfig = null;
	private NewsArea curNewsArea = null;
	private ContentArea curContentArea = null;
	private RelatedItem curRelatedItem = null;
	private NewsPageProcess curNewsPageItem = null;
	private String strCharset = null;
	// more page
	private List<String> pageList = null;
	
	// static members
	
	/*
		// hosts
	private  String strDescription = null;
	private  String strUrl = null;
	private  String strType = null;
	private  String strGetsMethod = null;
	private  int timeOut = 0;
			// newsList
	private String strBegin = null;
	private String[] tagList = null;
	private String strExcludePattern = null;
	private String strListTagPattern = null;
	private String strNewsUrlPattern = null;
	private String strNewsTitlePattern = null;
	private String strUrlLinkReg = null;
			// newsList end
			
			// newsContent
	private String strNewsContentTag = null;
	private String strRelItemType = null;
	private String strAbstractPattern = null;
	private HashMap<String, String> contentRepMap = null;
	private HashMap<String, String> elementsRepMap = null;
	private List<String> relItemList = null;
				// pages
	private String strSelectPattern = null;
	private String strUrlValue = null;
				// pages end
			// newsContent end
	
			// pictureProcess
	private String strPicContentTag = null;
	private String strPicAbstractPattern = null;
	private String strPicSlide = null;
	private String strPicCountMax = null;
	private String strPicCount = null;
	private int picCountIndex = 0;
	private String strFirstPicCountLetter = null;
	private String strSecondPicCountLetter = null;
	private String strNextPicturePattern = null;
	private String strNextPictureAttr = null;
	        // pictureProcess end
		// hosts end
	// xm config end
	 */
	boolean configInit = false;
	public void setConfigInit(boolean init) { configInit = init; }
	public boolean getConfigInit() { return configInit; }
	// sqlite db interface 
	// private List<Article> contList = new LinkedList<Article>();
	
	private Application curApp = null;
	private PartManager curPart = null;
	private Article curArticle = null;
	// sqlite db interface end
	
	//log
	static Logger logger = Logger.getLogger("newedu.webcrawl.hexun"/*hexun.class.getName()*/);
	
	
	
	private void processFileXml(String strXmlFile) throws HostConfigException {
		if (configInit) return;
		URL url = this.getClass().getResource(strXmlFile/*"/flipboard.xml"*/);
		String fileName = url.getFile();
		logger.info(fileName);
		// System.out.println(fileName);
		File file = new File(fileName);
		try {
			SAXReader saxReader = new SAXReader();
			// test git
			// is ok
			Document document = saxReader.read(file);
			List<?> list = document.selectNodes("//flipboard/hosts" );
        	Iterator<?> iter = list.iterator();
        	while (iter.hasNext()) {
        		Element host = (Element)iter.next();
        		HostConfig config = new HostConfig();
        		try {
        			config.processHostNode(host);
        		} catch(HostConfigException e) {
        			logger.error(e.getMessage());
        			throw e;
        		}
        		if (hostConfigList == null) {
        			hostConfigList = new ArrayList<HostConfig>();
        		}
        		hostConfigList.add(config);
        		// System.out.println(host.getTextTrim());
        	}
        	// configInit = true;
			
		}catch(DocumentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Deprecated
	private Boolean processSpecialLink(String url) {
		// Document doc = Jsoup.connect("http://www.hexun.com/").get();
		curArticle = new Article();
		curArticle.setOrgUrl(url);
		boolean isCorrect = true;
		try {
			if (text.length() > 0) {
				text.delete(0, text.length());
				// curCont.setHtml(text.toString());
			}
			
			if (relItemBuf_WithinNewsContTag.length() > 0) {
				relItemBuf_WithinNewsContTag.delete(0, relItemBuf_WithinNewsContTag.length());
			}
			
			if (relItemBuf_ExternalNewsContTag.length() > 0) {
				relItemBuf_ExternalNewsContTag.delete(0, relItemBuf_ExternalNewsContTag.length());
			}
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isCorrect = false;
			return isCorrect;
		}
		// curCont = new Content();
		// contList.add(curCont);

		String strWebContent = null;
		try {
			strWebContent = getContentFromUrl(url, "GBK");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isCorrect = false;
			return isCorrect;

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			isCorrect = false;
			return isCorrect;
		}
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
		 */
		org.jsoup.nodes.Document doc = Jsoup.parse(strWebContent, baseUrl);
		/*
		 * org.jsoup.nodes.Document doc =
		 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
		 */
		// for debug
		org.jsoup.nodes.Element head = doc.head();
		curArticle.setTitle(doc.title());
		String strRedirectUrl = HttpHelper.processUrlRedirect(head);
		if ((strRedirectUrl.length() > 0)
				&& (!url.equalsIgnoreCase(strRedirectUrl))) {
			// url = strRedirectUrl;
			try {
				strWebContent = getContentFromUrl(url, "GBK");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				isCorrect = false;
				return isCorrect;

			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				isCorrect = false;
				return isCorrect;
			}
			/*
			 * org.jsoup.nodes.Document doc =
			 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
			 */
			doc = Jsoup.parse(strWebContent, baseUrl);
			/*
			 * doc =
			 * Jsoup.connect(strRedirectUrl).timeout(curHostConfig.getTimeOut
			 * ()).get();
			 */
			curArticle.setTitle(doc.title());
		}

		Elements newsPageContEls = null;
		for (NewsPageProcess item : curNewsArea.getPageProcessList() )
		{
			curNewsPageItem = item;
			newsPageContEls = doc.select(curNewsPageItem.getNewsContentTag());
			if (newsPageContEls.first() != null)
				break;
		}

		// releted news process
		if (newsPageContEls.first() != null) {
			// 新闻相关处理
			for (RelatedItem relItem : curNewsPageItem.getRelatedItemList()) {
				// for (String strItem : relItemList) {
				curRelatedItem = relItem;
				String strItem = relItem.getRelatedPattern();
				Elements relItems = null;
				if (relItem.getRelatedItemsType().equals("1")) // 网页内容内部
					relItems = newsPageContEls.select(strItem);
				else
					relItems = doc.select(strItem);

				org.jsoup.nodes.Element first = relItems.first();
				if (relItems.first() != null) {
					Elements hrefs = first.getElementsByTag("a");
					if (!hrefs.isEmpty()) {
						logger.info(first.outerHtml());
						if (curRelatedItem.getRelatedItemsType().equals("1")) {
							// relItemBuf_WithinNewsContTag.append("<br/>");
							relItemBuf_WithinNewsContTag.append(first.outerHtml());
						}
						else {
							relItemBuf_ExternalNewsContTag.append("<br/>");
							relItemBuf_ExternalNewsContTag.append(first.outerHtml());
						}
						first.text("");

						// System.out.println(relItems.first().nextElementSibling().outerHtml());
					} else {
						if (curRelatedItem.getRelatedItemsType().equals("1")) {
							// relItemBuf_WithinNewsContTag.append("<br/>");
							relItemBuf_WithinNewsContTag.append(first.outerHtml());
						}
						else {
							relItemBuf_ExternalNewsContTag.append("<br/>");
							relItemBuf_ExternalNewsContTag.append(first.outerHtml());
						}
						first.text("");
						processNewsRelItem(first.nextElementSibling());
					}
				}

			}
			// System.out.println(p_s.outerHtml());
			text.append(getContent(newsPageContEls));
			// process image
			// getAbstract(els);
			
			try {
				processPageImage(newsPageContEls);
				processMorePages(doc, url);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			} catch (MalformedURLException e){
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return isCorrect;
			}
			text.append(relItemBuf_WithinNewsContTag.toString());
			text.append(relItemBuf_ExternalNewsContTag.toString());
			isCorrect = true;
			// for debug
			curArticle.setHtml(text.toString());
			// for debug end
			/* curArticle.saveContent(); */
			curArticle.setDbFile(curHostConfig.getDbFile());
			try {
				curArticle.saveArticle();
			} catch (SqliteException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				isCorrect = false;
				return isCorrect;
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				isCorrect = false;
				return isCorrect;
			}
		} else if (curNewsArea.processPicHtml(doc, curArticle)) {
			logger.info("full picture html:" + url);
			isCorrect = true;
		} else {
			isCorrect = false;
		}

		// System.out.println(text.toString());
		// System.out.println(relItemBuf.toString());

		return isCorrect;
	}
	
	private String getContentFromUrl(String url, String strCharset) throws ClientProtocolException, IOException {
		String content = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams();
			HttpGet request = new HttpGet(url);
			request.setHeader("User-Agent", "Mozilla/5.0 (SymbianOS/9.1; U; en-us) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
			baseUrl = request.getURI().getHost();
			// request.getParams()
			HttpResponse response = client.execute(request);
			byte[] bytes = EntityUtils.toByteArray(response.getEntity());
			content = new String(bytes, strCharset);
		} catch(ClientProtocolException e) {
			// e.printStackTrace();
			logger.error("process URL:" + url + "failed! " + e.getMessage());
			throw e;
			
		} catch(IOException e) {
			// e.printStackTrace();
			logger.error("process URL:" + url + "failed! " + e.getMessage());
			throw e;
		}
		return content;
	}
	
	// test function
	private void copyDb(String oriDb,String dstDb)
	{
		File oriFile= new File(oriDb);
		if (!oriFile.exists()) {
			System.err.println("file:"+ oriDb + " not exists!");
			return;
		}
		File dstFile = new File(dstDb);
		if(dstFile.exists()) {
			if(!dstFile.delete()) {
				System.err.println("delete file:" + dstDb + " failed!");
				return;
			}
		}
		try {
			if (!dstFile.createNewFile()) {
				System.err.println("create file:" + dstDb + " failed!");
				return;
			}
		} catch(IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return;
		}
		
		InputStream inStream = null;
		OutputStream outStream = null;
		
		try {
			inStream = new FileInputStream(oriFile);
			outStream = new FileOutputStream(dstFile);
			byte[] buffer = new byte[1024];
			int length;
		// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}finally {
			try {
				if (inStream != null)
					inStream.close();
				if (outStream != null) {
					outStream.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		
	}
	
	private String getCrawlTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return formatter.format(date);
	}

}

