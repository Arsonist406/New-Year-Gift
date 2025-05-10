package newYearGift.service.impl;

import newYearGift.dto.CandyDTO;
import newYearGift.dto.SearchCandiesDTO;
import newYearGift.exception.BusinessException;
import newYearGift.mapper.CandyMapper;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.repository.CandyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandyServiceImplTest {

    @Mock
    private CandyRepository candyRepository;

    @Mock
    private CandyMapper candyMapper;

    @InjectMocks
    private CandyServiceImpl candyServiceImpl;

    @Test
    void getCandyById_whenFound_returnsCandy() {
        Long id = 1L;
        Candy candy = mock(Candy.class);

        when(candyRepository.findById(id))
                .thenReturn(Optional.of(candy));

        Candy actual = candyServiceImpl.getCandyById(id);

        assertEquals(candy, actual);
    }

    @Test
    void getCandyById_whenNotFound_throwsException() {
        Long id = 1L;

        when(candyRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> candyServiceImpl.getCandyById(id)
        );
    }

    @Test
    void getCandies_whenSugarNotValid_throwsException() {
        BusinessException ex;
        String sortBy = "";
        String orderBy = "";
        SearchCandiesDTO sugarMinIsSmallerThan0 = new SearchCandiesDTO(
                "name",
                "trademark",
                CandyType.Chocolate,
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(1));
        SearchCandiesDTO sugarMaxIsBiggerThan100 = new SearchCandiesDTO(
                "name",
                "trademark",
                CandyType.Chocolate,
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(101));
        SearchCandiesDTO sugarMinIsBiggerThanSugarMax = new SearchCandiesDTO(
                "name",
                "trademark",
                CandyType.Chocolate,
                BigDecimal.valueOf(51),
                BigDecimal.valueOf(49));


        ex = assertThrows(
                BusinessException.class,
                () -> candyServiceImpl.getCandies(sugarMinIsSmallerThan0, sortBy, orderBy)
        );
        assertTrue(ex.getMessage().contains("Min must be bigger than 0"));

        ex = assertThrows(
                BusinessException.class,
                () -> candyServiceImpl.getCandies(sugarMaxIsBiggerThan100, sortBy, orderBy)
        );
        assertTrue(ex.getMessage().contains("Max must be smaller than 100"));

        ex = assertThrows(
                BusinessException.class,
                () -> candyServiceImpl.getCandies(sugarMinIsBiggerThanSugarMax, sortBy, orderBy)
        );
        assertTrue(ex.getMessage().contains("Min must be smaller than Max"));
    }

    @Test
    void getCandies_whenFound_returnsCandies() {
        SearchCandiesDTO dto = new SearchCandiesDTO(
                "name",
                "trademark",
                CandyType.Chocolate,
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(100));

        Candy candy1 = new Candy();
        candy1.setId(1L);
        candy1.setName("Apple Candy");
        candy1.setTrademark("Sweet Inc");
        candy1.setWeight(10);
        candy1.setCalories(50);
        candy1.setSugar(BigDecimal.valueOf(20));
        candy1.setFats(BigDecimal.valueOf(5));
        candy1.setProteins(BigDecimal.valueOf(2));
        candy1.setCarbohydrates(BigDecimal.valueOf(70));

        Candy candy2 = new Candy();
        candy2.setId(2L);
        candy2.setName("Banana Candy");
        candy2.setTrademark("Fruit Corp");
        candy2.setWeight(15);
        candy2.setCalories(40);
        candy2.setSugar(BigDecimal.valueOf(15));
        candy2.setFats(BigDecimal.valueOf(3));
        candy2.setProteins(BigDecimal.valueOf(1));
        candy2.setCarbohydrates(BigDecimal.valueOf(75));

        Candy candy3 = new Candy();
        candy3.setId(3L);
        candy3.setName("Cherry Candy");
        candy3.setTrademark("Berry Ltd");
        candy3.setWeight(12);
        candy3.setCalories(60);
        candy3.setSugar(BigDecimal.valueOf(25));
        candy3.setFats(BigDecimal.valueOf(6));
        candy3.setProteins(BigDecimal.valueOf(3));
        candy3.setCarbohydrates(BigDecimal.valueOf(65));

        List<Candy> listToSort = List.of(candy1, candy2, candy3);

        when(candyRepository.getCandies(
                dto.name(),
                dto.trademark(),
                dto.type(),
                dto.sugarMin(),
                dto.sugarMax()
        )).thenReturn(listToSort);


        assertEquals(List.of(candy1, candy2, candy3),
                candyServiceImpl.getCandies(dto, "Id", "Ascending"));
        assertEquals(List.of(candy1, candy2, candy3),
                candyServiceImpl.getCandies(dto, "Name", "Ascending"));
        assertEquals(List.of(candy3, candy2, candy1),
                candyServiceImpl.getCandies(dto, "Trademark", "Ascending"));
        assertEquals(List.of(candy1, candy3, candy2),
                candyServiceImpl.getCandies(dto, "Weight", "Ascending"));
        assertEquals(List.of(candy2, candy1, candy3),
                candyServiceImpl.getCandies(dto, "Calories", "Ascending"));
        assertEquals(List.of(candy2, candy1, candy3),
                candyServiceImpl.getCandies(dto, "Sugar", "Ascending"));
        assertEquals(List.of(candy2, candy1, candy3),
                candyServiceImpl.getCandies(dto, "Fats", "Ascending"));
        assertEquals(List.of(candy2, candy1, candy3),
                candyServiceImpl.getCandies(dto, "Proteins", "Ascending"));
        assertEquals(List.of(candy3, candy1, candy2),
                candyServiceImpl.getCandies(dto, "Carbohydrates", "Ascending"));

        assertEquals(List.of(candy3, candy2, candy1),
                candyServiceImpl.getCandies(dto, "Id", "Descending"));
        assertEquals(List.of(candy3, candy2, candy1),
                candyServiceImpl.getCandies(dto, "Name", "Descending"));
        assertEquals(List.of(candy1, candy2, candy3),
                candyServiceImpl.getCandies(dto, "Trademark", "Descending"));
        assertEquals(List.of(candy2, candy3, candy1),
                candyServiceImpl.getCandies(dto, "Weight", "Descending"));
        assertEquals(List.of(candy3, candy1, candy2),
                candyServiceImpl.getCandies(dto, "Calories", "Descending"));
        assertEquals(List.of(candy3, candy1, candy2),
                candyServiceImpl.getCandies(dto, "Sugar", "Descending"));
        assertEquals(List.of(candy3, candy1, candy2),
                candyServiceImpl.getCandies(dto, "Fats", "Descending"));
        assertEquals(List.of(candy3, candy1, candy2),
                candyServiceImpl.getCandies(dto, "Proteins", "Descending"));
        assertEquals(List.of(candy2, candy1, candy3),
                candyServiceImpl.getCandies(dto, "Carbohydrates", "Descending"));

        assertEquals(List.of(candy1, candy2, candy3),
                candyServiceImpl.getCandies(dto, "InvalidField", "Ascending"));
    }

    @Test
    void createCandy_whenNameNotUniqueForTrademark_throwsException() {
        CandyDTO dto = new CandyDTO(
                1L,
                "name",
                "trademark",
                CandyType.Chocolate,
                10,
                10,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10)
        );
        Candy candy = spy(new Candy());
        candy.setName("name");

        when(candyRepository.findByTrademark(dto.trademark()))
                .thenReturn(List.of(candy));


        assertThrows(
                BusinessException.class,
                () -> candyServiceImpl.createCandy(dto)
        );
    }

    @Test
    void createCandy_whenSavedToDB_returnsCandy() {
        CandyDTO dto = new CandyDTO(
                1L,
                "name1",
                "trademark",
                CandyType.Chocolate,
                10,
                10,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10)
        );
        Candy candy = spy(new Candy());
        candy.setName("name2");

        when(candyRepository.findByTrademark(dto.trademark()))
                .thenReturn(List.of(candy));
        when(candyMapper.candy(dto))
                .thenReturn(candy);
        when(candyRepository.save(candy))
                .thenReturn(candy);


        Candy actual = candyServiceImpl.createCandy(dto);


        assertEquals(candy, actual);
    }

    @Test
    void updateCandy_whenUpdatedInDB_returnCandy() {
        Long id = 1L;
        CandyDTO dto = new CandyDTO(
                id,
                "newName",
                "newTrademark",
                CandyType.Toffee,
                99,
                99,
                BigDecimal.valueOf(99),
                BigDecimal.valueOf(99),
                BigDecimal.valueOf(99),
                BigDecimal.valueOf(99)
        );
        Candy candy = spy(new Candy());
        candy.setId(id);
        candy.setName("oldName");
        candy.setTrademark("oldTrademark");
        candy.setType(CandyType.Chocolate);
        candy.setWeight(44);
        candy.setCalories(44);
        candy.setSugar(BigDecimal.valueOf(44));
        candy.setFats(BigDecimal.valueOf(44));
        candy.setProteins(BigDecimal.valueOf(44));
        candy.setCarbohydrates(BigDecimal.valueOf(44));

        when(candyRepository.findById(id))
                .thenReturn(Optional.of(candy));
        when(candyRepository.save(candy))
                .thenReturn(candy);


        Candy actual = candyServiceImpl.updateCandy(id, dto);


        assertEquals(candy, actual);
    }

    @Test
    void deleteCandy_whenDeletedFromDB_returnTrue() {
        Long id = 1L;
        Candy candy = mock(Candy.class);

        when(candyRepository.findById(id))
                .thenReturn(Optional.of(candy));
        doNothing().when(candyRepository)
                .delete(candy);


        candyServiceImpl.deleteCandy(id);


        verify(candyRepository).delete(any());
    }
}
