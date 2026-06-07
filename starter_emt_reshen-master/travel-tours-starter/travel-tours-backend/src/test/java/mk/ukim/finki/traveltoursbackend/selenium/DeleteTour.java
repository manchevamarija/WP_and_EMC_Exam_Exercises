package mk.ukim.finki.traveltoursbackend.selenium;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class DeleteTour extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";
    private static final String SUBMIT_BUTTON_SELECTOR = ".submit-btn";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    @FindBy(css = SUBMIT_BUTTON_SELECTOR)
    private WebElement submitButton;

    public DeleteTour(WebDriver driver) {
        super(driver);
    }

    public static DeleteTour delete(WebDriver driver, Tour tour, int expectedCount) {
        get(driver, "/tours");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(DeleteTour::isPageFullyLoaded);

        String deleteButtonSelector = String.format(".card[data-id='%s'] .delete-item", tour.getId());
        WebElement deleteButton = driver.findElement(By.cssSelector(deleteButtonSelector));

        deleteButton.click();

        DeleteTour deleteTour = PageFactory.initElements(driver, DeleteTour.class);
        wait.until(DeleteTour::isFormFullyLoaded);

        deleteTour.submitButton.click();

        wait.until(d -> {
            DeleteTour page = PageFactory.initElements(driver, DeleteTour.class);
            return page.cards.size() == expectedCount;
        });

        deleteTour = PageFactory.initElements(driver, DeleteTour.class);
        deleteTour.assertItems(expectedCount);

        return deleteTour;
    }

    private static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            CARD_SELECTOR
        );
    }

    private static boolean isFormFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            SUBMIT_BUTTON_SELECTOR
        );
    }

    public void assertItems(int expectedItemsNumber) {
        ExamAssert.assertEquals("The number of items does not match.", expectedItemsNumber, getCards().size());
    }
}
