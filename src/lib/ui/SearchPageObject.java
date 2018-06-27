package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Created by irinagavrilova on 6/27/18.
 */
public class SearchPageObject extends MainPageObject {

  private static final String
          SEARCH_INIT_ELEMENT = "//*[contains(@text,'Search Wikipedia')]",
          SEARCH_INPUT = "//*[contains(@text,'Search…')]",
          SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']";

  public SearchPageObject(AppiumDriver driver) {
    super(driver);
  }

  //TEMPLATES (TPL) METHODS
  private static String getResultSearchElement(String substring) {
    return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
  }
  //TEMPLATES (TPL) METHODS

  public void initSearchInput() {
    this.waitForElementAndClick(
            By.xpath(SEARCH_INIT_ELEMENT),
            "Cannot find and click search init element.");
    this.waitForElementPresent(
            By.xpath(SEARCH_INPUT),
            "Cannot find search input after clicking to search init element.");
  }

  public void typeSearchLine(String search_line) {
    this.waitForElementAndSendKeys(
            By.xpath(SEARCH_INPUT),
            search_line,
            "Cannot find and type into search line");
  }

  public void waitForSearchResult(String substring) {
    String search_result_xpath = getResultSearchElement(substring);
    this.waitForElementPresent(
            By.xpath(search_result_xpath),
            "Cannot find search result with substring " + substring);
  }
}
