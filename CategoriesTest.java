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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CategoriesTest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.garnethill.com/");
        System.out.println("Home page opened");
     
        Thread.sleep(4000);

        driver.get("https://www.garnethill.com/clothing/categories/?index=2");
        System.out.println("Categories  page opened");
        Thread.sleep(4000);

        //testCategorySlider(driver, js);
      //  testSortBySection(driver, js);
        //testSidebarCategories(driver, js);
      //  testFilters(driver, js);
      //testPaginationSection(driver,js);
        testProductCard(driver, js);
        
        driver.quit();
    }
    public static void testCategorySlider(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("Testing Category Slider ");

        try {

            driver.get("https://www.garnethill.com/");
            System.out.println("Home page opened");
            Thread.sleep(4000);

            driver.get("https://www.garnethill.com/clothing/categories/?index=2");
            System.out.println("Categories page opened");
            Thread.sleep(4000);

            WebElement slider = driver.findElement(
                    By.xpath("//div[contains(@class,'c-slider__content')]")
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", slider);
            Thread.sleep(2000);

           
            try {

                WebElement nextButton = driver.findElement(
                        By.xpath("//button[@aria-label='Next']")
                );

                js.executeScript("arguments[0].click();", nextButton);
                Thread.sleep(2000);

                System.out.println("Next arrow click PASS");

                WebElement prevButton = driver.findElement(
                        By.xpath("//button[@aria-label='Previous']")
                );

                js.executeScript("arguments[0].click();", prevButton);
                Thread.sleep(2000);

                System.out.println("Previous arrow click PASS");

            } catch (Exception e) {
                System.out.println("Slider arrows ERROR: " + e.getMessage());
            }

            
            try {

                WebElement range = driver.findElement(
                        By.xpath("//input[contains(@class,'c-slider__range')]")
                );

                // move to middle
                js.executeScript("arguments[0].value = arguments[0].max/2;", range);
                js.executeScript("arguments[0].dispatchEvent(new Event('input'));", range);
                Thread.sleep(2000);

                System.out.println("Progress bar move (middle) PASS");

                // move to end
                js.executeScript("arguments[0].value = arguments[0].max;", range);
                js.executeScript("arguments[0].dispatchEvent(new Event('input'));", range);
                Thread.sleep(2000);

                System.out.println("Progress bar move (end) PASS");

            } catch (Exception e) {
                System.out.println("Progress bar ERROR: " + e.getMessage());
            }

         
            int count = driver.findElements(
                    By.xpath("//div[contains(@class,'c-slider__item')]")
            ).size();

            System.out.println("Total cards: " + count);

           
            for (int i = 1; i <= count; i++) {

                // IMAGE CLICK
                try {

                    WebElement image = driver.findElement(
                            By.xpath("(//a[contains(@class,'c-universal-product-item__main-image')])[" + i + "]")
                    );

                    String before = driver.getCurrentUrl();

                    js.executeScript("arguments[0].click();", image);
                    Thread.sleep(3000);

                    String after = driver.getCurrentUrl();

                    if (!before.equals(after)) {
                        System.out.println("Card " + i + " → IMAGE CLICK");
                        System.out.println("Opened link: " + after);
                    } else {
                        System.out.println("Card " + i + " → IMAGE FAIL");
                    }

                    driver.navigate().back();
                    Thread.sleep(3000);

                    slider = driver.findElement(By.xpath("//div[contains(@class,'c-slider__content')]"));
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", slider);
                    Thread.sleep(1500);

                } catch (Exception e) {
                    System.out.println("Card " + i + " IMAGE ERROR: " + e.getMessage());
                }

                // TITLE CLICK
                try {

                    WebElement title = driver.findElement(
                            By.xpath("(//a[contains(@class,'c-universal-product-item-title-link')])[" + i + "]")
                    );

                    String before = driver.getCurrentUrl();

                    js.executeScript("arguments[0].click();", title);
                    Thread.sleep(3000);

                    String after = driver.getCurrentUrl();

                    if (!before.equals(after)) {
                        System.out.println("Card " + i + " → TITLE CLICK (Button)");
                        System.out.println("Opened link: " + after);
                    } else {
                        System.out.println("Card " + i + " → TITLE FAIL");
                    }

                    driver.navigate().back();
                    Thread.sleep(3000);

                    slider = driver.findElement(By.xpath("//div[contains(@class,'c-slider__content')]"));
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", slider);
                    Thread.sleep(1500);

                } catch (Exception e) {
                    System.out.println("Card " + i + " TITLE ERROR: " + e.getMessage());
                }
            }

          

        } catch (Exception e) {

            System.out.println("Category Slider test FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testSortBySection(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Sort By Section -----");

        try {

            driver.get("https://www.garnethill.com/clothing/categories/");
            Thread.sleep(4000);

            WebElement sort = driver.findElement(
                    By.xpath("//select[contains(@class,'c-product-list__sort-select-container')]")
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", sort);
            Thread.sleep(1500);

            if (sort.isDisplayed()) {
                System.out.println("Sort dropdown visible PASS");
            } else {
                System.out.println("Sort dropdown FAIL");
            }

            String defaultValue = sort.getAttribute("value");

            if (defaultValue.contains("productTopRated")) {
                System.out.println("Default = Top Rated PASS");
            } else {
                System.out.println("Default value FAIL: " + defaultValue);
            }

            String[] values = {
                    "default",
                    "startdate desc",
                    "productTopRated desc",
                    "unbxd_prime_sort_price asc",
                    "BazzarVoiceCombinedRating desc"
            };

            for (String val : values) {

                try {

                    String before = driver.getCurrentUrl();

                    js.executeScript(
                            "arguments[0].value='" + val + "';" +
                            "arguments[0].dispatchEvent(new Event('change'));",
                            sort
                    );

                    Thread.sleep(3000);

                    String after = driver.getCurrentUrl();

                    System.out.println("Selected: " + val);

                    if (!before.equals(after)) {
                        System.out.println("Sorting applied PASS");
                        System.out.println("New URL: " + after);
                    } else {
                        System.out.println("Sorting maybe applied (URL not changed)");
                    }

                } catch (Exception e) {
                    System.out.println("ERROR selecting " + val + ": " + e.getMessage());
                }
            }
         try {

             js.executeScript(
                     "arguments[0].value='unbxd_prime_sort_price asc';" +
                     "arguments[0].dispatchEvent(new Event('change'));",
                     sort
             );

             Thread.sleep(4000);

             List<WebElement> prices = driver.findElements(
                     By.xpath("//span[contains(@class,'price')]")
             );

             List<Double> priceValues = new ArrayList<>();

             for (WebElement p : prices) {
                 try {
                     String text = p.getText().replace("$", "").trim();
                     if (!text.isEmpty()) {
                         priceValues.add(Double.parseDouble(text));
                     }
                 } catch (Exception ignore) {}
             }

             boolean sorted = true;

             for (int i = 0; i < priceValues.size() - 1; i++) {
                 if (priceValues.get(i) > priceValues.get(i + 1)) {
                     sorted = false;
                     break;
                 }
             }

             if (sorted) {
                 System.out.println("Price Low → High SORT PASS");
             } else {
                 System.out.println("Price Low → High SORT FAIL");
             }

         } catch (Exception e) {
             System.out.println("Price sorting ERROR: " + e.getMessage());
         }

         try {

             js.executeScript(
                     "arguments[0].value='BazzarVoiceCombinedRating desc';" +
                     "arguments[0].dispatchEvent(new Event('change'));",
                     sort
             );

             Thread.sleep(4000);

             List<WebElement> ratings = driver.findElements(
                     By.xpath("//div[contains(@class,'rating')]//span")
             );

             List<Double> ratingValues = new ArrayList<>();

             for (WebElement r : ratings) {
                 try {
                     String val = r.getAttribute("data-rating");
                     if (val != null) {
                         ratingValues.add(Double.parseDouble(val));
                     }
                 } catch (Exception ignore) {}
             }

             boolean sortedDesc = true;

             for (int i = 0; i < ratingValues.size() - 1; i++) {
                 if (ratingValues.get(i) < ratingValues.get(i + 1)) {
                     sortedDesc = false;
                     break;
                 }
             }

             if (sortedDesc) {
                 System.out.println("Rating High → Low SORT PASS");
             } else {
                 System.out.println("Rating High → Low SORT FAIL");
             }

         } catch (Exception e) {
             System.out.println("Rating sorting ERROR: " + e.getMessage());
         }

            System.out.println("Sort By testing completed");

        } catch (Exception e) {

            System.out.println("Sort By test FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testSidebarCategories(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Sidebar Categories -----");

        try {

            driver.get("https://www.garnethill.com/clothing/categories/");
            Thread.sleep(5000);

            WebElement sidebar = driver.findElement(
                    By.xpath("//ul[contains(@class,'c-categories-listing-side-box__categories')]")
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", sidebar);
            Thread.sleep(2000);

            if (sidebar.isDisplayed()) {
                System.out.println("Sidebar visible PASS");
            } else {
                System.out.println("Sidebar FAIL");
            }

            System.out.println("----- Testing Features Section -----");

            try {

                // تأكد انه مفتوح
                WebElement featuresBtn = driver.findElement(
                        By.xpath("//a[contains(@href,'/clothing/features')]")
                );

                WebElement toggle = driver.findElement(
                        By.xpath("//a[contains(@href,'/clothing/features')]/following-sibling::button")
                );

                String expanded = toggle.getAttribute("aria-expanded");

                if (!expanded.equals("true")) {
                    js.executeScript("arguments[0].click();", toggle);
                    Thread.sleep(2000);
                    System.out.println("Features expanded");
                }
                List<WebElement> featuresList = driver.findElements(
                        By.xpath("//ul[contains(@class,'sidebar_ul_191620')]//a")
                );

                System.out.println("Features count: " + featuresList.size());

                for (int i = 1; i <= featuresList.size(); i++) {

                    try {

                        List<WebElement> updatedFeatures = driver.findElements(
                                By.xpath("//ul[contains(@class,'sidebar_ul_191620')]//a")
                        );

                        WebElement item = updatedFeatures.get(i - 1);

                        String name = item.getText();
                        String before = driver.getCurrentUrl();

                        js.executeScript("arguments[0].click();", item);
                        Thread.sleep(4000);

                        String after = driver.getCurrentUrl();

                        if (!before.equals(after)) {
                            System.out.println("Feature " + i + " (" + name + ") PASS");
                            System.out.println("Opened: " + after);
                        } else {
                            System.out.println("Feature " + i + " (" + name + ") FAIL");
                        }

                        driver.navigate().back();
                        Thread.sleep(4000);

                        sidebar = driver.findElement(
                                By.xpath("//ul[contains(@class,'c-categories-listing-side-box__categories')]")
                        );
                        js.executeScript("arguments[0].scrollIntoView({block:'center'});", sidebar);
                        Thread.sleep(1500);

                    } catch (Exception e) {
                        System.out.println("Feature " + i + " ERROR: " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                System.out.println("Features section ERROR: " + e.getMessage());
            }


            List<WebElement> categories = driver.findElements(
                    By.xpath("//ul[contains(@class,'sidebar_ul_191608')]//a")
            );

            System.out.println("Total categories: " + categories.size());

            for (int i = 1; i <= categories.size(); i++) {

                try {

                    List<WebElement> updatedList = driver.findElements(
                            By.xpath("//ul[contains(@class,'sidebar_ul_191608')]//a")
                    );

                    WebElement category = updatedList.get(i - 1);

                    String name = category.getText();
                    String before = driver.getCurrentUrl();

                    js.executeScript("arguments[0].click();", category);
                    Thread.sleep(4000);

                    String after = driver.getCurrentUrl();

                    if (!before.equals(after)) {
                        System.out.println("Category " + i + " (" + name + ") PASS");
                        System.out.println("Opened: " + after);
                    } else {
                        System.out.println("Category " + i + " (" + name + ") FAIL");
                    }

                    driver.navigate().back();
                    Thread.sleep(4000);

                    sidebar = driver.findElement(
                            By.xpath("//ul[contains(@class,'c-categories-listing-side-box__categories')]")
                    );
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", sidebar);
                    Thread.sleep(1500);

                } catch (Exception e) {
                    System.out.println("Category " + i + " ERROR: " + e.getMessage());
                }
            }

            System.out.println("All Sidebar + Features tested ");

        } catch (Exception e) {

            System.out.println("Sidebar test FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testFilters(WebDriver driver, JavascriptExecutor js) {

        System.out.println("----- Testing Filters Section (FIXED) -----");

        try {

            driver.get("https://www.garnethill.com/clothing/categories/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement filter = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.filter-label"))
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", filter);

            System.out.println("Filter visible PASS");

          
            int beforeCount = driver.findElements(
                    By.xpath("//div[contains(@class,'c-product-list-products__product-card')]")
            ).size();

            System.out.println("Products before: " + beforeCount);

           
            System.out.println("---- Multi Filter ----");

            try {

                wait.until(ExpectedConditions.elementToBeClickable(By.id("XS")));
                driver.findElement(By.id("XS")).click();

                wait.until(ExpectedConditions.stalenessOf(
                        driver.findElements(By.xpath("//div[contains(@class,'c-product-list-products__product-card')]")).get(0)
                ));

                wait.until(ExpectedConditions.elementToBeClickable(By.id("Black")));
                driver.findElement(By.id("Black")).click();

                Thread.sleep(3000);

                int afterCount = driver.findElements(
                        By.xpath("//div[contains(@class,'c-product-list-products__product-card')]")
                ).size();

                System.out.println("Products after: " + afterCount);

                if (afterCount <= beforeCount) {
                    System.out.println("Multi filter PASS");
                } else {
                    System.out.println("Multi filter FAIL");
                }

            } catch (Exception e) {
                System.out.println("Multi filter ERROR: " + e.getMessage());
            }


            System.out.println("---- Loop Sizes ----");

            String[] sizes = {"XS", "S", "M"};

            for (String s : sizes) {

                try {

                    wait.until(ExpectedConditions.elementToBeClickable(By.id(s)));
                    WebElement size = driver.findElement(By.id(s));

                    js.executeScript("arguments[0].click();", size);

                    Thread.sleep(2000);

                    System.out.println("Clicked size: " + s);

                    wait.until(ExpectedConditions.elementToBeClickable(By.id(s)));
                    driver.findElement(By.id(s)).click();

                } catch (Exception e) {
                    System.out.println("Size " + s + " ERROR: " + e.getMessage());
                }
            }

         
            System.out.println("---- View More ----");

            try {

                List<WebElement> viewMoreButtons = driver.findElements(
                        By.xpath("//div[contains(@class,'options-container_show-more')]")
                );

                if (!viewMoreButtons.isEmpty()) {

                    js.executeScript("arguments[0].click();", viewMoreButtons.get(0));
                    System.out.println("View More PASS");

                } else {
                    System.out.println("No View More found (OK)");
                }

            } catch (Exception e) {
                System.out.println("View More ERROR: " + e.getMessage());
            }

       
            System.out.println("---- Accordion ----");

            try {

                List<WebElement> accordions = driver.findElements(
                        By.cssSelector("button.c-accordion__header")
                );

                if (!accordions.isEmpty()) {

                    WebElement acc = accordions.get(0);

                    String before = acc.getAttribute("aria-expanded");

                    js.executeScript("arguments[0].click();", acc);

                    Thread.sleep(2000);

                    String after = acc.getAttribute("aria-expanded");

                    if (!before.equals(after)) {
                        System.out.println("Accordion PASS");
                    } else {
                        System.out.println("Accordion FAIL");
                    }

                }

            } catch (Exception e) {
                System.out.println("Accordion ERROR: " + e.getMessage());
            }

            System.out.println("Filters test completed ");

        } catch (Exception e) {

            System.out.println("Filter test FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testPaginationSection(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing ALL Pagination Pages -----");

        try {

            WebElement pagination = driver.findElement(
                    By.cssSelector(".c-product-list__pagination__bottom")
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", pagination);
            Thread.sleep(2000);

        
            String pageText = driver.findElement(
                    By.cssSelector(".c-pagination__current-page-message")
            ).getText(); // Page 1 of 9

            int totalPages = Integer.parseInt(pageText.split("of")[1].trim());

            System.out.println("Total pages: " + totalPages);

          
            for (int i = 1; i <= totalPages; i++) {

                String current = driver.findElement(
                        By.cssSelector(".c-pagination__current-page-message")
                ).getText();

                System.out.println("Now on: " + current);

                if (current.contains("Page " + i)) {
                    System.out.println("Page " + i + " PASS");
                } else {
                    System.out.println("Page " + i + " FAIL");
                }

                if (i == totalPages) {
                    break;
                }

                try {

                    WebElement nextBtn = driver.findElement(
                            By.cssSelector("button[aria-label='Next page']")
                    );

                    js.executeScript("arguments[0].click();", nextBtn);
                    Thread.sleep(3000);

                } catch (Exception e) {
                    System.out.println("Next ERROR: " + e.getMessage());
                    break;
                }
            }

            System.out.println("All pages tested ");

        } catch (Exception e) {
            System.out.println("Pagination FULL test FAIL");
            System.out.println("Reason: " + e.getMessage());
        }
    }
    public static void testProductCard(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- Testing Product Cards UI (ENHANCED) -----");

        Actions actions = new Actions(driver);

        List<WebElement> cards = driver.findElements(
                By.cssSelector(".c-product-list-products__product-card")
        );

        for (int i = 0; i < cards.size(); i++) {

            try {
                cards = driver.findElements(By.cssSelector(".c-product-list-products__product-card"));
                WebElement card = cards.get(i);

                js.executeScript("arguments[0].scrollIntoView({block:'center'});", card);
                Thread.sleep(1000);

                System.out.println("---- Card " + (i + 1) + " ----");

              
                String productName = "";
                String price = "";

                try {
                    productName = card.findElement(By.cssSelector(".c-product-title")).getText().trim();
                    price = card.findElement(By.cssSelector(".price")).getText().trim();

                    if (!productName.isEmpty()) {
                        System.out.println("Product name PASS");
                    } else {
                        System.out.println("Product name FAIL");
                    }

                    if (!price.isEmpty()) {
                        System.out.println("Price PASS");
                    } else {
                        System.out.println("Price FAIL");
                    }

                } catch (Exception e) {
                    System.out.println("Product data ERROR");
                }

                try {
                    WebElement img = card.findElement(By.cssSelector("img"));
                    String alt = img.getAttribute("alt");

                    if (alt != null && alt.toLowerCase().contains(productName.toLowerCase())) {
                        System.out.println("Image matches product PASS");
                    } else {
                        System.out.println("Image match FAIL");
                    }

                } catch (Exception e) {
                    System.out.println("Image validation ERROR");
                }

                actions.moveToElement(card).perform();
                Thread.sleep(1000);

                try {
                    WebElement quick = card.findElement(
                            By.xpath(".//div[contains(text(),'Quick Shop')]")
                    );

                    if (quick.isDisplayed()) {
                        System.out.println("Hover PASS");
                    }
                } catch (Exception e) {
                    System.out.println("Hover FAIL");
                }

               
                try {

                    WebElement img = card.findElement(By.cssSelector("img"));
                    String data = img.getAttribute("data-mainimage");

                    if (data != null && !data.isEmpty()) {

                        String[] images = data.split(",");
                        System.out.println("Total images: " + images.length);

                        List<WebElement> nextBtns = card.findElements(
                                By.cssSelector(".c-product-item__navigation-button--next")
                        );

                        if (!nextBtns.isEmpty()) {

                            for (int j = 0; j < images.length - 1; j++) {

                                js.executeScript("arguments[0].click();", nextBtns.get(0));
                                Thread.sleep(1200);

                                System.out.println("Moved to image " + (j + 2));
                            }

                            System.out.println("Image navigation PASS");

                        } else {
                            System.out.println("No arrows visible");
                        }

                    } else {
                        System.out.println("Single image only");
                    }

                } catch (Exception e) {
                    System.out.println("Image ERROR");
                }

                try {

                    List<WebElement> colors = card.findElements(
                            By.cssSelector(".personalization-swatches-link")
                    );

                    if (!colors.isEmpty()) {

                        WebElement color = colors.get(0);

                        WebElement img = card.findElement(By.cssSelector("img"));
                        String beforeImg = img.getAttribute("src");

                        js.executeScript("arguments[0].click();", color);
                        Thread.sleep(1500);

                        card = driver.findElements(By.cssSelector(".c-product-list-products__product-card")).get(i);

                        img = card.findElement(By.cssSelector("img"));
                        String afterImg = img.getAttribute("src");

                        if (!beforeImg.equals(afterImg)) {
                            System.out.println("Color Image change PASS");
                        } else {
                            System.out.println("Image same (maybe same color)");
                        }

                    } else {
                        System.out.println("No colors available");
                    }

                } catch (Exception e) {
                    System.out.println("Color ERROR");
                }

                try {

                    WebElement link = card.findElement(
                            By.cssSelector(".c-product-item-title-link")
                    );

                    String before = driver.getCurrentUrl();

                    js.executeScript("arguments[0].click();", link);
                    Thread.sleep(3000);

                    String after = driver.getCurrentUrl();

                    if (!before.equals(after)) {
                        System.out.println("PDP navigation PASS");
                    } else {
                        System.out.println("PDP navigation FAIL");
                    }

                    driver.navigate().back();
                    Thread.sleep(3000);

                } catch (Exception e) {
                    System.out.println("PDP navigation ERROR");
                }

            } catch (Exception e) {
                System.out.println("Card ERROR: " + e.getMessage());
            }
        }

        System.out.println("Product UI test completed ");
    }}
