package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recip.class);
        Recip recip1 = new Recip();
        recip1.setId(1L);
        Recip recip2 = new Recip();
        recip2.setId(recip1.getId());
        assertThat(recip1).isEqualTo(recip2);
        recip2.setId(2L);
        assertThat(recip1).isNotEqualTo(recip2);
        recip1.setId(null);
        assertThat(recip1).isNotEqualTo(recip2);
    }
}
