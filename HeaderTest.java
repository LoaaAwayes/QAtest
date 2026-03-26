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
        System.out.println("Website opened successfully");

      
        // Logo visible test
       
        if (driver.findElement(By.cssSelector("a[href='/']")).isDisplayed()) {
            System.out.println("Logo PASS");
        } else {
            System.out.println("Logo FAIL");
        }

        // TC-header-2
        driver.get("https://www.garnethill.com/clothing/");
        Thread.sleep(2000);

        driver.findElement(By.cssSelector("a[href='/']")).click();
        Thread.sleep(2000);

        if (driver.getCurrentUrl().equals("https://www.garnethill.com/")) {
            System.out.println("Logo navigation to home - PASS");
        } else {
            System.out.println("Logo navigation to home - FAIL");
            System.out.println("Current URL: " + driver.getCurrentUrl());
        }

        // TC-header-3
        js.executeScript("window.scrollBy(0,2000)");
        Thread.sleep(2000);

        driver.findElement(By.cssSelector("a[href='/']")).click();
        Thread.sleep(2000);

        Number scrollPosition = (Number) js.executeScript("return window.pageYOffset;");
        if (scrollPosition.intValue() == 0) {
            System.out.println("Logo scroll to top - PASS");
        } else {
            System.out.println("Logo scroll to top - FAIL");
            System.out.println("Scroll position: " + scrollPosition);
        }

        // Hover on all header items
        System.out.println("----- Hover Test on Header Items -----");

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

        // Dropdown tests

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