package mk.ukim.finki.onlinecoursesbackend.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "topics")
@NoArgsConstructor
@Getter
@Setter
public class Topic extends BaseAuditableEntity {
    private String name;

    private String description;

    @OneToMany(mappedBy = "topic")
    private List<Course> courses;

    public Topic(String name, String description) {
        this.name = name;
        this.description = description;
        this.courses = new ArrayList<>();
    }
}
