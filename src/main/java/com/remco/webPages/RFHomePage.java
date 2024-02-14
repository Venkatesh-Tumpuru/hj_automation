package com.remco.webPages;

import com.microsoft.playwright.ElementHandle;
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
		Thread.sleep(2000);
		
		//check for Terminal In Use
		String terminalInUseMsg		=page.querySelector(loginChecks).textContent();
		expectedToBeFalse((terminalInUseMsg.contains(terminalInUse_Txt)), "Can't login, as the terminal is in use.");
		
		//Is User Logged into any device check
		String loggedInUser 		=page.querySelector(loginChecks).textContent();
		if(loggedInUser.contains("LOG OFF")) {
		expectedToBeFalse(!(loggedInUser.isEmpty()), "Please '"+loggedInUser+"'.");}
		//Any user logged into the device
		else if(loggedInUser.contains("IN USE BY")) {
		expectedToBeFalse(!(loggedInUser.isEmpty()), "can't login to this device, as it is '"+loggedInUser+"'.");}
		
		//Is Logout Confirm Page displayed
		String logOutConfirmTextMsg	=page.querySelector(loginChecks).textContent();
		if(logOutConfirmTextMsg.contains(logoutConfirmTxt)) {
			ElementHandle inputBox= page.querySelector(rf_inputBox);
			inputBox.evaluate("input => input.value = ''");
			page.locator(rf_inputBox).type("Y");
			page.locator(rf_submit_btn).click();
			Thread.sleep(2000);
		}
		

	}
	
	public void loginToRF() throws Exception {
		String rfUserName	= System.getProperty("Drf_userID",getPropValue("rf_userId"));
		String rfPassword	= System.getProperty("Drf_userPassword",getPropValue("rf_userPassword"));
		String rfEquipment	= System.getProperty("Drf_userEquipment",getPropValue("rf_userEquipment"));
		String inputValuesArray[]= {rfUserName,rfPassword,rfEquipment};
		for (int counter=0;counter<inputValuesArray.length;counter++) {
		ElementHandle inputBox= page.querySelector(rf_inputBox);
		inputBox.evaluate("input => input.value = ''");
		page.locator(rf_inputBox).type(inputValuesArray[counter]);
		page.locator(rf_submit_btn).click();
		Thread.sleep(1000);}

	}
	
	public void logOutRF() throws Exception {
		while(true) {
			ElementHandle inputBox= page.querySelector(rf_inputBox);
			inputBox.evaluate("input => input.value = ''");
			page.locator(rf_inputBox).type("F1");
			page.locator(rf_submit_btn).click();
			Thread.sleep(2000);
		String logOutConfirmTextMsg	=page.querySelector(loginChecks).textContent();
		if(logOutConfirmTextMsg.contains(logoutConfirmTxt)) {
			inputBox.evaluate("input => input.value = ''");
			page.locator(rf_inputBox).type("Y");
			page.locator(rf_submit_btn).click();
			break;}
		}
	}
}
