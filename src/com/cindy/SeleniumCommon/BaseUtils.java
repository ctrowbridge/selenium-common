package com.cindy.SeleniumCommon;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Contains some utilities that don't depend on the driver.
 * 
 * @author Cindy
 */
public class BaseUtils {

	/**
	 * Checks to make sure URL exists
	 * 
	 * @param url URL to check
	 * @return Returns true if URL exists, FALSE otherwise
	 */
	public static boolean checkURL(String url) {

		try {
			URL url1 = new URL(url);
			String response = "";
			HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
			connection.connect();
			response = connection.getResponseMessage();
			connection.disconnect();
			if (response.equals("OK")) {
				return true;
			} else {
				System.out.println("response = " + response);
				return false;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
