package newYearGift.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftTest {

    @Test
    void giftConstructor_whenSuccess_returnGift() {
        Map<Candy, Integer> map = new HashMap<>();
        Gift actual = new Gift (
                1L,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                map
        );

        assertEquals(1L, actual.getId());
        assertEquals("name", actual.getName());
        assertEquals("description", actual.getDescription());
        assertEquals("author", actual.getAuthor());
        assertEquals(Instant.parse("2024-01-01T12:00:00Z"), actual.getCreationDate());
        assertEquals(map, actual.getCandyWeightMap());
    }
}
