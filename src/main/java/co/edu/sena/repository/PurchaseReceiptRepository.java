package co.edu.sena.repository;

import co.edu.sena.domain.PurchaseReceipt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PurchaseReceipt entity.
 */
@Repository
public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, Long> {
    default Optional<PurchaseReceipt> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchaseReceipt> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchaseReceipt> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct purchaseReceipt from PurchaseReceipt purchaseReceipt left join fetch purchaseReceipt.detailSale",
        countQuery = "select count(distinct purchaseReceipt) from PurchaseReceipt purchaseReceipt"
    )
    Page<PurchaseReceipt> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct purchaseReceipt from PurchaseReceipt purchaseReceipt left join fetch purchaseReceipt.detailSale")
    List<PurchaseReceipt> findAllWithToOneRelationships();

    @Query(
        "select purchaseReceipt from PurchaseReceipt purchaseReceipt left join fetch purchaseReceipt.detailSale where purchaseReceipt.id =:id"
    )
    Optional<PurchaseReceipt> findOneWithToOneRelationships(@Param("id") Long id);
}
