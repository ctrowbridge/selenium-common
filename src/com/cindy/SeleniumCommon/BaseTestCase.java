package com.cindy.SeleniumCommon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Implements a base test case for Selenium testing.
 * 
 * @author Cindy Trowbridge
 *
 */
public class BaseTestCase {

	protected WebDriver driver;
	protected int timeout;
	protected String httpProxy = "";
	protected String sslProxy = "";
	protected String ftpProxy = "";
	protected String proxyUserName = "";
	protected String proxyPassword = "";

	public enum DriverType {
		FIREFOX, IE, CHROME
	};

	protected PrintStream logFile;

	public BaseTestCase() {

	}

	/**
	 * Create the WebDriver
	 * 
	 * @param type
	 *            What type of driver to create
	 * @param timeout
	 *            Timeout [seconds]
	 */
	public void createDriver(DriverType type, int timeout) {

		DesiredCapabilities capabilities;
		String userProfile = System.getenv("USERPROFILE");

		System.out.println("BaseTestCase::createDriver:  creating driver ...");
		this.timeout = timeout;
		File file;

		switch (type) {
		case IE:
			file = new File(userProfile + "/Documents/Selenium/IEDriverServer_Win32_3.4.0/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

			capabilities = DesiredCapabilities.internetExplorer();
			addProxyCapabilities(capabilities);

			driver = new InternetExplorerDriver(capabilities);
			break;

		case CHROME:
			file = new File(userProfile + "/Documents/Selenium/chromedriver_win32_2_38/chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

			capabilities = DesiredCapabilities.chrome();
			//addProxyCapabilities(capabilities);
			Proxy proxy = getProxyOptions();
			capabilities.setCapability(CapabilityType.PROXY, proxy);
			
			ChromeOptions options = new ChromeOptions();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			options.addArguments("chrome.switches", "--disable-extensions");
			options.addArguments("chrome.switches", "--disable-spdy-proxy-dev-auth-origin");

			driver = new ChromeDriver(options);
			break;

		case FIREFOX:
			System.setProperty("webdriver.gecko.driver",
					userProfile + "\\Documents\\Selenium\\geckodriver-v0.19.0-win32\\geckodriver.exe");

			capabilities = DesiredCapabilities.firefox();
			addProxyCapabilities(capabilities);
			capabilities.setCapability("marionette", true);

			driver = new FirefoxDriver(capabilities);
			break;
		}

		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(1600, 1100));

		System.out.println("BaseTestCase::createDriver:  done, driver = " + driver);
	}

	/**
	 * Opens a file used to log testing.
	 * 
	 * @param name
	 *            Name of test
	 * @throws FileNotFoundException
	 */
	public void createLog(String name) throws FileNotFoundException {
		logFile = new PrintStream(name + "_log.csv");
		logFile.println("Area Tested,Steps Performed,Expected Results,Actual Results,Pass/Fail,Bug #");
	}

	public void closeLog() {
		logFile.close();
	}

	/**
	 * Adds something to log. TODO Implement method
	 */
	public void log() {

	}

	/**
	 * Shuts down the driver.
	 */
	public void shutdown() {
		driver.quit();
	}

	/**
	 * Sets proxy parameters
	 * 
	 * @param httpProxy
	 * @param sslProxy
	 * @param ftpProxy
	 */
	protected void useProxy(String httpProxy, String sslProxy, String ftpProxy) {
		this.httpProxy = httpProxy;
		this.sslProxy = sslProxy;
		this.ftpProxy = ftpProxy;
	}
	
	protected void setProxyLogin(String name, String password) {
		proxyUserName = name;
		proxyPassword = password;
	}

	/**
	 * Add proxy parameters to capabilities list
	 * 
	 * @param capability
	 *            Current list of capabilities
	 * @return Updated listed of capabilities
	 */
	private DesiredCapabilities addProxyCapabilities(DesiredCapabilities capability) {

		if (httpProxy != "" || sslProxy != "" || ftpProxy != "") {
			Proxy proxy = new Proxy();
			proxy.setProxyType(ProxyType.MANUAL);
			if (httpProxy != "") {
				proxy.setHttpProxy(httpProxy);
			}
			if (sslProxy != "") {
				proxy.setSslProxy(sslProxy);
			}
			if (ftpProxy != "") {
				proxy.setFtpProxy(ftpProxy);
			}

			capability.setCapability(CapabilityType.PROXY, proxy);
			capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		}
		return capability;
	}

	private Proxy getProxyOptions() {
		
		if (httpProxy != "" || sslProxy != "" || ftpProxy != "") {

			Proxy proxy = new org.openqa.selenium.Proxy();
			proxy.setSslProxy(sslProxy);
			proxy.setFtpProxy(ftpProxy);
			proxy.setHttpProxy(httpProxy);
			proxy.setSocksUsername(proxyUserName);
			proxy.setSocksPassword(proxyPassword);

			return null;
		}

		return null;
	}
}
