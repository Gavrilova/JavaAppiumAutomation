import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
  public void FirstTest() {
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
    Assert.assertFalse(text_input.isEmpty());
    assertEquals("We see unexpected search field text",
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

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Grand Cayman",
            "Cannot find search input");

    List<WebElement> search_results = waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find any results");

    assertTrue(search_results.size() > 0);

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
  /*
  Тест: проверка слов в поиске
      Ищет какое-то слово
      Убеждается, что в каждом результате поиска есть это слово.
   */
  public void testSearchResultsHaveKeyword() {
    String keyword = "Grand Cayman";
    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            keyword,
            "Cannot find search input");

    List<WebElement> search_results = waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find any results");
    search_results.forEach(e -> assertTrue(isSearchResultsHaveKeyword(e, keyword)));
  }

  private boolean isSearchResultsHaveKeyword(WebElement page_list_item, String keyword) {
    Boolean result = true;
    //each page_list_item has several textView fields with class "android.widget.TextView";
    List<WebElement> textView_fields = page_list_item.findElements(By.className("android.widget.TextView"));
    //we counting how many times keyword includes in each textView_fields in each search result page_list_item;
    if (textView_fields.stream().filter(e -> e.getAttribute("text").contains(keyword)).count() == 0) {
      result = false;
      System.out.println("\"" + keyword + "\"" + " didn't found in the search result:");
      textView_fields.stream()
              .forEach(e -> System.out.println(e.getAttribute("text")));
    }
    return result;
  }

  private void printPageListItem(WebElement page_list_item) {
    List<WebElement> textView_fields = page_list_item.findElements(By.className("android.widget.TextView"));
    textView_fields.stream()
            .forEach(e -> System.out.println(e.getAttribute("text")));
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

  protected void swipeUp(int timeOfSwipe){
    TouchAction action = new TouchAction(driver);
    Dimension size = driver.manage().window().getSize();
    int x = size.width/2;
    int start_y = (int)(size.height*0.8);
    int end_y = (int)(size.height*0.2);
    action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
  }

  protected void swipeUpQuick(){
     swipeUp(200);
  }

  protected void swipeUpToElement(By by, String error_message, int maxSwipes){
    int already_swiped=0;
    while (driver.findElements(by).size()==0) {
      if (already_swiped > maxSwipes) {
        waitForElementPresent(by, "Cannot find element by swiping Up. \n " + error_message);
        return;
      }
      swipeUpQuick();
      ++ already_swiped;
    }

  }
}


