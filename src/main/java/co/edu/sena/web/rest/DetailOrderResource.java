package co.edu.sena.web.rest;

import co.edu.sena.domain.DetailOrder;
import co.edu.sena.repository.DetailOrderRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.DetailOrder}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DetailOrderResource {

    private final Logger log = LoggerFactory.getLogger(DetailOrderResource.class);

    private static final String ENTITY_NAME = "detailOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailOrderRepository detailOrderRepository;

    public DetailOrderResource(DetailOrderRepository detailOrderRepository) {
        this.detailOrderRepository = detailOrderRepository;
    }

    /**
     * {@code POST  /detail-orders} : Create a new detailOrder.
     *
     * @param detailOrder the detailOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailOrder, or with status {@code 400 (Bad Request)} if the detailOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-orders")
    public ResponseEntity<DetailOrder> createDetailOrder(@Valid @RequestBody DetailOrder detailOrder) throws URISyntaxException {
        log.debug("REST request to save DetailOrder : {}", detailOrder);
        if (detailOrder.getId() != null) {
            throw new BadRequestAlertException("A new detailOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailOrder result = detailOrderRepository.save(detailOrder);
        return ResponseEntity
            .created(new URI("/api/detail-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-orders/:id} : Updates an existing detailOrder.
     *
     * @param id the id of the detailOrder to save.
     * @param detailOrder the detailOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailOrder,
     * or with status {@code 400 (Bad Request)} if the detailOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-orders/{id}")
    public ResponseEntity<DetailOrder> updateDetailOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailOrder detailOrder
    ) throws URISyntaxException {
        log.debug("REST request to update DetailOrder : {}, {}", id, detailOrder);
        if (detailOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailOrder result = detailOrderRepository.save(detailOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detail-orders/:id} : Partial updates given fields of an existing detailOrder, field will ignore if it is null
     *
     * @param id the id of the detailOrder to save.
     * @param detailOrder the detailOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailOrder,
     * or with status {@code 400 (Bad Request)} if the detailOrder is not valid,
     * or with status {@code 404 (Not Found)} if the detailOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailOrder> partialUpdateDetailOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailOrder detailOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailOrder partially : {}, {}", id, detailOrder);
        if (detailOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailOrder> result = detailOrderRepository
            .findById(detailOrder.getId())
            .map(existingDetailOrder -> {
                if (detailOrder.getQuantityOrdered() != null) {
                    existingDetailOrder.setQuantityOrdered(detailOrder.getQuantityOrdered());
                }
                if (detailOrder.getDate() != null) {
                    existingDetailOrder.setDate(detailOrder.getDate());
                }
                if (detailOrder.getInvoiceNumber() != null) {
                    existingDetailOrder.setInvoiceNumber(detailOrder.getInvoiceNumber());
                }
                if (detailOrder.getProductOrdered() != null) {
                    existingDetailOrder.setProductOrdered(detailOrder.getProductOrdered());
                }
                if (detailOrder.getPricePayment() != null) {
                    existingDetailOrder.setPricePayment(detailOrder.getPricePayment());
                }

                return existingDetailOrder;
            })
            .map(detailOrderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-orders} : get all the detailOrders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailOrders in body.
     */
    @GetMapping("/detail-orders")
    public List<DetailOrder> getAllDetailOrders(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DetailOrders");
        return detailOrderRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /detail-orders/:id} : get the "id" detailOrder.
     *
     * @param id the id of the detailOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-orders/{id}")
    public ResponseEntity<DetailOrder> getDetailOrder(@PathVariable Long id) {
        log.debug("REST request to get DetailOrder : {}", id);
        Optional<DetailOrder> detailOrder = detailOrderRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(detailOrder);
    }

    /**
     * {@code DELETE  /detail-orders/:id} : delete the "id" detailOrder.
     *
     * @param id the id of the detailOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-orders/{id}")
    public ResponseEntity<Void> deleteDetailOrder(@PathVariable Long id) {
        log.debug("REST request to delete DetailOrder : {}", id);
        detailOrderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
