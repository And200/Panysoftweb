package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Presentation.
 */
@Entity
@Table(name = "presentation")
public class Presentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "presentation", length = 30, nullable = false, unique = true)
    private String presentation;

    @OneToMany(mappedBy = "presentation")
    @JsonIgnoreProperties(value = { "purchaseReceipts", "productElaborated", "presentation" }, allowSetters = true)
    private Set<DetailSale> detailSales = new HashSet<>();

    @OneToMany(mappedBy = "presentation")
    @JsonIgnoreProperties(value = { "inventories", "recips", "provider", "presentation" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "presentations" }, allowSetters = true)
    private MeasureUnit measureUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Presentation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentation() {
        return this.presentation;
    }

    public Presentation presentation(String presentation) {
        this.setPresentation(presentation);
        return this;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public Set<DetailSale> getDetailSales() {
        return this.detailSales;
    }

    public void setDetailSales(Set<DetailSale> detailSales) {
        if (this.detailSales != null) {
            this.detailSales.forEach(i -> i.setPresentation(null));
        }
        if (detailSales != null) {
            detailSales.forEach(i -> i.setPresentation(this));
        }
        this.detailSales = detailSales;
    }

    public Presentation detailSales(Set<DetailSale> detailSales) {
        this.setDetailSales(detailSales);
        return this;
    }

    public Presentation addDetailSale(DetailSale detailSale) {
        this.detailSales.add(detailSale);
        detailSale.setPresentation(this);
        return this;
    }

    public Presentation removeDetailSale(DetailSale detailSale) {
        this.detailSales.remove(detailSale);
        detailSale.setPresentation(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setPresentation(null));
        }
        if (products != null) {
            products.forEach(i -> i.setPresentation(this));
        }
        this.products = products;
    }

    public Presentation products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Presentation addProduct(Product product) {
        this.products.add(product);
        product.setPresentation(this);
        return this;
    }

    public Presentation removeProduct(Product product) {
        this.products.remove(product);
        product.setPresentation(null);
        return this;
    }

    public MeasureUnit getMeasureUnit() {
        return this.measureUnit;
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Presentation measureUnit(MeasureUnit measureUnit) {
        this.setMeasureUnit(measureUnit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presentation)) {
            return false;
        }
        return id != null && id.equals(((Presentation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presentation{" +
            "id=" + getId() +
            ", presentation='" + getPresentation() + "'" +
            "}";
    }
}
