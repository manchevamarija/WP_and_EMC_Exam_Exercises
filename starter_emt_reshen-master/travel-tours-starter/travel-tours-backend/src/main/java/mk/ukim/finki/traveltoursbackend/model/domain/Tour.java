package mk.ukim.finki.traveltoursbackend.model.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tours")
@Getter
@Setter
public class Tour extends BaseAuditableEntity {
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Destination destination;

    private BigDecimal price;

    private Integer capacity;

    @Embedded
    private TourSchedule schedule;

    public Tour() {
    }

    public Tour(String title, String description, Destination destination, BigDecimal price, Integer capacity) {
        this.title = title;
        this.description = description;
        this.destination = destination;
        this.price = price;
        this.capacity = capacity;
    }

    public Tour(String title, String description, Destination destination, BigDecimal price, Integer capacity, TourSchedule schedule) {
        this.title = title;
        this.description = description;
        this.destination = destination;
        this.price = price;
        this.capacity = capacity;
        this.schedule = schedule;
    }
}
