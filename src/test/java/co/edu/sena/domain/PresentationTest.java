package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresentationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presentation.class);
        Presentation presentation1 = new Presentation();
        presentation1.setId(1L);
        Presentation presentation2 = new Presentation();
        presentation2.setId(presentation1.getId());
        assertThat(presentation1).isEqualTo(presentation2);
        presentation2.setId(2L);
        assertThat(presentation1).isNotEqualTo(presentation2);
        presentation1.setId(null);
        assertThat(presentation1).isNotEqualTo(presentation2);
    }
}
