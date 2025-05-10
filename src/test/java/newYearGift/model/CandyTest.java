package newYearGift.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CandyTest {

    @Test
    void candyConstructor_whenSuccess_returnCandy() {
        Candy actual = new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Chocolate,
                100,
                100,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100));

        assertEquals(1L, actual.getId());
        assertEquals("name", actual.getName());
        assertEquals("trademark", actual.getTrademark());
        assertEquals(CandyType.Chocolate, actual.getType());
        assertEquals(100, actual.getWeight());
        assertEquals(100, actual.getCalories());
        assertEquals(BigDecimal.valueOf(100), actual.getSugar());
        assertEquals(BigDecimal.valueOf(100), actual.getFats());
        assertEquals(BigDecimal.valueOf(100), actual.getProteins());
        assertEquals(BigDecimal.valueOf(100), actual.getCarbohydrates());

    }
}
