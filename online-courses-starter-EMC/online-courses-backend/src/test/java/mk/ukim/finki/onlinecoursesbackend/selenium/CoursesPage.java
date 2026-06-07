package mk.ukim.finki.onlinecoursesbackend.selenium;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class CoursesPage extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";
    private static final String ALL_BUTTON_SELECTOR = ".all-courses";
    private static final String AVAILABLE_BUTTON_SELECTOR = ".available-courses";
    private static final String UNAVAILABLE_BUTTON_SELECTOR = ".unavailable-courses";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    @FindBy(css = ALL_BUTTON_SELECTOR)
    private WebElement allButton;

    @FindBy(css = AVAILABLE_BUTTON_SELECTOR)
    private WebElement availableButton;

    @FindBy(css = UNAVAILABLE_BUTTON_SELECTOR)
    private WebElement unavailableButton;

    public CoursesPage(WebDriver driver) {
        super(driver);
    }

    public static void filter(WebDriver driver) {
        get(driver, "/courses");

        CoursesPage coursesPage = PageFactory.initElements(driver, CoursesPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(CoursesPage::isPageFullyLoaded);

        coursesPage.availableButton.click();

        wait.until(d -> {
            CoursesPage page = PageFactory.initElements(driver, CoursesPage.class);
            return page.cards.size() == 2;
        });
        ExamAssert.assertEquals("The number of available courses does not match.", 2, coursesPage.cards.size());

        coursesPage.unavailableButton.click();

        wait.until(d -> {
            CoursesPage page = PageFactory.initElements(driver, CoursesPage.class);
            return page.cards.size() == 1;
        });
        coursesPage = PageFactory.initElements(driver, CoursesPage.class);
        ExamAssert.assertEquals("The number of unavailable courses does not match.", 1, coursesPage.cards.size());

        coursesPage.allButton.click();

        wait.until(d -> {
            CoursesPage page = PageFactory.initElements(driver, CoursesPage.class);
            return page.cards.size() == 3;
        });
        coursesPage = PageFactory.initElements(driver, CoursesPage.class);
        ExamAssert.assertEquals("The number of all courses does not match.", 3, coursesPage.cards.size());
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
