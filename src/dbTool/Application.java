package dbTool;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


// Novel
public class Application {
	
	static Logger logger = Logger.getLogger(Application.class.getName());
	private String description = null;
	public String getDescription() { return description; }
	public void setDecription(String strDescription) { description = strDescription; }
	
	private int _id = 0;
	public int getID() { return _id; }
	
	private String dbFile = null;
	public void setDbFile(String file) {
		URL url = Application.class.getResource(file);
		dbFile = url.getFile();
	}
	
	public void saveApplication() throws SqliteException	{
		if (dbFile == null) {
			throw new SqliteException("Application sqlite db file is empty!please set it first!");
		}
		if (isApplicationExist())
			return ;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date);
		String sql = "insert into Novel(novelTitle, year) values('" + description + "','"+ strDate + "')";
		if ( !SqliteHelper.saveToDB(dbFile, sql) ) {
			logger.error("save novel content failed!");
			return;
		}
		sql = "select max(_id) as max_id from novel";
		// sql = "select _id from Novel where novelTitle='" + description + "'";
		String ret = SqliteHelper.queryToDbRetFirst(dbFile, sql);
		if (ret != null) {
			try {
				_id = Integer.parseInt(ret);
				logger.debug("novel id:" + _id);
			} catch (NumberFormatException e){
				logger.error(e.getMessage());
				logger.error("query error : " + sql);
				e.printStackTrace();
			}
		} else {
			logger.error("execute sql: " + sql + "failed!");
		}
	}
	
	private boolean isApplicationExist() throws SqliteException {
		boolean isExist = false;
		if (dbFile == null) {
			throw new SqliteException("Application sqlite db file is empty!please set it first!");
		}
		String sql = "select _id from Novel where novelTitle='" + description + "'";
		String ret = SqliteHelper.queryToDbRetFirst(dbFile, sql);
		if (ret != null) {
			try {
				_id = Integer.parseInt(ret);
				logger.debug("novel id:" + _id);
				isExist = true;
			} catch (NumberFormatException e){
				logger.error(e.getMessage());
				// logger.error("query error : " + sql);
				e.printStackTrace();
			}
		}
		return isExist;
	}
	
	private void getIdFromDb() {
		
	}
}
