package dbTool;

import org.apache.log4j.Logger;

import java.sql.*;

public class SqliteHelper {
	
	static Logger logger = Logger.getLogger("newedu.webcrawl.hexun");
	
	public static boolean saveToDB(String dbFile, String sql) throws SQLException
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
	    	throw e;
	    	// return isOK;
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
	    	  throw e;
	    	  // return isOK;
	      }
	    }
		
		return isOK;
	}
	
	// return first column to caller
	public static String queryToDbRetFirst(String dbFile, String sql) throws SQLException {
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
	    	// return ret;
	    	throw e;
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
	    	  throw e;
	    	  // return ret;
	      }
	    }
		
		return ret;
	}

}
