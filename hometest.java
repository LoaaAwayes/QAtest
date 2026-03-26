package atoumationtest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class hometest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.garnethill.com/");
        System.out.println("Home page opened");

       testHeroCardClick(driver, js);
        testShopByCategoryClick(driver, js);
       testShorelineSection(driver, js);
      testTwoUpSection(driver, js);
        testEileenFisherSection(driver, js);
        testMainHeroBanner(driver, js);
        testJustForYouSection(driver, js);
        driver.quit();
    }

    
  
    public static void testHeroCardClick(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

        System.out.println("----- TC-home-3 Hero banner click -----");

        try {

            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement heroImage = driver.findElement(
                    By.xpath("//img[contains(@alt,'Shop the Linen Shop')]")
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", heroImage);
            Thread.sleep(1000);

            String before = driver.getCurrentUrl();

            js.executeScript("arguments[0].click();", heroImage);
            Thread.sleep(3000);

            String after = driver.getCurrentUrl();

            if (!before.equals(after)) {
                System.out.println("Hero image navigation PASS");
                System.out.println("Opened page: " + after);
            } else {
                System.out.println("Hero image navigation FAIL");
            }

            driver.get("https://www.garnethill.com/");
            Thread.sleep(3000);

            WebElement heroButton = driver.findElement(
                    By.xpath("//span[text()='SHOP NOW']")
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", heroButton);
            Thread.sleep(1000);

            before = driver.getCurrentUrl();

            js.executeScript("arguments[0].click();", heroButton);
            Thread.sleep(3000);

            after = driver.getCurrentUrl();

            if (!before.equals(after)) {
                System.out.println("Hero button navigation PASS");
                System.out.println("Opened page: " + after);
            } else {
                System.out.println("Hero button navigation FAIL");
            }

        } catch (Exception e) {

            System.out.println("Hero test FAIL");
            System.out.println("Reason: " + e.getMessage());

        }
    }


 public static void testShopByCategoryClick(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

     System.out.println("----- TC-home-5 Shop by Category click -----");

     String[] categoryNames = {
             "Sweaters",
             "Tops & Tees",
             "Dresses",
             "Swimwear",
             "Sleepwear",
             "Sheets",
             "Quilts",
             "Bedding Basics",
             "Bath",
             "Rugs"
     };

     for (String categoryName : categoryNames) {

         try {
             driver.get("https://www.garnethill.com/");
             Thread.sleep(3000);

             WebElement sectionTitle = driver.findElement(
                     By.xpath("//h4[contains(text(),'Shop by Category')]")
             );

             js.executeScript("arguments[0].scrollIntoView({block:'center'});", sectionTitle);
             Thread.sleep(1500);

             WebElement categoryLink = driver.findElement(
                     By.xpath("//div[@id='hp-5up__gallery']//a[.//p[normalize-space()='" + categoryName + "']]")
             );

             js.executeScript("arguments[0].scrollIntoView({block:'center'});", categoryLink);
             Thread.sleep(1000);

             String before = driver.getCurrentUrl();

             js.executeScript("arguments[0].click();", categoryLink);
             Thread.sleep(3000);

             String after = driver.getCurrentUrl();

             if (!before.equals(after)) {
                 System.out.println(categoryName + " navigation PASS");
                 System.out.println("Opened page: " + after);
             } else {
                 System.out.println(categoryName + " navigation FAIL");
             }

         } catch (Exception e) {
             System.out.println(categoryName + " test FAIL");
             System.out.println("Reason: " + e.getMessage());
         }
     }
 }
 public static void testShorelineSection(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

     System.out.println("----- Testing Shoreline Swim Section -----");

     try {

         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement section = driver.findElement(
                 By.xpath("//*[contains(text(),'Made to flatter') or contains(text(),'Shoreline')]")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", section);
         Thread.sleep(1500);

         //  Click Image
         WebElement image = driver.findElement(
                 By.xpath("//a[.//img and .//*[contains(text(),'Made to flatter') or contains(text(),'Shoreline')]]")
         );

         String before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", image);
         Thread.sleep(3000);

         String after = driver.getCurrentUrl();

         if (!before.equals(after)) {
             System.out.println("Image navigation PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("Image navigation FAIL");
         }

         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         //  Click Heading
         WebElement heading = driver.findElement(
                 By.xpath("//*[contains(text(),'Made to flatter') or contains(text(),'Shoreline')]")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", heading);
         Thread.sleep(1000);

         before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", heading);
         Thread.sleep(3000);

         after = driver.getCurrentUrl();

         if (!before.equals(after)) {
             System.out.println("Heading navigation PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("Heading navigation FAIL");
         }

         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

        

      WebElement shopNow = driver.findElement(
              By.xpath("//a[.//*[text()='SHOP NOW'] and .//*[contains(text(),'Made to flatter') or contains(text(),'Shoreline')]]")
      );

      js.executeScript("arguments[0].scrollIntoView({block:'center'});", shopNow);
      Thread.sleep(1000);

      String expectedUrl = "https://www.garnethill.com/swimwear/womens/facet/f-collection-uFilter/The__spShoreline__spCollection";

      js.executeScript("arguments[0].click();", shopNow);
      Thread.sleep(3000);

      String actualUrl = driver.getCurrentUrl();

      if (actualUrl.equals(expectedUrl)) {
          System.out.println("SHOP NOW button PASS");
          System.out.println("Opened page: " + actualUrl);
      } else {
          System.out.println("SHOP NOW button FAIL");
          System.out.println("Expected page: " + expectedUrl);
          System.out.println("Actual page: " + actualUrl);
      }

     } catch (Exception e) {

         System.out.println("Shoreline section test FAIL");
         System.out.println("Reason: " + e.getMessage());

     }
 }
 public static void testTwoUpSection(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

     System.out.println("----- Testing gh-content-2-up Section -----");

     try {

         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement firstImage = driver.findElement(
                 By.xpath("//img[@alt='Shop Doormats.']")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstImage);
         Thread.sleep(1000);

         String expectedUrl = "https://www.garnethill.com/bedding-home/rugs/doormats/";
         String before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", firstImage);
         Thread.sleep(3000);

         String after = driver.getCurrentUrl();

         if (after.equals(expectedUrl)) {
             System.out.println("First image navigation PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("First image navigation FAIL");
             System.out.println("Expected page: " + expectedUrl);
             System.out.println("Actual page: " + after);
         }

         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement firstHeading = driver.findElement(
                 By.xpath("//h2[contains(.,'Step up') and contains(.,'your doorstep')]")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstHeading);
         Thread.sleep(1000);

         expectedUrl = "https://www.garnethill.com/bedding-home/rugs/doormats/";
         before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", firstHeading);
         Thread.sleep(3000);

         after = driver.getCurrentUrl();

         if (after.equals(expectedUrl)) {
             System.out.println("First heading navigation PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("First heading navigation FAIL");
             System.out.println("Expected page: " + expectedUrl);
             System.out.println("Actual page: " + after);
         }

         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement firstButton = driver.findElement(
                 By.xpath("//div[text()='SHOP DOORMATS']")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstButton);
         Thread.sleep(1000);

         expectedUrl = "https://www.garnethill.com/bedding-home/rugs/doormats/";
         before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", firstButton);
         Thread.sleep(3000);

         after = driver.getCurrentUrl();

         if (after.equals(expectedUrl)) {
             System.out.println("SHOP DOORMATS button PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("SHOP DOORMATS button FAIL");
             System.out.println("Expected page: " + expectedUrl);
             System.out.println("Actual page: " + after);
         }

   
         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement secondImage = driver.findElement(
                 By.xpath("//img[@alt='Shop Wrinkle-Resistant Sateen.']")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", secondImage);
         Thread.sleep(1000);

         expectedUrl = "https://www.garnethill.com/bedding-home/all-home/facet/f-material-uFilter/Sateen";
         before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", secondImage);
         Thread.sleep(3000);

         after = driver.getCurrentUrl();

         if (after.equals(expectedUrl)) {
             System.out.println("Second image navigation PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("Second image navigation FAIL");
             System.out.println("Expected page: " + expectedUrl);
             System.out.println("Actual page: " + after);
         }

        
    
         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement secondHeading = driver.findElement(
                 By.xpath("//h2[contains(.,'Our') and contains(.,'Wrinkle-') and contains(.,'Resistant Sateen')]")
         );

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", secondHeading);
         Thread.sleep(1000);

         expectedUrl = "https://www.garnethill.com/bedding-home/all-home/facet/f-material-uFilter/Sateen";
         before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", secondHeading);
         Thread.sleep(3000);

         after = driver.getCurrentUrl();

         if (after.equals(expectedUrl)) {
             System.out.println("Second heading navigation PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("Second heading navigation FAIL");
             System.out.println("Expected page: " + expectedUrl);
             System.out.println("Actual page: " + after);
         }

       
         driver.get("https://www.garnethill.com/");
         Thread.sleep(3000);

         WebElement secondButton = driver.findElement(
        		    By.xpath("//h2[contains(.,'Wrinkle')]/ancestor::a//div[text()='SHOP NOW']")
        		);

         js.executeScript("arguments[0].scrollIntoView({block:'center'});", secondButton);
         Thread.sleep(1000);

         expectedUrl = "https://www.garnethill.com/bedding-home/all-home/facet/f-material-uFilter/Sateen";
         before = driver.getCurrentUrl();

         js.executeScript("arguments[0].click();", secondButton);
         Thread.sleep(3000);

         after = driver.getCurrentUrl();

         if (after.equals(expectedUrl)) {
             System.out.println("Second SHOP NOW button PASS");
             System.out.println("Opened page: " + after);
         } else {
             System.out.println("Second SHOP NOW button FAIL");
             System.out.println("Expected page: " + expectedUrl);
             System.out.println("Actual page: " + after);
         }

     } catch (Exception e) {

         System.out.println("gh-content-2-up section test FAIL");
         System.out.println("Reason: " + e.getMessage());

     }
 }
public static void testEileenFisherSection(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

  System.out.println("----- Testing Eileen Fisher Section -----");

  try {

      driver.get("https://www.garnethill.com/");
      Thread.sleep(3000);

     // Click Image
      WebElement image = driver.findElement(
              By.xpath("//img[@alt='Shop Eileen Fisher.']")
      );

      js.executeScript("arguments[0].scrollIntoView({block:'center'});", image);
      Thread.sleep(1000);

      String expectedUrl = "https://www.garnethill.com/clothing/features/the-eileen-fisher-shop/";
      String before = driver.getCurrentUrl();

      js.executeScript("arguments[0].click();", image);
      Thread.sleep(3000);

      String after = driver.getCurrentUrl();

      if (after.equals(expectedUrl)) {
          System.out.println("Image navigation PASS");
          System.out.println("Opened page: " + after);
      } else {
          System.out.println("Image navigation FAIL");
          System.out.println("Expected page: " + expectedUrl);
          System.out.println("Actual page: " + after);
      }

      driver.get("https://www.garnethill.com/");
      Thread.sleep(3000);

      WebElement heading = driver.findElement(
              By.xpath("//h2[contains(.,'EILEEN FISHER')]")
      );

      js.executeScript("arguments[0].scrollIntoView({block:'center'});", heading);
      Thread.sleep(1000);

      before = driver.getCurrentUrl();

      js.executeScript("arguments[0].click();", heading);
      Thread.sleep(3000);

      after = driver.getCurrentUrl();

      if (after.equals(expectedUrl)) {
          System.out.println("Heading navigation PASS");
          System.out.println("Opened page: " + after);
      } else {
          System.out.println("Heading navigation FAIL");
          System.out.println("Expected page: " + expectedUrl);
          System.out.println("Actual page: " + after);
      }

      driver.get("https://www.garnethill.com/");
      Thread.sleep(3000);

      WebElement button = driver.findElement(
              By.xpath("//div[text()='EILEEN FISHER SHOP']")
      );

      js.executeScript("arguments[0].scrollIntoView({block:'center'});", button);
      Thread.sleep(1000);

      before = driver.getCurrentUrl();

      js.executeScript("arguments[0].click();", button);
      Thread.sleep(3000);

      after = driver.getCurrentUrl();

      if (after.equals(expectedUrl)) {
          System.out.println("Button navigation PASS");
          System.out.println("Opened page: " + after);
      } else {
          System.out.println("Button navigation FAIL");
          System.out.println("Expected page: " + expectedUrl);
          System.out.println("Actual page: " + after);
      }

  } catch (Exception e) {

      System.out.println("Eileen Fisher section test FAIL");
      System.out.println("Reason: " + e.getMessage());

  }
}

public static void testMainHeroBanner(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

 System.out.println(" Testing Main Hero Banner ");

 try {

     driver.get("https://www.garnethill.com/");
     Thread.sleep(3000);

     // Click Hero Image
     WebElement heroImage = driver.findElement(
             By.xpath("//img[contains(@src,'iwd-webbannermobile.jpg')]")
     );

     js.executeScript("arguments[0].scrollIntoView({block:'center'});", heroImage);
     Thread.sleep(1000);

     String expectedUrl = "https://www.garnethill.com/inspiration-hub/behind-the-designs/womens-day-2026/";
     String before = driver.getCurrentUrl();

     js.executeScript("arguments[0].click();", heroImage);
     Thread.sleep(3000);

     String after = driver.getCurrentUrl();

     if (after.equals(expectedUrl)) {
         System.out.println("Hero banner navigation PASS");
         System.out.println("Opened page: " + after);
     } else {
         System.out.println("Hero banner navigation FAIL");
         System.out.println("Expected page: " + expectedUrl);
         System.out.println("Actual page: " + after);
     }

 } catch (Exception e) {

     System.out.println("Main Hero test FAIL");
     System.out.println("Reason: " + e.getMessage());

 }
}

public static void testJustForYouSection(WebDriver driver, JavascriptExecutor js) throws InterruptedException {

 System.out.println(" Testing Just For You Section ");

 try {

     driver.get("https://www.garnethill.com/");
     Thread.sleep(3000);

   
     WebElement section = driver.findElement(
             By.xpath("//h2[text()='Just For You']")
     );

     js.executeScript("arguments[0].scrollIntoView({block:'center'});", section);
     Thread.sleep(1500);

     WebElement firstProduct = driver.findElement(
             By.xpath("(//div[@class='c-slider__content']//a[contains(@class,'c-universal-product-item__main-image')])[1]")
     );

     String before = driver.getCurrentUrl();

     js.executeScript("arguments[0].click();", firstProduct);
     Thread.sleep(3000);

     String after = driver.getCurrentUrl();

     if (!before.equals(after)) {
         System.out.println("First product navigation PASS");
         System.out.println("Opened page: " + after);
     } else {
         System.out.println("First product navigation FAIL");
     }

    
     driver.get("https://www.garnethill.com/");
     Thread.sleep(3000);

     WebElement section2 = driver.findElement(
             By.xpath("//h2[text()='Just For You']")
     );

     js.executeScript("arguments[0].scrollIntoView({block:'center'});", section2);
     Thread.sleep(1500);

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


     for (int i = 1; i <= 5; i++) {

         try {

             driver.get("https://www.garnethill.com/");
             Thread.sleep(3000);

             WebElement sec = driver.findElement(
                     By.xpath("//h2[text()='Just For You']")
             );

             js.executeScript("arguments[0].scrollIntoView({block:'center'});", sec);
             Thread.sleep(1500);

             WebElement item = driver.findElement(
                     By.xpath("(//a[contains(@class,'c-universal-product-item__main-image')])[" + i + "]")
             );

             before = driver.getCurrentUrl();

             js.executeScript("arguments[0].click();", item);
             Thread.sleep(3000);

             after = driver.getCurrentUrl();

             if (!before.equals(after)) {
                 System.out.println("Image " + i + " PASS");
             } else {
                 System.out.println("Image " + i + " FAIL");
             }

         } catch (Exception e) {
             System.out.println("Image " + i + " ERROR: " + e.getMessage());
         }
     }

     for (int i = 1; i <= 5; i++) {

         try {

             driver.get("https://www.garnethill.com/");
             Thread.sleep(3000);

             WebElement sec = driver.findElement(
                     By.xpath("//h2[text()='Just For You']")
             );

             js.executeScript("arguments[0].scrollIntoView({block:'center'});", sec);
             Thread.sleep(1500);

             WebElement title = driver.findElement(
                     By.xpath("(//a[contains(@class,'c-universal-product-item-title-link')])[" + i + "]")
             );

             before = driver.getCurrentUrl();

             js.executeScript("arguments[0].click();", title);
             Thread.sleep(3000);

             after = driver.getCurrentUrl();

             if (!before.equals(after)) {
                 System.out.println("Title " + i + " PASS");
             } else {
                 System.out.println("Title " + i + " FAIL");
             }

         } catch (Exception e) {
             System.out.println("Title " + i + " ERROR: " + e.getMessage());
         }
     }

 } catch (Exception e) {

     System.out.println("Just For You section test FAIL");
     System.out.println("Reason: " + e.getMessage());

 }
}
}
