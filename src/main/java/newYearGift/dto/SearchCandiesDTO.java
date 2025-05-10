package newYearGift.dto;

import jakarta.validation.constraints.Size;
import newYearGift.model.CandyType;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record SearchCandiesDTO(
        @Size(message = "Max=50 symbols", max = 50)
        String name,

        @Size(message = "Max=100 symbols", max = 100)
        String trademark,

        CandyType type,

        @Range(message = "Min=0, Max=100", min = 0, max = 100)
        BigDecimal sugarMin,

        @Range(message = "Min=0, Max=100", min = 0, max = 100)
        BigDecimal sugarMax
) {}
