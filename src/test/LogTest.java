package test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strFile = LogTest.class.getResource("/log4.properties").getFile();
		System.out.println(strFile);
		PropertyConfigurator.configure(strFile);
		logger.info("Enter application!");
		logger.info("As the logger com.foo.Bar does not have an assigned level, it inherits its level from com.foo, which was set to WARN in the configuration file. The log statement from the Bar.doIt method has the level DEBUG, lower than the logger level WARN. Consequently, doIt() method's log request is suppressed.");
		Bar bar = new Bar();
		bar.doIt();
		logger.info("Exit application!");
		/*Logger logger  =  Logger.getLogger(LogTest.class.getName() );
        logger.debug( " debug " );
        logger.error( " error " );*/

	}
	
	static Logger logger = Logger.getLogger(LogTest.class.getName());

}


class Bar {
	static Logger logger = Logger.getLogger(Bar.class);
	
	public void doIt() {
		logger.debug("Did it again!");
	}
}
