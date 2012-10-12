package dbTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class SqliteHelper {
	
	static Logger logger = Logger.getLogger(SqliteHelper.class.getName());
	
	public static boolean saveToDB(String dbFile, String sql)
	{
		boolean isOK = false;
		
		try {
			Class.forName("org.sqlite.JDBC");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isOK;
		}
		
		Connection connection = null;
		try
	    {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			statement.executeUpdate(sql);
			isOK = true;
	    }catch(SQLException e)
	    {
	    	e.printStackTrace();
	    	logger.error(e.getMessage());
	    	logger.error(sql);
	    	return isOK;
	    }
	    finally
	    {
	      try
	      {
	    	 if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	    	  e.printStackTrace();
	    	  logger.error(e.getMessage());
	    	  return isOK;
	      }
	    }
		
		return isOK;
	}
	
	// return first column to caller
	public static String queryToDbRetFirst(String dbFile, String sql) {
		String ret  = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ret;
		}
		
		Connection connection = null;
		try
	    {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next())
			{
				ret = rs.getString(1);
				break;
			}
			
	    }catch(SQLException e)
	    {
	    	e.printStackTrace();
	    	logger.error(e.getMessage());
	    	logger.error(sql);
	    	return ret;
	    }
	    finally
	    {
	      try
	      {
	    	 if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	    	  e.printStackTrace();
	    	  logger.error(e.getMessage());
	    	  return ret;
	      }
	    }
		
		return ret;
	}

}
