package test;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;





public class BaiduSearch {
	private static final String LOGTAG = "BaiduSearch"; 
	
	private String host = null;
	private String baiduUrl = null;
	private String searchType = null;
	private String contentCharset = null;
	private String encodeCharset = null;
	private int totalresultCount = 50;
	private int pageRecordCount = 10;
	private String nextPage = null;
	private String nextPageValue = null;
	private int getResultCnt = 0;
	private int searchResult = 0;
	
	private String tableSelect = null;
	private String tdSelect = null;
	private String h3Select = null;
	private String urlSelect = null;
	private String urlValue = null;
	private String titleSelect = null;
	private String descSelect = null;
	private List<SearchResultItem> result = new ArrayList<SearchResultItem>();
	// private int InitResLoop = 0;
	// public void setResultCnt(int cnt ) { resultCnt = cnt; }

	private boolean configInit = false;
	
	public List<SearchResultItem> searchKey(String strSearch) {
		result.clear();
		getResultCnt = 0;
		searchResult = 0;
		/*InputStream is = this..getResources().openRawResource(R.layout.baidu);
		//	debug
		printStream(is);*/
		// return result;
		// debug end
		// return result;
		if (!readConfig()) {
			return result;
		}
		
		String key = "";
		try {
			 key = URLEncoder.encode(strSearch, encodeCharset);
		 } catch(UnsupportedEncodingException e) {
			 e.printStackTrace();
			 System.out.println(e.getMessage());
			 return result;
		 }
		 String url = baiduUrl + key + searchType;
		 // Log.i(LOGTAG, url);
		 String content = getContentFromUrl(url, contentCharset);
		 org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(content);
		 getResult(doc);
		 return result;
	}
	
	private void getResult(org.jsoup.nodes.Document doc) {
		
		for (int i = 0; i < pageRecordCount; searchResult++, i++ ) {
			// Log.i(LOGTAG, "getResultCnt:" + getResultCnt);
			String strSelect = tableSelect + searchResult;
			// System.out.println(strSelect);
			Elements tableEls = doc.select(strSelect);
			for (org.jsoup.nodes.Element el : tableEls) {
				Elements tdEls = el.select(tdSelect);
				Elements h3Els = tdEls.select(h3Select);
				Elements aEls = h3Els.select(urlSelect);
				if (aEls.first() != null) {
					SearchResultItem item = new SearchResultItem();
					String strHref = aEls.first().attr(urlValue);
					Elements titelEls = h3Els.select(titleSelect);
					if (strHref.length() > 0) {
						String realUrl = getRealUrl(strHref);
						if (realUrl.length() <= 0)
							continue;
						// Log.i(LOGTAG, "url: " + realUrl);
						item.setUrl(realUrl);
						String strTitle = "";
						if (titelEls.first() != null) {
							strTitle = aEls.first().text()
									+ titelEls.first().html();
							// Log.i(LOGTAG, "title: " + aEls.first().text()
							// 		+ titelEls.first().html());
						} else {
							strTitle = aEls.first().text();
							// Log.i(LOGTAG, "title: " + aEls.first().text());
						}
						item.setTitle(strTitle);
						Elements textEls = tdEls.select(descSelect);
						if (textEls.first() != null) {
							// Log.i(LOGTAG, "desc: " + textEls.first().text());
							item.setDesc(textEls.first().text());
						} else {
							// Log.i(LOGTAG, "Can't find any description");
							item.setDesc("");
						}
						result.add(item);
						getResultCnt++;
						
					}

				} // end aEls.first()
					// return;
				if (getResultCnt == totalresultCount)
					return;
			} // end for tableEls
			if (getResultCnt == totalresultCount)
				return;
		} // end for i
		
		Elements nextEls = doc.select(nextPage);
		if (nextEls.first() != null) {
			String strNextHref = nextEls.first().attr(nextPageValue).trim();
			// Log.i(LOGTAG, strNextHref);
			if (strNextHref.length() <= 0)
				return;
			strNextHref = host + strNextHref;
			// Log.i(LOGTAG, strNextHref);
			String strContent = getContentFromUrl(strNextHref, contentCharset);
			org.jsoup.nodes.Document docTmp = org.jsoup.Jsoup.parse(strContent);
			getResult(docTmp);
		}
		
	}
	
	private String getContentFromUrl(String url, String charset) {
		// strBuf.delete(0, strBuf.length());
		String content = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams();
			HttpGet request = new HttpGet(url);
			// System.out.println(request.getURI().getHost());
			// request.getParams()
			HttpResponse response = client.execute(request);
			// request.getURI().getHost();
			//GetMethod method = new GetMethod(url);
			// System.out.println(request.getURI().getHost());
			// System.out.println(response.getHeaders("host").toString());
			// response.
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
			// Log.e(LOGTAG, e.getMessage());
			
		} catch(IOException e) {
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
		}
		return content;
	}
	
	private String getRealUrl(String url) {
		HttpClient httpclient = new DefaultHttpClient();  
		HttpParams params = httpclient.getParams();  
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, false); 
		// String content = "";
		String strRealUrl = "";
		HttpGet request = new HttpGet(url);
		// System.out.println(request.getURI().getHost());
		// request.getParams()
		try {
			HttpResponse response = httpclient.execute(request);
			Header header = response.getFirstHeader("Location");
			if (header != null) {
				strRealUrl = header.getValue();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
			
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
		}
		return strRealUrl;
	}
	
	private boolean readConfig() {
		if (configInit) {
			return true;
		}
		URL url = this.getClass().getResource("/baidu.xml");
		try {
			printStream(url.openStream());
		} catch(IOException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		String fileName = url.getFile();
		// Log.i(LOGTAG, fileName);
		File file = new File(fileName);
		try {
			SAXReader saxReader = new SAXReader();
			// saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			// saxReader.setEncoding("utf-8");
			Document document = saxReader.read(url.openStream( ));
			List<?> list = document.selectNodes("//baidu" );
        	Iterator<?> iter = list.iterator();
        	while (iter.hasNext()) {
        		Element baiduNode = (Element)iter.next();
        		if (!processBaiduConfig(baiduNode)) {
        			return false;
        		}
        		
        		// System.out.println(host.getTextTrim());
        	}
		} catch(DocumentException e) {
			// System.err.println(e.getMessage());
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
		}
		configInit  = true;
		return true;
	}
	
	private boolean processBaiduConfig(Element baiduNode) {
		Element hostNode = baiduNode.element("host");
		if (hostNode == null) {
			// Log.e(LOGTAG, "baidu must have host child node");
			return false;
		}
		host = hostNode.getTextTrim();
		// Log.i(LOGTAG, host);
		
		Element urlNode = baiduNode.element("url");
		if (urlNode == null) {
			// Log.e(LOGTAG, "baidu must have url child node");
			return false;
		}
		baiduUrl = urlNode.getTextTrim();
		
		Element searchTypeNode = baiduNode.element("searchType");
		if (searchTypeNode == null) {
			// Log.e(LOGTAG, "baidu must have searchType child node");
			return false;
		}
		searchType = searchTypeNode.getTextTrim();
		
		Element contentCharsetNode = baiduNode.element("contentCharset");
		if (contentCharsetNode == null) {
			// Log.e(LOGTAG, "baidu must have contentCharset child node");
			return false;
		}
		contentCharset = contentCharsetNode.getTextTrim();
		
		Element encodeCharsetNode = baiduNode.element("encodeCharset");
		if (encodeCharsetNode == null) {
			// Log.e(LOGTAG, "baidu must have encodeCharset child node");
			return false;
		}
		encodeCharset = encodeCharsetNode.getTextTrim();
		
		Element totalresultCountNode = baiduNode.element("totalresultCount");
		if (totalresultCountNode == null) {
			// Log.e(LOGTAG, "baidu must have totalresultCount child node");
			return false;
		}
		try {
			totalresultCount = Integer.parseInt(totalresultCountNode.getTextTrim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
			return false;
		}
		
		Element pageRecordCountNode = baiduNode.element("pageRecordCount");
		if (pageRecordCountNode == null) {
			// Log.e(LOGTAG, "baidu must have pageRecordCount child node");
			return false;
		}
		try {
			pageRecordCount = Integer.parseInt(pageRecordCountNode.getTextTrim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
		}
		
		Element nextPageNode = baiduNode.element("nextPage");
		if (nextPageNode == null) {
			// Log.e(LOGTAG, "baidu must have nextPage child node");
			return false;
		}
		nextPage = nextPageNode.getTextTrim();
		
		Element nextPageValueNode = baiduNode.element("nextPageValue");
		if (nextPageValueNode == null) {
			// Log.e(LOGTAG, "baidu must have nextPageValue child node");
			return false;
		}
		nextPageValue = nextPageValueNode.getTextTrim();
		
		Element recordNode = baiduNode.element("record");
		if (recordNode == null) {
			// Log.e(LOGTAG, "baidu must have record child node");
			return false;
		}
		
		Element tableSelectNode = recordNode.element("tableSelect");
		if (tableSelectNode == null) {
			// Log.e(LOGTAG, "record must have tableSelect child node");
			return false;
		}
		tableSelect = tableSelectNode.getTextTrim();
		
		Element tdSelectNode = recordNode.element("tdSelect");
		if (tdSelectNode == null) {
			// Log.e(LOGTAG, "record must have tdSelect child node");
			return false;
		}
		tdSelect = tdSelectNode.getTextTrim();
		
		Element h3SelectNode = recordNode.element("h3Select");
		if (h3SelectNode == null) {
			// Log.e(LOGTAG, "record must have h3Select child node");
			return false;
		}
		h3Select = h3SelectNode.getTextTrim();
		
		Element urlSelectNode = recordNode.element("urlSelect");
		if (urlSelectNode == null) {
			// Log.e(LOGTAG, "record must have urlSelect child node");
			return false;
		}
		urlSelect = urlSelectNode.getTextTrim();
		
		Element urlValueNode = recordNode.element("urlValue");
		if (urlValueNode == null) {
			// Log.e(LOGTAG, "record must have urlValue child node");
			return false;
		}
		urlValue = urlValueNode.getTextTrim();
		
		Element titleSelectNode = recordNode.element("titleSelect");
		if (titleSelectNode == null) {
			// Log.e(LOGTAG, "record must have titleSelect child node");
			return false;
		}
		titleSelect = titleSelectNode.getTextTrim();
		
		Element descSelectNode = recordNode.element("descSelect");
		if (descSelectNode == null) {
			// Log.e(LOGTAG, "record must have descSelect child node");
			return false;
		}
		descSelect = descSelectNode.getTextTrim();
		
		return true;
	}
	
	private void printStream(InputStream stream) {
		BufferedInputStream bis = null;
		BufferedReader  dis = null;
		// DataInputStream dis = null;
		 
		try {
			// fis = new FileInputStream(file);
 
			bis = new BufferedInputStream(stream);
			dis = new BufferedReader(new InputStreamReader(bis));
			String sCurrentLine;
			while ((sCurrentLine = dis.readLine()) != null)  {
				// Log.i(LOGTAG, sCurrentLine);
				System.out.println(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e(LOGTAG, e.getMessage());
		} finally {
			try {
				// fis.close();
				bis.close();
				dis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				// Log.e(LOGTAG, ex.getMessage());
			}
		}
	}
	
}
