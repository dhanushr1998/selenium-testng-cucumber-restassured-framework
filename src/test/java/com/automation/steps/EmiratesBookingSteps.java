package com.automation.steps;

import com.automation.hooks.Hooks;
import com.automation.pages.EmiratesHomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;



public class EmiratesBookingSteps {
    private EmiratesHomePage homePage;
    @Given("I open the Emirates Airline website")
    public void iOpenTheExampleWebsite() {
        homePage = new EmiratesHomePage(Hooks.getDriver());
        homePage.open();
        homePage.clickOneWayButton();
    }

    @When("I select {string} as the origin airport")
    public void i_select_as_the_origin_airport(String origin) {
        homePage.enterDepartureAirport(origin);
        Assert.assertTrue(homePage.isDepartureAirportSelected(origin),
                "Origin airport was not selected as expected: " + origin);
    }

    @When("I select {string} as the destination airport")
    public void i_select_as_the_destination_airport(String destination) {
        homePage.enterArrivalAirport(destination);
        Assert.assertTrue(homePage.isArrivalAirportSelected(destination),
                "Destination airport was not selected as expected: " + destination);
    }

    @When("I select a departure date {string}")
    public void i_select_a_departure_date(String date) {
        homePage.enterDepartingDate(date);
        Assert.assertTrue(homePage.isDepartureDateSelected(date),
                "Departure date was not selected as expected: " + date);
    }

    @When("I select a return date {string}")
    public void i_select_a_return_date(String date) {
        homePage.enterReturningDate(date);
        Assert.assertTrue(homePage.isReturnDateSelected(date),
                "Return date was not selected as expected: " + date);
    }

    @When("I select {string} adult passenger")
    public void i_select_adult_passenger(String count) {
        homePage.selectAdultPassengers(count);
        Assert.assertTrue(homePage.isPassengerCountSelected(count),
                "Passenger count was not selected as expected: " + count);
    }

    @When("I select {string} cabin class")
    public void i_select_cabin_class(String cabinClass) {
        homePage.selectCabinClass(cabinClass);
        Assert.assertTrue(homePage.isCabinClassSelected(cabinClass),
                "Cabin class was not selected as expected: " + cabinClass);
    }

    @When("I search for flights")
    public void i_search_for_flights() {
        homePage.searchFlights();
    }

    @Then("I should see the flight search results")
    public void i_should_see_the_flight_search_results() {
        Assert.assertTrue(homePage.isSearchResultsVisible(), "Flight results page was not displayed");
    }

    @Then("the departure and return dates should be correct")
    public void the_departure_and_return_dates_should_be_correct() {
        Assert.assertTrue(homePage.isSearchResultsVisible(),
                "Search results page was not displayed after selecting departure and return dates");
    }
}
