package com.remco.stepDefinitions;

import com.remco.ElementLocators.HomePageLocators;
import com.remco.webPages.HomePage;

import io.cucumber.java.en.*;
public class HomePageSteps extends HomePage implements HomePageLocators {
		@Given("user launches HighJump application")
	public void launchApplication() throws Exception {
		launchBrowser();
		loadHJUrl();
	}
	
	@Then("user login into the application")
	public void userLogin() throws Exception {
		logInToUI();
	}
	
	@Then("user logout from the application")
	public void userLogout() {
		logOut();
	}
	
	@Then("user imports an order")
	public void createOrder() throws Exception {
		connectToDB();
	}
	
	@Then("user creates a wave using the order")
	public void createWave() {
		
	}
	
	@Then("user releases the wave")
	public void releaseWave() {
		
	}
	
	@Then("user verifies wave condition based on wave color")
	public void verifyWaveCondition() {
		
	}
	
	@Then("user verifies wave status in t_pick_detail")
	public void verifyWaveStatus() {
		
	}
	
	
	
}
