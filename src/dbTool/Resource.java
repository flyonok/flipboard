package dbTool;

import java.net.URL;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class Resource {
	
	public static final String STRING = "string" ; // 字符串文本资源
	
	public static final String TEXTFILE = "textFile" ; // 资源是一个加密的文本文件
	
	public static final String IMAGE = "image" ; // 资源是一个图像
	
	public static final String AUDIO = "audio" ; // 资源是一个mp3声音文件
	
	public static final String VIDEO = "video" ; // 资源是一个视频文件
	
	public static final String CONTROL = "control" ;// 资源是一个交互式控制
	
	public static final String IMGURL = "imgUrl" ; // 资源是一个图像网址
	
	public static final String VIDEOURL = "videoUrl" ; // 资源是一个视频网址
	
	public static final String AUDIOURL = "audioUrl" ; // 资源是一个声音网址
	
	public static final String WEBURL = "webUrl" ; // 资源是一个普通网页
	
	static Logger logger = Logger.getLogger(Resource.class.getName());
	
	// must comment
	private String title;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	private String url;
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
	// comment end
	
	private int Width;
	public int getWidth() { return Width; }
	public void setWidth(int width) { this.Width = width;}
	
	private int Height;
	public int getHeight() { return Height; }
	public void setHeitht(int height) { this.Height = height; }
	
	// must comment latter
	private int type = 1;
	public int getType() { return type; }
	public void setType(int type) { this.type = type;}
	// comment end
	
	private String resType = STRING;
	public String getResType() { return resType; }
	public void setResType(String type) { resType = type; }
	
	private String resContent = null;
	public String getResContent() { return resContent; }
	public void setResContent(String content) { resContent = content; }
	
	private String resText = null ;
	public String getResText() { return resText; }
	public void setResText(String text) { resText = text ; }
	
	private int _id = 0;
	public int getResouceId() { return _id; }
	
	private String dbFile = null;
	public void setDbFile(String file) {
		URL url = Resource.class.getResource(file);
		dbFile = url.getFile();
	}
	
	public void saveResouce() throws SqliteException	{
		if (dbFile == null) {
			throw new SqliteException("Resouce sqlite db file is empty!please set it first!");
		}
		String sql = "insert into Resource(resType, resContent, resText, width, height) " +
				"values('" + resType + "','" + resContent + "','" + resText.replaceAll("'", "''") + "'," 
				+ Width + "," + Height+ ")" ;
		if ( !SqliteHelper.saveToDB(dbFile, sql) ) {
			logger.error("save Resource failed!");
			return;
		}
		sql = "select max(_id) from Resource";
		String ret = SqliteHelper.queryToDbRetFirst(dbFile, sql);
		if (ret != null) {
			try {
				_id = Integer.parseInt(ret);
			} catch (NumberFormatException e){
				logger.error(e.getMessage());
				logger.error("query error : " + sql);
				e.printStackTrace();
			}
		}
	}
	
	
	
}