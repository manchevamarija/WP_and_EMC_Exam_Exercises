package mk.ukim.finki.traveltoursbackend.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Notification extends BaseAuditableEntity {
    @ManyToOne
    private User user;

    private String message;
}
