package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Created by irinagavrilova on 6/27/18.
 */
public class NavigationUI extends MainPageObject {

  public NavigationUI (AppiumDriver driver) {
    super(driver);
  }
  private static final String
  MY_LISTS_LINK = "//android.widget.FrameLayout[@content-desc='My lists']";

  public void goToMyListPage() {
    this.waitForElementAndClick(
            By.xpath(MY_LISTS_LINK),
            "Cannot find navigation button 'My lists' to open 'My lists' folder");
  }
}
