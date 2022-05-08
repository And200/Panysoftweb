package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.DetailSale;
import co.edu.sena.domain.Presentation;
import co.edu.sena.domain.ProductElaborated;
import co.edu.sena.repository.DetailSaleRepository;
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
 * Integration tests for the {@link DetailSaleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DetailSaleResourceIT {

    private static final Integer DEFAULT_PRODUCT_AMOUNT = 1;
    private static final Integer UPDATED_PRODUCT_AMOUNT = 2;

    private static final String ENTITY_API_URL = "/api/detail-sales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailSaleRepository detailSaleRepository;

    @Mock
    private DetailSaleRepository detailSaleRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailSaleMockMvc;

    private DetailSale detailSale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailSale createEntity(EntityManager em) {
        DetailSale detailSale = new DetailSale().productAmount(DEFAULT_PRODUCT_AMOUNT);
        // Add required entity
        ProductElaborated productElaborated;
        if (TestUtil.findAll(em, ProductElaborated.class).isEmpty()) {
            productElaborated = ProductElaboratedResourceIT.createEntity(em);
            em.persist(productElaborated);
            em.flush();
        } else {
            productElaborated = TestUtil.findAll(em, ProductElaborated.class).get(0);
        }
        detailSale.setProductElaborated(productElaborated);
        // Add required entity
        Presentation presentation;
        if (TestUtil.findAll(em, Presentation.class).isEmpty()) {
            presentation = PresentationResourceIT.createEntity(em);
            em.persist(presentation);
            em.flush();
        } else {
            presentation = TestUtil.findAll(em, Presentation.class).get(0);
        }
        detailSale.setPresentation(presentation);
        return detailSale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailSale createUpdatedEntity(EntityManager em) {
        DetailSale detailSale = new DetailSale().productAmount(UPDATED_PRODUCT_AMOUNT);
        // Add required entity
        ProductElaborated productElaborated;
        if (TestUtil.findAll(em, ProductElaborated.class).isEmpty()) {
            productElaborated = ProductElaboratedResourceIT.createUpdatedEntity(em);
            em.persist(productElaborated);
            em.flush();
        } else {
            productElaborated = TestUtil.findAll(em, ProductElaborated.class).get(0);
        }
        detailSale.setProductElaborated(productElaborated);
        // Add required entity
        Presentation presentation;
        if (TestUtil.findAll(em, Presentation.class).isEmpty()) {
            presentation = PresentationResourceIT.createUpdatedEntity(em);
            em.persist(presentation);
            em.flush();
        } else {
            presentation = TestUtil.findAll(em, Presentation.class).get(0);
        }
        detailSale.setPresentation(presentation);
        return detailSale;
    }

    @BeforeEach
    public void initTest() {
        detailSale = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailSale() throws Exception {
        int databaseSizeBeforeCreate = detailSaleRepository.findAll().size();
        // Create the DetailSale
        restDetailSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailSale)))
            .andExpect(status().isCreated());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeCreate + 1);
        DetailSale testDetailSale = detailSaleList.get(detailSaleList.size() - 1);
        assertThat(testDetailSale.getProductAmount()).isEqualTo(DEFAULT_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void createDetailSaleWithExistingId() throws Exception {
        // Create the DetailSale with an existing ID
        detailSale.setId(1L);

        int databaseSizeBeforeCreate = detailSaleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailSale)))
            .andExpect(status().isBadRequest());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailSaleRepository.findAll().size();
        // set the field null
        detailSale.setProductAmount(null);

        // Create the DetailSale, which fails.

        restDetailSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailSale)))
            .andExpect(status().isBadRequest());

        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetailSales() throws Exception {
        // Initialize the database
        detailSaleRepository.saveAndFlush(detailSale);

        // Get all the detailSaleList
        restDetailSaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailSale.getId().intValue())))
            .andExpect(jsonPath("$.[*].productAmount").value(hasItem(DEFAULT_PRODUCT_AMOUNT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetailSalesWithEagerRelationshipsIsEnabled() throws Exception {
        when(detailSaleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDetailSaleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(detailSaleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetailSalesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(detailSaleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDetailSaleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(detailSaleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDetailSale() throws Exception {
        // Initialize the database
        detailSaleRepository.saveAndFlush(detailSale);

        // Get the detailSale
        restDetailSaleMockMvc
            .perform(get(ENTITY_API_URL_ID, detailSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailSale.getId().intValue()))
            .andExpect(jsonPath("$.productAmount").value(DEFAULT_PRODUCT_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingDetailSale() throws Exception {
        // Get the detailSale
        restDetailSaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailSale() throws Exception {
        // Initialize the database
        detailSaleRepository.saveAndFlush(detailSale);

        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();

        // Update the detailSale
        DetailSale updatedDetailSale = detailSaleRepository.findById(detailSale.getId()).get();
        // Disconnect from session so that the updates on updatedDetailSale are not directly saved in db
        em.detach(updatedDetailSale);
        updatedDetailSale.productAmount(UPDATED_PRODUCT_AMOUNT);

        restDetailSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetailSale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetailSale))
            )
            .andExpect(status().isOk());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
        DetailSale testDetailSale = detailSaleList.get(detailSaleList.size() - 1);
        assertThat(testDetailSale.getProductAmount()).isEqualTo(UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingDetailSale() throws Exception {
        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();
        detailSale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailSale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailSale() throws Exception {
        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();
        detailSale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailSale() throws Exception {
        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();
        detailSale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailSale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailSaleWithPatch() throws Exception {
        // Initialize the database
        detailSaleRepository.saveAndFlush(detailSale);

        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();

        // Update the detailSale using partial update
        DetailSale partialUpdatedDetailSale = new DetailSale();
        partialUpdatedDetailSale.setId(detailSale.getId());

        partialUpdatedDetailSale.productAmount(UPDATED_PRODUCT_AMOUNT);

        restDetailSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailSale))
            )
            .andExpect(status().isOk());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
        DetailSale testDetailSale = detailSaleList.get(detailSaleList.size() - 1);
        assertThat(testDetailSale.getProductAmount()).isEqualTo(UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateDetailSaleWithPatch() throws Exception {
        // Initialize the database
        detailSaleRepository.saveAndFlush(detailSale);

        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();

        // Update the detailSale using partial update
        DetailSale partialUpdatedDetailSale = new DetailSale();
        partialUpdatedDetailSale.setId(detailSale.getId());

        partialUpdatedDetailSale.productAmount(UPDATED_PRODUCT_AMOUNT);

        restDetailSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailSale))
            )
            .andExpect(status().isOk());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
        DetailSale testDetailSale = detailSaleList.get(detailSaleList.size() - 1);
        assertThat(testDetailSale.getProductAmount()).isEqualTo(UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingDetailSale() throws Exception {
        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();
        detailSale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailSale() throws Exception {
        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();
        detailSale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailSale() throws Exception {
        int databaseSizeBeforeUpdate = detailSaleRepository.findAll().size();
        detailSale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSaleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detailSale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailSale in the database
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailSale() throws Exception {
        // Initialize the database
        detailSaleRepository.saveAndFlush(detailSale);

        int databaseSizeBeforeDelete = detailSaleRepository.findAll().size();

        // Delete the detailSale
        restDetailSaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailSale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailSale> detailSaleList = detailSaleRepository.findAll();
        assertThat(detailSaleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
