package co.edu.sena.repository;

import co.edu.sena.domain.Presentation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Presentation entity.
 */
@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    default Optional<Presentation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Presentation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Presentation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct presentation from Presentation presentation left join fetch presentation.measureUnit",
        countQuery = "select count(distinct presentation) from Presentation presentation"
    )
    Page<Presentation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct presentation from Presentation presentation left join fetch presentation.measureUnit")
    List<Presentation> findAllWithToOneRelationships();

    @Query("select presentation from Presentation presentation left join fetch presentation.measureUnit where presentation.id =:id")
    Optional<Presentation> findOneWithToOneRelationships(@Param("id") Long id);
}
