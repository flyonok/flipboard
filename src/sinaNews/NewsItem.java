package sinaNews;

//	<summary>
//	新闻记录实体
//	</summary>
public class NewsItem {
	
	// <summary>
    // 新闻标题
    // </summary>
	private String title = null;
	
	public String getTitle(){ return title;	}
	
	public void setTitle(String strTitle) { title = strTitle; }
	
	// <summary>
    // 新闻链接
    // </summary>
    private String link = null;
    public String getLink() { return link; }
    public void setLink(String strLink) { link = strLink; }
    
    // <summary>
    // 作者
    // </summary>
    private String author = null;
    public String getAuthor() { return author; }
    public void setAuthor(String strAuthor) { author = strAuthor; }
    
     // <summary>
    // 分类
    // </summary>
    private String category = null;
    public String getCategory() { return category; }
    public void setCategory(String strCategory) { category = strCategory; }
    
    // <summary>
    // 发布时间
    // </summary>
    private String pubDate = null;
    public String getPubDate() { return pubDate; }
    public void setPubDate(String time) { pubDate = time; }
    
    // <summary>
    // 描述
    // </summary>
    private String description = null ;
    public String getDescription() { return description; }
    public void setDescription(String strDesc) { description = strDesc; }
    
    // <summary>
    // 其它说明
    // </summary>
    private String comments = null ;
    public String getComments() { return comments; }
    public void setComments(String strComm) { comments = strComm; }    
    
}
