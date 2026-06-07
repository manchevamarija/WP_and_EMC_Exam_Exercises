package mk.ukim.finki.traveltoursbackend.selenium;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class ReservationsPage extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    public ReservationsPage(WebDriver driver) {
        super(driver);
    }

    public static void cancel(WebDriver driver, Reservation reservation, int expectedCount) {
        get(driver, "/reservations");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ReservationsPage::isPageFullyLoaded);

        String cancelButtonSelector = String.format(".card[data-id='%s'] .cancel-item", reservation.getId());
        WebElement cancelButton = driver.findElement(By.cssSelector(cancelButtonSelector));

        cancelButton.click();

        wait.until(d -> {
            try {
                ReservationsPage page = PageFactory.initElements(driver, ReservationsPage.class);
                return page.cards.size() == expectedCount;
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        ReservationsPage reservationsPage = PageFactory.initElements(driver, ReservationsPage.class);
        ExamAssert.assertEquals("The number of reservations does not match.", expectedCount,
            reservationsPage.cards.size());
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            CARD_SELECTOR
        );
    }
}
