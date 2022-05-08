package co.edu.sena.repository;

import co.edu.sena.domain.DetailOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetailOrder entity.
 */
@Repository
public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {
    default Optional<DetailOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DetailOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DetailOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct detailOrder from DetailOrder detailOrder left join fetch detailOrder.provider left join fetch detailOrder.orderPlaced",
        countQuery = "select count(distinct detailOrder) from DetailOrder detailOrder"
    )
    Page<DetailOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct detailOrder from DetailOrder detailOrder left join fetch detailOrder.provider left join fetch detailOrder.orderPlaced"
    )
    List<DetailOrder> findAllWithToOneRelationships();

    @Query(
        "select detailOrder from DetailOrder detailOrder left join fetch detailOrder.provider left join fetch detailOrder.orderPlaced where detailOrder.id =:id"
    )
    Optional<DetailOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
