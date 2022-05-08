package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailOrder.class);
        DetailOrder detailOrder1 = new DetailOrder();
        detailOrder1.setId(1L);
        DetailOrder detailOrder2 = new DetailOrder();
        detailOrder2.setId(detailOrder1.getId());
        assertThat(detailOrder1).isEqualTo(detailOrder2);
        detailOrder2.setId(2L);
        assertThat(detailOrder1).isNotEqualTo(detailOrder2);
        detailOrder1.setId(null);
        assertThat(detailOrder1).isNotEqualTo(detailOrder2);
    }
}
