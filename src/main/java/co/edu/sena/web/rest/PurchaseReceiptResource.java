package co.edu.sena.web.rest;

import co.edu.sena.domain.PurchaseReceipt;
import co.edu.sena.repository.PurchaseReceiptRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.PurchaseReceipt}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PurchaseReceiptResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseReceiptResource.class);

    private static final String ENTITY_NAME = "purchaseReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseReceiptRepository purchaseReceiptRepository;

    public PurchaseReceiptResource(PurchaseReceiptRepository purchaseReceiptRepository) {
        this.purchaseReceiptRepository = purchaseReceiptRepository;
    }

    /**
     * {@code POST  /purchase-receipts} : Create a new purchaseReceipt.
     *
     * @param purchaseReceipt the purchaseReceipt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseReceipt, or with status {@code 400 (Bad Request)} if the purchaseReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-receipts")
    public ResponseEntity<PurchaseReceipt> createPurchaseReceipt(@Valid @RequestBody PurchaseReceipt purchaseReceipt)
        throws URISyntaxException {
        log.debug("REST request to save PurchaseReceipt : {}", purchaseReceipt);
        if (purchaseReceipt.getId() != null) {
            throw new BadRequestAlertException("A new purchaseReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseReceipt result = purchaseReceiptRepository.save(purchaseReceipt);
        return ResponseEntity
            .created(new URI("/api/purchase-receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-receipts/:id} : Updates an existing purchaseReceipt.
     *
     * @param id the id of the purchaseReceipt to save.
     * @param purchaseReceipt the purchaseReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseReceipt,
     * or with status {@code 400 (Bad Request)} if the purchaseReceipt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-receipts/{id}")
    public ResponseEntity<PurchaseReceipt> updatePurchaseReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchaseReceipt purchaseReceipt
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseReceipt : {}, {}", id, purchaseReceipt);
        if (purchaseReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseReceipt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseReceipt result = purchaseReceiptRepository.save(purchaseReceipt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseReceipt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-receipts/:id} : Partial updates given fields of an existing purchaseReceipt, field will ignore if it is null
     *
     * @param id the id of the purchaseReceipt to save.
     * @param purchaseReceipt the purchaseReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseReceipt,
     * or with status {@code 400 (Bad Request)} if the purchaseReceipt is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseReceipt is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-receipts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseReceipt> partialUpdatePurchaseReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchaseReceipt purchaseReceipt
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseReceipt partially : {}, {}", id, purchaseReceipt);
        if (purchaseReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseReceipt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseReceipt> result = purchaseReceiptRepository
            .findById(purchaseReceipt.getId())
            .map(existingPurchaseReceipt -> {
                if (purchaseReceipt.getDate() != null) {
                    existingPurchaseReceipt.setDate(purchaseReceipt.getDate());
                }
                if (purchaseReceipt.getTotalSale() != null) {
                    existingPurchaseReceipt.setTotalSale(purchaseReceipt.getTotalSale());
                }

                return existingPurchaseReceipt;
            })
            .map(purchaseReceiptRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseReceipt.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-receipts} : get all the purchaseReceipts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseReceipts in body.
     */
    @GetMapping("/purchase-receipts")
    public List<PurchaseReceipt> getAllPurchaseReceipts(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PurchaseReceipts");
        return purchaseReceiptRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /purchase-receipts/:id} : get the "id" purchaseReceipt.
     *
     * @param id the id of the purchaseReceipt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseReceipt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-receipts/{id}")
    public ResponseEntity<PurchaseReceipt> getPurchaseReceipt(@PathVariable Long id) {
        log.debug("REST request to get PurchaseReceipt : {}", id);
        Optional<PurchaseReceipt> purchaseReceipt = purchaseReceiptRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(purchaseReceipt);
    }

    /**
     * {@code DELETE  /purchase-receipts/:id} : delete the "id" purchaseReceipt.
     *
     * @param id the id of the purchaseReceipt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-receipts/{id}")
    public ResponseEntity<Void> deletePurchaseReceipt(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseReceipt : {}", id);
        purchaseReceiptRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
