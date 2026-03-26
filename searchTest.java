package atoumationtest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class searchTest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.garnethill.com/");
        System.out.println("Website opened successfully");

        // TC-header-16
        testInvalidSearchMessage(driver);

        // TC-header-9
        testSearch(driver, "dress", "valid");

        // TC-header-10
        testSearch(driver, "sjjsjss", "invalid");

        // TC-header-13
        testSearch(driver, "", "empty");

        // TC-header-15
        testSearch(driver, "@@@###$$$", "special");

        // TC-header-11
        testSearchSuggestionsDropdown(driver);

        // TC-header-12
        testClickPopularCategory(driver, js);

        // TC-header-14
        testSearchSuggestionClick(driver, js, "dress");
     // Edge tests
        testSearchWithSpaces(driver);
        testSearchUppercase(driver);
        testSearchLongKeyword(driver);
        testSearchUsingIcon(driver);
        driver.quit();
    }

    public static WebElement getSearchBox(WebDriver driver) {
        return driver.findElement(
                By.cssSelector("input[type='search'], input[placeholder*='help'], input[placeholder*='find']")
        );
    }

    public static void testSearch(WebDriver driver, String input, String searchType)
            throws InterruptedException {

        System.out.println("----- Search Test: " + searchType + " | Input: " + input + " -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            Thread.sleep(1000);
            searchBox.clear();
            Thread.sleep(500);

            if (!input.isEmpty()) {
                searchBox.sendKeys(input);
                Thread.sleep(1000);
            }

            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(3500);

            String pageSource = driver.getPageSource().toLowerCase();
            String currentUrl = driver.getCurrentUrl().toLowerCase();

            if (searchType.equalsIgnoreCase("valid")) {

                boolean titleVisible = false;
                boolean keywordInPage = false;
                boolean productsDisplayed = false;

                try {
                    WebElement resultTitle = driver.findElement(
                            By.xpath("//*[contains(text(),'Search Results for')]")
                    );
                    titleVisible = resultTitle.isDisplayed();
                } catch (Exception e) {
                    titleVisible = false;
                }

                if (pageSource.contains(input.toLowerCase()) || currentUrl.contains(input.toLowerCase())) {
                    keywordInPage = true;
                }

                try {
                    List<WebElement> products = driver.findElements(
                            By.xpath("//img[@alt and string-length(@alt) > 0]")
                    );

                    if (products.size() > 0) {
                        productsDisplayed = true;
                    }
                } catch (Exception e) {
                    productsDisplayed = false;
                }

                if (titleVisible) {
                    System.out.println("Search results title displayed - PASS");
                } else {
                    System.out.println("Search results title displayed - FAIL");
                }

                if (keywordInPage) {
                    System.out.println("Keyword appears in results page - PASS");
                } else {
                    System.out.println("Keyword appears in results page - FAIL");
                }

                if (productsDisplayed) {
                    System.out.println("Products displayed - PASS");
                } else {
                    System.out.println("Products displayed - FAIL");
                }

                if (titleVisible && keywordInPage && productsDisplayed) {
                    System.out.println("Valid search PASS");
                    System.out.println("Opened page: " + driver.getCurrentUrl());
                } else {
                    System.out.println("Valid search FAIL");
                }

            } else if (searchType.equalsIgnoreCase("invalid")) {

                if (pageSource.contains("weren’t able to find")
                        || pageSource.contains("weren't able to find")
                        || pageSource.contains("no results")
                        || pageSource.contains("unable to find")) {
                    System.out.println("Invalid search PASS");
                } else {
                    System.out.println("Invalid search FAIL");
                }

            } else if (searchType.equalsIgnoreCase("empty")) {

                if (pageSource.contains("please enter")
                        || pageSource.contains("keyword")
                        || pageSource.contains("item#")
                        || currentUrl.contains("productsearch")) {
                    System.out.println("Empty input search PASS");
                } else {
                    System.out.println("Empty input search FAIL");
                }

            } else if (searchType.equalsIgnoreCase("special")) {

                if (pageSource.contains("weren’t able to find")
                        || pageSource.contains("weren't able to find")
                        || pageSource.contains("no results")
                        || pageSource.contains("unable to find")) {
                    System.out.println("Special characters search PASS");
                } else {
                    System.out.println("Special characters search FAIL");
                }
            }

        } catch (Exception e) {
            System.out.println("Search test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testSearchSuggestionsDropdown(WebDriver driver) throws InterruptedException {

        System.out.println("----- TC-header-11: Search suggestions dropdown -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            Thread.sleep(2500);

            String pageSource = driver.getPageSource().toLowerCase();

            if (pageSource.contains("search suggestions")
                    || pageSource.contains("related products")
                    || pageSource.contains("popular")
                    || pageSource.contains("category")
                    || pageSource.contains("categories")
                    || pageSource.contains("shop")) {
                System.out.println("Search suggestions dropdown PASS");
            } else {
                System.out.println("Search suggestions dropdown FAIL");
            }

        } catch (Exception e) {
            System.out.println("Search suggestions dropdown test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testClickPopularCategory(WebDriver driver, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- TC-header-12: Click popular category from search dropdown -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            Thread.sleep(2500);

            String beforeClick = driver.getCurrentUrl();
            boolean clicked = false;

            List<WebElement> links = driver.findElements(By.xpath("//a[normalize-space()]"));

            for (WebElement link : links) {
                try {
                    String text = link.getText().trim();

                    if (!text.isEmpty() && link.isDisplayed()) {
                        js.executeScript("arguments[0].click();", link);
                        clicked = true;
                        break;
                    }
                } catch (Exception e) {
                    // skip
                }
            }

            Thread.sleep(3000);

            String afterClick = driver.getCurrentUrl();

            if (!clicked) {
                System.out.println("Popular category link not found - FAIL");
            } else if (!afterClick.equals(beforeClick)) {
                System.out.println("Popular category click PASS");
                System.out.println("Opened page: " + afterClick);
            } else {
                System.out.println("Popular category click FAIL");
            }

        } catch (Exception e) {
            System.out.println("Click popular category test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testSearchSuggestionClick(WebDriver driver, JavascriptExecutor js, String keyword)
            throws InterruptedException {

        System.out.println("----- TC-header-14: Click search suggestion -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            Thread.sleep(1000);

            searchBox.clear();
            searchBox.sendKeys(keyword);
            Thread.sleep(2500);

            String beforeClick = driver.getCurrentUrl();

            List<WebElement> suggestions = driver.findElements(
                    By.xpath("//a[contains(@href,'searchTerm=') or contains(@href,'dress') or contains(@class,'suggest')]")
            );

            boolean clicked = false;

            for (WebElement suggestion : suggestions) {
                try {
                    if (suggestion.isDisplayed()) {
                        js.executeScript("arguments[0].click();", suggestion);
                        clicked = true;
                        break;
                    }
                } catch (Exception e) {
                    // skip
                }
            }

            Thread.sleep(3000);

            String afterClick = driver.getCurrentUrl();

            if (!clicked) {
                System.out.println("Suggestion not found - FAIL");
            } else if (!afterClick.equals(beforeClick)) {
                System.out.println("Suggestion click PASS");
                System.out.println("Opened page: " + afterClick);
            } else {
                System.out.println("Suggestion click FAIL");
            }

        } catch (Exception e) {
            System.out.println("Suggestion test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testInvalidSearchMessage(WebDriver driver) throws InterruptedException {

        System.out.println("----- TC-header-16: Invalid search message -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            Thread.sleep(1000);
            searchBox.clear();
            searchBox.sendKeys("sslsl4");
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);

            Thread.sleep(3000);

            WebElement message = driver.findElement(
                    By.xpath("//*[contains(text(),'able to find any results') or contains(text(),'Please try again with a different search')]")
            );

            if (message.isDisplayed()) {
                System.out.println("Invalid search message displayed - PASS");
                System.out.println("Message: " + message.getText());
            } else {
                System.out.println("Invalid search message - FAIL");
            }

        } catch (Exception e) {
            System.out.println("Invalid search message test FAILED");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testSearchWithSpaces(WebDriver driver) throws InterruptedException {

        System.out.println("----- Edge Test: Search with spaces -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            searchBox.clear();
            searchBox.sendKeys("   dress   ");

            searchBox.sendKeys(Keys.ENTER);

            Thread.sleep(3000);

            if (driver.getCurrentUrl().toLowerCase().contains("dress")) {
                System.out.println("Search with spaces PASS");
            } else {
                System.out.println("Search with spaces FAIL");
            }

        } catch (Exception e) {

            System.out.println("Search with spaces FAILED");
            System.out.println("Reason: " + e.getMessage());

        }
    }
    public static void testSearchUppercase(WebDriver driver) throws InterruptedException {

        System.out.println("----- Edge Test: Uppercase search -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            searchBox.clear();
            searchBox.sendKeys("DRESS");

            searchBox.sendKeys(Keys.ENTER);

            Thread.sleep(3000);

            if (driver.getCurrentUrl().toLowerCase().contains("dress")) {
                System.out.println("Uppercase search PASS");
            } else {
                System.out.println("Uppercase search FAIL");
            }

        } catch (Exception e) {

            System.out.println("Uppercase search FAILED");
            System.out.println("Reason: " + e.getMessage());

        }
    }public static void testSearchLongKeyword(WebDriver driver) throws InterruptedException {

        System.out.println("----- Edge Test: Long keyword search -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            searchBox.clear();

            searchBox.sendKeys("dressdressdressdressdressdressdressdress");

            searchBox.sendKeys(Keys.ENTER);

            Thread.sleep(3000);

            String page = driver.getPageSource().toLowerCase();

            if (page.contains("able to find") || page.contains("search results")) {
                System.out.println("Long keyword handled PASS");
            } else {
                System.out.println("Long keyword search FAIL");
            }

        } catch (Exception e) {

            System.out.println("Long keyword search FAILED");
            System.out.println("Reason: " + e.getMessage());

        }
    }public static void testSearchUsingIcon(WebDriver driver) throws InterruptedException {

        System.out.println("----- Edge Test: Search using icon -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(2500);

            WebElement searchBox = getSearchBox(driver);

            searchBox.click();
            searchBox.clear();
            searchBox.sendKeys("dress");

            WebElement searchIcon = driver.findElement(
                    By.cssSelector("button[type='submit']")
            );

            searchIcon.click();

            Thread.sleep(3000);

            if (driver.getCurrentUrl().toLowerCase().contains("searchterm")) {
                System.out.println("Search icon click PASS");
            } else {
                System.out.println("Search icon click FAIL");
            }

        } catch (Exception e) {

            System.out.println("Search icon test FAILED");
            System.out.println("Reason: " + e.getMessage());

        }
    }
}