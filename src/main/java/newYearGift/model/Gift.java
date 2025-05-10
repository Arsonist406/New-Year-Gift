package newYearGift.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String description;

    @Column(length = 30, nullable = false)
    private String author;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant creationDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "gift_candy_weights",
            joinColumns = @JoinColumn(name = "gift_id")
    )
    @MapKeyJoinColumn(name = "candy_id")
    @Column(name = "weight_in_grams")
    private Map<Candy, Integer> candyWeightMap;

    @PrePersist
    protected void onCreate() {
        this.creationDate = Instant.now();
    }

    public Gift(Long id, String name, String description, String author, Instant creationDate, Map<Candy, Integer> candyWeightMap) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.creationDate = creationDate;
        this.candyWeightMap = candyWeightMap;
    }
}
