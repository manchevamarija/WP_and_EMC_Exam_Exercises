package mk.ukim.finki.onlinecoursesbackend.selenium;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class DeleteCourse extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";
    private static final String SUBMIT_BUTTON_SELECTOR = ".submit-btn";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    @FindBy(css = SUBMIT_BUTTON_SELECTOR)
    private WebElement submitButton;

    public DeleteCourse(WebDriver driver) {
        super(driver);
    }

    public static DeleteCourse delete(WebDriver driver, Course course, int expectedCount) {
        get(driver, "/courses");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(DeleteCourse::isPageFullyLoaded);

        String deleteButtonSelector = String.format(".card[data-id='%s'] .delete-item", course.getId());
        WebElement deleteButton = driver.findElement(By.cssSelector(deleteButtonSelector));

        deleteButton.click();

        DeleteCourse deleteCourse = PageFactory.initElements(driver, DeleteCourse.class);
        wait.until(DeleteCourse::isFormFullyLoaded);

        deleteCourse.submitButton.click();

        wait.until(d -> {
            DeleteCourse page = PageFactory.initElements(driver, DeleteCourse.class);
            return page.cards.size() == expectedCount;
        });

        deleteCourse = PageFactory.initElements(driver, DeleteCourse.class);
        deleteCourse.assertItems(expectedCount);

        return deleteCourse;
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
