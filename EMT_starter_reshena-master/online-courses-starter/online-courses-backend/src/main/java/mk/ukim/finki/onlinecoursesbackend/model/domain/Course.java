package mk.ukim.finki.onlinecoursesbackend.model.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course extends BaseAuditableEntity {
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;

    private BigDecimal price;

    private Integer capacity;

    @Embedded
    private CourseSchedule schedule;

    public Course() {
    }

    public Course(String title, String description, Topic topic, BigDecimal price, Integer capacity) {
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.price = price;
        this.capacity = capacity;
    }

    public Course(String title, String description, Topic topic, BigDecimal price, Integer capacity, CourseSchedule schedule) {
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.price = price;
        this.capacity = capacity;
        this.schedule = schedule;
    }
}
