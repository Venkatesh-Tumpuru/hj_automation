package com.remco.webPages;

import com.microsoft.playwright.options.SelectOption;
import com.remco.ElementLocators.HomePageLocators;

public class RFHomePage extends BasePage_Remco implements HomePageLocators {
	public void fillRFServerDetails() throws Exception {
		String servername,portnumber,devicename,screensize;
		servername	=(System.getProperty("rf_serverName"))!=null?System.getProperty("rf_serverName"):getPropValue("rf_servername");
		portnumber	=(System.getProperty("rf_portNumber"))!=null?System.getProperty("rf_portNumber"):getPropValue("rf_portnumber");
		devicename	=(System.getProperty("rf_deviceName"))!=null?System.getProperty("rf_deviceName"):getPropValue("rf_devicename");
		screensize	=(System.getProperty("rf_screenSize"))!=null?System.getProperty("rf_screenSize"):getPropValue("rf_screensize");
	
		page.locator(rf_serverName_inpt).type(servername);
		page.locator(rf_portNumber_inpt).type(portnumber);
		page.locator(rf_deviceName_inpt).type(devicename);
		page.selectOption(rf_screenSize_inpt,  new SelectOption[] { new SelectOption().setValue(screensize)});
		page.locator(rf_submit_btn).click();
		
		//check for Terminal In Use
		String terminalInUseMsg		=page.querySelector(rf_terminalInUse_msg).textContent();
		expectedToBeFalse((terminalInUseMsg.equalsIgnoreCase(terminalInUse_Txt)), "Can't login, as the terminal is in use.");

	}
	
	public void loginToRF() {
		
	}
	
	public void logOutRF() {
		
	}

}
