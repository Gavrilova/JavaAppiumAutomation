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
          FOOTER = "//*[@text = 'View page in browser']";

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
}
