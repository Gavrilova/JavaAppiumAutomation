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
    WebElement element_to_init_search = driver.findElement(By.xpath("//*[contains(@text,'Search Wikipedia')]"));
    element_to_init_search.click();
    WebElement element_to_enter_search_line = waitElementByXPath(
            "//*[contains(@text,'Search…')]",
            "Cannot find search input");
    element_to_enter_search_line.sendKeys("Appium");
    System.out.println("First test runs");
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
}


