package mk.ukim.finki.traveltoursbackend.selenium;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateTourRequestDto;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class EditTour extends AbstractPage {
    private static final String CARD_SELECTOR = ".card";
    private static final String TITLE_FIELD_SELECTOR = "input[name='title']";
    private static final String DESCRIPTION_FIELD_SELECTOR = "input[name='description']";
    private static final String DESTINATION_FIELD_SELECTOR = ".destination-select";
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

    public EditTour(WebDriver driver) {
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

    public static void edit(WebDriver driver, Tour tour, CreateOrUpdateTourRequestDto updateTourDto) {
        get(driver, "/tours");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(EditTour::isPageFullyLoaded);

        String editButtonSelector = String.format(".card[data-id='%s'] .edit-item", tour.getId());
        WebElement editButton = driver.findElement(By.cssSelector(editButtonSelector));

        editButton.click();

        EditTour editTour = PageFactory.initElements(driver, EditTour.class);
        wait.until(EditTour::isFormFullyLoaded);

        replaceText(driver, editTour.titleField, updateTourDto.title());
        replaceText(driver, editTour.descriptionField, updateTourDto.description());

        editTour.destinationField.click();
        String destinationOptionSelector = String.format("li.destination-option[data-value='%s']", updateTourDto.destinationId());
        wait.until(webDriver -> !webDriver.findElements(By.cssSelector(destinationOptionSelector)).isEmpty());
        driver.findElements(By.cssSelector(destinationOptionSelector)).getFirst().click();

        replaceText(driver, editTour.priceField, updateTourDto.price().toString());
        replaceText(driver, editTour.capacityField, updateTourDto.capacity().toString());

        setDateValue(driver, editTour.startDateField, updateTourDto.startDate());
        setDateValue(driver, editTour.endDateField, updateTourDto.endDate());

        editTour.submitButton.click();

        Long tourId = tour.getId();

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

                return title.equals(updateTourDto.title()) &&
                    description.equals(updateTourDto.description()) &&
                    price.equals(String.format("$%s", updateTourDto.price())) &&
                    capacity.equals(String.format("Capacity: %s", updateTourDto.capacity()));
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        String titleSelector = String.format(".card[data-id='%s'] .tour-title", tourId);
        String title = driver.findElement(By.cssSelector(titleSelector)).getText().trim();
        ExamAssert.assertEquals("The title is not as expected.", updateTourDto.title(), title);

        String descriptionSelector = String.format(".card[data-id='%s'] .tour-description", tourId);
        String description = driver.findElement(By.cssSelector(descriptionSelector)).getText().trim();
        ExamAssert.assertEquals("The description is not as expected.", updateTourDto.description(), description);

        String priceSelector = String.format(".card[data-id='%s'] .tour-price", tourId);
        String price = driver.findElement(By.cssSelector(priceSelector)).getText().trim();
        ExamAssert.assertEquals("The price is not as expected.", String.format("$%s", updateTourDto.price()), price);

        String capacitySelector = String.format(".card[data-id='%s'] .tour-capacity", tourId);
        String capacity = driver.findElement(By.cssSelector(capacitySelector)).getText().trim();
        ExamAssert.assertEquals("The capacity is not as expected.",
            String.format("Capacity: %s", updateTourDto.capacity()), capacity);
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
            DESTINATION_FIELD_SELECTOR,
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
