package mk.ukim.finki.onlinecoursesbackend.selenium;

import java.time.Duration;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class CoursePage extends AbstractPage {
    private static final String TITLE_SELECTOR = ".course-title";
    private static final String DESCRIPTION_SELECTOR = ".course-description";
    private static final String TOPIC_NAME_SELECTOR = ".topic-name";
    private static final String PRICE_SELECTOR = ".course-price";
    private static final String CAPACITY_SELECTOR = ".course-capacity";
    private static final String ENROLL_BUTTON_SELECTOR = ".enroll-btn";
    private static final String AVAILABLE_SPOTS_SELECTOR = ".available-spots";
    private static final String START_DATE_SELECTOR = ".course-start-date";
    private static final String END_DATE_SELECTOR = ".course-end-date";
    private static final String DURATION_SELECTOR = ".course-duration";

    @FindBy(css = TITLE_SELECTOR)
    private WebElement title;

    @FindBy(css = DESCRIPTION_SELECTOR)
    private WebElement description;

    @FindBy(css = TOPIC_NAME_SELECTOR)
    private WebElement topicName;

    @FindBy(css = PRICE_SELECTOR)
    private WebElement price;

    @FindBy(css = CAPACITY_SELECTOR)
    private WebElement capacity;

    @FindBy(css = ENROLL_BUTTON_SELECTOR)
    private WebElement enrollButton;

    @FindBy(css = AVAILABLE_SPOTS_SELECTOR)
    private WebElement availableSpots;

    @FindBy(css = START_DATE_SELECTOR)
    private WebElement startDate;

    @FindBy(css = END_DATE_SELECTOR)
    private WebElement endDate;

    @FindBy(css = DURATION_SELECTOR)
    private WebElement duration;

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public static CoursePage display(WebDriver driver, Course course) {
        get(driver, String.format("/courses/%s", course.getId()));

        CoursePage coursePage = PageFactory.initElements(driver, CoursePage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(CoursePage::isPageFullyLoaded);

        ExamAssert.assertUrlEquals("The title differs.", course.getTitle(), coursePage.title.getText().trim());
        ExamAssert.assertUrlEquals("The description differs.", course.getDescription(),
            coursePage.description.getText().trim());
        ExamAssert.assertUrlEquals("The topic's name differs.", course.getTopic().getName(),
            coursePage.topicName.getText().trim());
        ExamAssert.assertEquals("The price differs.", String.format("$%s", course.getPrice()),
            coursePage.price.getText().trim());
        ExamAssert.assertEquals("The capacity differs.", course.getCapacity().toString(),
            coursePage.capacity.getText().trim());

        return coursePage;
    }

    public static CoursePage navigate(WebDriver driver, Course course) {
        get(driver, String.format("/courses/%s", course.getId()));

        CoursePage coursePage = PageFactory.initElements(driver, CoursePage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(CoursePage::isPageFullyLoaded);
        wait.until(CoursePage::isEnrollButtonPresent);

        return coursePage;
    }

    public static CoursePage enroll(WebDriver driver, Course course) {
        get(driver, String.format("/courses/%s", course.getId()));

        CoursePage coursePage = PageFactory.initElements(driver, CoursePage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(CoursePage::isPageFullyLoaded);
        wait.until(CoursePage::isEnrollButtonPresent);

        coursePage.enrollButton.click();

        wait.until((d) -> {
            try {
                CoursePage page = PageFactory.initElements(driver, CoursePage.class);
                return page.enrollButton.getText().trim().equals("ALREADY ENROLLED") &&
                    page.availableSpots.getText().trim().equals(String.valueOf(course.getCapacity() - 1));
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });

        coursePage = PageFactory.initElements(driver, CoursePage.class);
        ExamAssert.assertEquals("The button's text should be 'ALREADY ENROLLED'", "ALREADY ENROLLED",
            coursePage.enrollButton.getText().trim());
        ExamAssert.assertEquals("The available slots should be decreased by 1.",
            String.valueOf(course.getCapacity() - 1), coursePage.availableSpots.getText().trim());

        return coursePage;
    }

    public static CoursePage assertSchedule(WebDriver driver, Course course) {
        get(driver, String.format("/courses/%s", course.getId()));

        CoursePage coursePage = PageFactory.initElements(driver, CoursePage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(CoursePage::isSchedulePageFullyLoaded);

        ExamAssert.assertEquals(
            "The course start date is not as expected.",
            course.getSchedule().getStartDate().toString(),
            coursePage.startDate.getText().trim()
        );
        ExamAssert.assertEquals(
            "The course end date is not as expected.",
            course.getSchedule().getEndDate().toString(),
            coursePage.endDate.getText().trim()
        );
        ExamAssert.assertEquals(
            "The course duration (in days) is not as expected.",
            course.getSchedule().getDurationInDays().toString(),
            coursePage.duration.getText().trim()
        );

        return coursePage;
    }

    // The Enroll button is rendered only for STUDENT users — admin and teacher do not
    // see it. So isPageFullyLoaded checks only the always-visible info elements; flows
    // that actually need the Enroll button (enroll(), navigate-then-read-button) call
    // isEnrollButtonPresent / waitForEnrollButton explicitly.
    public static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresent(
            driver,
            TITLE_SELECTOR,
            DESCRIPTION_SELECTOR,
            TOPIC_NAME_SELECTOR,
            PRICE_SELECTOR,
            CAPACITY_SELECTOR
        );
    }

    public static boolean isEnrollButtonPresent(WebDriver driver) {
        return AbstractPage.areElementsPresent(driver, ENROLL_BUTTON_SELECTOR);
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
