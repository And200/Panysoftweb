package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderPlacedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderPlaced.class);
        OrderPlaced orderPlaced1 = new OrderPlaced();
        orderPlaced1.setId(1L);
        OrderPlaced orderPlaced2 = new OrderPlaced();
        orderPlaced2.setId(orderPlaced1.getId());
        assertThat(orderPlaced1).isEqualTo(orderPlaced2);
        orderPlaced2.setId(2L);
        assertThat(orderPlaced1).isNotEqualTo(orderPlaced2);
        orderPlaced1.setId(null);
        assertThat(orderPlaced1).isNotEqualTo(orderPlaced2);
    }
}
