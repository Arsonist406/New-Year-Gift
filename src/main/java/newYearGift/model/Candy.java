package newYearGift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Candy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String trademark;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CandyType type;

    @Min(value = 1)
    @Max(value = 10000)
    @Column(nullable = false)
    private Integer weight;

    //Values per 100 g of product
    @Min(value = 0)
    @Max(value = 10000)
    @Column(nullable = false)
    private Integer calories;

    @Column(precision = 5, scale = 2, nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private BigDecimal sugar;

    @Column(precision = 5, scale = 2, nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private BigDecimal fats;

    @Column(precision = 5, scale = 2, nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private BigDecimal proteins;

    @Column(precision = 5, scale = 2, nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private BigDecimal carbohydrates;

    public Candy(Long id, String name, String trademark, CandyType type, Integer weight, Integer calories, BigDecimal sugar, BigDecimal fats, BigDecimal proteins, BigDecimal carbohydrates) {
        this.id = id;
        this.name = name;
        this.trademark = trademark;
        this.type = type;
        this.weight = weight;
        this.calories = calories;
        this.sugar = sugar;
        this.fats = fats;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
    }
}
