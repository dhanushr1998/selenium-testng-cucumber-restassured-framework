package com.automation.steps;

import com.automation.hooks.Hooks;
import com.automation.pages.ExamplePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class ExampleSteps {
    private ExamplePage page;

    @Given("I open the Demo website")
    public void iOpenTheExampleWebsite() {
        page = new ExamplePage(Hooks.getDriver());
        page.open();
    }

    @When("the page has loaded")
    public void thePageHasLoaded() {
        Assert.assertTrue(
                page.getCardText().contains("Elements"),
                "The Elements card was not displayed");
    }

    @Then("the card title should be {string}")
    public void thePageCardShouldBe(String expectedTitle) {
        Assert.assertEquals(page.getCardText(), expectedTitle);
    }

    @When("I click on {string} card")
    public void iClickOnCard(String cardTitle) {
        page.clickCard(cardTitle);
    }

    @Then("the Forms page should be successfully loaded")
    public void theFormsPageShouldBeSuccessfullyLoaded() {
        Assert.assertTrue(page.isFormsPageLoaded(), "The Forms page did not load");
    }

    @When("I open the Practice Form")
    public void iOpenThePracticeForm() {
        page.openPracticeForm();
    }

    @Then("I should see the following fields on the form:")
    public void iShouldSeeTheFollowingFieldsOnTheForm(DataTable dataTable) {
        List<Map<String, String>> fields = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> field : fields) {
            Assert.assertTrue(
                    page.isFormFieldDisplayed(field.get("Field Name"), field.get("Expected Type")),
                    field.get("Field Name") + " field is missing or has the wrong input type");
        }
    }
}
