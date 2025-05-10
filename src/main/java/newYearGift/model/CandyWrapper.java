package newYearGift.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class CandyWrapper {
    private final Candy candy;
    private Integer weightInGift;

    public Long getId() {
        return candy.getId();
    }

    public String getName() {
        return candy.getName();
    }

    public String getTrademark() {
        return candy.getTrademark();
    }

    public CandyType getType() {return candy.getType();}

    public Integer getWeight() {
        return candy.getWeight();
    }

    public Integer getCalories() {
        return candy.getCalories();
    }

    public BigDecimal getSugar() {
        return candy.getSugar();
    }

    public BigDecimal getFats() {
        return candy.getFats();
    }

    public BigDecimal getProteins() {
        return candy.getProteins();
    }

    public BigDecimal getCarbohydrates() {
        return candy.getCarbohydrates();
    }
}