package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.MeasureUnit;
import co.edu.sena.repository.MeasureUnitRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MeasureUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeasureUnitResourceIT {

    private static final String DEFAULT_NAME_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_UNIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/measure-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeasureUnitRepository measureUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasureUnitMockMvc;

    private MeasureUnit measureUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasureUnit createEntity(EntityManager em) {
        MeasureUnit measureUnit = new MeasureUnit().nameUnit(DEFAULT_NAME_UNIT);
        return measureUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasureUnit createUpdatedEntity(EntityManager em) {
        MeasureUnit measureUnit = new MeasureUnit().nameUnit(UPDATED_NAME_UNIT);
        return measureUnit;
    }

    @BeforeEach
    public void initTest() {
        measureUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createMeasureUnit() throws Exception {
        int databaseSizeBeforeCreate = measureUnitRepository.findAll().size();
        // Create the MeasureUnit
        restMeasureUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureUnit)))
            .andExpect(status().isCreated());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeCreate + 1);
        MeasureUnit testMeasureUnit = measureUnitList.get(measureUnitList.size() - 1);
        assertThat(testMeasureUnit.getNameUnit()).isEqualTo(DEFAULT_NAME_UNIT);
    }

    @Test
    @Transactional
    void createMeasureUnitWithExistingId() throws Exception {
        // Create the MeasureUnit with an existing ID
        measureUnit.setId(1L);

        int databaseSizeBeforeCreate = measureUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasureUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureUnit)))
            .andExpect(status().isBadRequest());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = measureUnitRepository.findAll().size();
        // set the field null
        measureUnit.setNameUnit(null);

        // Create the MeasureUnit, which fails.

        restMeasureUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureUnit)))
            .andExpect(status().isBadRequest());

        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeasureUnits() throws Exception {
        // Initialize the database
        measureUnitRepository.saveAndFlush(measureUnit);

        // Get all the measureUnitList
        restMeasureUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measureUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameUnit").value(hasItem(DEFAULT_NAME_UNIT)));
    }

    @Test
    @Transactional
    void getMeasureUnit() throws Exception {
        // Initialize the database
        measureUnitRepository.saveAndFlush(measureUnit);

        // Get the measureUnit
        restMeasureUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, measureUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measureUnit.getId().intValue()))
            .andExpect(jsonPath("$.nameUnit").value(DEFAULT_NAME_UNIT));
    }

    @Test
    @Transactional
    void getNonExistingMeasureUnit() throws Exception {
        // Get the measureUnit
        restMeasureUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeasureUnit() throws Exception {
        // Initialize the database
        measureUnitRepository.saveAndFlush(measureUnit);

        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();

        // Update the measureUnit
        MeasureUnit updatedMeasureUnit = measureUnitRepository.findById(measureUnit.getId()).get();
        // Disconnect from session so that the updates on updatedMeasureUnit are not directly saved in db
        em.detach(updatedMeasureUnit);
        updatedMeasureUnit.nameUnit(UPDATED_NAME_UNIT);

        restMeasureUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeasureUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeasureUnit))
            )
            .andExpect(status().isOk());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
        MeasureUnit testMeasureUnit = measureUnitList.get(measureUnitList.size() - 1);
        assertThat(testMeasureUnit.getNameUnit()).isEqualTo(UPDATED_NAME_UNIT);
    }

    @Test
    @Transactional
    void putNonExistingMeasureUnit() throws Exception {
        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();
        measureUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measureUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measureUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeasureUnit() throws Exception {
        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();
        measureUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measureUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeasureUnit() throws Exception {
        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();
        measureUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeasureUnitWithPatch() throws Exception {
        // Initialize the database
        measureUnitRepository.saveAndFlush(measureUnit);

        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();

        // Update the measureUnit using partial update
        MeasureUnit partialUpdatedMeasureUnit = new MeasureUnit();
        partialUpdatedMeasureUnit.setId(measureUnit.getId());

        partialUpdatedMeasureUnit.nameUnit(UPDATED_NAME_UNIT);

        restMeasureUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasureUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasureUnit))
            )
            .andExpect(status().isOk());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
        MeasureUnit testMeasureUnit = measureUnitList.get(measureUnitList.size() - 1);
        assertThat(testMeasureUnit.getNameUnit()).isEqualTo(UPDATED_NAME_UNIT);
    }

    @Test
    @Transactional
    void fullUpdateMeasureUnitWithPatch() throws Exception {
        // Initialize the database
        measureUnitRepository.saveAndFlush(measureUnit);

        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();

        // Update the measureUnit using partial update
        MeasureUnit partialUpdatedMeasureUnit = new MeasureUnit();
        partialUpdatedMeasureUnit.setId(measureUnit.getId());

        partialUpdatedMeasureUnit.nameUnit(UPDATED_NAME_UNIT);

        restMeasureUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasureUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasureUnit))
            )
            .andExpect(status().isOk());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
        MeasureUnit testMeasureUnit = measureUnitList.get(measureUnitList.size() - 1);
        assertThat(testMeasureUnit.getNameUnit()).isEqualTo(UPDATED_NAME_UNIT);
    }

    @Test
    @Transactional
    void patchNonExistingMeasureUnit() throws Exception {
        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();
        measureUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, measureUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measureUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeasureUnit() throws Exception {
        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();
        measureUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measureUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeasureUnit() throws Exception {
        int databaseSizeBeforeUpdate = measureUnitRepository.findAll().size();
        measureUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(measureUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasureUnit in the database
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeasureUnit() throws Exception {
        // Initialize the database
        measureUnitRepository.saveAndFlush(measureUnit);

        int databaseSizeBeforeDelete = measureUnitRepository.findAll().size();

        // Delete the measureUnit
        restMeasureUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, measureUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeasureUnit> measureUnitList = measureUnitRepository.findAll();
        assertThat(measureUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
