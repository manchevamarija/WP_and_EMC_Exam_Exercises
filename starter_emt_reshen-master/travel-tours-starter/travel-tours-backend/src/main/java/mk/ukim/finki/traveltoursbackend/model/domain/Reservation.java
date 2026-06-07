package mk.ukim.finki.traveltoursbackend.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@Getter
public class Reservation extends BaseAuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Reservation(Tour tour, User user) {
        this.tour = tour;
        this.user = user;
    }
}
