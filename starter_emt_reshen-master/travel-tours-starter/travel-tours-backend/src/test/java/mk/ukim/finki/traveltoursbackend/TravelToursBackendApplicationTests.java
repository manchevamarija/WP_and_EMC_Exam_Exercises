package mk.ukim.finki.traveltoursbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import java.net.UnknownHostException;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateTourRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.TourSchedule;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.model.domain.Notification;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.model.event.UserReservedTourEvent;
import mk.ukim.finki.traveltoursbackend.model.exception.TourCapacityIsFullException;
import mk.ukim.finki.traveltoursbackend.model.exception.TourNotFoundException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasNotReservedTourException;
import mk.ukim.finki.traveltoursbackend.repository.TourRepository;
import mk.ukim.finki.traveltoursbackend.repository.ReservationRepository;
import mk.ukim.finki.traveltoursbackend.repository.NotificationRepository;
import mk.ukim.finki.traveltoursbackend.repository.DestinationRepository;
import mk.ukim.finki.traveltoursbackend.repository.UserRepository;
import mk.ukim.finki.traveltoursbackend.selenium.AddTour;
import mk.ukim.finki.traveltoursbackend.selenium.TourPage;
import mk.ukim.finki.traveltoursbackend.selenium.DeleteTour;
import mk.ukim.finki.traveltoursbackend.selenium.EditTour;
import mk.ukim.finki.traveltoursbackend.selenium.ReservationsPage;
import mk.ukim.finki.traveltoursbackend.selenium.LoginPage;
import mk.ukim.finki.traveltoursbackend.service.application.ReservationApplicationService;
import mk.ukim.finki.traveltoursbackend.service.domain.DestinationService;
import mk.ukim.finki.traveltoursbackend.util.CodeExtractor;
import mk.ukim.finki.traveltoursbackend.util.ExamAssert;
import mk.ukim.finki.traveltoursbackend.util.SubmissionHelper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@RecordApplicationEvents
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(OutputCaptureExtension.class)
class TravelToursBackendApplicationTests {
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
    private DestinationRepository destinationRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ApplicationEvents applicationEvents;
    @Autowired
    private ReservationApplicationService reservationApplicationService;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Destination destination1;
    private Destination destination2;
    private Tour tour1;
    private Tour tour2;
    private Tour tour3;
    private User customer1;
    private User customer2;
    private User customer3;
    private User guide;
    private User admin;

    static {
        SubmissionHelper.exam = "emc2026-midterm-extra";
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
        reservationRepository.deleteAll();
        tourRepository.deleteAll();
        destinationRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        destination1 = destinationRepository.save(new Destination(
            "Programming",
            "Learn various programming languages and development skills."
        ));
        destination2 = destinationRepository.save(new Destination(
            "Data Science",
            "Dive into machine learning, AI, and data analysis."
        ));

        tour1 = tourRepository.save(new Tour(
            "Java for Beginners",
            "Introductory Java programming tour.",
            destination1,
            new BigDecimal("129.99"),
            2,
            new TourSchedule(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 1))
        ));
        tour2 = tourRepository.save(new Tour(
            "Spring Boot Web Development",
            "Build RESTful APIs with Spring Boot.",
            destination1, new BigDecimal("149.99"),
            5,
            new TourSchedule(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 15))
        ));
        tour3 = tourRepository.save(new Tour(
            "Machine Learning with Scikit-Learn",
            "Build classification and regression models.",
            destination2,
            new BigDecimal("159.99"),
            3,
            new TourSchedule(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 16))
        ));

        customer1 = userRepository.findByUsername("customer1").orElseThrow();
        customer2 = userRepository.findByUsername("customer2").orElseThrow();
        customer3 = userRepository.findByUsername("customer3").orElseThrow();
        guide = userRepository.findByUsername("guide").orElseThrow();
        admin = userRepository.findByUsername("admin").orElseThrow();
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        reservationRepository.deleteAll();
        tourRepository.deleteAll();
        destinationRepository.deleteAll();
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
    @WithMockUser(roles = {"CUSTOMER"})
    void testFindTourByIdWithDetails_5() throws Exception {
        SubmissionHelper.startTest("testFindTourByIdWithDetails", 5);

        mockMvc.perform(get("/api/tours/{id}/details", tour1.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(tour1.getTitle()))
            .andExpect(jsonPath("$.description").value(tour1.getDescription()))
            .andExpect(jsonPath("$.destination.name").value(tour1.getDestination().getName()))
            .andExpect(jsonPath("$.destination.description").value(tour1.getDestination().getDescription()))
            .andExpect(jsonPath("$.price").value(tour1.getPrice().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(tour1.getCapacity()))
            .andExpect(jsonPath("$.startDate").value("2026-09-01"))
            .andExpect(jsonPath("$.endDate").value("2026-10-01"))
            .andExpect(jsonPath("$.durationInDays").value(30));

        SubmissionHelper.endTest();
    }

    @Order(2)
    @Test
    void testFindTourByIdWithDetailsUi_5() {
        SubmissionHelper.startTest("testFindTourByIdWithDetailsUi", 5);

        LoginPage.login(driver, "admin", "admin");
        TourPage.display(driver, tour1);

        SubmissionHelper.endTest();
    }

    @Order(3)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testAddTour_4() throws Exception {
        SubmissionHelper.startTest("testAddTour", 4);

        CreateOrUpdateTourRequestDto dto = new CreateOrUpdateTourRequestDto(
            "Python Scripting Essentials",
            "Automate tasks and process data using Python.",
            destination1.getId(),
            new BigDecimal("109.99"),
            90,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        MvcResult result = mockMvc.perform(post("/api/tours/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.destinationId").value(dto.destinationId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()))
            .andReturn();

        Long tourId = objectMapper
            .readTree(result.getResponse().getContentAsString())
            .get("id").asLong();

        mockMvc.perform(get("/api/tours/{id}", tourId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.destinationId").value(dto.destinationId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()));

        SubmissionHelper.endTest();
    }

    @Order(4)
    @Test
    void testAddTourUi_4() {
        SubmissionHelper.startTest("testAddTourUi", 4);

        CreateOrUpdateTourRequestDto createTourDto = new CreateOrUpdateTourRequestDto(
            "Python Scripting Essentials",
            "Automate tasks and process data using Python.",
            destination1.getId(),
            new BigDecimal("109.99"),
            90,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        Long tourId = tourRepository.findTopByOrderByIdDesc().getFirst().getId() + 1;

        LoginPage.login(driver, "admin", "admin");
        AddTour.add(driver, createTourDto, (int) tourRepository.count() + 1, tourId);

        Tour tour = tourRepository
            .findById(tourId)
            .orElseThrow(() -> new TourNotFoundException(tourId));

        ExamAssert.assertEquals(
            "The added tour's destination ID is not as expected.",
            createTourDto.destinationId(),
            tour.getDestination().getId()
        );

        SubmissionHelper.endTest();
    }

    @Order(5)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testEditTour_4() throws Exception {
        SubmissionHelper.startTest("testEditTour", 4);

        CreateOrUpdateTourRequestDto dto = new CreateOrUpdateTourRequestDto(
            "Intro to Data Analysis with Python",
            "Use pandas, NumPy, and Matplotlib.",
            destination2.getId(),
            new BigDecimal("139.99"),
            100,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        mockMvc.perform(put("/api/tours/{id}/edit", tour2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.destinationId").value(dto.destinationId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()));

        mockMvc.perform(get("/api/tours/{id}", tour2.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.description").value(dto.description()))
            .andExpect(jsonPath("$.destinationId").value(dto.destinationId()))
            .andExpect(jsonPath("$.price").value(dto.price().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(dto.capacity()));

        SubmissionHelper.endTest();
    }

    @Order(6)
    @Test
    void testEditTourUi_4() {
        SubmissionHelper.startTest("testEditTourUi", 4);

        CreateOrUpdateTourRequestDto updateTourDto = new CreateOrUpdateTourRequestDto(
            "Intro to Data Analysis with Python",
            "Use pandas, NumPy, and Matplotlib.",
            destination2.getId(),
            new BigDecimal("139.99"),
            100,
            LocalDate.of(2027, 10, 7),
            LocalDate.of(2027, 11, 7)
        );

        LoginPage.login(driver, "admin", "admin");
        EditTour.edit(driver, tour2, updateTourDto);

        Tour tour = tourRepository.findById(tour2.getId())
            .orElseThrow(() -> new TourNotFoundException(tour2.getId()));

        ExamAssert.assertEquals(
            "The updated tour's destination ID is not as expected.",
            updateTourDto.destinationId(),
            tour.getDestination().getId()
        );

        SubmissionHelper.endTest();
    }

    @Order(7)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testCreateAndUpdateTourValidation_4() throws Exception {
        SubmissionHelper.startTest("testCreateAndUpdateTourValidation", 4);

        LocalDate validStart = LocalDate.now().plusDays(1);
        LocalDate validEnd = LocalDate.now().plusDays(30);

        Map<String, CreateOrUpdateTourRequestDto> invalidByField = new LinkedHashMap<>();
        invalidByField.put(
            "A blank title.",
            new CreateOrUpdateTourRequestDto(
                "",
                "desc",
                destination1.getId(),
                new BigDecimal("10.00"),
                5,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A blank description.",
            new CreateOrUpdateTourRequestDto(
                "title",
                "",
                destination1.getId(),
                new BigDecimal("10.00"),
                5,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A non-positive price.",
            new CreateOrUpdateTourRequestDto(
                "title",
                "desc",
                destination1.getId(),
                BigDecimal.ZERO,
                5,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A non-positive capacity.",
            new CreateOrUpdateTourRequestDto(
                "title",
                "desc",
                destination1.getId(),
                new BigDecimal("10.00"),
                0,
                validStart,
                validEnd
            )
        );
        invalidByField.put(
            "A past start date.",
            new CreateOrUpdateTourRequestDto(
                "title",
                "desc",
                destination1.getId(),
                new BigDecimal("10.00"),
                5,
                LocalDate.of(2020, 1, 1),
                validEnd
            )
        );
        invalidByField.put(
            "A non-future end date.",
            new CreateOrUpdateTourRequestDto(
                "title",
                "desc",
                destination1.getId(),
                new BigDecimal("10.00"),
                5,
                validStart,
                LocalDate.of(2020, 1, 2)
            )
        );

        for (Map.Entry<String, CreateOrUpdateTourRequestDto> entry : invalidByField.entrySet()) {
            String body = objectMapper.writeValueAsString(entry.getValue());

            mockMvc.perform(post("/api/tours/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isBadRequest());

            mockMvc.perform(put("/api/tours/{id}/edit", tour1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isBadRequest());
        }

        SubmissionHelper.endTest();
    }

    @Order(8)
    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void testDeleteTour_5() throws Exception {
        SubmissionHelper.startTest("testDeleteTour", 5);

        mockMvc.perform(delete("/api/tours/{id}/delete", tour3.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(tour3.getTitle()))
            .andExpect(jsonPath("$.description").value(tour3.getDescription()))
            .andExpect(jsonPath("$.destinationId").value(tour3.getDestination().getId()))
            .andExpect(jsonPath("$.price").value(tour3.getPrice().doubleValue()))
            .andExpect(jsonPath("$.capacity").value(tour3.getCapacity()));

        mockMvc.perform(get("/api/tours/{id}", tour3.getId()))
            .andExpect(status().isNotFound());

        SubmissionHelper.endTest();
    }

    @Order(9)
    @Test
    void testDeleteTourUi_5() {
        SubmissionHelper.startTest("testDeleteTourUi", 5);

        LoginPage.login(driver, "admin", "admin");
        DeleteTour.delete(driver, tour3, (int) tourRepository.count() - 1);

        Optional<Tour> tour = tourRepository.findById(tour3.getId());
        ExamAssert.assertEquals("The tour is not deleted.", true, tour.isEmpty());

        SubmissionHelper.endTest();
    }

    @Order(10)
    @Test
    void testFindAllReservationsByUserWithDetailsUsingEntityGraph_10() {
        SubmissionHelper.startTest("testFindAllReservationsByUserWithDetailsUsingEntityGraph", 10);

        reservationRepository.save(new Reservation(tour1, customer1));
        reservationRepository.save(new Reservation(tour2, customer1));
        reservationRepository.save(new Reservation(tour3, customer1));

        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();
        stats.clear();

        List<DisplayReservationDetailsResponseDto> result = reservationApplicationService
            .findAllByUserWithDetails(customer1);

        assertThat(result).hasSize(3);
        assertThat(stats.getPrepareStatementCount()).isEqualTo(1);

        SubmissionHelper.endTest();
    }

    @Order(11)
    @Test
    @WithUserDetails(value = "customer1")
    void testReserveTour_3() throws Exception {
        SubmissionHelper.startTest("testReserveTour", 3);

        mockMvc.perform(post("/api/reservations/tour/{tourId}/reserve", tour1.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("customer1"))
            .andExpect(jsonPath("$.tour.title").value(tour1.getTitle()))
            .andExpect(jsonPath("$.tour.description").value(tour1.getDescription()))
            .andExpect(jsonPath("$.tour.destinationId").value(tour1.getDestination().getId()))
            .andExpect(jsonPath("$.tour.price").value(tour1.getPrice().doubleValue()))
            .andExpect(jsonPath("$.tour.capacity").value(tour1.getCapacity()));

        Optional<Reservation> reservation = reservationRepository.findByTourAndUser(tour1, customer1);
        assertThat(reservation).isPresent();
        assertThat(reservation.get().getTour().getId()).isEqualTo(tour1.getId());
        assertThat(reservation.get().getUser().getId()).isEqualTo(customer1.getId());

        SubmissionHelper.endTest();
    }

    @Order(12)
    @Test
    @WithUserDetails(value = "customer1")
    void testUserHasAlreadyReservedTourException_2() throws Exception {
        SubmissionHelper.startTest("testUserHasAlreadyReservedTourException", 2);

        reservationRepository.save(new Reservation(tour1, customer1));

        mockMvc.perform(
                post("/api/reservations/tour/{tourId}/reserve", tour1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                "The user with username 'customer1' has already reserved the tour with ID %s.".formatted(tour1.getId())
            ));

        SubmissionHelper.endTest();
    }

    @Order(13)
    @Test
    @WithUserDetails(value = "customer3")
    void testTourCapacityIsFullException_2() throws Exception {
        SubmissionHelper.startTest("testTourCapacityIsFullException", 2);

        reservationRepository.save(new Reservation(tour1, customer1));
        reservationRepository.save(new Reservation(tour1, customer2));

        mockMvc.perform(
                post("/api/reservations/tour/{tourId}/reserve", tour1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                "The tour with ID %s has reached its capacity.".formatted(tour1.getId())
            ));

        SubmissionHelper.endTest();
    }

    @Order(14)
    @Test
    void testReserveTourUi_3() {
        SubmissionHelper.startTest("testReserveTourUi", 3);

        LoginPage.login(driver, "customer1", "customer1");
        TourPage.reserve(driver, tour1);

        SubmissionHelper.endTest();
    }

    @Order(15)
    @Test
    @WithUserDetails(value = "customer1")
    void testCancelReservationForTour_3() throws Exception {
        SubmissionHelper.startTest("testCancelReservationForTour", 3);

        reservationRepository.save(new Reservation(tour1, customer1));

        mockMvc.perform(delete("/api/reservations/tour/{tourId}/cancel", tour1.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("customer1"))
            .andExpect(jsonPath("$.tour.title").value(tour1.getTitle()))
            .andExpect(jsonPath("$.tour.description").value(tour1.getDescription()))
            .andExpect(jsonPath("$.tour.destinationId").value(tour1.getDestination().getId()))
            .andExpect(jsonPath("$.tour.price").value(tour1.getPrice().doubleValue()))
            .andExpect(jsonPath("$.tour.capacity").value(tour1.getCapacity()));

        assertThat(reservationRepository.findByTourAndUser(tour1, customer1)).isEmpty();

        SubmissionHelper.endTest();
    }

    @Order(16)
    @Test
    @WithUserDetails(value = "customer1")
    void testUserHasNotReservedTourException_3() throws Exception {
        SubmissionHelper.startTest("testUserHasNotReservedTourException", 3);

        mockMvc.perform(
                delete("/api/reservations/tour/{tourId}/cancel", tour1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                "The user with username 'customer1' has not reserved the tour with ID %s.".formatted(tour1.getId())
            ));

        SubmissionHelper.endTest();
    }

    @Order(17)
    @Test
    @WithUserDetails(value = "customer1")
    void testCancelReservationForTourUi_3() {
        SubmissionHelper.startTest("testCancelReservationForTourUi", 3);

        reservationRepository.save(new Reservation(tour1, customer1));
        Reservation reservation2 = reservationRepository.save(new Reservation(tour2, customer1));

        LoginPage.login(driver, "customer1", "customer1");
        ReservationsPage.cancel(driver, reservation2, reservationRepository.findAllByUser(customer1).size() - 1);

        SubmissionHelper.endTest();
    }

    @Order(18)
    @Test
    void testReserveTourWithRaceCondition_5() throws InterruptedException {
        SubmissionHelper.startTest("testReserveTourWithRaceCondition", 5);

        List<User> customers = List.of(customer1, customer2, customer3);
        int n = customers.size();

        ExecutorService executor = Executors.newFixedThreadPool(n);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(n);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger rejectedFull = new AtomicInteger();
        AtomicInteger unexpected = new AtomicInteger();

        for (User customer : customers) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    reservationApplicationService.reserveByTourAndUser(tour1.getId(), customer);
                    succeeded.incrementAndGet();
                } catch (TourCapacityIsFullException ex) {
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
        assertThat(succeeded.get()).isEqualTo(tour1.getCapacity());
        assertThat(rejectedFull.get()).isEqualTo(n - tour1.getCapacity());
        assertThat(reservationRepository.countByTour(tour1)).isEqualTo(tour1.getCapacity());

        SubmissionHelper.endTest();
    }

    @Order(19)
    @Test
    void testCancelReservationForTourWithRaceCondition_5() throws InterruptedException {
        SubmissionHelper.startTest("testCancelReservationForTourWithRaceCondition", 5);

        reservationRepository.save(new Reservation(tour1, customer1));
        int n = 4;

        ExecutorService executor = Executors.newFixedThreadPool(n);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(n);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger notReserved = new AtomicInteger();
        AtomicInteger unexpected = new AtomicInteger();

        for (int i = 0; i < n; i++) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    reservationApplicationService.cancelByTourAndUser(tour1.getId(), customer1);
                    succeeded.incrementAndGet();
                } catch (UserHasNotReservedTourException ex) {
                    notReserved.incrementAndGet();
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
        assertThat(notReserved.get()).isEqualTo(n - 1);
        assertThat(reservationRepository.findByTourAndUser(tour1, customer1)).isEmpty();

        SubmissionHelper.endTest();
    }

    @Order(20)
    @Test
    @WithUserDetails("customer1")
    void testReservationTriggersNotificationEvent_5() throws Exception {
        SubmissionHelper.startTest("testReservationTriggersNotificationEvent", 5);

        mockMvc.perform(
                post("/api/reservations/tour/{tourId}/reserve", tour1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        assertThat(applicationEvents.stream(UserReservedTourEvent.class).count())
            .isEqualTo(1);
        UserReservedTourEvent event = applicationEvents
            .stream(UserReservedTourEvent.class)
            .findFirst()
            .orElseThrow();
        assertThat(event.getTourId()).isEqualTo(tour1.getId());
        assertThat(event.getTourTitle()).isEqualTo(tour1.getTitle());
        assertThat(event.getUser().getId()).isEqualTo(customer1.getId());

        SubmissionHelper.endTest();
    }

    @Order(21)
    @Test
    void testNotificationListenerPersistsNotification_5() {
        SubmissionHelper.startTest("testNotificationListenerPersistsNotification", 5);

        txTemplate.executeWithoutResult(s ->
            publisher.publishEvent(
                new UserReservedTourEvent(customer1, tour1.getId(), tour1.getTitle())
            )
        );

        Awaitility.await().atMost(5, SECONDS).untilAsserted(() -> {
            List<Notification> notifications =
                notificationRepository.findAllByUserOrderByCreatedAtDesc(customer1);
            assertThat(notifications).hasSize(1);
            assertThat(notifications.getFirst().getMessage()).contains(tour1.getTitle());
        });

        SubmissionHelper.endTest();
    }

    @Order(22)
    @Test
    @WithMockUser(username = "customer1", roles = {"CUSTOMER"})
    void testCustomerCannotMutateTours_3() throws Exception {
        SubmissionHelper.startTest("testCustomerCannotMutateTours", 3);

        CreateOrUpdateTourRequestDto dto = new CreateOrUpdateTourRequestDto(
            "Java for Beginners",
            "Introductory Java programming tour.",
            destination1.getId(),
            new BigDecimal("129.99"),
            100,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        mockMvc.perform(post("/api/tours/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/tours/{id}/edit", tour1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/tours/{id}/delete", tour1.getId()))
            .andExpect(status().isForbidden());

        SubmissionHelper.endTest();
    }

    @Order(23)
    @Test
    @WithMockUser(username = "guide")
    void testGuideCannotMutateTours_3() throws Exception {
        SubmissionHelper.startTest("testGuideCannotMutateTours", 3);

        CreateOrUpdateTourRequestDto dto = new CreateOrUpdateTourRequestDto(
            "Python Scripting Essentials",
            "Automate tasks and process data using Python.",
            destination1.getId(),
            new BigDecimal("109.99"),
            90,
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
        );

        mockMvc.perform(post("/api/tours/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/tours/{id}/edit", tour1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/tours/{id}/delete", tour3.getId()))
            .andExpect(status().isForbidden());

        assertThat(tourRepository.findById(tour3.getId())).isPresent();

        SubmissionHelper.endTest();
    }

    @Order(24)
    @Test
    void testNonCustomerCannotReserveOrCancel_4() throws Exception {
        SubmissionHelper.startTest("testNonCustomerCannotReserveOrCancel", 4);

        mockMvc.perform(post("/api/reservations/tour/{tourId}/reserve", tour1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(guide)))
            .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/reservations/tour/{tourId}/reserve", tour1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(admin)))
            .andExpect(status().isForbidden());

        reservationRepository.save(new Reservation(tour1, customer1));

        mockMvc.perform(delete("/api/reservations/tour/{tourId}/cancel", tour1.getId())
                .with(user(guide)))
            .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/reservations/tour/{tourId}/cancel", tour1.getId())
                .with(user(admin)))
            .andExpect(status().isForbidden());

        assertThat(reservationRepository.findByTourAndUser(tour1, customer1)).isPresent();

        SubmissionHelper.endTest();
    }
}