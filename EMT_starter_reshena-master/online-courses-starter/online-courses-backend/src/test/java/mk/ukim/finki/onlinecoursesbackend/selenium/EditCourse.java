package mk.ukim.finki.onlinecoursesbackend.selenium;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateCourseRequestDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class EditCourse extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";
    private static final String TITLE_FIELD_SELECTOR = "input[name='title']";
    private static final String DESCRIPTION_FIELD_SELECTOR = "input[name='description']";
    private static final String TOPIC_FIELD_SELECTOR = ".topic-select";
    private static final String PRICE_FIELD_SELECTOR = "input[name='price']";
    private static final String CAPACITY_FIELD_SELECTOR = "input[name='capacity']";
    private static final String START_DATE_FIELD_SELECTOR = "input[name='startDate']";
    private static final String END_DATE_FIELD_SELECTOR = "input[name='endDate']";
    private static final String SUBMIT_BUTTON_SELECTOR = ".submit-btn";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    @FindBy(css = TITLE_FIELD_SELECTOR)
    private WebElement titleField;

    @FindBy(css = DESCRIPTION_FIELD_SELECTOR)
    private WebElement descriptionField;

    @FindBy(css = TOPIC_FIELD_SELECTOR)
    private WebElement topicField;

    @FindBy(css = PRICE_FIELD_SELECTOR)
    private WebElement priceField;

    @FindBy(css = CAPACITY_FIELD_SELECTOR)
    private WebElement capacityField;

    @FindBy(css = START_DATE_FIELD_SELECTOR)
    private WebElement startDateField;

    @FindBy(css = END_DATE_FIELD_SELECTOR)
    private WebElement endDateField;

    @FindBy(css = SUBMIT_BUTTON_SELECTOR)
    private WebElement submitButton;

    public EditCourse(WebDriver driver) {
        super(driver);
    }

    /**
     * Replace the contents of a pre-filled input. We can't rely on either {@code Keys.chord(Keys.CONTROL, "a")} (broken
     * on macOS — uses Cmd) or {@code WebElement.clear()} (often leaves React's controlled-input state stale). Instead
     * we use the input's native {@code select()} via JavaScript to highlight the existing text, then {@code sendKeys}
     * types the replacement, which the input treats as a replacement of the selection. Both the DOM value and React's
     * state stay in sync.
     */
    private static void replaceText(WebDriver driver, WebElement field, String value) {
        field.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].select();", field);
        field.sendKeys(value);
    }

    /**
     * Set the value of a native {@code <input type="date">}. We can't rely on {@code sendKeys} here: typing into a date
     * input requires the keystrokes to match the browser's locale display format (e.g. {@code MM/dd/yyyy} on en-US), so
     * a differently-configured ChromeDriver locale silently leaves the field empty — the form then submits a blank date
     * and the backend returns an error. Instead we set the ISO value ({@code yyyy-MM-dd}, the canonical date-input
     * value) through React's native value setter and dispatch {@code input}/{@code change} so the controlled
     * component's state stays in sync. This is locale-independent.
     */
    private static void setDateValue(WebDriver driver, WebElement field, LocalDate date) {
        String script = """
            const input = arguments[0];
            const value = arguments[1];
            const setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;
            setter.call(input, value);
            input.dispatchEvent(new Event('input', { bubbles: true }));
            input.dispatchEvent(new Event('change', { bubbles: true }));
            """;
        ((JavascriptExecutor) driver).executeScript(script, field, date.toString());
    }

    public static void edit(WebDriver driver, Course course, CreateOrUpdateCourseRequestDto updateCourseDto) {
        get(driver, "/courses");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(EditCourse::isPageFullyLoaded);

        String editButtonSelector = String.format(".card[data-id='%s'] .edit-item", course.getId());
        WebElement editButton = driver.findElement(By.cssSelector(editButtonSelector));

        editButton.click();

        EditCourse editCourse = PageFactory.initElements(driver, EditCourse.class);
        wait.until(EditCourse::isFormFullyLoaded);

        replaceText(driver, editCourse.titleField, updateCourseDto.title());
        replaceText(driver, editCourse.descriptionField, updateCourseDto.description());

        editCourse.topicField.click();
        String topicOptionSelector = String.format("li.topic-option[data-value='%s']", updateCourseDto.topicId());
        wait.until(webDriver -> !webDriver.findElements(By.cssSelector(topicOptionSelector)).isEmpty());
        driver.findElements(By.cssSelector(topicOptionSelector)).getFirst().click();

        replaceText(driver, editCourse.priceField, updateCourseDto.price().toString());
        replaceText(driver, editCourse.capacityField, updateCourseDto.capacity().toString());

        setDateValue(driver, editCourse.startDateField, updateCourseDto.startDate());
        setDateValue(driver, editCourse.endDateField, updateCourseDto.endDate());

        editCourse.submitButton.click();

        Long courseId = course.getId();

        wait.until((d) -> {
            try {
                String titleSelector = String.format(".card[data-id='%s'] .course-title", courseId);
                String title = driver.findElement(By.cssSelector(titleSelector)).getText().trim();

                String descriptionSelector = String.format(".card[data-id='%s'] .course-description", courseId);
                String description = driver.findElement(By.cssSelector(descriptionSelector)).getText().trim();

                String priceSelector = String.format(".card[data-id='%s'] .course-price", courseId);
                String price = driver.findElement(By.cssSelector(priceSelector)).getText().trim();

                String capacitySelector = String.format(".card[data-id='%s'] .course-capacity", courseId);
                String capacity = driver.findElement(By.cssSelector(capacitySelector)).getText().trim();

                return title.equals(updateCourseDto.title()) &&
                    description.equals(updateCourseDto.description()) &&
                    price.equals(String.format("$%s", updateCourseDto.price())) &&
                    capacity.equals(String.format("Capacity: %s", updateCourseDto.capacity()));
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        String titleSelector = String.format(".card[data-id='%s'] .course-title", courseId);
        String title = driver.findElement(By.cssSelector(titleSelector)).getText().trim();
        ExamAssert.assertEquals("The title is not as expected.", updateCourseDto.title(), title);

        String descriptionSelector = String.format(".card[data-id='%s'] .course-description", courseId);
        String description = driver.findElement(By.cssSelector(descriptionSelector)).getText().trim();
        ExamAssert.assertEquals("The description is not as expected.", updateCourseDto.description(), description);

        String priceSelector = String.format(".card[data-id='%s'] .course-price", courseId);
        String price = driver.findElement(By.cssSelector(priceSelector)).getText().trim();
        ExamAssert.assertEquals("The price is not as expected.", String.format("$%s", updateCourseDto.price()), price);

        String capacitySelector = String.format(".card[data-id='%s'] .course-capacity", courseId);
        String capacity = driver.findElement(By.cssSelector(capacitySelector)).getText().trim();
        ExamAssert.assertEquals("The capacity is not as expected.",
            String.format("Capacity: %s", updateCourseDto.capacity()), capacity);
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
            TITLE_FIELD_SELECTOR,
            DESCRIPTION_FIELD_SELECTOR,
            TOPIC_FIELD_SELECTOR,
            PRICE_FIELD_SELECTOR,
            CAPACITY_FIELD_SELECTOR,
            START_DATE_FIELD_SELECTOR,
            END_DATE_FIELD_SELECTOR,
            SUBMIT_BUTTON_SELECTOR
        );
    }

    public void assertItems(int expectedItemsNumber) {
        ExamAssert.assertEquals("The number of items does not match.", expectedItemsNumber, getCards().size());
    }
}
