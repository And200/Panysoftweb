package co.edu.sena.repository;

import co.edu.sena.domain.ProductElaborated;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductElaborated entity.
 */
@Repository
public interface ProductElaboratedRepository extends JpaRepository<ProductElaborated, Long> {
    default Optional<ProductElaborated> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductElaborated> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductElaborated> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct productElaborated from ProductElaborated productElaborated left join fetch productElaborated.category",
        countQuery = "select count(distinct productElaborated) from ProductElaborated productElaborated"
    )
    Page<ProductElaborated> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct productElaborated from ProductElaborated productElaborated left join fetch productElaborated.category")
    List<ProductElaborated> findAllWithToOneRelationships();

    @Query(
        "select productElaborated from ProductElaborated productElaborated left join fetch productElaborated.category where productElaborated.id =:id"
    )
    Optional<ProductElaborated> findOneWithToOneRelationships(@Param("id") Long id);
}
