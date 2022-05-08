package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductElaboratedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductElaborated.class);
        ProductElaborated productElaborated1 = new ProductElaborated();
        productElaborated1.setId(1L);
        ProductElaborated productElaborated2 = new ProductElaborated();
        productElaborated2.setId(productElaborated1.getId());
        assertThat(productElaborated1).isEqualTo(productElaborated2);
        productElaborated2.setId(2L);
        assertThat(productElaborated1).isNotEqualTo(productElaborated2);
        productElaborated1.setId(null);
        assertThat(productElaborated1).isNotEqualTo(productElaborated2);
    }
}
