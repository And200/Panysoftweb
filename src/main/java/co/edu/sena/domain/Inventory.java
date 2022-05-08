package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "stocks", nullable = false)
    private Integer stocks;

    @NotNull
    @Column(name = "buy_price", nullable = false)
    private Double buyPrice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "inventories", "recips", "provider", "presentation" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStocks() {
        return this.stocks;
    }

    public Inventory stocks(Integer stocks) {
        this.setStocks(stocks);
        return this;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Double getBuyPrice() {
        return this.buyPrice;
    }

    public Inventory buyPrice(Double buyPrice) {
        this.setBuyPrice(buyPrice);
        return this;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Inventory product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return id != null && id.equals(((Inventory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", stocks=" + getStocks() +
            ", buyPrice=" + getBuyPrice() +
            "}";
    }
}
