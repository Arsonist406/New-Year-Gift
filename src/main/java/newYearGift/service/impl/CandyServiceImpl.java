package newYearGift.service.impl;

import newYearGift.dto.CandyDTO;
import newYearGift.dto.SearchCandiesDTO;
import newYearGift.exception.BusinessException;
import newYearGift.mapper.CandyMapper;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.repository.CandyRepository;
import newYearGift.service.CandyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@Validated
public class CandyServiceImpl implements CandyService {

    private final CandyRepository candyRepository;
    private final CandyMapper candyMapper;

    @Autowired
    public CandyServiceImpl(
            CandyRepository candyRepository,
            CandyMapper candyMapper
    ) {
        this.candyRepository = candyRepository;
        this.candyMapper = candyMapper;
    }

    public Candy getCandyById(
            Long id
    ) {
        return candyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Candy not found by id " + id));
    }

    public List<Candy> getCandies(
            SearchCandiesDTO dto,
            String sortBy,
            String orderBy
    ) {
        validateSugarContent(dto.sugarMin(), dto.sugarMax());

        List<Candy> foundCandies = candyRepository.getCandies(
                dto.name(),
                dto.trademark(),
                dto.type(),
                dto.sugarMin(),
                dto.sugarMax()
        );

        return sortCandyList(foundCandies, sortBy, orderBy);
    }

    private void validateSugarContent(
            BigDecimal minSugar,
            BigDecimal maxSugar
    ) {
        if (minSugar.compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new BusinessException("Min must be bigger than 0");
        }

        if (maxSugar.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("Max must be smaller than 100");
        }

        if (minSugar.compareTo(maxSugar) > 0) {
            throw new BusinessException("Min must be smaller than Max");
        }
    }

    private List<Candy> sortCandyList(
            List<Candy> candies,
            String sortBy,
            String orderBy
    ) {
        Comparator<Candy> comparator = switch (sortBy) {
            case "Id"       -> Comparator.comparing(Candy::getId);
            case "Name"     -> Comparator.comparing(Candy::getName);
            case "Trademark"    -> Comparator.comparing(Candy::getTrademark);
            case "Weight"    -> Comparator.comparing(Candy::getWeight);
            case "Calories" -> Comparator.comparing(Candy::getCalories);
            case "Sugar"    -> Comparator.comparing(Candy::getSugar);
            case "Fats"    -> Comparator.comparing(Candy::getFats);
            case "Proteins"    -> Comparator.comparing(Candy::getProteins);
            case "Carbohydrates"    -> Comparator.comparing(Candy::getCarbohydrates);
            default -> Comparator.comparing(Candy::getId);
        };

        if ("Descending".equals(orderBy)) {
            comparator = comparator.reversed();
        }

        return candies
                .stream()
                .sorted(comparator)
                .toList();
    }

    @Transactional
    public Candy createCandy(
            CandyDTO dto
    ) {
        checkCandyNameUniquenessForTrademark(dto);

        Candy newCandy = candyMapper.candy(dto);
        return candyRepository.save(newCandy);
    }

    private void checkCandyNameUniquenessForTrademark(
            CandyDTO dto
    ) {
        String newCandyName = dto.name();
        String newCandyTrademark = dto.trademark();

        List<String> namesOfCandiesWithSameTrademarkAsNewCandy =
                candyRepository.findByTrademark(newCandyTrademark)
                        .stream()
                        .map(Candy::getName)
                        .toList();

        if (namesOfCandiesWithSameTrademarkAsNewCandy.contains(newCandyName)) {
            throw new BusinessException("This trademark already have candy with this name");
        }
    }

    @Transactional
    public void updateCandy(
            Long id,
            CandyDTO dto
    ) {
        Candy candy = getCandyById(id);

        String newName = dto.name();
        String oldName = candy.getName();
        if (newName != null && !newName.equals(oldName)) {
            checkCandyNameUniquenessForTrademark(dto);

            candy.setName(newName);
        }

        String newTrademark = dto.trademark();
        String oldTrademark = candy.getTrademark();
        if (newTrademark != null && !newTrademark.equals(oldTrademark)) {
            candy.setTrademark(newTrademark);
        }

        CandyType newType = dto.type();
        CandyType oldType = candy.getType();
        if (newType != null && !newType.equals(oldType)) {
            candy.setType(newType);
        }

        Integer newWeight = dto.weight();
        Integer oldWeight = candy.getWeight();
        if (newWeight != null && !newWeight.equals(oldWeight)) {
            candy.setWeight(newWeight);
        }

        BigDecimal newSugar = dto.sugar();
        BigDecimal oldSugar = candy.getSugar();
        if (newSugar != null && !newSugar.equals(oldSugar)) {
            candy.setSugar(newSugar);
        }

        Integer newCalories = dto.calories();
        Integer oldCalories = candy.getCalories();
        if (newCalories != null && !newCalories.equals(oldCalories)) {
            candy.setCalories(newCalories);
        }

        BigDecimal newFats = dto.fats();
        BigDecimal oldFats = candy.getFats();
        if (newFats != null && !newFats.equals(oldFats)) {
            candy.setFats(newFats);
        }

        BigDecimal newProteins = dto.proteins();
        BigDecimal oldProteins = candy.getProteins();
        if (newProteins != null && !newProteins.equals(oldProteins)) {
            candy.setProteins(newProteins);
        }

        BigDecimal newCarbohydrates = dto.carbohydrates();
        BigDecimal oldCarbohydrates = candy.getCarbohydrates();
        if (newCarbohydrates != null && !newCarbohydrates.equals(oldCarbohydrates)) {
            candy.setCarbohydrates(newCarbohydrates);
        }

        candyRepository.save(candy);
    }

    @Transactional
    public void deleteCandy(
            Long id
    ) {
        candyRepository.delete(getCandyById(id));
    }
}
