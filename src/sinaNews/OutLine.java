package sinaNews;

import java.util.ArrayList;
import java.util.List;

// <summary>
// 新闻频道列表 
// </summary>
public class OutLine {
	
	// <summary>
    // 频道标题
    // </summary>
    private String title = null ;
    public String getTitle() { return title; }
    public void setTitle(String strTitle) { title = strTitle; }
    
    // <summary>
    // 频道文本
    // </summary>
    private String text = null ;
    public String getText() { return text; }
    public void setText(String strText) { text = strText ;}
    
    // <summary>
    // 频道类型-RSS
    // </summary>
    private String type = null ;
    public String getType() { return type; }
    public void setType(String strType) { type = strType; }
    
    // <summary>
    // XML地址
    // </summary>
    private String xmlUrl = null  ;
    public String getXmlUrl() { return xmlUrl; }
    public void setXmlUrl(String strXmlUrl) { xmlUrl = strXmlUrl ; }
    
    // <summary>
    // HTML地址
    // </summary>
    private String htmlUrl = null ;
    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String strHtmlUrl) { htmlUrl = strHtmlUrl; }

    private List<OutLine> _olChildren = new ArrayList<OutLine>();

    // <summary>
    // 子频道
    // </summary>
    public List<OutLine> childrenOutline()
    {
        return _olChildren;
    }
}
