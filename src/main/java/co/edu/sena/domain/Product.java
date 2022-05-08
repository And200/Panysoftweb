package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "product_detail", length = 50, nullable = false)
    private String productDetail;

    @NotNull
    @Size(max = 50)
    @Column(name = "product_name", length = 50, nullable = false, unique = true)
    private String productName;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Inventory> inventories = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product", "category" }, allowSetters = true)
    private Set<Recip> recips = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "detailOrders" }, allowSetters = true)
    private Provider provider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "detailSales", "products", "measureUnit" }, allowSetters = true)
    private Presentation presentation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductDetail() {
        return this.productDetail;
    }

    public Product productDetail(String productDetail) {
        this.setProductDetail(productDetail);
        return this;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Set<Inventory> getInventories() {
        return this.inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        if (this.inventories != null) {
            this.inventories.forEach(i -> i.setProduct(null));
        }
        if (inventories != null) {
            inventories.forEach(i -> i.setProduct(this));
        }
        this.inventories = inventories;
    }

    public Product inventories(Set<Inventory> inventories) {
        this.setInventories(inventories);
        return this;
    }

    public Product addInventory(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.setProduct(this);
        return this;
    }

    public Product removeInventory(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.setProduct(null);
        return this;
    }

    public Set<Recip> getRecips() {
        return this.recips;
    }

    public void setRecips(Set<Recip> recips) {
        if (this.recips != null) {
            this.recips.forEach(i -> i.setProduct(null));
        }
        if (recips != null) {
            recips.forEach(i -> i.setProduct(this));
        }
        this.recips = recips;
    }

    public Product recips(Set<Recip> recips) {
        this.setRecips(recips);
        return this;
    }

    public Product addRecip(Recip recip) {
        this.recips.add(recip);
        recip.setProduct(this);
        return this;
    }

    public Product removeRecip(Recip recip) {
        this.recips.remove(recip);
        recip.setProduct(null);
        return this;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Product provider(Provider provider) {
        this.setProvider(provider);
        return this;
    }

    public Presentation getPresentation() {
        return this.presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    public Product presentation(Presentation presentation) {
        this.setPresentation(presentation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productDetail='" + getProductDetail() + "'" +
            ", productName='" + getProductName() + "'" +
            "}";
    }
}
