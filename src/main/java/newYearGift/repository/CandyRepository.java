package newYearGift.repository;

import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CandyRepository extends JpaRepository<Candy, Long> {

    List<Candy> findByTrademark(String trademark);

    @Query("SELECT c FROM Candy c WHERE " +
            "(:name IS NULL OR c.name LIKE %:name%) AND " +
            "(:trademark IS NULL OR c.trademark LIKE %:trademark%) AND " +
            "(:type IS NULL OR c.type = :type) AND " +
            "(:sugarMin IS NULL OR c.sugar >= :sugarMin) AND " +
            "(:sugarMax IS NULL OR c.sugar <= :sugarMax)")
    List<Candy> getCandies(
            @Param("name") String name,
            @Param("trademark") String trademark,
            @Param("type") CandyType type,
            @Param("sugarMin") BigDecimal sugarMin,
            @Param("sugarMax") BigDecimal sugarMax
    );
}
