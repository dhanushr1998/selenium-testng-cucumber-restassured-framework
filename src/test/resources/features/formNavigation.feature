Feature: Verify navigation to Forms page

  Scenario: Verify the form page text fields
    Given I open the Demo website
    When I click on "Forms" card
    Then the Forms page should be successfully loaded
    When I open the Practice Form
    Then I should see the following fields on the form:
      | Field Name | Expected Type |
      | Name       | text          |
      | Email      | text          |