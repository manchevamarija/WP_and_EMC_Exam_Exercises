package mk.ukim.finki.traveltoursbackend.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "destinations")
@NoArgsConstructor
@Getter
@Setter
public class Destination extends BaseAuditableEntity {
    private String name;

    private String description;

    @OneToMany(mappedBy = "destination")
    private List<Tour> tours;

    public Destination(String name, String description) {
        this.name = name;
        this.description = description;
        this.tours = new ArrayList<>();
    }
}
