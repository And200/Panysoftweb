package co.edu.sena.web.rest;

import co.edu.sena.domain.Presentation;
import co.edu.sena.repository.PresentationRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.Presentation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PresentationResource {

    private final Logger log = LoggerFactory.getLogger(PresentationResource.class);

    private static final String ENTITY_NAME = "presentation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresentationRepository presentationRepository;

    public PresentationResource(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }

    /**
     * {@code POST  /presentations} : Create a new presentation.
     *
     * @param presentation the presentation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presentation, or with status {@code 400 (Bad Request)} if the presentation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presentations")
    public ResponseEntity<Presentation> createPresentation(@Valid @RequestBody Presentation presentation) throws URISyntaxException {
        log.debug("REST request to save Presentation : {}", presentation);
        if (presentation.getId() != null) {
            throw new BadRequestAlertException("A new presentation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Presentation result = presentationRepository.save(presentation);
        return ResponseEntity
            .created(new URI("/api/presentations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presentations/:id} : Updates an existing presentation.
     *
     * @param id the id of the presentation to save.
     * @param presentation the presentation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentation,
     * or with status {@code 400 (Bad Request)} if the presentation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presentation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presentations/{id}")
    public ResponseEntity<Presentation> updatePresentation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Presentation presentation
    ) throws URISyntaxException {
        log.debug("REST request to update Presentation : {}, {}", id, presentation);
        if (presentation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presentationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Presentation result = presentationRepository.save(presentation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presentation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /presentations/:id} : Partial updates given fields of an existing presentation, field will ignore if it is null
     *
     * @param id the id of the presentation to save.
     * @param presentation the presentation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentation,
     * or with status {@code 400 (Bad Request)} if the presentation is not valid,
     * or with status {@code 404 (Not Found)} if the presentation is not found,
     * or with status {@code 500 (Internal Server Error)} if the presentation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/presentations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Presentation> partialUpdatePresentation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Presentation presentation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Presentation partially : {}, {}", id, presentation);
        if (presentation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presentationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Presentation> result = presentationRepository
            .findById(presentation.getId())
            .map(existingPresentation -> {
                if (presentation.getPresentation() != null) {
                    existingPresentation.setPresentation(presentation.getPresentation());
                }

                return existingPresentation;
            })
            .map(presentationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presentation.getId().toString())
        );
    }

    /**
     * {@code GET  /presentations} : get all the presentations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presentations in body.
     */
    @GetMapping("/presentations")
    public List<Presentation> getAllPresentations(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Presentations");
        return presentationRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /presentations/:id} : get the "id" presentation.
     *
     * @param id the id of the presentation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presentation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presentations/{id}")
    public ResponseEntity<Presentation> getPresentation(@PathVariable Long id) {
        log.debug("REST request to get Presentation : {}", id);
        Optional<Presentation> presentation = presentationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(presentation);
    }

    /**
     * {@code DELETE  /presentations/:id} : delete the "id" presentation.
     *
     * @param id the id of the presentation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presentations/{id}")
    public ResponseEntity<Void> deletePresentation(@PathVariable Long id) {
        log.debug("REST request to delete Presentation : {}", id);
        presentationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
