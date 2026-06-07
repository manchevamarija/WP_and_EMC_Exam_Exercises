package mk.ukim.finki.traveltoursbackend.selenium;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateTourRequestDto;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class AddTour extends AbstractPage {
    private static final String ADD_BUTTON_SELECTOR = ".add-item";
    private static final String CARD_SELECTOR = ".card";
    private static final String TITLE_FIELD_SELECTOR = "input[name='title']";
    private static final String DESCRIPTION_FIELD_SELECTOR = "input[name='description']";
    private static final String DESTINATION_FIELD_SELECTOR = ".destination-select";
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

    @FindBy(css = DESTINATION_FIELD_SELECTOR)
    private WebElement destinationField;

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

    public AddTour(WebDriver driver) {
        super(driver);
    }

    public static void add(
        WebDriver driver,
        CreateOrUpdateTourRequestDto createTourDto,
        int expectedCount,
        Long tourId
    ) {
        get(driver, "/tours");

        AddTour addTour = PageFactory.initElements(driver, AddTour.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(AddTour::isPageFullyLoaded);

        addTour.addButton.click();

        addTour = PageFactory.initElements(driver, AddTour.class);
        wait.until(AddTour::isFormFullyLoaded);

        addTour.titleField.sendKeys(createTourDto.title());

        addTour.descriptionField.sendKeys(createTourDto.description());

        addTour.destinationField.click();
        String workspaceOptionSelector = String.format("li.destination-option[data-value='%s']", createTourDto.destinationId());
        wait.until(webDriver -> !webDriver.findElements(By.cssSelector(workspaceOptionSelector)).isEmpty());
        driver.findElements(By.cssSelector(workspaceOptionSelector)).getFirst().click();

        addTour.priceField.sendKeys(createTourDto.price().toString());

        addTour.capacityField.sendKeys(createTourDto.capacity().toString());

        setDateValue(driver, addTour.startDateField, createTourDto.startDate());

        setDateValue(driver, addTour.endDateField, createTourDto.endDate());

        addTour.submitButton.click();

        wait.until(d -> {
            AddTour page = PageFactory.initElements(driver, AddTour.class);
            return page.cards.size() == expectedCount;
        });

        addTour = PageFactory.initElements(driver, AddTour.class);
        addTour.assertItems(expectedCount);

        wait.until((d) -> {
            try {
                String titleSelector = String.format(".card[data-id='%s'] .tour-title", tourId);
                String title = driver.findElement(By.cssSelector(titleSelector)).getText().trim();

                String descriptionSelector = String.format(".card[data-id='%s'] .tour-description", tourId);
                String description = driver.findElement(By.cssSelector(descriptionSelector)).getText().trim();

                String priceSelector = String.format(".card[data-id='%s'] .tour-price", tourId);
                String price = driver.findElement(By.cssSelector(priceSelector)).getText().trim();

                String capacitySelector = String.format(".card[data-id='%s'] .tour-capacity", tourId);
                String capacity = driver.findElement(By.cssSelector(capacitySelector)).getText().trim();

                return title.equals(createTourDto.title()) &&
                    description.equals(createTourDto.description()) &&
                    price.equals(String.format("$%s", createTourDto.price())) &&
                    capacity.equals(String.format("Capacity: %s", createTourDto.capacity()));
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        String titleSelector = String.format(".card[data-id='%s'] .tour-title", tourId);
        String title = driver.findElement(By.cssSelector(titleSelector)).getText().trim();
        ExamAssert.assertEquals("The title is not as expected.", createTourDto.title(), title);

        String descriptionSelector = String.format(".card[data-id='%s'] .tour-description", tourId);
        String description = driver.findElement(By.cssSelector(descriptionSelector)).getText().trim();
        ExamAssert.assertEquals("The description is not as expected.", createTourDto.description(), description);

        String priceSelector = String.format(".card[data-id='%s'] .tour-price", tourId);
        String price = driver.findElement(By.cssSelector(priceSelector)).getText().trim();
        ExamAssert.assertEquals("The price is not as expected.", String.format("$%s", createTourDto.price()), price);

        String capacitySelector = String.format(".card[data-id='%s'] .tour-capacity", tourId);
        String capacity = driver.findElement(By.cssSelector(capacitySelector)).getText().trim();
        ExamAssert.assertEquals("The capacity is not as expected.",
            String.format("Capacity: %s", createTourDto.capacity()), capacity);
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
            DESTINATION_FIELD_SELECTOR,
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
