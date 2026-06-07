package mk.ukim.finki.traveltoursbackend.selenium;

import java.time.Duration;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class TourPage extends AbstractPage {
    private static final String TITLE_SELECTOR = ".tour-title";
    private static final String DESCRIPTION_SELECTOR = ".tour-description";
    private static final String DESTINATION_NAME_SELECTOR = ".destination-name";
    private static final String PRICE_SELECTOR = ".tour-price";
    private static final String CAPACITY_SELECTOR = ".tour-capacity";
    private static final String RESERVE_BUTTON_SELECTOR = ".reserve-btn";
    private static final String AVAILABLE_SPOTS_SELECTOR = ".available-spots";
    private static final String START_DATE_SELECTOR = ".tour-start-date";
    private static final String END_DATE_SELECTOR = ".tour-end-date";
    private static final String DURATION_SELECTOR = ".tour-duration";

    @FindBy(css = TITLE_SELECTOR)
    private WebElement title;

    @FindBy(css = DESCRIPTION_SELECTOR)
    private WebElement description;

    @FindBy(css = DESTINATION_NAME_SELECTOR)
    private WebElement destinationName;

    @FindBy(css = PRICE_SELECTOR)
    private WebElement price;

    @FindBy(css = CAPACITY_SELECTOR)
    private WebElement capacity;

    @FindBy(css = RESERVE_BUTTON_SELECTOR)
    private WebElement reserveButton;

    @FindBy(css = AVAILABLE_SPOTS_SELECTOR)
    private WebElement availableSpots;

    @FindBy(css = START_DATE_SELECTOR)
    private WebElement startDate;

    @FindBy(css = END_DATE_SELECTOR)
    private WebElement endDate;

    @FindBy(css = DURATION_SELECTOR)
    private WebElement duration;

    public TourPage(WebDriver driver) {
        super(driver);
    }

    public static TourPage display(WebDriver driver, Tour tour) {
        get(driver, String.format("/tours/%s", tour.getId()));

        TourPage tourPage = PageFactory.initElements(driver, TourPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(TourPage::isPageFullyLoaded);

        ExamAssert.assertUrlEquals("The title differs.", tour.getTitle(), tourPage.title.getText().trim());
        ExamAssert.assertUrlEquals("The description differs.", tour.getDescription(),
            tourPage.description.getText().trim());
        ExamAssert.assertUrlEquals("The destination's name differs.", tour.getDestination().getName(),
            tourPage.destinationName.getText().trim());
        ExamAssert.assertEquals("The price differs.", String.format("$%s", tour.getPrice()),
            tourPage.price.getText().trim());
        ExamAssert.assertEquals("The capacity differs.", tour.getCapacity().toString(),
            tourPage.capacity.getText().trim());

        return tourPage;
    }

    public static TourPage navigate(WebDriver driver, Tour tour) {
        get(driver, String.format("/tours/%s", tour.getId()));

        TourPage tourPage = PageFactory.initElements(driver, TourPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(TourPage::isPageFullyLoaded);
        wait.until(TourPage::isReserveButtonPresent);

        return tourPage;
    }

    public static TourPage reserve(WebDriver driver, Tour tour) {
        get(driver, String.format("/tours/%s", tour.getId()));

        TourPage tourPage = PageFactory.initElements(driver, TourPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(TourPage::isPageFullyLoaded);
        wait.until(TourPage::isReserveButtonPresent);

        tourPage.reserveButton.click();

        wait.until((d) -> {
            try {
                TourPage page = PageFactory.initElements(driver, TourPage.class);
                return page.reserveButton.getText().trim().equals("ALREADY RESERVED") &&
                    page.availableSpots.getText().trim().equals(String.valueOf(tour.getCapacity() - 1));
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        tourPage = PageFactory.initElements(driver, TourPage.class);
        ExamAssert.assertEquals("The button's text should be 'ALREADY RESERVED'", "ALREADY RESERVED",
            tourPage.reserveButton.getText().trim());
        ExamAssert.assertEquals("The available slots should be decreased by 1.",
            String.valueOf(tour.getCapacity() - 1), tourPage.availableSpots.getText().trim());

        return tourPage;
    }

    public static TourPage assertSchedule(WebDriver driver, Tour tour) {
        get(driver, String.format("/tours/%s", tour.getId()));

        TourPage tourPage = PageFactory.initElements(driver, TourPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(TourPage::isSchedulePageFullyLoaded);

        ExamAssert.assertEquals(
            "The tour start date is not as expected.",
            tour.getSchedule().getStartDate().toString(),
            tourPage.startDate.getText().trim()
        );
        ExamAssert.assertEquals(
            "The tour end date is not as expected.",
            tour.getSchedule().getEndDate().toString(),
            tourPage.endDate.getText().trim()
        );
        ExamAssert.assertEquals(
            "The tour duration (in days) is not as expected.",
            tour.getSchedule().getDurationInDays().toString(),
            tourPage.duration.getText().trim()
        );

        return tourPage;
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            TITLE_SELECTOR,
            DESCRIPTION_SELECTOR,
            DESTINATION_NAME_SELECTOR,
            PRICE_SELECTOR,
            CAPACITY_SELECTOR
        );
    }

    public static boolean isReserveButtonPresent(WebDriver driver) {
        return AbstractPage.areElementsPresent(driver, RESERVE_BUTTON_SELECTOR);
    }

    public static boolean isSchedulePageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            TITLE_SELECTOR,
            START_DATE_SELECTOR,
            END_DATE_SELECTOR,
            DURATION_SELECTOR
        );
    }
}
