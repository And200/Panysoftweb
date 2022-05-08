package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.OrderPlaced;
import co.edu.sena.domain.enumeration.StateOrder;
import co.edu.sena.repository.OrderPlacedRepository;
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
 * Integration tests for the {@link OrderPlacedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderPlacedResourceIT {

    private static final StateOrder DEFAULT_ORDER_PLACED_STATE = StateOrder.DELIVERED;
    private static final StateOrder UPDATED_ORDER_PLACED_STATE = StateOrder.UNDELIVERED;

    private static final String ENTITY_API_URL = "/api/order-placeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderPlacedRepository orderPlacedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderPlacedMockMvc;

    private OrderPlaced orderPlaced;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderPlaced createEntity(EntityManager em) {
        OrderPlaced orderPlaced = new OrderPlaced().orderPlacedState(DEFAULT_ORDER_PLACED_STATE);
        return orderPlaced;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderPlaced createUpdatedEntity(EntityManager em) {
        OrderPlaced orderPlaced = new OrderPlaced().orderPlacedState(UPDATED_ORDER_PLACED_STATE);
        return orderPlaced;
    }

    @BeforeEach
    public void initTest() {
        orderPlaced = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderPlaced() throws Exception {
        int databaseSizeBeforeCreate = orderPlacedRepository.findAll().size();
        // Create the OrderPlaced
        restOrderPlacedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderPlaced)))
            .andExpect(status().isCreated());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeCreate + 1);
        OrderPlaced testOrderPlaced = orderPlacedList.get(orderPlacedList.size() - 1);
        assertThat(testOrderPlaced.getOrderPlacedState()).isEqualTo(DEFAULT_ORDER_PLACED_STATE);
    }

    @Test
    @Transactional
    void createOrderPlacedWithExistingId() throws Exception {
        // Create the OrderPlaced with an existing ID
        orderPlaced.setId(1L);

        int databaseSizeBeforeCreate = orderPlacedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderPlacedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderPlaced)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderPlacedStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderPlacedRepository.findAll().size();
        // set the field null
        orderPlaced.setOrderPlacedState(null);

        // Create the OrderPlaced, which fails.

        restOrderPlacedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderPlaced)))
            .andExpect(status().isBadRequest());

        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderPlaceds() throws Exception {
        // Initialize the database
        orderPlacedRepository.saveAndFlush(orderPlaced);

        // Get all the orderPlacedList
        restOrderPlacedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderPlaced.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderPlacedState").value(hasItem(DEFAULT_ORDER_PLACED_STATE.toString())));
    }

    @Test
    @Transactional
    void getOrderPlaced() throws Exception {
        // Initialize the database
        orderPlacedRepository.saveAndFlush(orderPlaced);

        // Get the orderPlaced
        restOrderPlacedMockMvc
            .perform(get(ENTITY_API_URL_ID, orderPlaced.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderPlaced.getId().intValue()))
            .andExpect(jsonPath("$.orderPlacedState").value(DEFAULT_ORDER_PLACED_STATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderPlaced() throws Exception {
        // Get the orderPlaced
        restOrderPlacedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderPlaced() throws Exception {
        // Initialize the database
        orderPlacedRepository.saveAndFlush(orderPlaced);

        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();

        // Update the orderPlaced
        OrderPlaced updatedOrderPlaced = orderPlacedRepository.findById(orderPlaced.getId()).get();
        // Disconnect from session so that the updates on updatedOrderPlaced are not directly saved in db
        em.detach(updatedOrderPlaced);
        updatedOrderPlaced.orderPlacedState(UPDATED_ORDER_PLACED_STATE);

        restOrderPlacedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderPlaced.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderPlaced))
            )
            .andExpect(status().isOk());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
        OrderPlaced testOrderPlaced = orderPlacedList.get(orderPlacedList.size() - 1);
        assertThat(testOrderPlaced.getOrderPlacedState()).isEqualTo(UPDATED_ORDER_PLACED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingOrderPlaced() throws Exception {
        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();
        orderPlaced.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderPlacedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderPlaced.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderPlaced))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderPlaced() throws Exception {
        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();
        orderPlaced.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderPlacedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderPlaced))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderPlaced() throws Exception {
        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();
        orderPlaced.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderPlacedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderPlaced)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderPlacedWithPatch() throws Exception {
        // Initialize the database
        orderPlacedRepository.saveAndFlush(orderPlaced);

        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();

        // Update the orderPlaced using partial update
        OrderPlaced partialUpdatedOrderPlaced = new OrderPlaced();
        partialUpdatedOrderPlaced.setId(orderPlaced.getId());

        partialUpdatedOrderPlaced.orderPlacedState(UPDATED_ORDER_PLACED_STATE);

        restOrderPlacedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderPlaced.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderPlaced))
            )
            .andExpect(status().isOk());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
        OrderPlaced testOrderPlaced = orderPlacedList.get(orderPlacedList.size() - 1);
        assertThat(testOrderPlaced.getOrderPlacedState()).isEqualTo(UPDATED_ORDER_PLACED_STATE);
    }

    @Test
    @Transactional
    void fullUpdateOrderPlacedWithPatch() throws Exception {
        // Initialize the database
        orderPlacedRepository.saveAndFlush(orderPlaced);

        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();

        // Update the orderPlaced using partial update
        OrderPlaced partialUpdatedOrderPlaced = new OrderPlaced();
        partialUpdatedOrderPlaced.setId(orderPlaced.getId());

        partialUpdatedOrderPlaced.orderPlacedState(UPDATED_ORDER_PLACED_STATE);

        restOrderPlacedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderPlaced.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderPlaced))
            )
            .andExpect(status().isOk());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
        OrderPlaced testOrderPlaced = orderPlacedList.get(orderPlacedList.size() - 1);
        assertThat(testOrderPlaced.getOrderPlacedState()).isEqualTo(UPDATED_ORDER_PLACED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderPlaced() throws Exception {
        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();
        orderPlaced.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderPlacedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderPlaced.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderPlaced))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderPlaced() throws Exception {
        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();
        orderPlaced.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderPlacedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderPlaced))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderPlaced() throws Exception {
        int databaseSizeBeforeUpdate = orderPlacedRepository.findAll().size();
        orderPlaced.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderPlacedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderPlaced))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderPlaced in the database
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderPlaced() throws Exception {
        // Initialize the database
        orderPlacedRepository.saveAndFlush(orderPlaced);

        int databaseSizeBeforeDelete = orderPlacedRepository.findAll().size();

        // Delete the orderPlaced
        restOrderPlacedMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderPlaced.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderPlaced> orderPlacedList = orderPlacedRepository.findAll();
        assertThat(orderPlacedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
