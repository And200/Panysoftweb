package co.edu.sena.web.rest;

import co.edu.sena.domain.ProductElaborated;
import co.edu.sena.repository.ProductElaboratedRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.ProductElaborated}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductElaboratedResource {

    private final Logger log = LoggerFactory.getLogger(ProductElaboratedResource.class);

    private static final String ENTITY_NAME = "productElaborated";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductElaboratedRepository productElaboratedRepository;

    public ProductElaboratedResource(ProductElaboratedRepository productElaboratedRepository) {
        this.productElaboratedRepository = productElaboratedRepository;
    }

    /**
     * {@code POST  /product-elaborateds} : Create a new productElaborated.
     *
     * @param productElaborated the productElaborated to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productElaborated, or with status {@code 400 (Bad Request)} if the productElaborated has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-elaborateds")
    public ResponseEntity<ProductElaborated> createProductElaborated(@Valid @RequestBody ProductElaborated productElaborated)
        throws URISyntaxException {
        log.debug("REST request to save ProductElaborated : {}", productElaborated);
        if (productElaborated.getId() != null) {
            throw new BadRequestAlertException("A new productElaborated cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductElaborated result = productElaboratedRepository.save(productElaborated);
        return ResponseEntity
            .created(new URI("/api/product-elaborateds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-elaborateds/:id} : Updates an existing productElaborated.
     *
     * @param id the id of the productElaborated to save.
     * @param productElaborated the productElaborated to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productElaborated,
     * or with status {@code 400 (Bad Request)} if the productElaborated is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productElaborated couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-elaborateds/{id}")
    public ResponseEntity<ProductElaborated> updateProductElaborated(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductElaborated productElaborated
    ) throws URISyntaxException {
        log.debug("REST request to update ProductElaborated : {}, {}", id, productElaborated);
        if (productElaborated.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productElaborated.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productElaboratedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductElaborated result = productElaboratedRepository.save(productElaborated);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productElaborated.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-elaborateds/:id} : Partial updates given fields of an existing productElaborated, field will ignore if it is null
     *
     * @param id the id of the productElaborated to save.
     * @param productElaborated the productElaborated to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productElaborated,
     * or with status {@code 400 (Bad Request)} if the productElaborated is not valid,
     * or with status {@code 404 (Not Found)} if the productElaborated is not found,
     * or with status {@code 500 (Internal Server Error)} if the productElaborated couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-elaborateds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductElaborated> partialUpdateProductElaborated(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductElaborated productElaborated
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductElaborated partially : {}, {}", id, productElaborated);
        if (productElaborated.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productElaborated.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productElaboratedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductElaborated> result = productElaboratedRepository
            .findById(productElaborated.getId())
            .map(existingProductElaborated -> {
                if (productElaborated.getAmountProduced() != null) {
                    existingProductElaborated.setAmountProduced(productElaborated.getAmountProduced());
                }
                if (productElaborated.getProductName() != null) {
                    existingProductElaborated.setProductName(productElaborated.getProductName());
                }
                if (productElaborated.getBuyPrice() != null) {
                    existingProductElaborated.setBuyPrice(productElaborated.getBuyPrice());
                }

                return existingProductElaborated;
            })
            .map(productElaboratedRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productElaborated.getId().toString())
        );
    }

    /**
     * {@code GET  /product-elaborateds} : get all the productElaborateds.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productElaborateds in body.
     */
    @GetMapping("/product-elaborateds")
    public List<ProductElaborated> getAllProductElaborateds(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ProductElaborateds");
        return productElaboratedRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /product-elaborateds/:id} : get the "id" productElaborated.
     *
     * @param id the id of the productElaborated to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productElaborated, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-elaborateds/{id}")
    public ResponseEntity<ProductElaborated> getProductElaborated(@PathVariable Long id) {
        log.debug("REST request to get ProductElaborated : {}", id);
        Optional<ProductElaborated> productElaborated = productElaboratedRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(productElaborated);
    }

    /**
     * {@code DELETE  /product-elaborateds/:id} : delete the "id" productElaborated.
     *
     * @param id the id of the productElaborated to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-elaborateds/{id}")
    public ResponseEntity<Void> deleteProductElaborated(@PathVariable Long id) {
        log.debug("REST request to delete ProductElaborated : {}", id);
        productElaboratedRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
