package co.edu.sena.repository;

import co.edu.sena.domain.DetailSale;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetailSale entity.
 */
@Repository
public interface DetailSaleRepository extends JpaRepository<DetailSale, Long> {
    default Optional<DetailSale> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DetailSale> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DetailSale> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct detailSale from DetailSale detailSale left join fetch detailSale.productElaborated left join fetch detailSale.presentation",
        countQuery = "select count(distinct detailSale) from DetailSale detailSale"
    )
    Page<DetailSale> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct detailSale from DetailSale detailSale left join fetch detailSale.productElaborated left join fetch detailSale.presentation"
    )
    List<DetailSale> findAllWithToOneRelationships();

    @Query(
        "select detailSale from DetailSale detailSale left join fetch detailSale.productElaborated left join fetch detailSale.presentation where detailSale.id =:id"
    )
    Optional<DetailSale> findOneWithToOneRelationships(@Param("id") Long id);
}
