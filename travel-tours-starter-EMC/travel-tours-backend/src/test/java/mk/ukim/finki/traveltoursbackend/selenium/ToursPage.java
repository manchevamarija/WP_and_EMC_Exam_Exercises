package mk.ukim.finki.traveltoursbackend.selenium;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class ToursPage extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";
    private static final String ALL_BUTTON_SELECTOR = ".all-tours";
    private static final String AVAILABLE_BUTTON_SELECTOR = ".available-tours";
    private static final String UNAVAILABLE_BUTTON_SELECTOR = ".unavailable-tours";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    @FindBy(css = ALL_BUTTON_SELECTOR)
    private WebElement allButton;

    @FindBy(css = AVAILABLE_BUTTON_SELECTOR)
    private WebElement availableButton;

    @FindBy(css = UNAVAILABLE_BUTTON_SELECTOR)
    private WebElement unavailableButton;

    public ToursPage(WebDriver driver) {
        super(driver);
    }

    public static void filter(WebDriver driver) {
        get(driver, "/tours");

        ToursPage toursPage = PageFactory.initElements(driver, ToursPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ToursPage::isPageFullyLoaded);

        toursPage.availableButton.click();

        wait.until(d -> {
            ToursPage page = PageFactory.initElements(driver, ToursPage.class);
            return page.cards.size() == 2;
        });
        ExamAssert.assertEquals("The number of available tours does not match.", 2, toursPage.cards.size());

        toursPage.unavailableButton.click();

        wait.until(d -> {
            ToursPage page = PageFactory.initElements(driver, ToursPage.class);
            return page.cards.size() == 1;
        });
        toursPage = PageFactory.initElements(driver, ToursPage.class);
        ExamAssert.assertEquals("The number of unavailable tours does not match.", 1, toursPage.cards.size());

        toursPage.allButton.click();

        wait.until(d -> {
            ToursPage page = PageFactory.initElements(driver, ToursPage.class);
            return page.cards.size() == 3;
        });
        toursPage = PageFactory.initElements(driver, ToursPage.class);
        ExamAssert.assertEquals("The number of all tours does not match.", 3, toursPage.cards.size());
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            CARD_SELECTOR,
            ALL_BUTTON_SELECTOR,
            AVAILABLE_BUTTON_SELECTOR,
            UNAVAILABLE_BUTTON_SELECTOR
        );
    }
}
