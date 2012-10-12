package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class CommonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonTest test = new CommonTest();
		test.copyDb("e:\\private\\Flipboard.docx", "e:\\private\\test.docx");
	}
	
	// image
		private void testImage(String strUrl) {
			try {
				URL imgUrl = new URL(strUrl);
				BufferedImage bufImage = ImageIO.read(imgUrl);
				System.out.println("height: " + bufImage.getHeight());
				System.out.println("width: " + bufImage.getWidth());
				
			}catch (MalformedURLException e) {
				
				e.printStackTrace();
				System.err.println(e.getMessage());
			}catch (IOException e) {
				
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		
		private void testDirection(String url)
		{
			try {
				URL test = new URL(url);
				HttpURLConnection conn=(HttpURLConnection)test.openConnection();
				System.out.println("HTTP响应代码" + conn.getResponseCode());
				String realUrl=conn.getURL().toString();
				conn.disconnect();
				System.out.println("真实URL:"+realUrl);
				
			} catch (MalformedURLException e ) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
		}
		
		// test function 
		private void copyDb(String oriDb,String dstDb)
		{
			File oriFile= new File(oriDb);
			if (!oriFile.exists()) {
				System.err.println("file:"+ oriDb + " not exists!");
				return;
			}
			File dstFile = new File(dstDb);
			if(dstFile.exists()) {
				if(!dstFile.delete()) {
					System.err.println("delete file:" + dstDb + " failed!");
					return;
				}
			}
			try {
				if (!dstFile.createNewFile()) {
					System.err.println("create file:" + dstDb + " failed!");
					return;
				}
			} catch(IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
				return;
			}
			
			InputStream inStream = null;
			OutputStream outStream = null;
			
			try {
				inStream = new FileInputStream(oriFile);
				outStream = new FileOutputStream(dstFile);
				byte[] buffer = new byte[1024];
				int length;
			// copy the file content in bytes
				while ((length = inStream.read(buffer)) > 0) {

					outStream.write(buffer, 0, length);

				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}catch (IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}finally {
				try {
					if (inStream != null)
						inStream.close();
					if (outStream != null) {
						outStream.close();
					}
				}catch (IOException e) {
					e.printStackTrace();
					System.err.println(e.getMessage());
				}
			}
			
		}

		// test function end


}
