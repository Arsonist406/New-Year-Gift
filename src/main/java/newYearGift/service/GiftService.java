package newYearGift.service;

import jakarta.validation.Valid;
import newYearGift.dto.GiftDTO;
import newYearGift.model.Gift;

import java.util.List;

public interface GiftService {

    Gift getGiftById(Long id);

    List<Gift> getGiftsByName(String name);

    Gift createGift(@Valid GiftDTO dto);

    void updateGift(Long id, @Valid GiftDTO dto);

    void deleteGift(Long id);

}
