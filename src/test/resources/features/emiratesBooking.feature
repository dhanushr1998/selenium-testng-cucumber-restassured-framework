Feature: Emirates Airline flight booking

  As a passenger
  I want to search for and book Emirates flights
  So that I can travel to my destination

  Background:
    Given I open the Emirates Airline website

  @smoke @positive
  Scenario: Search for a one-way flight
    When I select "Dubai" as the origin airport
    And I select "London Heathrow" as the destination airport
    And I select a departure date "20/12/2026"
    And I select "2" adult passenger
    And I select "Economy" cabin class
    And I search for flights
    Then I should see the flight search results
    And the search results should match my selected route
    And the departure date should be displayed correctly

  @positive @roundtrip
  Scenario: Search for a return flight
    When I select "Dubai" as the origin airport
    And I select "London Heathrow" as the destination airport
    And I select a departure date "20/12/2026"
    And I select a return date "30/12/2026"
    And I select "1" adult passenger
    And I select "Economy" cabin class
    And I search for flights
    Then I should see the flight search results
    And the departure and return dates should be correct

  @negative @validation
  Scenario: Prevent searching with the same origin and destination
    When I select "Dubai" as the origin airport
    And I select "Dubai" as the destination airport
    And I enter a valid future departure date
    And I search for flights
    Then I should see an appropriate route validation message
    And the invalid search should not proceed

  @negative @validation
  Scenario: Prevent searching without a destination
    When I select "Dubai" as the origin airport
    And I leave the destination airport empty
    And I enter a valid future departure date
    And I search for flights
    Then I should see a destination validation message

  @passengers
  Scenario Outline: Search flights with different passenger counts
    When I select "Dubai" as the origin airport
    And I select "London Heathrow" as the destination airport
    And I select a departure date "20/12/2026"
    And I select "<adults>" adult passenger
    And I select "<cabin>" cabin class
    And I search for flights
    Then I should see the flight search results
    And the results should reflect "<adults>" adult passengers

    Examples:
      | adults | cabin   |
      | 1      | Economy |
      | 2      | Economy |
      | 1      | Business|

  @booking @positive
  Scenario: Continue to passenger details after selecting a flight
    Given I have searched for a valid flight
    And flight search results are displayed
    When I select an available flight
    And I select an available fare
    Then I should see the passenger details page
    And the selected flight details should be displayed correctly

  @booking @negative
  Scenario: Handle an unavailable fare
    Given I have searched for a valid flight
    And flight search results are displayed
    When the selected fare becomes unavailable
    Then I should see an appropriate availability message
    And I should not proceed with the unavailable fare

  @booking @security
  Scenario: Prevent duplicate booking submission
    Given I have completed the passenger details in a test environment
    And I have selected a valid fare
    When I submit the booking request twice
    Then only one valid booking should be created
    And the booking status should be consistent