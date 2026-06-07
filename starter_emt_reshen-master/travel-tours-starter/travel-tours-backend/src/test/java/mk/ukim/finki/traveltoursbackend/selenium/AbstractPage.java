package mk.ukim.finki.traveltoursbackend.selenium;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AbstractPage {
    protected WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static void get(WebDriver driver, String relativeUrl) {
        String url = "http://localhost:3000" + relativeUrl;
        driver.get(url);
    }

    public static void assertRelativeUrl(WebDriver driver, String relativeUrl) {
        String url = "http://localhost:3000" + relativeUrl;
        String currentUrl = driver.getCurrentUrl();
        ExamAssert.assertUrlEquals("Requesting " + relativeUrl, url, currentUrl);
    }

    public static boolean areElementsPresent(WebDriver driver, String... selectors) {
        for (String selector : selectors) {
            List<WebElement> elements = driver.findElements(By.cssSelector(selector));
            if (elements.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
