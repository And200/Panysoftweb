package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ProductElaborated.
 */
@Entity
@Table(name = "product_elaborated")
public class ProductElaborated implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount_produced", nullable = false)
    private Integer amountProduced;

    @NotNull
    @Size(max = 30)
    @Column(name = "product_name", length = 30, nullable = false, unique = true)
    private String productName;

    @NotNull
    @Column(name = "buy_price", nullable = false)
    private Double buyPrice;

    @OneToMany(mappedBy = "productElaborated")
    @JsonIgnoreProperties(value = { "purchaseReceipts", "productElaborated", "presentation" }, allowSetters = true)
    private Set<DetailSale> detailSales = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productElaborateds", "recips" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductElaborated id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmountProduced() {
        return this.amountProduced;
    }

    public ProductElaborated amountProduced(Integer amountProduced) {
        this.setAmountProduced(amountProduced);
        return this;
    }

    public void setAmountProduced(Integer amountProduced) {
        this.amountProduced = amountProduced;
    }

    public String getProductName() {
        return this.productName;
    }

    public ProductElaborated productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBuyPrice() {
        return this.buyPrice;
    }

    public ProductElaborated buyPrice(Double buyPrice) {
        this.setBuyPrice(buyPrice);
        return this;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Set<DetailSale> getDetailSales() {
        return this.detailSales;
    }

    public void setDetailSales(Set<DetailSale> detailSales) {
        if (this.detailSales != null) {
            this.detailSales.forEach(i -> i.setProductElaborated(null));
        }
        if (detailSales != null) {
            detailSales.forEach(i -> i.setProductElaborated(this));
        }
        this.detailSales = detailSales;
    }

    public ProductElaborated detailSales(Set<DetailSale> detailSales) {
        this.setDetailSales(detailSales);
        return this;
    }

    public ProductElaborated addDetailSale(DetailSale detailSale) {
        this.detailSales.add(detailSale);
        detailSale.setProductElaborated(this);
        return this;
    }

    public ProductElaborated removeDetailSale(DetailSale detailSale) {
        this.detailSales.remove(detailSale);
        detailSale.setProductElaborated(null);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductElaborated category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductElaborated)) {
            return false;
        }
        return id != null && id.equals(((ProductElaborated) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductElaborated{" +
            "id=" + getId() +
            ", amountProduced=" + getAmountProduced() +
            ", productName='" + getProductName() + "'" +
            ", buyPrice=" + getBuyPrice() +
            "}";
    }
}
