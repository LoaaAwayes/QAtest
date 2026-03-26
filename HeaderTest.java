package atoumationtest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class HeaderTest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.garnethill.com/");
        Thread.sleep(3000);
        System.out.println("Website opened successfully");

       // testLogoVisible(driver);
       // testLogoNavigationToHome(driver);
       // testLogoScrollToTop(driver, js);
        //testHeaderHover(driver, actions);
        testAboutUs(driver, js);
        testSupport(driver, js);
        testSignIn(driver, js);
        testBag(driver, js);

        testDropdown(driver, actions, js, "New Arrivals",
                new String[] { "Clothing", "Swimwear", "Shoes & Accessories", "Bedding & Home" });

        testDropdown(driver, actions, js, "Clothing",
                new String[] { "Top-Rated Clothing", "The Vacation Shop", "The Cashmere Shop", "The Linen Shop",
                        "The EILEEN FISHER Shop", "The Organic-Cotton Shop", "The Petite Shop",
                        "Get the Look: In Bloom", "All Clothing", "Dresses", "Tops & Tees", "Sweaters", "Skirts",
                        "Pants", "Coats & Jackets", "Pajamas & Sleepwear", "Shorts", "Swimwear", "Athleisure",
                        "Intimates", "SHOP TOPS & TEES", "SHOP DRESSES" });

        testDropdown(driver, actions, js, "Swimwear",
                new String[] { "All Swim", "One-Piece Swimsuits", "Tankinis", "Bikinis", "Swim Cover-Ups",
                        "Designer Swimwear Collection", "SHOP SWIMWEAR", "SHOP COVER-UPS" });

        testDropdown(driver, actions, js, "Shoes & Accessories",
                new String[] { "All Shoes", "Flats", "Sandals", "Sneakers", "Clogs & Mules", "Heels & Wedges",
                        "Boots", "Slippers", "All Accessories", "Bags", "Scarves & Wraps", "Jewelry", "Hats",
                        "Belts", "Eyewear", "Socks" });

        testDropdown(driver, actions, js, "Linen",
                new String[] { "LINEN CLOTHING", "LINEN BEDDING & HOME" });

        testDropdown(driver, actions, js, "Bedding & Home",
                new String[] { "Top-Rated Home", "The Linen Shop", "The Flannel Shop", "Monogram Shop",
                        "Kids' Bedding", "Get the Look: From the Archives", "Get the Look: Bold Botanicals",
                        "All Bedding", "Quilts", "Sheets", "Duvet Covers", "Comforters", "Blankets & Throws",
                        "Bedding Basics", "Shams", "All Bath", "Towels", "Bath Rugs & Mats", "Shower Curtains",
                        "All Home Decor", "Furniture", "Pillow Covers", "Lighting", "Throws",
                        "Decorative Accessories", "All Rugs", "Area Rugs", "Doormats", "SHOP SHEETS",
                        "SHOP TOWELS" });

        testDropdown(driver, actions, js, "Sale",
                new String[] { "The Weekly Sale", "Cashmere Savings", "Just Reduced", "All Sale", "Clothing",
                        "Shoes & Accessories", "Bedding & Home" });

        driver.quit();
    }

    public static void testLogoVisible(WebDriver driver) throws InterruptedException {

        System.out.println("----- Testing Logo Visible -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement logo = driver.findElement(By.cssSelector("a[href='/']"));

            if (logo.isDisplayed()) {
                System.out.println("Logo PASS");
            } else {
                System.out.println("Logo FAIL");
            }

        } catch (Exception e) {
            System.out.println("Logo FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testLogoNavigationToHome(WebDriver driver) throws InterruptedException {

        System.out.println("----- Testing Logo Navigation To Home -----");

        try {
            driver.get("https://www.garnethill.com/clothing/");
            Thread.sleep(3000);

            WebElement logo = driver.findElement(By.cssSelector("a[href='/']"));
            logo.click();
            Thread.sleep(3000);

            if (driver.getCurrentUrl().equals("https://www.garnethill.com/")) {
                System.out.println("Logo navigation to home PASS");
            } else {
                System.out.println("Logo navigation to home FAIL");
                System.out.println("Current URL: " + driver.getCurrentUrl());
            }

        } catch (Exception e) {
            System.out.println("Logo navigation to home FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testLogoScrollToTop(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Logo Scroll To Top -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            js.executeScript("window.scrollBy(0,2000)");
            Thread.sleep(2000);

            WebElement logo = driver.findElement(By.cssSelector("a[href='/']"));
            logo.click();
            Thread.sleep(3000);

            Number scrollPosition = (Number) js.executeScript("return window.pageYOffset;");

            if (scrollPosition.intValue() == 0) {
                System.out.println("Logo scroll to top PASS");
            } else {
                System.out.println("Logo scroll to top FAIL");
                System.out.println("Scroll position: " + scrollPosition);
            }

        } catch (Exception e) {
            System.out.println("Logo scroll to top FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testHeaderHover(WebDriver driver, Actions actions) throws InterruptedException {

        System.out.println("----- Hover Test on Header Items -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            List<WebElement> itemsCount = driver.findElements(By.cssSelector(".nav-link-title"));

            for (int i = 0; i < itemsCount.size(); i++) {
                try {
                    List<WebElement> currentItems = driver.findElements(By.cssSelector(".nav-link-title"));
                    WebElement item = currentItems.get(i);

                    String itemText = item.getText().trim();

                    if (!itemText.isEmpty()) {
                        actions.moveToElement(item).perform();
                        Thread.sleep(1500);
                        System.out.println("Hover done on: " + itemText);
                    }

                } catch (Exception e) {
                    System.out.println("Hover failed on item index: " + i);
                }
            }

        } catch (Exception e) {
            System.out.println("Header hover FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testAboutUs(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing About Us -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement aboutUs = driver.findElement(By.cssSelector("a[href='/about-us/content']"));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", aboutUs);
            Thread.sleep(1000);

            String before = driver.getCurrentUrl();

            js.executeScript("arguments[0].click();", aboutUs);
            Thread.sleep(3000);

            String after = driver.getCurrentUrl();

            if (!before.equals(after)) {
                System.out.println("About Us PASS");
                System.out.println("Opened page: " + after);
            } else {
                System.out.println("About Us FAIL");
            }

        } catch (Exception e) {
            System.out.println("About Us FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testSupport(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Support -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement support = driver.findElement(By.cssSelector("a[href='/CustomerServiceFormView']"));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", support);
            Thread.sleep(1000);

            String before = driver.getCurrentUrl();

            js.executeScript("arguments[0].click();", support);
            Thread.sleep(3000);

            String after = driver.getCurrentUrl();

            if (!before.equals(after)) {
                System.out.println("Support PASS");
                System.out.println("Opened page: " + after);
            } else {
                System.out.println("Support FAIL");
            }

        } catch (Exception e) {
            System.out.println("Support FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testSignIn(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Sign In -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement signIn = driver.findElement(By.cssSelector("a[href='/UserLogonView']"));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", signIn);
            Thread.sleep(1000);

            String before = driver.getCurrentUrl();

            js.executeScript("arguments[0].click();", signIn);
            Thread.sleep(3000);

            String after = driver.getCurrentUrl();

            if (!before.equals(after)) {
                System.out.println("Sign In PASS");
                System.out.println("Opened page: " + after);
            } else {
                System.out.println("Sign In FAIL");
            }

        } catch (Exception e) {
            System.out.println("Sign In FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testBag(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Bag -----");

        try {
            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement bag = driver.findElement(By.cssSelector("button[data-analytics-name='show_mini_cart']"));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", bag);
            Thread.sleep(1000);

            js.executeScript("arguments[0].click();", bag);
            Thread.sleep(3000);

            System.out.println("Bag click PASS");

        } catch (Exception e) {
            System.out.println("Bag FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    public static void testDropdown(WebDriver driver, Actions actions, JavascriptExecutor js,
            String menuName, String[] itemNames) throws InterruptedException {

        System.out.println(" Testing " + menuName + " dropdown ");

        for (String itemName : itemNames) {
            try {
                driver.get("https://www.garnethill.com/");
                Thread.sleep(2500);

                WebElement menu = driver.findElement(
                        By.xpath("//span[contains(@class,'nav-link-title') and normalize-space()='" + menuName + "']")
                );

                actions.moveToElement(menu).perform();
                Thread.sleep(2500);

                boolean clicked = false;
                String beforeClick = driver.getCurrentUrl();

                List<WebElement> dropdownLinks = driver.findElements(By.xpath("//a[normalize-space()]"));

                for (WebElement link : dropdownLinks) {
                    try {
                        String text = link.getText().trim().replaceAll("\\s+", " ");

                        if (text.equalsIgnoreCase(itemName) && link.isDisplayed()) {
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
                    System.out.println(itemName + " link not found - FAIL");
                } else if (!afterClick.equals(beforeClick)) {
                    System.out.println(itemName + " navigation PASS");
                    System.out.println("Opened page: " + afterClick);
                } else {
                    System.out.println(itemName + " navigation FAIL");
                    System.out.println("Opened page: " + afterClick);
                }

            } catch (Exception e) {
                System.out.println(itemName + " test failed - FAIL");
                System.out.println("Reason: " + e.getMessage());
            }
        }
    }
}
