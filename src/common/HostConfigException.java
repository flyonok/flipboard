package common;

public class HostConfigException extends Exception {
	public HostConfigException(String Message) {
		super(Message);
	}
	
	public HostConfigException(String Message, Throwable throwable) {
		super(Message, throwable);
	}
}
