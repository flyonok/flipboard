package common;

import java.util.List;

import org.dom4j.Element;
import org.jsoup.nodes.Document;

public interface HtmlPaging {
	public boolean processPageNode(Element pageConfigNode) throws HostConfigException;
	public List<String> getPageUrls(Document doc);
}
