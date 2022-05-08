package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Category;
import co.edu.sena.domain.Product;
import co.edu.sena.domain.Recip;
import co.edu.sena.repository.RecipRepository;
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
 * Integration tests for the {@link RecipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecipResourceIT {

    private static final String DEFAULT_NAME_RECIP = "AAAAAAAAAA";
    private static final String UPDATED_NAME_RECIP = "BBBBBBBBBB";

    private static final Double DEFAULT_ESTIMATED_PRICE_PREPARATION = 1D;
    private static final Double UPDATED_ESTIMATED_PRICE_PREPARATION = 2D;

    private static final Double DEFAULT_AMOUNT_PRODUCT_USED = 1D;
    private static final Double UPDATED_AMOUNT_PRODUCT_USED = 2D;

    private static final String ENTITY_API_URL = "/api/recips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecipRepository recipRepository;

    @Mock
    private RecipRepository recipRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipMockMvc;

    private Recip recip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recip createEntity(EntityManager em) {
        Recip recip = new Recip()
            .nameRecip(DEFAULT_NAME_RECIP)
            .estimatedPricePreparation(DEFAULT_ESTIMATED_PRICE_PREPARATION)
            .amountProductUsed(DEFAULT_AMOUNT_PRODUCT_USED);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        recip.setProduct(product);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        recip.setCategory(category);
        return recip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recip createUpdatedEntity(EntityManager em) {
        Recip recip = new Recip()
            .nameRecip(UPDATED_NAME_RECIP)
            .estimatedPricePreparation(UPDATED_ESTIMATED_PRICE_PREPARATION)
            .amountProductUsed(UPDATED_AMOUNT_PRODUCT_USED);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        recip.setProduct(product);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        recip.setCategory(category);
        return recip;
    }

    @BeforeEach
    public void initTest() {
        recip = createEntity(em);
    }

    @Test
    @Transactional
    void createRecip() throws Exception {
        int databaseSizeBeforeCreate = recipRepository.findAll().size();
        // Create the Recip
        restRecipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isCreated());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeCreate + 1);
        Recip testRecip = recipList.get(recipList.size() - 1);
        assertThat(testRecip.getNameRecip()).isEqualTo(DEFAULT_NAME_RECIP);
        assertThat(testRecip.getEstimatedPricePreparation()).isEqualTo(DEFAULT_ESTIMATED_PRICE_PREPARATION);
        assertThat(testRecip.getAmountProductUsed()).isEqualTo(DEFAULT_AMOUNT_PRODUCT_USED);
    }

    @Test
    @Transactional
    void createRecipWithExistingId() throws Exception {
        // Create the Recip with an existing ID
        recip.setId(1L);

        int databaseSizeBeforeCreate = recipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isBadRequest());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameRecipIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipRepository.findAll().size();
        // set the field null
        recip.setNameRecip(null);

        // Create the Recip, which fails.

        restRecipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isBadRequest());

        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstimatedPricePreparationIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipRepository.findAll().size();
        // set the field null
        recip.setEstimatedPricePreparation(null);

        // Create the Recip, which fails.

        restRecipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isBadRequest());

        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountProductUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipRepository.findAll().size();
        // set the field null
        recip.setAmountProductUsed(null);

        // Create the Recip, which fails.

        restRecipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isBadRequest());

        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecips() throws Exception {
        // Initialize the database
        recipRepository.saveAndFlush(recip);

        // Get all the recipList
        restRecipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recip.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameRecip").value(hasItem(DEFAULT_NAME_RECIP)))
            .andExpect(jsonPath("$.[*].estimatedPricePreparation").value(hasItem(DEFAULT_ESTIMATED_PRICE_PREPARATION.doubleValue())))
            .andExpect(jsonPath("$.[*].amountProductUsed").value(hasItem(DEFAULT_AMOUNT_PRODUCT_USED.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(recipRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recipRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recipRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recipRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRecip() throws Exception {
        // Initialize the database
        recipRepository.saveAndFlush(recip);

        // Get the recip
        restRecipMockMvc
            .perform(get(ENTITY_API_URL_ID, recip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recip.getId().intValue()))
            .andExpect(jsonPath("$.nameRecip").value(DEFAULT_NAME_RECIP))
            .andExpect(jsonPath("$.estimatedPricePreparation").value(DEFAULT_ESTIMATED_PRICE_PREPARATION.doubleValue()))
            .andExpect(jsonPath("$.amountProductUsed").value(DEFAULT_AMOUNT_PRODUCT_USED.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecip() throws Exception {
        // Get the recip
        restRecipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecip() throws Exception {
        // Initialize the database
        recipRepository.saveAndFlush(recip);

        int databaseSizeBeforeUpdate = recipRepository.findAll().size();

        // Update the recip
        Recip updatedRecip = recipRepository.findById(recip.getId()).get();
        // Disconnect from session so that the updates on updatedRecip are not directly saved in db
        em.detach(updatedRecip);
        updatedRecip
            .nameRecip(UPDATED_NAME_RECIP)
            .estimatedPricePreparation(UPDATED_ESTIMATED_PRICE_PREPARATION)
            .amountProductUsed(UPDATED_AMOUNT_PRODUCT_USED);

        restRecipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecip))
            )
            .andExpect(status().isOk());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
        Recip testRecip = recipList.get(recipList.size() - 1);
        assertThat(testRecip.getNameRecip()).isEqualTo(UPDATED_NAME_RECIP);
        assertThat(testRecip.getEstimatedPricePreparation()).isEqualTo(UPDATED_ESTIMATED_PRICE_PREPARATION);
        assertThat(testRecip.getAmountProductUsed()).isEqualTo(UPDATED_AMOUNT_PRODUCT_USED);
    }

    @Test
    @Transactional
    void putNonExistingRecip() throws Exception {
        int databaseSizeBeforeUpdate = recipRepository.findAll().size();
        recip.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecip() throws Exception {
        int databaseSizeBeforeUpdate = recipRepository.findAll().size();
        recip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecip() throws Exception {
        int databaseSizeBeforeUpdate = recipRepository.findAll().size();
        recip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipWithPatch() throws Exception {
        // Initialize the database
        recipRepository.saveAndFlush(recip);

        int databaseSizeBeforeUpdate = recipRepository.findAll().size();

        // Update the recip using partial update
        Recip partialUpdatedRecip = new Recip();
        partialUpdatedRecip.setId(recip.getId());

        partialUpdatedRecip.nameRecip(UPDATED_NAME_RECIP).amountProductUsed(UPDATED_AMOUNT_PRODUCT_USED);

        restRecipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecip))
            )
            .andExpect(status().isOk());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
        Recip testRecip = recipList.get(recipList.size() - 1);
        assertThat(testRecip.getNameRecip()).isEqualTo(UPDATED_NAME_RECIP);
        assertThat(testRecip.getEstimatedPricePreparation()).isEqualTo(DEFAULT_ESTIMATED_PRICE_PREPARATION);
        assertThat(testRecip.getAmountProductUsed()).isEqualTo(UPDATED_AMOUNT_PRODUCT_USED);
    }

    @Test
    @Transactional
    void fullUpdateRecipWithPatch() throws Exception {
        // Initialize the database
        recipRepository.saveAndFlush(recip);

        int databaseSizeBeforeUpdate = recipRepository.findAll().size();

        // Update the recip using partial update
        Recip partialUpdatedRecip = new Recip();
        partialUpdatedRecip.setId(recip.getId());

        partialUpdatedRecip
            .nameRecip(UPDATED_NAME_RECIP)
            .estimatedPricePreparation(UPDATED_ESTIMATED_PRICE_PREPARATION)
            .amountProductUsed(UPDATED_AMOUNT_PRODUCT_USED);

        restRecipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecip))
            )
            .andExpect(status().isOk());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
        Recip testRecip = recipList.get(recipList.size() - 1);
        assertThat(testRecip.getNameRecip()).isEqualTo(UPDATED_NAME_RECIP);
        assertThat(testRecip.getEstimatedPricePreparation()).isEqualTo(UPDATED_ESTIMATED_PRICE_PREPARATION);
        assertThat(testRecip.getAmountProductUsed()).isEqualTo(UPDATED_AMOUNT_PRODUCT_USED);
    }

    @Test
    @Transactional
    void patchNonExistingRecip() throws Exception {
        int databaseSizeBeforeUpdate = recipRepository.findAll().size();
        recip.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecip() throws Exception {
        int databaseSizeBeforeUpdate = recipRepository.findAll().size();
        recip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecip() throws Exception {
        int databaseSizeBeforeUpdate = recipRepository.findAll().size();
        recip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recip in the database
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecip() throws Exception {
        // Initialize the database
        recipRepository.saveAndFlush(recip);

        int databaseSizeBeforeDelete = recipRepository.findAll().size();

        // Delete the recip
        restRecipMockMvc
            .perform(delete(ENTITY_API_URL_ID, recip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recip> recipList = recipRepository.findAll();
        assertThat(recipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
