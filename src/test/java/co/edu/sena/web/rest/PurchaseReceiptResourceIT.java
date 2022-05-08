package co.edu.sena.web.rest;

import static co.edu.sena.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Client;
import co.edu.sena.domain.DetailSale;
import co.edu.sena.domain.Employee;
import co.edu.sena.domain.PurchaseReceipt;
import co.edu.sena.repository.PurchaseReceiptRepository;
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
 * Integration tests for the {@link PurchaseReceiptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchaseReceiptResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_TOTAL_SALE = 1D;
    private static final Double UPDATED_TOTAL_SALE = 2D;

    private static final String ENTITY_API_URL = "/api/purchase-receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseReceiptRepository purchaseReceiptRepository;

    @Mock
    private PurchaseReceiptRepository purchaseReceiptRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseReceiptMockMvc;

    private PurchaseReceipt purchaseReceipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseReceipt createEntity(EntityManager em) {
        PurchaseReceipt purchaseReceipt = new PurchaseReceipt().date(DEFAULT_DATE).totalSale(DEFAULT_TOTAL_SALE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        purchaseReceipt.setEmployee(employee);
        // Add required entity
        DetailSale detailSale;
        if (TestUtil.findAll(em, DetailSale.class).isEmpty()) {
            detailSale = DetailSaleResourceIT.createEntity(em);
            em.persist(detailSale);
            em.flush();
        } else {
            detailSale = TestUtil.findAll(em, DetailSale.class).get(0);
        }
        purchaseReceipt.setDetailSale(detailSale);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        purchaseReceipt.setClient(client);
        return purchaseReceipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseReceipt createUpdatedEntity(EntityManager em) {
        PurchaseReceipt purchaseReceipt = new PurchaseReceipt().date(UPDATED_DATE).totalSale(UPDATED_TOTAL_SALE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        purchaseReceipt.setEmployee(employee);
        // Add required entity
        DetailSale detailSale;
        if (TestUtil.findAll(em, DetailSale.class).isEmpty()) {
            detailSale = DetailSaleResourceIT.createUpdatedEntity(em);
            em.persist(detailSale);
            em.flush();
        } else {
            detailSale = TestUtil.findAll(em, DetailSale.class).get(0);
        }
        purchaseReceipt.setDetailSale(detailSale);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        purchaseReceipt.setClient(client);
        return purchaseReceipt;
    }

    @BeforeEach
    public void initTest() {
        purchaseReceipt = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseReceipt() throws Exception {
        int databaseSizeBeforeCreate = purchaseReceiptRepository.findAll().size();
        // Create the PurchaseReceipt
        restPurchaseReceiptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseReceipt testPurchaseReceipt = purchaseReceiptList.get(purchaseReceiptList.size() - 1);
        assertThat(testPurchaseReceipt.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPurchaseReceipt.getTotalSale()).isEqualTo(DEFAULT_TOTAL_SALE);
    }

    @Test
    @Transactional
    void createPurchaseReceiptWithExistingId() throws Exception {
        // Create the PurchaseReceipt with an existing ID
        purchaseReceipt.setId(1L);

        int databaseSizeBeforeCreate = purchaseReceiptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseReceiptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseReceiptRepository.findAll().size();
        // set the field null
        purchaseReceipt.setDate(null);

        // Create the PurchaseReceipt, which fails.

        restPurchaseReceiptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalSaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseReceiptRepository.findAll().size();
        // set the field null
        purchaseReceipt.setTotalSale(null);

        // Create the PurchaseReceipt, which fails.

        restPurchaseReceiptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchaseReceipts() throws Exception {
        // Initialize the database
        purchaseReceiptRepository.saveAndFlush(purchaseReceipt);

        // Get all the purchaseReceiptList
        restPurchaseReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].totalSale").value(hasItem(DEFAULT_TOTAL_SALE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseReceiptsWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchaseReceiptRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseReceiptRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseReceiptsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchaseReceiptRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseReceiptRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPurchaseReceipt() throws Exception {
        // Initialize the database
        purchaseReceiptRepository.saveAndFlush(purchaseReceipt);

        // Get the purchaseReceipt
        restPurchaseReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseReceipt.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.totalSale").value(DEFAULT_TOTAL_SALE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseReceipt() throws Exception {
        // Get the purchaseReceipt
        restPurchaseReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPurchaseReceipt() throws Exception {
        // Initialize the database
        purchaseReceiptRepository.saveAndFlush(purchaseReceipt);

        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();

        // Update the purchaseReceipt
        PurchaseReceipt updatedPurchaseReceipt = purchaseReceiptRepository.findById(purchaseReceipt.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseReceipt are not directly saved in db
        em.detach(updatedPurchaseReceipt);
        updatedPurchaseReceipt.date(UPDATED_DATE).totalSale(UPDATED_TOTAL_SALE);

        restPurchaseReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseReceipt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseReceipt))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
        PurchaseReceipt testPurchaseReceipt = purchaseReceiptList.get(purchaseReceiptList.size() - 1);
        assertThat(testPurchaseReceipt.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPurchaseReceipt.getTotalSale()).isEqualTo(UPDATED_TOTAL_SALE);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseReceipt() throws Exception {
        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();
        purchaseReceipt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseReceipt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseReceipt() throws Exception {
        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();
        purchaseReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseReceipt() throws Exception {
        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();
        purchaseReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseReceiptMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseReceiptWithPatch() throws Exception {
        // Initialize the database
        purchaseReceiptRepository.saveAndFlush(purchaseReceipt);

        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();

        // Update the purchaseReceipt using partial update
        PurchaseReceipt partialUpdatedPurchaseReceipt = new PurchaseReceipt();
        partialUpdatedPurchaseReceipt.setId(purchaseReceipt.getId());

        partialUpdatedPurchaseReceipt.date(UPDATED_DATE).totalSale(UPDATED_TOTAL_SALE);

        restPurchaseReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseReceipt))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
        PurchaseReceipt testPurchaseReceipt = purchaseReceiptList.get(purchaseReceiptList.size() - 1);
        assertThat(testPurchaseReceipt.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPurchaseReceipt.getTotalSale()).isEqualTo(UPDATED_TOTAL_SALE);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseReceiptWithPatch() throws Exception {
        // Initialize the database
        purchaseReceiptRepository.saveAndFlush(purchaseReceipt);

        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();

        // Update the purchaseReceipt using partial update
        PurchaseReceipt partialUpdatedPurchaseReceipt = new PurchaseReceipt();
        partialUpdatedPurchaseReceipt.setId(purchaseReceipt.getId());

        partialUpdatedPurchaseReceipt.date(UPDATED_DATE).totalSale(UPDATED_TOTAL_SALE);

        restPurchaseReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseReceipt))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
        PurchaseReceipt testPurchaseReceipt = purchaseReceiptList.get(purchaseReceiptList.size() - 1);
        assertThat(testPurchaseReceipt.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPurchaseReceipt.getTotalSale()).isEqualTo(UPDATED_TOTAL_SALE);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseReceipt() throws Exception {
        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();
        purchaseReceipt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseReceipt() throws Exception {
        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();
        purchaseReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseReceipt() throws Exception {
        int databaseSizeBeforeUpdate = purchaseReceiptRepository.findAll().size();
        purchaseReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseReceipt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseReceipt in the database
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseReceipt() throws Exception {
        // Initialize the database
        purchaseReceiptRepository.saveAndFlush(purchaseReceipt);

        int databaseSizeBeforeDelete = purchaseReceiptRepository.findAll().size();

        // Delete the purchaseReceipt
        restPurchaseReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseReceipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseReceipt> purchaseReceiptList = purchaseReceiptRepository.findAll();
        assertThat(purchaseReceiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
