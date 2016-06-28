package test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		testSpilit();

	}
	
	private static void testSpilit() {
		String test = "div.test";
		String[] tagList = test.split("\\|");
		for (String tag : tagList) {
			System.out.println(tag);
		}
	}
	
	public static void testReg() {
		String aa="aaa[测试]bbbb".replaceAll("(.+)\\[(.+?)\\](.+)", "$1"+"$2"+"$3");
		System.out.println(aa);
		String test = "[测试]";
		if (test.matches("\\[(.+?)\\]"))
		{
			System.out.println("matches");
		}
	}
	
	public static void testUtc()
	{
		// String str = "2009.08.18 0700 UTC " ;
		String str = "Wed, 18 Jul 2012 03:33:46 GMT";
		try{
	        SimpleDateFormat sf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US) ;
	        sf.setTimeZone(TimeZone.getTimeZone("GMT"));
	        Date date = sf.parse(str) ;
	        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
	        // SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        sf1.setTimeZone(TimeZone.getTimeZone("Asia/ShangHai"));
	        System.out.println("---------"+sf.format(date));
	        System.out.println("---------"+sf1.format(date));
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void testUrl(String surl)
	{
		try
		{
			// get the url as a string
			// String surl = "http://www.foshanshop.net/ejb3/ActivePort.exe";
			URL url = new URL(surl);
			URLConnection con = url.openConnection();
			System.out.println("Received a : " + con.getClass().getName());
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/*String msg = "Hi HTTP SERVER! Just a quick hello!";
			con.setRequestProperty("CONTENT_LENGTH", "" + msg.length()); 
			// Not checked
			System.out.println("Msg Length: " + msg.length());
			System.out.println("Getting an output stream...");
			OutputStream os = con.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(msg);
			osw.flush();
			osw.close();
			System.out.println("After flushing output stream. ");
			System.out.println("Getting an input stream...");*/
			InputStream is = con.getInputStream();
			// any response?
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ( (line = br.readLine()) != null)
			{
				System.out.println("line: " + line);
			}
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	private static void testUrlImage(String imgUrl)
	{
		Image image = null;
        try {
            URL url = new URL(imgUrl);
            image = ImageIO.read(url);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        JLabel label = new JLabel(new ImageIcon(image));
        frame.add(label);
        frame.setVisible(true);
	}
	
	
	public static void testString()
	{
		String obj = "h'i";
		
		System.out.println(obj.replaceAll("'", "''"));
	}

}
