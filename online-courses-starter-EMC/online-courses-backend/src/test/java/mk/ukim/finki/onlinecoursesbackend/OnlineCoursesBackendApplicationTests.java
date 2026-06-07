package mk.ukim.finki.onlinecoursesbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import java.net.UnknownHostException;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateCourseRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.domain.CourseSchedule;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Notification;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.model.event.UserEnrolledInCourseEvent;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseCapacityIsFullException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseNotFoundException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UserIsNotEnrolledInCourseException;
import mk.ukim.finki.onlinecoursesbackend.repository.CourseRepository;
import mk.ukim.finki.onlinecoursesbackend.repository.EnrollmentRepository;
import mk.ukim.finki.onlinecoursesbackend.repository.NotificationRepository;
import mk.ukim.finki.onlinecoursesbackend.repository.TopicRepository;
import mk.ukim.finki.onlinecoursesbackend.repository.UserRepository;
import mk.ukim.finki.onlinecoursesbackend.selenium.AddCourse;
import mk.ukim.finki.onlinecoursesbackend.selenium.CoursePage;
import mk.ukim.finki.onlinecoursesbackend.selenium.CoursesPage;
import mk.ukim.finki.onlinecoursesbackend.selenium.DeleteCourse;
import mk.ukim.finki.onlinecoursesbackend.selenium.EditCourse;
import mk.ukim.finki.onlinecoursesbackend.selenium.EnrollmentsPage;
import mk.ukim.finki.onlinecoursesbackend.selenium.LoginPage;
import mk.ukim.finki.onlinecoursesbackend.service.application.EnrollmentApplicationService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.TopicService;
import mk.ukim.finki.onlinecoursesbackend.util.CodeExtractor;
import mk.ukim.finki.onlinecoursesbackend.util.ExamAssert;
import mk.ukim.finki.onlinecoursesbackend.util.SubmissionHelper;
import org.awaitility.Awaitility;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@RecordApplicationEvents
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(OutputCaptureExtension.class)
class OnlineCoursesBackendApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    TransactionTemplate txTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ApplicationEvents applicationEvents;
    @Autowired
    private EnrollmentApplicationService enrollmentApplicationService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Topic topic1;
    private Topic topic2;
    private Course course1;
    private Course course2;
    private Course course3;
    private User student1;
    private User student2;
    private User student3;
    private User teacher;
    private User admin;

    static {
        SubmissionHelper.exam = "emc-2026";
        //TODO: CHANGE THE VALUE OF THE SubmissionHelper.index WITH YOUR INDEX NUMBER!!!
        SubmissionHelper.index = "231286";
    }

    private ChromeDriver driver;

    @BeforeAll
    void setUpAll() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        notificationRepository.deleteAll();
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        topicRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        topic1 = topicRepository.save(new Topic(
            "Programming",
            "Learn various programming languages and development skills."
        ));
        topic2 = topicRepository.save(new Topic(
            "Data Science",
            "Dive into machine learning, AI, and data analysis."
        ));

        course1 = courseRepository.save(new Course(
            "Java for Beginners",
            "Introductory Java programming course.",
            topic1,
            new BigDecimal("129.99"),
            2,
            new CourseSchedule(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 1))
        ));
        course2 = courseRepository.save(new Course(
            "Spring Boot Web Development",
            "Build RESTful APIs with Spring Boot.",
            topic1, new BigDecimal("149.99"),
            5,
            new CourseSchedule(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 15))
        ));
        course3 = courseRepository.save(new Course(
            "Machine Learning with Scikit-Learn",
            "Build classification and regression models.",
            topic2,
            new BigDecimal("159.99"),
            3,
            new CourseSchedule(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 16))
        ));

        student1 = userRepository.findByUsername("student1").orElseThrow();
        student2 = userRepository.findByUsername("student2").orElseThrow();
        student3 = userRepository.findByUsername("student3").orElseThrow();
        teacher = userRepository.findByUsername("teacher").orElseThrow();
        admin = userRepository.findByUsername("admin").orElseThrow();
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        topicRepository.deleteAll();
    }

    @AfterAll
    public void finalizeAndSubmit() throws JsonProcessingException, UnknownHostException {
        if (driver != null) {
            driver.quit();
        }

        CodeExtractor.submitSourcesAndLogs();
    }

    @Order(1)
    @Test
    @WithMockUser(roles = {"STUDENT"})
    void testFindCourseByIdWithDetails_3() throws Exception {
        SubmissionHelper.startTest("testFindCourseByIdWithDetails", 3);

        mockMvc.perform(get("/api/courses/{id}/details", course1.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(course1.getTitle()))
            .andExpect(jsonPath("$.description").value(course1.getDescription()))
            .andExpect(jsonPath("$.topic.name").value(course1.getTopic().getName()))
            .andExpect(jsonPath("$.topic.description").value(course1.getTopic().getDescription()))
            .andExpect(jsonPath("$.price").value(course1.getPrice().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(course1.getCapacity()))
            .andExpect(jsonPath("$.startDate").value("2026-09-01"))
            .andExpect(jsonPath("$.endDate").value("2026-10-01"))
            .andExpect(jsonPath("$.durationInDays").value(30));

        SubmissionHelper.endTest();
    }

    @Order(2)
    @Test
    void testFindCourseByIdWithDetailsUi_3() {
        SubmissionHelper.startTest("testFindCourseByIdWithDetailsUi", 3);

        LoginPage.login(driver, "admin", "admin");
        CoursePage.display(driver, course1);

        SubmissionHelper.endTest();
    }

    @Order(3)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testAddCourse_4() throws Exception {
        SubmissionHelper.startTest("testAddCourse", 4);

        CreateOrUpdateCourseRequestDto dto = new CreateOrUpdateCourseRequestDto(
            "Python Scripting Essentials",
            "Automate tasks and process data using Python.",
            topic1.getId(),
            new BigDecimal("109.99"),
            90,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        MvcResult result = mockMvc.perform(post("/api/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.topicId").value(dto.topicId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()))
            .andReturn();

        Long courseId = objectMapper
            .readTree(result.getResponse().getContentAsString())
            .get("id").asLong();

        mockMvc.perform(get("/api/courses/{id}", courseId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.topicId").value(dto.topicId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()));

        SubmissionHelper.endTest();
    }

    @Order(4)
    @Test
    void testAddCourseUi_4() {
        SubmissionHelper.startTest("testAddCourseUi", 4);

        CreateOrUpdateCourseRequestDto createCourseDto = new CreateOrUpdateCourseRequestDto(
            "Python Scripting Essentials",
            "Automate tasks and process data using Python.",
            topic1.getId(),
            new BigDecimal("109.99"),
            90,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        Long courseId = courseRepository.findTopByOrderByIdDesc().getFirst().getId() + 1;

        LoginPage.login(driver, "admin", "admin");
        AddCourse.add(driver, createCourseDto, (int) courseRepository.count() + 1, courseId);

        Course course = courseRepository
            .findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException(courseId));

        ExamAssert.assertEquals(
            "The added course's topic ID is not as expected.",
            createCourseDto.topicId(),
            course.getTopic().getId()
        );

        SubmissionHelper.endTest();
    }

    @Order(5)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testEditCourse_4() throws Exception {
        SubmissionHelper.startTest("testEditCourse", 4);

        CreateOrUpdateCourseRequestDto dto = new CreateOrUpdateCourseRequestDto(
            "Intro to Data Analysis with Python",
            "Use pandas, NumPy, and Matplotlib.",
            topic2.getId(),
            new BigDecimal("139.99"),
            100,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        mockMvc.perform(put("/api/courses/{id}/edit", course2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.topicId").value(dto.topicId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()));

        mockMvc.perform(get("/api/courses/{id}", course2.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.topicId").value(dto.topicId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()));

        SubmissionHelper.endTest();
    }

    @Order(6)
    @Test
    void testEditCourseUi_4() {
        SubmissionHelper.startTest("testEditCourseUi", 4);

        CreateOrUpdateCourseRequestDto updateCourseDto = new CreateOrUpdateCourseRequestDto(
            "Intro to Data Analysis with Python",
            "Use pandas, NumPy, and Matplotlib.",
            topic2.getId(),
            new BigDecimal("139.99"),
            100,
            LocalDate.of(2027, 10, 7),
            LocalDate.of(2027, 11, 7)
        );

        LoginPage.login(driver, "admin", "admin");
        EditCourse.edit(driver, course2, updateCourseDto);

        Course course = courseRepository.findById(course2.getId())
            .orElseThrow(() -> new CourseNotFoundException(course2.getId()));

        ExamAssert.assertEquals(
            "The updated course's topic ID is not as expected.",
            updateCourseDto.topicId(),
            course.getTopic().getId()
        );

        SubmissionHelper.endTest();
    }

    @Order(7)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testCreateAndUpdateCourseValidation_4() throws Exception {
        SubmissionHelper.startTest("testCreateAndUpdateCourseValidation", 4);

        LocalDate validStart = LocalDate.now().plusDays(1);
        LocalDate validEnd = LocalDate.now().plusDays(30);

        Map<String, CreateOrUpdateCourseRequestDto> invalidByField = new LinkedHashMap<>();
        invalidByField.put(
            "A blank title.",
            new CreateOrUpdateCourseRequestDto(
                "",
                "desc",
                topic1.getId(),
                new BigDecimal("10.00"),
                5,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A blank description.",
            new CreateOrUpdateCourseRequestDto(
                "title",
                "",
                topic1.getId(),
                new BigDecimal("10.00"),
                5,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A non-positive price.",
            new CreateOrUpdateCourseRequestDto(
                "title",
                "desc",
                topic1.getId(),
                BigDecimal.ZERO,
                5,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A non-positive capacity.",
            new CreateOrUpdateCourseRequestDto(
                "title",
                "desc",
                topic1.getId(),
                new BigDecimal("10.00"),
                0,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A past start date.",
            new CreateOrUpdateCourseRequestDto(
                "title",
                "desc",
                topic1.getId(),
                new BigDecimal("10.00"),
                5,
                LocalDate.of(2020, 1, 1),
                validEnd
            )
        );
        invalidByField.put(
            "A non-future end date.",
            new CreateOrUpdateCourseRequestDto(
                "title",
                "desc",
                topic1.getId(),
                new BigDecimal("10.00"),
                5,
                validStart,
                LocalDate.of(2020, 1, 2)
            )
        );

        for (Map.Entry<String, CreateOrUpdateCourseRequestDto> entry : invalidByField.entrySet()) {
            String body = objectMapper.writeValueAsString(entry.getValue());

            mockMvc.perform(post("/api/courses/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isBadRequest());

            mockMvc.perform(put("/api/courses/{id}/edit", course1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isBadRequest());
        }

        SubmissionHelper.endTest();
    }

    @Order(8)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testDeleteCourse_3() throws Exception {
        SubmissionHelper.startTest("testDeleteCourse", 3);

        mockMvc.perform(delete("/api/courses/{id}/delete", course3.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(course3.getTitle()))
            .andExpect(jsonPath("$.description").value(course3.getDescription()))
            .andExpect(jsonPath("$.topicId").value(course3.getTopic().getId()))
            .andExpect(jsonPath("$.price").value(course3.getPrice().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(course3.getCapacity()));

        mockMvc.perform(get("/api/courses/{id}", course3.getId()))
            .andExpect(status().isNotFound());

        SubmissionHelper.endTest();
    }

    @Order(9)
    @Test
    void testDeleteCourseUi_3() {
        SubmissionHelper.startTest("testDeleteCourseUi", 3);

        LoginPage.login(driver, "admin", "admin");
        DeleteCourse.delete(driver, course3, (int) courseRepository.count() - 1);

        Optional<Course> course = courseRepository.findById(course3.getId());
        ExamAssert.assertEquals("The course is not deleted.", true, course.isEmpty());

        SubmissionHelper.endTest();
    }

    @Order(10)
    @Test
    @WithMockUser(roles = {"STUDENT"})
    void testFindAllByAvailability_3() throws Exception {
        SubmissionHelper.startTest("testFindAllByAvailability", 3);

        enrollmentRepository.save(new Enrollment(course1, student1));
        enrollmentRepository.save(new Enrollment(course1, student2));
        enrollmentRepository.save(new Enrollment(course2, student3));

        mockMvc.perform(get("/api/courses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(course1.getId()))
            .andExpect(jsonPath("$[1].id").value(course2.getId()))
            .andExpect(jsonPath("$[2].id").value(course3.getId()));

        mockMvc.perform(get("/api/courses").param("available", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(course2.getId()))
            .andExpect(jsonPath("$[1].id").value(course3.getId()));

        mockMvc.perform(get("/api/courses").param("available", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(course1.getId()));

        SubmissionHelper.endTest();
    }

    @Order(11)
    @Test
    void testFindAllByAvailabilityUi_2() {
        SubmissionHelper.startTest("testFindAllByAvailabilityUi", 2);

        enrollmentRepository.save(new Enrollment(course1, student1));
        enrollmentRepository.save(new Enrollment(course1, student2));
        enrollmentRepository.save(new Enrollment(course2, student3));

        LoginPage.login(driver, "admin", "admin");
        CoursesPage.filter(driver);

        SubmissionHelper.endTest();
    }

    @Order(12)
    @Test
    void testFindAllEnrollmentsByUserWithDetailsUsingEntityGraph_5() {
        SubmissionHelper.startTest("testFindAllEnrollmentsByUserWithDetailsUsingEntityGraph", 5);

        enrollmentRepository.save(new Enrollment(course1, student1));
        enrollmentRepository.save(new Enrollment(course2, student1));
        enrollmentRepository.save(new Enrollment(course3, student1));

        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();
        stats.clear();

        List<DisplayEnrollmentDetailsResponseDto> result = enrollmentApplicationService
            .findAllByUserWithDetails(student1);

        assertThat(result).hasSize(3);
        assertThat(stats.getPrepareStatementCount()).isEqualTo(1);

        SubmissionHelper.endTest();
    }

    @Order(13)
    @Test
    @WithUserDetails(value = "student1")
    void testIsEnrolledInCourse_3() throws Exception {
        SubmissionHelper.startTest("testIsEnrolledInCourse", 3);

        mockMvc.perform(get("/api/enrollments/course/{courseId}/is-enrolled", course1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string("false"));

        enrollmentRepository.save(new Enrollment(course1, student1));

        mockMvc.perform(get("/api/enrollments/course/{courseId}/is-enrolled", course1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        SubmissionHelper.endTest();
    }

    @Order(14)
    @Test
    void testIsEnrolledInCourseUi_2() {
        SubmissionHelper.startTest("testIsEnrolledInCourseUi", 2);

        LoginPage.login(driver, "student1", "student1");
        CoursePage coursePage = CoursePage.navigate(driver, course1);
        ExamAssert.assertEquals(
            "The button's text should be 'ENROLL'.",
            "ENROLL", coursePage.getEnrollButton().getText().trim()
        );

        Enrollment enrollment = new Enrollment(course1, student1);
        enrollmentRepository.save(enrollment);

        coursePage = CoursePage.navigate(driver, course1);
        ExamAssert.assertEquals(
            "The button's text should be 'ALREADY ENROLLED'.",
            "ALREADY ENROLLED", coursePage.getEnrollButton().getText().trim()
        );

        SubmissionHelper.endTest();
    }

    @Order(15)
    @Test
    @WithUserDetails(value = "student1")
    void testGetAvailableSpotsInCourse_3() throws Exception {
        SubmissionHelper.startTest("testGetAvailableSpotsInCourse", 3);

        mockMvc.perform(get("/api/enrollments/course/{courseId}/available-spots", course1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string(String.valueOf(course1.getCapacity())));

        enrollmentRepository.save(new Enrollment(course1, student1));

        mockMvc.perform(get("/api/enrollments/course/{courseId}/available-spots", course1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string(String.valueOf(course1.getCapacity() - 1)));

        SubmissionHelper.endTest();
    }

    @Order(16)
    @Test
    void testGetAvailableSpotsInCourseUi_2() {
        SubmissionHelper.startTest("testGetAvailableSpotsInCourseUi", 2);

        LoginPage.login(driver, "student1", "student1");
        CoursePage coursePage = CoursePage.navigate(driver, course1);
        ExamAssert.assertEquals(
            "The available spots are not as expected.",
            String.valueOf(course1.getCapacity()), coursePage.getAvailableSpots().getText().trim()
        );

        Enrollment enrollment = new Enrollment(course1, student1);
        enrollmentRepository.save(enrollment);

        coursePage = CoursePage.navigate(driver, course1);
        ExamAssert.assertEquals(
            "The available spots are not as expected.",
            String.valueOf(course1.getCapacity() - 1), coursePage.getAvailableSpots().getText().trim()
        );

        SubmissionHelper.endTest();
    }

    @Order(17)
    @Test
    @WithUserDetails(value = "student1")
    void testEnrollInCourse_3() throws Exception {
        SubmissionHelper.startTest("testEnrollInCourse", 3);

        mockMvc.perform(post("/api/enrollments/course/{courseId}/enroll", course1.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("student1"))
            .andExpect(jsonPath("$.course.title").value(course1.getTitle()))
            .andExpect(jsonPath("$.course.description").value(course1.getDescription()))
            .andExpect(jsonPath("$.course.topicId").value(course1.getTopic().getId()))
            .andExpect(jsonPath("$.course.price").value(course1.getPrice().doubleValue()))
            .andExpect(jsonPath("$.course.capacity").value(course1.getCapacity()));

        Optional<Enrollment> enrollment = enrollmentRepository.findByCourseAndUser(course1, student1);
        assertThat(enrollment).isPresent();
        assertThat(enrollment.get().getCourse().getId()).isEqualTo(course1.getId());
        assertThat(enrollment.get().getUser().getId()).isEqualTo(student1.getId());

        SubmissionHelper.endTest();
    }

    @Order(18)
    @Test
    @WithUserDetails(value = "student1")
    void testUserIsAlreadyEnrolledInCourseException_2() throws Exception {
        SubmissionHelper.startTest("testUserIsAlreadyEnrolledInCourseException", 2);

        enrollmentRepository.save(new Enrollment(course1, student1));

        mockMvc.perform(
                post("/api/enrollments/course/{courseId}/enroll", course1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                "The user with username 'student1' is already enrolled in course with ID %s.".formatted(course1.getId())
            ));

        SubmissionHelper.endTest();
    }

    @Order(19)
    @Test
    @WithUserDetails(value = "student3")
    void testCourseCapacityIsFullException_2() throws Exception {
        SubmissionHelper.startTest("testCourseCapacityIsFullException", 2);

        enrollmentRepository.save(new Enrollment(course1, student1));
        enrollmentRepository.save(new Enrollment(course1, student2));

        mockMvc.perform(
                post("/api/enrollments/course/{courseId}/enroll", course1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                "The course with ID %s has reached its capacity.".formatted(course1.getId())
            ));

        SubmissionHelper.endTest();
    }

    @Order(20)
    @Test
    void testEnrollInCourseUi_2() {
        SubmissionHelper.startTest("testEnrollInCourseUi", 2);

        LoginPage.login(driver, "student1", "student1");
        CoursePage.enroll(driver, course1);

        SubmissionHelper.endTest();
    }

    @Order(21)
    @Test
    @WithUserDetails(value = "student1")
    void testUnenrollFromCourse_3() throws Exception {
        SubmissionHelper.startTest("testUnenrollFromCourse", 3);

        enrollmentRepository.save(new Enrollment(course1, student1));

        mockMvc.perform(delete("/api/enrollments/course/{courseId}/unenroll", course1.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("student1"))
            .andExpect(jsonPath("$.course.title").value(course1.getTitle()))
            .andExpect(jsonPath("$.course.description").value(course1.getDescription()))
            .andExpect(jsonPath("$.course.topicId").value(course1.getTopic().getId()))
            .andExpect(jsonPath("$.course.price").value(course1.getPrice().doubleValue()))
            .andExpect(jsonPath("$.course.capacity").value(course1.getCapacity()));

        assertThat(enrollmentRepository.findByCourseAndUser(course1, student1)).isEmpty();

        SubmissionHelper.endTest();
    }

    @Order(22)
    @Test
    @WithUserDetails(value = "student1")
    void testUserIsNotEnrolledInCourseException_2() throws Exception {
        SubmissionHelper.startTest("testUserIsNotEnrolledInCourseException", 2);

        mockMvc.perform(
                delete("/api/enrollments/course/{courseId}/unenroll", course1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                "The user with username 'student1' is not enrolled in course with ID %s.".formatted(course1.getId())
            ));

        SubmissionHelper.endTest();
    }

    @Order(23)
    @Test
    @WithUserDetails(value = "student1")
    void testUnenrollFromCourseUi_2() {
        SubmissionHelper.startTest("testUnenrollFromCourseUi", 2);

        enrollmentRepository.save(new Enrollment(course1, student1));
        Enrollment enrollment2 = enrollmentRepository.save(new Enrollment(course2, student1));

        LoginPage.login(driver, "student1", "student1");
        EnrollmentsPage.unenroll(driver, enrollment2, enrollmentRepository.findAllByUser(student1).size() - 1);

        SubmissionHelper.endTest();
    }

    @Order(24)
    @Test
    void testEnrollInCourseWithRaceCondition_3() throws InterruptedException {
        SubmissionHelper.startTest("testEnrollInCourseWithRaceCondition", 3);

        List<User> students = List.of(student1, student2, student3);
        int n = students.size();

        ExecutorService executor = Executors.newFixedThreadPool(n);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(n);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger rejectedFull = new AtomicInteger();
        AtomicInteger unexpected = new AtomicInteger();

        for (User student : students) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    enrollmentApplicationService.enrollByCourseAndUser(course1.getId(), student);
                    succeeded.incrementAndGet();
                } catch (CourseCapacityIsFullException ex) {
                    rejectedFull.incrementAndGet();
                } catch (Exception ex) {
                    unexpected.incrementAndGet();
                } finally {
                    finished.countDown();
                }
            });
        }

        startGate.countDown();
        assertThat(finished.await(30, SECONDS)).isTrue();
        executor.shutdown();

        assertThat(unexpected.get()).isZero();
        assertThat(succeeded.get()).isEqualTo(course1.getCapacity());
        assertThat(rejectedFull.get()).isEqualTo(n - course1.getCapacity());
        assertThat(enrollmentRepository.countByCourse(course1)).isEqualTo(course1.getCapacity());

        SubmissionHelper.endTest();
    }

    @Order(25)
    @Test
    void testUnenrollFromCourseWithRaceCondition_3() throws InterruptedException {
        SubmissionHelper.startTest("testUnenrollFromCourseWithRaceCondition", 3);

        enrollmentRepository.save(new Enrollment(course1, student1));
        int n = 4;

        ExecutorService executor = Executors.newFixedThreadPool(n);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(n);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger notEnrolled = new AtomicInteger();
        AtomicInteger unexpected = new AtomicInteger();

        for (int i = 0; i < n; i++) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    enrollmentApplicationService.unenrollByCourseAndUser(course1.getId(), student1);
                    succeeded.incrementAndGet();
                } catch (UserIsNotEnrolledInCourseException ex) {
                    notEnrolled.incrementAndGet();
                } catch (Exception ex) {
                    unexpected.incrementAndGet();
                } finally {
                    finished.countDown();
                }
            });
        }

        startGate.countDown();
        assertThat(finished.await(30, SECONDS)).isTrue();
        executor.shutdown();

        assertThat(unexpected.get()).isZero();
        assertThat(succeeded.get()).isEqualTo(1);
        assertThat(notEnrolled.get()).isEqualTo(n - 1);
        assertThat(enrollmentRepository.findByCourseAndUser(course1, student1)).isEmpty();

        SubmissionHelper.endTest();
    }

    @Order(26)
    @Test
    @WithUserDetails("student1")
    void testEnrollmentTriggersNotificationEvent_3() throws Exception {
        SubmissionHelper.startTest("testEnrollmentTriggersNotificationEvent", 3);

        mockMvc.perform(
                post("/api/enrollments/course/{courseId}/enroll", course1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        assertThat(applicationEvents.stream(UserEnrolledInCourseEvent.class).count())
            .isEqualTo(1);
        UserEnrolledInCourseEvent event = applicationEvents
            .stream(UserEnrolledInCourseEvent.class)
            .findFirst()
            .orElseThrow();
        assertThat(event.getCourseId()).isEqualTo(course1.getId());
        assertThat(event.getCourseTitle()).isEqualTo(course1.getTitle());
        assertThat(event.getUser().getId()).isEqualTo(student1.getId());

        SubmissionHelper.endTest();
    }

    @Order(27)
    @Test
    void testNotificationListenerPersistsNotification_3() {
        SubmissionHelper.startTest("testNotificationListenerPersistsNotification", 3);

        txTemplate.executeWithoutResult(s ->
            publisher.publishEvent(
                new UserEnrolledInCourseEvent(student1, course1.getId(), course1.getTitle())
            )
        );

        Awaitility.await().atMost(5, SECONDS).untilAsserted(() -> {
            List<Notification> notifications =
                notificationRepository.findAllByUserOrderByCreatedAtDesc(student1);
            assertThat(notifications).hasSize(1);
            assertThat(notifications.getFirst().getMessage()).contains(course1.getTitle());
        });

        SubmissionHelper.endTest();
    }

    @Order(28)
    @Test
    void testCourseRevenueView_5() {
        SubmissionHelper.startTest("testCourseRevenueView", 5);

        enrollmentRepository.save(new Enrollment(course1, student1));
        enrollmentRepository.save(new Enrollment(course1, student2));
        enrollmentRepository.save(new Enrollment(course2, student1));

        Map<String, Object> row1 = jdbcTemplate.queryForMap(
            "select * from course_revenue where course_id = ?",
            course1.getId()
        );
        assertThat(((Number) row1.get("enrollments")).longValue()).isEqualTo(2L);
        assertThat(new BigDecimal(row1.get("revenue").toString()))
            .isEqualByComparingTo(course1.getPrice().multiply(BigDecimal.valueOf(2)));

        Map<String, Object> row2 = jdbcTemplate.queryForMap(
            "select * from course_revenue where course_id = ?",
            course2.getId()
        );
        assertThat(((Number) row2.get("enrollments")).longValue()).isEqualTo(1L);
        assertThat(new BigDecimal(row2.get("revenue").toString()))
            .isEqualByComparingTo(course2.getPrice());

        Map<String, Object> row3 = jdbcTemplate.queryForMap(
            "select * from course_revenue where course_id = ?",
            course3.getId()
        );
        assertThat(((Number) row3.get("enrollments")).longValue()).isZero();
        assertThat(new BigDecimal(row3.get("revenue").toString()))
            .isEqualByComparingTo(BigDecimal.ZERO);

        SubmissionHelper.endTest();
    }

    @Order(29)
    @Test
    void testTopicServiceLogging_5(CapturedOutput output) {
        SubmissionHelper.startTest("testTopicServiceLogging", 5);

        Topic created = topicService.create(new Topic("Test Topic", "Test description"));

        topicService.findAll();
        topicService.findById(created.getId());
        topicService.update(created.getId(), new Topic("Updated Topic", "Updated description"));
        topicService.deleteById(created.getId());

        assertThat(output).contains("Created topic id=" + created.getId() + " name='Test Topic'");
        assertThat(output).contains("Finding all topics");
        assertThat(output).contains("Finding topic by id=" + created.getId());
        assertThat(output).contains("Updated topic id=" + created.getId() + " name='Updated Topic'");
        assertThat(output).contains("Deleted topic id=" + created.getId() + " name='Updated Topic'");

        SubmissionHelper.endTest();
    }

    @Order(30)
    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testStudentCannotMutateCourses_3() throws Exception {
        SubmissionHelper.startTest("testStudentCannotMutateCourses", 3);

        CreateOrUpdateCourseRequestDto dto = new CreateOrUpdateCourseRequestDto(
            "Java for Beginners",
            "Introductory Java programming course.",
            topic1.getId(),
            new BigDecimal("129.99"),
            100,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        mockMvc.perform(post("/api/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/courses/{id}/edit", course1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/courses/{id}/delete", course1.getId()))
            .andExpect(status().isForbidden());

        SubmissionHelper.endTest();
    }

    @Order(31)
    @Test
    @WithMockUser(username = "teacher")
    void testTeacherCannotMutateCourses_3() throws Exception {
        SubmissionHelper.startTest("testTeacherCannotMutateCourses", 3);

        CreateOrUpdateCourseRequestDto dto = new CreateOrUpdateCourseRequestDto(
            "Python Scripting Essentials",
            "Automate tasks and process data using Python.",
            topic1.getId(),
            new BigDecimal("109.99"),
            90,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        mockMvc.perform(post("/api/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/courses/{id}/edit", course1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/courses/{id}/delete", course3.getId()))
            .andExpect(status().isForbidden());

        assertThat(courseRepository.findById(course3.getId())).isPresent();

        SubmissionHelper.endTest();
    }

    @Order(32)
    @Test
    void testNonStudentCannotEnrollOrUnenroll_4() throws Exception {
        SubmissionHelper.startTest("testNonStudentCannotEnrollOrUnenroll", 4);

        mockMvc.perform(post("/api/enrollments/course/{courseId}/enroll", course1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(teacher)))
            .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/enrollments/course/{courseId}/enroll", course1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(admin)))
            .andExpect(status().isForbidden());

        enrollmentRepository.save(new Enrollment(course1, student1));

        mockMvc.perform(delete("/api/enrollments/course/{courseId}/unenroll", course1.getId())
                .with(user(teacher)))
            .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/enrollments/course/{courseId}/unenroll", course1.getId())
                .with(user(admin)))
            .andExpect(status().isForbidden());

        assertThat(enrollmentRepository.findByCourseAndUser(course1, student1)).isPresent();

        SubmissionHelper.endTest();
    }
}