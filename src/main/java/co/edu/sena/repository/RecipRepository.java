package co.edu.sena.repository;

import co.edu.sena.domain.Recip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Recip entity.
 */
@Repository
public interface RecipRepository extends JpaRepository<Recip, Long> {
    default Optional<Recip> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Recip> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Recip> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct recip from Recip recip left join fetch recip.product left join fetch recip.category",
        countQuery = "select count(distinct recip) from Recip recip"
    )
    Page<Recip> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct recip from Recip recip left join fetch recip.product left join fetch recip.category")
    List<Recip> findAllWithToOneRelationships();

    @Query("select recip from Recip recip left join fetch recip.product left join fetch recip.category where recip.id =:id")
    Optional<Recip> findOneWithToOneRelationships(@Param("id") Long id);
}
