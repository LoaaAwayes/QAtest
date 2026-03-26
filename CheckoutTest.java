package atoumationtest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutTest {

    static class ProductData {
        String title = "";
        String itemNumber = "";
        String color = "";
        String size = "";
        String qty = "";
        String price = "";
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            testCheckoutSummaryFlow(driver, wait, js);
            testLoginValidations(driver, wait, js);
            testForgotPassword(driver, wait, js);

            driver.get("https://www.garnethill.com/SinglePageCheckoutView?storeId=10054&langId=-1&catalogId=10054");
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .toString().equals("complete"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[data-analytics-name='continue-as-guest']")));

            testContinueAsGuest(driver, wait, js);
            testShippingFormInputsAndContinue(driver, wait, js);
            testDeliveryMethodStep(driver, wait, js);
            testSelectDeliveryMethodAndContinueToPayment(driver, wait, js);
        } finally {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    public static void testCheckoutSummaryFlow(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Full Checkout Flow -----");

        driver.get("https://www.garnethill.com/");
        System.out.println("Home page opened successfully");

        cart.waitForHomePage(driver, wait);
        cart.openCartFromHome(driver, wait, js);
        cart.openCartAndWaitForRealContent(driver, wait);

        String countText = "";
        String msgText = "";

        try {
            countText = driver.findElement(By.cssSelector("span.t-cart__header-count")).getText().trim();
        } catch (Exception e) {
        }

        try {
            msgText = driver.findElement(By.cssSelector("div.cart-empty-text")).getText().trim();
        } catch (Exception e) {
        }

        boolean cartIsEmpty =
                countText.equalsIgnoreCase("0 Items")
                || msgText.equalsIgnoreCase("Your Bag Is Empty");

        if (cartIsEmpty) {
            System.out.println("Cart is empty -> add one item only");

            WebElement continueShoppingBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.t-cart__continue-shopping")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueShoppingBtn);
            Thread.sleep(1000);

            try {
                continueShoppingBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueShoppingBtn);
            }

            cart.waitForPageReady(driver, wait);
            Thread.sleep(3000);

            cart.openProductAndAddToBagByIndex(driver, wait, js, 0);

            driver.get("https://www.garnethill.com/");
            cart.waitForHomePage(driver, wait);
            cart.openCartFromHome(driver, wait, js);
            cart.openCartAndWaitForRealContent(driver, wait);
            cart.verifyItemAddedToCart(driver, wait);
        }

        ProductData cartData = getCartItemData(driver, wait, js);

        clickCheckoutNow(driver, wait, js);

        testCheckoutSummaryMatchesCart(driver, wait, js, cartData);
    }

    public static ProductData getCartItemData(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Reading Cart Item Data -----");

        ProductData data = new ProductData();

        try {
            WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.order-product-item-container")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", cartItem);
            Thread.sleep(1000);

            data.title = cartItem.findElement(
                    By.cssSelector("p.c-product-card__title"))
                    .getText().trim().toLowerCase();

            data.itemNumber = cartItem.findElement(
                    By.cssSelector("span.c-product-details__item-number"))
                    .getText().trim().toLowerCase();

            List<WebElement> options = cartItem.findElements(
                    By.cssSelector("div.c-product-details__option"));

            for (WebElement option : options) {
                String optionName = option.findElement(
                        By.cssSelector("span.c-product-details__option-name"))
                        .getText().trim().toLowerCase();

                String optionValue = option.findElement(
                        By.cssSelector("span.c-product-details__option-value"))
                        .getText().trim().toLowerCase();

                if (optionName.equals("color")) {
                    data.color = optionValue;
                } else if (optionName.equals("size")) {
                    data.size = optionValue;
                }
            }

            data.qty = cartItem.findElement(
                    By.cssSelector("div.c-universal-product-details__quantity-value"))
                    .getText().trim().toLowerCase();

            data.price = normalizePrice(
                    cartItem.findElement(By.cssSelector("div.c-product-card-price .price"))
                            .getAttribute("aria-label")
            );

            System.out.println("Cart Title      : " + data.title);
            System.out.println("Cart Item No.   : " + data.itemNumber);
            System.out.println("Cart Color      : " + data.color);
            System.out.println("Cart Size       : " + data.size);
            System.out.println("Cart Qty        : " + data.qty);
            System.out.println("Cart Price      : " + data.price);

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Could not read cart item data: " + e.getMessage());
        }
    }

    public static void clickCheckoutNow(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Clicking Checkout Now -----");

        try {
            WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.c-checkout-buttons__checkout")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", checkoutBtn);
            Thread.sleep(1000);

            try {
                checkoutBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", checkoutBtn);
            }

            cart.waitForPageReady(driver, wait);

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("SinglePageCheckoutView"),
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.c-checkout-desktop-summary-details__container"))
            ));

            Thread.sleep(3000);

            System.out.println("Checkout Now PASS");
            System.out.println("Current URL: " + driver.getCurrentUrl());

        } catch (Exception e) {
            System.out.println("Checkout Now FAIL");
            System.out.println("Reason: " + e.getMessage());
            throw e;
        }
    }

    public static void testCheckoutSummaryMatchesCart(WebDriver driver, WebDriverWait wait,
            JavascriptExecutor js, ProductData cartData) throws InterruptedException {

        System.out.println("----- Testing Checkout Summary Matches Cart Data -----");

        try {
            WebElement summaryBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-checkout-desktop-summary-details__container")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", summaryBox);
            Thread.sleep(1000);

            String summaryTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("p.c-order-summary-products-list__card-details-title")))
                    .getText().trim().toLowerCase();

            WebElement detailsLine = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-order-summary-products-list__card-details p:nth-of-type(2)")));

            String summaryDetails = detailsLine.getText().trim().toLowerCase();

            String summaryQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'c-order-summary-products-list__card-details')]//p[contains(text(),'QTY')]")))
                    .getText().replaceAll("[^0-9]", "").trim();

            String summaryPrice = normalizePrice(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.c-order-summary-products-list__card-price .price")))
                            .getAttribute("aria-label")
            );

            boolean titleMatch = summaryTitle.equals(cartData.title);
            boolean itemNumberMatch = summaryDetails.contains(cartData.itemNumber);
            boolean colorMatch = summaryDetails.contains(cartData.color);
            boolean sizeMatch = summaryDetails.contains(cartData.size);
            boolean qtyMatch = summaryQty.equals(cartData.qty.replaceAll("[^0-9]", "").trim());
            boolean priceMatch = summaryPrice.equals(cartData.price);

            System.out.println("Summary Title    : " + summaryTitle);
            System.out.println("Summary Details  : " + summaryDetails);
            System.out.println("Summary Qty      : " + summaryQty);
            System.out.println("Summary Price    : " + summaryPrice);

            if (titleMatch && itemNumberMatch && colorMatch && sizeMatch && qtyMatch && priceMatch) {
                System.out.println("Checkout summary matches cart PASS");
            } else {
                System.out.println("Checkout summary matches cart FAIL");
                System.out.println("Title Match      : " + titleMatch);
                System.out.println("Item Number Match: " + itemNumberMatch);
                System.out.println("Color Match      : " + colorMatch);
                System.out.println("Size Match       : " + sizeMatch);
                System.out.println("Qty Match        : " + qtyMatch);
                System.out.println("Price Match      : " + priceMatch);
            }

        } catch (Exception e) {
            System.out.println("Checkout summary matches cart FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static String normalizePrice(String raw) {
        if (raw == null) {
            return "";
        }

        String cleaned = raw.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) {
            return "";
        }

        try {
            double value = Double.parseDouble(cleaned);
            return String.format("%.2f", value);
        } catch (Exception e) {
            return cleaned;
        }
    }

    public static void testLoginValidations(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Login Validations (All in One) -----");

        try {
            driver.get("https://www.garnethill.com/SinglePageCheckoutView?storeId=10054&langId=-1&catalogId=10054");

            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .toString().equals("complete"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#signin-form")));

            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#email")));
            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#password")));
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.login-button")));

            System.out.println("-> Empty Email/Password Test");

            emailInput.clear();
            passwordInput.clear();

            try {
                signInButton.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }

            Thread.sleep(1500);

            String bodyText1 = driver.findElement(By.tagName("body")).getText().toLowerCase();

            boolean emptyPass =
                    bodyText1.contains("required")
                    || bodyText1.contains("please enter")
                    || driver.findElements(By.cssSelector(".error, .field-error")).size() > 0;

            System.out.println(emptyPass ? "Empty validation PASS" : "Empty validation FAIL");

            System.out.println("-> Invalid Email Format Test");

            emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#email")));
            passwordInput = driver.findElement(By.cssSelector("input#password"));
            signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.login-button")));

            emailInput.clear();
            passwordInput.clear();

            emailInput.sendKeys("abc");
            passwordInput.sendKeys("Test12345");

            try {
                signInButton.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }

            Thread.sleep(2000);

            String bodyText2 = driver.findElement(By.tagName("body")).getText().toLowerCase();

            boolean invalidEmailPass =
                    bodyText2.contains("valid email")
                    || bodyText2.contains("valid format")
                    || bodyText2.contains("please enter email address in valid format")
                    || bodyText2.contains("please enter email address in a valid format")
                    || bodyText2.contains("please enter a valid email");

            System.out.println(invalidEmailPass ? "Invalid email PASS" : "Invalid email FAIL");

            System.out.println("-> Wrong Email/Password Test");

            emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#email")));
            passwordInput = driver.findElement(By.cssSelector("input#password"));
            signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.login-button")));

            emailInput.clear();
            passwordInput.clear();

            emailInput.sendKeys("wrong@test.com");
            passwordInput.sendKeys("Wrong123");

            try {
                signInButton.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }

            Thread.sleep(3000);

            String bodyText3 = driver.findElement(By.tagName("body")).getText().toLowerCase();

            boolean wrongPass =
                    bodyText3.contains("not correct")
                    || bodyText3.contains("incorrect")
                    || bodyText3.contains("try again");

            System.out.println(wrongPass ? "Wrong email/password PASS" : "Wrong email/password FAIL");

        } catch (Exception e) {
            System.out.println("Login validations FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testForgotPassword(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- T-CHECKOUT-6: Forgot Password -----");

        try {
            WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Forgot your password?')]")));

            try {
                forgotPasswordLink.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", forgotPasswordLink);
            }

            Thread.sleep(2000);

            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();

            boolean popupOrSectionShown =
                    bodyText.contains("password reset")
                    || bodyText.contains("reset password")
                    || bodyText.contains("forgot password")
                    || driver.findElements(By.cssSelector(".modal, .popup, .c-modal, .c-sheet__outer-wrapper")).size() > 0;

            if (popupOrSectionShown) {
                System.out.println("Forgot password PASS");
            } else {
                System.out.println("Forgot password FAIL");
            }

        } catch (Exception e) {
            System.out.println("Forgot password FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testContinueAsGuest(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Continue As Guest → Shipping Step -----");

        try {
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-analytics-name='continue-as-guest']")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
            Thread.sleep(1000);

            try {
                continueBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueBtn);
            }

            WebElement shippingStep = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div#accordion__item-step1")));

            WebElement shippingHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[contains(text(),'Shipping')]")));

            WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#checkout_step1_email")));

            WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#fName")));

            WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#lName")));

            WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[data-analytics-name='street_address'], input[autocomplete='address-line1']")));

            WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#zipbox")));

            WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#phone1box")));

            WebElement continueToDeliveryBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button#shipping-next-btn")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", address);
            Thread.sleep(500);

            boolean passed =
                    shippingStep.isDisplayed()
                    && shippingHeader.isDisplayed()
                    && email.isDisplayed()
                    && firstName.isDisplayed()
                    && lastName.isDisplayed()
                    && address.isDisplayed()
                    && zip.isDisplayed()
                    && phone.isDisplayed()
                    && continueToDeliveryBtn.isDisplayed();

            if (passed) {
                System.out.println("Continue as Guest → Shipping PASS");
            } else {
                System.out.println("Continue as Guest → Shipping FAIL");
                System.out.println("Reason: One or more shipping elements are not visible");
            }

        } catch (Exception e) {
            System.out.println("Continue as Guest FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testShippingFormInputsAndContinue(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Shipping Inputs + Error Messages + Continue -----");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div#accordion__item-step1")));

            WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#checkout_step1_email")));

            WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#fName")));

            WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#lName")));

            WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[data-analytics-name='street_address'], input[autocomplete='address-line1']")));

            WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#zipbox")));

            WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#phone1box")));

            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button#shipping-next-btn")));

            email.clear();
            firstName.clear();
            lastName.clear();
            address.clear();
            zip.clear();
            phone.clear();

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
            Thread.sleep(800);

            try {
                continueBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueBtn);
            }

            Thread.sleep(2000);

            String bodyAfterEmpty = driver.findElement(By.tagName("body")).getText();

            System.out.println("----- Empty Form Errors -----");
            if (bodyAfterEmpty.toLowerCase().contains("email")) {
                System.out.println("Email error may be shown");
            }
            if (bodyAfterEmpty.toLowerCase().contains("first")) {
                System.out.println("First Name error may be shown");
            }
            if (bodyAfterEmpty.toLowerCase().contains("last")) {
                System.out.println("Last Name error may be shown");
            }
            if (bodyAfterEmpty.toLowerCase().contains("street") || bodyAfterEmpty.toLowerCase().contains("address")) {
                System.out.println("Address error may be shown");
            }
            if (bodyAfterEmpty.toLowerCase().contains("zip")) {
                System.out.println("ZIP error may be shown");
            }
            if (bodyAfterEmpty.toLowerCase().contains("phone")) {
                System.out.println("Phone error may be shown");
            }

            email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#checkout_step1_email")));
            firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#fName")));
            lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#lName")));
            address = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[data-analytics-name='street_address'], input[autocomplete='address-line1']")));
            zip = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#zipbox")));
            phone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#phone1box")));
            continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button#shipping-next-btn")));

            email.clear();
            firstName.clear();
            lastName.clear();
            address.clear();
            zip.clear();
            phone.clear();

            email.sendKeys("abc");
            firstName.sendKeys("1");
            lastName.sendKeys("2");
            address.sendKeys("a");
            zip.sendKeys("12");
            phone.sendKeys("123");

            try {
                continueBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueBtn);
            }

            Thread.sleep(2500);

            String bodyAfterInvalid = driver.findElement(By.tagName("body")).getText();

            System.out.println("----- Invalid Input Errors -----");
            System.out.println(bodyAfterInvalid);

            email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#checkout_step1_email")));
            firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#fName")));
            lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#lName")));
            address = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[data-analytics-name='street_address'], input[autocomplete='address-line1']")));
            zip = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#zipbox")));
            phone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#phone1box")));
            continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button#shipping-next-btn")));

            email.clear();
            firstName.clear();
            lastName.clear();
            address.clear();
            zip.clear();
            phone.clear();

            email.sendKeys("test@example.com");
            firstName.sendKeys("Looaa");
            lastName.sendKeys("Awayes");
            address.sendKeys("123 Main Street");
            zip.sendKeys("10001");
            phone.sendKeys("2125551234");

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
            Thread.sleep(800);

            try {
                continueBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueBtn);
            }

            Thread.sleep(3000);

            boolean movedToDelivery =
                    !driver.findElements(By.xpath("//span[contains(text(),'Delivery Method')]")).isEmpty()
                    || !driver.findElements(By.xpath("//span[contains(text(),'Gift Packaging')]")).isEmpty()
                    || driver.findElement(By.tagName("body")).getText().toLowerCase().contains("delivery method");

            if (movedToDelivery) {
                System.out.println("Continue to Delivery Method PASS");
            } else {
                System.out.println("Continue to Delivery Method FAIL");
            }

        } catch (Exception e) {
            System.out.println("Shipping form test FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testDeliveryMethodStep(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("\n========== DELIVERY METHOD STEP ==========");

        try {
            WebElement step2Container = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div#accordion__item-step2")));

            WebElement step2Header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[contains(text(),'Delivery Method')]")));

            WebElement shippingMethodBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[data-analytics-name='shipping_method']")));

            WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("a.c-order-product-item_title")));

            WebElement productPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-checkout-delivery__item-price .price")));

            WebElement productQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-checkout-delivery__product-card-quantity-value")));

            WebElement continueToPaymentBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button#nextBtn")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", shippingMethodBtn);
            Thread.sleep(1000);

            try {
                shippingMethodBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", shippingMethodBtn);
            }

            Thread.sleep(1500);

            List<WebElement> options = new ArrayList<>();

            try {
                options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//button[contains(@data-analytics-name,'shipping') and not(@data-analytics-name='shipping_method')]")));
            } catch (Exception e) {
                try {
                    options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'standard') "
                                    + "or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'express') "
                                    + "or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'next day')]")));
                } catch (Exception ex) {
                    options = driver.findElements(
                            By.xpath("//button[contains(.,'Standard') or contains(.,'Express') or contains(.,'Next')]"));
                }
            }

            List<String> optionTexts = new ArrayList<>();

            for (WebElement option : options) {
                try {
                    String txt = option.getText().replace("\n", " ").trim();
                    if (!txt.isEmpty() && !optionTexts.contains(txt)) {
                        optionTexts.add(txt);
                    }
                } catch (Exception ignored) {
                }
            }

            String option1 = optionTexts.size() > 0 ? optionTexts.get(0) : "";
            String option2 = optionTexts.size() > 1 ? optionTexts.get(1) : "";
            String option3 = optionTexts.size() > 2 ? optionTexts.get(2) : "";

            System.out.println("Header             : " + step2Header.getText().trim());
            System.out.println("Product            : " + productTitle.getText().trim());
            System.out.println("Price              : " + productPrice.getAttribute("aria-label"));
            System.out.println("Quantity           : " + productQty.getText().trim());
            System.out.println("Current Method Btn : " + shippingMethodBtn.getText().replace("\n", " ").trim());
            System.out.println("Option 1           : " + option1);
            System.out.println("Option 2           : " + option2);
            System.out.println("Option 3           : " + option3);

            boolean passed =
                    step2Container.isDisplayed()
                    && step2Header.isDisplayed()
                    && shippingMethodBtn.isDisplayed()
                    && productTitle.isDisplayed()
                    && productPrice.isDisplayed()
                    && productQty.isDisplayed()
                    && continueToPaymentBtn.isDisplayed()
                    && optionTexts.size() > 0;

            if (passed) {
                System.out.println("Delivery Method & Gift Packaging PASS");
            } else {
                System.out.println(" Delivery Method & Gift Packaging FAIL");
            }

        } catch (Exception e) {
            System.out.println(" Delivery Method & Gift Packaging FAIL");
            System.out.println("Reason             : " + e.getMessage());
        }

        System.out.println("=========================================\n");
    }public static void testSelectDeliveryMethodAndContinueToPayment(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("\n========== SELECT DELIVERY METHOD ==========");

        try {
            WebElement shippingMethodBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[data-analytics-name='shipping_method']")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", shippingMethodBtn);
            Thread.sleep(1000);

            try {
                shippingMethodBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", shippingMethodBtn);
            }

            Thread.sleep(1500);

            List<WebElement> options = new ArrayList<>();

            try {
                options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//button[contains(@data-analytics-name,'shipping') and not(@data-analytics-name='shipping_method')]")));
            } catch (Exception e) {
                try {
                    options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'standard') "
                                    + "or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'express') "
                                    + "or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'next day')]")));
                } catch (Exception ex) {
                    options = driver.findElements(
                            By.xpath("//button[contains(.,'Standard') or contains(.,'Express') or contains(.,'Next')]"));
                }
            }

            WebElement chosenOption = null;
            String chosenOptionText = "";

            for (WebElement option : options) {
                try {
                    String txt = option.getText().replace("\n", " ").trim().toLowerCase();

                    if (txt.contains("express")) {
                        chosenOption = option;
                        chosenOptionText = option.getText().replace("\n", " ").trim();
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            if (chosenOption == null) {
                for (WebElement option : options) {
                    try {
                        String txt = option.getText().replace("\n", " ").trim();
                        if (!txt.isEmpty()) {
                            chosenOption = option;
                            chosenOptionText = txt;
                            break;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }

            if (chosenOption == null) {
                throw new RuntimeException("No delivery option found");
            }

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", chosenOption);
            Thread.sleep(1000);

            try {
                chosenOption.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", chosenOption);
            }

            Thread.sleep(2000);

            shippingMethodBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[data-analytics-name='shipping_method']")));

            String selectedMethod = shippingMethodBtn.getText().replace("\n", " ").trim();

            System.out.println("Chosen Option      : " + chosenOptionText);
            System.out.println("Selected Method    : " + selectedMethod);

            WebElement continueToPaymentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button#nextBtn")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueToPaymentBtn);
            Thread.sleep(1000);

            try {
                continueToPaymentBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", continueToPaymentBtn);
            }

            Thread.sleep(3000);

            boolean movedToPayment = false;

            try {
                movedToPayment = wait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Payment')]")),
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Payment')]")),
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Payment')]"))
                )) != null;
            } catch (Exception e) {
                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
                movedToPayment = bodyText.contains("payment");
            }

            boolean methodChanged =
                    selectedMethod.toLowerCase().contains("express")
                    || chosenOptionText.toLowerCase().contains("express");

            System.out.println("Moved To Payment   : " + movedToPayment);

            if (methodChanged && movedToPayment) {
                System.out.println("Continue To Payment PASS");
            } else {
                System.out.println("Continue To Payment FAIL");
                System.out.println("Method Changed     : " + methodChanged);
            }

        } catch (Exception e) {
            System.out.println(" Select Delivery FAIL");
            System.out.println("Reason             : " + e.getMessage());
        }

    }
}
