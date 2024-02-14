Feature: RFDHJFRPLW01_Validate HighJump and RF Device functionalities
 
 @rflogin
	Scenario: RFDHJSCPLW01_Login to the RF device with valid credentials.
	Given user launches RF Device WebTerminal
	Then user provides the server details
	Then user login into RF Device
	Then user logout from RF Device
