package com.remco.webPages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ColorScheme;

public class BasePage_Remco{
	protected Page page;
	String configProp;
	Browser browser;
	BrowserContext	context;
	
	public void launchBrowser() throws Exception {
		Playwright 	playwright	= Playwright.create();

		String browserName;
		browserName=(System.getProperty("browser")!=null)? System.getProperty("browser"):getPropValue("browser");
		boolean isHeadLess = Boolean.parseBoolean(getPropValue("headless"));

		switch(browserName) {
		case "chrome":
			browser		= playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(isHeadLess));
			break;
		case "chromium":
			browser		= playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadLess));
			break;
		case "firefox":
			browser		= playwright.firefox().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(isHeadLess));
			break;
		default:
			throw new Exception("No case statement fo to run the scriptss on "+browserName+" browser");
		}
		context	= browser.newContext(new Browser.NewContextOptions().setColorScheme(ColorScheme.DARK));
		page	= context.newPage();
	}
	
	public void loadHJUrl() throws Exception {
		String appUrl;
		appUrl=(System.getProperty("hjurl")!=null)? System.getProperty("hjurl"):getPropValue("hjurl");
		page.navigate(appUrl);
	}
	
	public void loadRFUrl() throws Exception {
		String appUrl;
		appUrl=(System.getProperty("rf_Url")!=null)? System.getProperty("rf_Url"):getPropValue("rf_url");
		page.navigate(appUrl);
	}
	
	
	public void sendKeysToTheInputField(String locator,String value)
	{
		page.locator(locator).type(value);
	}
	public void clickOn(String locator)
	{
		page.locator(locator).click();
	}
	

	public void tearDown() {
		page.close();
		browser.close();
	}
	
	public String getPropValue(String inptValue) throws Exception {
		Properties config	= new Properties();
		String	filePath="src/test/resources/configurations/TestConfig.properties";
		try {
			FileInputStream file_inptStream	=	new FileInputStream(filePath);
			config.load(file_inptStream);
			configProp	=	config.getProperty(inptValue);
			}
		catch(FileNotFoundException e) {
			throw new Exception("TestConfig.properties files is missing at "+filePath);
			}
		return configProp;
		}

}
