package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by irinagavrilova on 6/27/18.
 */
public class ArticlePageObject extends MainPageObject {
  private static final String
          TITLE = "org.wikipedia:id/view_page_title_text",
          FOOTER = "//*[@text = 'View page in browser']",
  OPTIONS_BUTTON = "//android.widget.ImageView[@content-desc='More options']",
  OPTIONS_ADD_TO_READING_LIST = "//*[@text='Add to reading list']",
  ADD_TO_MY_LIST_OVERLAY = "org.wikipedia:id/onboarding_button",
  MY_LIST_NAME_INPUT = "org.wikipedia:id/text_input",
  MY_LIST_OK_BUTTON = "//*[@text='OK']",
  CLOSE_ARTICLE_X_BUTTON = "//android.widget.ImageButton[@content-desc='Navigate up']";

  public ArticlePageObject(AppiumDriver driver) {
    super(driver);
  }

  public WebElement waitForTitleElement() {
    return waitForElementPresent(
            By.id(TITLE),
            "Cannot find the title for article",
            15);
  }

  public String getArticleTitle() {
    WebElement title_element = waitForTitleElement();
    return title_element.getAttribute("text");
  }

  public void swipeToFooter() {
    this.swipeUpToElement(
            By.xpath(FOOTER),
            "Cannot find the end of the article",
            20);
  }

  public void addArticleToMyList(String name_of_folder){
    waitForElementAndClick(
            By.xpath(OPTIONS_BUTTON),
            "Cannot find 'More options' button to open article options");
    waitForElementAndClick(
            By.xpath(OPTIONS_ADD_TO_READING_LIST),
            "Cannot find options to add article to reading list ");
    waitForElementAndClick(
            By.id(ADD_TO_MY_LIST_OVERLAY),
            "Cannot 'GOT IT' tip overlay");
    waitForElementAndClear(
            By.id(MY_LIST_NAME_INPUT),
            "Cannot find input to set name of article's folder");
    waitForElementAndSendKeys(
            By.id(MY_LIST_NAME_INPUT),
            name_of_folder,
            "Cannot put text into article's folder input");
    waitForElementAndClick(
            By.xpath(MY_LIST_OK_BUTTON),
            "Cannot press 'OK' button to save article's folder");
  }

  public void closeArticle(){
    this.waitForElementAndClick(
            By.xpath(CLOSE_ARTICLE_X_BUTTON),
            "Cannot find 'X' button to close article");
  }
}
