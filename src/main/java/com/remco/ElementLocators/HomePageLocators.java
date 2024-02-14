package com.remco.ElementLocators;

public interface HomePageLocators {
	
	 String hp_userName					=	"//input[@placeholder='User Name']";
	 String hp_password					=	"//input[@placeholder='Password']";
	 String hp_loginButton				=	"//button[@class='k-button']";
	 String hp_accountIcon				=	"//i[@class='fa fa-user']";
	 String hp_logoutText				=	"//span[text()='Logout']";
	 String hp_confirmLogout_btn		=	"//button[@class='k-button']";
	 String rf_serverName_inpt			=	"//input[@name='server_name']";
	 String rf_portNumber_inpt			=	"//input[@name='port_number']";
	 String rf_deviceName_inpt			=	"//input[@name='terminal_name']";
	 String rf_screenSize_inpt			=	"//select[@name='screen_size']";
	 String rf_submit_btn				=	"//input[@value='Submit']";
	 String loginChecks					=	"//body";
//	 String loginChecks					=	"//*[@id='output']/tbody/tr/td/pre/font/text()";
	 String rf_inputBox					=   "//input[@name='inputdata']";
	 
	 
	 //Static Values
	 String terminalInUse_Txt		=	"Terminal is already in use.Please choose another terminal and retry.";
	 String logoutConfirmTxt		= 	"Are you sure you";
	

}
