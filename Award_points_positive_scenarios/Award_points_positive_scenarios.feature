Feature: User must click on the Award Point and navigated to the Reddemed award points page

  Scenario: Validate that the user is navigated to Welcome page
    Given Browser is open
    And User clicks on Welcome Login button

  Scenario: Validate that the user is able to navigated to Utilities screen
    When User should click on Utilities tab and navigated to Utilities Dashboard

  Scenario: Validate that user can click the Tile "Award Points"in the Utilities dashboard
    When User should be able to click on Award point tile and to Award points page successfully

  Scenario: Validate that user is able to redeem points  in the Award points dashboard.
    When User should be able to click on the Redeem Points button and navigated to redeem award points page

  Scenario: Validate that the user is able to navigate to the redeemed page
    When User should be able to navigated to the redeemed page successfully