package com.remco.stepDefinitions;

import com.remco.ElementLocators.HomePageLocators;
import com.remco.webPages.HomePage;

import io.cucumber.java.en.*;
public class HomePageSteps extends HomePage implements HomePageLocators {
		@Given("User launches HighJump application")
	public void launchApplication() throws Exception {
		launchBrowser();
		loadUrl();
	}
	
	@Then("user login into the application")
	public void userLogin() throws Exception {
		logInToUI();
	}
	
	@Then("user logout from the application")
	public void userLogout() {
		logOut();
	}
	
	
}
