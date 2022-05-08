package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.DocumentType;
import co.edu.sena.domain.enumeration.StateDocument;
import co.edu.sena.repository.DocumentTypeRepository;
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
 * Integration tests for the {@link DocumentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTypeResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INITIALS = "AAAAAAAAAA";
    private static final String UPDATED_INITIALS = "BBBBBBBBBB";

    private static final StateDocument DEFAULT_STATE_DOCUMENT_TYPE = StateDocument.ACTIVE;
    private static final StateDocument UPDATED_STATE_DOCUMENT_TYPE = StateDocument.INACTIVE;

    private static final String ENTITY_API_URL = "/api/document-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTypeMockMvc;

    private DocumentType documentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createEntity(EntityManager em) {
        DocumentType documentType = new DocumentType()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .initials(DEFAULT_INITIALS)
            .stateDocumentType(DEFAULT_STATE_DOCUMENT_TYPE);
        return documentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createUpdatedEntity(EntityManager em) {
        DocumentType documentType = new DocumentType()
            .documentName(UPDATED_DOCUMENT_NAME)
            .initials(UPDATED_INITIALS)
            .stateDocumentType(UPDATED_STATE_DOCUMENT_TYPE);
        return documentType;
    }

    @BeforeEach
    public void initTest() {
        documentType = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentType() throws Exception {
        int databaseSizeBeforeCreate = documentTypeRepository.findAll().size();
        // Create the DocumentType
        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isCreated());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testDocumentType.getInitials()).isEqualTo(DEFAULT_INITIALS);
        assertThat(testDocumentType.getStateDocumentType()).isEqualTo(DEFAULT_STATE_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void createDocumentTypeWithExistingId() throws Exception {
        // Create the DocumentType with an existing ID
        documentType.setId(1L);

        int databaseSizeBeforeCreate = documentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentTypeRepository.findAll().size();
        // set the field null
        documentType.setDocumentName(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitialsIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentTypeRepository.findAll().size();
        // set the field null
        documentType.setInitials(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateDocumentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentTypeRepository.findAll().size();
        // set the field null
        documentType.setStateDocumentType(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentTypes() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS)))
            .andExpect(jsonPath("$.[*].stateDocumentType").value(hasItem(DEFAULT_STATE_DOCUMENT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getDocumentType() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get the documentType
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, documentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentType.getId().intValue()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.initials").value(DEFAULT_INITIALS))
            .andExpect(jsonPath("$.stateDocumentType").value(DEFAULT_STATE_DOCUMENT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentType() throws Exception {
        // Get the documentType
        restDocumentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentType() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();

        // Update the documentType
        DocumentType updatedDocumentType = documentTypeRepository.findById(documentType.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentType are not directly saved in db
        em.detach(updatedDocumentType);
        updatedDocumentType.documentName(UPDATED_DOCUMENT_NAME).initials(UPDATED_INITIALS).stateDocumentType(UPDATED_STATE_DOCUMENT_TYPE);

        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testDocumentType.getInitials()).isEqualTo(UPDATED_INITIALS);
        assertThat(testDocumentType.getStateDocumentType()).isEqualTo(UPDATED_STATE_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();
        documentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType.initials(UPDATED_INITIALS).stateDocumentType(UPDATED_STATE_DOCUMENT_TYPE);

        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testDocumentType.getInitials()).isEqualTo(UPDATED_INITIALS);
        assertThat(testDocumentType.getStateDocumentType()).isEqualTo(UPDATED_STATE_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType
            .documentName(UPDATED_DOCUMENT_NAME)
            .initials(UPDATED_INITIALS)
            .stateDocumentType(UPDATED_STATE_DOCUMENT_TYPE);

        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testDocumentType.getInitials()).isEqualTo(UPDATED_INITIALS);
        assertThat(testDocumentType.getStateDocumentType()).isEqualTo(UPDATED_STATE_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();
        documentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentType() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        int databaseSizeBeforeDelete = documentTypeRepository.findAll().size();

        // Delete the documentType
        restDocumentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
