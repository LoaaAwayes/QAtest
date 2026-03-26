package atoumationtest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class footerTest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.garnethill.com/");
        System.out.println("Website opened successfully");
        Thread.sleep(3000);

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);

        // ----- Footer Visibility -----
        try {
            System.out.println("----- Testing Footer Visibility -----");

            WebElement footer = driver.findElement(By.cssSelector(".c-main-footer-wrapper"));

            if (footer.isDisplayed()) {
                System.out.println("Footer PASS");
            } else {
                System.out.println("Footer FAIL");
            }

        } catch (Exception e) {
            System.out.println("Footer FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Connect Title -----
        try {
            System.out.println("----- Testing Connect Title -----");

            WebElement connectTitle = driver.findElement(By.cssSelector(".c-footer-signup__column-title"));

            if (connectTitle.isDisplayed() && connectTitle.getText().trim().equalsIgnoreCase("Connect")) {
                System.out.println("Connect title PASS");
            } else {
                System.out.println("Connect title FAIL");
            }

        } catch (Exception e) {
            System.out.println("Connect title FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Join Our Email List Title -----
        try {
            System.out.println("----- Testing Join Our Email List Title -----");

            WebElement title = driver.findElement(By.cssSelector(".c-footer-signup__form-title"));

            if (title.isDisplayed() && title.getText().trim().equalsIgnoreCase("Join our email list")) {
                System.out.println("Join our email list PASS");
            } else {
                System.out.println("Join our email list FAIL");
            }

        } catch (Exception e) {
            System.out.println("Join our email list FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Email Input Visibility -----
        try {
            System.out.println("----- Testing Email Input Visibility -----");

            WebElement emailInput = driver.findElement(By.cssSelector(".c-footer-signup__form-container-input"));

            if (emailInput.isDisplayed()) {
                System.out.println("Email input visibility PASS");
            } else {
                System.out.println("Email input visibility FAIL");
            }

        } catch (Exception e) {
            System.out.println("Email input visibility FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Email Input Type And Max Length -----
        try {
            System.out.println("----- Testing Email Input Type And Max Length -----");

            WebElement emailInput = driver.findElement(By.cssSelector(".c-footer-signup__form-container-input"));

            String type = emailInput.getAttribute("type");
            String maxLength = emailInput.getAttribute("maxlength");

            if ("email".equalsIgnoreCase(type) && "64".equals(maxLength)) {
                System.out.println("Email input attributes PASS");
            } else {
                System.out.println("Email input attributes FAIL");
                System.out.println("Type      : " + type);
                System.out.println("MaxLength : " + maxLength);
            }

        } catch (Exception e) {
            System.out.println("Email input attributes FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Email Input Accepts Text -----
        try {
            System.out.println("----- Testing Email Input Accepts Text -----");

            WebElement emailInput = driver.findElement(By.cssSelector(".c-footer-signup__form-container-input"));
            emailInput.clear();
            Thread.sleep(1000);
            emailInput.sendKeys("test@gmail.com");
            Thread.sleep(1000);

            String actualValue = emailInput.getAttribute("value");

            if ("test@gmail.com".equals(actualValue)) {
                System.out.println("Email input text PASS");
            } else {
                System.out.println("Email input text FAIL");
                System.out.println("Actual value: " + actualValue);
            }

        } catch (Exception e) {
            System.out.println("Email input text FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Empty Subscribe -----
        try {
            System.out.println("----- Testing Empty Subscribe -----");

            WebElement emailInput = driver.findElement(By.cssSelector(".c-footer-signup__form-container-input"));
            WebElement subscribeBtn = driver.findElement(By.id("universalFooterEmailSignUpSubmit"));

            emailInput.clear();
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", subscribeBtn);
            Thread.sleep(2000);

            String valueAfterClick = emailInput.getAttribute("value");

            if (valueAfterClick.trim().isEmpty()) {
                System.out.println("Empty subscribe PASS");
            } else {
                System.out.println("Empty subscribe FAIL");
            }

        } catch (Exception e) {
            System.out.println("Empty subscribe FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Invalid Email Subscribe -----
        try {
            System.out.println("----- Testing Invalid Email Subscribe -----");

            WebElement emailInput = driver.findElement(By.cssSelector(".c-footer-signup__form-container-input"));
            WebElement subscribeBtn = driver.findElement(By.id("universalFooterEmailSignUpSubmit"));

            emailInput.clear();
            Thread.sleep(1000);
            emailInput.sendKeys("test");
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", subscribeBtn);
            Thread.sleep(2000);

            String enteredValue = emailInput.getAttribute("value");

            if ("test".equals(enteredValue)) {
                System.out.println("Invalid email subscribe PASS");
            } else {
                System.out.println("Invalid email subscribe FAIL");
            }

        } catch (Exception e) {
            System.out.println("Invalid email subscribe FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Subscribe Button Visibility -----
        try {
            System.out.println("----- Testing Subscribe Button Visibility -----");

            WebElement subscribeBtn = driver.findElement(By.id("universalFooterEmailSignUpSubmit"));

            if (subscribeBtn.isDisplayed()) {
                System.out.println("Subscribe button visibility PASS");
            } else {
                System.out.println("Subscribe button visibility FAIL");
            }

        } catch (Exception e) {
            System.out.println("Subscribe button visibility FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Footer Description -----
        try {
            System.out.println("----- Testing Footer Description -----");

            WebElement desc = driver.findElement(By.cssSelector(".c-footer-signup__form-description"));

            if (desc.isDisplayed() && !desc.getText().trim().isEmpty()) {
                System.out.println("Footer description PASS");
                System.out.println("Description: " + desc.getText().trim());
            } else {
                System.out.println("Footer description FAIL");
            }

        } catch (Exception e) {
            System.out.println("Footer description FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Social Icons Visibility -----
        try {
            System.out.println("----- Testing Social Icons Visibility -----");

            WebElement facebook = driver.findElement(By.cssSelector("a[data-analytics-name='facebook']"));
            WebElement instagram = driver.findElement(By.cssSelector("a[data-analytics-name='instagram']"));
            WebElement pinterest = driver.findElement(By.cssSelector("a[data-analytics-name='pinterest']"));

            if (facebook.isDisplayed() && instagram.isDisplayed() && pinterest.isDisplayed()) {
                System.out.println("Social icons visibility PASS");
            } else {
                System.out.println("Social icons visibility FAIL");
            }

        } catch (Exception e) {
            System.out.println("Social icons visibility FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Social Icons Count -----
        try {
            System.out.println("----- Testing Social Icons Count -----");

            List<WebElement> socialLinks = driver.findElements(By.cssSelector(".c-footer-social__link"));

            if (socialLinks.size() == 3) {
                System.out.println("Social icons count PASS");
            } else {
                System.out.println("Social icons count FAIL");
                System.out.println("Found: " + socialLinks.size());
            }

        } catch (Exception e) {
            System.out.println("Social icons count FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Social Links Href -----
        try {
            System.out.println("----- Testing Social Links Href -----");

            WebElement facebook = driver.findElement(By.cssSelector("a[data-analytics-name='facebook']"));
            WebElement instagram = driver.findElement(By.cssSelector("a[data-analytics-name='instagram']"));
            WebElement pinterest = driver.findElement(By.cssSelector("a[data-analytics-name='pinterest']"));

            String facebookHref = facebook.getAttribute("href");
            String instagramHref = instagram.getAttribute("href");
            String pinterestHref = pinterest.getAttribute("href");

            if (facebookHref != null && facebookHref.contains("facebook")
                    && instagramHref != null && instagramHref.contains("instagram")
                    && pinterestHref != null && pinterestHref.contains("pinterest")) {
                System.out.println("Social links href PASS");
            } else {
                System.out.println("Social links href FAIL");
                System.out.println("Facebook  : " + facebookHref);
                System.out.println("Instagram : " + instagramHref);
                System.out.println("Pinterest : " + pinterestHref);
            }

        } catch (Exception e) {
            System.out.println("Social links href FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Customer Service Section -----
        try {
            System.out.println("----- Testing Customer Service Section -----");

            WebElement section = driver.findElement(By.xpath("//*[normalize-space()='Customer Service']"));

            if (section.isDisplayed()) {
                System.out.println("Customer Service section PASS");
            } else {
                System.out.println("Customer Service section FAIL");
            }

        } catch (Exception e) {
            System.out.println("Customer Service section FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Our Company Section -----
        try {
            System.out.println("----- Testing Our Company Section -----");

            WebElement section = driver.findElement(By.xpath("//*[normalize-space()='Our Company']"));

            if (section.isDisplayed()) {
                System.out.println("Our Company section PASS");
            } else {
                System.out.println("Our Company section FAIL");
            }

        } catch (Exception e) {
            System.out.println("Our Company section FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Shopping Tools Section -----
        try {
            System.out.println("----- Testing Shopping Tools Section -----");

            WebElement section = driver.findElement(By.xpath("//*[normalize-space()='Shopping Tools']"));

            if (section.isDisplayed()) {
                System.out.println("Shopping Tools section PASS");
            } else {
                System.out.println("Shopping Tools section FAIL");
            }

        } catch (Exception e) {
            System.out.println("Shopping Tools section FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Legal Section -----
        try {
            System.out.println("----- Testing Legal Section -----");

            WebElement legal = driver.findElement(By.xpath("//*[normalize-space()='Legal']"));

            if (legal.isDisplayed()) {
                System.out.println("Legal section PASS");
            } else {
                System.out.println("Legal section FAIL");
            }

        } catch (Exception e) {
            System.out.println("Legal section FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Copyright -----
        try {
            System.out.println("----- Testing Copyright -----");

            WebElement copyright = driver.findElement(By.cssSelector(".c-footer-copyright__text"));

            if (copyright.isDisplayed()) {
                System.out.println("Copyright PASS");
                System.out.println("Copyright text: " + copyright.getText());
            } else {
                System.out.println("Copyright FAIL");
            }

        } catch (Exception e) {
            System.out.println("Copyright FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Footer Offerings -----
        try {
            System.out.println("----- Testing Footer Offerings -----");

            List<WebElement> offerings = driver.findElements(By.cssSelector(".c-footer-offerings__item"));

            if (offerings.size() == 4) {
                System.out.println("Offerings count PASS");
            } else {
                System.out.println("Offerings count FAIL");
                System.out.println("Found: " + offerings.size());
            }

            for (WebElement item : offerings) {
                try {
                    String text = item.getText().trim().replaceAll("\\s+", " ");
                    System.out.println("Found offering: " + text);

                    if (item.isDisplayed()) {
                        System.out.println("Offering visible PASS");
                    } else {
                        System.out.println("Offering visible FAIL");
                    }

                } catch (Exception e) {
                    System.out.println("Offering check FAIL");
                }
            }

        } catch (Exception e) {
            System.out.println("Footer Offerings FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Contact Section -----
        try {
            System.out.println("----- Testing Contact Section -----");

            List<WebElement> contactItems = driver.findElements(By.cssSelector(".c-footer-contact__item"));

            if (contactItems.size() >= 2) {
                System.out.println("Contact items count PASS");
            } else {
                System.out.println("Contact items count FAIL");
                System.out.println("Found: " + contactItems.size());
            }

            WebElement contactUs = driver.findElement(By.xpath("//*[normalize-space()='Contact Us']"));
            WebElement phone = driver.findElement(By.xpath("//*[contains(normalize-space(),'800.870.3513')]"));

            if (contactUs.isDisplayed() && phone.isDisplayed()) {
                System.out.println("Contact section PASS");
            } else {
                System.out.println("Contact section FAIL");
            }

        } catch (Exception e) {
            System.out.println("Contact section FAIL");
            System.out.println("Reason: " + e.getMessage());
        }

        // ----- Offerings Navigation -----
        testFooterOffering(driver, js, "Digital Catalog");
        testFooterOffering(driver, js, "Store Locations");
        testFooterOffering(driver, js, "Shopping Guides");
        testFooterOffering(driver, js, "Design Services");

        // ----- Contact Navigation -----
        testFooterContactLink(driver, js, "Contact Us");
        testPhoneLink(driver, js, "800.870.3513");

        // ----- Social Links -----
        testSocialLink(driver, js, "facebook");
        testSocialLink(driver, js, "instagram");
        testSocialLink(driver, js, "pinterest");

        // ----- Footer Links Navigation -----
        testFooterLink(driver, js, "Contact Us");
        testFooterLink(driver, js, "Order Status");
        testFooterLink(driver, js, "Shipping & Handling");
        testFooterLink(driver, js, "Returns & Exchanges");

        testFooterLink(driver, js, "About Us");
        testFooterLink(driver, js, "Our Brands");
        testFooterLink(driver, js, "Our Responsibility");
        testFooterLink(driver, js, "Our Stores");

        testFooterLink(driver, js, "Digital Catalog");
        testFooterLink(driver, js, "Weekly Sale");
        testFooterLink(driver, js, "Request a Catalog");
        testFooterLink(driver, js, "Wish List");
        testFooterLink(driver, js, "Gift Cards");

        // ----- Legal Links Navigation -----
        testLegalLink(driver, js, "Accessibility");
        testLegalLink(driver, js, "CA Supply Chains Transparency");
        testLegalLink(driver, js, "Conditions Of Use");
        testLegalLink(driver, js, "Privacy & Security");
        testLegalLink(driver, js, "Your Privacy Choices");

        driver.quit();
    }

    public static void testFooterOffering(WebDriver driver, JavascriptExecutor js,
            String itemText) throws InterruptedException {

        System.out.println("----- Testing " + itemText + " Offering -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(3000);

            String beforeClick = driver.getCurrentUrl();
            boolean clicked = false;

            List<WebElement> items = driver.findElements(By.cssSelector(".c-footer-offerings__item"));

            for (WebElement item : items) {
                try {
                    String text = item.getText().trim().replaceAll("\\s+", " ");

                    if (text.toLowerCase().contains(itemText.toLowerCase()) && item.isDisplayed()) {
                        js.executeScript("arguments[0].click();", item);
                        clicked = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }

            Thread.sleep(3000);

            String afterClick = driver.getCurrentUrl();

            if (!clicked) {
                System.out.println(itemText + " not found - FAIL");
            } else if (!afterClick.equals(beforeClick)) {
                System.out.println(itemText + " navigation PASS");
                System.out.println("Opened page: " + afterClick);
            } else {
                System.out.println(itemText + " navigation FAIL");
                System.out.println("Opened page: " + afterClick);
            }

        } catch (Exception e) {
            System.out.println(itemText + " FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testFooterContactLink(WebDriver driver, JavascriptExecutor js,
            String linkText) throws InterruptedException {

        System.out.println("----- Testing " + linkText + " Contact Link -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(3000);

            String beforeClick = driver.getCurrentUrl();
            boolean clicked = false;

            List<WebElement> items = driver.findElements(By.cssSelector(".c-footer-contact__item"));

            for (WebElement item : items) {
                try {
                    String text = item.getText().trim().replaceAll("\\s+", " ");

                    if (text.equalsIgnoreCase(linkText) && item.isDisplayed()) {
                        js.executeScript("arguments[0].click();", item);
                        clicked = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }

            Thread.sleep(3000);

            String afterClick = driver.getCurrentUrl();

            if (!clicked) {
                System.out.println(linkText + " contact link not found - FAIL");
            } else if (!afterClick.equals(beforeClick)) {
                System.out.println(linkText + " contact navigation PASS");
                System.out.println("Opened page: " + afterClick);
            } else {
                System.out.println(linkText + " contact navigation FAIL");
                System.out.println("Opened page: " + afterClick);
            }

        } catch (Exception e) {
            System.out.println(linkText + " contact test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testPhoneLink(WebDriver driver, JavascriptExecutor js,
            String phoneText) throws InterruptedException {

        System.out.println("----- Testing " + phoneText + " Phone Link -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(3000);

            boolean found = false;

            List<WebElement> items = driver.findElements(By.cssSelector(".c-footer-contact__item"));

            for (WebElement item : items) {
                try {
                    String text = item.getText().trim().replaceAll("\\s+", " ");
                    String href = item.getAttribute("href");

                    if (text.contains(phoneText) && href != null && href.startsWith("tel:")) {
                        found = true;
                        System.out.println("Phone link PASS");
                        System.out.println("Href: " + href);
                        break;
                    }
                } catch (Exception e) {
                }
            }

            if (!found) {
                System.out.println("Phone link FAIL");
            }

        } catch (Exception e) {
            System.out.println("Phone link FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testSocialLink(WebDriver driver, JavascriptExecutor js,
            String socialName) throws InterruptedException {

        System.out.println("----- Testing " + socialName + " Social Link -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(3000);

            WebElement socialLink = driver.findElement(
                    By.cssSelector("a[data-analytics-name='" + socialName + "']")
            );

            String href = socialLink.getAttribute("href");
            String target = socialLink.getAttribute("target");
            String aria = socialLink.getAttribute("aria-label");

            if (socialLink.isDisplayed()
                    && href != null && href.toLowerCase().contains(socialName)
                    && target != null && target.equals("_blank")
                    && aria != null && !aria.trim().isEmpty()) {
                System.out.println(socialName + " social link PASS");
                System.out.println("Href: " + href);
            } else {
                System.out.println(socialName + " social link FAIL");
                System.out.println("Href   : " + href);
                System.out.println("Target : " + target);
                System.out.println("Aria   : " + aria);
            }

        } catch (Exception e) {
            System.out.println(socialName + " social link FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testFooterLink(WebDriver driver, JavascriptExecutor js,
            String linkText) throws InterruptedException {

        System.out.println("----- Testing " + linkText + " Link -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(3000);

            String beforeClick = driver.getCurrentUrl();
            boolean clicked = false;

            List<WebElement> links = driver.findElements(By.cssSelector(".c-footer-columns__link"));

            for (WebElement link : links) {
                try {
                    String text = link.getText().trim().replaceAll("\\s+", " ");

                    if (text.equalsIgnoreCase(linkText) && link.isDisplayed()) {
                        js.executeScript("arguments[0].click();", link);
                        clicked = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }

            Thread.sleep(3000);

            String afterClick = driver.getCurrentUrl();

            if (!clicked) {
                System.out.println(linkText + " link not found - FAIL");
            } else if (!afterClick.equals(beforeClick)) {
                System.out.println(linkText + " navigation PASS");
                System.out.println("Opened page: " + afterClick);
            } else {
                System.out.println(linkText + " navigation FAIL");
                System.out.println("Opened page: " + afterClick);
            }

        } catch (Exception e) {
            System.out.println(linkText + " test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testLegalLink(WebDriver driver, JavascriptExecutor js,
            String linkText) throws InterruptedException {

        System.out.println("----- Testing " + linkText + " Legal Link -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(3000);

            String beforeClick = driver.getCurrentUrl();
            boolean clicked = false;

            List<WebElement> legalLinks = driver.findElements(By.cssSelector(".c-footer-legal-links__link"));

            for (WebElement link : legalLinks) {
                try {
                    String text = link.getText().trim().replaceAll("\\s+", " ");

                    if (text.equalsIgnoreCase(linkText) && link.isDisplayed()) {
                        js.executeScript("arguments[0].click();", link);
                        clicked = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }

            Thread.sleep(3000);

            String afterClick = driver.getCurrentUrl();

            if (!clicked) {
                System.out.println(linkText + " legal link not found - FAIL");
            } else if (!afterClick.equals(beforeClick)) {
                System.out.println(linkText + " legal navigation PASS");
                System.out.println("Opened page: " + afterClick);
            } else {
                System.out.println(linkText + " legal navigation FAIL");
                System.out.println("Opened page: " + afterClick);
            }

        } catch (Exception e) {
            System.out.println(linkText + " legal test failed - FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}