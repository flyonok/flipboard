package dbTool;

import org.apache.log4j.Logger;

import java.sql.*;

public class MysqlHelper {
	
static Logger logger = Logger.getLogger("newedu.webcrawl.hexun");
	
	public static boolean saveToDB(String dbFile, String sql) throws SQLException
	{
		boolean isOK = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return isOK;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return isOK;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return isOK;
		}
		
		Connection connection = null;
		try
	    {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:mysql://192.168.1.2/newsCrawl?" +
                    "user=newedu&password=cs2506");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			// statement.execute("SET NAMES utf8");
			statement.executeUpdate(sql);
			isOK = true;
	    }catch(SQLException e)
	    {
	    	e.printStackTrace();
	    	logger.error("SQLException: " + e.getMessage());
	    	logger.error("SQLState: " + e.getSQLState());
	    	logger.error("VendorError: " + e.getErrorCode());
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
	    	  logger.error("SQLException: " + e.getMessage());
	    	  logger.error("SQLState: " + e.getSQLState());
		      logger.error("VendorError: " + e.getErrorCode());
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
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ret;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return ret;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return ret;
		}
		
		Connection connection = null;
		try
	    {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:mysql://192.168.1.2/newsCrawl?" +
                    "user=newedu&password=cs2506");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			// statement.execute("SET NAMES utf8");
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next())
			{
				ret = rs.getString(1);
				break;
			}
			
	    }catch(SQLException e)
	    {
	    	e.printStackTrace();
	    	logger.error("SQLException: " + e.getMessage());
	    	logger.error("SQLState: " + e.getSQLState());
		    logger.error("VendorError: " + e.getErrorCode());
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
	    	  logger.error("SQLException: " + e.getMessage());
	    	  logger.error("SQLState: " + e.getSQLState());
		      logger.error("VendorError: " + e.getErrorCode());
	    	  throw e;
	    	  // return ret;
	      }
	    }
		
		return ret;
	}

}
