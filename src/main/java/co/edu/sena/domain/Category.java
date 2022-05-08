package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "name_category", length = 30, nullable = false, unique = true)
    private String nameCategory;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "detailSales", "category" }, allowSetters = true)
    private Set<ProductElaborated> productElaborateds = new HashSet<>();

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "product", "category" }, allowSetters = true)
    private Set<Recip> recips = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCategory() {
        return this.nameCategory;
    }

    public Category nameCategory(String nameCategory) {
        this.setNameCategory(nameCategory);
        return this;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Set<ProductElaborated> getProductElaborateds() {
        return this.productElaborateds;
    }

    public void setProductElaborateds(Set<ProductElaborated> productElaborateds) {
        if (this.productElaborateds != null) {
            this.productElaborateds.forEach(i -> i.setCategory(null));
        }
        if (productElaborateds != null) {
            productElaborateds.forEach(i -> i.setCategory(this));
        }
        this.productElaborateds = productElaborateds;
    }

    public Category productElaborateds(Set<ProductElaborated> productElaborateds) {
        this.setProductElaborateds(productElaborateds);
        return this;
    }

    public Category addProductElaborated(ProductElaborated productElaborated) {
        this.productElaborateds.add(productElaborated);
        productElaborated.setCategory(this);
        return this;
    }

    public Category removeProductElaborated(ProductElaborated productElaborated) {
        this.productElaborateds.remove(productElaborated);
        productElaborated.setCategory(null);
        return this;
    }

    public Set<Recip> getRecips() {
        return this.recips;
    }

    public void setRecips(Set<Recip> recips) {
        if (this.recips != null) {
            this.recips.forEach(i -> i.setCategory(null));
        }
        if (recips != null) {
            recips.forEach(i -> i.setCategory(this));
        }
        this.recips = recips;
    }

    public Category recips(Set<Recip> recips) {
        this.setRecips(recips);
        return this;
    }

    public Category addRecip(Recip recip) {
        this.recips.add(recip);
        recip.setCategory(this);
        return this;
    }

    public Category removeRecip(Recip recip) {
        this.recips.remove(recip);
        recip.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", nameCategory='" + getNameCategory() + "'" +
            "}";
    }
}
