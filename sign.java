package atoumationtest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class sign {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            testOpenHomeAndClickSignIn(driver, wait, js);
            testSignInFormElements(driver, wait, js);
            testEmptyLoginValidation(driver, wait, js);
            testInvalidLogin(driver, wait, js);
            testLongTextLogin(driver, wait, js);
            testNewCustomerRegistrationSection(driver, wait, js);
            testClickCreateAccount(driver, wait, js);
            testRegistration(driver, wait, js);
            testBillingStep(driver, wait, js);
        } finally {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    public static void testOpenHomeAndClickSignIn(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Open Home Page And Click Sign In -----");

        try {
            driver.get("https://www.garnethill.com/");
            System.out.println("Home page opened successfully");

            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .toString().equals("complete"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            Thread.sleep(3000);

            System.out.println("Home page fully loaded");

            WebElement signInBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[@title='Sign In' and contains(@href,'UserLogonView')]")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", signInBtn);
            Thread.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(signInBtn));

            try {
                signInBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInBtn);
            }

            System.out.println("Clicked Sign In");

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("UserLogonView"),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#signin-form")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#email"))));

            Thread.sleep(2000);

            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);

            boolean movedToSignIn =
                    currentUrl.contains("UserLogonView")
                    || !driver.findElements(By.cssSelector("form#signin-form")).isEmpty()
                    || !driver.findElements(By.cssSelector("input#email")).isEmpty();

            if (movedToSignIn) {
                System.out.println("Sign In Navigation PASS");
            } else {
                System.out.println("Sign In Navigation FAIL");
            }

        } catch (Exception e) {
            System.out.println("Sign In Navigation FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testSignInFormElements(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Sign In Form Elements -----");

        try {
            WebElement signInContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-login")));

            WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h2.c-login__heading")));

            WebElement signInForm = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("form#signin-form")));

            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#email")));

            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#password")));

            WebElement rememberMeCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#rememberMe")));

            WebElement signInButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button.login-button")));

            WebElement forgotPasswordLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(),'Forgot your password?')]")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", signInContainer);
            Thread.sleep(1000);

            String headingText = heading.getText().trim();
            String emailType = emailInput.getAttribute("type");
            String passwordType = passwordInput.getAttribute("type");
            String signInBtnText = signInButton.getText().trim();
            String forgotPasswordText = forgotPasswordLink.getText().trim();

            System.out.println("Heading            : " + headingText);
            System.out.println("Email Input Type   : " + emailType);
            System.out.println("Password Input Type: " + passwordType);
            System.out.println("Remember Me Found  : " + rememberMeCheckbox.isDisplayed());
            System.out.println("Sign In Button     : " + signInBtnText);
            System.out.println("Forgot Password    : " + forgotPasswordText);

            boolean passed =
                    signInContainer.isDisplayed()
                    && signInForm.isDisplayed()
                    && heading.isDisplayed()
                    && emailInput.isDisplayed()
                    && passwordInput.isDisplayed()
                    && rememberMeCheckbox.isDisplayed()
                    && signInButton.isDisplayed()
                    && forgotPasswordLink.isDisplayed();

            if (passed) {
                System.out.println("Sign In Form Elements PASS");
            } else {
                System.out.println("Sign In Form Elements FAIL");
            }

        } catch (Exception e) {
            System.out.println("Sign In Form Elements FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testEmptyLoginValidation(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Empty Email/Password -----");

        try {
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#email")));
            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#password")));
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.login-button")));

            emailInput.clear();
            passwordInput.clear();
            Thread.sleep(500);

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", signInButton);
            Thread.sleep(500);

            try {
                signInButton.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }

            Thread.sleep(2000);

            String currentUrl = driver.getCurrentUrl();
            String emailValue = emailInput.getAttribute("value");
            String passwordValue = passwordInput.getAttribute("value");
            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();

            boolean stayedOnLoginPage =
                    currentUrl.contains("UserLogonView")
                    || bodyText.contains("registered customers")
                    || !driver.findElements(By.cssSelector("form#signin-form")).isEmpty();

            boolean passed =
                    stayedOnLoginPage
                    && emailValue.isEmpty()
                    && passwordValue.isEmpty();

            if (passed) {
                System.out.println("Empty Email/Password PASS");
            } else {
                System.out.println("Empty Email/Password FAIL");
            }

        } catch (Exception e) {
            System.out.println("Empty Email/Password FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testInvalidLogin(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Invalid Email / Wrong Password -----");

        try {
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#email")));
            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#password")));
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.login-button")));

            emailInput.clear();
            passwordInput.clear();

            emailInput.sendKeys("loaa@@test");
            passwordInput.sendKeys("123");
            Thread.sleep(500);

            try {
                signInButton.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }

            Thread.sleep(2500);

            String currentUrl = driver.getCurrentUrl();
            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();

            boolean stayedOnLoginPage =
                    currentUrl.contains("UserLogonView")
                    || bodyText.contains("sign in")
                    || !driver.findElements(By.cssSelector("form#signin-form")).isEmpty();

            if (stayedOnLoginPage) {
                System.out.println("Invalid Email / Wrong Password PASS");
            } else {
                System.out.println("Invalid Email / Wrong Password FAIL");
            }

        } catch (Exception e) {
            System.out.println("Invalid Email / Wrong Password FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testLongTextLogin(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Long Text In Email/Password -----");

        try {
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#email")));
            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#password")));
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.login-button")));

            emailInput.clear();
            passwordInput.clear();

            String longEmail = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@test.com";
            String longPassword = "1234567890123456789012345678901234567890";

            emailInput.sendKeys(longEmail);
            passwordInput.sendKeys(longPassword);
            Thread.sleep(1000);

            String actualEmail = emailInput.getAttribute("value");
            String actualPassword = passwordInput.getAttribute("value");

            int emailLength = actualEmail.length();
            int passwordLength = actualPassword.length();

            try {
                signInButton.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }

            Thread.sleep(2000);

            boolean emailMaxWorked = emailLength <= 64;
            boolean passwordMaxWorked = passwordLength <= 32;

            if (emailMaxWorked && passwordMaxWorked) {
                System.out.println("Long Text Validation PASS");
            } else {
                System.out.println("Long Text Validation FAIL");
            }

        } catch (Exception e) {
            System.out.println("Long Text Validation FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testNewCustomerRegistrationSection(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing New Customer Registration Section -----");

        try {
            WebElement registrationSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.t-registration-panel__action")));

            WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h2.t-registration-panel__heading")));

            WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("p.t-registration-panel__description")));

            WebElement createAccountBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[data-analytics-name='register']")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", registrationSection);
            Thread.sleep(1000);

            String headingText = heading.getText().trim();
            String descText = description.getText().trim();
            String btnText = createAccountBtn.getText().trim();

            System.out.println("Heading        : " + headingText);
            System.out.println("Description    : " + descText);
            System.out.println("Button Text    : " + btnText);

            boolean passed =
                    registrationSection.isDisplayed()
                    && heading.isDisplayed()
                    && description.isDisplayed()
                    && createAccountBtn.isDisplayed()
                    && headingText.contains("New Customer")
                    && btnText.equalsIgnoreCase("Create an Account");

            if (passed) {
                System.out.println("New Customer Registration Section PASS");
            } else {
                System.out.println("New Customer Registration Section FAIL");
            }

        } catch (Exception e) {
            System.out.println("New Customer Registration Section FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testClickCreateAccount(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Click Create An Account -----");

        try {
            WebElement createAccountBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[data-analytics-name='register']")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", createAccountBtn);
            Thread.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(createAccountBtn));

            try {
                createAccountBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", createAccountBtn);
            }

            System.out.println("Clicked Create an Account");
            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();
            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();

            System.out.println("Current URL: " + currentUrl);

            boolean movedToRegister =
                    currentUrl.toLowerCase().contains("register")
                    || currentUrl.toLowerCase().contains("registration")
                    || currentUrl.toLowerCase().contains("createaccount")
                    || bodyText.contains("create an account")
                    || bodyText.contains("new customer registration")
                    || bodyText.contains("registration");

            if (movedToRegister) {
                System.out.println("Create Account Navigation PASS");
            } else {
                System.out.println("Create Account Navigation FAIL");
            }

        } catch (Exception e) {
            System.out.println("Create Account Navigation FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testRegistration(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- FULL REGISTRATION FLOW IN ONE FUNCTION -----");

        try {
            System.out.println("----- Testing Registration Page Elements -----");

            WebElement registrationTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h1#mainContentTitle, h1")));

            WebElement regForm = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("form#userRegAddressForm1")));

            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#logonId")));

            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#logonPassword")));

            WebElement verifyPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#logonPasswordVerify")));

            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("form#userRegAddressForm1 button[data-analytics-name='confirmation']")));

            WebElement verifyEmailInput = null;
            boolean verifyEmailExists = !driver.findElements(By.cssSelector("input#verifyLogonId")).isEmpty();
            boolean verifyEmailVisible = false;

            if (verifyEmailExists) {
                try {
                    verifyEmailInput = driver.findElement(By.cssSelector("input#verifyLogonId"));
                    verifyEmailVisible = verifyEmailInput.isDisplayed() && verifyEmailInput.isEnabled();
                } catch (Exception e) {
                    verifyEmailVisible = false;
                }
            }

            boolean emailNewsExists = !driver.findElements(By.cssSelector("input[name='sendMeEmails']")).isEmpty();
            WebElement emailNewsCheckbox = null;
            if (emailNewsExists) {
                emailNewsCheckbox = driver.findElement(By.cssSelector("input[name='sendMeEmails']"));
            }

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", registrationTitle);
            Thread.sleep(1000);

            System.out.println("Page Title         : " + registrationTitle.getText().trim());
            System.out.println("Email Input Found  : " + emailInput.isDisplayed());
            System.out.println("Verify Email Found : " + verifyEmailVisible);
            System.out.println("Password Found     : " + passwordInput.isDisplayed());
            System.out.println("Verify Pass Found  : " + verifyPasswordInput.isDisplayed());
            System.out.println("Email News Found   : " + emailNewsExists);
            if (emailNewsExists) {
                System.out.println("Email News Checked : " + emailNewsCheckbox.isSelected());
            }
            System.out.println("Continue Button    : " + continueBtn.getText().trim());

            boolean pageElementsPassed =
                    registrationTitle.isDisplayed()
                    && regForm.isDisplayed()
                    && emailInput.isDisplayed()
                    && passwordInput.isDisplayed()
                    && verifyPasswordInput.isDisplayed()
                    && continueBtn.isDisplayed();

            if (pageElementsPassed) {
                System.out.println("Registration Page Elements PASS");
            } else {
                System.out.println("Registration Page Elements FAIL");
            }

            System.out.println("----- Testing Empty Registration Validation -----");

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", emailInput);
            Thread.sleep(500);

            emailInput.click();
            emailInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            if (verifyEmailVisible) {
                verifyEmailInput.click();
                verifyEmailInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            }

            passwordInput.click();
            passwordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            verifyPasswordInput.click();
            verifyPasswordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            Thread.sleep(500);

            try {
                continueBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueBtn);
            }

            Thread.sleep(2000);

            boolean emailErrorExists = !driver.findElements(By.cssSelector("#errorMessage-logonId")).isEmpty();
            boolean passwordErrorExists = !driver.findElements(By.cssSelector("#errorMessage-logonPassword")).isEmpty();
            boolean verifyPasswordErrorExists = !driver.findElements(By.cssSelector("#errorMessage-logonPasswordVerify")).isEmpty();

            if (emailErrorExists) {
                System.out.println("Email Error        : "
                        + driver.findElement(By.cssSelector("#errorMessage-logonId")).getText().trim());
            }
            if (passwordErrorExists) {
                System.out.println("Password Error     : "
                        + driver.findElement(By.cssSelector("#errorMessage-logonPassword")).getText().trim());
            }
            if (verifyPasswordErrorExists) {
                System.out.println("Verify Pass Error  : "
                        + driver.findElement(By.cssSelector("#errorMessage-logonPasswordVerify")).getText().trim());
            }

            boolean emptyValidationPassed =
                    emailErrorExists
                    && passwordErrorExists
                    && verifyPasswordErrorExists;

            if (emptyValidationPassed) {
                System.out.println("Empty Registration Validation PASS");
            } else {
                System.out.println("Empty Registration Validation FAIL");
            }

            System.out.println("----- Testing Long Text In Registration -----");

            emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#logonId")));
            passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#logonPassword")));
            verifyPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#logonPasswordVerify")));

            if (verifyEmailExists) {
                try {
                    verifyEmailInput = driver.findElement(By.cssSelector("input#verifyLogonId"));
                    verifyEmailVisible = verifyEmailInput.isDisplayed() && verifyEmailInput.isEnabled();
                } catch (Exception e) {
                    verifyEmailVisible = false;
                }
            }

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", emailInput);
            Thread.sleep(500);

            String longEmail = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@test.com";
            String longPassword = "1234567890123456789012345678901234567890";

            emailInput.click();
            emailInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            emailInput.sendKeys(longEmail);

            if (verifyEmailVisible) {
                verifyEmailInput.click();
                verifyEmailInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                verifyEmailInput.sendKeys(longEmail);
            }

            passwordInput.click();
            passwordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            passwordInput.sendKeys(longPassword);

            verifyPasswordInput.click();
            verifyPasswordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            verifyPasswordInput.sendKeys(longPassword);

            Thread.sleep(1000);

            int actualEmailLength = emailInput.getAttribute("value").length();
            int actualVerifyEmailLength = verifyEmailVisible ? verifyEmailInput.getAttribute("value").length() : 0;
            int actualPasswordLength = passwordInput.getAttribute("value").length();
            int actualVerifyPasswordLength = verifyPasswordInput.getAttribute("value").length();

            boolean emailMaxWorked = actualEmailLength <= 64;
            boolean verifyEmailMaxWorked = !verifyEmailVisible || actualVerifyEmailLength <= 64;
            boolean passwordMaxWorked = actualPasswordLength <= 32;
            boolean verifyPasswordMaxWorked = actualVerifyPasswordLength <= 32;

            if (emailMaxWorked && verifyEmailMaxWorked && passwordMaxWorked && verifyPasswordMaxWorked) {
                System.out.println("Long Text Registration PASS");
            } else {
                System.out.println("Long Text Registration FAIL");
            }

            System.out.println("----- Filling Registration Step 1 And Continue -----");

            emailInput.click();
            emailInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            emailInput.sendKeys("testuser123@mail.com");

            if (verifyEmailVisible) {
                verifyEmailInput.click();
                verifyEmailInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                verifyEmailInput.sendKeys("testuser123@mail.com");
            }

            passwordInput.click();
            passwordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            passwordInput.sendKeys("Test@12345");

            verifyPasswordInput.click();
            verifyPasswordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            verifyPasswordInput.sendKeys("Test@12345");

            Thread.sleep(1000);

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
            Thread.sleep(1000);

            try {
                continueBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueBtn);
            }

            Thread.sleep(3000);

            boolean movedToStep2 =
                    !driver.findElements(By.cssSelector("div.t-registration__step2")).isEmpty()
                    || !driver.findElements(By.cssSelector("input#bill_fName")).isEmpty()
                    || !driver.findElements(By.cssSelector("form#userRegAddressForm2")).isEmpty()
                    || !driver.findElements(By.cssSelector("input[name='bill_fName']")).isEmpty();

            if (movedToStep2) {
                System.out.println("Registration Step 1 Continue PASS");
            } else {
                System.out.println("Registration Step 1 Continue FAIL");
            }

            System.out.println("----- FULL REGISTRATION FLOW FINISHED -----");

        } catch (Exception e) {
            System.out.println("FULL REGISTRATION FLOW FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testBillingStep(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("==================================================");
        System.out.println("----- FULL BILLING FLOW IN ONE FUNCTION -----");
        System.out.println("==================================================");

        try {

            System.out.println("----- Testing Billing Page Elements -----");

            WebElement billingTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h3.t-registration__billing-title")));

            WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_fName")));
            WebElement middleInitial = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_mdName")));
            WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_lName")));
            WebElement address1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_strAdd1")));
            WebElement address2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_strAdd2")));
            WebElement company = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_addrCompany")));
            WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_postalCode")));
            WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_city")));
            WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("select#bill_region")));
            WebElement country = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("select#bill_country")));
            WebElement phone1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_phone1")));
            WebElement phone2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#bill_phone2")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", billingTitle);
            Thread.sleep(1000);
            js.executeScript("window.scrollBy(0, 250);");
            Thread.sleep(500);

            System.out.println("Page Title        : " + billingTitle.getText().trim());
            System.out.println("First Name Found  : " + firstName.isDisplayed());
            System.out.println("Middle Init Found : " + middleInitial.isDisplayed());
            System.out.println("Last Name Found   : " + lastName.isDisplayed());
            System.out.println("Address 1 Found   : " + address1.isDisplayed());
            System.out.println("Address 2 Found   : " + address2.isDisplayed());
            System.out.println("Company Found     : " + company.isDisplayed());
            System.out.println("Zip Found         : " + zip.isDisplayed());
            System.out.println("City Found        : " + city.isDisplayed());
            System.out.println("State Found       : " + state.isDisplayed());
            System.out.println("Country Found     : " + country.isDisplayed());
            System.out.println("Phone 1 Found     : " + phone1.isDisplayed());
            System.out.println("Phone 2 Found     : " + phone2.isDisplayed());

            boolean billingElementsPass =
                    billingTitle.isDisplayed()
                    && firstName.isDisplayed()
                    && lastName.isDisplayed()
                    && address1.isDisplayed()
                    && zip.isDisplayed()
                    && city.isDisplayed()
                    && state.isDisplayed()
                    && country.isDisplayed()
                    && phone1.isDisplayed();

            if (billingElementsPass) {
                System.out.println("Billing Page Elements PASS");
            } else {
                System.out.println("Billing Page Elements FAIL");
            }

            System.out.println("----- Testing Empty Billing Validation -----");

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstName);
            Thread.sleep(500);
            js.executeScript("window.scrollBy(0, 250);");
            Thread.sleep(500);

            js.executeScript("arguments[0].click();", firstName);
            firstName.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", middleInitial);
            middleInitial.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", lastName);
            lastName.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", address1);
            address1.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", address2);
            address2.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", company);
            company.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", zip);
            zip.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", city);
            city.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", phone1);
            phone1.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            js.executeScript("arguments[0].click();", phone2);
            phone2.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

            Select stateSelect = new Select(state);
            stateSelect.selectByVisibleText("State");

            Select countrySelect = new Select(country);
            countrySelect.selectByVisibleText("Select Country");

            Thread.sleep(500);

            WebElement continueBtn = null;
            if (!driver.findElements(By.cssSelector("button[type='submit']")).isEmpty()) {
                continueBtn = driver.findElement(By.cssSelector("button[type='submit']"));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
                Thread.sleep(500);
                js.executeScript("window.scrollBy(0, 250);");
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", continueBtn);
            }

            Thread.sleep(2000);

            boolean firstNameError = !driver.findElements(By.cssSelector("[id*='bill_fName'][id*='error'], #errorMessage-bill_fName")).isEmpty();
            boolean lastNameError = !driver.findElements(By.cssSelector("[id*='bill_lName'][id*='error'], #errorMessage-bill_lName")).isEmpty();
            boolean addressError = !driver.findElements(By.cssSelector("[id*='bill_strAdd1'][id*='error'], #errorMessage-bill_strAdd1")).isEmpty();
            boolean zipError = !driver.findElements(By.cssSelector("[id*='bill_postalCode'][id*='error'], #errorMessage-bill_postalCode")).isEmpty();
            boolean cityError = !driver.findElements(By.cssSelector("[id*='bill_city'][id*='error'], #errorMessage-bill_city")).isEmpty();
            boolean phoneError = !driver.findElements(By.cssSelector("[id*='bill_phone1'][id*='error'], #errorMessage-bill_phone1")).isEmpty();

            System.out.println("First Name Error  : " + firstNameError);
            System.out.println("Last Name Error   : " + lastNameError);
            System.out.println("Address Error     : " + addressError);
            System.out.println("Zip Error         : " + zipError);
            System.out.println("City Error        : " + cityError);
            System.out.println("Phone Error       : " + phoneError);

            if (firstNameError || lastNameError || addressError || zipError || cityError || phoneError) {
                System.out.println("Empty Billing Validation PASS");
            } else {
                System.out.println("Empty Billing Validation FAIL");
            }

            System.out.println("----- Testing Long Text In Billing -----");

            String longText = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            String longZip = "12345678901234567890";
            String longPhone = "123456789012345678901234567890";

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstName);
            Thread.sleep(500);
            js.executeScript("window.scrollBy(0, 250);");
            Thread.sleep(500);

            js.executeScript("arguments[0].click();", firstName);
            firstName.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            firstName.sendKeys(longText);

            js.executeScript("arguments[0].click();", middleInitial);
            middleInitial.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            middleInitial.sendKeys(longText);

            js.executeScript("arguments[0].click();", lastName);
            lastName.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            lastName.sendKeys(longText);

            js.executeScript("arguments[0].click();", address1);
            address1.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            address1.sendKeys(longText);

            js.executeScript("arguments[0].click();", address2);
            address2.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            address2.sendKeys(longText);

            js.executeScript("arguments[0].click();", company);
            company.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            company.sendKeys(longText);

            js.executeScript("arguments[0].click();", zip);
            zip.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            zip.sendKeys(longZip);

            js.executeScript("arguments[0].click();", city);
            city.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            city.sendKeys(longText);

            js.executeScript("arguments[0].click();", phone1);
            phone1.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            phone1.sendKeys(longPhone);

            js.executeScript("arguments[0].click();", phone2);
            phone2.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            phone2.sendKeys(longPhone);

            Thread.sleep(1000);

            int actualFirstNameLength = firstName.getAttribute("value").length();
            int actualMiddleLength = middleInitial.getAttribute("value").length();
            int actualLastNameLength = lastName.getAttribute("value").length();
            int actualAddress1Length = address1.getAttribute("value").length();
            int actualAddress2Length = address2.getAttribute("value").length();
            int actualCompanyLength = company.getAttribute("value").length();
            int actualCityLength = city.getAttribute("value").length();

            boolean firstNameMaxWorked = actualFirstNameLength <= 15;
            boolean middleInitialMaxWorked = actualMiddleLength <= 1;
            boolean lastNameMaxWorked = actualLastNameLength <= 25;
            boolean address1MaxWorked = actualAddress1Length <= 30;
            boolean address2MaxWorked = actualAddress2Length <= 30;
            boolean companyMaxWorked = actualCompanyLength <= 20;
            boolean cityMaxWorked = actualCityLength <= 25;

            if (firstNameMaxWorked
                    && middleInitialMaxWorked
                    && lastNameMaxWorked
                    && address1MaxWorked
                    && address2MaxWorked
                    && companyMaxWorked
                    && cityMaxWorked) {
                System.out.println("Long Text Billing PASS");
            } else {
                System.out.println("Long Text Billing FAIL");
            }

            System.out.println("----- Filling Billing Data -----");

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstName);
            Thread.sleep(500);
            js.executeScript("window.scrollBy(0, 250);");
            Thread.sleep(500);

            js.executeScript("arguments[0].click();", firstName);
            firstName.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            firstName.sendKeys("Test");

            js.executeScript("arguments[0].click();", middleInitial);
            middleInitial.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            middleInitial.sendKeys("A");

            js.executeScript("arguments[0].click();", lastName);
            lastName.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            lastName.sendKeys("User");

            js.executeScript("arguments[0].click();", address1);
            address1.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            address1.sendKeys("123 Test Street");

            js.executeScript("arguments[0].click();", address2);
            address2.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            address2.sendKeys("Apt 4B");

            js.executeScript("arguments[0].click();", company);
            company.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            company.sendKeys("TestCo");

            js.executeScript("arguments[0].click();", zip);
            zip.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            zip.sendKeys("10001");

            js.executeScript("arguments[0].click();", city);
            city.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            city.sendKeys("New York");

            stateSelect = new Select(state);
            stateSelect.selectByValue("NY");

            countrySelect = new Select(country);
            countrySelect.selectByValue("US");

            js.executeScript("arguments[0].click();", phone1);
            phone1.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            phone1.sendKeys("1234567890");

            js.executeScript("arguments[0].click();", phone2);
            phone2.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            phone2.sendKeys("0987654321");

            Thread.sleep(1000);

            boolean fillPass =
                    firstName.getAttribute("value").length() > 0
                    && lastName.getAttribute("value").length() > 0
                    && address1.getAttribute("value").length() > 0
                    && zip.getAttribute("value").length() > 0
                    && city.getAttribute("value").length() > 0
                    && phone1.getAttribute("value").length() > 0;

            if (fillPass) {
                System.out.println("Billing Fill PASS");
            } else {
                System.out.println("Billing Fill FAIL");
            }

            System.out.println("----- FULL BILLING FLOW FINISHED -----");

        } catch (Exception e) {
            System.out.println("FULL BILLING FLOW FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    
}