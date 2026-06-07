package mk.ukim.finki.onlinecoursesbackend.selenium;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class EnrollmentsPage extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    public EnrollmentsPage(WebDriver driver) {
        super(driver);
    }

    public static void unenroll(WebDriver driver, Enrollment enrollment, int expectedCount) {
        get(driver, "/enrollments");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(EnrollmentsPage::isPageFullyLoaded);

        String unenrollButtonSelector = String.format(".card[data-id='%s'] .unenroll-item", enrollment.getId());
        WebElement unenrollButton = driver.findElement(By.cssSelector(unenrollButtonSelector));

        unenrollButton.click();

        wait.until(d -> {
            try {
                EnrollmentsPage page = PageFactory.initElements(driver, EnrollmentsPage.class);
                return page.cards.size() == expectedCount;
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        EnrollmentsPage enrollmentsPage = PageFactory.initElements(driver, EnrollmentsPage.class);
        ExamAssert.assertEquals("The number of enrollments does not match.", expectedCount,
            enrollmentsPage.cards.size());
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            CARD_SELECTOR
        );
    }
}
