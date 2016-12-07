package com.cindy.SeleniumCommon;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.testng.Assert;

/**
 * Implements a base object for all Page Objects for Selenium testing.
 * 
 * @author Cindy
 */
public abstract class BasePage {

	protected final WebDriver driver;
	protected final int defaultTimeout = 30;

	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * Checks to see if a particular element is present on the current web page.
	 * 
	 * @param selector
	 * @return true if element is present, false if element is not present
	 */
	protected boolean isElementPresent(By selector) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		boolean returnVal = true;
		try {
			driver.findElement(selector);
		} catch (NoSuchElementException e) {
			returnVal = false;
		} finally {
			driver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
		}
		return returnVal;
	}
	
	/**
	 * Check to see if a particular element is present as a child of the given
	 * element.
	 * 
	 * @param inElement Parent element to search
	 * @param selector
	 * @return true if element is present, false if element is not present
	 */
	protected boolean isElementPresent(WebElement inElement, By selector) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		boolean returnVal = true;
		try {
			inElement.findElement(selector);
		} catch (NoSuchElementException e) {
			returnVal = false;
		} finally {
			driver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
		}
		return returnVal;
	}
	
	/**
	 * Shows all links on the current web page.
	 */
	public void showAllLinks() {
		System.out.println("\n*** showAllLinks: " + this.getClass().getName());
		List<WebElement> links = driver.findElements(By.tagName("a"));

		System.out.println("*** links.size() = " + links.size());

		int i = 0;
		for (WebElement link : links) {
			System.out.println("*** Link " + i + " / \"" + link.getText() + "\" / <" + link.getTagName() + ">");
			i++;
		}
	}

	/**
	 * Shows all elements on the current web page.
	 */
	public void showAllElements() {
		System.out.println("\n*** showAllElements: " + this.getClass().getName());
		WebElement body = driver.findElement(By.tagName("body"));
		List<WebElement> children = body.findElements(By.xpath(".//*"));
		System.out.println("\n*** showAllElements: " + children.size());
		int i = 0;
		for (WebElement link : children) {
			System.out.println("*** Element " + i + " / \"" + link.getText() + "\" / <" + link.getTagName() + "> {"
					+ link.getAttribute("id") + "}");
			i++;
		}
	}

	/**
	 * Highlights the given element.
	 * 
	 * Source:
	 * http://www.testingdiaries.com/highlight-element-using-selenium-webdriver/
	 * 
	 * @param element
	 */
	public void elementHighlight(WebElement element) {
		for (int i = 0; i < 2; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: red; border: 3px solid red;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}
	}

	/**
	 * Waits for a particular element to be visible
	 * 
	 * @param by
	 * @throws InterruptedException
	 */
	public void waitForElement(By by) throws InterruptedException {
		for (int second = 0;; second++) {
			if (second >= 60)
				Assert.fail("timeout");
			try {
				WebElement element = driver.findElement(by);
				break;

			} catch (Exception e) {
			}
			Thread.sleep(1000);
			System.out.println(" Try again " + second + " " + by);
		}
	}
	
	/**
	 * Takes a screenshot of the current page and saves it to a file.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void takeScreenshot(String fileName) throws IOException {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File(fileName));
	}
}
