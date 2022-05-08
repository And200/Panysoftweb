package co.edu.sena.web.rest;

import co.edu.sena.domain.Recip;
import co.edu.sena.repository.RecipRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.Recip}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RecipResource {

    private final Logger log = LoggerFactory.getLogger(RecipResource.class);

    private static final String ENTITY_NAME = "recip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipRepository recipRepository;

    public RecipResource(RecipRepository recipRepository) {
        this.recipRepository = recipRepository;
    }

    /**
     * {@code POST  /recips} : Create a new recip.
     *
     * @param recip the recip to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recip, or with status {@code 400 (Bad Request)} if the recip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recips")
    public ResponseEntity<Recip> createRecip(@Valid @RequestBody Recip recip) throws URISyntaxException {
        log.debug("REST request to save Recip : {}", recip);
        if (recip.getId() != null) {
            throw new BadRequestAlertException("A new recip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recip result = recipRepository.save(recip);
        return ResponseEntity
            .created(new URI("/api/recips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recips/:id} : Updates an existing recip.
     *
     * @param id the id of the recip to save.
     * @param recip the recip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recip,
     * or with status {@code 400 (Bad Request)} if the recip is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recips/{id}")
    public ResponseEntity<Recip> updateRecip(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Recip recip)
        throws URISyntaxException {
        log.debug("REST request to update Recip : {}, {}", id, recip);
        if (recip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Recip result = recipRepository.save(recip);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recip.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recips/:id} : Partial updates given fields of an existing recip, field will ignore if it is null
     *
     * @param id the id of the recip to save.
     * @param recip the recip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recip,
     * or with status {@code 400 (Bad Request)} if the recip is not valid,
     * or with status {@code 404 (Not Found)} if the recip is not found,
     * or with status {@code 500 (Internal Server Error)} if the recip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recips/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Recip> partialUpdateRecip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Recip recip
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recip partially : {}, {}", id, recip);
        if (recip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Recip> result = recipRepository
            .findById(recip.getId())
            .map(existingRecip -> {
                if (recip.getNameRecip() != null) {
                    existingRecip.setNameRecip(recip.getNameRecip());
                }
                if (recip.getEstimatedPricePreparation() != null) {
                    existingRecip.setEstimatedPricePreparation(recip.getEstimatedPricePreparation());
                }
                if (recip.getAmountProductUsed() != null) {
                    existingRecip.setAmountProductUsed(recip.getAmountProductUsed());
                }

                return existingRecip;
            })
            .map(recipRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recip.getId().toString())
        );
    }

    /**
     * {@code GET  /recips} : get all the recips.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recips in body.
     */
    @GetMapping("/recips")
    public List<Recip> getAllRecips(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Recips");
        return recipRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /recips/:id} : get the "id" recip.
     *
     * @param id the id of the recip to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recip, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recips/{id}")
    public ResponseEntity<Recip> getRecip(@PathVariable Long id) {
        log.debug("REST request to get Recip : {}", id);
        Optional<Recip> recip = recipRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(recip);
    }

    /**
     * {@code DELETE  /recips/:id} : delete the "id" recip.
     *
     * @param id the id of the recip to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recips/{id}")
    public ResponseEntity<Void> deleteRecip(@PathVariable Long id) {
        log.debug("REST request to delete Recip : {}", id);
        recipRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
