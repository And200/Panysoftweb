package co.edu.sena.repository;

import co.edu.sena.domain.OrderPlaced;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderPlaced entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderPlacedRepository extends JpaRepository<OrderPlaced, Long> {}
