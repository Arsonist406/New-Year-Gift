package newYearGift.dto;

import jakarta.validation.constraints.*;
import newYearGift.model.CandyType;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record CandyDTO(

        Long id,

        @NotBlank(message = "Can't be blank")
        @Size(message = "Max=50 symbols", max = 50)
        String name,

        @NotBlank(message = "Can't be blank")
        @Size(message = "Max=100 symbols", max = 100)
        String trademark,

        @NotNull(message = "Can't be null")
        CandyType type,

        @NotNull(message = "Can't be null")
        @Range(message = "Min=1, Max=10000", min = 1, max = 10000)
        Integer weight,

        @NotNull(message = "Can't be null")
        @Range(message = "Min=0, Max=10000", min = 0, max = 10000)
        Integer calories,

        @NotNull(message = "Can't be null")
        @DecimalMin(value = "0.0", message = "Min=0")
        @DecimalMax(value = "100.0", message = "Max=100")
        BigDecimal sugar,

        @NotNull(message = "Can't be null")
        @DecimalMin(value = "0.0", message = "Min=0")
        @DecimalMax(value = "100.0", message = "Max=100")
        BigDecimal fats,

        @NotNull(message = "Can't be null")
        @DecimalMin(value = "0.0", message = "Min=0")
        @DecimalMax(value = "100.0", message = "Max=100")
        BigDecimal proteins,

        @NotNull(message = "Can't be null")
        @DecimalMin(value = "0.0", message = "Min=0")
        @DecimalMax(value = "100.0", message = "Max=100")
        BigDecimal carbohydrates

) {}