package newYearGift.mapper;

import newYearGift.dto.GiftDTO;
import newYearGift.model.Candy;
import newYearGift.model.Gift;
import newYearGift.service.CandyService;
import org.mapstruct.*;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = CandyMapper.class)
public interface GiftMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "candyWeightMap", ignore = true)
    Gift gift(GiftDTO dto, @Context CandyService candyService);

    @AfterMapping
    default void mapCandyWeightMap(
            GiftDTO dto,
            @MappingTarget Gift gift,
            @Context CandyService candyService
    ) {
        if (dto.candyIdWeightMap() != null) {
            Map<Candy, Integer> result = dto.candyIdWeightMap()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            entry -> candyService.getCandyById(entry.getKey()),
                            Map.Entry::getValue
                    ));
            gift.setCandyWeightMap(result);
        }
    }
}
