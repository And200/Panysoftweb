package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseReceipt.class);
        PurchaseReceipt purchaseReceipt1 = new PurchaseReceipt();
        purchaseReceipt1.setId(1L);
        PurchaseReceipt purchaseReceipt2 = new PurchaseReceipt();
        purchaseReceipt2.setId(purchaseReceipt1.getId());
        assertThat(purchaseReceipt1).isEqualTo(purchaseReceipt2);
        purchaseReceipt2.setId(2L);
        assertThat(purchaseReceipt1).isNotEqualTo(purchaseReceipt2);
        purchaseReceipt1.setId(null);
        assertThat(purchaseReceipt1).isNotEqualTo(purchaseReceipt2);
    }
}
