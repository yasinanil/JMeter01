@E2E
Feature: Verify successful registration

  Scenario: Register Test
    Given go to homepage
    When click on Sign up button
    And Enter valid values for all fields
    And Submit the form
    Then Assert the success message
    And Close the browser
    And Validate user via API
    And Validate user via DB