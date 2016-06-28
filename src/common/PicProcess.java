package common;

// import org.dom4j.Document;

import dbTool.Article;
import org.apache.http.client.ClientProtocolException;
import org.dom4j.Element;

import java.io.IOException;

public interface PicProcess {
	public boolean processPicNode(Element picConfigNode) throws HostConfigException;
	public boolean processHtml(org.jsoup.nodes.Document doc, Article article ) 
			throws NumberFormatException, IOException, ClientProtocolException ;
}
