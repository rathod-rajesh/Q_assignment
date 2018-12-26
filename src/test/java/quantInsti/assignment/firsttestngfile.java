package quantInsti.assignment;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class firsttestngfile {
    public String baseUrl = "https://quantra.quantinsti.com/";
    String driverPath = "browserDrivers/chromedriver";
    public WebDriver driver ;     
    
  @BeforeTest
  public void launchBrowser() {
	  System.setProperty("webdriver.chrome.drive", driverPath);
      driver = new ChromeDriver();
      driver.get(baseUrl);
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }
  @Test
  public void verifyHomepageTitle() throws InterruptedException, IOException {
	  waitForPageLoaded();
	  String winHandleBefore = driver.getWindowHandle();
	  WebElement login_topnav_link = driver.findElement(By.xpath("//a[@class='openLogin header-navbar-link']"));	   
	  
	  login_topnav_link.sendKeys(Keys.chord(Keys.CONTROL,Keys.RETURN));
	  waitForPageLoaded();
	  for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		}
	  WebElement username_input_field = driver.findElement(By.xpath("//input[@name='email']"));
	  WebElement password_input_field = driver.findElement(By.xpath("//input[@name='password']"));
	  WebElement submit_button = driver.findElement(By.xpath("//button[@type='submit']"));
	  
	  username_input_field.click();
	  username_input_field.clear();
	  username_input_field.sendKeys("sachin.p+dummy1@quantinsti.com");
	  password_input_field.click();
	  password_input_field.clear();
	  password_input_field.sendKeys("sachintest");
	  submit_button.click();
	  Thread.sleep(2000);
	  driver.switchTo().window(winHandleBefore);
	  waitForPageLoaded();
	  
	  WebElement user_nav = driver.findElement(By.cssSelector(".learner-profile-alt"));
	  WebElement user_profile = driver.findElement(By.cssSelector(".profile-icon"));
	  
	  //check mobile number validation
	  user_nav.click();
	  user_profile.click();
	  ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	  driver.switchTo().window(tabs.get(1));	  
	  waitForPageLoaded();
	  WebElement mobile_input_field = driver.findElement(By.xpath("//input[contains(@name,'mobile')]"));
	  WebElement myservicer_link = driver.findElement(By.xpath("//a[contains(text(),'My Services')]"));

	  mobile_input_field.click();
	  Thread.sleep(2000);
	  mobile_input_field.clear();	  
	  mobile_input_field.sendKeys("22222222");
	  submit_button.click();
	  
	  String expected = "Phone is not valid";
	  String actual= driver.findElement(By.id("swal2-content")).getText();
	  assertEquals(actual, expected);
	  
	  // valid number
	  mobile_input_field.click();
	  Thread.sleep(2000);
	  mobile_input_field.clear();	  
	  mobile_input_field.sendKeys("9768226717");	  
	  
	  // select yr-of-exp
	  Select yrs_of_exp = new Select(driver.findElement(By.xpath("//select[contains(@name,'exp')]")));
	  yrs_of_exp.selectByVisibleText("0-1 Year");
	  
	  // take screenshot
	  String datetime = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
	  File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	  FileUtils.copyFile(screenshotFile, new File("screeshoot/screenshot_"+datetime+".png"));
	  submit_button.click();
	  
	  //navigate to  myservicer_link
	  myservicer_link.click();
	  waitForPageLoaded();
	  WebElement quantra_lunch_button = driver.findElement(By.xpath("//button[contains(text(),'launch')]"));	
	  // no application found for quantra & ETP pages hence not coded
	  WebElement logout_link = driver.findElement(By.xpath("//a[contains(text(),'Logout')]"));
	  logout_link.click();
	  
	  driver.close();	  

 }
  @AfterTest
  public void terminateBrowser(){
     // driver.close();
  }
  
  public void waitForPageLoaded() {
      ExpectedCondition<Boolean> expectation = new
              ExpectedCondition<Boolean>() {
                  public Boolean apply(WebDriver driver) {
                      return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                  }
              };
      try {
          Thread.sleep(1000);
          WebDriverWait wait = new WebDriverWait(driver, 30);
          wait.until(expectation);
      } catch (Throwable error) {
          Assert.fail("Timeout waiting for Page Load Request to complete.");
      }
  }
}