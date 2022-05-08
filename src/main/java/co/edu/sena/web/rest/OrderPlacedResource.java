package co.edu.sena.web.rest;

import co.edu.sena.domain.OrderPlaced;
import co.edu.sena.repository.OrderPlacedRepository;
import co.edu.sena.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.sena.domain.OrderPlaced}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderPlacedResource {

    private final Logger log = LoggerFactory.getLogger(OrderPlacedResource.class);

    private static final String ENTITY_NAME = "orderPlaced";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderPlacedRepository orderPlacedRepository;

    public OrderPlacedResource(OrderPlacedRepository orderPlacedRepository) {
        this.orderPlacedRepository = orderPlacedRepository;
    }

    /**
     * {@code POST  /order-placeds} : Create a new orderPlaced.
     *
     * @param orderPlaced the orderPlaced to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderPlaced, or with status {@code 400 (Bad Request)} if the orderPlaced has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-placeds")
    public ResponseEntity<OrderPlaced> createOrderPlaced(@Valid @RequestBody OrderPlaced orderPlaced) throws URISyntaxException {
        log.debug("REST request to save OrderPlaced : {}", orderPlaced);
        if (orderPlaced.getId() != null) {
            throw new BadRequestAlertException("A new orderPlaced cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderPlaced result = orderPlacedRepository.save(orderPlaced);
        return ResponseEntity
            .created(new URI("/api/order-placeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-placeds/:id} : Updates an existing orderPlaced.
     *
     * @param id the id of the orderPlaced to save.
     * @param orderPlaced the orderPlaced to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderPlaced,
     * or with status {@code 400 (Bad Request)} if the orderPlaced is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderPlaced couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-placeds/{id}")
    public ResponseEntity<OrderPlaced> updateOrderPlaced(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderPlaced orderPlaced
    ) throws URISyntaxException {
        log.debug("REST request to update OrderPlaced : {}, {}", id, orderPlaced);
        if (orderPlaced.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderPlaced.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderPlacedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderPlaced result = orderPlacedRepository.save(orderPlaced);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderPlaced.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-placeds/:id} : Partial updates given fields of an existing orderPlaced, field will ignore if it is null
     *
     * @param id the id of the orderPlaced to save.
     * @param orderPlaced the orderPlaced to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderPlaced,
     * or with status {@code 400 (Bad Request)} if the orderPlaced is not valid,
     * or with status {@code 404 (Not Found)} if the orderPlaced is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderPlaced couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-placeds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderPlaced> partialUpdateOrderPlaced(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderPlaced orderPlaced
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderPlaced partially : {}, {}", id, orderPlaced);
        if (orderPlaced.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderPlaced.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderPlacedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderPlaced> result = orderPlacedRepository
            .findById(orderPlaced.getId())
            .map(existingOrderPlaced -> {
                if (orderPlaced.getOrderPlacedState() != null) {
                    existingOrderPlaced.setOrderPlacedState(orderPlaced.getOrderPlacedState());
                }

                return existingOrderPlaced;
            })
            .map(orderPlacedRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderPlaced.getId().toString())
        );
    }

    /**
     * {@code GET  /order-placeds} : get all the orderPlaceds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderPlaceds in body.
     */
    @GetMapping("/order-placeds")
    public List<OrderPlaced> getAllOrderPlaceds() {
        log.debug("REST request to get all OrderPlaceds");
        return orderPlacedRepository.findAll();
    }

    /**
     * {@code GET  /order-placeds/:id} : get the "id" orderPlaced.
     *
     * @param id the id of the orderPlaced to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderPlaced, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-placeds/{id}")
    public ResponseEntity<OrderPlaced> getOrderPlaced(@PathVariable Long id) {
        log.debug("REST request to get OrderPlaced : {}", id);
        Optional<OrderPlaced> orderPlaced = orderPlacedRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderPlaced);
    }

    /**
     * {@code DELETE  /order-placeds/:id} : delete the "id" orderPlaced.
     *
     * @param id the id of the orderPlaced to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-placeds/{id}")
    public ResponseEntity<Void> deleteOrderPlaced(@PathVariable Long id) {
        log.debug("REST request to delete OrderPlaced : {}", id);
        orderPlacedRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
