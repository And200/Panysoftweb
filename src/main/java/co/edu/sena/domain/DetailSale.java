package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DetailSale.
 */
@Entity
@Table(name = "detail_sale")
public class DetailSale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "product_amount", nullable = false)
    private Integer productAmount;

    @OneToMany(mappedBy = "detailSale")
    @JsonIgnoreProperties(value = { "employee", "detailSale", "client" }, allowSetters = true)
    private Set<PurchaseReceipt> purchaseReceipts = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "detailSales", "category" }, allowSetters = true)
    private ProductElaborated productElaborated;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "detailSales", "products", "measureUnit" }, allowSetters = true)
    private Presentation presentation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetailSale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductAmount() {
        return this.productAmount;
    }

    public DetailSale productAmount(Integer productAmount) {
        this.setProductAmount(productAmount);
        return this;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Set<PurchaseReceipt> getPurchaseReceipts() {
        return this.purchaseReceipts;
    }

    public void setPurchaseReceipts(Set<PurchaseReceipt> purchaseReceipts) {
        if (this.purchaseReceipts != null) {
            this.purchaseReceipts.forEach(i -> i.setDetailSale(null));
        }
        if (purchaseReceipts != null) {
            purchaseReceipts.forEach(i -> i.setDetailSale(this));
        }
        this.purchaseReceipts = purchaseReceipts;
    }

    public DetailSale purchaseReceipts(Set<PurchaseReceipt> purchaseReceipts) {
        this.setPurchaseReceipts(purchaseReceipts);
        return this;
    }

    public DetailSale addPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipts.add(purchaseReceipt);
        purchaseReceipt.setDetailSale(this);
        return this;
    }

    public DetailSale removePurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipts.remove(purchaseReceipt);
        purchaseReceipt.setDetailSale(null);
        return this;
    }

    public ProductElaborated getProductElaborated() {
        return this.productElaborated;
    }

    public void setProductElaborated(ProductElaborated productElaborated) {
        this.productElaborated = productElaborated;
    }

    public DetailSale productElaborated(ProductElaborated productElaborated) {
        this.setProductElaborated(productElaborated);
        return this;
    }

    public Presentation getPresentation() {
        return this.presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    public DetailSale presentation(Presentation presentation) {
        this.setPresentation(presentation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailSale)) {
            return false;
        }
        return id != null && id.equals(((DetailSale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailSale{" +
            "id=" + getId() +
            ", productAmount=" + getProductAmount() +
            "}";
    }
}
