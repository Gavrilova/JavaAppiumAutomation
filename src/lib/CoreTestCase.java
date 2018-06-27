package lib;

import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

/**
 * Created by irinagavrilova on 6/27/18.
 */
public class CoreTestCase extends TestCase {
  protected AndroidDriver driver;
  private static String AppiumURL = "http://127.0.0.1:4723/wd/hub";

  @Override
  public void setUp() throws Exception {
    super.setUp();
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "AndroidTestDevice");
    capabilities.setCapability("platformVersion", "8.0");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");
    capabilities.setCapability("app", "/Users/irinagavrilova/Desktop/Devel/trainings/JavaAppiumAutomation/apks/org.wikipedia.apk");

    driver = new AndroidDriver(new URL(AppiumURL), capabilities);
    setRotation(ScreenOrientation.PORTRAIT);

  }

  private void setRotation(ScreenOrientation rotation) {
    try {
      driver.rotate(rotation);
    } catch (Exception exc) {
      exc.printStackTrace();
    }
    assertEquals(
            "Cannot rotate screen to default " + rotation + " mode.",
            driver.getOrientation(),
            rotation);
  }

  @Override
  public void tearDown() throws Exception {
    driver.quit();
    driver = null;
    super.tearDown();
  }
}
