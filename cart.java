package atoumationtest;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class cart {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.get("https://www.garnethill.com/");
            System.out.println("Home page opened successfully");

            waitForHomePage(driver, wait);
            openCartFromHome(driver, wait, js);
            testEmptyCartAndContinueShopping(driver, wait, js);

        } finally {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    public static void waitForHomePage(WebDriver driver, WebDriverWait wait) {
        wait.until((ExpectedCondition<Boolean>) webDriver -> {
            try {
                return ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .toString()
                        .equals("complete");
            } catch (Exception e) {
                return false;
            }
        });

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("button[data-analytics-name='show_mini_cart']")),
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[contains(.,'Bag')]"))
        ));

        System.out.println("Home page fully loaded");
    }

    public static void waitForPageReady(WebDriver driver, WebDriverWait wait) {
        wait.until((ExpectedCondition<Boolean>) webDriver -> {
            try {
                return ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .toString()
                        .equals("complete");
            } catch (Exception e) {
                return false;
            }
        });
    }

    public static void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String getFreshText(WebDriver driver, WebDriverWait wait, By locator) {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                return element.getText().trim();
            } catch (StaleElementReferenceException e) {
                sleepSilently(500);
            }
        }
        throw new RuntimeException("Could not get fresh text from locator: " + locator);
    }

    public static WebElement getFreshClickableElement(WebDriver driver, WebDriverWait wait, By locator) {
        for (int i = 0; i < 5; i++) {
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (StaleElementReferenceException e) {
                sleepSilently(500);
            }
        }
        throw new RuntimeException("Could not get fresh clickable element: " + locator);
    }

    public static boolean isAddToBagEnabled(WebDriver driver) {
        try {
            WebElement btn = driver.findElement(By.cssSelector("button[data-cs-override-id='pdp_add_to_cart']"));
            String disabled = btn.getAttribute("disabled");
            String ariaDisabled = btn.getAttribute("aria-disabled");

            return btn.isDisplayed()
                    && btn.isEnabled()
                    && disabled == null
                    && !"true".equalsIgnoreCase(ariaDisabled);
        } catch (Exception e) {
            return false;
        }
    }

    public static void openCartFromHome(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Opening cart from home page -----");

        waitForHomePage(driver, wait);

        WebElement bagButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[data-analytics-name='show_mini_cart']")));

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", bagButton);
        Thread.sleep(1000);

        try {
            bagButton.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", bagButton);
        }

        System.out.println("Clicked Bag button");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("ShoppingCartView"),
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[contains(normalize-space(),'Shopping Bag')]"))
        ));

        System.out.println("Cart page opened");
    }

    public static void openCartAndWaitForRealContent(WebDriver driver, WebDriverWait wait)
            throws InterruptedException {

        waitForPageReady(driver, wait);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(normalize-space(),'Shopping Bag')]")));

        boolean realContentLoaded = false;

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("div.cart-empty-text")),
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("button.t-cart__continue-shopping")),
                        ExpectedConditions.textToBePresentInElementLocated(
                                By.cssSelector("span.t-cart__header-count"), "Items"),
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//button[contains(normalize-space(),'Remove')]")),
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(normalize-space(),'Checkout Now')]"))
                ));

                realContentLoaded = true;
                break;

            } catch (Exception e) {
                System.out.println("Cart content not ready yet, refreshing...");
                driver.navigate().refresh();
                waitForPageReady(driver, wait);
                Thread.sleep(2000);
            }
        }

        if (!realContentLoaded) {
            throw new RuntimeException("Cart real content did not load after refresh.");
        }

        System.out.println("Cart page fully loaded successfully");
    }

    public static void testEmptyCartAndContinueShopping(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Empty Cart -> Continue Shopping -> Add Item -----");

        openCartAndWaitForRealContent(driver, wait);

        By cartHeaderLocator = By.xpath("//h1[contains(normalize-space(),'Shopping Bag')]");
        By itemCountLocator = By.cssSelector("span.t-cart__header-count");
        By emptyMessageLocator = By.cssSelector("div.cart-empty-text");
        By continueShoppingBtnLocator = By.cssSelector("button.t-cart__continue-shopping");

        String headerText = getFreshText(driver, wait, cartHeaderLocator);
        String countText = getFreshText(driver, wait, itemCountLocator);
        String msgText = getFreshText(driver, wait, emptyMessageLocator);

        System.out.println("Header: " + headerText);
        System.out.println("Count : " + countText);
        System.out.println("Msg   : " + msgText);

        if (headerText.contains("Shopping Bag")
                && countText.equalsIgnoreCase("0 Items")
                && msgText.equalsIgnoreCase("Your Bag Is Empty")) {
            System.out.println("Empty cart validation PASS");
        } else {
            System.out.println("Empty cart validation FAIL");
            System.out.println("Reason: Cart empty state did not match expected values");
            return;
        }

        WebElement continueShoppingBtn = getFreshClickableElement(driver, wait, continueShoppingBtnLocator);

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueShoppingBtn);
        Thread.sleep(1000);

        try {
            continueShoppingBtn.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", continueShoppingBtn);
        }

        System.out.println("Clicked Continue Shopping");

        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("ShoppingCartView")));
        waitForPageReady(driver, wait);
        Thread.sleep(3000);

        openProductAndAddToBagByIndex(driver, wait, js, 0);

        driver.get("https://www.garnethill.com/");
        waitForHomePage(driver, wait);
        openCartFromHome(driver, wait, js);
        openCartAndWaitForRealContent(driver, wait);

        verifyItemAddedToCart(driver, wait);

        addSecondProductToCart(driver, wait, js);

        driver.get("https://www.garnethill.com/");
        waitForHomePage(driver, wait);
        openCartFromHome(driver, wait, js);
        openCartAndWaitForRealContent(driver, wait);

        testMultipleItemsInCart(driver, wait, js);
        testCartItemActions(driver, wait, js);
    }

    public static List<WebElement> findVisibleProductCards(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        List<By> productLocators = List.of(
                By.cssSelector("article.c-product-item"),
                By.cssSelector("div.c-product-item"),
                By.cssSelector("div[data-analytics-name='product']"),
                By.cssSelector("li[class*='product']"),
                By.cssSelector("article[class*='product']"),
                By.cssSelector("div[class*='product-item']")
        );

        for (int scrollRound = 0; scrollRound < 6; scrollRound++) {

            for (By locator : productLocators) {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    if (!elements.isEmpty()) {
                        return elements;
                    }
                } catch (Exception e) {
                }
            }

            js.executeScript("window.scrollBy(0, 800);");
            Thread.sleep(1200);
        }

        return List.of();
    }

    public static void openProductAndAddToBagByIndex(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, int productIndex)
            throws InterruptedException {

        System.out.println("----- Opening product index " + productIndex + " and adding it to bag -----");

        waitForPageReady(driver, wait);
        Thread.sleep(3000);

        List<WebElement> productCards = findVisibleProductCards(driver, wait, js);

        if (productCards.isEmpty()) {
            driver.get("https://www.garnethill.com/clothing/");
            waitForPageReady(driver, wait);
            Thread.sleep(3000);
            productCards = findVisibleProductCards(driver, wait, js);
        }

        if (productCards.isEmpty()) {
            throw new RuntimeException("No visible product cards found.");
        }

        if (productIndex >= productCards.size()) {
            throw new RuntimeException("Requested product index " + productIndex
                    + " not found. Total products found: " + productCards.size());
        }

        WebElement targetCard = productCards.get(productIndex);

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", targetCard);
        Thread.sleep(1000);

        boolean productOpened = false;

        List<By> quickShopLocators = List.of(
                By.cssSelector("div.c-product-item-btn-container"),
                By.cssSelector("button[data-analytics-name*='quick']"),
                By.cssSelector("button[aria-label*='Quick Shop']"),
                By.cssSelector(".quick-shop"),
                By.cssSelector("[data-testid*='quick']")
        );

        for (By locator : quickShopLocators) {
            try {
                WebElement quickShop = targetCard.findElement(locator);
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", quickShop);
                Thread.sleep(800);
                js.executeScript("arguments[0].click();", quickShop);
                Thread.sleep(3000);
                productOpened = true;
                System.out.println("Clicked Quick Shop for product index " + productIndex);
                break;
            } catch (Exception e) {
            }
        }

        if (!productOpened) {
            List<By> productLinkLocators = List.of(
                    By.cssSelector("a.c-link.u-w-100"),
                    By.cssSelector("a.c-product-item-title-link"),
                    By.cssSelector("a[href*='/p/']"),
                    By.cssSelector("a[href]")
            );

            for (By locator : productLinkLocators) {
                try {
                    WebElement productLink = targetCard.findElement(locator);
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", productLink);
                    Thread.sleep(800);

                    try {
                        productLink.click();
                    } catch (Exception ex) {
                        js.executeScript("arguments[0].click();", productLink);
                    }

                    waitForPageReady(driver, wait);
                    Thread.sleep(3000);
                    productOpened = true;
                    System.out.println("Opened product page for index " + productIndex);
                    break;
                } catch (Exception e) {
                }
            }
        }

        if (!productOpened) {
            throw new RuntimeException("Could not open product at index " + productIndex);
        }

        testUnavailableOptionsCannotBeSelected(driver, wait, js);
        selectVerifiedColor(driver, wait, js);
        selectVerifiedSize(driver, wait, js);
        clickVerifiedAddToBag(driver, wait, js);
    }

    public static void addSecondProductToCart(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Adding Second Product To Cart -----");

        driver.get("https://www.garnethill.com/");
        waitForHomePage(driver, wait);
        Thread.sleep(2000);

        openProductAndAddToBagByIndex(driver, wait, js, 1);

        Thread.sleep(3000);
        System.out.println("Second product added to bag");
    }

    public static void testUnavailableOptionsCannotBeSelected(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Unavailable Size/Color Cannot Be Selected -----");

        try {
            waitForPageReady(driver, wait);
            Thread.sleep(2000);

            List<WebElement> allOptionButtons = driver.findElements(
                    By.cssSelector("button.c-universal-options__option-swatch"));

            if (allOptionButtons.isEmpty()) {
                System.out.println("Unavailable option test skipped");
                System.out.println("Reason: No size/color options found");
                return;
            }

            WebElement disabledOption = null;

            for (WebElement option : allOptionButtons) {
                try {
                    String disabled = option.getAttribute("disabled");
                    String ariaDisabled = option.getAttribute("aria-disabled");
                    String text = option.getText() != null ? option.getText().trim().toLowerCase() : "";
                    String ariaLabel = option.getAttribute("aria-label") != null
                            ? option.getAttribute("aria-label").toLowerCase()
                            : "";
                    String className = option.getAttribute("class") != null
                            ? option.getAttribute("class").toLowerCase()
                            : "";

                    boolean isDisabled =
                            disabled != null
                            || "true".equalsIgnoreCase(ariaDisabled)
                            || text.contains("out of stock")
                            || ariaLabel.contains("out of stock")
                            || className.contains("disabled")
                            || className.contains("unavailable");

                    if (isDisabled) {
                        disabledOption = option;
                        break;
                    }

                } catch (Exception e) {
                }
            }

            if (disabledOption == null) {
                System.out.println("Unavailable option test skipped");
                System.out.println("Reason: No disabled / out-of-stock option found");
                return;
            }

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", disabledOption);
            Thread.sleep(700);

            String beforeChecked = disabledOption.getAttribute("aria-checked");
            if (beforeChecked == null) {
                beforeChecked = "false";
            }

            boolean addToBagEnabledBefore = isAddToBagEnabled(driver);

            try {
                disabledOption.click();
            } catch (Exception e) {
                try {
                    js.executeScript("arguments[0].click();", disabledOption);
                } catch (Exception ignored) {
                }
            }

            Thread.sleep(1200);

            String afterChecked = disabledOption.getAttribute("aria-checked");
            if (afterChecked == null) {
                afterChecked = "false";
            }

            boolean addToBagEnabledAfter = isAddToBagEnabled(driver);

            boolean becameSelected = beforeChecked.equalsIgnoreCase("false")
                    && afterChecked.equalsIgnoreCase("true");

            boolean addToBagWronglyActivated = !addToBagEnabledBefore && addToBagEnabledAfter;

            boolean passed = !becameSelected && !addToBagWronglyActivated;

            if (passed) {
                System.out.println("Unavailable size/color PASS");
            } else {
                System.out.println("Unavailable size/color FAIL");
                System.out.println("Reason: Disabled/out-of-stock option became selectable or incorrectly activated Add to Bag");
            }

        } catch (Exception e) {
            System.out.println("Unavailable size/color FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void selectVerifiedColor(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Color Selection -----");

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("button.c-universal-options__option-swatch")));
        } catch (TimeoutException e) {
            System.out.println("Color selection skipped");
            return;
        }

        boolean anyAlreadySelected = false;
        List<WebElement> colorButtons = driver.findElements(
                By.cssSelector("button.c-universal-options__option-swatch"));

        for (WebElement color : colorButtons) {
            try {
                if ("true".equalsIgnoreCase(color.getAttribute("aria-checked"))) {
                    anyAlreadySelected = true;
                    break;
                }
            } catch (Exception e) {
            }
        }

        if (anyAlreadySelected) {
            System.out.println("Color selection PASS");
            return;
        }

        boolean colorSelected = false;

        for (WebElement color : colorButtons) {
            try {
                if (!color.isDisplayed() || !color.isEnabled()) {
                    continue;
                }

                String disabled = color.getAttribute("disabled");
                String ariaDisabled = color.getAttribute("aria-disabled");

                if (disabled != null || "true".equalsIgnoreCase(ariaDisabled)) {
                    continue;
                }

                js.executeScript("arguments[0].scrollIntoView({block:'center'});", color);
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", color);
                Thread.sleep(1200);

                if ("true".equalsIgnoreCase(color.getAttribute("aria-checked"))) {
                    colorSelected = true;
                    break;
                }

            } catch (Exception e) {
            }
        }

        if (colorSelected) {
            System.out.println("Color selection PASS");
        } else {
            System.out.println("Color selection FAIL");
            System.out.println("Reason: Color was not selected successfully");
        }
    }

    public static void selectVerifiedSize(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Size Selection -----");

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("div.c-universal-options__option.text-item button.c-universal-options__option-swatch")));
        } catch (TimeoutException e) {
            System.out.println("Size selection skipped");
            return;
        }

        List<WebElement> sizeButtons = driver.findElements(
                By.cssSelector("div.c-universal-options__option.text-item button.c-universal-options__option-swatch"));

        if (sizeButtons.isEmpty()) {
            System.out.println("Size selection skipped");
            return;
        }

        boolean anyAlreadySelected = false;

        for (WebElement size : sizeButtons) {
            try {
                if ("true".equalsIgnoreCase(size.getAttribute("aria-checked"))) {
                    anyAlreadySelected = true;
                    break;
                }
            } catch (Exception e) {
            }
        }

        if (!anyAlreadySelected) {
            boolean sizeSelected = false;

            for (WebElement size : sizeButtons) {
                try {
                    if (!size.isDisplayed() || !size.isEnabled()) {
                        continue;
                    }

                    String disabled = size.getAttribute("disabled");
                    String ariaDisabled = size.getAttribute("aria-disabled");

                    if (disabled != null || "true".equalsIgnoreCase(ariaDisabled)) {
                        continue;
                    }

                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", size);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].click();", size);
                    Thread.sleep(1200);

                    if ("true".equalsIgnoreCase(size.getAttribute("aria-checked"))) {
                        sizeSelected = true;
                        break;
                    }

                } catch (Exception e) {
                }
            }

            if (!sizeSelected) {
                System.out.println("Size selection FAIL");
                System.out.println("Reason: Size was not selected successfully");
                return;
            }
        }

        wait.until(driver1 -> {
            try {
                WebElement btn = driver1.findElement(By.cssSelector("button[data-cs-override-id='pdp_add_to_cart']"));
                String disabled = btn.getAttribute("disabled");
                String ariaDisabled = btn.getAttribute("aria-disabled");
                return btn.isDisplayed() && disabled == null && !"true".equalsIgnoreCase(ariaDisabled);
            } catch (Exception e) {
                return false;
            }
        });

        System.out.println("Size selection PASS");
    }

    public static void clickVerifiedAddToBag(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Add To Bag -----");

        WebElement addToBagBtn = wait.until(driver1 -> {
            try {
                WebElement btn = driver1.findElement(By.cssSelector("button[data-cs-override-id='pdp_add_to_cart']"));
                String disabled = btn.getAttribute("disabled");
                String ariaDisabled = btn.getAttribute("aria-disabled");

                if (btn.isDisplayed() && disabled == null && !"true".equalsIgnoreCase(ariaDisabled)) {
                    return btn;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        });

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", addToBagBtn);
        Thread.sleep(800);

        try {
            addToBagBtn.click();
        } catch (Exception e1) {
            try {
                js.executeScript("arguments[0].click();", addToBagBtn);
            } catch (Exception e2) {
                System.out.println("Add to Bag FAIL");
                System.out.println("Reason: Failed to click Add to Bag");
                return;
            }
        }

        Thread.sleep(4000);
        System.out.println("Add to Bag PASS");
    }

    public static void verifyItemAddedToCart(WebDriver driver, WebDriverWait wait) {
        System.out.println("----- Testing Item Added To Cart -----");

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'1 Item')]")),
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'Items') and not(contains(text(),'0 Items'))]")),
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//button[contains(normalize-space(),'Remove')]")),
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(normalize-space(),'Checkout Now')]"))
            ));

            String bodyText = driver.findElement(By.tagName("body")).getText();

            if (bodyText.contains("0 Items") || bodyText.contains("Your Bag Is Empty")) {
                System.out.println("Item added to cart FAIL");
                System.out.println("Reason: Cart still appears empty");
            } else {
                System.out.println("Item added to cart PASS");
            }

        } catch (Exception e) {
            System.out.println("Item added to cart FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static boolean verifyCartSummaryNumbers(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement orderSummary = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-order-summary")));

            String summaryText = orderSummary.getText().replaceAll(",", "").trim().toLowerCase();

            boolean hasSubtotal = summaryText.contains("subtotal");
            boolean hasEstimatedTotal = summaryText.contains("estimated total");
            boolean hasDollarValue = summaryText.matches("(?s).*\\$\\s*\\d+(\\.\\d{2})?.*");

            return hasSubtotal && hasEstimatedTotal && hasDollarValue;

        } catch (Exception e) {
            return false;
        }
    }

    public static void testMultipleItemsInCart(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Multiple Items In Cart -----");

        try {
            List<WebElement> cartItems = driver.findElements(By.cssSelector("div.order-product-item-container"));
            int itemCount = cartItems.size();

            String bodyText = driver.findElement(By.tagName("body")).getText();
            boolean countLooksCorrect =
                    bodyText.contains("2 Items")
                    || bodyText.contains("2 Item")
                    || itemCount >= 2;

            boolean allItemsHaveDetails = true;

            for (WebElement item : cartItems) {
                try {
                    String text = item.getText().toLowerCase();

                    boolean hasName = text.length() > 10;
                    boolean hasColorOrSize = text.contains("color") || text.contains("size");
                    boolean hasQty = text.contains("qty") || text.contains("quantity")
                            || text.matches("(?s).*\\b[1-9]\\b.*");

                    if (!(hasName && (hasColorOrSize || hasQty))) {
                        allItemsHaveDetails = false;
                        break;
                    }
                } catch (Exception e) {
                    allItemsHaveDetails = false;
                    break;
                }
            }

            boolean summaryValid = verifyCartSummaryNumbers(driver, wait);

            if (countLooksCorrect && itemCount >= 2 && allItemsHaveDetails && summaryValid) {
                System.out.println("Multiple items in cart PASS");
                System.out.println("Cart contains " + itemCount + " items and totals look valid");
            } else {
                System.out.println("Multiple items in cart FAIL");
                System.out.println("Reason: Count/details/totals validation failed");
                System.out.println("Detected cart items: " + itemCount);
                System.out.println("Details valid: " + allItemsHaveDetails);
                System.out.println("Summary valid: " + summaryValid);
            }

        } catch (Exception e) {
            System.out.println("Multiple items in cart FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testCartItemActions(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Cart Item Actions -----");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.order-product-item-container")));

            testIncreaseQuantity(driver, wait, js);
            testDecreaseQuantity(driver, wait, js);
            testMinQuantityBehavior(driver, wait, js);
            testMaxQuantityLimit20(driver, wait, js);
            testEditItem(driver, wait, js);

            boolean savedForLaterPassed = testSaveForLaterOnly(driver, wait, js);

            if (savedForLaterPassed) {
                moveSavedItemToBag(driver, wait, js);
                testOrderSummarySection(driver, wait, js);
                testRemoveItemFromCart(driver, wait, js);
            } else {
                System.out.println("Move To Bag / Order Summary / Remove skipped");
            }

        } catch (Exception e) {
            System.out.println("Cart item actions FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testIncreaseQuantity(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Increase Quantity -----");

        try {
            By plusLocator = By.cssSelector("button.increment-quantity");

            int beforeQty = getCurrentQuantity(driver, wait);

            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(plusLocator));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", plusBtn);
            Thread.sleep(500);

            try {
                plusBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", plusBtn);
            }

            wait.until(driver1 -> {
                try {
                    return getCurrentQuantity(driver1, wait) == beforeQty + 1;
                } catch (Exception e) {
                    return false;
                }
            });

            System.out.println("Increase quantity PASS");

        } catch (Exception e) {
            System.out.println("Increase quantity FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testDecreaseQuantity(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Decrease Quantity -----");

        try {
            By minusLocator = By.cssSelector("button.decrement-quantity");

            int beforeQty = getCurrentQuantity(driver, wait);

            if (beforeQty <= 1) {
                System.out.println("Decrease quantity skipped");
                return;
            }

            WebElement minusBtn = wait.until(ExpectedConditions.elementToBeClickable(minusLocator));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", minusBtn);
            Thread.sleep(500);

            try {
                minusBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", minusBtn);
            }

            wait.until(driver1 -> {
                try {
                    return getCurrentQuantity(driver1, wait) == beforeQty - 1;
                } catch (Exception e) {
                    return false;
                }
            });

            System.out.println("Decrease quantity PASS");

        } catch (Exception e) {
            System.out.println("Decrease quantity FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testMinQuantityBehavior(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Minimum Quantity Validation -----");

        try {
            By minusLocator = By.cssSelector("button.decrement-quantity");
            int currentQty = getCurrentQuantity(driver, wait);

            while (currentQty > 1) {
                WebElement minusBtn = wait.until(ExpectedConditions.elementToBeClickable(minusLocator));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", minusBtn);
                Thread.sleep(400);

                try {
                    minusBtn.click();
                } catch (Exception e) {
                    js.executeScript("arguments[0].click();", minusBtn);
                }

                final int expectedQty = currentQty - 1;
                wait.until(driver1 -> {
                    try {
                        return getCurrentQuantity(driver1, wait) == expectedQty;
                    } catch (Exception e) {
                        return false;
                    }
                });

                Thread.sleep(700);
                currentQty = getCurrentQuantity(driver, wait);
            }

            int beforeClickAtOne = getCurrentQuantity(driver, wait);

            WebElement minusBtnAtOne = wait.until(ExpectedConditions.presenceOfElementLocated(minusLocator));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", minusBtnAtOne);
            Thread.sleep(400);

            boolean minusDisabledAtOne =
                    !minusBtnAtOne.isEnabled()
                    || "true".equalsIgnoreCase(minusBtnAtOne.getAttribute("disabled"))
                    || "true".equalsIgnoreCase(minusBtnAtOne.getAttribute("aria-disabled"));

            if (!minusDisabledAtOne) {
                try {
                    minusBtnAtOne.click();
                } catch (Exception e) {
                    js.executeScript("arguments[0].click();", minusBtnAtOne);
                }
                Thread.sleep(1200);
            }

            boolean itemStillExists = !driver.findElements(
                    By.cssSelector("div.order-product-item-container")).isEmpty();

            boolean emptyBagShown = driver.findElement(By.tagName("body"))
                    .getText().contains("Your Bag Is Empty");

            int afterClickQty = -1;
            if (itemStillExists) {
                try {
                    afterClickQty = getCurrentQuantity(driver, wait);
                } catch (Exception ignored) {
                }
            }

            if (!itemStillExists || emptyBagShown) {
                System.out.println("Minimum quantity validation PASS");
            } else if (afterClickQty == 1 && beforeClickAtOne == 1) {
                System.out.println("Minimum quantity validation PASS");
            } else {
                System.out.println("Minimum quantity validation FAIL");
                System.out.println("Reason: Quantity went below minimum");
            }

        } catch (Exception e) {
            System.out.println("Minimum quantity validation FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testMaxQuantityLimit20(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Max Quantity Limit -----");

        try {
            By plusLocator = By.cssSelector("button.increment-quantity");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.order-product-item-container")));

            int currentQty = getCurrentQuantity(driver, wait);

            while (currentQty < 20) {
                boolean increased = false;

                for (int attempt = 1; attempt <= 3; attempt++) {
                    try {
                        WebElement plusBtn = wait.until(ExpectedConditions.presenceOfElementLocated(plusLocator));
                        js.executeScript("arguments[0].scrollIntoView({block:'center'});", plusBtn);
                        Thread.sleep(400);

                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(plusLocator));
                            plusBtn.click();
                        } catch (Exception e) {
                            js.executeScript("arguments[0].click();", plusBtn);
                        }

                        Thread.sleep(1200);

                        int newQty = getCurrentQuantity(driver, wait);

                        if (newQty > currentQty) {
                            currentQty = newQty;
                            increased = true;
                            break;
                        }

                    } catch (Exception e) {
                        Thread.sleep(700);
                    }
                }

                if (!increased) {
                    System.out.println("Max quantity limit FAIL");
                    System.out.println("Reason: Could not increase quantity");
                    return;
                }
            }

            int qtyAtLimit = getCurrentQuantity(driver, wait);

            if (qtyAtLimit != 20) {
                System.out.println("Max quantity limit FAIL");
                System.out.println("Reason: Quantity did not reach 20");
                return;
            }

            try {
                WebElement plusBtnAt20 = wait.until(ExpectedConditions.presenceOfElementLocated(plusLocator));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", plusBtnAt20);
                Thread.sleep(400);

                try {
                    plusBtnAt20.click();
                } catch (Exception e) {
                    js.executeScript("arguments[0].click();", plusBtnAt20);
                }

                Thread.sleep(1200);
            } catch (Exception e) {
            }

            int qtyAfterExtraClick = getCurrentQuantity(driver, wait);

            if (qtyAfterExtraClick == 20) {
                System.out.println("Max quantity limit PASS");
            } else {
                System.out.println("Max quantity limit FAIL");
                System.out.println("Reason: Quantity exceeded max limit");
            }

        } catch (Exception e) {
            System.out.println("Max quantity limit FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testEditItem(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Edit Item -----");

        try {
            WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-analytics-name='edit_item']")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", editBtn);
            Thread.sleep(500);

            try {
                editBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", editBtn);
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-custom-sheet__content")));

            List<WebElement> colorButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("div.c-custom-sheet__content button.c-universal-options__option-swatch")));

            for (WebElement color : colorButtons) {
                try {
                    if (!color.isDisplayed() || !color.isEnabled()) {
                        continue;
                    }

                    String checked = color.getAttribute("aria-checked");

                    if ("false".equalsIgnoreCase(checked)) {
                        js.executeScript("arguments[0].scrollIntoView({block:'center'});", color);
                        Thread.sleep(500);
                        js.executeScript("arguments[0].click();", color);
                        Thread.sleep(1200);
                        break;
                    }
                } catch (Exception e) {
                }
            }

            WebElement saveAndCloseBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'c-custom-sheet__content')]//button[contains(.,'Save and Close')]")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", saveAndCloseBtn);
            Thread.sleep(500);

            try {
                saveAndCloseBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", saveAndCloseBtn);
            }

            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.c-custom-sheet__content")));

            System.out.println("Edit item PASS");

        } catch (Exception e) {
            System.out.println("Edit item FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static boolean testSaveForLaterOnly(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Save For Later -----");

        try {
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.c-product-card-actions__wishlist-btn")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", saveBtn);
            Thread.sleep(500);

            try {
                saveBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", saveBtn);
            }

            boolean savedSectionFound = false;

            for (int i = 0; i < 6; i++) {
                try {
                    js.executeScript("window.scrollBy(0, 500);");
                    Thread.sleep(1000);

                    List<WebElement> headers = driver.findElements(
                            By.xpath("//h2[contains(@class,'c-custom-carousel-v2__title')]//*[contains(text(),'My Saved Items')]"));

                    if (!headers.isEmpty() && headers.get(0).isDisplayed()) {
                        savedSectionFound = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }

            if (!savedSectionFound) {
                System.out.println("Save For Later FAIL");
                System.out.println("Reason: My Saved Items section did not appear");
                return false;
            }

            WebElement savedItemsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(@class,'c-custom-carousel-v2__title')]//*[contains(text(),'My Saved Items')]")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", savedItemsHeader);
            Thread.sleep(1000);

            WebElement savedItemsCount = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("span.c-custom-carousel-v2__count")));

            WebElement savedItemCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-saved-items-product-item")));

            WebElement moveToBagBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button.c-saved-items-product-item__move-to-cart")));

            String headerText = savedItemsHeader.getText().trim();
            String countText = savedItemsCount.getText().trim().toLowerCase();
            String savedCardText = savedItemCard.getText().toLowerCase();
            String moveBtnText = moveToBagBtn.getText().trim().toLowerCase();

            boolean passed =
                    headerText.contains("My Saved Items")
                    && countText.contains("item")
                    && savedCardText.contains("color")
                    && savedCardText.contains("size")
                    && moveBtnText.contains("move to bag");

            if (passed) {
                System.out.println("Save For Later PASS");
                return true;
            } else {
                System.out.println("Save For Later FAIL");
                System.out.println("Reason: Saved item details are incomplete");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Save For Later FAIL");
            System.out.println("Reason: " + e.getMessage());
            return false;
        }
    }

    public static void moveSavedItemToBag(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Move To Bag -----");

        try {
            WebElement savedHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(@class,'c-custom-carousel-v2__title')]//*[contains(text(),'My Saved Items')]")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", savedHeader);
            Thread.sleep(1000);

            int beforeSavedCount = driver.findElements(
                    By.cssSelector("div.c-saved-items-product-item")).size();

            WebElement moveToBagBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.c-saved-items-product-item__move-to-cart")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", moveToBagBtn);
            Thread.sleep(500);

            try {
                moveToBagBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", moveToBagBtn);
            }

            wait.until(driver1 -> {
                try {
                    int afterSavedCount = driver1.findElements(
                            By.cssSelector("div.c-saved-items-product-item")).size();

                    boolean cartHasItem =
                            !driver1.findElements(By.cssSelector("div.order-product-item-container")).isEmpty()
                            || driver1.findElement(By.tagName("body")).getText().contains("Remove");

                    return afterSavedCount < beforeSavedCount || cartHasItem;
                } catch (Exception e) {
                    return false;
                }
            });

            System.out.println("Move To Bag PASS");

        } catch (Exception e) {
            System.out.println("Move To Bag FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testOrderSummarySection(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Order Summary -----");

        try {
            WebElement orderSummaryBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-order-summary")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", orderSummaryBox);
            Thread.sleep(1000);

            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h2.c-order-summary__title")));

            WebElement subtotalRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-order-summary__subtotal")));

            WebElement promoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#offerCodeValue")));

            WebElement applyBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button.c-order-summary__offer-code-add-btn")));

            WebElement estimatedTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.c-order-summary__estimated-total")));

            WebElement checkoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button.c-checkout-buttons__checkout")));

            String titleText = title.getText().trim();
            String subtotalText = subtotalRow.getText().trim();
            String promoPlaceholder = promoInput.getAttribute("placeholder");
            String applyBtnText = applyBtn.getText().trim();
            String estimatedTotalText = estimatedTotal.getText().trim();
            String checkoutBtnText = checkoutBtn.getText().trim();

            boolean passed =
                    titleText.equalsIgnoreCase("Order summary")
                    && subtotalText.toLowerCase().contains("subtotal")
                    && promoInput.isDisplayed()
                    && applyBtnText.equalsIgnoreCase("Apply")
                    && estimatedTotalText.toLowerCase().contains("estimated total")
                    && checkoutBtnText.equalsIgnoreCase("Checkout Now");

            if (passed) {
                System.out.println("Order Summary PASS");
                System.out.println("Order Summary Contents:");
                System.out.println("Title: " + titleText);
                System.out.println("Subtotal: " + subtotalText);
                System.out.println("Promo Field Placeholder: " + promoPlaceholder);
                System.out.println("Apply Button: " + applyBtnText);
                System.out.println("Estimated Total: " + estimatedTotalText);
                System.out.println("Checkout Button: " + checkoutBtnText);
            } else {
                System.out.println("Order Summary FAIL");
                System.out.println("Reason: One or more summary elements are missing");
            }

        } catch (Exception e) {
            System.out.println("Order Summary FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testRemoveItemFromCart(WebDriver driver, WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {

        System.out.println("----- Testing Remove Item -----");

        try {
            WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.order-product-item-container")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", cartItem);
            Thread.sleep(1000);

            int beforeCartCount = driver.findElements(
                    By.cssSelector("div.order-product-item-container")).size();

            WebElement removeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("button[data-analytics-name='remove_item']")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", removeBtn);
            Thread.sleep(500);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(removeBtn));
                removeBtn.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", removeBtn);
            }

            wait.until(driver1 -> {
                try {
                    int afterCartCount = driver1.findElements(
                            By.cssSelector("div.order-product-item-container")).size();

                    String bodyText = driver1.findElement(By.tagName("body")).getText();

                    return afterCartCount < beforeCartCount
                            || bodyText.contains("0 Items")
                            || bodyText.contains("Your Bag Is Empty");
                } catch (Exception e) {
                    return false;
                }
            });

            System.out.println("Remove Item PASS");

        } catch (Exception e) {
            System.out.println("Remove Item FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static int getCurrentQuantity(WebDriver driver, WebDriverWait wait) {
        WebElement qtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.c-universal-product-details__quantity-value")));

        String txt = qtyElement.getText().replaceAll("[^0-9]", "").trim();
        return Integer.parseInt(txt);
    }
}