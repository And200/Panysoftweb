package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Provider.
 */
@Entity
@Table(name = "provider")
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @NotNull
    @Size(max = 50)
    @Column(name = "adress", length = 50, nullable = false)
    private String adress;

    @NotNull
    @Size(max = 50)
    @Column(name = "nit", length = 50, nullable = false, unique = true)
    private String nit;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(max = 30)
    @Column(name = "phone", length = 30, nullable = false, unique = true)
    private String phone;

    @OneToMany(mappedBy = "provider")
    @JsonIgnoreProperties(value = { "inventories", "recips", "provider", "presentation" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "provider")
    @JsonIgnoreProperties(value = { "provider", "orderPlaced" }, allowSetters = true)
    private Set<DetailOrder> detailOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Provider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public Provider email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return this.adress;
    }

    public Provider adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNit() {
        return this.nit;
    }

    public Provider nit(String nit) {
        this.setNit(nit);
        return this;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() {
        return this.name;
    }

    public Provider name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Provider phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProvider(null));
        }
        if (products != null) {
            products.forEach(i -> i.setProvider(this));
        }
        this.products = products;
    }

    public Provider products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Provider addProduct(Product product) {
        this.products.add(product);
        product.setProvider(this);
        return this;
    }

    public Provider removeProduct(Product product) {
        this.products.remove(product);
        product.setProvider(null);
        return this;
    }

    public Set<DetailOrder> getDetailOrders() {
        return this.detailOrders;
    }

    public void setDetailOrders(Set<DetailOrder> detailOrders) {
        if (this.detailOrders != null) {
            this.detailOrders.forEach(i -> i.setProvider(null));
        }
        if (detailOrders != null) {
            detailOrders.forEach(i -> i.setProvider(this));
        }
        this.detailOrders = detailOrders;
    }

    public Provider detailOrders(Set<DetailOrder> detailOrders) {
        this.setDetailOrders(detailOrders);
        return this;
    }

    public Provider addDetailOrder(DetailOrder detailOrder) {
        this.detailOrders.add(detailOrder);
        detailOrder.setProvider(this);
        return this;
    }

    public Provider removeDetailOrder(DetailOrder detailOrder) {
        this.detailOrders.remove(detailOrder);
        detailOrder.setProvider(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Provider)) {
            return false;
        }
        return id != null && id.equals(((Provider) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Provider{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", adress='" + getAdress() + "'" +
            ", nit='" + getNit() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
