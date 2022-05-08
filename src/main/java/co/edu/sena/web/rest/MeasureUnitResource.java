package co.edu.sena.web.rest;

import co.edu.sena.domain.MeasureUnit;
import co.edu.sena.repository.MeasureUnitRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.MeasureUnit}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MeasureUnitResource {

    private final Logger log = LoggerFactory.getLogger(MeasureUnitResource.class);

    private static final String ENTITY_NAME = "measureUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasureUnitRepository measureUnitRepository;

    public MeasureUnitResource(MeasureUnitRepository measureUnitRepository) {
        this.measureUnitRepository = measureUnitRepository;
    }

    /**
     * {@code POST  /measure-units} : Create a new measureUnit.
     *
     * @param measureUnit the measureUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measureUnit, or with status {@code 400 (Bad Request)} if the measureUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measure-units")
    public ResponseEntity<MeasureUnit> createMeasureUnit(@Valid @RequestBody MeasureUnit measureUnit) throws URISyntaxException {
        log.debug("REST request to save MeasureUnit : {}", measureUnit);
        if (measureUnit.getId() != null) {
            throw new BadRequestAlertException("A new measureUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeasureUnit result = measureUnitRepository.save(measureUnit);
        return ResponseEntity
            .created(new URI("/api/measure-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /measure-units/:id} : Updates an existing measureUnit.
     *
     * @param id the id of the measureUnit to save.
     * @param measureUnit the measureUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measureUnit,
     * or with status {@code 400 (Bad Request)} if the measureUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measureUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measure-units/{id}")
    public ResponseEntity<MeasureUnit> updateMeasureUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MeasureUnit measureUnit
    ) throws URISyntaxException {
        log.debug("REST request to update MeasureUnit : {}, {}", id, measureUnit);
        if (measureUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measureUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measureUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeasureUnit result = measureUnitRepository.save(measureUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measureUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /measure-units/:id} : Partial updates given fields of an existing measureUnit, field will ignore if it is null
     *
     * @param id the id of the measureUnit to save.
     * @param measureUnit the measureUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measureUnit,
     * or with status {@code 400 (Bad Request)} if the measureUnit is not valid,
     * or with status {@code 404 (Not Found)} if the measureUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the measureUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/measure-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeasureUnit> partialUpdateMeasureUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MeasureUnit measureUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeasureUnit partially : {}, {}", id, measureUnit);
        if (measureUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measureUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measureUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeasureUnit> result = measureUnitRepository
            .findById(measureUnit.getId())
            .map(existingMeasureUnit -> {
                if (measureUnit.getNameUnit() != null) {
                    existingMeasureUnit.setNameUnit(measureUnit.getNameUnit());
                }

                return existingMeasureUnit;
            })
            .map(measureUnitRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measureUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /measure-units} : get all the measureUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measureUnits in body.
     */
    @GetMapping("/measure-units")
    public List<MeasureUnit> getAllMeasureUnits() {
        log.debug("REST request to get all MeasureUnits");
        return measureUnitRepository.findAll();
    }

    /**
     * {@code GET  /measure-units/:id} : get the "id" measureUnit.
     *
     * @param id the id of the measureUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measureUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measure-units/{id}")
    public ResponseEntity<MeasureUnit> getMeasureUnit(@PathVariable Long id) {
        log.debug("REST request to get MeasureUnit : {}", id);
        Optional<MeasureUnit> measureUnit = measureUnitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(measureUnit);
    }

    /**
     * {@code DELETE  /measure-units/:id} : delete the "id" measureUnit.
     *
     * @param id the id of the measureUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measure-units/{id}")
    public ResponseEntity<Void> deleteMeasureUnit(@PathVariable Long id) {
        log.debug("REST request to delete MeasureUnit : {}", id);
        measureUnitRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
