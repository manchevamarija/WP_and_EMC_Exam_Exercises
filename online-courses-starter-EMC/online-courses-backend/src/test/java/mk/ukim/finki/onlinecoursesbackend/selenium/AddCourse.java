package mk.ukim.finki.onlinecoursesbackend.selenium;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateCourseRequestDto;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class AddCourse extends AbstractPage {
    private static final String ADD_BUTTON_SELECTOR = ".add-item";
    private static final String CARD_SELECTOR = ".card";
    private static final String TITLE_FIELD_SELECTOR = "input[name='title']";
    private static final String DESCRIPTION_FIELD_SELECTOR = "input[name='description']";
    private static final String TOPIC_FIELD_SELECTOR = ".topic-select";
    private static final String PRICE_FIELD_SELECTOR = "input[name='price']";
    private static final String CAPACITY_FIELD_SELECTOR = "input[name='capacity']";
    private static final String START_DATE_FIELD_SELECTOR = "input[name='startDate']";
    private static final String END_DATE_FIELD_SELECTOR = "input[name='endDate']";
    private static final String SUBMIT_BUTTON_SELECTOR = ".submit-btn";

    @FindBy(css = ADD_BUTTON_SELECTOR)
    private WebElement addButton;

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

    public AddCourse(WebDriver driver) {
        super(driver);
    }

    public static void add(
        WebDriver driver,
        CreateOrUpdateCourseRequestDto createCourseDto,
        int expectedCount,
        Long courseId
    ) {
        get(driver, "/courses");

        AddCourse addCourse = PageFactory.initElements(driver, AddCourse.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(AddCourse::isPageFullyLoaded);

        addCourse.addButton.click();

        addCourse = PageFactory.initElements(driver, AddCourse.class);
        wait.until(AddCourse::isFormFullyLoaded);

        addCourse.titleField.sendKeys(createCourseDto.title());

        addCourse.descriptionField.sendKeys(createCourseDto.description());

        addCourse.topicField.click();
        String workspaceOptionSelector = String.format("li.topic-option[data-value='%s']", createCourseDto.topicId());
        wait.until(webDriver -> !webDriver.findElements(By.cssSelector(workspaceOptionSelector)).isEmpty());
        driver.findElements(By.cssSelector(workspaceOptionSelector)).getFirst().click();

        addCourse.priceField.sendKeys(createCourseDto.price().toString());

        addCourse.capacityField.sendKeys(createCourseDto.capacity().toString());

        setDateValue(driver, addCourse.startDateField, createCourseDto.startDate());

        setDateValue(driver, addCourse.endDateField, createCourseDto.endDate());

        addCourse.submitButton.click();

        wait.until(d -> {
            AddCourse page = PageFactory.initElements(driver, AddCourse.class);
            return page.cards.size() == expectedCount;
        });

        addCourse = PageFactory.initElements(driver, AddCourse.class);
        addCourse.assertItems(expectedCount);

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

                return title.equals(createCourseDto.title()) &&
                    description.equals(createCourseDto.description()) &&
                    price.equals(String.format("$%s", createCourseDto.price())) &&
                    capacity.equals(String.format("Capacity: %s", createCourseDto.capacity()));
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        String titleSelector = String.format(".card[data-id='%s'] .course-title", courseId);
        String title = driver.findElement(By.cssSelector(titleSelector)).getText().trim();
        ExamAssert.assertEquals("The title is not as expected.", createCourseDto.title(), title);

        String descriptionSelector = String.format(".card[data-id='%s'] .course-description", courseId);
        String description = driver.findElement(By.cssSelector(descriptionSelector)).getText().trim();
        ExamAssert.assertEquals("The description is not as expected.", createCourseDto.description(), description);

        String priceSelector = String.format(".card[data-id='%s'] .course-price", courseId);
        String price = driver.findElement(By.cssSelector(priceSelector)).getText().trim();
        ExamAssert.assertEquals("The price is not as expected.", String.format("$%s", createCourseDto.price()), price);

        String capacitySelector = String.format(".card[data-id='%s'] .course-capacity", courseId);
        String capacity = driver.findElement(By.cssSelector(capacitySelector)).getText().trim();
        ExamAssert.assertEquals("The capacity is not as expected.",
            String.format("Capacity: %s", createCourseDto.capacity()), capacity);
    }

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

    private static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            ADD_BUTTON_SELECTOR,
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
        ExamAssert.assertEquals("The number of items does not match.", expectedItemsNumber, cards.size());
    }
}
