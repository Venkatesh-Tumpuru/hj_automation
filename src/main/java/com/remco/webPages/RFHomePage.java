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
		if(someOneUsing_Check()) {
			throw new Exception("Someone already logged in and is in Log out confirm Screen");}
		if(isDeviceBeingUsed()) {
			throw new Exception("Someone already logged in using the device. Please choose another device.");}
	}
	
	public void loginToRF() throws Exception {
		//Is Logout Confirm Page displayed
		String logOutConfirmTextMsg	=page.querySelector(loginChecks).textContent();
		if(logOutConfirmTextMsg.contains(logoutConfirmTxt)) {
			ElementHandle inputBox= page.querySelector(rf_inputBox);
			inputBox.evaluate("input => input.value = ''");
			page.locator(rf_inputBox).type("Y");
			page.locator(rf_submit_btn).click();
			Thread.sleep(1000);
		}
		String rfUserName	= System.getProperty("Drf_userID",getPropValue("rf_userId"));
		String rfPassword	= System.getProperty("Drf_userPassword",getPropValue("rf_userPassword"));
		String rfEquipment	= System.getProperty("Drf_userEquipment",getPropValue("rf_userEquipment"));
		String inputValuesArray[]= {rfUserName,rfPassword,rfEquipment};
		for (int counter=0;counter<inputValuesArray.length;counter++) {
		ElementHandle inputBox= page.querySelector(rf_inputBox);
		inputBox.evaluate("input => input.value = ''");
		page.locator(rf_inputBox).type(inputValuesArray[counter]);
		page.locator(rf_submit_btn).click();
		Thread.sleep(1000);
		String loggedInUser 		=page.querySelector(loginChecks).textContent();
		if(counter==0) {
			//Any user logged into the device
			if(loggedInUser.contains("IN USE BY")) {
			String inUseBy	=loggedInUser.substring(loggedInUser.indexOf("IN"),loggedInUser.indexOf("USER")).trim();
			expectedToBeFalse(loggedInUser.contains("IN USE BY"), "can't login to this device, as it is '"+inUseBy+"'.");}}
		if(counter==1) {
			//Is User Logged into any device check
			if(loggedInUser.contains("LOG OFF")) {
			String devicename	=loggedInUser.substring(loggedInUser.indexOf("LOG"),loggedInUser.indexOf("USER")).trim();;
			expectedToBeFalse(loggedInUser.contains("LOG OFF"), "Please '"+devicename+"'device.");}}
		}
	}
	
	public void logOutRF() throws Exception {
		while(true) {
			ElementHandle inputBox= page.querySelector(rf_inputBox);
			inputBox.evaluate("input => input.value = ''");
			page.locator(rf_inputBox).type("F1");
			page.locator(rf_submit_btn).click();
			Thread.sleep(2000);
			if(confirmLogOutScreen()) {break;}
		}
		expectedToBeTrue(!(isDeviceBeingUsed()),"Seems like device not logged out successfully.")
	}
	
	public boolean confirmLogOutScreen() {
		String logOutConfirmTextMsg	=page.querySelector(loginChecks).textContent();
		if(logOutConfirmTextMsg.contains(logoutConfirmTxt)) {
			ElementHandle logoutConfirmBox= page.querySelector(rf_inputBox);
			logoutConfirmBox.evaluate("input => input.value = ''");
			page.locator(rf_inputBox).type("Y");
			page.locator(rf_submit_btn).click();
			return true;}
		else {return false;}
	}
	
	public boolean someOneUsing_Check() {
		String rfBody	=page.querySelector(loginChecks).textContent();
		if(rfBody.contains(logoutConfirmTxt)) {
			return true;}
		else {return false;}
	}
	
	public boolean isDeviceBeingUsed() {
		String rfBody	=page.querySelector(loginChecks).textContent();
		if((rfBody.contains("USER ID"))) {
			return false;}
		else {return true;}
	}
}
