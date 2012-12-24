package dbTool;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

// chapter
public class PageManager {
	static Logger logger = Logger.getLogger("newedu.webcrawl.hexun");
	
	private String title = null;
	public void setTitle(String title) { this.title = title; }
	public String getTitle() { return title; }
	
	private int season_id = 0;
	public int getSeasonID() { return season_id; }
	public void setSeasonID(int id) { season_id = id; }
	
	private int _id = 0;
	public int getID() { return _id; }
	
	private int template_id = 0;
	public int getTemplateID() { return template_id; }
	public void setTemplateID(int id) { template_id = id; }
	
	private List<Integer> article_list = null;
	public void addArticle(int id) {
		if (article_list == null) {
			article_list = new ArrayList<Integer>();
		}
		article_list.add(id);
	}
	
	private String dbFile = null;
	public void setDbFile(String file) {
		URL url = PageManager.class.getResource(file);
		dbFile = url.getFile();
	}
	
	public void savePage() throws SqliteException, SQLException	{
		if (dbFile == null) {
			throw new SqliteException("PartManager sqlite db file is empty!please set it first!");
		}
		Gson gson = new Gson();
		String strArcJson = gson.toJson(article_list);
		String sql = "insert into Chapter(chapterTitle, seasonId, templateId, articleList) " +
				"values('" + title + "'," + season_id + "," + template_id + ",'" + strArcJson +"')" ;
		/*if ( !SqliteHelper.saveToDB(dbFile, sql) ) {*/
		if ( !MysqlHelper.saveToDB(dbFile, sql) ) {
			logger.error("save season content failed!");
			return;
		}
		sql = "select max(_id) from Chapter";
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
