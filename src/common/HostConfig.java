package common;

import hexun.hexun;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

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

import dbTool.Article;
import dbTool.Resource;

public class HostConfig {
	static Logger logger = Logger.getLogger("newedu.webcrawl.hexun"/*HostConfig.class.getName()*/);
	// hosts attribute
	private String strDescription = null;
	public String getHostDescription() { return strDescription;}
	
	private String strUrl = null;
	public String getUrl() { return strUrl; }
	
	private String charset = null;
	public String getCharset() { return charset; }
	
	private String strGetsMethod = null;
	public String getMethod() { return strGetsMethod; }
	
	
	private String strDbFile = null;
	public String getDbFile() { return strDbFile; }
	private int timeOut = 0;
	public int getTimeOut() { return timeOut; }
	
	public class ConfigItem {
		private String first = null ;
		public void setFirst(String first) { this.first = first; }
		public String getFirst() { return first ; }
		
		private String second = null ;
		public void setSecond(String second) { this.second = second; }
		public String getSecond() { return second; }
	}
	
	public class RelatedItem {
		private String relatedPattern = null;
		public String getRelatedPattern() { return relatedPattern; }
		public void setRelatedPattern(String pattern) {
			relatedPattern = pattern;
		}
		
		private String relatedItemsType = null;
		public String getRelatedItemsType() { return relatedItemsType;}
		public void setRelatedItemType(String type) {
			relatedItemsType = type;
		}
		
		private String ignoreTags = null;
		public String getIgnoreTags() { return ignoreTags; }
		public void setIgnoreTags(String tag) {
			ignoreTags = tag;
		}
		public boolean isIgnoreTag(String tag) {
			if (ignoreTags.indexOf(tag) != -1 ) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public class ContentArea {
		private String contentSelector = null ;
		public void setContentSelector(String selector) { contentSelector = selector; }
		public String getContentSelector() { return contentSelector ; }
		
		private String selectorTitle = null ;
		public void setTitle(String title) { selectorTitle = title ; }
		public String getTitle() { return selectorTitle; }
		
		private String selectorType = null ;
		public String getType() { return selectorType ; }
		public void setType(String type) { selectorType = type ;}
		
		private	String mustHaveChild = null;
		public String getMustHaveChild() { return mustHaveChild;}
		public void setMustHaveChild(String child) { mustHaveChild = child; }
		
	}
	
	public class NewsPageProcess {
		// newsContent
		private String strNewsContentTag = null;

		public String getNewsContentTag() {
			return strNewsContentTag;
		}

		private String strAbstractPattern = null;

		public String getAbstractPattern() {
			return strAbstractPattern;
		}

		private List<ConfigItem> elementReplaceList = null;

		public List<ConfigItem> getElementReplaceList() {
			return elementReplaceList;
		}

		private List<ConfigItem> contentReplaceList = null;

		public List<ConfigItem> getContentReplaceList() {
			return contentReplaceList;
		}

		private List<RelatedItem> relatedItemsList = null;

		public List<RelatedItem> getRelatedItemList() {
			return relatedItemsList;
		}
		
		// pages
		private String strSelectPattern = null;

		public String getPageSelectorPattern() {
			return strSelectPattern;
		}

		private String strUrlValue = null;

		public String getPageUrl() {
			return strUrlValue;
		}

		// pages end
		
		public void processNewsPageItem(Element newsPageItemNode) throws HostConfigException {
			Element newsContentTagNode = newsPageItemNode.element("newsContentTag");
			if (newsContentTagNode == null)
				throw new HostConfigException("newsPageProcess node must have newsContentTag child node!");
			strNewsContentTag = newsContentTagNode.getTextTrim();
			logger.info("newsPageProcess---------begin");
			logger.info("newsContentTag: " + strNewsContentTag);
			
			Element abstractPatternNode = newsPageItemNode.element("abstractPattern");
			if (abstractPatternNode == null)
				throw new HostConfigException("newsPageProcess node must have abstractPattern child node!");
			strAbstractPattern = abstractPatternNode.getTextTrim();
			logger.info("abstractPattern: " + strAbstractPattern);
			try {
				processElementReplace(newsPageItemNode);
				processContentReplace(newsPageItemNode);
				processRelatedItems(newsPageItemNode);
				processPages(newsPageItemNode);
			} catch (HostConfigException e) {
				throw e;
			}
			logger.info("newsPageProcess---------end");
		}
		
		private void processElementReplace(Element newsPageItemNode) throws HostConfigException {
			Element elementReplaceNode = newsPageItemNode.element("elementReplace");
			if (elementReplaceNode == null) 
				throw new HostConfigException("newsPageProcess node must have elementReplace child node!");
			if (elementReplaceList == null) {
				elementReplaceList = new ArrayList<ConfigItem>();
			}
			List<?> elementReplaceItemList = elementReplaceNode.elements("item");
			logger.info("elementReplace---------begin");
			for (Object obj : elementReplaceItemList) {
				Element el = (Element)obj;
				Element orgPatternNode = el.element("orgPattern");
				if (orgPatternNode == null) 
					throw new HostConfigException("elementReplace's item node must have orgPattern child node!");
				Element newContentNode = el.element("newContent");
				if (newContentNode == null) 
					throw new HostConfigException("elementReplace's item node must have newContent child node!");
				ConfigItem elementRepItem = new ConfigItem();
				elementRepItem.setFirst(orgPatternNode.getTextTrim());
				elementRepItem.setSecond(newContentNode.getTextTrim());
				logger.info("orgPattern: " + orgPatternNode.getTextTrim());
				logger.info("newContent: " + newContentNode.getTextTrim());
				/*if (elementReplaceList == null) {
					elementReplaceList = new ArrayList<ConfigItem>();
				}*/
				elementReplaceList.add(elementRepItem);
			}
			logger.info("elementReplace---------end");
		}
		
		private void processContentReplace(Element newsPageItemNode) throws HostConfigException {
			Element contentReplaceNode = newsPageItemNode.element("contentReplace");
			if (contentReplaceNode == null) 
				throw new HostConfigException("newsPageProcess node must have contentReplace child node!");
			if (contentReplaceList == null) {
				contentReplaceList = new ArrayList<ConfigItem>();
			}
			List<?> contentReplaceItemList = contentReplaceNode.elements("item");
			logger.info("contentReplace -----begin");
			for (Object obj : contentReplaceItemList) {
				Element el = (Element)obj;
				Element orgPatternNode = el.element("orgPattern");
				if (orgPatternNode == null) 
					throw new HostConfigException("contentReplace's item node must have orgPattern child node!");
				Element newContentNode = el.element("newContent");
				if (newContentNode == null) 
					throw new HostConfigException("contentReplace's item node must have newContent child node!");
				ConfigItem contentRepItem = new ConfigItem();
				contentRepItem.setFirst(orgPatternNode.getTextTrim());
				contentRepItem.setSecond(newContentNode.getTextTrim());
				logger.info("orgPattern: " + orgPatternNode.getTextTrim());
				logger.info("newContent: " + newContentNode.getTextTrim());
				/*if (contentReplaceList == null) {
					contentReplaceList = new ArrayList<ConfigItem>();
				}*/
				contentReplaceList.add(contentRepItem);
			}
			logger.info("contentReplace -----end");
		}
		
		private void processRelatedItems(Element newsPageItemNode) throws HostConfigException {
			Element relatedItemsNode = newsPageItemNode.element("relatedItems");
			if (relatedItemsNode == null) 
				throw new HostConfigException("newsPageProcess node must have relatedItems child node!");
			if (relatedItemsList == null) {
				relatedItemsList = new ArrayList<RelatedItem>();
			}
			List<?> relatedList = relatedItemsNode.elements("item");
			logger.info("relatedItems-----begin");
			for (Object obj : relatedList) {
				Element el = (Element)obj;
				Element relatedPatternNode = el.element("relatedPattern");
				if (relatedPatternNode == null) 
					throw new HostConfigException("relatedItems's item node must have relatedPattern child node!");
				Element relatedItemsTypeNode = el.element("relatedItemsType");
				if (relatedItemsTypeNode == null) 
					throw new HostConfigException("relatedItems's item node must have relatedItemsType child node!");
				Element ignoreTagsNode = el.element("ignoreTags");
				if (ignoreTagsNode == null) {
					throw new HostConfigException("relatedItems's item node must have ignoreTags child node!");
				}
				RelatedItem relatedItem = new RelatedItem();
				relatedItem.setRelatedPattern(relatedPatternNode.getTextTrim());
				relatedItem.setRelatedItemType(relatedItemsTypeNode.getTextTrim());
				relatedItem.setIgnoreTags(ignoreTagsNode.getTextTrim());
				logger.info("relatedPattern: " + relatedPatternNode.getTextTrim());
				logger.info("relatedItemsType: " + relatedItemsTypeNode.getTextTrim() );
				logger.info("ignoreTags: " + ignoreTagsNode.getTextTrim());
				/*if (relatedItemsList == null) {
					relatedItemsList = new ArrayList<RelatedItem>();
				}*/
				relatedItemsList.add(relatedItem);
			}
			logger.info("relatedItems-----end");
		}
		
		private void processPages(Element newsPageItemNode) throws HostConfigException {
			// pages
			Element pagesNode = newsPageItemNode.element("pages");
			if (pagesNode == null) throw new HostConfigException("newsPageProcess node must have pages child node!");
			Element selectPatternNode = pagesNode.element("selectPattern");
			if (selectPatternNode == null ) throw new HostConfigException("pages node must have selectPattern child node!");
			strSelectPattern = selectPatternNode.getTextTrim();
			logger.info("pages -------begin");
			logger.info("selectPattern:" + strSelectPattern );
			Element urlValueNode = pagesNode.element("urlValue");
			if (urlValueNode == null ) throw new HostConfigException("pages node must have urlValue child node!");
			strUrlValue = urlValueNode.getTextTrim();
			logger.info("urlValue:" + strUrlValue );
			logger.info("pages -------end");
		}
		
	}
	
	public class NewsArea {
		private List<ContentArea> contentSelectorList = null ;
		public List<ContentArea> getContentSelectorList() { return contentSelectorList; }
		// newsList
		private String strListTagPattern = null;
		public String getListTagPattern() { return strListTagPattern; }
		
		private String strNewsUrlPattern = null;
		public String getNewsUrlPattern() { return strNewsUrlPattern; }
		
		private String strExcludePattern = null;
		public String getExcludePattern() { return strExcludePattern; }
		
		private String strNewsTitlePattern = null;
		public String getNewTitlePattern() { return strNewsTitlePattern; }
		
		private String strUrlLinkReg = null;
		public String getUrlLinkReg() { return strUrlLinkReg; }
		// newsList end
		
		/*// newsContent
		private String strNewsContentTag = null;
		public String getNewsContentTag() { return strNewsContentTag; }
		
		private String strAbstractPattern = null;
		public String getAbstractPattern() { return strAbstractPattern;}
		
		private List<ConfigItem> elementReplaceList = null;
		public List<ConfigItem> getElementReplaceList() { return elementReplaceList; }
		
		private List<ConfigItem> contentReplaceList = null ;
		public List<ConfigItem> getContentReplaceList() { return contentReplaceList; }
		
		private List<RelatedItem> relatedItemsList = null ;
		public List<RelatedItem> getRelatedItemList() { return relatedItemsList; }
		
		// pages
		private String strSelectPattern = null;
		public String getPageSelectorPattern() { return strSelectPattern; }
		
		private String strUrlValue = null;
		public String getPageUrl() { return strUrlValue; }*/
		
		private List<NewsPageProcess> pageProcessList = null;
		public List<NewsPageProcess> getPageProcessList() {
			return pageProcessList;
		}
		// pages end
		
		// newsContent end
		
		// pictureProcess
		private PicProcess picProcess = null;
		public PicProcess getPicHandler() { return picProcess; }
		/*private String strPicContentTag = null;
		public String getPicContentTag() { return strPicContentTag; }
		
		private String strPicAbstractPattern = null;
		public String getPicAbstractPattern() { return strPicAbstractPattern; }
		
		private String strPicSlide = null;
		public String getPicSlide() { return strPicSlide; }
		
		private String strPicCountMax = null;
		public String getPicCountMax() { return strPicCountMax; }
		
		private String strPicCount = null;
		public String getPicCount() { return strPicCount; }
		
		private int picCountIndex = 0;
		public int getPicCountIndex() { return picCountIndex; }
		
		private String strFirstPicCountLetter = null;
		public String getFirstPicCountLetter() { return strFirstPicCountLetter; }
		
		private String strSecondPicCountLetter = null;
		public String getSecondPicCountLetter() { return strSecondPicCountLetter; }
		
		private String strNextPicturePattern = null;
		public String getNextPicturePattern() { return strNextPicturePattern; }
		
		private String strNextPictureAttr = null;
		public String getNextPictureAttr() { return strNextPictureAttr; }*/
		// pictureProcess end
		
		public boolean processPicHtml(org.jsoup.nodes.Document doc, Article article) {
			return picProcess.processHtml(doc, article);
		}
		
		public void processNewsArea(Element newsAreaNode) throws HostConfigException {
			Element contentAreaNode = newsAreaNode.element("contentArea");
			if (contentAreaNode == null ) 
				throw new HostConfigException("newsArea node must have contentArea child node!");
			List<?> itemList = contentAreaNode.elements("item");
			if (itemList.isEmpty()) 
				throw new HostConfigException("contentArea node must have item child node!");
			logger.info("newsArea-------begin");
			for (Object obj : itemList) {
				Element el = (Element) obj;
				try {
					processContentAreaItem(el);
				} catch (HostConfigException e) {
					throw e;
				}

			}
			
			Element newsListNode = newsAreaNode.element("newsList");
			if (newsListNode == null) 
				throw new HostConfigException("newsArea node must have newsList child node!");
			try {
				processNewsList(newsListNode);
			} catch (HostConfigException e) {
				throw e;
			}
			
			Element newsPageProcessNode = newsAreaNode.element("newsPageProcess");
			if (newsPageProcessNode == null)
				throw new HostConfigException("newsArea node must have newsPageProcess child node!");
			try {
				processNewsPageProcess(newsPageProcessNode);
				processPicturePage(newsAreaNode);
			} catch (HostConfigException e) {
				throw e;
			}
			
			logger.info("newsArea-------end");
			
		}
		
		private void processContentAreaItem(Element contentAreaItem) throws HostConfigException {
			ContentArea area = new ContentArea();
			Element contentSelectorNode = contentAreaItem.element("contentSelector");
			if (contentSelectorNode == null)
				throw new HostConfigException("contentArea's item node must have contentSelector child node!");
			area.setContentSelector(contentSelectorNode.getTextTrim());
			logger.info("contentArea's item-------begin");
			logger.info("contentSelector: " + contentSelectorNode.getTextTrim());
			
			Element mustHaveChildNode = contentAreaItem.element("mustHaveChild");
			if (mustHaveChildNode == null)
				throw new HostConfigException("contentArea's item node must have mustHaveChild child node!");
			area.setMustHaveChild(mustHaveChildNode.getTextTrim());
			// logger.info("contentArea's item-------begin");
			logger.info("mustHaveChild: " + mustHaveChildNode.getTextTrim());
			
			Element titleNode = contentAreaItem.element("title");
			if (titleNode == null)
				throw new HostConfigException("contentArea's item node must have title child node!");
			area.setTitle(titleNode.getTextTrim());
			logger.info("title: " + titleNode.getTextTrim());
			
			Element typeNode = contentAreaItem.element("type");
			if (typeNode == null)
				throw new HostConfigException("contentArea's item node must have type child node!");
			area.setType(typeNode.getTextTrim());
			logger.info("type: " + typeNode.getTextTrim());
			logger.info("contentArea's item-------end");
			
			if (contentSelectorList == null)
				contentSelectorList = new ArrayList<ContentArea>();
			contentSelectorList.add(area);
		}
		
		private void processNewsList(Element newsListNode) throws HostConfigException {
			Element listTagPatternNode = newsListNode.element("listTagPattern");
			if (listTagPatternNode == null) throw new HostConfigException("newsList node must have listTagPattern child node!");
			strListTagPattern = listTagPatternNode.getTextTrim();
			logger.info("newsList--------begin");
			logger.info("listTagPattern: " + strListTagPattern);
			
			Element newsUrlPatternNode = newsListNode.element("newsUrlPattern");
			if (newsUrlPatternNode == null ) throw new HostConfigException("newsList node must have newsUrlPattern child node!");
			strNewsUrlPattern = newsUrlPatternNode.getTextTrim();
			logger.info("newsUrlPattern: " + strNewsUrlPattern);
			
			Element excludePatternNode = newsListNode.element("excludePattern");
			if (excludePatternNode == null) 
				throw new HostConfigException("newsList node must have excludePattern child node!");
			strExcludePattern = excludePatternNode.getTextTrim();
			logger.info("excludePattern: " + strExcludePattern);
			
			Element urlLinkRegNode = newsListNode.element("urlLinkReg");
			if (urlLinkRegNode == null) 
				throw new HostConfigException("newsList node must have urlLinkReg child node!");
			strUrlLinkReg = urlLinkRegNode.getTextTrim();
			logger.info("urlLinkReg: " + strUrlLinkReg);
			
			Element newsTitlePatternNode = newsListNode.element("newsTitlePattern");
			if (newsTitlePatternNode == null) 
				throw new HostConfigException("newsList node must have newsTitlePattern child node!");
			strNewsTitlePattern = newsTitlePatternNode.getTextTrim();
			logger.info("newsTitlePattern: " + strNewsTitlePattern);
			
			logger.info("newsList--------end");
		}
		
		private void processNewsPageProcess(Element newsPageProcessNode) throws HostConfigException {
			/*Element newsContentTagNode = newsPageProcessNode.element("newsContentTag");
			if (newsContentTagNode == null)
				throw new HostConfigException("newsPageProcess node must have newsContentTag child node!");
			strNewsContentTag = newsContentTagNode.getTextTrim();
			logger.info("newsPageProcess---------begin");
			logger.info("newsContentTag: " + strNewsContentTag);
			
			Element abstractPatternNode = newsPageProcessNode.element("abstractPattern");
			if (abstractPatternNode == null)
				throw new HostConfigException("newsPageProcess node must have abstractPattern child node!");
			strAbstractPattern = abstractPatternNode.getTextTrim();
			logger.info("abstractPattern: " + strAbstractPattern);
			try {
				processElementReplace(newsPageProcessNode);
				processContentReplace(newsPageProcessNode);
				processRelatedItems(newsPageProcessNode);
				processPages(newsPageProcessNode);
			} catch (HostConfigException e) {
				throw e;
			}
			logger.info("newsPageProcess---------end");*/
			
			List<?> itemList = newsPageProcessNode.elements("item");
			if (itemList.isEmpty()) 
				throw new HostConfigException("newsPageProcess node must have item child node!");
			logger.info("newsPageProcess-------begin");
			if (pageProcessList == null) {
				pageProcessList = new ArrayList<NewsPageProcess>();
			}
			for (Object obj : itemList) {
				Element el = (Element) obj;
				try {
					NewsPageProcess page = new NewsPageProcess();
					page.processNewsPageItem(el);
					pageProcessList.add(page);
				} catch (HostConfigException e) {
					throw e;
				}

			}
			
			logger.info("newsPageProcess---------end");
		}
		
		/*private void processElementReplace(Element newsPageProcessNode) throws HostConfigException {
			Element elementReplaceNode = newsPageProcessNode.element("elementReplace");
			if (elementReplaceNode == null) 
				throw new HostConfigException("newsPageProcess node must have elementReplace child node!");
			if (elementReplaceList == null) {
				elementReplaceList = new ArrayList<ConfigItem>();
			}
			List<?> elementReplaceItemList = elementReplaceNode.elements("item");
			logger.info("elementReplace---------begin");
			for (Object obj : elementReplaceItemList) {
				Element el = (Element)obj;
				Element orgPatternNode = el.element("orgPattern");
				if (orgPatternNode == null) 
					throw new HostConfigException("elementReplace's item node must have orgPattern child node!");
				Element newContentNode = el.element("newContent");
				if (newContentNode == null) 
					throw new HostConfigException("elementReplace's item node must have newContent child node!");
				ConfigItem elementRepItem = new ConfigItem();
				elementRepItem.setFirst(orgPatternNode.getTextTrim());
				elementRepItem.setSecond(newContentNode.getTextTrim());
				logger.info("orgPattern: " + orgPatternNode.getTextTrim());
				logger.info("newContent: " + newContentNode.getTextTrim());
				if (elementReplaceList == null) {
					elementReplaceList = new ArrayList<ConfigItem>();
				}
				elementReplaceList.add(elementRepItem);
			}
			logger.info("elementReplace---------end");
		}*/
		
		/*private void processContentReplace(Element newsPageProcessNode) throws HostConfigException {
			Element contentReplaceNode = newsPageProcessNode.element("contentReplace");
			if (contentReplaceNode == null) 
				throw new HostConfigException("newsPageProcess node must have contentReplace child node!");
			if (contentReplaceList == null) {
				contentReplaceList = new ArrayList<ConfigItem>();
			}
			List<?> contentReplaceItemList = contentReplaceNode.elements("item");
			logger.info("contentReplace -----begin");
			for (Object obj : contentReplaceItemList) {
				Element el = (Element)obj;
				Element orgPatternNode = el.element("orgPattern");
				if (orgPatternNode == null) 
					throw new HostConfigException("contentReplace's item node must have orgPattern child node!");
				Element newContentNode = el.element("newContent");
				if (newContentNode == null) 
					throw new HostConfigException("contentReplace's item node must have newContent child node!");
				ConfigItem contentRepItem = new ConfigItem();
				contentRepItem.setFirst(orgPatternNode.getTextTrim());
				contentRepItem.setSecond(newContentNode.getTextTrim());
				logger.info("orgPattern: " + orgPatternNode.getTextTrim());
				logger.info("newContent: " + newContentNode.getTextTrim());
				if (contentReplaceList == null) {
					contentReplaceList = new ArrayList<ConfigItem>();
				}
				contentReplaceList.add(contentRepItem);
			}
			logger.info("contentReplace -----end");
		}*/
		
		/*private void processRelatedItems(Element newsPageProcessNode) throws HostConfigException {
			Element relatedItemsNode = newsPageProcessNode.element("relatedItems");
			if (relatedItemsNode == null) 
				throw new HostConfigException("newsPageProcess node must have relatedItems child node!");
			if (relatedItemsList == null) {
				relatedItemsList = new ArrayList<RelatedItem>();
			}
			List<?> relatedList = relatedItemsNode.elements("item");
			logger.info("relatedItems-----begin");
			for (Object obj : relatedList) {
				Element el = (Element)obj;
				Element relatedPatternNode = el.element("relatedPattern");
				if (relatedPatternNode == null) 
					throw new HostConfigException("relatedItems's item node must have relatedPattern child node!");
				Element relatedItemsTypeNode = el.element("relatedItemsType");
				if (relatedItemsTypeNode == null) 
					throw new HostConfigException("relatedItems's item node must have relatedItemsType child node!");
				Element ignoreTagsNode = el.element("ignoreTags");
				if (ignoreTagsNode == null) {
					throw new HostConfigException("relatedItems's item node must have ignoreTags child node!");
				}
				RelatedItem relatedItem = new RelatedItem();
				relatedItem.setRelatedPattern(relatedPatternNode.getTextTrim());
				relatedItem.setRelatedItemType(relatedItemsTypeNode.getTextTrim());
				relatedItem.setIgnoreTags(ignoreTagsNode.getTextTrim());
				logger.info("relatedPattern: " + relatedPatternNode.getTextTrim());
				logger.info("relatedItemsType: " + relatedItemsTypeNode.getTextTrim() );
				logger.info("ignoreTags: " + ignoreTagsNode.getTextTrim());
				if (relatedItemsList == null) {
					relatedItemsList = new ArrayList<RelatedItem>();
				}
				relatedItemsList.add(relatedItem);
			}
			logger.info("relatedItems-----end");
		}*/
		
		/*private void processPages(Element newsPageProcessNode) throws HostConfigException {
			// pages
			Element pagesNode = newsPageProcessNode.element("pages");
			if (pagesNode == null) throw new HostConfigException("newsPageProcess node must have pages child node!");
			Element selectPatternNode = pagesNode.element("selectPattern");
			if (selectPatternNode == null ) throw new HostConfigException("pages node must have selectPattern child node!");
			strSelectPattern = selectPatternNode.getTextTrim();
			logger.info("pages -------begin");
			logger.info("selectPattern:" + strSelectPattern );
			Element urlValueNode = pagesNode.element("urlValue");
			if (urlValueNode == null ) throw new HostConfigException("pages node must have urlValue child node!");
			strUrlValue = urlValueNode.getTextTrim();
			logger.info("urlValue:" + strUrlValue );
			logger.info("pages -------end");
		}*/
		
		private void processPicturePage(Element newsAreaNode) throws HostConfigException  {
			Element pictureNode = newsAreaNode.element("picturePageProcess");
			if (pictureNode == null)
				throw new HostConfigException("newsArea node must have picturePageProcess child node!");
			
			Element hexunNode = pictureNode.element("hexun");
			if (hexunNode != null)
			{
				picProcess = new HexunPicProcess();
				picProcess.processPicNode(hexunNode);
				return;
			}
			
			Element sinaNode = pictureNode.element("sina");
			if (sinaNode != null) {
				picProcess = new SinaPicProcess();
				picProcess.processPicNode(sinaNode);
				return;
			}
			throw new HostConfigException("picturePageProcess node must have hexun or sina node");
			/*logger.info("picturePageProcess----begin");
			Element picContentTagNode = pictureNode.element("picContentTag");
			if (picContentTagNode == null)
				throw new HostConfigException("picturePageProcess node must have picContentTag child node!");
			strPicContentTag = picContentTagNode.getTextTrim();
			logger.info("picContentTag:" + strPicContentTag);
			
			Element picAbstractPatternNode = pictureNode.element("picAbstractPattern");
			if (picAbstractPatternNode == null)
				throw new HostConfigException("picturePageProcess node must have picAbstractPattern child node!");
			strPicAbstractPattern = picAbstractPatternNode.getTextTrim();
			logger.info("picAbstractPattern:" + strPicAbstractPattern);
			
			Element picSlideNode = pictureNode.element("picSlide");
			if (picSlideNode == null)
				throw new HostConfigException("picturePageProcess node must have picSlide child node!");
			strPicSlide = picSlideNode.getTextTrim();
			logger.info("picSlide:" + strPicSlide );
			
			Element picCountMaxNode = pictureNode.element("picCountMax");
			if (picCountMaxNode == null)
				throw new HostConfigException("picturePageProcess node must have picCountMax child node!");
			strPicCountMax = picCountMaxNode.getTextTrim();
			logger.info("picCountMax:" + strPicCountMax);
			
			Element picCountNode = pictureNode.element("picCount");
			if (picCountNode == null)
				throw new HostConfigException("picturePageProcess node must have picCount child node!");
			strPicCount = picCountNode.getTextTrim();
			logger.info("picCount:" + strPicCount);
			
			Element firstPicCountLetterNode = pictureNode.element("firstPicCountLetter");
			if (firstPicCountLetterNode == null)
				throw new HostConfigException("picturePageProcess node must have firstPicCountLetter child node!");
			strFirstPicCountLetter = firstPicCountLetterNode.getTextTrim();
			logger.info("firstPicCountLetter:" + strFirstPicCountLetter );
			
			Element secondPicCountLetterNode = pictureNode.element("secondPicCountLetter");
			if (secondPicCountLetterNode == null)
				throw new HostConfigException("picturePageProcess node must have secondPicCountLetter child node!");
			strSecondPicCountLetter = secondPicCountLetterNode.getTextTrim();
			logger.info("secondPicCountLetter:" + strSecondPicCountLetter );
			
			Element nextPicturePatternNode = pictureNode.element("nextPicturePattern");
			if (nextPicturePatternNode == null)
				throw new HostConfigException("picturePageProcess node must have nextPicturePattern child node!");
			strNextPicturePattern = nextPicturePatternNode.getTextTrim();
			logger.info("nextPicturePattern:" + strNextPicturePattern);
			
			Element nextPictureAttrNode = pictureNode.element("nextPictureAttr");
			if (nextPictureAttrNode == null)
				throw new HostConfigException("picturePageProcess node must have nextPictureAttr child node!");
			strNextPictureAttr = nextPictureAttrNode.getTextTrim();
			logger.info("nextPictureAttr:" + strNextPictureAttr);
			
			try {
				Element picCountIndexNode = pictureNode.element("picCountIndex");
				if (picCountIndexNode == null)
					throw new HostConfigException("picturePageProcess node must have picCountIndex child node!");
				picCountIndex = Integer.parseInt( picCountIndexNode.getTextTrim());
				logger.info("picCountIndex:" + picCountIndex );
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HostConfigException("picCountIndex node must be number!");
			}
			logger.info("picturePageProcess----end");*/
		}
		
		
	}
	public class HexunPicProcess implements PicProcess {
		// pictureProcess
		private String strPicContentTag = null;

		public String getPicContentTag() {
			return strPicContentTag;
		}

		private String strPicAbstractPattern = null;

		public String getPicAbstractPattern() {
			return strPicAbstractPattern;
		}

		private String strPicSlide = null;

		public String getPicSlide() {
			return strPicSlide;
		}

		private String strPicCountMax = null;

		public String getPicCountMax() {
			return strPicCountMax;
		}

		private String strPicCount = null;

		public String getPicCount() {
			return strPicCount;
		}

		private int picCountIndex = 0;

		public int getPicCountIndex() {
			return picCountIndex;
		}

		private String strFirstPicCountLetter = null;

		public String getFirstPicCountLetter() {
			return strFirstPicCountLetter;
		}

		private String strSecondPicCountLetter = null;

		public String getSecondPicCountLetter() {
			return strSecondPicCountLetter;
		}

		private String strNextPicturePattern = null;

		public String getNextPicturePattern() {
			return strNextPicturePattern;
		}

		private String strNextPictureAttr = null;

		public String getNextPictureAttr() {
			return strNextPictureAttr;
		}
		
		Article curArticle = null;
		private String baseUrl;

		// pictureProcess end
		public boolean processPicNode(Element hexunNode) throws HostConfigException
		{
			/*
			Element hexunNode = hexunNode.element("picturePageProcess");
			if (hexunNode == null)
				throw new HostConfigException("newsPageProcess node must have picturePageProcess child node!");
			*/
			logger.info("picturePageProcess-->hexun  ----begin");
			Element picContentTagNode = hexunNode.element("picContentTag");
			if (picContentTagNode == null)
				throw new HostConfigException("hexun node must have picContentTag child node!");
			strPicContentTag = picContentTagNode.getTextTrim();
			logger.info("picContentTag:" + strPicContentTag);
			
			Element picAbstractPatternNode = hexunNode.element("picAbstractPattern");
			if (picAbstractPatternNode == null)
				throw new HostConfigException("hexun node must have picAbstractPattern child node!");
			strPicAbstractPattern = picAbstractPatternNode.getTextTrim();
			logger.info("picAbstractPattern:" + strPicAbstractPattern);
			
			Element picSlideNode = hexunNode.element("picSlide");
			if (picSlideNode == null)
				throw new HostConfigException("hexun node must have picSlide child node!");
			strPicSlide = picSlideNode.getTextTrim();
			logger.info("picSlide:" + strPicSlide );
			
			Element picCountMaxNode = hexunNode.element("picCountMax");
			if (picCountMaxNode == null)
				throw new HostConfigException("hexun node must have picCountMax child node!");
			strPicCountMax = picCountMaxNode.getTextTrim();
			logger.info("picCountMax:" + strPicCountMax);
			
			Element picCountNode = hexunNode.element("picCount");
			if (picCountNode == null)
				throw new HostConfigException("hexun node must have picCount child node!");
			strPicCount = picCountNode.getTextTrim();
			logger.info("picCount:" + strPicCount);
			
			Element firstPicCountLetterNode = hexunNode.element("firstPicCountLetter");
			if (firstPicCountLetterNode == null)
				throw new HostConfigException("hexun node must have firstPicCountLetter child node!");
			strFirstPicCountLetter = firstPicCountLetterNode.getTextTrim();
			logger.info("firstPicCountLetter:" + strFirstPicCountLetter );
			
			Element secondPicCountLetterNode = hexunNode.element("secondPicCountLetter");
			if (secondPicCountLetterNode == null)
				throw new HostConfigException("hexun node must have secondPicCountLetter child node!");
			strSecondPicCountLetter = secondPicCountLetterNode.getTextTrim();
			logger.info("secondPicCountLetter:" + strSecondPicCountLetter );
			
			Element nextPicturePatternNode = hexunNode.element("nextPicturePattern");
			if (nextPicturePatternNode == null)
				throw new HostConfigException("hexun node must have nextPicturePattern child node!");
			strNextPicturePattern = nextPicturePatternNode.getTextTrim();
			logger.info("nextPicturePattern:" + strNextPicturePattern);
			
			Element nextPictureAttrNode = hexunNode.element("nextPictureAttr");
			if (nextPictureAttrNode == null)
				throw new HostConfigException("hexun node must have nextPictureAttr child node!");
			strNextPictureAttr = nextPictureAttrNode.getTextTrim();
			logger.info("nextPictureAttr:" + strNextPictureAttr);
			
			try {
				Element picCountIndexNode = hexunNode.element("picCountIndex");
				if (picCountIndexNode == null)
					throw new HostConfigException("hexun node must have picCountIndex child node!");
				picCountIndex = Integer.parseInt( picCountIndexNode.getTextTrim());
				logger.info("picCountIndex:" + picCountIndex );
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HostConfigException("picCountIndex node must be number!");
			}
			logger.info("picturePageProcess-->hexun  ----end");
			return true;
		}
		
		public boolean processHtml(org.jsoup.nodes.Document doc, Article article) {
			if (article == null) {
				return false;
			}
			curArticle = article;
			Elements els = doc.select(strPicContentTag);
			try {
				if (els.first() != null) {
					Elements subTitles = els.select(strPicAbstractPattern);
					Elements pics = els.select(strPicSlide);
					if (subTitles.first() != null) {
						// curArticle.setAbstract( subTitles.first().html() );
						curArticle.setHtml(subTitles.first().html());
						if (pics.first() != null) {
							curArticle.setHtml(subTitles.first().html()
									+ pics.first().html());
							curArticle.setArcType(Article.ALLRESOURCE);
							processPageImage(els);
							Elements count_max = els.select(strPicCountMax);
							String strMax = count_max.first().text();
							Elements pic_count = els.select(strPicCount);
							String strCount = pic_count.get(picCountIndex)
									.text();
							// System.out.println( strMax + "\t" + strCount );
							if (Integer.parseInt(strMax) != Integer
									.parseInt(strCount.substring(
											strCount.indexOf(strFirstPicCountLetter) + 1,
											strCount.indexOf(strSecondPicCountLetter)))) {

								Elements nexts = els
										.select(strNextPicturePattern);
								String href = nexts.first().attr(
										strNextPictureAttr);
								processMorePictureHtml(href);
							}

						}
					}
					return true;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}

			return false;
		}
		
		private boolean processMorePictureHtml(String url) {
			boolean isRight = true;

			/*
			 * org.jsoup.nodes.Document doc =
			 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
			 */
			String strWebContent = null;
			try {
				strWebContent = getContentFromUrl(url);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				isRight = false;

			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				isRight = false;
			}
			/*
			 * org.jsoup.nodes.Document doc =
			 * Jsoup.connect(url).timeout(curHostConfig.getTimeOut()).get();
			 */
			org.jsoup.nodes.Document doc = Jsoup.parse(strWebContent, baseUrl);
			Elements els = doc.select(strPicContentTag);
			if (els.first() != null) {
				processPageImage(els);
				Elements count_max = els.select(strPicCountMax);
				String strMax = count_max.first().text();
				Elements pic_count = els.select(strPicCount);
				String strCount = pic_count.get(picCountIndex).text();
				try {
					if (Integer.parseInt(strMax) != Integer
							.parseInt(strCount.substring(
									strCount.indexOf(strFirstPicCountLetter) + 1,
									strCount.indexOf(strSecondPicCountLetter)))) {
						Elements nexts = els.select(strNextPicturePattern);
						String href = nexts.first().attr(strNextPictureAttr);
						processMorePictureHtml(href);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					isRight = false;
				}

			}
			return isRight;
		}
		
		private String getContentFromUrl(String url)
				throws ClientProtocolException, IOException {
			String content = null;
			try {
				HttpClient client = new DefaultHttpClient();
				client.getParams();
				HttpGet request = new HttpGet(url);
				baseUrl = request.getURI().getHost();
				// request.getParams()
				HttpResponse response = client.execute(request);
				byte[] bytes = EntityUtils.toByteArray(response.getEntity());
				content = new String(bytes, "gbk");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				logger.error("process URL:" + url + "failed! " + e.getMessage());
				throw e;

			} catch (IOException e) {
				e.printStackTrace();
				logger.error("process URL:" + url + "failed! " + e.getMessage());
				throw e;
			}
			return content;
		}
		
		
		private void processPageImage(Elements els) {
			Elements images = els.select("img");
			for (org.jsoup.nodes.Element image : images) {
				String url = image.absUrl("src");

				logger.info(url);
				logger.info(curArticle.getOrgUrl());
				// testImage(url);
				try {
					URL imgUrl = new URL(url);
					URLConnection connection = imgUrl.openConnection();
					connection.setConnectTimeout(timeOut);
					connection.setReadTimeout(timeOut);

					BufferedImage bufImage = ImageIO.read(connection
							.getInputStream());
					Resource media = new Resource();
					media.setDbFile(strDbFile);
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
					if (curArticle.getArcType() == Article.ALLRESOURCE) {
						Elements subTitles = els.select(strPicSlide);
						if (subTitles.first() != null
								&& (subTitles.first().text().length() > 0)) {
							/* media.setTitle(subTitles.first().html()); */
							media.setResText(subTitles.first().html());
						} else {
							/* media.setTitle(curArticle.getTitle()); */
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
					}
					// default:
					// break;
					// }

					curArticle.addMedia(media);
					// System.out.println("height: " + bufImage.getHeight());
					// System.out.println("width: " + bufImage.getWidth());

				} catch (MalformedURLException e) {

					e.printStackTrace();
					logger.error(e.getMessage());
					continue;
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					logger.error(url);
					continue;
				}
			}
		}
	}
	
	public class SinaPicProcess implements PicProcess {
		
		private String strHtmlContent = null;
		public String getHtmlContent() { return strHtmlContent ; }
		
		private int htmlContentIndex = 0;
		public int getHtmlContentIndex() { return htmlContentIndex; }
		
		private String strPicContentTag = null;
		public String getPicContentTag() { return strPicContentTag; }
		
		private String strPicElements = null;
		public String getPicElements() { return strPicElements; }
		
		private String strPicDesc = null;
		public String getPicDesc() { return strPicDesc; }
		
		private String strPicSpec = null;
		public String getPicSpec() { return strPicSpec; }
		
		private int picIndex = 0;
		public int getPicIndex() { return picIndex; }
		
		Article curArticle = null;
		
		public boolean processPicNode(Element sinaNode) throws HostConfigException
		{
			logger.info("picturePageProcess-->sina  ----begin");
			Element picContentTagNode = sinaNode.element("picContentTag");
			if (picContentTagNode == null) {
				throw new HostConfigException("sina node must have picContentTag child node!");
			}
			strPicContentTag = picContentTagNode.getTextTrim();
			logger.info("picContentTag:"+strPicContentTag);
			
			Element picElementsNode = sinaNode.element("picElements");
			if (picElementsNode == null) {
				throw new HostConfigException("sina node must have picElements child node!");
			}
			strPicElements = picElementsNode.getTextTrim();
			logger.info("picElements:" + strPicElements);
			
			Element picDescNode = sinaNode.element("picDesc");
			if (picDescNode == null) {
				throw new HostConfigException("sina node must have picDesc child node!");
			}
			strPicDesc = picDescNode.getTextTrim();
			logger.info("picDesc:" + strPicDesc);
			
			Element picSpecNode = sinaNode.element("picSpec");
			if (picSpecNode == null) {
				throw new HostConfigException("sina node must have picSpec child node!");
			}
			strPicSpec = picSpecNode.getTextTrim();
			logger.info("picSpec:" + strPicSpec);
			
			Element picIndexNode = sinaNode.element("picIndex");
			if (picIndexNode == null) {
				throw new HostConfigException("sina node must have picIndex child node!");
			}
			
			try {
				picIndex = Integer.parseInt(picIndexNode.getTextTrim());
			} catch(NumberFormatException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HostConfigException("sina-->picIndex must be number!");
			}
			logger.info("picIndex:" + picIndex);
			
			Element htmlContentNode = sinaNode.element("htmlContentIndex");
			if (htmlContentNode == null) {
				throw new HostConfigException("sina node must have htmlContentNodeIndex child node!");
			}
			try {
				htmlContentIndex = Integer.parseInt(htmlContentNode.getTextTrim());
			} catch(NumberFormatException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HostConfigException("sina-->htmlContentIndex must be number!");
			}
			logger.info("htmlContentIndex:" + htmlContentIndex);
			
			logger.info("picturePageProcess-->sina  ----end");
			return true;
		}
		public boolean processHtml(org.jsoup.nodes.Document doc, Article article ) {
			if (article == null) {
				return false;
			}
			boolean isCorrect = false;
			curArticle = article;
			
			Elements picContentEls = doc.select(strPicContentTag/*"div#eData"*/);
			if (picContentEls.first() == null)
				return isCorrect;
			Elements imgCollections = picContentEls.select(strPicElements/*"dl"*/);
			if (imgCollections.first() == null)
				return isCorrect;
			if (imgCollections.first() != null)
			{
				curArticle.setHtml(imgCollections.first().select(strPicSpec).get(htmlContentIndex).html());
				curArticle.setArcType(Article.ALLRESOURCE);
			}
			for (org.jsoup.nodes.Element dl : imgCollections) {
				Resource media = new Resource();
				Elements imgDesc = dl.select(strPicDesc/*"dt"*/);
				// System.out.println(imgDesc.first().html());
				media.setResText(imgDesc.first().html());
				Elements imgUrls = dl.select(strPicSpec/*"dd"*/);
				logger.info(imgUrls.get(picIndex).html());
				media.setResContent(imgUrls.get(picIndex).html());
				media.setResType(Resource.IMGURL);
				media.setDbFile(strDbFile);
				processPageImage(imgUrls.get(picIndex).html(), media);
			}
			isCorrect = true;
			return isCorrect;
		}
		
		private void processPageImage(String url, Resource res)
		{
			try {
				URL imgUrl = new URL(url);
				URLConnection connection = imgUrl.openConnection();
				connection.setConnectTimeout(timeOut);
				connection.setReadTimeout(timeOut);

				BufferedImage bufImage = ImageIO.read(connection
						.getInputStream());
				res.setHeitht(bufImage.getHeight());
				res.setWidth(bufImage.getWidth());
				curArticle.addMedia(res);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				// System.out.println(url);				
			}
		}
	}
	
	private List<NewsArea> newsAreaList = null ;
	public List<NewsArea> getNewsAreaList() { return newsAreaList; }
	
	public void processHostNode(Element hostNode) throws HostConfigException {
		// boolean isCorrect = true;
		Element descNode = hostNode.element("description");
		if (descNode == null) throw new HostConfigException("hosts node must have description child node!");
		strDescription = descNode.getTextTrim();
		logger.info("hosts---------begin");
		logger.info("description:" + strDescription);
		Element urlNode = hostNode.element("url");
		if (urlNode == null) throw new HostConfigException("hosts node must have url child node!");
		strUrl = urlNode.getTextTrim();
		logger.info("url:" + strUrl);
		Element charsetNode = hostNode.element("charset");
		if (charsetNode == null)
			throw new HostConfigException("hosts node must have charset child node!");
		charset = charsetNode.getTextTrim();
		logger.info("charset:" + charset);
		
		Element methodNode = hostNode.element("getsMethod");
		strGetsMethod = methodNode.getTextTrim();
		logger.info("getsMethod:" + strGetsMethod );
		try {
			Element timeOutNode = hostNode.element("timeout");
			if (timeOutNode == null ) throw new HostConfigException("hosts node must have timeout child node!");
			timeOut = Integer.parseInt(timeOutNode.getTextTrim());
			logger.info("timeout:" + timeOut);
		} catch (NumberFormatException e)
		{			
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new HostConfigException("timeout node must be number!");
			// isCorrect = false;
		}
		Element dbNode = hostNode.element("dbFile");
		if (dbNode == null)
			throw new HostConfigException("hosts node must have dbFile child node!");
		strDbFile = dbNode.getTextTrim();
		logger.info("dbFile: " + strDbFile);
		
		List<?> newsAreaNodeList = hostNode.elements("newsArea");
		if (newsAreaNodeList.isEmpty()) throw new HostConfigException("hosts node must have newsArea child node!");
		// logger.info("newsArea-------begin");
		for (Object obj : newsAreaNodeList) {
			Element el = (Element)obj;
			NewsArea news = new NewsArea();
			try {
				news.processNewsArea(el);
				if ( newsAreaList == null ) newsAreaList = new ArrayList<NewsArea>();
				newsAreaList.add(news);
			} catch (HostConfigException e) {
				throw e;
			}
		}
		logger.info("hosts---------end");
		// return isCorrect;
	}
	// hosts attribute end
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(hexun.class.getResource("/log4.properties"));
		HostConfig config = new HostConfig();
		config.processXml("/flipboard.xml");
	}
	
	public void processXml(String xmlFile) {
		URL url = this.getClass().getResource(/* "/flipboard.xml" */ xmlFile);
		String fileName = url.getFile();
		// System.out.println(fileName);
		File file = new File(fileName);
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			List<?> list = document.selectNodes("//flipboard/hosts" );
        	Iterator<?> iter = list.iterator();
        	while (iter.hasNext()) {
        		Element host = (Element)iter.next();
        		processHostNode(host);
        		// System.out.println(host.getTextTrim());
        	}
		} catch(DocumentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch(HostConfigException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
