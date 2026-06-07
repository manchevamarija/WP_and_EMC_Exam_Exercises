package mk.ukim.finki.wp.june2025g1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double valuation;

    private Integer yearFounded;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    @ManyToOne
    private Founder founder;

    public Startup(String name, Double valuation, Integer yearFounded, Industry industry, Founder founder) {
        this.name = name;
        this.valuation = valuation;
        this.yearFounded = yearFounded;
        this.industry = industry;
        this.founder = founder;
    }

    public Startup(String name, Double valuation, Integer yearFounded, boolean active, Industry industry, Founder founder) {
        this.name = name;
        this.valuation = valuation;
        this.yearFounded = yearFounded;
        this.active = active;
        this.industry = industry;
        this.founder = founder;
    }
}
