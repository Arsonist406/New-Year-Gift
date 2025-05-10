package newYearGift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Map;

public record GiftDTO(

        Long id,

        @NotBlank(message = "Can't be blank")
        @Size(message = "Max=30 symbols", max = 30)
        String name,

        @NotBlank(message = "Can't be blank")
        @Size(message = "Max=200 symbols", max = 200)
        String description,

        @NotBlank(message = "Can't be blank")
        @Size(message = "Max=30 symbols", max = 30)
        String author,

        @NotNull(message = "Can't be null")
        Instant creationDate,

        @NotNull(message = "Can't be null")
        @Size(max = 10, message = "Max amount of candies in gift = 10")
        Map<Long, Integer> candyIdWeightMap
) {}
