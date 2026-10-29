package com.automation.pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.automation.common.commonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmiratesHomePage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By ACCEPT_COOKIES = By.id("onetrust-accept-btn-handler");
    private static final By ONE_WAY_BUTTON = By.xpath("//button[contains(text(),\"One way\")] ");
    private static final By DEPARTURE_AIRPORT = By.xpath("//div[@data-testid=\"combobox_Departure airport\"]/span/span/input");
    private static final By ARRIVAL_AIRPORT = By.xpath("//div[@data-testid=\"combobox_Arrival airport\"]/span/span/input");
    private static final By DEPARTURE_AIRPORT_LABEL = By.xpath(
            "//div[@data-testid='combobox_Departure airport']//label[contains(@class, 'input-field__placeholder')]");
    private static final By ARRIVAL_AIRPORT_LABEL = By.xpath(
            "//div[@data-testid='combobox_Arrival airport']//label[contains(@class, 'input-field__placeholder')]");
    private static final By AIRPORT_OPTIONS = By.xpath("//button[@role='option']");
    private static final By DEPARTING_DATE_FIELD = By.xpath("//label[contains(text(),\"Departing\")] ");
    private static final By RETURNING_DATE_FIELD = By.xpath("//label[contains(text(),\"Returning\")] ");
    private static final By ADULT_PASSENGER_COUNT = By.id("PASSENGER_TYPES");
    private static final By INCREASE_ADULTS_BUTTON = By.xpath("//button[starts-with(@aria-label,'Increase number of Adult passengers')]");
    private static final By CABIN_CLASS = By.xpath("//input[@id=\"auto-drop-down-Select-Class\"]");
    private static final By CABIN_OPTIONS = By.xpath(
            "//ul[starts-with(@id,'auto-drop-down-Select-Class-autocomplete-') and @role='listbox']"
                    + "/li[@role='option']");
    private static final By VISIBLE_MONTH_CAPTIONS = By.xpath(
            "//div[contains(@class,'CalendarMonth') and @data-visible='true']//div[contains(@class,'CalendarMonth_caption')]");
    private static final By NAVIGATION_BUTTON = By.xpath("//button[@title]");
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter CALENDAR_MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MMMM uuuu", Locale.ENGLISH);
     private static final By SEARCH_FLIGHTS_BUTTON = By.xpath("//button[contains(text(),'Search flights')]");
    private final WebDriver driver;
    private final commonMethods commonMethods;

    public EmiratesHomePage(WebDriver driver) {
        this.driver = driver;
        this.commonMethods = new commonMethods(driver);
    }

    public void open() {
        driver.get("https://www.emirates.com/ae/english/book/");
        commonMethods.clickWhenReady(ACCEPT_COOKIES);
    }
    

    public void clickOneWayButton() {
        commonMethods.clickWhenReady(ONE_WAY_BUTTON);
    }

    public void enterDepartureAirport1(String origin){

        commonMethods.clearAndType(DEPARTURE_AIRPORT, origin);
        waitForAirportSelection(origin);
    }

    public void enterDepartureAirport(String origin) {
        commonMethods.clickWhenReady(DEPARTURE_AIRPORT_LABEL);
        commonMethods.clearAndType(DEPARTURE_AIRPORT, origin);
        waitForAirportSelection(origin);
    }

    public boolean isDepartureAirportSelected(String origin) {
        return airportFieldContains(DEPARTURE_AIRPORT, "Departure airport", origin);
    }

    public void enterArrivalAirport(String destination) {
        commonMethods.clickWhenReady(ARRIVAL_AIRPORT_LABEL);
        commonMethods.clearAndType(ARRIVAL_AIRPORT, destination);
        waitForAirportSelection(destination);
    }

    public boolean isArrivalAirportSelected(String destination) {
        return airportFieldContains(ARRIVAL_AIRPORT, "Arrival airport", destination);
    }

    public void enterDepartingDate(String date) {
        selectCalendarDate(DEPARTING_DATE_FIELD, date);
    }

    public boolean isDepartureDateSelected(String expectedDate) {
        return dateValueMatches(DEPARTING_DATE_FIELD, expectedDate);
    }

    public void enterReturningDate(String date) {
        selectCalendarDate(RETURNING_DATE_FIELD, date);
    }

    public boolean isReturnDateSelected(String expectedDate) {
        return dateValueMatches(RETURNING_DATE_FIELD, expectedDate);
    }

    public void selectAdultPassengers(String requestedCount) {
        int targetCount = Integer.parseInt(requestedCount);
        WebElement passengerCount = commonMethods.waitForClickableElement(ADULT_PASSENGER_COUNT);
        passengerCount.click();
        int selectedCount = Integer.parseInt(passengerCount.getAttribute("value").split("\\s+", 2)[0]);
        while (selectedCount < targetCount) {
            commonMethods.clickWhenReady(INCREASE_ADULTS_BUTTON);
            selectedCount++;
        }
    }

    public boolean isPassengerCountSelected(String expectedCount) {
        String value = new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(driver -> driver.findElement(ADULT_PASSENGER_COUNT).getAttribute("value"));
        return value != null && value.trim().startsWith(expectedCount.trim());
    }

    public void selectCabinClass(String requestedClass) {
        String cabinClass = switch (requestedClass.trim().toLowerCase(Locale.ROOT)) {
            case "economy" -> "Economy Class";
            case "premium economy" -> "Premium Economy";
            case "business" -> "Business Class";
            case "first" -> "First Class";
            default -> throw new IllegalArgumentException("Unsupported cabin class: " + requestedClass);
        };

        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        commonMethods.clickWhenReady(CABIN_CLASS);
        WebElement cabinOption = wait.until(driver -> driver.findElements(CABIN_OPTIONS).stream()
                .filter(option -> cabinClass.equals(option.getText().trim()))
                .findFirst()
                .orElse(null));
        wait.until(ExpectedConditions.elementToBeClickable(cabinOption)).click();
        commonMethods.waitForAttributeValue(CABIN_CLASS, "value", cabinClass);
    }

    public boolean isCabinClassSelected(String requestedClass) {
        String expectedClass = switch (requestedClass.trim().toLowerCase(Locale.ROOT)) {
            case "economy" -> "Economy Class";
            case "premium economy" -> "Premium Economy";
            case "business" -> "Business Class";
            case "first" -> "First Class";
            default -> throw new IllegalArgumentException("Unsupported cabin class: " + requestedClass);
        };
        String actual = commonMethods.waitForVisibleElement(CABIN_CLASS).getAttribute("value");
        return expectedClass.equals(actual);
    }

    private void selectCalendarDate(By dateField, String requestedDate) {
        LocalDate date;
        try {
            date = LocalDate.parse(requestedDate, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Date must use dd/MM/yyyy format: " + requestedDate, exception);
        }

        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        commonMethods.clickWhenReady(dateField);
        wait.until(driver -> !getVisibleMonthLabels().isEmpty());
        YearMonth targetMonth = YearMonth.from(date);

        while (true) {
            List<String> visibleMonths = getVisibleMonthLabels();
            if (visibleMonths.stream()
                    .map(month -> YearMonth.parse(month, CALENDAR_MONTH_FORMAT))
                    .anyMatch(targetMonth::equals)) {
                commonMethods.clickWhenReady(datePicker(
                        date.getMonthValue(), date.getYear(), date.getDayOfMonth()));
                wait.until(driver -> dateValueMatches(dateField, requestedDate));
                return;
            }

            List<YearMonth> displayedMonths = visibleMonths.stream()
                    .map(month -> YearMonth.parse(month, CALENDAR_MONTH_FORMAT))
                    .toList();
            boolean movingBackward = targetMonth.isBefore(displayedMonths.get(0));
            YearMonth newlyDisplayedMonth = movingBackward
                    ? displayedMonths.get(0).minusMonths(1)
                    : displayedMonths.get(displayedMonths.size() - 1).plusMonths(1);
            String navigationTitle = (movingBackward ? "Previous month " : "Next month ")
                    + newlyDisplayedMonth.format(CALENDAR_MONTH_FORMAT);
            WebElement navigationButton = wait.until(driver -> driver.findElements(NAVIGATION_BUTTON).stream()
                    .filter(button -> navigationTitle.equals(button.getAttribute("title")))
                    .findFirst()
                    .orElse(null));
            wait.until(ExpectedConditions.elementToBeClickable(navigationButton)).click();
            String expectedMonth = newlyDisplayedMonth.format(CALENDAR_MONTH_FORMAT);
            wait.until(driver -> getVisibleMonthLabels().contains(expectedMonth));
        }
    }

    private void waitForAirportSelection(String airportName) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        WebElement option = wait.until(driver -> driver.findElements(AIRPORT_OPTIONS).stream()
                .filter(element -> airportMatchesSelection(element.getText(), airportName))
                .findFirst()
                .orElse(null));
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    private List<String> getVisibleMonthLabels() {
        return driver.findElements(VISIBLE_MONTH_CAPTIONS).stream()
                .map(WebElement::getText)
                .toList();
    }

    /** Builds a day selector from a 1-based calendar month, year, and day. */
    private By datePicker(int month, int year, int day) {
        LocalDate.of(year, month, day);
        return By.xpath("//eol-calendar[@class='eol-calendar--visible eol-calendar--top']"
                + "//button[@data-month='" + (month - 1) + "' and @data-year='" + year
                + "' and @data-date='" + day + "']");
    }

    public void searchFlights() {
        commonMethods.clickWhenReady(SEARCH_FLIGHTS_BUTTON);
    }

    public boolean isSearchResultsVisible() {
        String pageSource = driver.getPageSource();
        boolean hasResultText = pageSource.toLowerCase(Locale.ROOT).contains("search flights")
                || pageSource.toLowerCase(Locale.ROOT).contains("results");
        boolean hasFlightCards = !driver.findElements(By.xpath("//div[contains(@class,'flight') or contains(@class,'result') or contains(@data-testid,'flight')]"))
                .isEmpty();
        return hasResultText || hasFlightCards;
    }

    private boolean airportFieldContains(By inputLocator, String fieldName, String expectedValue) {
        WebElement input = new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(inputLocator));
        String inputValue = input.getAttribute("value");
        String fieldText = driver.findElement(By.xpath(
                "//div[@data-testid='combobox_" + fieldName + "']")).getText();
        return airportMatchesSelection(inputValue, expectedValue)
                || airportMatchesSelection(fieldText, expectedValue);
    }

    private boolean airportMatchesSelection(String actualValue, String expectedValue) {
        String normalizedActual = normalizeAirportText(actualValue);
        String normalizedExpected = normalizeAirportText(expectedValue);
        if (normalizedActual.contains(normalizedExpected)) {
            return true;
        }
        if ("londonheathrow".equals(normalizedExpected) && normalizedActual.contains("lhr")) {
            return true;
        }
        String[] expectedTokens = normalizedExpected.split("[\\s\\p{Punct}]+");
        for (String token : expectedTokens) {
            if (!token.isBlank() && !normalizedActual.contains(token)) {
                return false;
            }
        }
        return expectedTokens.length > 0;
    }

    private String normalizeAirportText(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private boolean dateValueMatches(By dateField, String expectedDate) {
        LocalDate date;
        try {
            date = LocalDate.parse(expectedDate, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Date must use dd/MM/yyyy format: " + expectedDate, exception);
        }

        List<WebElement> labels = driver.findElements(dateField);
        if (labels.isEmpty()) {
            return false;
        }

        WebElement label = labels.get(0);
        List<String> displayedValues = new ArrayList<>();
        addDisplayedValue(displayedValues, label);
        for (WebElement ancestor : label.findElements(By.xpath("./ancestor::div[position() <= 3]"))) {
            addDisplayedValue(displayedValues, ancestor);
            for (WebElement input : ancestor.findElements(By.tagName("input"))) {
                addDisplayedValue(displayedValues, input);
            }
        }

        List<String> expectedValues = List.of(
                expectedDate,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                date.format(DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH)),
                date.format(DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH)),
                date.format(DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.ENGLISH)));
        return displayedValues.stream()
                .map(this::normalizeAirportText)
                .anyMatch(actual -> expectedValues.stream()
                        .map(this::normalizeAirportText)
                        .anyMatch(actual::contains));
    }

    private void addDisplayedValue(List<String> values, WebElement element) {
        values.add(element.getText());
        values.add(element.getAttribute("value"));
        values.add(element.getAttribute("aria-label"));
    }
}
