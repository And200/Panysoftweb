package co.edu.sena.web.rest;

import co.edu.sena.domain.DetailSale;
import co.edu.sena.repository.DetailSaleRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.DetailSale}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DetailSaleResource {

    private final Logger log = LoggerFactory.getLogger(DetailSaleResource.class);

    private static final String ENTITY_NAME = "detailSale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailSaleRepository detailSaleRepository;

    public DetailSaleResource(DetailSaleRepository detailSaleRepository) {
        this.detailSaleRepository = detailSaleRepository;
    }

    /**
     * {@code POST  /detail-sales} : Create a new detailSale.
     *
     * @param detailSale the detailSale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailSale, or with status {@code 400 (Bad Request)} if the detailSale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-sales")
    public ResponseEntity<DetailSale> createDetailSale(@Valid @RequestBody DetailSale detailSale) throws URISyntaxException {
        log.debug("REST request to save DetailSale : {}", detailSale);
        if (detailSale.getId() != null) {
            throw new BadRequestAlertException("A new detailSale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailSale result = detailSaleRepository.save(detailSale);
        return ResponseEntity
            .created(new URI("/api/detail-sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-sales/:id} : Updates an existing detailSale.
     *
     * @param id the id of the detailSale to save.
     * @param detailSale the detailSale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailSale,
     * or with status {@code 400 (Bad Request)} if the detailSale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailSale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-sales/{id}")
    public ResponseEntity<DetailSale> updateDetailSale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailSale detailSale
    ) throws URISyntaxException {
        log.debug("REST request to update DetailSale : {}, {}", id, detailSale);
        if (detailSale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailSale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailSaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailSale result = detailSaleRepository.save(detailSale);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailSale.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detail-sales/:id} : Partial updates given fields of an existing detailSale, field will ignore if it is null
     *
     * @param id the id of the detailSale to save.
     * @param detailSale the detailSale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailSale,
     * or with status {@code 400 (Bad Request)} if the detailSale is not valid,
     * or with status {@code 404 (Not Found)} if the detailSale is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailSale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-sales/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailSale> partialUpdateDetailSale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailSale detailSale
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailSale partially : {}, {}", id, detailSale);
        if (detailSale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailSale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailSaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailSale> result = detailSaleRepository
            .findById(detailSale.getId())
            .map(existingDetailSale -> {
                if (detailSale.getProductAmount() != null) {
                    existingDetailSale.setProductAmount(detailSale.getProductAmount());
                }

                return existingDetailSale;
            })
            .map(detailSaleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailSale.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-sales} : get all the detailSales.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailSales in body.
     */
    @GetMapping("/detail-sales")
    public List<DetailSale> getAllDetailSales(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DetailSales");
        return detailSaleRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /detail-sales/:id} : get the "id" detailSale.
     *
     * @param id the id of the detailSale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailSale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-sales/{id}")
    public ResponseEntity<DetailSale> getDetailSale(@PathVariable Long id) {
        log.debug("REST request to get DetailSale : {}", id);
        Optional<DetailSale> detailSale = detailSaleRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(detailSale);
    }

    /**
     * {@code DELETE  /detail-sales/:id} : delete the "id" detailSale.
     *
     * @param id the id of the detailSale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-sales/{id}")
    public ResponseEntity<Void> deleteDetailSale(@PathVariable Long id) {
        log.debug("REST request to delete DetailSale : {}", id);
        detailSaleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
