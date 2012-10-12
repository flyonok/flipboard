package dbTool;

public class SqliteException extends Exception {
	
	public SqliteException(String Message) {
		super(Message);
	}
	
	public SqliteException(String Message, Throwable throwable) {
		super(Message, throwable);
	}
}
