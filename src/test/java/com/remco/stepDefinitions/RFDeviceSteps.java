package com.remco.stepDefinitions;

import com.remco.ElementLocators.HomePageLocators;
import com.remco.webPages.RFHomePage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class RFDeviceSteps extends RFHomePage implements HomePageLocators {
	@Given("user launches RF Device WebTerminal")
	public void launcRFDevice() throws Exception {
		launchBrowser();
		loadRFUrl();
	}
	@Then("user provides the server details")
	public void scanRFServerDetails() throws Exception {
		fillRFServerDetails();
	}
	
	@Then("user login into RF Device")
	public void rf_userLogin() throws Exception {
		
	}
	
	@Then("user logout from RF Device")
	public void rf_userLogout() {
	
	}

}
