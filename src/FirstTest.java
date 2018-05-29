import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

/**
 * Created by irinagavrilova on 5/17/18.
 */
public class FirstTest {
  private AndroidDriver driver;

  @Before
  public void setUp() throws Exception {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "AndroidTestDevice");
    capabilities.setCapability("platformVersion", "8.0");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");
    capabilities.setCapability("app", "/Users/irinagavrilova/Desktop/Devel/trainings/JavaAppiumAutomation/apks/org.wikipedia.apk");

    driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void FirstTest() {
    waitForElementByXpathAndClick(
            "//*[contains(@text,'Search Wikipedia')]",
            "Cannot find Search Wikipedia input",
            5);
    waitForElementByXpathAndSendKeys(
            "//*[contains(@text,'Search…')]",
            "Java",
            "Cannot find search input",
            5);

    waitElementByXPath(
            "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']",
            "Cannot find 'Object-oriented programming language' text in topuc searching by 'Java'",
            15);
  }

  private WebElement waitElementByXPath(String xpath, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    By by = By.xpath(xpath);
    return wait.until(presenceOfElementLocated(by));
  }

  private WebElement waitElementByXPath(String xpath, String error_message) {
    return waitElementByXPath(xpath, error_message, 5);
  }

  private WebElement waitForElementByXpathAndClick(String xpath, String error_message, long timeoutInSeconds) {
    //wait for present of element and after click on it
    WebElement element = waitElementByXPath(xpath, error_message, timeoutInSeconds);
    element.click();
    return element;
  }

  private WebElement waitForElementByXpathAndSendKeys(String xpath, String value, String error_message, long timeoutInSeconds) {
    WebElement element = waitElementByXPath(xpath, error_message, timeoutInSeconds);
    element.sendKeys(value);
    return element;
  }
}


