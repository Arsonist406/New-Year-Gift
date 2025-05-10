package newYearGift.mapper;

import newYearGift.dto.CandyDTO;
import newYearGift.model.Candy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandyMapper {

    @Mapping(target = "id", ignore = true)
    Candy candy(CandyDTO dto);
}
