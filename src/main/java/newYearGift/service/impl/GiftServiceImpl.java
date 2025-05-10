package newYearGift.service.impl;

import newYearGift.dto.GiftDTO;
import newYearGift.exception.BusinessException;
import newYearGift.mapper.GiftMapper;
import newYearGift.model.Candy;
import newYearGift.model.Gift;
import newYearGift.repository.GiftRepository;
import newYearGift.service.CandyService;
import newYearGift.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
public class GiftServiceImpl implements GiftService {

    private final GiftRepository giftRepository;
    private final GiftMapper giftMapper;
    private final CandyService candyService;

    @Autowired
    public GiftServiceImpl(
            GiftRepository giftRepository,
            GiftMapper giftMapper,
            CandyService candyService
    ) {
        this.giftRepository = giftRepository;
        this.giftMapper = giftMapper;
        this.candyService = candyService;
    }

    public Gift getGiftById(
            Long id
    ) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Gift not found by id " + id));
    }

    public List<Gift> getGiftsByName(
            String name
    ) {
        return giftRepository.findByNameContaining(name);
    }

    @Transactional
    public Gift createGift(
            GiftDTO dto
    ) {
        validateCandyIdWeightMap(dto.candyIdWeightMap());

        Gift newGift = giftMapper.gift(dto, candyService);
        return giftRepository.save(newGift);
    }

    private void validateCandyIdWeightMap(
            Map<Long, Integer> candyIdWeightMap
    ) {
        if (candyIdWeightMap.size() > 10) {
            throw new BusinessException("Max size of gift is 10 candies");
        }

        if (isCandyIdWeightMapHasAnyWeightThatBiggerThan10_000(candyIdWeightMap)) {
            throw new BusinessException("Candy weight in gift must be smaller than 10 kg");
        }
    }

    private boolean isCandyIdWeightMapHasAnyWeightThatBiggerThan10_000(
            Map<Long, Integer> candyIdWeightMap
    ) {
        return candyIdWeightMap
                .values()
                .stream()
                .anyMatch(weight -> weight > 10000);
    }

    @Transactional
    public void updateGift(
            Long id,
            GiftDTO dto
    ) {
        Gift gift = getGiftById(id);

        String newName = dto.name();
        String oldName = gift.getName();
        if (newName != null && !newName.equals(oldName)) {
            gift.setName(newName);
        }

        String newDescription = dto.description();
        String oldDescription = gift.getDescription();
        if (newDescription != null && !newDescription.equals(oldDescription)) {
            gift.setDescription(newDescription);
        }

        String newAuthor = dto.author();
        String oldAuthor = gift.getAuthor();
        if (newAuthor != null && !newAuthor.equals(oldAuthor)) {
            gift.setAuthor(newAuthor);
        }

        Instant newCreationDate = dto.creationDate();
        Instant oldCreationDate = gift.getCreationDate();
        if (newCreationDate != null && !newCreationDate.equals(oldCreationDate)) {
            gift.setCreationDate(newCreationDate);
        }

        Map<Long, Integer> newCandyIdWeightMap = dto.candyIdWeightMap();
        Map<Candy, Integer> oldCandyWeightMap = gift.getCandyWeightMap();
        Map<Long, Integer> oldCandyIdWeightMap = getIdsFromCandyWeightMap(oldCandyWeightMap);
        if (newCandyIdWeightMap != null && !newCandyIdWeightMap.equals(oldCandyIdWeightMap)) {
            validateCandyIdWeightMap(newCandyIdWeightMap);

            Map<Candy, Integer> newCandyWeightMap = getCandiesFromCandyIdWeightMap(newCandyIdWeightMap);
            gift.setCandyWeightMap(newCandyWeightMap);
        }

        giftRepository.save(gift);
    }

    private Map<Long, Integer> getIdsFromCandyWeightMap(
            Map<Candy, Integer> candyWeightMap
    ) {
        return candyWeightMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                ));
    }

    private Map<Candy, Integer> getCandiesFromCandyIdWeightMap(
            Map<Long, Integer> candyIdWeightMap
    ) {
        return candyIdWeightMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> candyService.getCandyById(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    @Transactional
    public void deleteGift(
            Long id
    ) {
        giftRepository.delete(getGiftById(id));
    }
}
