package atoumationtest;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class pdp {

	public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.garnethill.com/");
        System.out.println("Website opened successfully");
        openRandomProduct(driver, js);
       // testPDP(driver, js);
       // testPDPImageArrows(driver, js);
       // testImageZoom(driver);
       // testSideImges(driver, js);
       // testRatingSummary(driver, js);
        testReviewsSection(driver, js);
        
        
      // testQuantityAndPrice(driver);
        driver.quit();

	}
	
	public static void openRandomProduct(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

	 
	    driver.get("https://www.garnethill.com/clothing/categories/");
	    Thread.sleep(3000);

	    List<WebElement> products = driver.findElements(
	            By.cssSelector(".c-product-item-title-link")
	    );

	    if (!products.isEmpty()) {

	        js.executeScript("arguments[0].click();", products.get(0));
	        Thread.sleep(3000);

	        System.out.println("Product page opened");

	    } else {
	        System.out.println("No products found");
	    }
	}
	public static void testPDP(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

	    System.out.println("----- Testing PDP (FIXED) -----");

	
	    try {

	        WebElement name = driver.findElement(By.cssSelector("h1"));
	        WebElement price = driver.findElement(By.cssSelector(".price"));

	        if (name.isDisplayed() && price.isDisplayed()) {
	            System.out.println("PDP load PASS");
	        }

	    } catch (Exception e) {
	        System.out.println("PDP load FAIL");
	    }

	    try {

	        List<WebElement> nextBtns = driver.findElements(
	                By.cssSelector("[data-analytics-content='next']")
	        );

	        if (!nextBtns.isEmpty()) {

	            WebElement img = driver.findElement(By.cssSelector(".c-carousel__item.is--active img"));
	            String before = img.getAttribute("src");

	            js.executeScript("arguments[0].click();", nextBtns.get(0));
	            Thread.sleep(1500);

	            img = driver.findElement(By.cssSelector(".c-carousel__item.is--active img"));
	            String after = img.getAttribute("src");

	            if (!before.equals(after)) {
	                System.out.println("Image change PASS");
	            } else {
	                System.out.println("Image NOT changed");
	            }

	        } else {
	            System.out.println("No image arrows");
	        }

	    } catch (Exception e) {
	        System.out.println("Image ERROR");
	    }
	    try {

	        List<WebElement> colors = driver.findElements(
	                By.cssSelector(".c-universal-options__option-swatch")
	        );

	        if (!colors.isEmpty()) {

	            WebElement color = colors.get(0);

	            js.executeScript("arguments[0].click();", color);
	            Thread.sleep(1500);

	            String selected = color.getAttribute("aria-checked");

	            if ("true".equals(selected)) {
	                System.out.println("Color PASS");
	            } else {
	                System.out.println("Color NOT selected");
	            }

	        } else {
	            System.out.println("No colors");
	        }

	    } catch (Exception e) {
	        System.out.println("Color ERROR");
	    }

	    try {

	        List<WebElement> sizes = driver.findElements(
	                By.cssSelector("[data-attr='size'] button")
	        );

	        if (!sizes.isEmpty()) {

	            WebElement size = sizes.get(0);
	            js.executeScript("arguments[0].click();", size);
	            Thread.sleep(1000);

	            System.out.println("Size clicked PASS");

	        } else {
	            System.out.println("No sizes (select color first)");
	        }

	    } catch (Exception e) {
	        System.out.println("Size ERROR");
	    }

	    try {

	        WebElement add = driver.findElement(
	                By.cssSelector("[data-cs-override-id='pdp_add_to_cart']")
	        );

	        if (add.isEnabled()) {

	            js.executeScript("arguments[0].click();", add);
	            Thread.sleep(2000);

	            System.out.println("Add to cart PASS");

	        } else {
	            System.out.println("Add to cart DISABLED (need size)");
	        }

	    } catch (Exception e) {
	        System.out.println("Add to cart ERROR");
	    }

	    try {

	        driver.navigate().refresh();
	        Thread.sleep(2000);

	        WebElement add = driver.findElement(
	                By.cssSelector("[data-cs-override-id='pdp_add_to_cart']")
	        );

	        js.executeScript("arguments[0].click();", add);
	        Thread.sleep(2000);

	        if (driver.getPageSource().toLowerCase().contains("select")) {
	            System.out.println("Validation PASS");
	        } else {
	            System.out.println("Validation maybe not visible");
	        }

	    } catch (Exception e) {
	        System.out.println("Validation ERROR");
	    }

	    System.out.println("PDP test completed ");
	}
	public static void testPDPImageArrows(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

	    System.out.println("----- Testing PDP Image Arrows -----");

	    try {

	        List<WebElement> nextBtns = driver.findElements(
	                By.cssSelector("[data-analytics-content='next']")
	        );

	        if (!nextBtns.isEmpty()) {

	            WebElement img = driver.findElement(
	                    By.cssSelector(".c-carousel__item.is--active img")
	            );

	            String before = img.getAttribute("src");

	            js.executeScript("arguments[0].click();", nextBtns.get(0));
	            Thread.sleep(1500);

	            img = driver.findElement(
	                    By.cssSelector(".c-carousel__item.is--active img")
	            );

	            String after = img.getAttribute("src");

	            if (!before.equals(after)) {
	                System.out.println("Image arrows PASS");
	            } else {
	                System.out.println("Image NOT changed ");
	            }

	        } else {
	            System.out.println("No arrows found");
	        }

	    } catch (Exception e) {
	        System.out.println("Image arrows ERROR");
	    }
	}
	public static void testImageZoom(WebDriver driver) throws InterruptedException {

	    System.out.println("----- Testing Image Zoom (Hover) -----");

	    Actions actions = new Actions(driver);

	    try {

	        WebElement image = driver.findElement(
	                By.cssSelector(".c-carousel__item.is--active img")
	        );

	        actions.moveToElement(image).perform();
	        Thread.sleep(1500);

	        List<WebElement> zoom = driver.findElements(
	                By.cssSelector(".c-zoom-magnifier__lens")
	        );

	        if (!zoom.isEmpty() && zoom.get(0).isDisplayed()) {
	            System.out.println("Zoom hover PASS");
	        } else {
	            System.out.println("Zoom NOT visible ");
	        }

	    } catch (Exception e) {
	        System.out.println("Zoom ERROR");
	    }
	}
	public static void testSideImges(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

	    System.out.println("----- Testing side Gallery -----");

	    try {

	        List<WebElement> thumbs = driver.findElements(
	                By.cssSelector(".c-horizontal-scrollbar__item a")
	        );

	        System.out.println("Total imgs: " + thumbs.size());

	        for (int i = 0; i < thumbs.size(); i++) {

	            try {

	                thumbs = driver.findElements(By.cssSelector(".c-horizontal-scrollbar__item a"));
	                WebElement thumb = thumbs.get(i);

	                js.executeScript("arguments[0].scrollIntoView({block:'center'});", thumb);
	                Thread.sleep(800);

	                WebElement mainImg = driver.findElement(
	                        By.cssSelector(".c-carousel__item.is--active img")
	                );

	                String before = mainImg.getAttribute("src");

	                js.executeScript("arguments[0].click();", thumb);
	                Thread.sleep(1500);

	                WebElement activeThumb = driver.findElement(
	                        By.cssSelector(".c-horizontal-scrollbar__item a.is--active")
	                );

	                if (activeThumb.equals(thumb)) {
	                    System.out.println("img " + (i + 1) + " ACTIVE PASS");
	                } else {
	                    System.out.println("img " + (i + 1) + " ACTIVE FAIL");
	                }
	                mainImg = driver.findElement(
	                        By.cssSelector(".c-carousel__item.is--active img")
	                );

	                String after = mainImg.getAttribute("src");

	                if (!before.equals(after)) {
	                    System.out.println("Image updated PASS");
	                } else {
	                    System.out.println("Image NOT changed ");
	                }

	            } catch (Exception e) {
	                System.out.println("Thumbnail ERROR");
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("Gallery NOT FOUND");
	    }

	    System.out.println("Thumbnails test completed ");
	}
	
	public static void testRatingSummary(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

	    System.out.println("----- Testing Rating Summary Section -----");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    try {
	        WebElement section = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(
	                        By.cssSelector(".bv_main_container")
	                )
	        );

	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", section);
	        Thread.sleep(1000);

	        if (section.isDisplayed()) {
	            System.out.println("Rating summary section PASS");
	        } else {
	            System.out.println("Rating summary section FAIL");
	        }

	        
	        try {
	            WebElement avgButton = section.findElement(
	                    By.cssSelector(".bv_ratings_summary.bv_main_rating_button[aria-label*='average rating value']")
	            );

	            WebElement avgValue = avgButton.findElement(
	                    By.cssSelector(".bv_avgRating_component_container")
	            );

	            String avgText = avgValue.getText().trim();
	            String ariaText = avgButton.getAttribute("aria-label");

	            if (!avgText.isEmpty()) {
	                System.out.println("Average rating PASS");
	                System.out.println("Average rating value: " + avgText);
	                System.out.println("Average rating aria: " + ariaText);
	            } else {
	                System.out.println("Average rating FAIL");
	            }

	        } catch (Exception e) {
	            System.out.println("Average rating ERROR");
	        }
	        try {
	            WebElement starsContainer = section.findElement(
	                    By.cssSelector(".bv_stars_component_container")
	            );

	            List<WebElement> stars = starsContainer.findElements(By.cssSelector("svg"));

	            if (stars.size() == 5) {
	                System.out.println("Stars count PASS");
	            } else {
	                System.out.println("Stars count FAIL: " + stars.size());
	            }

	        } catch (Exception e) {
	            System.out.println("Stars ERROR");
	        }

	        try {
	            WebElement reviewsCount = section.findElement(
	                    By.cssSelector(".bv_numReviews_text")
	            );

	            String reviewsText = reviewsCount.getText().trim();

	            if (!reviewsText.isEmpty()) {
	                System.out.println("Reviews count PASS");
	                System.out.println("Reviews count value: " + reviewsText);
	            } else {
	                System.out.println("Reviews count FAIL");
	            }

	        } catch (Exception e) {
	            System.out.println("Reviews count ERROR");
	        }

	        try {
	            WebElement writeReviewBtn = section.findElement(
	                    By.cssSelector(".bv_button_buttonMinimalist.bv_war_button")
	            );

	            if (writeReviewBtn.isDisplayed() && writeReviewBtn.isEnabled()) {
	                System.out.println("Write review button PASS");
	            } else {
	                System.out.println("Write review button FAIL");
	            }

	        } catch (Exception e) {
	            System.out.println("Write review button ERROR");
	        }

	        System.out.println("Rating summary test completed ");

	    } catch (Exception e) {
	        System.out.println("Rating summary test FAIL");
	        System.out.println("Reason: " + e.getMessage());
	    }
	}
	public static void testQuantityAndPrice(WebDriver driver) throws InterruptedException {

	    System.out.println("----- Testing Quantity & TOTAL Calculation (FINAL ROBUST) -----");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    try {
	        List<WebElement> colors = driver.findElements(
	                By.cssSelector(".c-universal-options__option-swatch")
	        );

	        if (!colors.isEmpty()) {
	            js.executeScript("arguments[0].click();", colors.get(0));
	            Thread.sleep(1500);
	            System.out.println("Color selected ");
	        } else {
	            System.out.println("No color options found");
	        }

	        boolean sizeSelected = false;
	        String chosenSize = "";

	        // selector 1: buttons inside size option block
	        List<WebElement> sizeCandidates = driver.findElements(
	                By.xpath(
	                    "//span[contains(@class,'option-name') and contains(normalize-space(.),'Size')]" +
	                    "/ancestor::div[contains(@class,'c-universal-options__option')][1]" +
	                    "//button[" +
	                    "not(contains(@class,'size-chart-button')) and " +
	                    "not(contains(@class,'c-universal-options__size-chart-button')) and " +
	                    "not(@disabled)" +
	                    "]"
	                )
	        );

	        if (sizeCandidates.isEmpty()) {
	            sizeCandidates = driver.findElements(
	                    By.xpath(
	                        "//div[contains(@class,'c-universal-options')]//button[" +
	                        "not(contains(@class,'c-universal-options__option-swatch')) and " +
	                        "not(contains(@class,'size-chart-button')) and " +
	                        "not(contains(@class,'c-universal-options__size-chart-button')) and " +
	                        "not(@disabled) and normalize-space(.)!=''" +
	                        "]"
	                    )
	            );
	        }

	        for (WebElement btn : sizeCandidates) {
	            try {
	                String txt = btn.getText().trim();
	                if (btn.isDisplayed() && btn.isEnabled() && !txt.isEmpty()) {
	                    js.executeScript("arguments[0].click();", btn);
	                    Thread.sleep(1500);
	                    chosenSize = txt;
	                    sizeSelected = true;
	                    System.out.println("Size selected: " + chosenSize);
	                    break;
	                }
	            } catch (Exception ignore) {}
	        }

	        if (!sizeSelected) {
	            System.out.println("No clickable size found ");
	            System.out.println("Likely this product did not render size buttons after color selection");
	            return;
	        }

	        WebElement unitPriceEl = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(
	                        By.cssSelector(".c-universal-price-header .price")
	                )
	        );

	        String unitRaw = unitPriceEl.getAttribute("aria-label");
	        if (unitRaw == null || unitRaw.trim().isEmpty()) {
	            unitRaw = unitPriceEl.getText();
	        }

	        String unitClean = unitRaw.replaceAll("[^0-9.]", "");
	        double unitPrice = Double.parseDouble(unitClean);

	        System.out.println("Unit price: " + unitPrice);
D
	        
	        WebElement qtyDropdown = wait.until(driver1 -> {
	            WebElement el = driver1.findElement(
	                    By.cssSelector("[data-cs-override-id='pdp_quantity_dropdown']")
	            );
	            return el.isEnabled() ? el : null;
	        });

	        int quantity = 3;
	        Select qtySelect = new Select(qtyDropdown);
	        qtySelect.selectByValue(String.valueOf(quantity));
	        Thread.sleep(2500);

	        System.out.println("Quantity selected: " + quantity);

	        WebElement totalContainer = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(
	                        By.cssSelector(".c-universal-total-price-v2")
	                )
	        );

	        String totalRaw = totalContainer.getAttribute("aria-label");
	        if (totalRaw == null || totalRaw.trim().isEmpty()) {
	            WebElement totalPrice = totalContainer.findElement(By.cssSelector(".price"));
	            totalRaw = totalPrice.getAttribute("aria-label");
	            if (totalRaw == null || totalRaw.trim().isEmpty()) {
	                totalRaw = totalPrice.getText();
	            }
	        }

	        String totalClean = totalRaw.replaceAll("[^0-9.]", "");
	        double actualTotal = Double.parseDouble(totalClean);

	        double expectedTotal = unitPrice * quantity;

	        System.out.println("Chosen size: " + chosenSize);
	        System.out.println("Chosen quantity: " + quantity);
	        System.out.println("Expected total (" + unitPrice + " x " + quantity + "): " + expectedTotal);
	        System.out.println("Actual total: " + actualTotal);

	        if (Math.abs(expectedTotal - actualTotal) < 0.01) {
	            System.out.println("Total calculation PASS ");
	        } else {
	            System.out.println("Total calculation FAIL ");
	        }

	    } catch (Exception e) {
	        System.out.println("Quantity test ERROR: " + e.getMessage());
	    }
	}
	public static void testReviewsSection(WebDriver driver, JavascriptExecutor js) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    try {
	        System.out.println("----- Testing Reviews Section -----");

	        for (int i = 0; i < 6; i++) {
	            js.executeScript("window.scrollBy(0, 600);");
	            Thread.sleep(1000);
	        }

	        WebElement readReviewsBtn = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                By.xpath("//button[contains(@aria-label,'Read') and contains(@aria-label,'reviews')]")
	            )
	        );

	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", readReviewsBtn);
	        Thread.sleep(1000);
	        js.executeScript("arguments[0].click();", readReviewsBtn);

	        System.out.println("Clicked 'Read Reviews'");

	        WebElement ratingRecap = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[contains(normalize-space(),'Rating Recap')]")
	            )
	        );

	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", ratingRecap);
	        Thread.sleep(1000);

	        if (!ratingRecap.isDisplayed()) {
	            throw new Exception("Rating Recap not visible");
	        }
	        System.out.println("Rating Recap PASS");

	        WebElement firstReviewCard = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                By.cssSelector("section[id^='bv-review-']")
	            )
	        );

	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstReviewCard);
	        Thread.sleep(1000);

	        if (!firstReviewCard.isDisplayed()) {
	            throw new Exception("Review card not visible");
	        }

	        List<WebElement> reviewTexts = firstReviewCard.findElements(
	            By.cssSelector("[id^='bv-review-text-'], p")
	        );

	        if (reviewTexts.isEmpty()) {
	            throw new Exception("Review text not found inside review card");
	        }

	        System.out.println("One review card PASS");
	        System.out.println("----- Testing Reviews Section PASS -----");

	    } catch (Exception e) {
	        System.out.println("Reviews section FAIL");
	        System.out.println("Reason: " + e.getMessage());
	    }
	}
	}
