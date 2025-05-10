package newYearGift.service.impl;

import newYearGift.dto.GiftDTO;
import newYearGift.exception.BusinessException;
import newYearGift.mapper.GiftMapper;
import newYearGift.model.Candy;
import newYearGift.model.Gift;
import newYearGift.repository.GiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftServiceImplTest {

    @Mock
    private GiftRepository giftRepository;

    @Mock
    private GiftMapper giftMapper;

    @Mock
    private CandyServiceImpl candyServiceImpl;

    @InjectMocks
    private GiftServiceImpl giftServiceImpl;

    @Test
    void getGiftById_whenFound_returnGift() {
        Long id = 1L;
        Gift gift = mock(Gift.class);

        when(giftRepository.findById(id))
                .thenReturn(Optional.of(gift));

        Gift actual = giftServiceImpl.getGiftById(id);

        assertEquals(gift, actual);
    }

    @Test
    void getGiftById_whenNotFound_throwsException() {
        Long id = 1L;

        when(giftRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> giftServiceImpl.getGiftById(id)
        );
    }

    @Test
    void getGiftsByName_whenFound_returnGifts() {
        String name = "name";
        Gift gift = mock(Gift.class);

        when(giftRepository.findByNameContaining(name))
                .thenReturn(List.of(gift));

        List<Gift> actual = giftServiceImpl.getGiftsByName(name);

        assertEquals(List.of(gift), actual);
    }

    @Test
    void createGift_whenCandyIdWeightMapIsNotValid_throwsException() {
        BusinessException ex;

        Map<Long, Integer> sizeIsBiggerThan10 = new HashMap<>();
        sizeIsBiggerThan10.put(1L, 50);
        sizeIsBiggerThan10.put(2L, 50);
        sizeIsBiggerThan10.put(3L, 50);
        sizeIsBiggerThan10.put(4L, 50);
        sizeIsBiggerThan10.put(5L, 50);
        sizeIsBiggerThan10.put(6L, 50);
        sizeIsBiggerThan10.put(7L, 50);
        sizeIsBiggerThan10.put(8L, 50);
        sizeIsBiggerThan10.put(9L, 50);
        sizeIsBiggerThan10.put(10L, 50);
        sizeIsBiggerThan10.put(11L, 50);
        GiftDTO dtoWhereMapSizeIsBiggerThan10 = new GiftDTO(
                1L,
                "name",
                "description",
                "author",
                Instant.now(),
                sizeIsBiggerThan10
        );

        Map<Long, Integer> weightInMapIsBiggerThan10_000 = new HashMap<>();
        weightInMapIsBiggerThan10_000.put(1L, 10001);
        GiftDTO dtoWhereWeightInMapIsBiggerThan10_000 = new GiftDTO(
                1L,
                "name",
                "description",
                "author",
                Instant.now(),
                weightInMapIsBiggerThan10_000
        );


        ex = assertThrows(
                BusinessException.class,
                () -> giftServiceImpl.createGift(dtoWhereMapSizeIsBiggerThan10)
        );
        assertTrue(ex.getMessage().contains("Max size of gift is 10 candies"));

        ex = assertThrows(
                BusinessException.class,
                () -> giftServiceImpl.createGift(dtoWhereWeightInMapIsBiggerThan10_000)
        );
        assertTrue(ex.getMessage().contains("Candy weight in gift must be smaller than 10 kg"));
    }

    @Test
    void createGift_whenFound_returnsGift() {
        Map<Long, Integer> candyIdWeightMap = new HashMap<>();
        candyIdWeightMap.put(1L, 100);
        candyIdWeightMap.put(2L, 50);
        candyIdWeightMap.put(3L, 150);
        GiftDTO dto = new GiftDTO(
                1L,
                "name",
                "description",
                "author",
                Instant.now(),
                candyIdWeightMap
        );
        Gift gift = mock(Gift.class);

        when(giftMapper.gift(dto, candyServiceImpl))
                .thenReturn(gift);
        when(giftRepository.save(gift))
                .thenReturn(gift);


        Gift actual = giftServiceImpl.createGift(dto);


        assertEquals(gift, actual);
    }

    @Test
    void updateGift_whenUpdatedInDB_returnsGift() {
        Long id = 1L;
        Map<Long, Integer> newCandyIdWeightMap = new HashMap<>();
        newCandyIdWeightMap.put(4L, 100);
        newCandyIdWeightMap.put(5L, 50);
        newCandyIdWeightMap.put(6L, 150);
        GiftDTO dto = new GiftDTO(
                1L,
                "newName",
                "newDescription",
                "newAuthor",
                Instant.now(),
                newCandyIdWeightMap
        );

        Gift gift = spy(new Gift());
        gift.setId(1L);
        gift.setName("oldName");
        gift.setDescription("oldDescription");
        gift.setAuthor("oldAuthor");
        gift.setCreationDate(Instant.parse("2024-01-01T12:00:00Z"));
        Candy candy1 = spy(new Candy());
        Candy candy2 = spy(new Candy());
        Candy candy3 = spy(new Candy());
        candy1.setId(1L);
        candy2.setId(2L);
        candy3.setId(3L);
        Map<Candy, Integer> oldCandyIdWeightMap = new HashMap<>();
        oldCandyIdWeightMap.put(candy1, 200);
        oldCandyIdWeightMap.put(candy2, 150);
        oldCandyIdWeightMap.put(candy3, 250);
        gift.setCandyWeightMap(oldCandyIdWeightMap);

        when(giftRepository.findById(id))
                .thenReturn(Optional.of(gift));
        when(candyServiceImpl.getCandyById(4L))
                .thenReturn(mock(Candy.class));
        when(candyServiceImpl.getCandyById(5L))
                .thenReturn(mock(Candy.class));
        when(candyServiceImpl.getCandyById(6L))
                .thenReturn(mock(Candy.class));
        when(giftRepository.save(gift))
                .thenReturn(gift);


        giftServiceImpl.updateGift(id, dto);


        verify(giftRepository).save(any());
    }

    @Test
    void deleteGift_whenDeletedFromDB_returnTrue() {
        Long id = 1L;
        Gift gift = mock(Gift.class);

        when(giftRepository.findById(id))
                .thenReturn(Optional.of(gift));
        doNothing().when(giftRepository)
                .delete(gift);


        giftServiceImpl.deleteGift(id);


        verify(giftRepository).delete(any());
    }
}
