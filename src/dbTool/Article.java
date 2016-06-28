package dbTool;

import com.google.gson.Gson;
import common.ResJson;
import org.apache.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



public class Article {
	
	// html content type 
	public static final int LITERAL = 1; // 纯文字
	public static final int ONE_HVHD_RES = 2; // 包含1张横版高清图片的 – 简称"横版高清"
	public static final int ONE_VVHD_RES = 3; // 包含1张竖版高清图片的 – 简称"竖版高清
	public static final int ONE_VIDEO_RES = 4; // 只有一个视频的 – 简称"视频"
	public static final int OVER_THREE_HV_RES = 5; // 超过3个横版图片的 – 简称"多个横版小图"
	public static final int OVER_THREE_VV_RES = 6; // 超过3个竖版图片的 – 简称"多个竖版小图"
	public static final int OVER_TWO_HVHD_RES = 7; // 包含2张以上横版高清图片的 – 简称"多张横版高清"
	public static final int OVER_TWO_VVHD_RES = 8; // 包含2张以上竖版高清图片的 – 简称"多张竖版高清"
	// 包含至少一张横版和至少一张竖版高清图片的 – 简称"横竖高清"
	public static final int OVER_ONE_VVHD_AND_OVER_ONE_HVHD_RES = 9;
	public static final int ALLRESOURCE = 10; // 以图片为主的 – 简称"图片集"
	// html content type end
	
	// theTitle
	private String title = "";
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	// must comment
	private String Abstract = "";
	public String getAbstract() { return Abstract; }
	public void setAbstract(String Abstract) { this.Abstract = Abstract; }
	// comment end
	
	// content
	private String Html = "";
	public String getHtml() { return Html; }
	public void setHtml(String Html) { this.Html = Html; }
	
	private StringBuilder mediaBuf = new StringBuilder(); // media list
	
	/*// must comment
	private int mediaIcon = 0;
	
	private int Type = 1;
	public int getType() { return Type; }
	public void setType(int type) { this.Type = type; }
	// comment end
*/	
	// original html url address
	private String orgURL = "";
	public String getOrgUrl() { return orgURL; }
	public void setOrgUrl(String url) { orgURL = url; }
	
	/*// must comment
	private long time = 0;
	public long getTime() { return time; }
	public void setTime(long time) { this.time = time; }
	// comment end
*/	
	private int arcType = LITERAL;
	public int getArcType() { return arcType; }
	public void setArcType(int type) { arcType = type; }
	
	private String artTime = null;
	public String getArtTime() { return artTime; }
	public void SetArtTime(String time) { artTime = time ; }
	
	private String artSource = null;
	public String getArtSource() { return artSource; }
	public void setArtSource(String source) { artSource = source; }
	
	private int resCnt = 0; // 记录资源个数
	public int getResCnt() { return resCnt; }
	
	private void decideArcType() {
		if (arcType == ALLRESOURCE) 
			return ;
		if (mediaList.isEmpty()) {
			arcType = LITERAL;
			return ;
		}
		if (mediaList.size() == 1) {
			arcType = ONE_HVHD_RES;
			return ;
		}
		if (mediaList.size() > 1) {
			// arcType = ALLRESOURCE;
			arcType = OVER_THREE_HV_RES;
			return;
		}
	}	
	
	public boolean containsText() { 
		// decideArcType();
		if ( (arcType == LITERAL )  || (arcType == ONE_HVHD_RES) || (arcType == ONE_VVHD_RES) )
			return true;
		else
			return false;
	}
	
	private int season_id = 0;
	public int getSeasonId() { return season_id; }
	public void setSeasonId(int id) { season_id = id; }
	
	private int _id = 0;
	public int getArticleId( ) { return _id; }
	
	private List<Resource> mediaList = new LinkedList<Resource>();
	public void addMedia(Resource media) {
		resCnt++;
		mediaList.add(media); 
		}
	
	private ResJson json = new ResJson();
	
	// db file name
	private static String dbFile = null;
	public void setDbFile(String file) {
		URL url = PageManager.class.getResource(file);
		dbFile = url.getFile();
	}
	
	// logger
	static Logger logger = Logger.getLogger("newedu.webcrawl.hexun");
	
	
	public static boolean isContentExist(String url, String sqlDbFile /*/xxtebook.db */) throws SQLException {
		/*System.out.println("title: " + title);
		System.out.println("abstract: " + Abstract);
		System.out.println("html: " + Html);*/
		// URL url = this.getClass().getResource("/db.db");
		// String dbFile=url.getFile();
		// boolean isIcon = true;
		URL dburl = Article.class.getResource(sqlDbFile);
		String fullDbFile = dburl.getFile();
		boolean isExist = false;
		String sql = "select _id from Article where oriUrl = '" + url + "'";
		String ret = SqliteHelper.queryToDbRetFirst(fullDbFile, sql);
		/*String ret = MysqlHelper.queryToDbRetFirst(fullDbFile, sql);*/
		if (ret != null) {
			try {
				int id = Integer.parseInt(ret);
				isExist = true;
			} catch (NumberFormatException e){
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return isExist;
	}
	
	public void saveArticle() throws SqliteException, SQLException	{
		if (dbFile == null) {
			throw new SqliteException("Resouce sqlite db file is empty! please set it first!");
		}
		for (Resource res : mediaList) {
			// res.setDbFile(dbFile);
			res.saveResouce();
			if ( (res.getResType()== Resource.IMAGE) ||(res.getResType()== Resource.IMGURL) ) {
				json.addPicture(res.getResouceId(), res.getWidth(), res.getHeight());
			}
		}
		decideArcType();
		String sql = null;
		Gson gson = new Gson();
		String jsonRes = gson.toJson(json);
		if (!jsonRes.equals("{}")) {
			/*jsonRes = jsonRes.replaceAll("\\[", "\\{");
			jsonRes = jsonRes.replaceAll("\\]", "\\}");*/
			sql = "insert into Article(theTitle, content, resList, oriUrl, artType, seasonId, artTime, artSource) " +
					"values('" + title.replaceAll("'", "''") + "','" + Html.replaceAll("'", "''")
					+ "','" + jsonRes + "','" + orgURL + "',"
					+ arcType + "," + season_id + ",'" + artTime + "','" + artSource +"')" ;
		}
		else {
			sql = "insert into Article(theTitle, content, oriUrl, artType, seasonId, artTime, artSource) " +
					"values('" + title.replaceAll("'", "''") + "','" + Html.replaceAll("'", "''")
					+ "','" + orgURL + "',"
					+ arcType + "," + season_id+ ",'" + artTime + "','" + artSource +"')" ;
		}
		
		
		if ( !SqliteHelper.saveToDB(dbFile, sql) ) {
		/*if ( !MysqlHelper.saveToDB(dbFile, sql) ) {*/
			logger.error("save season content failed!");
			return;
		}
		sql = "select max(_id) from Article";
		String ret = SqliteHelper.queryToDbRetFirst(dbFile, sql);
		/*String ret = MysqlHelper.queryToDbRetFirst(dbFile, sql);*/
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
