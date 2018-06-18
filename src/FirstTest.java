import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

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

  @Ignore
  @Test
  public void testSearch() {
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input",
            5);

    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot find 'Object-oriented programming language' text in topic searching by 'Java'",
            15);
  }

  @Ignore
  @Test
  public void testCancelSearch() {

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Cannot find 'Search Wikipedia' input",
            5);

    waitForElementAndSendKeys(
            By.xpath("/*//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input",
            5);
    waitForElementAndClear(
            By.id("org.wikipedia:id/search_src_text"),
            "Cannot find search field",
            5
    );

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_close_btn"),
            "Cannot find 'X to cancel search",
            5
    );
    waitForElementNotPresent(
            By.id("org.wikipedia:id/search_close_btn"),
            "'X' is still present on the page",
            5
    );
  }

  @Ignore
  @Test
  public void testCompareArticleTitle() {
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input",
            5);
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'",
            5);
    WebElement title_element = waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
    String article_title = title_element.getAttribute("text");

    assertEquals("We see unexpected title",
            "Java (programming language)",
            article_title);
  }

  @Ignore
  @Test
  public void testSwipeArticle() {
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Appium",
            "Cannot find search input",
            5);
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
            "Cannot find 'Appium' article in search results",
            5);
    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
    swipeUpToElement(
            By.xpath("//*[@text = 'View page in browser']"),
            "Cannot find the end of the article",
            20);
  }

  @Ignore
  @Test
  public void testSaveArticleToMyList() {
    String name_of_folder = "Learning Programming";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'");
    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
    waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Cannot find 'More options' button to open article options");
    waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Cannot find options to add article to reading list ");
    waitForElementAndClick(
            By.id("org.wikipedia:id/onboarding_button"),
            "Cannot 'GOT IT' tip overlay");
    waitForElementAndClear(
            By.id("org.wikipedia:id/text_input"),
            "Cannot find input to set name of article's folder");
    waitForElementAndSendKeys(
            By.id("org.wikipedia:id/text_input"),
            name_of_folder,
            "Cannot put text into article's folder input");
    waitForElementAndClick(
            By.xpath("//*[@text='OK']"),
            "Cannot press 'OK' button to save article's folder");
    waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Cannot find 'X' button to close article");
    waitForElementAndClick(
            By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
            "Cannot find navigation button 'My lists' to open 'My lists' folder");
    waitForElementAndClick(
            By.xpath("//*[@text='" + name_of_folder + "']"),
            "Cannot find created '" + name_of_folder + "' folder in 'My lists'");
    waitForElementPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot find article 'Java (programming language)' in '" + name_of_folder + "' folder");
    swipeElementToLeft(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot find article saved  article 'Java (programming language)'");
    waitForElementNotPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot delete saved article 'Java (programming language)'");
  }

  @Test

 /* 1. Сохраняет две статьи в одну папку
    2. Удаляет одну из статей
    3. Убеждается, что вторая осталась
    4. Переходит в неё и убеждается, что title совпадает*/

  public void testSavingTwoArticlesToOneReadingListAndDeletingOneOfThem() {
    String name_of_myList = "Learning Programming";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'");
    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
    waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Cannot find 'More options' button to open article options");
    //we wait here presents of all menus from context menu 'More options' android.widget.ListView;
    waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/title"),
            "Cannot find commands from context menu 'android.widget.ListView'",
            15);
    waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Cannot find options to add article to reading list ");
    waitForElementAndClick(
            By.id("org.wikipedia:id/onboarding_button"),
            "Cannot 'GOT IT' tip overlay");
    waitForElementAndClear(
            By.id("org.wikipedia:id/text_input"),
            "Cannot find input to set name of article's folder");
    waitForElementAndSendKeys(
            By.id("org.wikipedia:id/text_input"),
            name_of_myList,
            "Cannot put text into article's folder input");
    waitForElementAndClick(
            By.xpath("//*[@text='OK']"),
            "Cannot press 'OK' button to save article's folder");

    verifyThatAddedArticleToMyListMessageAppears(name_of_myList);

    // close first saved article
    waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Cannot find 'X' button to close article",
            15);
    //adding second article 'Wikimedia list article'
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Wikimedia list article']"),
            "Cannot click to the article 'Wikimedia list article'");
    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
    waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Cannot find 'More options' button to open article options");
    //we wait here presents of all menus from context menu 'More options' android.widget.ListView;
    waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/title"),
            "Cannot find commands from context menu 'android.widget.ListView'");
    //adding to already created MyList
    assertElementPresent(By.xpath("//*[@text='Add to reading list']"));
    waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Cannot find options to add article to reading list ");
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + name_of_myList + "']"),
            "Cannot find " + name_of_myList + " among the MyList folders");

    verifyThatAddedArticleToMyListMessageAppears(name_of_myList);

    waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Cannot find 'X' button to close article",
            15);
    //Finish adding second article
    waitForElementAndClick(
            By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
            "Cannot find navigation button 'My lists' to open 'My lists' folder");
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/item_container']//*[@text='Learning Programming']"),
            "Cannot find created '" + name_of_myList + "' folder in 'My lists'",
            15);
    waitForElementPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot find article 'Java (programming language)' in '" + name_of_myList + "' folder");
    swipeElementToLeft(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot find article saved  article 'Java (programming language)'");
    waitForElementNotPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot delete saved article 'Java (programming language)'");
    waitForElementPresent(
            By.xpath("//*[@text='Java version history']"),
            "Cannot find saved article 'Java version history'");
  }

  private void verifyThatAddedArticleToMyListMessageAppears(String name_of_myList) {

    //We verify, that article were added to the list:
    //1) appropriate message appears;
    //2) number of articles in the list increased to 1;

    //we verify that snackbar text is present and will be about adding article to the list: "Added to "+name_of_myList + ".""
/*
    waitForElementPresent(
            By.id("org.wikipedia:id/snackbar_text"),
            "There is no snackbar text after adding second article 'name of the article' to the folder");
*/
    if (assertElementPresent(By.id("org.wikipedia:id/snackbar_text"))) {
      String message_after_adding_first_article =
              waitForElementAndGetAttribute(
                      By.id("org.wikipedia:id/snackbar_text"),
                      "text",
                      "There is no snackbar text after adding second article 'name of the article' to the folder",
                      5);
      assertEquals(
              "Message after adding atricle should be 'Added to " + name_of_myList + ".'",
              message_after_adding_first_article,
              "Added to " + name_of_myList + ".");

      waitForElementNotPresent(
              By.id("org.wikipedia:id/snackbar_text"),
              "Snackbar text doesn't disappear.",
              15);
    }
  }

  @Ignore
  @Test
  public void testAmountOfNotEmptySearch() {
    String search_keyword = "Linkin Park discography";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            search_keyword,
            "Cannot find search input");
    String search_result_locator =
            "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
    waitForElementPresent(
            By.xpath(search_result_locator),
            "Cannot find anything by request '" + search_keyword + "'",
            15);
    int amount_of_searching_results = getAmountOfElements(By.xpath(search_result_locator));
    assertTrue(
            "We found too few results!",
            amount_of_searching_results > 0);
  }

  @Ignore
  @Test
  public void testAmountOfEmptySearch() {
    String search_keyword = "m23052";
    String search_result_locator =
            "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
    String empty_result_label = "//*[@text='No results found']";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            search_keyword,
            "Cannot find search input");
    waitForElementPresent(
            By.xpath(empty_result_label),
            "Cannot find empty result label by request '" + search_keyword + "'",
            15);
    isElementNotPresent(
            By.xpath(search_result_locator),
            "We've found some results by request '" + search_keyword + "'");
  }

  @Test
  public void testChangeScreenOrientationOnSearchResults() {
    String search_keyword = "Java";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language' tipc searching by " + search_keyword,
            15);
    String title_before_rotation = waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Cannot find title of article",
            15);
    driver.rotate(ScreenOrientation.LANDSCAPE);
    String title_after_rotation_to_LANDSCAPE_mode = waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Cannot find title of article",
            15);
    assertEquals(
            "Article's title has been changed: title after rotation doesn't equal to the title before rotation!",
            title_before_rotation,
            title_after_rotation_to_LANDSCAPE_mode);
    driver.rotate(ScreenOrientation.PORTRAIT);
    String title_after_second_rotation_to_PORTRAIT_mode = waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Cannot find title of article",
            15);
    assertEquals(
            "Article's title has been changed: title after 2nd rotation to Portrait mode doesn't equal to the title before rotation!",
            title_before_rotation,
            title_after_second_rotation_to_PORTRAIT_mode);
  }

  @Test
  public void testCheckSearchArticleInBackGround() {
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find 'Search Wikipedia' input");
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'");
    driver.runAppInBackground(10);
    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot find article 'Object-oriented programming language' after returning from background");
  }

  @Ignore
  @Test
  //проверяет наличие текста “Search…” в строке поиска перед вводом текста и помечает тест упавшим, если такого текста нет.
  public void testAssertTextInSearchField() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    WebElement input_element = waitForElementPresent(
            By.id("org.wikipedia:id/search_src_text"),
            "Cannot find search field",
            5);
    String text_input = input_element.getAttribute("text");
    assertFalse(
            "Search text field is empty",
            text_input.isEmpty());
    assertEquals(
            "We see unexpected search field text",
            "Search…",
            text_input);
  }

  @Ignore
  @Test
   /*
   Тест, который:
      Ищет какое-то слово
      Убеждается, что найдено несколько статей
      Отменяет поиск
      Убеждается, что результат поиска пропал
  */
  public void testSearchResults() {
    String keyword = "Grand Cayman";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            keyword,
            "Cannot find search input");
    //assert that we didn't get error of wiki app during the search;
    waitForElementNotPresent(
            By.id("org.wikipedia:id/view_wiki_error_text"),
            "Cannot implement the search '" + keyword + "'. An error of wiki app occured.");

    List<WebElement> search_results = waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find any searching results. List of searching results is empty.");

    assertTrue("No results were found for searching keyword(s): '" + keyword + "'. \n At least one result was expected.",
            (search_results.size() > 0));

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_close_btn"),
            "Cannot find 'X to cancel search");

    //assert that after clicking to 'X' remains only default search filed text: "Search…"
    assertEquals(
            "Text in the search field is not default",
            waitForElementPresent(By.id("org.wikipedia:id/search_src_text"), "Cannot find search field").getAttribute("text"),
            "Search…");

    //assert that there is no search result after clicking to 'X' in search field;
    assertTrue(waitForElementNotPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Can find searching results"));
  }

  @Ignore
  @Test
  public void testSearchResultsHaveKeyword1() {
    //All searching results will contain searching keyword: "Grand Cayman":
    testSearchResultsHaveKeyword("Grand Cayman");
  }

  @Ignore
  @Test
  public void testSearchResultsHaveKeyword2() {
    //At least one search result which doesn't include search keyword: "Zello":
    testSearchResultsHaveKeyword("Zello");
  }

  @Ignore
  @Test
  public void testSearchResultsHaveKeyword3() {
    //There is no any searching results with (nonsence) keyword: "m56743":
    testSearchResultsHaveKeyword("m56743");
  }


  /*
  Тест: проверка слов в поиске
      Ищет какое-то слово
      Убеждается, что в каждом результате поиска есть это слово.
   */
  private void testSearchResultsHaveKeyword(String keyword) {
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            keyword,
            "Cannot find search input");

    List<WebElement> search_results = waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find any search results");
    for (WebElement search_result : search_results) {
      assertTrue(isSearchResultsHaveKeyword(
              search_result,
              keyword,
              "Cannot find searching keyword(s) '" + keyword + "' in the article"));
    }
  }

  private boolean isSearchResultsHaveKeyword(WebElement page_list_item, String keyword, String error_message) {
    Boolean result = true;
    //each page_list_item has several textView fields with class "android.widget.TextView";
    //but at first we should confirm the presents of all webElements with locator By.className("android.widget.TextView") on the screen;
    waitListOfAllElementsPresent(
            By.className("android.widget.TextView"),
            "Cannot find title and/or description and/or redirect info of the article");
    List<WebElement> textView_fields = page_list_item.findElements(By.className("android.widget.TextView"));

    //we counting how many times keyword includes in each textView_fields in each search result page_list_item;
    if (textView_fields.stream().filter(e -> e.getAttribute("text").contains(keyword)).count() == 0) {
      result = false;
      waitAttributeContains(textView_fields.get(0),
              keyword,
              error_message + getArticleText(textView_fields));
    }
    return result;
  }

  private String getArticleText(List<WebElement> list) {
    String textOfArticle = "";
    for (WebElement aList : list) {
      textOfArticle = textOfArticle + "\n" + aList.getAttribute("text");
    }
    return textOfArticle;
  }

  private boolean waitForElementNotPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(invisibilityOfElementLocated(by));
  }

  private boolean waitForElementNotPresent(By by, String error_message) {
    return waitForElementNotPresent(by, error_message, 5);
  }

  private WebElement waitForElementPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(presenceOfElementLocated(by));
  }

  private WebElement waitForElementPresent(By by, String error_message) {
    return waitForElementPresent(by, error_message, 5);
  }

  private List<WebElement> waitListOfAllElementsPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(presenceOfAllElementsLocatedBy(by));
  }

  private List<WebElement> waitListOfAllElementsPresent(By by, String error_message) {
    return waitListOfAllElementsPresent(by, error_message, 5);
  }

  private Boolean waitAttributeContains(WebElement webElement, String attribute, String keyword, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(attributeContains(webElement, attribute, keyword));
  }

  private Boolean waitAttributeContains(WebElement webElement, String keyword, String error_message) {
    return waitAttributeContains(webElement, "text", keyword, error_message, 5);
  }

  private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
    element.click();
    return element;
  }

  private WebElement waitForElementAndClick(By by, String error_message) {
    return waitForElementAndClick(by, error_message, 5);
  }

  private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
    element.sendKeys(value);
    return element;
  }

  private WebElement waitForElementAndSendKeys(By by, String value, String error_message) {
    return waitForElementAndSendKeys(by, value, error_message, 5);
  }

  private WebElement waitForElementAndClear(By by, String error_message, long timeInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
    element.clear();
    return element;
  }

  private WebElement waitForElementAndClear(By by, String error_message) {
    return waitForElementAndClear(by, error_message, 5);
  }

  protected void swipeUp(int timeOfSwipe) {
    TouchAction action = new TouchAction(driver);
    Dimension size = driver.manage().window().getSize();
    int x = size.width / 2;
    int start_y = (int) (size.height * 0.8);
    int end_y = (int) (size.height * 0.2);
    action
            .press(x, start_y)
            .waitAction(timeOfSwipe)
            .moveTo(x, end_y)
            .release()
            .perform();
  }

  protected void swipeUpQuick() {
    swipeUp(200);
  }

  protected void swipeUpToElement(By by, String error_message, int maxSwipes) {
    int already_swiped = 0;
    while (driver.findElements(by).size() == 0) {
      if (already_swiped > maxSwipes) {
        waitForElementPresent(by, "Cannot find element by swiping Up. \n " + error_message);
        return;
      }
      swipeUpQuick();
      ++already_swiped;
    }

  }

  protected void swipeElementToLeft(By by, String error_message) {
    WebElement element = waitForElementPresent(
            by,
            error_message,
            10);
    int left_x = element.getLocation().getX();
    int right_x = left_x + element.getSize().getWidth();
    int upper_y = element.getLocation().getY();
    int lower_y = upper_y + element.getSize().getHeight();
    int middle_y = (upper_y + lower_y) / 2;
    TouchAction action = new TouchAction(driver);
    action
            .press(right_x, middle_y)
            .waitAction(300)
            .moveTo(left_x, middle_y)
            .release()
            .perform();
  }

  private int getAmountOfElements(By by) {
    List elements = driver.findElements(by);
    return elements.size();
  }

  private void isElementNotPresent(By by, String error_message) {
    int amount_of_elements = getAmountOfElements(by);
    if (amount_of_elements > 0) {
      String default_message =
              "An element '" + by.toString() + "' supposed to be not present.";
      throw new AssertionError(default_message + " " + error_message);
    }
  }

  private void isElementPresent(By by, String error_message) {
    int amount_of_elements = getAmountOfElements(by);
    if (amount_of_elements > 0) {
      String default_message =
              "An element '" + by.toString() + "' supposed to be present.";
      throw new AssertionError(default_message + " " + error_message);
    }
  }

  public boolean assertElementPresent(By locator) {
    try {
      driver.findElement(locator);
      return true;
    } catch (NoSuchElementException ex) {
      return false;
    }
  }

  private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
    return element.getAttribute(attribute);
  }

  private List<String> waitForElementsAndGetAttributes(By by, String attribute, String error_message, long timeoutInSeconds) {
    List<WebElement> elements = waitListOfAllElementsPresent(by, error_message, timeoutInSeconds);
    return elements.stream().map(e -> e.getAttribute(attribute)).collect(Collectors.toList());
  }
}


