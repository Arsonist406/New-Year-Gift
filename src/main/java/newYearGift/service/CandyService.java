package newYearGift.service;

import jakarta.validation.Valid;
import newYearGift.dto.CandyDTO;
import newYearGift.dto.SearchCandiesDTO;
import newYearGift.model.Candy;

import java.util.List;

public interface CandyService {

    Candy getCandyById(Long id);

    List<Candy> getCandies(
            @Valid SearchCandiesDTO dto,
            String sortBy,
            String orderBy
    );

    Candy createCandy(@Valid CandyDTO dto);

    void updateCandy(Long id, @Valid CandyDTO dto);

    void deleteCandy(Long id);
}
