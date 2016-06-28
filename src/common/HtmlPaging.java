package common;

import org.dom4j.Element;
import org.jsoup.nodes.Document;

import java.util.List;

public interface HtmlPaging {
	public boolean processPageNode(Element pageConfigNode) throws HostConfigException;
	public List<String> getPageUrls(Document doc, String charset);
}
