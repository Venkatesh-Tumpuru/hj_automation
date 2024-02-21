Feature: HPHJFRPLW01_Validate HighJump and RF Device functionalities
 
 @login
	Scenario: HPHJSCPLW01_Login to the HighJump Application with valid credentials.
	Given user launches HighJump application
	Then user login into the application
	Then user logout from the application
	
  @login
	Scenario: HPHJSCPLW02_Wave Creation and Wave Release.
	Given user launches HighJump application
	Then user imports an order
	Then user creates a wave using the order
	Then user releases the wave
	Then user verifies wave condition based on wave color
	Then user verifies wave status in t_pick_detail
	Then user logout from the application
