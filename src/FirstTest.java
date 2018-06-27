import io.appium.java_client.TouchAction;
import lib.CoreTestCase;
import lib.ui.MainPageObject;
import lib.ui.SearchPageObject;
import org.junit.Test;
import org.openqa.selenium.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by irinagavrilova on 5/17/18.
 */
public class FirstTest extends CoreTestCase {
  private MainPageObject mainPageObject;
  private SearchPageObject searchPageObject;

  protected void setUp() throws Exception {
    super.setUp();
    mainPageObject = new MainPageObject(driver);
    searchPageObject = new SearchPageObject(driver);
  }


  @Test
  public void testSearch() {
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForSearchResult("Object-oriented programming language");

  }


  @Test
  public void testCancelSearch() {
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clearSearchTextField();
    searchPageObject.clickCancelSearch();
    searchPageObject.waitForCancelButtonToDisappear();
  }


  @Test
  public void testCompareArticleTitle() {
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input",
            5);
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'",
            5);
    WebElement title_element = mainPageObject.waitForElementPresent(
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
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Appium",
            "Cannot find search input",
            5);
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
            "Cannot find 'Appium' article in search results",
            5);
    mainPageObject.waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
    mainPageObject.swipeUpToElement(
            By.xpath("//*[@text = 'View page in browser']"),
            "Cannot find the end of the article",
            20);
  }


  @Test
  public void testSaveArticleToMyList() {
    String name_of_folder = "Learning Programming";
    openAnArticle();
    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Cannot find 'More options' button to open article options");
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Cannot find options to add article to reading list ");
    creatingFirstReadingList(name_of_folder);
    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Cannot find 'X' button to close article");
    goToMyListPage();
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@text='" + name_of_folder + "']"),
            "Cannot find created '" + name_of_folder + "' folder in 'My lists'");
    mainPageObject.waitForElementPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot find article 'Java (programming language)' in '" + name_of_folder + "' folder");
    mainPageObject.swipeElementToLeft(
            By.xpath("//*[@text='Java (programming language)']"),
            "Cannot find article saved  article 'Java (programming language)'");
    mainPageObject.waitForElementNotPresent(
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
    String search_keyword = "Java programming";
    int counter_article = 0;
    List<String> articleTitles = getArticlesTitles(search_keyword);
    openAnArticle(search_keyword, articleTitles.get(0));
    Point location_Close_button = getLocationXbutton();
    choosingMoreOptionsAddToReadingListCommand();
    creatingFirstReadingList(name_of_myList);
    closeAnArticle(location_Close_button);
    goToMyListPage();
    clickToReadingList(name_of_myList);
    List<String> titles_after_saving_First_article = checkNumbersOfArticles(name_of_myList, counter_article);
    checkTitleFirstSavedArticle(articleTitles, titles_after_saving_First_article);
    goBack();
    goToExplorePage();
    openAnArticle(search_keyword, articleTitles.get(1));
    addArticleToReadingListUsingActionBar(name_of_myList);
    closeAnArticle(location_Close_button);
    goToMyListPage();
    clickToReadingList(name_of_myList);
    checkTitleSecondSavedArticle(name_of_myList, articleTitles);
    checkNumbersOfArticles(name_of_myList, titles_after_saving_First_article.size());
    deleteFirstArticle(articleTitles);
  }


  @Test
  /* открывает статью и убеждается, что у нее есть элемент title. Важно: тест не должен дожидаться появления title,
  проверка должна производиться сразу. Если title не найден - тест падает с ошибкой. Метод можно назвать
  assertElementPresent.
  */
  public void testTitleAssertion() {
    String searchKeyword = "Zello";
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchKeyword,
            "Cannot find search input");
    List<String> search_results =
            mainPageObject.waitForElementsAndGetAttributes(
                    By.id("org.wikipedia:id/page_list_item_title"),
                    "text",
                    "Cannot find any search results for '" + searchKeyword + "'.",
                    15);
    assertTrue(
            "Unfortunately, we didn't get enough search results by: '" + searchKeyword + "'",
            (search_results.size() > 0));
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + search_results.get(0) + "']"),
            "Cannot click to the article '" + search_results.get(0) + "'");
    mainPageObject.waitForElementNotPresent(
            By.id("org.wikipedia:id/single_fragment_toolbar_wordmark"),
            "Cannot open article page. Still on Explore page with Wikipedia logo.");
    mainPageObject.waitForElementNotPresent(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot open article page. Still on Search page.");
    assertElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title.");
  }

  private void isElementPresent(By by, String error_message) {
    int amount_of_elements = mainPageObject.getAmountOfElements(by);
    if (amount_of_elements == 0) {
      String default_message =
              "An element '" + by.toString() + "' supposed to be present.";
      throw new AssertionError(error_message + default_message);
    }
  }

  private void assertElementPresent(By locator, String error_message) {
    try {
      driver.findElement(locator);
    } catch (NoSuchElementException exc) {
      throw new NoSuchElementException(error_message + " Element is not present. Cannot find element by this locator: " + locator);
    }
  }

  private Point getLocationXbutton() {
    return mainPageObject.waitForElementPresent(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Cannot fins Close 'X' button").getLocation();
  }

  private void checkTitleFirstSavedArticle(List<String> articleTitles, List<String> titles_after_saving_First_article) {
    assertEquals(
            "Title of the article: '" + titles_after_saving_First_article.get(0) + "' is not as supposed to be.",
            titles_after_saving_First_article.get(0),
            articleTitles.get(0));
  }

  private void checkTitleSecondSavedArticle(String name_of_myList, List<String> articleTitles) {
    mainPageObject.waitForElementPresent(
            By.xpath("//*[@text='" + articleTitles.get(1) + "']"),
            "Cannot find article '" + articleTitles.get(1) + "' in '" + name_of_myList + "' folder");
  }

  private void clickToReadingList(String name_of_myList) {
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/item_container']//*[@text='" + name_of_myList + "']"),
            "Cannot find created reading list: '" + name_of_myList + "'  in 'My lists'",
            15);
    mainPageObject.waitForElementNotPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/single_fragment_toolbar']//*[@text='My lists']"),
            "Cannot open reading list '" + name_of_myList + "'  in 'My lists'",
            10);
  }

  private void goToExplorePage() {
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/fragment_main_nav_tab_layout']//*[@content-desc='Explore']"),
            "Cannot locate and click to the 'Explore' icon on low tab.");
  }

  private void goBack() {
    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Cannot tap arrow back button");
  }

  private void deleteFirstArticle(List<String> articleTitles) {
    mainPageObject.swipeElementToLeft(
            By.xpath("//*[@text='" + articleTitles.get(0) + "']"),
            "Cannot find article saved  article '" + articleTitles.get(0) + "'");
    mainPageObject.waitForElementNotPresent(
            By.xpath("//*[@text='" + articleTitles.get(0) + "']"),
            "Cannot delete saved article '" + articleTitles.get(0) + "'");
    mainPageObject.waitForElementPresent(
            By.xpath("//*[@text='" + articleTitles.get(1) + "']"),
            "Cannot find saved article '" + articleTitles.get(1) + "'");
  }

  private List<String> checkNumbersOfArticles(String name_of_myList, int counter) {
    List<String> titles = mainPageObject.waitForElementsAndGetAttributes(
            By.xpath("//*[@resource-id='org.wikipedia:id/reading_list_contents']//*[@resource-id='org.wikipedia:id/page_list_item_title']"),
            "text",
            "Cannot find any saved articles in the reading list " + name_of_myList + ".",
            15);
    assertTrue(
            "Quantity of saved article should be " + counter + 1 + ", instead of " + titles.size() + ".",
            titles.size() == (counter + 1));
    return titles;
  }

  private void addArticleToReadingListUsingActionBar(String name_of_myList) {
    mainPageObject.waitListOfAllElementsPresent(
            By.className("android.support.v7.app.ActionBar$Tab"),
            "Cannot locate button on Action Tab").get(0).click();
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/item_container']//*[@text='" + name_of_myList + "']"),
            "Cannot find " + name_of_myList + " list in the Reading Lists.");
  }

  private void goToMyListPage() {
    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
            "Cannot find navigation button 'My lists' to open 'My lists' folder");
  }

  private void closeAnArticle(Point location) {
    if (mainPageObject.assertionElementPresent(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"))) {
      mainPageObject.waitForElementAndClick(
              By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
              "Cannot find 'X' button to close article",
              15);
    } else {
      clickToButton(location);
    }
    mainPageObject.waitForElementNotPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot close an article. The title is still here.");
  }

  private void choosingMoreOptionsAddToReadingListCommand() {
    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Cannot find 'More options' button to open article options");
    //we wait here presents of all menus from context menu 'More options' android.widget.ListView;
    mainPageObject.waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/title"),
            "Cannot find commands from context menu 'android.widget.ListView'",
            15);
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Cannot find options to add article to reading list ");
  }

  private void openAnArticle() {
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'");
    mainPageObject.waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find title for article",
            15);
  }

  private List<String> getArticlesTitles(String searchKeyword) {
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchKeyword,
            "Cannot find search input");
    List<String> search_results =
            mainPageObject.waitForElementsAndGetAttributes(
                    By.id("org.wikipedia:id/page_list_item_title"),
                    "text",
                    "Cannot find any search results for '" + searchKeyword + "'.",
                    15);
    assertTrue(
            "Unfortunately, we didn't get enough search results by: '" + searchKeyword + "'",
            (search_results.size() > 1));
    String article1 = search_results.get((int) (Math.random() * search_results.size()));
    search_results.remove(article1);
    String article2 = search_results.get((int) (Math.random() * search_results.size()));
    return Arrays.asList(article1, article2);
  }

  private void openAnArticle(String searchKeyword, String articleName) {
    if (mainPageObject.assertionElementPresent(By.id("org.wikipedia:id/single_fragment_toolbar_wordmark"))
            && mainPageObject.assertionElementPresent(By.id("org.wikipedia:id/search_container"))) {
      mainPageObject.waitForElementAndClick(
              By.xpath("//*[contains(@text,'Search Wikipedia')]"),
              "Cannot find Search Wikipedia input");
      mainPageObject.waitForElementAndSendKeys(
              By.xpath("//*[contains(@text,'Search…')]"),
              searchKeyword,
              "Cannot find search input");
      mainPageObject.waitForElementAndClick(
              By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleName + "']"),
              "Cannot click to the article '" + articleName + "'");
      mainPageObject.waitForElementPresent(
              By.id("org.wikipedia:id/view_page_title_text"),
              "Cannot find title for article",
              15);
    } else {
      mainPageObject.waitForElementAndClick(
              By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleName + "']"),
              "Cannot click to the article '" + articleName + "'");
      mainPageObject.waitForElementPresent(
              By.id("org.wikipedia:id/view_page_title_text"),
              "Cannot find title for article",
              15);

    }
  }

  private void creatingFirstReadingList(String name_of_myList) {
    mainPageObject.waitForElementAndClick(
            By.id("org.wikipedia:id/onboarding_button"),
            "Cannot 'GOT IT' tip overlay");
    mainPageObject.waitForElementAndClear(
            By.id("org.wikipedia:id/text_input"),
            "Cannot find input to set name of article's folder");
    mainPageObject.waitForElementAndSendKeys(
            By.id("org.wikipedia:id/text_input"),
            name_of_myList,
            "Cannot put text into article's folder input");
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@text='OK']"),
            "Cannot press 'OK' button to save article's folder");
  }

  private void clickToButton(Point button_location_Add_this_article_to_reading_list) {
    TouchAction action = new TouchAction(driver);
    action
            .press(
                    button_location_Add_this_article_to_reading_list.getX(),
                    button_location_Add_this_article_to_reading_list.getY())
            .release()
            .perform();
  }


  @Test
  public void testAmountOfNotEmptySearch() {
    String search_keyword = "Linkin Park discography";
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            search_keyword,
            "Cannot find search input");
    String search_result_locator =
            "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
    mainPageObject.waitForElementPresent(
            By.xpath(search_result_locator),
            "Cannot find anything by request '" + search_keyword + "'",
            15);
    int amount_of_searching_results = mainPageObject.getAmountOfElements(By.xpath(search_result_locator));
    assertTrue(
            "We found too few results!",
            amount_of_searching_results > 0);
  }


  @Test
  public void testAmountOfEmptySearch() {
    String search_keyword = "m23052";
    String search_result_locator =
            "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
    String empty_result_label = "//*[@text='No results found']";
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            search_keyword,
            "Cannot find search input");
    mainPageObject.waitForElementPresent(
            By.xpath(empty_result_label),
            "Cannot find empty result label by request '" + search_keyword + "'",
            15);
    mainPageObject.isElementNotPresent(
            By.xpath(search_result_locator),
            "We've found some results by request '" + search_keyword + "'");
  }

  @Test
  public void testChangeScreenOrientationOnSearchResults() {
    String search_keyword = "Java";
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language' tipc searching by " + search_keyword,
            15);
    String title_before_rotation = mainPageObject.waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Cannot find title of article",
            15);
    driver.rotate(ScreenOrientation.LANDSCAPE);
    String title_after_rotation_to_LANDSCAPE_mode = mainPageObject.waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Cannot find title of article",
            15);
    assertEquals(
            "Article's title has been changed: title after rotation doesn't equal to the title before rotation!",
            title_before_rotation,
            title_after_rotation_to_LANDSCAPE_mode);
    driver.rotate(ScreenOrientation.PORTRAIT);
    String title_after_second_rotation_to_PORTRAIT_mode = mainPageObject.waitForElementAndGetAttribute(
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
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find 'Search Wikipedia' input");
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find search input");
    mainPageObject.waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot click to the article 'Object-oriented programming language'");
    driver.runAppInBackground(10);
    mainPageObject.waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot find article 'Object-oriented programming language' after returning from background");
  }


  @Test
  //проверяет наличие текста “Search…” в строке поиска перед вводом текста и помечает тест упавшим, если такого текста нет.
  public void testAssertTextInSearchField() {

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input",
            5);
    WebElement input_element = mainPageObject.waitForElementPresent(
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
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");

    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            keyword,
            "Cannot find search input");
    //assert that we didn't get error of wiki app during the search;
    mainPageObject.waitForElementNotPresent(
            By.id("org.wikipedia:id/view_wiki_error_text"),
            "Cannot implement the search '" + keyword + "'. An error of wiki app occured.");

    List<WebElement> search_results = mainPageObject.waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find any searching results. List of searching results is empty.");

    assertTrue("No results were found for searching keyword(s): '" + keyword + "'. \n At least one result was expected.",
            (search_results.size() > 0));

    mainPageObject.waitForElementAndClick(
            By.id("org.wikipedia:id/search_close_btn"),
            "Cannot find 'X to cancel search");

    //assert that after clicking to 'X' remains only default search filed text: "Search…"
    assertEquals(
            "Text in the search field is not default",
            mainPageObject.waitForElementPresent(By.id("org.wikipedia:id/search_src_text"), "Cannot find search field").getAttribute("text"),
            "Search…");

    //assert that there is no search result after clicking to 'X' in search field;
    assertTrue(mainPageObject.waitForElementNotPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Can find searching results"));
  }


  @Test
  public void SearchResultsHaveKeyword1() {
    //All searching results will contain searching keyword: "Grand Cayman":
    testSearchResultsHaveKeyword("Grand Cayman");
  }


  @Test
  public void SearchResultsHaveKeyword2() {
    //At least one search result which doesn't include search keyword: "Zello":
    testSearchResultsHaveKeyword("Zello");
  }


  @Test
  public void SearchResultsHaveKeyword3() {
    //There is no any searching results with (nonsence) keyword: "m56743":
    testSearchResultsHaveKeyword("m56743");
  }


  /*
  Тест: проверка слов в поиске
      Ищет какое-то слово
      Убеждается, что в каждом результате поиска есть это слово.
   */
  private void testSearchResultsHaveKeyword(String keyword) {
    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia input");

    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            keyword,
            "Cannot find search input");

    List<WebElement> search_results = mainPageObject.waitListOfAllElementsPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find any search results");
    for (WebElement search_result : search_results) {
      assertTrue(mainPageObject.isSearchResultsHaveKeyword(
              search_result,
              keyword,
              "Cannot find searching keyword(s) '" + keyword + "' in the article"));
    }
  }

}


