Feature: Verify Elements card

  Scenario: Verify the Demo page Elements card
    Given I open the Demo website
    When the page has loaded
    Then the card title should be "Elements"



