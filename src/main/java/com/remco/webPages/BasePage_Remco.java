package com.remco.webPages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		browserName=System.getProperty("Dbrowser",getPropValue("browser"));
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
	public void expectedToBeFalse(boolean value,String message) throws Exception 
	{
		if(value==true) {
			throw new Exception(message);
		}
	}
	public void expectedToBeTrue(boolean value,String message) throws Exception 
	{
		if(value==false) {
			throw new Exception(message);
		}
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
	public void connectToDB() throws Exception {
		String servername,dbName;
		servername=(System.getProperty("db_serverName")!=null)? System.getProperty("db_serverName"):getPropValue("db_servername");
		dbName=(System.getProperty("db_Name")!=null)? System.getProperty("db_Name"):getPropValue("db_name");
		 // JDBC URL for SQL Server with Windows authentication
        //String jdbcUrl = "jdbc:sqlserver://"+servername+":1433;databaseName="+dbName+";integratedSecurity=true";
		String jdbcUrl = "jdbc:sqlserver://"+servername+":1433;user=HJS;password=HJSPASS#1A;database="+dbName;
        
        // Establishing the connection
        try{
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        	Connection connection = DriverManager.getConnection(jdbcUrl);
        	if (connection != null) {
        		  Statement stmt=connection.createStatement();
                  ResultSet resultSet=stmt.executeQuery("SELECT top 1order_number from t_pick_detail where order_number like 'MIL.LD%'");
                  while (resultSet.next()) {
                      // Accessing data from the result set
                      String orderNumber = resultSet.getString("order_number");                 
                      // Process retrieved data, for example:
                      System.err.println("order_number: " + orderNumber);
                  }               }
        	else {
                System.out.println("Failed to make connection!");}
        	} 
        catch (SQLException e) {
            System.err.println("Connection failed! Error message: " + e.getMessage());
            }
        }

}
