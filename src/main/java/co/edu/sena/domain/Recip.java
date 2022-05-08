package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Recip.
 */
@Entity
@Table(name = "recip")
public class Recip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "name_recip", length = 30, nullable = false, unique = true)
    private String nameRecip;

    @NotNull
    @Column(name = "estimated_price_preparation", nullable = false)
    private Double estimatedPricePreparation;

    @NotNull
    @Column(name = "amount_product_used", nullable = false)
    private Double amountProductUsed;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "inventories", "recips", "provider", "presentation" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productElaborateds", "recips" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRecip() {
        return this.nameRecip;
    }

    public Recip nameRecip(String nameRecip) {
        this.setNameRecip(nameRecip);
        return this;
    }

    public void setNameRecip(String nameRecip) {
        this.nameRecip = nameRecip;
    }

    public Double getEstimatedPricePreparation() {
        return this.estimatedPricePreparation;
    }

    public Recip estimatedPricePreparation(Double estimatedPricePreparation) {
        this.setEstimatedPricePreparation(estimatedPricePreparation);
        return this;
    }

    public void setEstimatedPricePreparation(Double estimatedPricePreparation) {
        this.estimatedPricePreparation = estimatedPricePreparation;
    }

    public Double getAmountProductUsed() {
        return this.amountProductUsed;
    }

    public Recip amountProductUsed(Double amountProductUsed) {
        this.setAmountProductUsed(amountProductUsed);
        return this;
    }

    public void setAmountProductUsed(Double amountProductUsed) {
        this.amountProductUsed = amountProductUsed;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Recip product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Recip category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recip)) {
            return false;
        }
        return id != null && id.equals(((Recip) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recip{" +
            "id=" + getId() +
            ", nameRecip='" + getNameRecip() + "'" +
            ", estimatedPricePreparation=" + getEstimatedPricePreparation() +
            ", amountProductUsed=" + getAmountProductUsed() +
            "}";
    }
}
