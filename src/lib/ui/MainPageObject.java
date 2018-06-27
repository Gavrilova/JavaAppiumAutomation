package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * Created by irinagavrilova on 6/27/18.
 */
public class MainPageObject {
  protected AppiumDriver driver;
  public MainPageObject(AppiumDriver driver) {this.driver = driver;}
  public boolean isSearchResultsHaveKeyword(WebElement page_list_item, String keyword, String error_message) {
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

  public String getArticleText(List<WebElement> list) {
    String textOfArticle = "";
    for (WebElement aList : list) {
      textOfArticle = textOfArticle + "\n" + aList.getAttribute("text");
    }
    return textOfArticle;
  }

  public boolean waitForElementNotPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(invisibilityOfElementLocated(by));
  }

  public boolean waitForElementNotPresent(By by, String error_message) {
    return waitForElementNotPresent(by, error_message, 5);
  }

  public WebElement waitForElementPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(presenceOfElementLocated(by));
  }

  public WebElement waitForElementPresent(By by, String error_message) {
    return waitForElementPresent(by, error_message, 5);
  }

  public List<WebElement> waitListOfAllElementsPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(presenceOfAllElementsLocatedBy(by));
  }

  public List<WebElement> waitListOfAllElementsPresent(By by, String error_message) {
    return waitListOfAllElementsPresent(by, error_message, 5);
  }

  public Boolean waitAttributeContains(WebElement webElement, String attribute, String keyword, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(attributeContains(webElement, attribute, keyword));
  }

  public Boolean waitAttributeContains(WebElement webElement, String keyword, String error_message) {
    return waitAttributeContains(webElement, "text", keyword, error_message, 5);
  }

  public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
    element.click();
    return element;
  }

  public WebElement waitForElementAndClick(By by, String error_message) {
    return waitForElementAndClick(by, error_message, 5);
  }

  public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
    element.sendKeys(value);
    return element;
  }

  public WebElement waitForElementAndSendKeys(By by, String value, String error_message) {
    return waitForElementAndSendKeys(by, value, error_message, 5);
  }

  public WebElement waitForElementAndClear(By by, String error_message, long timeInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
    element.clear();
    return element;
  }

  public WebElement waitForElementAndClear(By by, String error_message) {
    return waitForElementAndClear(by, error_message, 5);
  }

  public void swipeUp(int timeOfSwipe) {
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

  public void swipeUpQuick() {
    swipeUp(200);
  }

  public void swipeUpToElement(By by, String error_message, int maxSwipes) {
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

  public void swipeElementToLeft(By by, String error_message) {
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

  public int getAmountOfElements(By by) {
    List elements = driver.findElements(by);
    return elements.size();
  }

  public void isElementNotPresent(By by, String error_message) {
    int amount_of_elements = getAmountOfElements(by);
    if (amount_of_elements > 0) {
      String default_message =
              "An element '" + by.toString() + "' supposed to be not present.";
      throw new AssertionError(default_message + " " + error_message);
    }
  }

  public boolean assertionElementPresent(By locator) {
    String error_message = "Element is not present. Cannot find element by this locator: " + locator;
    try {
      driver.findElement(locator);
      return true;
    } catch (NoSuchElementException exc) {
      driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      System.out.println(error_message);
      return false;
    }
  }

  public boolean assertElementsPresent(By locator) {
    return driver.findElements(locator).size() > 0;
  }

  public String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
    return element.getAttribute(attribute);
  }

  public List<String> waitForElementsAndGetAttributes(By by, String attribute, String error_message, long timeoutInSeconds) {
    List<WebElement> elements = waitListOfAllElementsPresent(by, error_message, timeoutInSeconds);
    return elements.stream().map(e -> e.getAttribute(attribute)).collect(Collectors.toList());
  }
}
