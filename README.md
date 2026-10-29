# Basic Selenium + TestNG + Cucumber + REST Assured framework

A small automation-practice project using Java 17 and Maven. It contains:
- Selenium WebDriver browser test (Chrome)
- TestNG as the test runner
- Cucumber feature file + step definitions + hooks
- REST Assured API test
- A tiny Page Object Model example

## Prerequisites
1. JDK 17 installed.
2. Maven installed (`mvn -version` should work).
3. Google Chrome installed.
4. VS Code with the **Extension Pack for Java** (and optionally Cucumber support).

Recent Selenium versions use Selenium Manager to locate/download a compatible ChromeDriver automatically. The first browser run needs internet access. If your environment blocks this, configure ChromeDriver manually.

## Open in VS Code
1. Extract the ZIP.
2. In VS Code, choose **File → Open Folder** and select this project folder.
3. Wait for Maven dependencies to finish importing.
4. Run from the terminal:

```bash
mvn clean test
```

On Windows PowerShell, the same command works. It runs the Cucumber browser scenario and the REST Assured API test. Browser settings are in `src/test/resources/config.properties`:

```properties
browser=chrome
driver.path=
headless=false
browser.maximize=true
browser.keepOpen=false
retry.count=0
action.delay.ms=0
```

Set `browser` to `chrome`, `firefox`, or `edge`; set `headless=true` to run without a visible browser window. Set `browser.maximize=true` to maximize the browser window on startup; set it to `false` to use the browser's default window size. Set `browser.keepOpen=true` to leave the browser open after each Cucumber scenario (useful for debugging); the default `false` closes it. `retry.count` is the number of retries after an initial failure for browser scenarios and the API test. `action.delay.ms` pauses after browser actions such as clicks, typing, and navigation. Keep it at `0` to disable the delay. Settings can also be overridden for a Maven run, for example:

```bash
mvn -Dheadless=true -Dbrowser=firefox test
```

Selenium Manager normally locates or downloads the matching browser driver and requires access to the driver download service. If that download is blocked, install the driver matching your browser version and set `driver.path` to the full path of its executable. Because this is a Java properties file, escape Windows backslashes by doubling them (for example, `driver.path=C:\\WebDriver\\msedgedriver.exe` for Edge). Forward slashes also work in Windows paths. For Edge, the driver and browser major versions must match. Driver startup failures are not retried because retries cannot fix a missing or unreachable driver download.

The Emirates sample depends on the live Emirates site allowing access from the test environment. If it returns an access-restricted page, the scenario reports that explicitly and does not retry the site denial; run it only where Emirates permits testing or point the test at an authorized test environment.

## TestNG suite

The root `testng.xml` suite includes the API test and the Cucumber/Selenium runner. Maven Surefire is configured to use this suite, so running `mvn clean test` executes both.

Use TestNG groups to run just one type of test:

```bash
mvn -Dgroups=api test
mvn -Dgroups=cucumber test
```

The Cucumber runner includes all feature files by default. To filter scenarios by a Cucumber tag, for example:

```bash
mvn -Dgroups=cucumber "-Dcucumber.filter.tags=@passengers" test
```

In VS Code, you can also open `testng.xml` and run the suite using the TestNG extension. Run the Maven command from the project root to use the suite through Surefire.

The Emirates feature currently contains steps that are still pending in `EmiratesBookingSteps`; those scenarios will fail until the step definitions are implemented. The API test can be run independently with the `api` group while working on those steps.

## Project structure

```text
testng.xml
src
└── test
    ├── java
    │   └── com/automation
    │       ├── api/ApiTest.java
    │       ├── config/FrameworkConfig.java
    │       ├── hooks/Hooks.java
    │       ├── pages/ExamplePage.java
    │       ├── retry/ConfigurableRetryAnalyzer.java
    │       ├── runner/TestRunner.java
    │       └── steps/ExampleSteps.java
    └── resources
        ├── config.properties
        └── features/example.feature
```

## Automation talking points
- **Page Object Model:** locators and page actions are kept in `ExamplePage`.
- **Hooks:** `@Before` creates the browser; `@After` closes it even when a scenario fails.
- **Cucumber:** Gherkin feature → step definitions → page object.
- **TestNG:** `AbstractTestNGCucumberTests` runs Cucumber scenarios through TestNG.
- **REST Assured:** `given / when / then` sends a GET request and validates status and JSON fields.
- **Waits:** use `WebDriverWait` for explicit waits instead of `Thread.sleep`.
- **Test independence:** each scenario should establish its own state and clean up after itself.

The sample sites are public demo endpoints. If a public endpoint is unavailable on automation day, replace the URL with the automation-provided test URL.
