package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailSaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailSale.class);
        DetailSale detailSale1 = new DetailSale();
        detailSale1.setId(1L);
        DetailSale detailSale2 = new DetailSale();
        detailSale2.setId(detailSale1.getId());
        assertThat(detailSale1).isEqualTo(detailSale2);
        detailSale2.setId(2L);
        assertThat(detailSale1).isNotEqualTo(detailSale2);
        detailSale1.setId(null);
        assertThat(detailSale1).isNotEqualTo(detailSale2);
    }
}
