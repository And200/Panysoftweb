package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Category;
import co.edu.sena.domain.ProductElaborated;
import co.edu.sena.repository.ProductElaboratedRepository;
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
 * Integration tests for the {@link ProductElaboratedResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductElaboratedResourceIT {

    private static final Integer DEFAULT_AMOUNT_PRODUCED = 1;
    private static final Integer UPDATED_AMOUNT_PRODUCED = 2;

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_BUY_PRICE = 1D;
    private static final Double UPDATED_BUY_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/product-elaborateds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductElaboratedRepository productElaboratedRepository;

    @Mock
    private ProductElaboratedRepository productElaboratedRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductElaboratedMockMvc;

    private ProductElaborated productElaborated;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductElaborated createEntity(EntityManager em) {
        ProductElaborated productElaborated = new ProductElaborated()
            .amountProduced(DEFAULT_AMOUNT_PRODUCED)
            .productName(DEFAULT_PRODUCT_NAME)
            .buyPrice(DEFAULT_BUY_PRICE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        productElaborated.setCategory(category);
        return productElaborated;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductElaborated createUpdatedEntity(EntityManager em) {
        ProductElaborated productElaborated = new ProductElaborated()
            .amountProduced(UPDATED_AMOUNT_PRODUCED)
            .productName(UPDATED_PRODUCT_NAME)
            .buyPrice(UPDATED_BUY_PRICE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        productElaborated.setCategory(category);
        return productElaborated;
    }

    @BeforeEach
    public void initTest() {
        productElaborated = createEntity(em);
    }

    @Test
    @Transactional
    void createProductElaborated() throws Exception {
        int databaseSizeBeforeCreate = productElaboratedRepository.findAll().size();
        // Create the ProductElaborated
        restProductElaboratedMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isCreated());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeCreate + 1);
        ProductElaborated testProductElaborated = productElaboratedList.get(productElaboratedList.size() - 1);
        assertThat(testProductElaborated.getAmountProduced()).isEqualTo(DEFAULT_AMOUNT_PRODUCED);
        assertThat(testProductElaborated.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProductElaborated.getBuyPrice()).isEqualTo(DEFAULT_BUY_PRICE);
    }

    @Test
    @Transactional
    void createProductElaboratedWithExistingId() throws Exception {
        // Create the ProductElaborated with an existing ID
        productElaborated.setId(1L);

        int databaseSizeBeforeCreate = productElaboratedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductElaboratedMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountProducedIsRequired() throws Exception {
        int databaseSizeBeforeTest = productElaboratedRepository.findAll().size();
        // set the field null
        productElaborated.setAmountProduced(null);

        // Create the ProductElaborated, which fails.

        restProductElaboratedMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productElaboratedRepository.findAll().size();
        // set the field null
        productElaborated.setProductName(null);

        // Create the ProductElaborated, which fails.

        restProductElaboratedMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBuyPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productElaboratedRepository.findAll().size();
        // set the field null
        productElaborated.setBuyPrice(null);

        // Create the ProductElaborated, which fails.

        restProductElaboratedMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductElaborateds() throws Exception {
        // Initialize the database
        productElaboratedRepository.saveAndFlush(productElaborated);

        // Get all the productElaboratedList
        restProductElaboratedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productElaborated.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountProduced").value(hasItem(DEFAULT_AMOUNT_PRODUCED)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].buyPrice").value(hasItem(DEFAULT_BUY_PRICE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductElaboratedsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productElaboratedRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductElaboratedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productElaboratedRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductElaboratedsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productElaboratedRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductElaboratedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productElaboratedRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProductElaborated() throws Exception {
        // Initialize the database
        productElaboratedRepository.saveAndFlush(productElaborated);

        // Get the productElaborated
        restProductElaboratedMockMvc
            .perform(get(ENTITY_API_URL_ID, productElaborated.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productElaborated.getId().intValue()))
            .andExpect(jsonPath("$.amountProduced").value(DEFAULT_AMOUNT_PRODUCED))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.buyPrice").value(DEFAULT_BUY_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductElaborated() throws Exception {
        // Get the productElaborated
        restProductElaboratedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductElaborated() throws Exception {
        // Initialize the database
        productElaboratedRepository.saveAndFlush(productElaborated);

        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();

        // Update the productElaborated
        ProductElaborated updatedProductElaborated = productElaboratedRepository.findById(productElaborated.getId()).get();
        // Disconnect from session so that the updates on updatedProductElaborated are not directly saved in db
        em.detach(updatedProductElaborated);
        updatedProductElaborated.amountProduced(UPDATED_AMOUNT_PRODUCED).productName(UPDATED_PRODUCT_NAME).buyPrice(UPDATED_BUY_PRICE);

        restProductElaboratedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductElaborated.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductElaborated))
            )
            .andExpect(status().isOk());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
        ProductElaborated testProductElaborated = productElaboratedList.get(productElaboratedList.size() - 1);
        assertThat(testProductElaborated.getAmountProduced()).isEqualTo(UPDATED_AMOUNT_PRODUCED);
        assertThat(testProductElaborated.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProductElaborated.getBuyPrice()).isEqualTo(UPDATED_BUY_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingProductElaborated() throws Exception {
        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();
        productElaborated.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductElaboratedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productElaborated.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductElaborated() throws Exception {
        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();
        productElaborated.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductElaboratedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductElaborated() throws Exception {
        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();
        productElaborated.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductElaboratedMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductElaboratedWithPatch() throws Exception {
        // Initialize the database
        productElaboratedRepository.saveAndFlush(productElaborated);

        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();

        // Update the productElaborated using partial update
        ProductElaborated partialUpdatedProductElaborated = new ProductElaborated();
        partialUpdatedProductElaborated.setId(productElaborated.getId());

        restProductElaboratedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductElaborated.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductElaborated))
            )
            .andExpect(status().isOk());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
        ProductElaborated testProductElaborated = productElaboratedList.get(productElaboratedList.size() - 1);
        assertThat(testProductElaborated.getAmountProduced()).isEqualTo(DEFAULT_AMOUNT_PRODUCED);
        assertThat(testProductElaborated.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProductElaborated.getBuyPrice()).isEqualTo(DEFAULT_BUY_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateProductElaboratedWithPatch() throws Exception {
        // Initialize the database
        productElaboratedRepository.saveAndFlush(productElaborated);

        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();

        // Update the productElaborated using partial update
        ProductElaborated partialUpdatedProductElaborated = new ProductElaborated();
        partialUpdatedProductElaborated.setId(productElaborated.getId());

        partialUpdatedProductElaborated
            .amountProduced(UPDATED_AMOUNT_PRODUCED)
            .productName(UPDATED_PRODUCT_NAME)
            .buyPrice(UPDATED_BUY_PRICE);

        restProductElaboratedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductElaborated.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductElaborated))
            )
            .andExpect(status().isOk());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
        ProductElaborated testProductElaborated = productElaboratedList.get(productElaboratedList.size() - 1);
        assertThat(testProductElaborated.getAmountProduced()).isEqualTo(UPDATED_AMOUNT_PRODUCED);
        assertThat(testProductElaborated.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProductElaborated.getBuyPrice()).isEqualTo(UPDATED_BUY_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingProductElaborated() throws Exception {
        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();
        productElaborated.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductElaboratedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productElaborated.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductElaborated() throws Exception {
        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();
        productElaborated.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductElaboratedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductElaborated() throws Exception {
        int databaseSizeBeforeUpdate = productElaboratedRepository.findAll().size();
        productElaborated.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductElaboratedMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productElaborated))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductElaborated in the database
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductElaborated() throws Exception {
        // Initialize the database
        productElaboratedRepository.saveAndFlush(productElaborated);

        int databaseSizeBeforeDelete = productElaboratedRepository.findAll().size();

        // Delete the productElaborated
        restProductElaboratedMockMvc
            .perform(delete(ENTITY_API_URL_ID, productElaborated.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductElaborated> productElaboratedList = productElaboratedRepository.findAll();
        assertThat(productElaboratedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
