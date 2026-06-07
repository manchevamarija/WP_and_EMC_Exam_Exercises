package mk.ukim.finki.onlinecoursesbackend.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "enrollments")
@NoArgsConstructor
@Getter
public class Enrollment extends BaseAuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Enrollment(Course course, User user) {
        this.course = course;
        this.user = user;
    }
}
