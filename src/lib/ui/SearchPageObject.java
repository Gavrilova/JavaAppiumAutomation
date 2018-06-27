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
          SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
          SEARCH_CANCEL_BUTTON = "org.wikipedia:id/search_close_btn",
          SEARCH_SRC_TEXT = "org.wikipedia:id/search_src_text";

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

  public void waitForCancelButtonToAppear() {
    this.waitForElementPresent(
            By.id(SEARCH_CANCEL_BUTTON),
            "Cannot find Search Cancel button.");
  }

  public void waitForCancelButtonToDisappear() {
    this.waitForElementNotPresent(
            By.id(SEARCH_CANCEL_BUTTON),
            "Search Cancel button is still present on the screen!");
  }

  public void clearSearchTextField() {
    this.waitForElementAndClear(
            By.id(SEARCH_SRC_TEXT),
            "Cannot find search test field");
  }
  public void clickCancelSearch() {
    this.waitForElementAndClick(
            By.id(SEARCH_CANCEL_BUTTON),
            "Cannot find and click to Search Cancel button");
  }

  public void waitForSearchResult(String substring) {
    String search_result_xpath = getResultSearchElement(substring);
    this.waitForElementPresent(
            By.xpath(search_result_xpath),
            "Cannot find search result with substring " + substring);
  }
}
