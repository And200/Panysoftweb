package co.edu.sena.web.rest;

import static co.edu.sena.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.DetailOrder;
import co.edu.sena.domain.OrderPlaced;
import co.edu.sena.domain.Provider;
import co.edu.sena.repository.DetailOrderRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link DetailOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DetailOrderResourceIT {

    private static final Integer DEFAULT_QUANTITY_ORDERED = 1;
    private static final Integer UPDATED_QUANTITY_ORDERED = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_ORDERED = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ORDERED = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE_PAYMENT = 1D;
    private static final Double UPDATED_PRICE_PAYMENT = 2D;

    private static final String ENTITY_API_URL = "/api/detail-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailOrderRepository detailOrderRepository;

    @Mock
    private DetailOrderRepository detailOrderRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailOrderMockMvc;

    private DetailOrder detailOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailOrder createEntity(EntityManager em) {
        DetailOrder detailOrder = new DetailOrder()
            .quantityOrdered(DEFAULT_QUANTITY_ORDERED)
            .date(DEFAULT_DATE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .productOrdered(DEFAULT_PRODUCT_ORDERED)
            .pricePayment(DEFAULT_PRICE_PAYMENT);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        detailOrder.setProvider(provider);
        // Add required entity
        OrderPlaced orderPlaced;
        if (TestUtil.findAll(em, OrderPlaced.class).isEmpty()) {
            orderPlaced = OrderPlacedResourceIT.createEntity(em);
            em.persist(orderPlaced);
            em.flush();
        } else {
            orderPlaced = TestUtil.findAll(em, OrderPlaced.class).get(0);
        }
        detailOrder.setOrderPlaced(orderPlaced);
        return detailOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailOrder createUpdatedEntity(EntityManager em) {
        DetailOrder detailOrder = new DetailOrder()
            .quantityOrdered(UPDATED_QUANTITY_ORDERED)
            .date(UPDATED_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .productOrdered(UPDATED_PRODUCT_ORDERED)
            .pricePayment(UPDATED_PRICE_PAYMENT);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createUpdatedEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        detailOrder.setProvider(provider);
        // Add required entity
        OrderPlaced orderPlaced;
        if (TestUtil.findAll(em, OrderPlaced.class).isEmpty()) {
            orderPlaced = OrderPlacedResourceIT.createUpdatedEntity(em);
            em.persist(orderPlaced);
            em.flush();
        } else {
            orderPlaced = TestUtil.findAll(em, OrderPlaced.class).get(0);
        }
        detailOrder.setOrderPlaced(orderPlaced);
        return detailOrder;
    }

    @BeforeEach
    public void initTest() {
        detailOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailOrder() throws Exception {
        int databaseSizeBeforeCreate = detailOrderRepository.findAll().size();
        // Create the DetailOrder
        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isCreated());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeCreate + 1);
        DetailOrder testDetailOrder = detailOrderList.get(detailOrderList.size() - 1);
        assertThat(testDetailOrder.getQuantityOrdered()).isEqualTo(DEFAULT_QUANTITY_ORDERED);
        assertThat(testDetailOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDetailOrder.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testDetailOrder.getProductOrdered()).isEqualTo(DEFAULT_PRODUCT_ORDERED);
        assertThat(testDetailOrder.getPricePayment()).isEqualTo(DEFAULT_PRICE_PAYMENT);
    }

    @Test
    @Transactional
    void createDetailOrderWithExistingId() throws Exception {
        // Create the DetailOrder with an existing ID
        detailOrder.setId(1L);

        int databaseSizeBeforeCreate = detailOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isBadRequest());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityOrderedIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailOrderRepository.findAll().size();
        // set the field null
        detailOrder.setQuantityOrdered(null);

        // Create the DetailOrder, which fails.

        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isBadRequest());

        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailOrderRepository.findAll().size();
        // set the field null
        detailOrder.setDate(null);

        // Create the DetailOrder, which fails.

        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isBadRequest());

        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailOrderRepository.findAll().size();
        // set the field null
        detailOrder.setInvoiceNumber(null);

        // Create the DetailOrder, which fails.

        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isBadRequest());

        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductOrderedIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailOrderRepository.findAll().size();
        // set the field null
        detailOrder.setProductOrdered(null);

        // Create the DetailOrder, which fails.

        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isBadRequest());

        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailOrderRepository.findAll().size();
        // set the field null
        detailOrder.setPricePayment(null);

        // Create the DetailOrder, which fails.

        restDetailOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isBadRequest());

        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetailOrders() throws Exception {
        // Initialize the database
        detailOrderRepository.saveAndFlush(detailOrder);

        // Get all the detailOrderList
        restDetailOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityOrdered").value(hasItem(DEFAULT_QUANTITY_ORDERED)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].productOrdered").value(hasItem(DEFAULT_PRODUCT_ORDERED)))
            .andExpect(jsonPath("$.[*].pricePayment").value(hasItem(DEFAULT_PRICE_PAYMENT.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetailOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(detailOrderRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDetailOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(detailOrderRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetailOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(detailOrderRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDetailOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(detailOrderRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDetailOrder() throws Exception {
        // Initialize the database
        detailOrderRepository.saveAndFlush(detailOrder);

        // Get the detailOrder
        restDetailOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, detailOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailOrder.getId().intValue()))
            .andExpect(jsonPath("$.quantityOrdered").value(DEFAULT_QUANTITY_ORDERED))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.productOrdered").value(DEFAULT_PRODUCT_ORDERED))
            .andExpect(jsonPath("$.pricePayment").value(DEFAULT_PRICE_PAYMENT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDetailOrder() throws Exception {
        // Get the detailOrder
        restDetailOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailOrder() throws Exception {
        // Initialize the database
        detailOrderRepository.saveAndFlush(detailOrder);

        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();

        // Update the detailOrder
        DetailOrder updatedDetailOrder = detailOrderRepository.findById(detailOrder.getId()).get();
        // Disconnect from session so that the updates on updatedDetailOrder are not directly saved in db
        em.detach(updatedDetailOrder);
        updatedDetailOrder
            .quantityOrdered(UPDATED_QUANTITY_ORDERED)
            .date(UPDATED_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .productOrdered(UPDATED_PRODUCT_ORDERED)
            .pricePayment(UPDATED_PRICE_PAYMENT);

        restDetailOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetailOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetailOrder))
            )
            .andExpect(status().isOk());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
        DetailOrder testDetailOrder = detailOrderList.get(detailOrderList.size() - 1);
        assertThat(testDetailOrder.getQuantityOrdered()).isEqualTo(UPDATED_QUANTITY_ORDERED);
        assertThat(testDetailOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDetailOrder.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testDetailOrder.getProductOrdered()).isEqualTo(UPDATED_PRODUCT_ORDERED);
        assertThat(testDetailOrder.getPricePayment()).isEqualTo(UPDATED_PRICE_PAYMENT);
    }

    @Test
    @Transactional
    void putNonExistingDetailOrder() throws Exception {
        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();
        detailOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailOrder() throws Exception {
        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();
        detailOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailOrder() throws Exception {
        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();
        detailOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailOrderWithPatch() throws Exception {
        // Initialize the database
        detailOrderRepository.saveAndFlush(detailOrder);

        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();

        // Update the detailOrder using partial update
        DetailOrder partialUpdatedDetailOrder = new DetailOrder();
        partialUpdatedDetailOrder.setId(detailOrder.getId());

        partialUpdatedDetailOrder.quantityOrdered(UPDATED_QUANTITY_ORDERED).date(UPDATED_DATE).pricePayment(UPDATED_PRICE_PAYMENT);

        restDetailOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailOrder))
            )
            .andExpect(status().isOk());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
        DetailOrder testDetailOrder = detailOrderList.get(detailOrderList.size() - 1);
        assertThat(testDetailOrder.getQuantityOrdered()).isEqualTo(UPDATED_QUANTITY_ORDERED);
        assertThat(testDetailOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDetailOrder.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testDetailOrder.getProductOrdered()).isEqualTo(DEFAULT_PRODUCT_ORDERED);
        assertThat(testDetailOrder.getPricePayment()).isEqualTo(UPDATED_PRICE_PAYMENT);
    }

    @Test
    @Transactional
    void fullUpdateDetailOrderWithPatch() throws Exception {
        // Initialize the database
        detailOrderRepository.saveAndFlush(detailOrder);

        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();

        // Update the detailOrder using partial update
        DetailOrder partialUpdatedDetailOrder = new DetailOrder();
        partialUpdatedDetailOrder.setId(detailOrder.getId());

        partialUpdatedDetailOrder
            .quantityOrdered(UPDATED_QUANTITY_ORDERED)
            .date(UPDATED_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .productOrdered(UPDATED_PRODUCT_ORDERED)
            .pricePayment(UPDATED_PRICE_PAYMENT);

        restDetailOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailOrder))
            )
            .andExpect(status().isOk());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
        DetailOrder testDetailOrder = detailOrderList.get(detailOrderList.size() - 1);
        assertThat(testDetailOrder.getQuantityOrdered()).isEqualTo(UPDATED_QUANTITY_ORDERED);
        assertThat(testDetailOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDetailOrder.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testDetailOrder.getProductOrdered()).isEqualTo(UPDATED_PRODUCT_ORDERED);
        assertThat(testDetailOrder.getPricePayment()).isEqualTo(UPDATED_PRICE_PAYMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDetailOrder() throws Exception {
        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();
        detailOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailOrder() throws Exception {
        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();
        detailOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailOrder() throws Exception {
        int databaseSizeBeforeUpdate = detailOrderRepository.findAll().size();
        detailOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detailOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailOrder in the database
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailOrder() throws Exception {
        // Initialize the database
        detailOrderRepository.saveAndFlush(detailOrder);

        int databaseSizeBeforeDelete = detailOrderRepository.findAll().size();

        // Delete the detailOrder
        restDetailOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailOrder> detailOrderList = detailOrderRepository.findAll();
        assertThat(detailOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
