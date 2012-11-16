package dbTool;

import java.net.URL;
import java.sql.SQLException;

import org.apache.log4j.Logger;

// season--栏目
public class PartManager {
	
	static Logger logger = Logger.getLogger(PartManager.class.getName());
	
	private String title = null;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	private int novel_id = 0;
	public int getNovelID() { return novel_id; }
	public void setNovelId(int id) { novel_id = id; }
	
	private int _id = 0;
	public int getId() { return _id; }
	
	// for config
	private String contentSelector = null;
	public void setContentSelector(String content) { contentSelector = content; }
	public String getContentSelector() { return contentSelector; }
	
	private String type = null;
	public void setType(String type) { this.type = type; }
	public String getType() { return type; }
	// for config
	
	
	private String dbFile = null;
	public void setDbFile(String file) {
		URL url = PartManager.class.getResource(file);
		dbFile = url.getFile();
	}
	
	public void savePart() throws SqliteException, SQLException	{
		if (dbFile == null) {
			throw new SqliteException("PartManager sqlite db file is empty!please set it first!");
		}
		if (isPartExist())
			return;
		String sql = "insert into Season(seasonTitle, novelId) values('" + title + "'," + novel_id + ")" ;
		if ( !SqliteHelper.saveToDB(dbFile, sql) ) {
			logger.error("save season content failed!");
			return;
		}
		sql = "select max(_id) from Season";
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
	
	private boolean isPartExist() throws SqliteException, SQLException {
		boolean isExist = false;
		if (dbFile == null) {
			throw new SqliteException("PartManager sqlite db file is empty! please set it first!");
		}
		String sql = "select _id from Season where seasonTitle = '" + title + "' and novelId = " + novel_id;
		String ret = SqliteHelper.queryToDbRetFirst(dbFile, sql);
		if (ret != null) {
			try {
				_id = Integer.parseInt(ret);
				isExist = true;
			} catch (NumberFormatException e){
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return isExist;
	}
	
	
}
