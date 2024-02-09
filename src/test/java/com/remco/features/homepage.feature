Feature: Validate all the scenarios related to Home Page
 
 @login
	Scenario: Login to the HighJump Application with the correct credentials.
	Given User launches HighJump application
	Then user login into the application
	Then user logout from the application
