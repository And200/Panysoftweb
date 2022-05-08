package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.MeasureUnit;
import co.edu.sena.domain.Presentation;
import co.edu.sena.repository.PresentationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PresentationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PresentationResourceIT {

    private static final String DEFAULT_PRESENTATION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/presentations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresentationRepository presentationRepository;

    @Mock
    private PresentationRepository presentationRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresentationMockMvc;

    private Presentation presentation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createEntity(EntityManager em) {
        Presentation presentation = new Presentation().presentation(DEFAULT_PRESENTATION);
        // Add required entity
        MeasureUnit measureUnit;
        if (TestUtil.findAll(em, MeasureUnit.class).isEmpty()) {
            measureUnit = MeasureUnitResourceIT.createEntity(em);
            em.persist(measureUnit);
            em.flush();
        } else {
            measureUnit = TestUtil.findAll(em, MeasureUnit.class).get(0);
        }
        presentation.setMeasureUnit(measureUnit);
        return presentation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createUpdatedEntity(EntityManager em) {
        Presentation presentation = new Presentation().presentation(UPDATED_PRESENTATION);
        // Add required entity
        MeasureUnit measureUnit;
        if (TestUtil.findAll(em, MeasureUnit.class).isEmpty()) {
            measureUnit = MeasureUnitResourceIT.createUpdatedEntity(em);
            em.persist(measureUnit);
            em.flush();
        } else {
            measureUnit = TestUtil.findAll(em, MeasureUnit.class).get(0);
        }
        presentation.setMeasureUnit(measureUnit);
        return presentation;
    }

    @BeforeEach
    public void initTest() {
        presentation = createEntity(em);
    }

    @Test
    @Transactional
    void createPresentation() throws Exception {
        int databaseSizeBeforeCreate = presentationRepository.findAll().size();
        // Create the Presentation
        restPresentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentation)))
            .andExpect(status().isCreated());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeCreate + 1);
        Presentation testPresentation = presentationList.get(presentationList.size() - 1);
        assertThat(testPresentation.getPresentation()).isEqualTo(DEFAULT_PRESENTATION);
    }

    @Test
    @Transactional
    void createPresentationWithExistingId() throws Exception {
        // Create the Presentation with an existing ID
        presentation.setId(1L);

        int databaseSizeBeforeCreate = presentationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentation)))
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPresentationIsRequired() throws Exception {
        int databaseSizeBeforeTest = presentationRepository.findAll().size();
        // set the field null
        presentation.setPresentation(null);

        // Create the Presentation, which fails.

        restPresentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentation)))
            .andExpect(status().isBadRequest());

        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPresentations() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        // Get all the presentationList
        restPresentationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPresentationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(presentationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPresentationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(presentationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPresentationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(presentationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPresentationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(presentationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPresentation() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        // Get the presentation
        restPresentationMockMvc
            .perform(get(ENTITY_API_URL_ID, presentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presentation.getId().intValue()))
            .andExpect(jsonPath("$.presentation").value(DEFAULT_PRESENTATION));
    }

    @Test
    @Transactional
    void getNonExistingPresentation() throws Exception {
        // Get the presentation
        restPresentationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPresentation() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();

        // Update the presentation
        Presentation updatedPresentation = presentationRepository.findById(presentation.getId()).get();
        // Disconnect from session so that the updates on updatedPresentation are not directly saved in db
        em.detach(updatedPresentation);
        updatedPresentation.presentation(UPDATED_PRESENTATION);

        restPresentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPresentation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPresentation))
            )
            .andExpect(status().isOk());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
        Presentation testPresentation = presentationList.get(presentationList.size() - 1);
        assertThat(testPresentation.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void putNonExistingPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();
        presentation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presentation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();
        presentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();
        presentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresentationWithPatch() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();

        // Update the presentation using partial update
        Presentation partialUpdatedPresentation = new Presentation();
        partialUpdatedPresentation.setId(presentation.getId());

        restPresentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresentation))
            )
            .andExpect(status().isOk());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
        Presentation testPresentation = presentationList.get(presentationList.size() - 1);
        assertThat(testPresentation.getPresentation()).isEqualTo(DEFAULT_PRESENTATION);
    }

    @Test
    @Transactional
    void fullUpdatePresentationWithPatch() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();

        // Update the presentation using partial update
        Presentation partialUpdatedPresentation = new Presentation();
        partialUpdatedPresentation.setId(presentation.getId());

        partialUpdatedPresentation.presentation(UPDATED_PRESENTATION);

        restPresentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresentation))
            )
            .andExpect(status().isOk());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
        Presentation testPresentation = presentationList.get(presentationList.size() - 1);
        assertThat(testPresentation.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void patchNonExistingPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();
        presentation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();
        presentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();
        presentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(presentation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresentation() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        int databaseSizeBeforeDelete = presentationRepository.findAll().size();

        // Delete the presentation
        restPresentationMockMvc
            .perform(delete(ENTITY_API_URL_ID, presentation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
