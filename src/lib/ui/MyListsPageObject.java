package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Created by irinagavrilova on 6/27/18.
 */
public class MyListsPageObject extends MainPageObject {

  public static final String
          FOLDER_BY_NAME_TPL = "//*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER_NAME}']",
          ARTICLE_BY_TITLE_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{TITLE}']";

  public MyListsPageObject(AppiumDriver driver) {
    super(driver);
  }

  //TEMPLATES (TPL) METHODS
  private static String getFolderXpathByName(String substring) {
    return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", substring);
  }

  private static String getSavedArticleXpathByTitle(String substring) {
    return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", substring);
  }
  //TEMPLATES (TPL) METHODS

  public void openFolderByName(String name_of_folder) {
    this.waitForElementAndClick(
            By.xpath(getFolderXpathByName(name_of_folder)),
            "Cannot find created '" + name_of_folder + "' folder in 'My lists'");
  }

  public void waitForArticleToAppearByTitle(String article_title, String name_of_folder) {
    this.waitForElementPresent(
            By.xpath(getSavedArticleXpathByTitle(article_title)),
            "Cannot find saved article by title: '" + article_title + "' in '" + name_of_folder + "' folder");
  }

  public void waitForArticleToDisappearByTitle(String article_title) {
    this.waitForElementNotPresent(
            By.xpath(getSavedArticleXpathByTitle(article_title)),
            "Saved article still present. Cannot delete saved article '" + article_title + "'",
            15);
  }

  public void swipeByArticleToDisappearByTitle(String article_title, String name_of_folder) {
    this.waitForArticleToAppearByTitle(article_title, name_of_folder );
    String article_xpath = getSavedArticleXpathByTitle(article_title);
    this.swipeElementToLeft(
            By.xpath(article_xpath),
            "Cannot find article saved  article '" + article_title + "'");
    this.waitForArticleToDisappearByTitle(article_title);
  }

}
