Feature: HPHJFRPLW01_Validate HighJump and RF Device functionalities
 
 @login
	Scenario: HPHJSCPLW01_Login to the HighJump Application with valid credentials.
	Given user launches HighJump application
	Then user login into the application
	Then user logout from the application
