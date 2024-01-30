package com.remco.webPages;
import com.microsoft.playwright.Locator;
import com.remco.ElementLocators.HomePageLocators;

public class HomePage extends BasePage_Remco implements HomePageLocators {
	public void logInToUI() throws Exception {
		String userName;
		userName	=(System.getProperty("username"))!=null?System.getProperty("username"):getPropValue("hj_userId");
		String password;
		password=(System.getProperty("password"))!=null?System.getProperty("password"):getPropValue("hj_password");
		page.locator(hp_userName).type(userName);
		page.locator(hp_password).type(password);
		page.locator(hp_loginButton).click();
	}
	public void logOut() {
		page.waitForSelector(hp_accountIcon);
		page.locator(hp_accountIcon).click();
		page.locator(hp_logoutText).click();
		Locator buttons	=	page.locator(hp_confirmLogout_btn);
		for(int i=0;i<buttons.count();i++) {
			String textContent	=	buttons.nth(i).textContent();
			if(textContent.equalsIgnoreCase("Logout")) {
				buttons.nth(i).click();
				break;
			}
		}
	}
}
